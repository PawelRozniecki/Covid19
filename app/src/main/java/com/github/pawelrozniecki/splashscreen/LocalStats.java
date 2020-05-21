package com.github.pawelrozniecki.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocalStats extends AppCompatActivity {
    String url = "https://api.covid19api.com/country/poland?from=2020-03-01";
    int differenceConfirmed, differenceDeaths, differenceRecovered;
    String cLoc;
    String cLocYesterday;
    TextView confirmedText, deathText, recoveredText, diffConfirmed, diffDeath, diffRecovered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_stats);

        confirmedText = findViewById(R.id.confirmedNumber);
        deathText = findViewById(R.id.deathsNumber);
        recoveredText = findViewById(R.id.recoveredNumber);

        diffConfirmed = findViewById(R.id.confirmedDifference);
        diffDeath = findViewById(R.id.deathsDifference);
        diffRecovered = findViewById(R.id.recoveredDifference);

        loadJson();
    }

    public void loadJson() {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        @SuppressLint("SetTextI18n") JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        response -> {

                            try {

                                final JSONArray jArray = new JSONArray(response.toString());
                                Log.v("array", jArray.toString());

                                JSONObject object = jArray.getJSONObject(jArray.length() - 2);
                                JSONObject secondObject = jArray.getJSONObject(jArray.length() - 1);

                                String confirmedLocalToday = secondObject.getString("Confirmed");
                                String deathLocalToday = secondObject.getString("Deaths");
                                String recoveredLocalToday = secondObject.getString("Recovered");

                                confirmedText.setText(confirmedLocalToday);
                                deathText.setText(deathLocalToday);
                                recoveredText.setText(recoveredLocalToday);


                                String confirmedYesterday = object.getString("Confirmed");
                                String deathLocalYesterday = object.getString("Deaths");
                                String recoveredLocalYesterday = object.getString("Recovered");


                                //calculating differences

                                differenceConfirmed = Integer.parseInt(confirmedLocalToday) - Integer.parseInt(confirmedYesterday);
                                differenceDeaths = Integer.parseInt(deathLocalToday) - Integer.parseInt(deathLocalYesterday);
                                differenceRecovered = Integer.parseInt(recoveredLocalToday) - Integer.parseInt(recoveredLocalYesterday);

                                diffConfirmed.setText("+" + Integer.toString(differenceConfirmed));
                                diffRecovered.setText("+" + Integer.toString(differenceRecovered));
                                diffDeath.setText("+" + Integer.toString(differenceDeaths));


                                Log.v("diff con", Integer.toString(differenceConfirmed));
                                Log.v("diff death", Integer.toString(differenceDeaths));
                                Log.v("diff recovered", Integer.toString(differenceRecovered));

                            } catch (JSONException e) {
                                Log.e("error marker", e.toString());
                            }
                        }, error -> Log.e("error", error.toString()));

        queue.add(jsObjRequest);


    }
}
