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


    private TextView mTextP;

    private ImageView captureButton;

    private Button nothingButton;
    private Button vosButton;
    private Button pButton;

    private Button nButton;
    private Button jpButton;
    private Button jnButton;

    private Button oneaButton;
    private Button onebButton;
    private Button onecButton;

    private Button twoaButton;
    private Button twobButton;
    private Button twocButton;

    private Button fButton;
    private Button kButton;
    private Button gButton;

    private ImageView pickImage;

    private ImageView howtoButton;

    private int pos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        pos = 0;

        cameraView = findViewById(R.id.camera);
        pickImage = findViewById(R.id.img_cover_live);
        mTextP = findViewById(R.id.text_p);

        howtoButton = findViewById(R.id.imgView_howto);

        captureButton = findViewById(R.id.capture_btn);

        nothingButton = findViewById(R.id.btn_nothing);
        vosButton = findViewById(R.id.btn_vos);
        pButton = findViewById(R.id.btn_p);

        nButton = findViewById(R.id.btn_n);
        jpButton = findViewById(R.id.btn_jp);
        jnButton = findViewById(R.id.btn_jn);

        oneaButton = findViewById(R.id.btn_1a);
        onebButton = findViewById(R.id.btn_1b);
        onecButton = findViewById(R.id.btn_1c);

        twoaButton = findViewById(R.id.btn_2a);
        twobButton = findViewById(R.id.btn_2b);
        twocButton = findViewById(R.id.btn_2c);

        fButton = findViewById(R.id.btn_f);
        kButton = findViewById(R.id.btn_k);
        gButton = findViewById(R.id.btn_g);

        ImageView mBack = findViewById(R.id.img_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        howtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CameraActivity.this, HowToActivity.class);
                CameraActivity.this.startActivity(myIntent);
            }
        });



        nothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.GONE);
                cleabBackground();
                nothingButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                pos = 0;
            }
        });

        vosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                vosButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("วอส.");
                pos = 1;
            }
        });

        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                pButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("ผ.");
                pos = 2;
            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                nButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("น.");
                pos = 3;
            }
        });

        jpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                jpButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("จผ.");
                pos = 4;
            }
        });

        jnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                jnButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("จน.");
                pos = 5;
            }
        });

        oneaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                oneaButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("1A");
                pos = 6;
            }
        });

        onebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                onebButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("1B");
                pos = 7;
            }
        });

        onecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                onecButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("1C");
                pos = 8;
            }
        });

        twoaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                twoaButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("2A");
                pos = 9;
            }
        });

        twobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                twobButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("2B");
                pos = 10;
            }
        });

        twocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                twocButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("2C");
                pos = 11;
            }
        });

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                fButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("F");
                pos = 12;
            }
        });

        kButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                kButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("K");
                pos = 13;
            }
        });

        gButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mTextP.setVisibility(View.VISIBLE);
                cleabBackground();
                gButton.setBackgroundResource(R.drawable.btn_round_red_bg_cam);
                mTextP.setText("G");
                pos = 14;
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

    private void cleabBackground()
    {
        nothingButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        vosButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        pButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        nButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        jpButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        jnButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        oneaButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        onebButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        onecButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        twoaButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        twobButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        twocButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        fButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        kButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
        gButton.setBackgroundResource(R.drawable.btn_round_orange_bg);
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

        Date d = new Date();
        CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
        String filename = "bitmap_" + s + ".png";

        Intent in1 = new Intent(this, CropImagesActivity.class);
        in1.putExtra("image", filename);
        startActivity(in1);

        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("ff",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }

//        try {
//            FileOutputStream fos = new FileOutputStream(pictureFile);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
//            fos.close();
//        } catch (FileNotFoundException e) {
//            Log.d("ff", "File not found: " + e.getMessage());
//        } catch (IOException e) {
//            Log.d("ff", "Error accessing file: " + e.getMessage());
//        }

        try {
            //Write file

//            Date d = new Date();
//            CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
//            String filename = "bitmap_" + s + ".png";
//
//            Intent in1 = new Intent(this, CropImagesActivity.class);
//            in1.putExtra("image", filename);
//            startActivity(in1);

            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            bitmap.recycle();

            EventBus.getDefault().post(new ChoosePrefixEvent(pos));

            //Pop intent
//            Intent in1 = new Intent(this, CropImagesActivity.class);
//            in1.putExtra("image", filename);
//            startActivity(in1);
//            finish();
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
