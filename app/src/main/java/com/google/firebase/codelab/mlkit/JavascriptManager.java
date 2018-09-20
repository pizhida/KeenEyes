package com.google.firebase.codelab.mlkit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.firebase.codelab.mlkit.event.SearchJavaEvent;
import com.google.firebase.codelab.mlkit.model.SearchItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JavascriptManager {
    Activity mContext;
    private List<SearchItem> mSearchItem;

    /** Instantiate the interface and set the context */
    JavascriptManager(Activity c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(final String numberId, final String catType ,final String check) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {

                //Toast.makeText(mContext, "fefwef" + numberId, Toast.LENGTH_LONG).show();

                retrieveArray();
                SearchItem searchItem = new SearchItem();
                searchItem.setSearchDate(Calendar.getInstance().getTime());
                searchItem.setSearchId(numberId);
                searchItem.setCatType(catType);
                searchItem.setCheck(check);
                if(mSearchItem == null)
                    mSearchItem = new ArrayList<SearchItem>();
                mSearchItem.add(searchItem);
                saveArray();



                EventBus.getDefault().post(new SearchJavaEvent());
            }
        });

    }


    private void saveArray()
    {
        List<SearchItem> httpParamList = mSearchItem;
        String httpParamJSONList = new Gson().toJson(httpParamList);
        SharedPreferences prefs = mContext.getSharedPreferences("aaa", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("date", httpParamJSONList);
        editor.apply();
    }

    private void retrieveArray()
    {
        SharedPreferences prefs = mContext.getSharedPreferences("aaa", Context.MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("date", "");
        mSearchItem =
                new Gson().fromJson(httpParamJSONList, new TypeToken<List<SearchItem>>() {
                }.getType());
    }

}