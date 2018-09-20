package com.google.firebase.codelab.mlkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SpeechToTextActivity extends AppCompatActivity
{

    private static final int REQUEST_CODE_VOICE_RECOGNITION = 1001;

    private EditText mEditTextspeechToText;
    private ImageView mButtonSpeechToTextSearch;
    private Button mButtonSpeechToTextReset;
    private Button mButtonSpeechTotextRecord;

    private ImageView howtoButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        ImageView mBack = findViewById(R.id.img_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        initView();
        initValue();

    }

    private void initView()
    {
        mEditTextspeechToText = findViewById(R.id.edtSpeechTotext);
        mButtonSpeechToTextSearch = findViewById(R.id.btnSearch);
        mButtonSpeechToTextReset = findViewById(R.id.btnReset);
        mButtonSpeechTotextRecord = findViewById(R.id.btnRec);

        howtoButton = findViewById(R.id.imgView_howto);
    }

    private void initValue()
    {
        mButtonSpeechTotextRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVoiceRecognition();
            }
        });

        mButtonSpeechToTextReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextspeechToText.setText("");
            }
        });

        howtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SpeechToTextActivity.this, HowToActivity.class);
                SpeechToTextActivity.this.startActivity(myIntent);
            }
        });

        mButtonSpeechToTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SpeechToTextActivity.this, WebViewActivity.class);
                myIntent.putExtra("number", mEditTextspeechToText.getText().toString()); //Optional parameters
                SpeechToTextActivity.this.startActivity(myIntent);
                finish();
            }
        });
    }

    private void callVoiceRecognition()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH");
        this.startActivityForResult(intent, REQUEST_CODE_VOICE_RECOGNITION );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VOICE_RECOGNITION && resultCode == Activity.RESULT_OK)
        {
//            if(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).replace(" ", "").replaceAll("[^0-9]", "").matches("[0-9]+"))
//            {
//                mEditTextspeechToText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).replace(" ", "").replaceAll("[^0-9]", ""));
//            }

            String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).replace(" ","");

            if(!result.isEmpty())
            {
                if(result.charAt(0) == '/')
                {
                    result = result.substring(1);
                }
                if(result.charAt(result.length()-1) == '/')
                {
                    result = result.substring(0,result.length()-1);
                }
                String[] sums = result.split("-");
                if(sums.length == 0)
                {
                    mEditTextspeechToText.setText(result);
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

                    mEditTextspeechToText.setText(realsum);
                }
            }
            //mEditTextspeechToText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }

    }
}
