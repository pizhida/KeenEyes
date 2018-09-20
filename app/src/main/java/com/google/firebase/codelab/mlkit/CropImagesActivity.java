package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.codelab.mlkit.event.SendImageEvent;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CropImagesActivity extends AppCompatActivity
{
    private CropImageView cropImageView;
    private Bitmap mBitmap;

    private TextView cropButton;

    private ImageView howtoButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        ImageView mBack = findViewById(R.id.img_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cropImageView = findViewById(R.id.cropImageView);
        cropButton = findViewById(R.id.crop_btn);

        howtoButton = findViewById(R.id.imgView_howto);
        //byte[] byteArray = getIntent().getByteArrayExtra("image");
        //mBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            mBitmap = BitmapFactory.decodeStream(is);
            cropImageView.setImageBitmap(mBitmap);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        howtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CropImagesActivity.this, HowToActivity.class);
                CropImagesActivity.this.startActivity(myIntent);
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cropImageView.getCroppedImageAsync();
// or
                Bitmap cropped = cropImageView.getCroppedImage();
                startNewAct(cropped);
            }
        });

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

            }
        });
    }


    private void startNewAct(Bitmap bitmap)
    {
        try {
            //Write file
            String filename = "bitmap_finish.png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //Cleanup
            stream.close();
            bitmap.recycle();
            //Pop intent
            EventBus.getDefault().post(new SendImageEvent(filename));
            finish();
//                    Intent in1 = new Intent(this, CropImagesActivity.class);
//                    in1.putExtra("image", filename);
//                    startActivity(in1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
