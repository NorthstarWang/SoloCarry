package com.example.solocarry.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solocarry.R;
import com.example.solocarry.model.Code;
import com.example.solocarry.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
/**
 * This CustomCodeShareAdapter class is an adapter for code sharing
 */
public class CustomCodeShareAdapter extends BaseAdapter{

    private ArrayList<Code> dataSet;
    private Context mContext;
    private ImageView imageView;
    private TextView textView;

    /**
     * This constructor for CustomCodeShareAdapter class
     * @param context the app context
     * @param dataSet the array list of codes
     */
    public CustomCodeShareAdapter(Context context, ArrayList<Code> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }

    /**
     * This getCount method returns the number of codes included
     * @return int
     */
    @Override
    public int getCount() {
        return dataSet.size();
    }

    /**
     * This getItem method returns the code object at given position
     * @return int
     */
    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    /**
     * This getItemId method returns a specific position
     * @return long
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * This getView method constructs the view for this adapter
     * @return int
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.custom_location_attachment, viewGroup, false);
        imageView = view.findViewById(R.id.code_imageView);
        textView = view.findViewById(R.id.code_name_text);

        Picasso.get().load("https://robohash.org/" + dataSet.get(i).getHashCode()).transform(new CircleTransform()).into(imageView);
        textView.setText(dataSet.get(i).getName());
        return view;
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
