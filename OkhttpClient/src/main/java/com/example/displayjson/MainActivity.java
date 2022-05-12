package com.example.displayjson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String myResponse;
    ListView lv;
    ArrayList<HashMap<String,String>> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();
        lv = findViewById(R.id.listview);
        OkHttpClient client = new OkHttpClient();
        String url = "https://protocoderspoint.com/jsondata/superheros.json";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject reader = new JSONObject(myResponse);
                                JSONArray superheros = reader.getJSONArray("superheros"); // get the whole json array list
                                System.out.println("json size is : "+superheros.length());
                                for(int i = 0; i<superheros.length(); i++)
                                {
                                    JSONObject hero = superheros.getJSONObject(i);
                                    String name = hero.getString("name");
                                    String power = hero.getString("power");
                                    System.out.println(i+" Name: "+name +" Power : "+power);
                                    HashMap<String,String> data = new HashMap<>();
                                    data.put("name",name);
                                    data.put("power",power);
                                    arrayList.add(data);
                                    ListAdapter adapter = new SimpleAdapter(MainActivity.this,arrayList,R.layout.listview_layout
                                            ,new String[]{"name","power"},new int[]{R.id.name,R.id.power});
                                    lv.setAdapter(adapter);
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }
}