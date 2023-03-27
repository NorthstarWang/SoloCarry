package com.example.solocarry.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.databinding.MyCodeListBinding;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.CustomCodeListAdapter;
import com.example.solocarry.util.CustomMyCodeListAdapter;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.PopMenu;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.style.MIUIStyle;

import java.util.ArrayList;

public class CodeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = MyCodeListBinding.inflate(getLayoutInflater()).getRoot();
        setContentView(view);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        ListView codeList = findViewById(R.id.code_list);
        Button backButton = findViewById(R.id.back_button);
        Button sortButton = findViewById(R.id.sort_button);

        ArrayList<Code> codes = new ArrayList<>();
        CustomMyCodeListAdapter customCodeListAdapter = new CustomMyCodeListAdapter(CodeListActivity.this, codes);
        codeList.setAdapter(customCodeListAdapter);

        codeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopMenu.show(view, new String[]{"Delete"})
                        .setOverlayBaseView(true)
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PopMenu>() {
                            @Override
                            public boolean onClick(PopMenu dialog, CharSequence text, int index) {
                                switch (index){
                                    case 0:
                                        WaitDialog.show("Deleting...");
                                        //delete code in user collection
                                        db.collection("users").document(AuthUtil.getFirebaseAuth().getUid()).collection("codes").document(customCodeListAdapter.getItem(i).getHashCode())
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //deduct score from user
                                                        db.collection("users").document(AuthUtil.getFirebaseAuth().getUid()).update("score", FieldValue.increment(-customCodeListAdapter.getItem(i).getScore()))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        //remove user from codeInMap
                                                                        db.collection("codeMap").document(customCodeListAdapter.getItem(i).getHashCode()).update("ownerIds", FieldValue.arrayRemove(AuthUtil.getFirebaseAuth().getUid()))
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        //remove from current item
                                                                                        codes.remove(customCodeListAdapter.getItem(i));
                                                                                        customCodeListAdapter.notifyDataSetChanged();
                                                                                        WaitDialog.dismiss();
                                                                                        TipDialog.show("Code deleted", WaitDialog.TYPE.SUCCESS);
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                });
                                }
                                return false;
                            }
                        });
                return true;
            }
        });

        codeList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCodeListAdapter.sort();
            }
        });

        WaitDialog.show("Loading...");

        db.collection("users").document(AuthUtil.getFirebaseAuth().getUid()).collection("codes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    codes.add(documentSnapshot.toObject(Code.class));
                }
                customCodeListAdapter.notifyDataSetChanged();
                WaitDialog.dismiss();
                if(codes.isEmpty()){
                    PopTip.show("You don't have any code recorded yet, gotta catch'Em all!");
                }
            }
        });
    }
}