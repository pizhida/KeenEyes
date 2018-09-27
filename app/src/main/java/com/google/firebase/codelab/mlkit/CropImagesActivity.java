package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.codelab.mlkit.event.SendImageEvent;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    private File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/SampleML");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Uri contentUri = Uri.fromFile(mediaFile);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        return mediaFile;
    }


    private void startNewAct(Bitmap bitmap)
    {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("ff",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("ff", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("ff", "Error accessing file: " + e.getMessage());
        }


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
