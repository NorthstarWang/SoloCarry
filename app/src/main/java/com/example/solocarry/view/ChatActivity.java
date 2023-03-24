package com.example.solocarry.view;

import static java.util.Collections.singletonList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.solocarry.R;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.databinding.ActivityChatBinding;
import com.example.solocarry.util.AuthUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener;
import com.kongzue.dialogx.style.MIUIStyle;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.api.models.FilterObject;
import io.getstream.chat.android.client.channel.ChannelClient;
import io.getstream.chat.android.client.logger.ChatLogLevel;
import io.getstream.chat.android.client.models.Channel;
import io.getstream.chat.android.client.models.Filters;
import io.getstream.chat.android.client.models.User;
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType;
import io.getstream.chat.android.offline.plugin.configuration.Config;
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory;
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel;
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModelBinding;
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory;

public final class ChatActivity extends AppCompatActivity {

    String apiKey = "qvsv49je5gz4";
    String api_token = "3hwc24bnce3xw6v926fa7s2e2cp62v8r5xkxmwdrm49nwn3n66nvv33pqwq8a5hp";
    private ArrayList<com.example.solocarry.model.User> friends;
    private int selectMenuIndex;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        WaitDialog.show("Loading...");

        // Step 0 - inflate binding
        ActivityChatBinding binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Step 1 - Set up the OfflinePlugin for offline storage
        StreamOfflinePluginFactory streamOfflinePluginFactory = new StreamOfflinePluginFactory(
                new Config(
                        true,
                        true,
                        true,
                        UploadAttachmentsNetworkType.NOT_ROAMING
                ),
                getApplicationContext()
        );

        // Step 2 - Set up the client for API calls with the plugin for offline storage
        ChatClient client = new ChatClient.Builder(apiKey, getApplicationContext())
                .withPlugin(streamOfflinePluginFactory)
                .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
                .build();

        // Step 3 - Authenticate and connect the user
        AuthUtil authUtil = new AuthUtil();
        User user = new User();
        user.setId(authUtil.getCurrentUser().getUid());
        user.setName(authUtil.getCurrentUser().getDisplayName());
        user.setImage(String.valueOf(authUtil.getCurrentUser().getPhotoUrl()));

        // Set up the payload for the JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", authUtil.getCurrentUser().getUid()); // Replace with the user ID you want to authenticate
        claims.put("exp", new Date().getTime() / 1000 + 3600); // Set the token expiration time

        // Generate the JWT token using the API secret
        Algorithm algorithm = Algorithm.HMAC256(api_token);
        String token = JWT.create()
                .withIssuer(apiKey)
                .withHeader(null)
                .withPayload(claims)
                .sign(algorithm);

        client.connectUser(user, token).enqueue((result) -> {
            if (result.isSuccess()) {
                // Handle success
                WaitDialog.dismiss();
            } else {
                // Handler error
                Log.d("Error", result.error().getMessage());

            }
        });

        // Step 4 - Set the channel list filter and order
        // This can be read as requiring only channels whose "type" is "messaging" AND
        // whose "members" include our "user.id"
        FilterObject filter = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.in("members", singletonList(user.getId()))
        );

        ViewModelProvider.Factory factory = new ChannelListViewModelFactory.Builder()
                .filter(filter)
                .sort(ChannelListViewModel.DEFAULT_SORT)
                .build();

        ChannelListViewModel channelsViewModel =
                new ViewModelProvider(this, factory).get(ChannelListViewModel.class);

        // Step 5 - Connect the ChannelListViewModel to the ChannelListView, loose
        //          coupling makes it easy to customize
        ChannelListViewModelBinding.bind(channelsViewModel, binding.channelListView, this);
        binding.channelListView.setChannelItemClickListener(
                channel -> startActivity(ChannelActivity.newIntent(ChatActivity.this, channel))
        );

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        friends = new ArrayList<>();

        binding.buttonAddChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaitDialog.show("Loading your friends...");
                UserController.loadFriend(AuthUtil.getFirebaseAuth().getUid(), new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        friends = new ArrayList<>();
                        com.example.solocarry.model.User userModal = UserController.transformUser(documentSnapshot);
                        for (String userId: userModal.getFriends()) {
                            UserController.getUser(userId, new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    friends.add(UserController.transformUser(documentSnapshot));
                                    if(friends.size()==userModal.getFriends().size()){
                                        WaitDialog.dismiss();
                                        BottomMenu.show(friends.stream().map(com.example.solocarry.model.User::getName).collect(Collectors.toList()))
                                                .setTitle("Chat with friends")
                                                .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                    @Override
                                                    public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                        WaitDialog.show("Creating chat...");
                                                        selectMenuIndex = index;
                                                        //create channel
                                                        ChannelClient channelClient = client.channel("messaging","");
                                                        List<String> memberIds = new LinkedList<>();
                                                        Map<String, Object> extraData = new HashMap<>();
                                                        memberIds.add(user.getId());
                                                        memberIds.add(friends.get(selectMenuIndex).getUid());
                                                        channelClient.create(memberIds, extraData).enqueue((result) -> {
                                                            if (result.isSuccess()) {
                                                                WaitDialog.dismiss();
                                                                // Use channel by calling methods on channelClient
                                                            } else {
                                                                // Handle result.error()
                                                            }
                                                        });
                                                        return false;
                                                    }
                                                })
                                                .setSingleSelection()
                                                .setCancelButton("Back")
                                                .setCancelable(false);
                                    }
                                }
                            }, null);
                        }
                    }
                });
            }
        });

    }
}