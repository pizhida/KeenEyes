package com.google.firebase.codelab.mlkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraHowToActivity extends AppCompatActivity
{
    LinearLayout llCheck;
    ImageView closeImage;
    CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);

        closeImage = findViewById(R.id.imgView_close);
        llCheck = findViewById(R.id.ll_check);
        checkBox = findViewById(R.id.checkBox);

        llCheck.setVisibility(View.VISIBLE);

        ImageView mBack = findViewById(R.id.img_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CameraHowToActivity.this, CameraActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                CameraHowToActivity.this.startActivity(myIntent);
                finish();
            }
        });


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked())
                {
                    //Boolean statusLocked = prefs.edit().putBoolean("locked", true).commit();
                    prefs.edit().putBoolean("locked", true).apply();
                }
                else
                {
                    //Boolean statusLocked = prefs.edit().putBoolean("locked", false).commit();
                    prefs.edit().putBoolean("locked", false).apply();
                }

            }
        });



    }

}
