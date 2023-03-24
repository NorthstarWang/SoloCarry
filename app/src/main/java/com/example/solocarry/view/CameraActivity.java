package com.example.solocarry.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.MIUIStyle;

public class CameraActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private Bitmap captureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        FloatingActionButton buttonBack = findViewById(R.id.button_back_camera);
        buttonBack.setOnClickListener(view -> finish());

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            showDialog(result.toString());
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void showDialog(String result){
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        String uid = AuthUtil.currentUser.getUid();

        MessageDialog.build()
                .setCancelButton("No")
                .setCancelButton((dialog, v) -> {
                    mCodeScanner.startPreview();
                    dialog.dismiss();
                    return false;
                })
                .setOkButton("Yes")
                .setOkButton((dialog, v) -> {
                    dialog.dismiss();
                    WaitDialog.show("Checking...");
                    // check there exist such code in user code collection
                    OnSuccessListener<DocumentSnapshot> successListener = new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()){
                                WaitDialog.dismiss();
                                Intent intent = new Intent(CameraActivity.this, CodePreferenceActivity.class);
                                intent.putExtra("hash",Code.stringToSHA256(result));
                                startActivity(intent);
                            }else{
                                WaitDialog.dismiss();
                                TipDialog.show("Code exists in collection!", WaitDialog.TYPE.WARNING);
                                mCodeScanner.startPreview();
                            }
                        }
                    };
                    CodeController.checkUserHaveSuchCode(uid,Code.stringToSHA256(result), successListener);
                    return false;
                })
                .setTitle("You found a code!")
                .setMessage("This code worth "+ Code.hashCodeToScore(Code.stringToSHA256(result)) +" points, do you want to add this code to your collection?")
                .setCancelable(false)
                .show();
    }
}