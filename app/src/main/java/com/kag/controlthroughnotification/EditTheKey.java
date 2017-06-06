package com.kag.controlthroughnotification;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;



public class EditTheKey extends AppCompatActivity {
    String keyTitle;
    String key1;
    String key2;
    Spanned description_text;
    TextView view_title,discription;
    Boolean save_state;
    EditText Edit_key1,Edit_key2;
    String file_key1,file_key2,tempString,file_name1,file_name2,file_name_enable;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save_state=false;
        Intent intent=getIntent();
        keyTitle=intent.getStringExtra("dialog");
        if(keyTitle.contains("call")){
           file_key1= fileGetName("file_call_key1");
           file_key2=fileGetName("file_call_key2");
            file_name1="file_call_key1";
            file_name2="file_call_key2";
            file_name_enable="call_isEnable";
            tempString="123456789 \n \t then it will automatically calls to that number 123456789";
            description_text=Html.fromHtml(getString(R.string.call_text));
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
                    Toast.makeText(this,"grant permission otherwise app will not work properly",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);

                }else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
                }
            }
        }
        if(keyTitle.contains("wifi")){
            file_key1=fileGetName("file_wifi_key1");
            file_key2=fileGetName("file_wifi_key2");
            file_name1="file_wifi_key1";
            file_name2="file_wifi_key2";
            file_name_enable="wifi_isEnable";
            tempString="on \n \t then it will automatically turn on wifi";
            description_text=Html.fromHtml(getString(R.string.wifi_text));
        }
        if(keyTitle.contains("hotspot")){
            file_key1=fileGetName("file_hotspot_key1");
            file_key2=fileGetName("file_hotspot_key2");
            file_name1="file_hotspot_key1";
            file_name2="file_hotspot_key2";
            file_name_enable="hotspot_isEnable";
            tempString="on \n \t then it will automatically turn on hotspot";
            description_text=Html.fromHtml(getString(R.string.hotspot_text));
        }
        if(keyTitle.contains("bluetooth")){
            file_key1=fileGetName("file_bluetooth_key1");
            file_key2=fileGetName("file_bluetooth_key2");
            file_name1="file_bluetooth_key1";
            file_name2="file_bluetooth_key2";
            file_name_enable="bluetooth_isEnable";
            tempString="on \n \t then it will automatically turn on bluetooth";
            description_text=Html.fromHtml(getString(R.string.bluetooth_text));
        }
        if(keyTitle.contains("openApp")){
            file_key1=fileGetName("file_openApp_key1");
            file_key2=fileGetName("file_openApp_key2");
            file_name1="file_openApp_key1";
            file_name2="file_openApp_key2";
            file_name_enable="openApp_isEnable";
            tempString="com.whatsapp \n \t then it will automatically open the whatsapp application";
            description_text=Html.fromHtml(getString(R.string.openApp_text));
        }
        if(keyTitle.contains("soundMode")){
            file_key1=fileGetName("file_soundMode_key1");
            file_key2=fileGetName("file_soundMode_key2");
            file_name1="file_soundMode_key1";
            file_name2="file_soundMode_key2";
            file_name_enable="soundMode_isEnable";
            tempString="vibrate \n \t then it will automatically set vibrate mode";
            description_text=Html.fromHtml(getString(R.string.soundMode_text));
        }
        if(keyTitle.contains("soundVolume")){
            file_key1=fileGetName("file_soundVolume_key1");
            file_key2=fileGetName("file_soundVolume_key2");
            file_name1="file_soundVolume_key1";
            file_name2="file_soundVolume_key2";
            file_name_enable="soundVolume_isEnable";
            tempString="increase \n \t then it will automatically increase notification sound by 1";
            description_text=Html.fromHtml(getString(R.string.soundVolume_text));
        }
        if(keyTitle.contains("mobileData")){
            file_key1=fileGetName("file_mobileData_key1");
            file_key2=fileGetName("file_mobileData_key2");
            file_name1="file_mobileData_key1";
            file_name2="file_mobileData_key2";
            file_name_enable="mobileData_isEnable";
            tempString="on \n \t then it will automatically turn on mobileData";
            description_text=Html.fromHtml(getString(R.string.mobileData_text));
        }


        setContentView(R.layout.activity_edit_the_key);
        view_title=(TextView) findViewById(R.id.text_title);
        view_title.setText(keyTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Edit_key1=(EditText)findViewById(R.id.key1);
        Edit_key2=(EditText)findViewById(R.id.key2);
        discription=(TextView)findViewById(R.id.description);
        assert discription != null;
        discription.setText(description_text);
        Edit_key1.setText(file_key1);
        Edit_key2.setText(file_key2);

        MobileAds.initialize(getApplicationContext(),"@string/banner_ad_unit_id_edit");
        AdView mAdView=(AdView)findViewById(R.id.adView1);
        AdRequest adRequest=new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private String fileGetName(String file_call_key1) {
       String d;
        preferences=getSharedPreferences(file_call_key1,Context.MODE_PRIVATE);
        d=preferences.getString(file_call_key1,"");
        return d;
    }

    public void save(View v){
        key1=Edit_key1.getText().toString();
        key2=Edit_key2.getText().toString();
        String temp="If notification contains \n "+key1+" "+key2+" "+tempString;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage(temp);
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                preferences=getSharedPreferences(file_name1,Context.MODE_PRIVATE);
                preferences.edit().putString(file_name1,key1).commit();
                preferences=getSharedPreferences(file_name2,Context.MODE_PRIVATE);
                preferences.edit().putString(file_name2,key2).commit();
                preferences=getSharedPreferences(file_name_enable,Context.MODE_PRIVATE);
                preferences.edit().putBoolean(file_name_enable,true).commit();

                Intent i=new Intent(EditTheKey.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.show();
    }
    public void close(View v){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Abort changes?");
        builder.setMessage("Do you want to leave and abort all changes?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(EditTheKey.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode!=KeyEvent.KEYCODE_BACK)
        return super.onKeyDown(keyCode, event);
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Abort changes?");
        builder.setMessage("Do you want to leave and abort all changes?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(EditTheKey.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.setNeutralButton("save changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save(view_title.getRootView());
            }
        });
        builder.show();
        return super.onKeyDown(keyCode,event);
    }
}
