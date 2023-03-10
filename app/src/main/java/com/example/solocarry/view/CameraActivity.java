package com.example.solocarry.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.solocarry.R;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.google.zxing.Result;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

public class CameraActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        DialogX.init(this);

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            MessageDialog.build()
                    .setCancelButton("No")
                    .setCancelButton((OnDialogButtonClickListener<MessageDialog>) (dialog, v) -> {
                        mCodeScanner.startPreview();
                        dialog.dismiss();
                        return false;
                    })
                    .setOkButton("Yes")
                    .setOkButton((OnDialogButtonClickListener<MessageDialog>) (dialog, v) -> {
                        dialog.dismiss();
                        FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_full_screen_dialog) {
                            @Override
                            public void onBind(FullScreenDialog fDialog, View v) {
                                Button childView = v.findViewById(R.id.button2);
                                childView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mCodeScanner.startPreview();
                                        fDialog.dismiss();
                                    }
                                });
                            }
                        });
                        return false;
                    })
                    .setTitle("You found a code!")
                    .setMessage("This code worth "+ Code.hashCodeToScore(Code.stringToSHA256(result.toString())) +" points, do you want to add this code to your collection?")
                    .show();
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
}