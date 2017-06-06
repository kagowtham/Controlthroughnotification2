package com.kag.controlthroughnotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_start0);
    }
    public void start0(View v){
        setContentView(R.layout.activity_start);
    }
    public void start(View v){
        setContentView(R.layout.layout_start1);
    }
    public void start1(View v){
        setContentView(R.layout.layout_start2);
    }
    public void start2(View v){
        SharedPreferences preferences=getSharedPreferences("start", Context.MODE_PRIVATE);

        Intent intent=new Intent(Start.this,MainActivity.class);
        preferences.edit().putBoolean("start",false).commit();
        startActivity(intent);
        finish();
    }
}
