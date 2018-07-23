// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.codelab.mlkit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.codelab.mlkit.GraphicOverlay.Graphic;
import com.google.firebase.codelab.mlkit.event.ChoosePrefixEvent;
import com.google.firebase.codelab.mlkit.event.SelectImageInGalleryEvent;
import com.google.firebase.codelab.mlkit.event.SendImageEvent;
import com.google.firebase.codelab.mlkit.interfaces.ImageUtilsCallback;
import com.google.firebase.codelab.mlkit.interfaces.OnpermissionResultListener;
import com.google.firebase.codelab.mlkit.tools.RequestPermissionsTool;
import com.google.firebase.codelab.mlkit.tools.RequestPermissionsToolImpl;
import com.google.firebase.codelab.mlkit.utils.ImageUtils;
import com.google.firebase.codelab.mlkit.utils.PermissionManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudDocumentTextDetector;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ImageUtilsCallback<SimpleDraweeView>, ActivityCompat.OnRequestPermissionsResultCallback
{
    private static final String TAG = "MainActivity";
   // private ImageView mImageView;
    private Button mButton;
    private Button mCloudButton;
    private Bitmap mSelectedImage;
    private ImageView mButtonCapture;
    private ImageView mButtonCaptureCm;

    private String filename;

    private EditText textResult;

    private TessBaseAPI tessBaseApi;

    private ImageView searchBtn;

    private String prefix;
    private GraphicOverlay mGraphicOverlay;
    // Max width (portrait mode)
    private Integer mImageMaxWidth;
    // Max height (portrait mode)
    private Integer mImageMaxHeight;
    private Uri outputFileUri;

    private RequestPermissionsTool requestTool; //for API >=23 only

    static final int PHOTO_REQUEST_CODE = 1;

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";

    private static final int REQUEST_CODE_VOICE_RECOGNITION = 1001;

    private ImageUtils<SimpleDraweeView> imageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        prefix = "";
        filename = "";

        //mImageView = findViewById(R.id.image_view);

        mButton = findViewById(R.id.button_text);
        mCloudButton = findViewById(R.id.button_cloud_text);
        mButtonCapture = findViewById(R.id.img_voice_footer);
        mButtonCaptureCm = findViewById(R.id.img_camera_footer);
        searchBtn = findViewById(R.id.btn_search);

        textResult = findViewById(R.id.text_result);

        //mGraphicOverlay = findViewById(R.id.graphic_overlay);

        imageUtils = new ImageUtils();
        imageUtils.setCallback(this);

        mButtonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                imageUtils.setConfig(true, CropImageView.CropShape.RECTANGLE, 1, 1).takeAPicture(MainActivity.this);
                Intent myIntent = new Intent(MainActivity.this, SpeechToTextActivity.class);
                myIntent.putExtra("number", textResult.getText().toString()); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, WebViewActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

        mButtonCaptureCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                AlertDialog.Builder builder =
//                        new AlertDialog.Builder(MainActivity.this);
//                builder.setMessage("เลือกวิธีสแกน OCR ?");
//                builder.setPositiveButton("เลือกรูป", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        imageUtils.setConfig(true, CropImageView.CropShape.RECTANGLE, 1, 1).pickImage(MainActivity.this);
//                    }
//                });
//                builder.setNegativeButton("ถ่ายรูป", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //dialog.dismiss();
//                        granted();
//                    }
//                });
//                builder.show();


                granted();

//                Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
//                //myIntent.putExtra("key", value); //Optional parameters
//                MainActivity.this.startActivity(myIntent);
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTextRecognition();
            }
        });
//        mCloudButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runCloudTextRecognition(outputFileUri);
//            }
//        });
        //Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Image 1", "Image 2", "Image 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }

    private void granted()
    {
        String [] sArray = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

        if (PermissionManager.Companion.getInstance().isHavePermission(MainActivity.this, sArray)) {
            //currentView = view

            Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);

        } else
        {
            PermissionManager.Companion.getInstance().setPermissionResultListener(new OnpermissionResultListener() {
                @Override
                public void onResult(@NotNull List<String> permission, @NotNull List<Boolean> grandted)
                {
                    if(grandted.size() == 2) {
                        granted();
                    }
                }
            });

            PermissionManager.Companion.getInstance().requestPermission(MainActivity.this, sArray);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void runTextRecognition()
    {
        try {
            FileInputStream is = this.openFileInput(filename);
            mSelectedImage = BitmapFactory.decodeStream(is);
            //cropImageView.setImageBitmap(mBitmap);

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
            FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                    .getVisionTextDetector();
            mButton.setEnabled(false);
            detector.detectInImage(image)
                    .addOnSuccessListener(
                            new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText texts) {
                                    mButton.setEnabled(true);
                                    processTextRecognitionResult(texts);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    mButton.setEnabled(true);
                                    e.printStackTrace();
                                }
                            });
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
//        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
//                .getVisionTextDetector();
//        mButton.setEnabled(false);
//        detector.detectInImage(image)
//                .addOnSuccessListener(
//                        new OnSuccessListener<FirebaseVisionText>() {
//                            @Override
//                            public void onSuccess(FirebaseVisionText texts) {
//                                mButton.setEnabled(true);
//                                processTextRecognitionResult(texts);
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Task failed with an exception
//                                mButton.setEnabled(true);
//                                e.printStackTrace();
//                            }
//                        });
    }

    private void runTextRecognition(Uri imguri)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
        Bitmap bitmap = BitmapFactory.decodeFile(imguri.getPath(), options);


        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        mButton.setEnabled(false);
        detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                mButton.setEnabled(true);
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                mButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }



    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }
        String sum = "";

        //mGraphicOverlay.clear();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();

            for (int j = 0; j < lines.size(); j++)
            {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {

//                    if(!elements.get(k).getText().matches("[a-zA-Z]+"))
//                    {
////                        sum = sum + elements.get(k).getText().replace(" ","");
//
//                        sum = sum + elements.get(k).getText().replace(" ","");
//                    }


//                        sum = sum + elements.get(k).getText().replace(" ","");

                        sum = sum + elements.get(k).getText().replace(" ","");

                    //sum = sum + elements.get(k).getText();
//                    Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
//                    mGraphicOverlay.add(textGraphic);

                }
            }
        }

        if(!sum.isEmpty())
        {
            //sum = "1";
            if(sum.charAt(0) == '/')
            {
                sum = sum.substring(1,sum.length()-1);
            }
            if(sum.charAt(sum.length()-1) == '/')
            {
                sum = sum.substring(0,sum.length()-2);
            }
            String[] sums = sum.split("-");
            if(sums.length == 0)
            {
                textResult.setText(prefix + sum);
            }
            else
            {
                String realsum = "";
                for(int i=0;i<sums.length;i++)
                {
                    realsum += sums[i].replaceAll("[^0-9]", "");
                    if(i != sums.length-1)
                    {
                        realsum += "-";
                    }
                }

                textResult.setText(prefix + realsum);
            }

        }


        //Toast.makeText(this, texts.getText(), Toast.LENGTH_LONG).show();
    }

//    private void runCloudTextRecognition(Uri imguri)
//    {
//
//        BitmapFactory.Options optionss = new BitmapFactory.Options();
//        optionss.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
//        Bitmap bitmap = BitmapFactory.decodeFile(imguri.getPath(), optionss);
//
//
//        FirebaseVisionCloudDetectorOptions options =
//                new FirebaseVisionCloudDetectorOptions.Builder()
//                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
//                        .setMaxResults(15)
//                        .build();
//        mCloudButton.setEnabled(false);
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//        FirebaseVisionCloudDocumentTextDetector detector = FirebaseVision.getInstance()
//                .getVisionCloudDocumentTextDetector(options);
//        detector.detectInImage(image)
//                .addOnSuccessListener(
//                        new OnSuccessListener<FirebaseVisionCloudText>() {
//                            @Override
//                            public void onSuccess(FirebaseVisionCloudText texts) {
//                                mCloudButton.setEnabled(true);
//                                processCloudTextRecognitionResult(texts);
//                            }
//                        })
//                .addOnFailureListener(
//                        new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Task failed with an exception
//                                mCloudButton.setEnabled(true);
//                                e.printStackTrace();
//                            }
//                        });
//    }

//    private void processCloudTextRecognitionResult(FirebaseVisionCloudText text) {
//        // Task completed successfully
//        if (text == null) {
//            showToast("No text found");
//            return;
//        }
//        mGraphicOverlay.clear();
//        StringBuilder allWords = new StringBuilder();
//        List<FirebaseVisionCloudText.Page> pages = text.getPages();
//        for (int i = 0; i < pages.size(); i++) {
//            FirebaseVisionCloudText.Page page = pages.get(i);
//            List<FirebaseVisionCloudText.Block> blocks = page.getBlocks();
//            for (int j = 0; j < blocks.size(); j++) {
//                List<FirebaseVisionCloudText.Paragraph> paragraphs = blocks.get(j).getParagraphs();
//                for (int k = 0; k < paragraphs.size(); k++)
//                {
//                    FirebaseVisionCloudText.Paragraph paragraph = paragraphs.get(k);
//                    List<FirebaseVisionCloudText.Word> words = paragraph.getWords();
//
//                    for (int l = 0; l < words.size(); l++)
//                    {
////                        Graphic cloudTextGraphic = new CloudTextGraphic(mGraphicOverlay, words
////                                .get(l));
////                        mGraphicOverlay.add(cloudTextGraphic);
//
//
//                        //allWords.append(words.get(l).getSymbols().toString());
//                    }
//                }
//            }
//        }
//
//
//        Toast.makeText(this, text.getText(), Toast.LENGTH_LONG).show();
//    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Functions for loading images from app assets.

    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
//    private Integer getImageMaxWidth() {
//        if (mImageMaxWidth == null) {
//            // Calculate the max width in portrait mode. This is done lazily since we need to
//            // wait for
//            // a UI layout pass to get the right values. So delay it to first time image
//            // rendering time.
//            mImageMaxWidth = mImageView.getWidth();
//        }
//
//        return mImageMaxWidth;
//    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
//    private Integer getImageMaxHeight() {
//        if (mImageMaxHeight == null) {
//            // Calculate the max width in portrait mode. This is done lazily since we need to
//            // wait for
//            // a UI layout pass to get the right values. So delay it to first time image
//            // rendering time.
//            mImageMaxHeight =
//                    mImageView.getHeight();
//        }
//
//        return mImageMaxHeight;
//    }

    // Gets the targeted width / height.
//    private Pair<Integer, Integer> getTargetedWidthHeight() {
//        int targetWidth;
//        int targetHeight;
//        int maxWidthForPortraitMode = getImageMaxWidth();
//        int maxHeightForPortraitMode = getImageMaxHeight();
//        targetWidth = maxWidthForPortraitMode;
//        targetHeight = maxHeightForPortraitMode;
//        return new Pair<>(targetWidth, targetHeight);
//    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        //mGraphicOverlay.clear();
        switch (position) {
            case 0:
                mSelectedImage = getBitmapFromAsset(this, "Please_walk_on_the_grass.jpg");

                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                mSelectedImage = getBitmapFromAsset(this, "non-latin.jpg");
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                mSelectedImage = getBitmapFromAsset(this, "nl2.jpg");
                break;
        }
        if (mSelectedImage != null) {
            // Get the dimensions of the View
//            Pair<Integer, Integer> targetedSize = getTargetedWidthHeight();
//
//            int targetWidth = targetedSize.first;
//            int maxHeight = targetedSize.second;
//
//            // Determine how much to scale down the image
//            float scaleFactor =
//                    Math.max(
//                            (float) mSelectedImage.getWidth() / (float) targetWidth,
//                            (float) mSelectedImage.getHeight() / (float) maxHeight);
//
//            Bitmap resizedBitmap =
//                    Bitmap.createScaledBitmap(
//                            mSelectedImage,
//                            (int) (mSelectedImage.getWidth() / scaleFactor),
//                            (int) (mSelectedImage.getHeight() / scaleFactor),
//                            true);
//
//            mImageView.setImageBitmap(resizedBitmap);
//            mSelectedImage = resizedBitmap;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream is;
        Bitmap bitmap = null;
        try {
            is = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    public void onPicked(SimpleDraweeView simpleDraweeView, Uri uri)
    {
        outputFileUri = uri;
        textResult.setText("");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            runTextRecognition(uri);
        } else{
            doEngOCR();
        }


        //runCloudTextRecognition(outputFileUri);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //runCloudTextRecognition(outputFileUri);
            //doThaiOCR();
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
//            mTXLivePusher.switchCamera()
//            mTXLivePusher.switchCamera()
            imageUtils.onCropImageResult(resultCode, data);
//            doOCR();
        }
        if (requestCode == ImageUtils.Companion.getREQ_CODE())
        {
            imageUtils.cropImage(resultCode, this, data);
        }

        else if (requestCode == REQUEST_CODE_VOICE_RECOGNITION && resultCode == Activity.RESULT_OK)
        {
            ArrayList<String> resultList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, resultList.get(0), Toast.LENGTH_LONG).show();
            // Do something with resultList
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean grantedAllPermissions = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                grantedAllPermissions = false;
            }
        }

        if (grantResults.length != permissions.length || (!grantedAllPermissions)) {

            requestTool.onPermissionDenied();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestTool = new RequestPermissionsToolImpl();
        requestTool.requestPermissions(this, permissions);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendImage(SendImageEvent event)
    {
//        showConfirmDialog({
//                releaseDownStream()
//                super.onBackPressed()
//        })
        textResult.setText("");
        filename = event.getBitmap();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            runTextRecognition();
        } else{
            doEngOCR();
        }
    }

    private void doEngOCR()
    {
        prepareTesseract();
        startOCR();
    }

    private void doEngOCR2()
    {
        prepareTesseract();
        startOCR(outputFileUri);
    }

    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyTessDataFiles(TESSDATA);
    }

    private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + path);
        }
    }


    private void startOCR() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.

            FileInputStream is = this.openFileInput(filename);
            mSelectedImage = BitmapFactory.decodeStream(is);

            //Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);

            result = extractText(mSelectedImage);

            if(!result.isEmpty())
            {
                result.replace(" ","");

//            if(result.replaceAll("[^0-9]", "").matches("[0-9]+"))
//            //if(!result.matches("[a-zA-Z]+"))
//            {
//                textResult.setText(prefix + result);
//            }

                if(result.charAt(0) == '/')
                {
                    result = result.substring(1,result.length()-1);
                }
                if(result.charAt(result.length()-1) == '/')
                {
                    result = result.substring(0,result.length()-2);
                }
                String[] sums = result.split("-");
                if(sums.length == 0)
                {
                    textResult.setText(prefix + result);
                }
                else
                {
                    String realsum = "";
                    for(int i=0;i<sums.length;i++)
                    {
                        realsum += sums[i].replaceAll("[^0-9]", "");
                        if(i != sums.length-1)
                        {
                            realsum += "-";
                        }
                    }

                    textResult.setText(prefix + realsum);
                }
            }


            //doThaiOCR();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void startOCR(Uri uri)
    {
        try {

            BitmapFactory.Options optionss = new BitmapFactory.Options();
            optionss.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            mSelectedImage = BitmapFactory.decodeFile(uri.getPath(), optionss);

            //Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);

            result = extractText(mSelectedImage);

            textResult.setText(prefix + result);

            //doThaiOCR();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }



    String result = "empty";

    private static final String TESSDATA = "tessdata";

    private void copyTessDataFiles(String path) {
        try {
            String fileList[] = getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }

    private String extractText(Bitmap bitmap) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }

        tessBaseApi.init(DATA_PATH, "eng");

//       //EXTRA SETTINGS
//        //For example if we only want to detect numbers
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//
//        //blackList Example
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

        Log.d(TAG, "Training file loaded");
        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePos(ChoosePrefixEvent event)
    {
        if(event.getNum() == 0)
        {
            prefix = "";
        }
        else if(event.getNum() == 1)
        {
            prefix = "วอส. ";
        }
        else
        {
            prefix = "ผ. ";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectImageInGallery(SelectImageInGalleryEvent event)
    {
        imageUtils.setConfig(true, CropImageView.CropShape.RECTANGLE, 1, 1).pickImage(MainActivity.this);
    }

}
