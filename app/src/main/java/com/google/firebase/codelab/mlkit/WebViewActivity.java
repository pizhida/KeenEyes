package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.firebase.codelab.mlkit.model.SearchItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity
{
    private SharedPreferences shared;
    private ArrayList<String> arrPackage;
    private List<SearchItem> mSearchItem;
    private String number;

    private ImageView howtoButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        number = getIntent().getStringExtra("number");

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
                Intent myIntent = new Intent(WebViewActivity.this, HowToActivity.class);
                WebViewActivity.this.startActivity(myIntent);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavascriptManager(this), "Android");
        webView.loadUrl("https://oryor.com/oryor2015/check_product_ocr.php?number_src=" +
                number +
                "&ref=android");


//        retrieveArray();
//        SearchItem searchItem = new SearchItem();
//        searchItem.setSearchDate(Calendar.getInstance().getTime());
//        //searchItem.setSearchDetail(number);
//        if(mSearchItem == null)
//            mSearchItem = new ArrayList<SearchItem>();
//        mSearchItem.add(searchItem);
//        saveArray();
    }

//    private void saveArray()
//    {
//        List<SearchItem> httpParamList = mSearchItem;
//        String httpParamJSONList = new Gson().toJson(httpParamList);
//        SharedPreferences prefs = getSharedPreferences("aaa", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("date", httpParamJSONList);
//        editor.apply();
//    }
//
//    private void retrieveArray()
//    {
//        SharedPreferences prefs = getSharedPreferences("aaa", Context.MODE_PRIVATE);
//        String httpParamJSONList = prefs.getString("date", "");
//        mSearchItem =
//                new Gson().fromJson(httpParamJSONList, new TypeToken<List<SearchItem>>() {
//                }.getType());
//    }

}
