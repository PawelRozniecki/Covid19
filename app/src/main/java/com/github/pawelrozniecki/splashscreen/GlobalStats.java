package com.github.pawelrozniecki.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlobalStats extends AppCompatActivity {
String url = "https://api.covid19api.com/world/total";
TextView world, deaths, recovered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_stats);
        world = findViewById(R.id.world);
        deaths = findViewById(R.id.deaths);
        recovered = findViewById(R.id.recovered);
        loadJson();
    }

    public void loadJson(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET,url,null,
                        response -> {
                    Log.v("response", response.toString());

                    String jsonString = response.toString();
                            Log.v("jsonString", jsonString);


                            try{

                                JSONObject object = new JSONObject(jsonString);

                                String w = object.getString("TotalConfirmed");
                                String d = object.getString("TotalDeaths");
                                String r = object.getString("TotalRecovered");

                                world.setText(w);
                                deaths.setText(d);
                                recovered.setText(r);


                            }catch (JSONException e){
                                Log.e("error marker", e.toString());
                            }
                        }, error -> Log.e("error" , error.toString()));

        queue.add(jsObjRequest);


    }
}
