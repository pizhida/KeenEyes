package com.google.firebase.codelab.mlkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.codelab.mlkit.event.ChoosePrefixEvent;
import com.google.firebase.codelab.mlkit.event.SelectImageInGalleryEvent;
import com.google.firebase.codelab.mlkit.interfaces.ImageUtilsCallback;
import com.google.firebase.codelab.mlkit.utils.ImageUtils;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.SizeSelector;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity
{
    private CameraView cameraView;
//    private CameraView cameraView1;
//    private CameraView cameraView2;


    private TextView mTextVos;
    private TextView mTextP;

    private ImageView captureButton;

    private Button nothingButton;
    private Button vosButton;
    private Button pButton;

    private ImageView pickImage;

    private int pos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        pos = 0;

        cameraView = findViewById(R.id.camera);
        pickImage = findViewById(R.id.img_cover_live);
        mTextVos = findViewById(R.id.text_vos);
        mTextP = findViewById(R.id.text_p);

        captureButton = findViewById(R.id.capture_btn);

        nothingButton = findViewById(R.id.btn_nothing);
        vosButton = findViewById(R.id.btn_vos);
        pButton = findViewById(R.id.btn_p);

        nothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.GONE);
                mTextVos.setVisibility(View.GONE);
                pos = 0;
            }
        });

        vosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.GONE);
                mTextVos.setVisibility(View.VISIBLE);
                pos = 1;
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                mTextVos.setVisibility(View.GONE);
                pos = 2;
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SelectImageInGalleryEvent());
                finish();
            }
        });



        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                // Create a bitmap or a file...
                // CameraUtils will read EXIF orientation for you, in a worker thread.
                CameraUtils.decodeBitmap(picture, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        startNewAct(bitmap);
                    }
                });

            }
        });
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER);
//        cameraView.setJpegQuality(100);
        cameraView.setCropOutput(true );
        cameraView.start();
//        cameraView.setPictureSize(new SizeSelector());


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.capturePicture();
            }
        });


    }


    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
//        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
//                + "/Android/data/"
//                + getApplicationContext().getPackageName()
//                + "/Files");


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
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
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
            Date d = new Date();
            CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
            String filename = "bitmap_" + s + ".png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            bitmap.recycle();

            EventBus.getDefault().post(new ChoosePrefixEvent(pos));

            //Pop intent
            Intent in1 = new Intent(this, CropImagesActivity.class);
            in1.putExtra("image", filename);
            startActivity(in1);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

}
