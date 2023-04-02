package com.example.solocarry.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.databinding.ActivityChannelBinding;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.CustomCodeShareAdapter;
import com.example.solocarry.util.LocationAttachmentFactory;
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel;
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel;
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Normal;
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.Mode.Thread;
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel.State.NavigateUp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.dialogs.PopMenu;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.models.Attachment;
import io.getstream.chat.android.client.models.Channel;
import io.getstream.chat.android.client.models.Message;
import io.getstream.chat.android.ui.message.input.MessageInputView;
import io.getstream.chat.android.ui.message.input.viewmodel.MessageInputViewModelBinding;
import io.getstream.chat.android.ui.message.list.adapter.viewholder.attachment.AttachmentFactoryManager;
import io.getstream.chat.android.ui.message.list.header.MessageListHeaderView;
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel;
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModelBinding;
import io.getstream.chat.android.ui.message.list.viewmodel.MessageListViewModelBinding;
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory;
/**
 * This Auth Util class is used to handle message stream.
 */
public class ChannelActivity extends AppCompatActivity {

    private final static String CID_KEY = "key:cid";

    public static Intent newIntent(Context context, Channel channel) {
        final Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(CID_KEY, channel.getCid());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Step 0 - inflate binding
        ActivityChannelBinding binding = ActivityChannelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String cid = getIntent().getStringExtra(CID_KEY);
        if (cid == null) {
            throw new IllegalStateException("Specifying a channel id is required when starting ChannelActivity");
        }

        // Step 1 - Create three separate ViewModels for the views so it's easy
        //          to customize them individually
        ViewModelProvider.Factory factory = new MessageListViewModelFactory.Builder()
                .cid(cid)
                .build();
        ViewModelProvider provider = new ViewModelProvider(this, factory);
        MessageListHeaderViewModel messageListHeaderViewModel = provider.get(MessageListHeaderViewModel.class);
        MessageListViewModel messageListViewModel = provider.get(MessageListViewModel.class);
        MessageInputViewModel messageInputViewModel = provider.get(MessageInputViewModel.class);

        LocationAttachmentFactory locationAttachmentFactory = new LocationAttachmentFactory();
        List<LocationAttachmentFactory> locationAttachmentFactories = new ArrayList<>();
        locationAttachmentFactories.add(locationAttachmentFactory);
        AttachmentFactoryManager attachmentFactoryManager = new AttachmentFactoryManager(locationAttachmentFactories);
        binding.messageListView.setAttachmentFactoryManager(attachmentFactoryManager);

        // Step 2 - Bind the view and ViewModels, they are loosely coupled so it's easy to customize
        MessageListHeaderViewModelBinding.bind(messageListHeaderViewModel, binding.messageListHeaderView, this);
        MessageListViewModelBinding.bind(messageListViewModel, binding.messageListView, this, true);
        MessageInputViewModelBinding.bind(messageInputViewModel, binding.messageInputView, this);

        // Step 3 - Let both MessageListHeaderView and MessageInputView know when we open a thread
        messageListViewModel.getMode().observe(this, mode -> {
            if (mode instanceof Thread) {
                Message parentMessage = ((Thread) mode).getParentMessage();
                messageListHeaderViewModel.setActiveThread(parentMessage);
                messageInputViewModel.setActiveThread(parentMessage);
            } else if (mode instanceof Normal) {
                messageListHeaderViewModel.resetThread();
                messageInputViewModel.resetThread();
            }
        });

        // Step 4 - Let the message input know when we are editing a message
        binding.messageListView.setMessageEditHandler(messageInputViewModel::postMessageToEdit);

        // Step 5 - Handle navigate up state
        messageListViewModel.getState().observe(this, state -> {
            if (state instanceof NavigateUp) {
                finish();
            }
        });

        // Step 6 - Handle back button behaviour correctly when you're in a thread
        MessageListHeaderView.OnClickListener backHandler = () -> {
            messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed.INSTANCE);
        };
        binding.messageListHeaderView.setBackButtonClickListener(backHandler);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backHandler.onClick();
            }
        });

        AuthUtil authUtil = new AuthUtil();

        binding.messageInputView.setAttachmentButtonClickListener(new MessageInputView.AttachmentButtonClickListener() {
            @Override
            public void onAttachmentButtonClicked() {
                WaitDialog.show("Loading code collection...");
                ArrayList<Code> publicCodes = new ArrayList<>();
                CodeController.getUserPublicCode(AuthUtil.getFirebaseAuth().getUid(), new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document: queryDocumentSnapshots) {
                            publicCodes.add(document.toObject(Code.class));
                        }
                        WaitDialog.dismiss();
                        PopMenu.build()
                                .setCustomView(new OnBindView<PopMenu>(R.layout.layout_custom_view) {
                                    @Override
                                    public void onBind(PopMenu dialog, View v) {
                                        ListView lv = v.findViewById(R.id.code_view);
                                        CustomCodeShareAdapter customCodeShareAdapter = new CustomCodeShareAdapter(ChannelActivity.this, publicCodes);
                                        lv.setAdapter(customCodeShareAdapter);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Attachment attachment = new Attachment();
                                                attachment.setType("location");
                                                Map<String, Object> extraData = new HashMap<>();
                                                extraData.put("lat", publicCodes.get(i).getLatitude());
                                                extraData.put("lon", publicCodes.get(i).getLongitude());
                                                attachment.setExtraData(extraData);

                                                Message message = new Message();
                                                message.setCid(cid);
                                                message.setText(authUtil.getCurrentUser().getDisplayName()+" has shared a code");
                                                message.setAttachments(Collections.singletonList(attachment));
                                                ChatClient.instance().sendMessage("messaging",cid.split(":")[1], message).enqueue(result -> {
                                                            if (result.isSuccess()) {
                                                                // Use result.data()
                                                                dialog.dismiss();
                                                            } else {
                                                                // Handle result.error()
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                );
                                            }
                                        });
                                    }
                                })
                                .show();
                    }
                });
            }
        });
    }

}
