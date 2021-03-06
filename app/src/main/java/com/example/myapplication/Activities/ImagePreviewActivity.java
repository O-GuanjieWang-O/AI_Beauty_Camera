package com.example.myapplication.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.ad.zoomimageview.ZoomImageView;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

public class ImagePreviewActivity extends AppCompatActivity {
    private ZoomImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        imageView = findViewById(R.id.preview_imageview);
        getImageUriFromIntent();
    }
    String data = "";
    private void getImageUriFromIntent() {
        try {
            Intent intent = getIntent();
            imageUri = Uri.fromFile(new File(Objects.requireNonNull(intent.getStringExtra("imageUrl"))));
            final InputStream stream = getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);

            data = intent.getStringExtra("opt");
            if (data.equals("captureBtn-opt-front")){
                bitmap = rotateImage(bitmap, -90);
            }

            if (data.equals("captureBtn-opt-back")){
                bitmap = rotateImage(bitmap, 90);
            }

            if (data.equals("pickImageBtn")){
                bitmap = rotateImage(bitmap, 0);
            }

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClose(View view) {
        File fDelete = new File(Objects.requireNonNull(imageUri.getPath()));
        if (fDelete.exists()) {
            if (fDelete.delete()) {
                finish();
            } else {
                finish();
            }
        }

    }

    public void onCorrect(View view) {
        Intent intent = new Intent(ImagePreviewActivity.this, DesignActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        intent.putExtra("opt",data);
        startActivity(intent);
        finish();
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????90??????????????????????????????????????????90???
     *
     * @param bitmap    ??????
     * @param degree ????????????????????????
     * @return ????????????????????????
     */
    public Bitmap rotateImage(Bitmap bitmap, float degree) {
        //create new matrix
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }
}