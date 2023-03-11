package com.example.solocarry.view;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.example.solocarry.util.LocationUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;

public class CodePreferenceActivity extends AppCompatActivity{

    private Button cancelButton;
    private Button confirmButton;
    private EditText editTextCodeName;
    private EditText editTextCodeComment;
    private Switch codeImagePreference;
    private ImageView imageView;
    private Bitmap randomBitmap;
    private Bitmap customBitmap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_preference);

        Intent intent = getIntent();
        String SHA256 = intent.getStringExtra("hash");
        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();
        String uid = AuthUtil.currentUser.getUid();

        codeImagePreference = findViewById(R.id.switch_code_thmbnail);
        confirmButton = findViewById(R.id.btn_upload_code);
        cancelButton = findViewById(R.id.btn_cancel_code);
        imageView = findViewById(R.id.imageView_code);
        editTextCodeComment = findViewById(R.id.editText_code_comment);
        editTextCodeName = findViewById(R.id.editText_code_name);

        codeImagePreference.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                compoundButton.setText("Random");
                WaitDialog.show("Loading...");
                Picasso.get().load("https://picsum.photos/200").into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        WaitDialog.dismiss();
                        TipDialog.show("Loading Success!", WaitDialog.TYPE.SUCCESS);
                        randomBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    }

                    @Override
                    public void onError(Exception e) {
                        WaitDialog.dismiss();
                        TipDialog.show("Loading Error!", WaitDialog.TYPE.ERROR);
                    }
                });
            }else{
                compoundButton.setText("Custom");
                if (customBitmap!=null){
                    imageView.setImageBitmap(customBitmap);
                }else{
                    imageView.setImageResource(R.mipmap.user_default);
                }
            }
        });

        imageView.setOnClickListener(view -> {
            if(!codeImagePreference.isChecked()){
                //if custom, take picture
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
                startActivityForResult(captureIntent, 100);
            }else{
                Picasso.get().load("https://picsum.photos/200").into(imageView);
            }
        });

        confirmButton.setOnClickListener(view -> {
            WaitDialog.show("Uploading...");
            if(editTextCodeName.getText().toString().trim().length()<2){
                WaitDialog.dismiss();
                TipDialog.show("Input at least 2 characters for code name!", WaitDialog.TYPE.WARNING);
            }else if(codeImagePreference.isChecked()&&customBitmap==null){
                WaitDialog.dismiss();
                TipDialog.show("No custom image uploaded!", WaitDialog.TYPE.WARNING);
            }else if(!codeImagePreference.isChecked()&&randomBitmap==null){
                WaitDialog.dismiss();
                TipDialog.show("No random image loaded!", WaitDialog.TYPE.WARNING);
            }else{
                // check there exist such code in user code collection
                db.collection("users").document(uid)
                        .collection("codes").document(SHA256)
                        .get().addOnSuccessListener(documentSnapshot -> {
                            if(!documentSnapshot.exists()){
                                //get location
                                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();

                                FirebaseStorage storage = DatabaseUtil.getFirebaseStorageInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl("gs://solocarry-8bf9e.appspot.com/");
                                String storagePath = uid+ LocalDateTime.now().toString()+".jpg";
                                StorageReference codeRef = storageReference.child(storagePath);

                                Code code = new Code(SHA256, editTextCodeName.getText().toString().trim(), true);
                                code.setLocation((float) latitude, (float) longitude);
                                code.setComment(editTextCodeComment.getText().toString());
                                code.updateScore(Code.hashCodeToScore(SHA256));
                                code.setPhoto(storagePath);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                Bitmap bitmap = codeImagePreference.isChecked()?randomBitmap:customBitmap;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = codeRef.putBytes(data);
                                uploadTask.addOnSuccessListener(taskSnapshot -> db.collection("users").document(uid)
                                        .collection("codes").document(SHA256)
                                        .set(code)
                                        .addOnSuccessListener(unused -> {
                                            WaitDialog.dismiss();
                                            TipDialog.show("Code Added!", WaitDialog.TYPE.SUCCESS);
                                        })
                                );
                            }else{
                                WaitDialog.dismiss();
                                TipDialog.show("Code exists in collection!", WaitDialog.TYPE.WARNING);
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(view -> finish());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Rect rect = new Rect(-50, -50, 150, 150);
            assert(rect.left < rect.right && rect.top < rect.bottom);
            customBitmap = (Bitmap)data.getExtras().get("data");
            Bitmap resultBmp = Bitmap.createBitmap(rect.right-rect.left, rect.bottom-rect.top, Bitmap.Config.ARGB_8888);
            new Canvas(resultBmp).drawBitmap(customBitmap, -rect.left, -rect.top, null);
            imageView.setImageBitmap(customBitmap);
        }
    }
}