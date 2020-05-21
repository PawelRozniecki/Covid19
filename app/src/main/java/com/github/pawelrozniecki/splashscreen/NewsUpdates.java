package com.github.pawelrozniecki.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class NewsUpdates extends AppCompatActivity {
   public String link  = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=adda19627a474e23aa5bd9ffc486c3de";
    private  RecyclerViewerAdapter adapter;
    private RecyclerView recyclerView;
    public ArrayList<String>news, imageLinks;

    CardView card, cardLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_updates);
        recyclerView = findViewById(R.id.recyclerViewer);
        news = new ArrayList<>();
        card = findViewById(R.id.global);

        cardLocal = findViewById(R.id.local);
        loadJson();

        Log.v("populate", Integer.toString(news.size()));

        card.setOnClickListener(v -> {

            Intent intent = new Intent(NewsUpdates.this,GlobalStats.class);
            startActivity(intent);
        });

        cardLocal.setOnClickListener(v -> {

            Intent intent = new Intent(NewsUpdates.this,LocalStats.class);
            startActivity(intent);
        });

    }

    public ArrayList<String> loadJson(){
        ArrayList<String> titles =new ArrayList<>();
        ArrayList<String> images =new ArrayList<>();
        ArrayList<String> description =new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET,link,null,
                        response -> {


                            try{

                                JSONArray jsonArray = response.getJSONArray("articles");

                                for(int i=0;i<jsonArray.length();i++) {

                                 JSONObject tableData = jsonArray.getJSONObject(i);
                                 Log.v("object", tableData.getString("title"));
                                 String t = tableData.get("title").toString();
                                 String url = tableData.get("urlToImage").toString();
                                 String desc = tableData.get("description").toString();

                                 titles.add(t);
                                 images.add(url);
                                 description.add(desc);

                                 Log.v("adding", titles.get(i).toString());

                                }

                                adapter = new RecyclerViewerAdapter(titles,images,description);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(this));


                            }catch (JSONException e){
                                Log.e("error marker", e.toString());
                            }
                        }, error -> Log.e("error" , error.toString()));

        queue.add(jsObjRequest);

        return titles;
    }


}
