package com.example.solocarry.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.solocarry.R;
import com.example.solocarry.controller.UserController;
import com.example.solocarry.databinding.PinCodeInfoBinding;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.CodeInMap;
import com.example.solocarry.model.User;
import com.example.solocarry.util.CustomInfoWindowAdapter;
import com.example.solocarry.util.CustomOwnerListAdapter;
import com.example.solocarry.util.DatabaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class CodeDetailActivity extends AppCompatActivity {

    private boolean codeImageRobo;
    private String previousImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PinCodeInfoBinding binding = PinCodeInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore db = DatabaseUtil.getFirebaseFirestoreInstance();

        ArrayList<User> users = new ArrayList<>();
        CustomOwnerListAdapter customOwnerListAdapter = new CustomOwnerListAdapter(CodeDetailActivity.this, users);
        binding.pinUserList.setAdapter(customOwnerListAdapter);

        Intent intent = getIntent();
        String hashcode = intent.getStringExtra("hashcode");
        String id = intent.getStringExtra("id");

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WaitDialog.show("Loading...");
        db.collection("users").document(id).collection("codes").document(hashcode)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Code code = documentSnapshot.toObject(Code.class);
                        binding.codeName.setText(code.getName());
                        binding.codeScore.setText(String.valueOf(code.getScore()));
                        binding.codeLocation.setText(code.isShowPublic() ? "(" + code.getLatitude() + "," + code.getLongitude() + ")" : "Not recorded");
                        binding.codeComment.setText(code.getComment().length() > 0 ? code.getComment() : "No comment for this code");

                        //load code image
                        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.drawable.ic_code)
                                .error(R.drawable.ic_code)
                                .fallback(R.drawable.ic_code)
                                .override(100, 100);

                        Glide.with(CodeDetailActivity.this)
                                .load("https://robohash.org/" + code.getHashCode())
                                .override(100, 100)
                                .centerCrop()
                                .apply(requestOptions)
                                .transform(new CircleCrop())
                                .transition(DrawableTransitionOptions.withCrossFade(factory))
                                .into(binding.codeImage);

                        if (code.isShowPublic()) {
                            //if show public, it can switch between robo image and capture image
                            codeImageRobo = true;
                            binding.codeImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (codeImageRobo) {
                                        FirebaseStorage storage = DatabaseUtil.getFirebaseStorageInstance();
                                        StorageReference storageReference = storage.getReferenceFromUrl("gs://solocarry-8bf9e.appspot.com/");
                                        String storagePath = code.getPhoto();
                                        StorageReference codeRef = storageReference.child(storagePath);
                                        if(previousImageUrl==null){
                                            WaitDialog.show("Loading image...");
                                            codeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Picasso.get().load(uri)
                                                            .resize(100,100)
                                                            .centerCrop().noFade()
                                                            .transform(new CircleTransform())
                                                            .into(binding.codeImage, new Callback() {
                                                                @Override
                                                                public void onSuccess() {
                                                                    WaitDialog.dismiss();
                                                                    codeImageRobo = !codeImageRobo;
                                                                }

                                                                @Override
                                                                public void onError(Exception e) {
                                                                    codeImageRobo = !codeImageRobo;
                                                                    WaitDialog.dismiss();
                                                                    TipDialog.show("Error Occurred!", WaitDialog.TYPE.ERROR);
                                                                }
                                                            });
                                                }
                                            });
                                        }else{
                                            Picasso.get().load(previousImageUrl)
                                                    .resize(100,100)
                                                    .centerCrop().noFade()
                                                    .transform(new CircleTransform())
                                                    .into(binding.codeImage, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            WaitDialog.dismiss();
                                                            codeImageRobo = !codeImageRobo;
                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            codeImageRobo = !codeImageRobo;
                                                            WaitDialog.dismiss();
                                                            TipDialog.show("Error Occurred!", WaitDialog.TYPE.ERROR);
                                                        }
                                                    });
                                        }


                                    } else {
                                        Glide.with(CodeDetailActivity.this)
                                                .load("https://robohash.org/" + code.getHashCode())
                                                .override(100, 100)
                                                .centerCrop()
                                                .apply(requestOptions)
                                                .transform(new CircleCrop())
                                                .transition(DrawableTransitionOptions.withCrossFade(factory))
                                                .into(binding.codeImage);
                                        codeImageRobo = !codeImageRobo;
                                    }

                                }
                            });
                        }
                        db.collection("codeMap").document(hashcode).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                WaitDialog.dismiss();
                                CodeInMap codeInMap = documentSnapshot.toObject(CodeInMap.class);
                                if (codeInMap.getOwnerIds().size() > 0) {
                                    for (String uid : codeInMap.getOwnerIds()) {
                                        UserController.getUser(uid, new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                users.add(UserController.transformUser(documentSnapshot));
                                                customOwnerListAdapter.notifyDataSetChanged();
                                            }
                                        }, null);
                                    }
                                }
                            }
                        });
                    }
                });


        binding.pinUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(CodeDetailActivity.this, ProfileActivity.class);
                intent1.putExtra("id", customOwnerListAdapter.getItem(i).getUid());
                startActivity(intent1);
            }
        });
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