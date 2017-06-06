package com.kag.controlthroughnotification;


import android.app.Notification;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import android.os.Bundle;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationReader extends NotificationListenerService {
    String array[],array1[];
    SharedPreferences preferences;
    String[] isEnable_files;
    Boolean[] Enabled;
    private String key1, key2;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
      //  super.onNotificationPosted(sbn);
        String pack = sbn.getPackageName();
        String ticker = "";
        if (sbn.getNotification().tickerText != null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text="";

        try {
           if( extras.getCharSequence("android.text")!=null) {
               text = extras.getCharSequence("android.text").toString();
           }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap id = sbn.getNotification().largeIcon;
        array = ticker.split(" ");
      if (!text .contentEquals("")) {
          array1 = text.split(" ");
          Log.i("Text", text);
      }
        Log.i("Package", pack);
        Log.i("Ticker", ticker);
        Log.i("Title", title);

        isEnable_files = new String[]{"call_isEnable", "wifi_isEnable","hotspot_isEnable","bluetooth_isEnable","openApp_isEnable","soundMode_isEnable","soundVolume_isEnable","mobileData_isEnable"};
        Enabled = new Boolean[isEnable_files.length];
        for (int i = 0; i < isEnable_files.length; i++) {
            preferences = getSharedPreferences(isEnable_files[i], Context.MODE_PRIVATE);
            Boolean t = preferences.getBoolean(isEnable_files[i], false);
            if (t) {
                Enabled[i] = true;
            } else Enabled[i] = false;
        }
        Boolean gotit=false;
        for(int j=0;j<2;j++) {
            if (Enabled[0]) {
                preferences = getSharedPreferences("file_call_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_call_key1", "");
                preferences = getSharedPreferences("file_call_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_call_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + array[i + 2]));
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(getApplicationContext(), "give permission for call", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            gotit=true;
                            startActivity(intent1);
                        }
                    }
                }
            }
            if (Enabled[1]) {
                preferences = getSharedPreferences("file_wifi_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_wifi_key1", "");
                preferences = getSharedPreferences("file_wifi_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_wifi_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            if (array[i + 2].contains("on")) {
                                wifiManager.setWifiEnabled(true);
                                gotit=true;
                            }
                            if (array[i + 2].contains("off")) {
                                wifiManager.setWifiEnabled(false);
                                gotit=true;
                            }
                        }
                    }
                }
            }
            if (Enabled[2]){
                preferences = getSharedPreferences("file_hotspot_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_hotspot_key1", "");
                preferences = getSharedPreferences("file_hotspot_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_hotspot_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
                            WifiConfiguration wifiConfiguration=null;
                            wifiManager.setWifiEnabled(false);
                            try {
                                Method method=wifiManager.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,Boolean.TYPE);
                                if(array[i+2].contains("on")){
                                    method.invoke(wifiManager,wifiConfiguration,true);
                                    gotit=true;
                                }
                                if(array[i+2].contains("off")){
                                    method.invoke(wifiManager,wifiConfiguration,false);
                                    gotit=true;
                                }
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if(Enabled[3]){
                preferences = getSharedPreferences("file_bluetooth_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_bluetooth_key1", "");
                preferences = getSharedPreferences("file_bluetooth_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_bluetooth_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                            if(array[i+2].contains("on")){
                                bluetoothAdapter.enable();
                                bluetoothAdapter.startDiscovery();
                                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                gotit=true;
                            }
                            if(array[i+2].contains("off")){
                                bluetoothAdapter.disable();
                                gotit=true;
                            }
                        }
                    }
                }
            }
            if(Enabled[4]){
                preferences = getSharedPreferences("file_openApp_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_openApp_key1", "");
                preferences = getSharedPreferences("file_openApp_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_openApp_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            Intent intent=getPackageManager().getLaunchIntentForPackage(array[i + 2]);
                            if(intent != null){
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                gotit=true;
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
            if(Enabled[5]){
                preferences = getSharedPreferences("file_soundMode_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_soundMode_key1", "");
                preferences = getSharedPreferences("file_soundMode_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_soundMode_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                            if(array[i+2].contains("normal")){
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                gotit=true;
                            }
                            if(array[i+2].contains("vibrate")){
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                gotit=true;
                            }
                            if(array[i+2].contains("silent")){
                                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                gotit=true;
                            }
                        }
                    }
                }
            }
            if(Enabled[6]){
                preferences = getSharedPreferences("file_soundVolume_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_soundVolume_key1", "");
                preferences = getSharedPreferences("file_soundVolume_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_soundVolume_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                            AudioManager audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                            if(array[i+2].contains("increase")){
                                audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
                                gotit=true;
                            }

                            if(array[i+2].contains("decrease")){
                                audioManager.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND);
                                gotit=true;
                            }
                            if(array[i+2].contains("high")){
                                audioManager.setStreamVolume(AudioManager.STREAM_RING,audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),1);
                                gotit=true;
                            }
                            if(array[i+2].contains("musicHigh")){
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),1);
                                gotit=true;
                            }
                        }
                    }
                }
            }
            if(Enabled[7]){
                preferences = getSharedPreferences("file_mobileData_key1", Context.MODE_PRIVATE);
                key1 = preferences.getString("file_mobileData_key1", "");
                preferences = getSharedPreferences("file_mobileData_key2", Context.MODE_PRIVATE);
                key2 = preferences.getString("file_mobileData_key2", "");
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains(key1)) {
                        if (array[i + 1].contains(key2)) {
                          MobileData mobileData=new MobileData();
                            if(array[i+2].contains("on")){
                                mobileData.enable(getApplicationContext());
                                gotit=true;
                            }
                            if(array[i+2].contains("off")){
                                mobileData.disable(getApplicationContext());
                                gotit=true;
                            }
                        }
                    }
                }
            }
            if (!gotit){
                if (!text .contentEquals("")) {
                   array = array1;
                }
            }else break;
        }


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //super.onNotificationRemoved(sbn);
    }
}
