package com.google.firebase.codelab.mlkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.codelab.mlkit.adapter.SearchListAdapter;
import com.google.firebase.codelab.mlkit.event.SearchJavaEvent;
import com.google.firebase.codelab.mlkit.model.SearchItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SearchListActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerViewSearchList;
    private List<SearchItem> mSearchItem;
    private SearchListAdapter mSearchAdapter;

    private ImageView howtoButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        EventBus.getDefault().register(this);
        initView();
        initValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView()
    {
        howtoButton = findViewById(R.id.imgView_howto);
        mRecyclerViewSearchList = findViewById(R.id.recyclerSearchList);
    }

    private void initValue()
    {
        retrieveArray();
        mSearchAdapter = new SearchListAdapter();
        mSearchAdapter.setSearchItems(mSearchItem);
        mRecyclerViewSearchList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewSearchList.setAdapter(mSearchAdapter);

        howtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SearchListActivity.this, HowToActivity.class);
                SearchListActivity.this.startActivity(myIntent);
            }
        });
    }

    private void retrieveArray()
    {
        SharedPreferences prefs = getSharedPreferences("aaa", Context.MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("date", "");
        mSearchItem =
                new Gson().fromJson(httpParamJSONList, new TypeToken<List<SearchItem>>() {
                }.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendImage(SearchJavaEvent event)
    {
//        showConfirmDialog({
//                releaseDownStream()
//                super.onBackPressed()
//        })
       initValue();
    }
}
