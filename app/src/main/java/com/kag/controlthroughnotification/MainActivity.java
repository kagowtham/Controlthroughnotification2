package com.kag.controlthroughnotification;


import android.app.AlertDialog;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    String files[], predifined[], isEnable_files[], title_array[], key1_array[], key2_array[],delete_file_name[];
    ListView listView;
    ArrayList<HashMap<String, String>> keyList;
    TextView textView;
    Boolean Enabled[];
    SharedPreferences preferences;
    Boolean is_go_settings=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getSharedPreferences("start",Context.MODE_PRIVATE);
        if(preferences.getBoolean("start",true)){
            Intent intent=new Intent(MainActivity.this,Start.class);
            startActivity(intent);
            finish();
        }else {

            ContentResolver contentResolver = getContentResolver();
            String enableNotificationListener = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
            if (enableNotificationListener == null || !enableNotificationListener.contains(getPackageName())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enable permission").setMessage("grant permission for reading notification ");
                builder.setPositiveButton("go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        startActivity(intent);
                        is_go_settings = true;
                    }
                });
                builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else {
                setContentView(R.layout.layout);
               Log.e("status","granted");
                AsynsTaskRunner runner = new AsynsTaskRunner();
                runner.execute();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
            finish();
            return true;
        }
        if(id == R.id.about){
            Intent intent=new Intent(MainActivity.this,About.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.start){
            Intent intent=new Intent(MainActivity.this,Start.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void popup(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String s[] = {"call","wifi","hotspot","bluetooth","openApp","soundMode","soundVolume","mobileData"};
        builder.setTitle("Add");
        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, EditTheKey.class);
                intent.putExtra("dialog", s[which]);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private String fileGetName(String file_call_key1) {
        String d = new String();

        preferences=getSharedPreferences(file_call_key1,Context.MODE_PRIVATE);
        d=preferences.getString(file_call_key1,"");
        return d;
    }
    private class AsynsTaskRunner extends AsyncTask<String,String,String>{
        AdView mAdView;
        AdRequest adRequest;
        int count1=0;
        Boolean firstrun=false;
        @Override
        protected String doInBackground(String... params) {
            if (getPreferences(MODE_PRIVATE).getBoolean("firstrun", true)) {
                Log.e("isfirstrun", Boolean.toString(true));
                files = new String[]{"file_call_key1", "file_call_key2", "file_wifi_key1", "file_wifi_key2","file_hotspot_key1","file_hotspot_key2","file_bluetooth_key1","file_bluetooth_key2","file_openApp_key1","file_openApp_key2","file_soundMode_key1","file_soundMode_key2","file_soundVolume_key1","file_soundVolume_key2","file_mobileData_key1","file_mobileData_key2"};
                predifined = new String[]{"Android", "call", "Android", "wifi","Android","hotspot","Android","bluetooth","Android","openApp","Android","soundMode","Android","soundVolume","Android","mobileData"};
                for (int i = 0; i < files.length; i++) {

                    preferences = getSharedPreferences(files[i], Context.MODE_PRIVATE);
                    preferences.edit().putString(files[i], predifined[i]).commit();
                    predifined[i]=null;
                    files[i]=null;
                }
                isEnable_files = new String[]{"call_isEnable","wifi_isEnable","hotspot_isEnable","bluetooth_isEnable","openApp_isEnable","soundMode_isEnable","soundVolume_isEnable","mobileData_isEnable"};
                for (int i = 0; i < isEnable_files.length; i++) {
                    preferences = getSharedPreferences(isEnable_files[i], Context.MODE_PRIVATE);
                    preferences.edit().putBoolean(isEnable_files[i], false).commit();
                    isEnable_files[i]=null;
                }
                getPreferences(MODE_PRIVATE).edit().putBoolean("firstrun", false).commit();
               firstrun=true;
                } else {

                int count = 0;

                isEnable_files = new String[]{"call_isEnable", "wifi_isEnable","hotspot_isEnable","bluetooth_isEnable","openApp_isEnable","soundMode_isEnable","soundVolume_isEnable","mobileData_isEnable"};
                Enabled = new Boolean[isEnable_files.length];
                for (int i = 0; i < isEnable_files.length; i++) {
                    preferences = getSharedPreferences(isEnable_files[i], Context.MODE_PRIVATE);
                    Boolean t = preferences.getBoolean(isEnable_files[i], false);
                    isEnable_files[i]=null;
                    if (t) {
                        Enabled[i] = true;
                        count++;
                    } else Enabled[i] = false;

                }
                count1 = 0;
                title_array = new String[count];
                key1_array = new String[count];
                key2_array = new String[count];
                delete_file_name = new String[count];
                if (Enabled[0]) {
                    title_array[count1] = "call";
                    key1_array[count1] = fileGetName("file_call_key1");
                    key2_array[count1] = fileGetName("file_call_key2");
                    delete_file_name[count1] = "call_isEnable";
                    count1++;
                }
                if (Enabled[1]) {
                    title_array[count1] = "wifi";
                    key1_array[count1] = fileGetName("file_wifi_key1");
                    key2_array[count1] = fileGetName("file_wifi_key2");
                    delete_file_name[count1] = "wifi_isEnable";
                    count1++;
                }
                if(Enabled[2]){
                    title_array[count1] = "hotspot";
                    key1_array[count1] = fileGetName("file_hotspot_key1");
                    key2_array[count1] = fileGetName("file_hotspot_key2");
                    delete_file_name[count1] = "hotspot_isEnable";
                    count1++;
                }
                if(Enabled[3]){
                    title_array[count1] = "bluetooth";
                    key1_array[count1] = fileGetName("file_bluetooth_key1");
                    key2_array[count1] = fileGetName("file_bluetooth_key2");
                    delete_file_name[count1] = "bluetooth_isEnable";
                    count1++;
                }
                if(Enabled[4]){
                    title_array[count1] = "openApp";
                    key1_array[count1] = fileGetName("file_openApp_key1");
                    key2_array[count1] = fileGetName("file_openApp_key2");
                    delete_file_name[count1] = "openApp_isEnable";
                    count1++;
                }
                if(Enabled[5]){
                    title_array[count1] = "soundMode";
                    key1_array[count1] = fileGetName("file_soundMode_key1");
                    key2_array[count1] = fileGetName("file_soundMode_key2");
                    delete_file_name[count1] = "soundMode_isEnable";
                    count1++;
                }
                if(Enabled[6]){
                    title_array[count1] = "soundVolume";
                    key1_array[count1] = fileGetName("file_soundVolume_key1");
                    key2_array[count1] = fileGetName("file_soundVolume_key2");
                    delete_file_name[count1] = "soundVolume_isEnable";
                    count1++;
                }
                if(Enabled[7]){
                    title_array[count1] = "mobileData";
                    key1_array[count1] = fileGetName("file_mobileData_key1");
                    key2_array[count1] = fileGetName("file_mobileData_key2");
                    delete_file_name[count1] = "mobileData_isEnable";
                    count1++;
                }


                keyList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < count1; i++) {
                    HashMap<String, String> keys = new HashMap<String, String>();
                    keys.put("sno", (i + 1) + ".");
                    keys.put("title", title_array[i]);
                    keys.put("key1", "key1  :" + key1_array[i]);
                    keys.put("key2", "key2  :" + key2_array[i]);
                    keyList.add(keys);
                    key1_array[i]=null;
                    key2_array[i]=null;
                }

                MobileAds.initialize(getApplicationContext(), "@string/banner_ad_unit_id");

                adRequest = new AdRequest.Builder().build();


            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            setContentView(R.layout.layout);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (firstrun) {
                recreate();
            } else {
                setContentView(R.layout.activity_main);
                mAdView = (AdView) findViewById(R.id.adView);
                mAdView.loadAd(adRequest);
                listView = (ListView) findViewById(R.id.listView);
                if (count1==0){
                    TextView textView=(TextView)findViewById(R.id.help);
                    TextView textView1=(TextView) findViewById(R.id.help2);
                    ImageView imageView=(ImageView) findViewById(R.id.help1);
                    textView.setText("   WELCOME ");
                    imageView.setImageResource(R.drawable.ic_launcher1);
                    textView1.setText(" Tap the PLUS button \n to begin");
                }
                ListAdapter adapter = new SimpleAdapter(getApplicationContext(), keyList, R.layout.list_item, new String[]{"sno", "title", "key1", "key2"}, new int[]{R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4});
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        String b[] = new String[]{"Edit", "Delete"};
                        builder.setItems(b, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    textView = (TextView) view.findViewById(R.id.textView2);
                                    String a = textView.getText().toString();
                                    Intent intent = new Intent(MainActivity.this, EditTheKey.class);
                                    intent.putExtra("dialog", a);
                                    startActivity(intent);
                                    finish();
                                }
                                if (which == 1) {
                                    SharedPreferences preferences = getSharedPreferences(delete_file_name[position], Context.MODE_PRIVATE);
                                    preferences.edit().putBoolean(delete_file_name[position], false).commit();
                                    recreate();
                                }
                            }
                        });
                        builder.setTitle(title_array[position]);
                        builder.show();
                    }
                });
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(is_go_settings){
            recreate();
            is_go_settings=false;
        }
    }
}
