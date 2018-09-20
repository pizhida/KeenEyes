package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.firebase.codelab.mlkit.model.SearchItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CallWebViewActivity extends AppCompatActivity
{
    private SharedPreferences shared;
    private ArrayList<String> arrPackage;
    private List<SearchItem> mSearchItem;
    private String number;

    private ImageView mBack;

    private ImageView howtoButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = (WebView) findViewById(R.id.webview);

        howtoButton = findViewById(R.id.imgView_howto);

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
                Intent myIntent = new Intent(CallWebViewActivity.this, HowToActivity.class);
                CallWebViewActivity.this.startActivity(myIntent);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://oryor.com/oryor2015/complain_ocr.php");
    }

}
