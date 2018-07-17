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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        initView();
        initValue();

    }

    private void initView()
    {
        mEditTextspeechToText = findViewById(R.id.edtSpeechTotext);
        mButtonSpeechToTextSearch = findViewById(R.id.btnSearch);
        mButtonSpeechToTextReset = findViewById(R.id.btnReset);
        mButtonSpeechTotextRecord = findViewById(R.id.btnRec);
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
            mEditTextspeechToText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).replace(" ", ""));
        }

    }
}
