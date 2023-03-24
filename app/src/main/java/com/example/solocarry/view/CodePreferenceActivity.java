package com.example.solocarry.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.solocarry.R;
import com.example.solocarry.controller.CodeController;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.model.Code;
import com.example.solocarry.util.AuthUtil;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.style.MIUIStyle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

public class CodePreferenceActivity extends AppCompatActivity {

    private Button confirmButton;
    private TextView editTextCodeName, tvLat, tvLong;
    private EditText editTextCodeComment;
    private Switch codeImagePreference;
    private ImageView imageView, cancelButton;
    private Bitmap randomBitmap;
    private Bitmap customBitmap;
    private Switch showPublic;
    private boolean showPublicStatus = true;
    private double latitude, longitude;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_preference);

        DialogX.init(this);
        DialogX.globalStyle = MIUIStyle.style();
        DialogX.globalTheme = DialogX.THEME.DARK;

        Intent intent = getIntent();
        String SHA256 = intent.getStringExtra("hash");
        String uid = AuthUtil.currentUser.getUid();

        tvLat = findViewById(R.id.textView_latitude);
        tvLong = findViewById(R.id.textView_longitude);
        codeImagePreference = findViewById(R.id.switch_code_thmbnail);
        confirmButton = findViewById(R.id.btn_upload_code);
        cancelButton = findViewById(R.id.button_back);
        imageView = findViewById(R.id.imageView_code);
        editTextCodeComment = findViewById(R.id.editText_code_comment);
        editTextCodeName = findViewById(R.id.editText_code_name);
        showPublic = findViewById(R.id.switch_showPublic);

        editTextCodeName.setText(Code.hashCodeToName(SHA256));
        showPublic.setText("Show to Public");
        codeImagePreference.setClickable(true);
        codeImagePreference.setChecked(false);
        codeImagePreference.setText("View visualization");
        WaitDialog.show("Loading...");
        Picasso.get().load("https://robohash.org/" + SHA256).transform(new CircleTransform()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                WaitDialog.dismiss();
                TipDialog.show("Loading Success!", WaitDialog.TYPE.SUCCESS);
                randomBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            }

            @Override
            public void onError(Exception e) {
                WaitDialog.dismiss();
                TipDialog.show("Loading Error!", WaitDialog.TYPE.ERROR);
            }
        });

        //get location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        tvLat.setText("Latitude: " + latitude);
        tvLong.setText("Longitude: " + longitude);

        showPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showPublicStatus = false;
                    showPublic.setText("Keep as Private");
                    tvLat.setText("Latitude: Not shared");
                    tvLong.setText("Longitude: Not shared");
                    codeImagePreference.setChecked(false);
                    codeImagePreference.setClickable(false);
                } else {
                    showPublicStatus = true;
                    tvLat.setText("Latitude: " + latitude);
                    tvLong.setText("Longitude: " + longitude);
                    showPublic.setText("Show to Public");
                    codeImagePreference.setClickable(true);
                }
            }
        });

        codeImagePreference.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                compoundButton.setText("View visualization");
                WaitDialog.show("Loading...");
                Picasso.get().load("https://robohash.org/" + SHA256).transform(new CircleTransform()).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        WaitDialog.dismiss();
                        TipDialog.show("Loading Success!", WaitDialog.TYPE.SUCCESS);
                        randomBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    }

                    @Override
                    public void onError(Exception e) {
                        WaitDialog.dismiss();
                        TipDialog.show("Loading Error!", WaitDialog.TYPE.ERROR);
                    }
                });
            }else{
                compoundButton.setText("View upload image");
                if (customBitmap!=null){
                    imageView.setImageBitmap(customBitmap);
                }else{
                    imageView.setImageResource(R.mipmap.user_default);
                }
            }
        });

        imageView.setOnClickListener(view -> {
            if (codeImagePreference.isChecked()) {
                //if upload mode, take picture
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
                startActivityForResult(captureIntent, 100);
            }
        });

        confirmButton.setOnClickListener(view -> {
            WaitDialog.show("Uploading...");
            if (!showPublic.isChecked() && customBitmap == null) {
                WaitDialog.dismiss();
                TipDialog.show("No custom image uploaded!", WaitDialog.TYPE.WARNING);
            } else {
                Code code = new Code(SHA256, editTextCodeName.getText().toString().trim(), true);
                code.setComment(editTextCodeComment.getText().toString());
                code.updateScore(Code.hashCodeToScore(SHA256));
                if (showPublicStatus) {
                    //upload image and location
                    FirebaseStorage storage = DatabaseUtil.getFirebaseStorageInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://solocarry-8bf9e.appspot.com/");
                    String storagePath = uid + LocalDateTime.now().toString() + ".jpg";
                    StorageReference codeRef = storageReference.child(storagePath);

                    code.setLocation((float) latitude, (float) longitude);
                    code.setPhoto(storagePath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = codeImagePreference.isChecked() ? randomBitmap : customBitmap;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = codeRef.putBytes(data);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        //add to collection
                        CodeController.addCodeToCodeMap(code, uid, unused ->
                                CodeController.addCode(code, uid, unused1 ->
                                        CodeController.addCodeToUser(uid, code, unused11 ->
                                                UserController.addScoreToUser(uid, Code.hashCodeToScore(SHA256), unused111 -> {
                                                    WaitDialog.dismiss();
                                                    TipDialog.show("Code Added!", WaitDialog.TYPE.SUCCESS);
                                                    MessageDialog.build()
                                                            .setCancelButton("No, back to map")
                                                            .setCancelButton((dialog, v) -> {
                                                                dialog.dismiss();
                                                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(homeIntent);
                                                                return false;
                                                            })
                                                            .setOkButton("Yes, back to camera")
                                                            .setOkButton((dialog, v) -> {
                                                                dialog.dismiss();
                                                                finish();
                                                                return false;
                                                            })
                                                            .setTitle("More code?")
                                                            .setMessage("The code has been added to your collection, do you want to upload more code?")
                                                            .setCancelable(false)
                                                            .show();
                                                }, null)), null), null);
                    });
                } else {
                    code.setShowPublic(false);
                    //add to collection
                    CodeController.addCodeToCodeMap(code, uid, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            CodeController.addCode(code, uid, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused1) {
                                    CodeController.addCodeToUser(uid, code, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused2) {
                                            UserController.addScoreToUser(uid, Code.hashCodeToScore(SHA256), new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused3) {
                                                    WaitDialog.dismiss();
                                                    TipDialog.show("Code Added!", WaitDialog.TYPE.SUCCESS);
                                                    MessageDialog.build()
                                                            .setCancelButton("No, back to map")
                                                            .setCancelButton((dialog, v) -> {
                                                                dialog.dismiss();
                                                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(homeIntent);
                                                                return false;
                                                            })
                                                            .setOkButton("Yes, back to camera")
                                                            .setOkButton((dialog, v) -> {
                                                                dialog.dismiss();
                                                                finish();
                                                                return false;
                                                            })
                                                            .setTitle("More code?")
                                                            .setMessage("The code has been added to your collection, do you want to upload more code?")
                                                            .setCancelable(false)
                                                            .show();
                                                }
                                            }, null);
                                        }
                                    });
                                }
                            }, null);
                        }
                    }, null);
                }
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

    class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}