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

import com.google.firebase.codelab.mlkit.event.SendImageEvent;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CropImagesActivity extends AppCompatActivity
{
    private CropImageView cropImageView;
    private Bitmap mBitmap;

    private Button cropButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropImageView = findViewById(R.id.cropImageView);
        cropButton = findViewById(R.id.crop_btn);
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
