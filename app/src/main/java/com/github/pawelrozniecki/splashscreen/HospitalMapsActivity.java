package com.github.pawelrozniecki.splashscreen;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HospitalMapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private FusedLocationProviderClient fusedLocationProvider;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_maps);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);


        fusedLocationProvider.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            loadNearbyPlaces(location.getLatitude(), location.getLongitude());
                        }
                    }
                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        checkPermission();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            googleMap.setMyLocationEnabled(true);

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    public void loadNearbyPlaces(double latitude, double longitude) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append("20000");
        googlePlacesUrl.append("&types=").append("hospital");
        googlePlacesUrl.append("&key=" + "AIzaSyDsWKa6NcUF6_D7fLucvVN236zcI9g1r-o");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET,googlePlacesUrl.toString(),null,
                        response -> {

                            try{

                                JSONArray jsonArray = response.getJSONArray("results");

                                for(int i=0;i<jsonArray.length();i++) {

                                    JSONObject tableData = jsonArray.getJSONObject(i);
                                    JSONObject loc = tableData.getJSONObject("geometry")
                                            .getJSONObject("location");

                                    mMap.addMarker(new MarkerOptions().title(tableData.getString("name")).position(new LatLng(Double.parseDouble(loc.getString("lat")),
                                            Double.parseDouble(loc.getString("lng")))).snippet(tableData.getString("vicinity")));
                                }

                                }catch (JSONException e){
                                Log.e("error marker", e.toString());
                            }
                        }, error -> Log.e("error" , error.toString()));

        queue.add(jsObjRequest);



    }

}