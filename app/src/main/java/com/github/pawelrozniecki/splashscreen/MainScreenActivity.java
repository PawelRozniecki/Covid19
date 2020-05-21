package com.github.pawelrozniecki.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DownloadManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


public class MainScreenActivity extends AppCompatActivity {
    TextView title;
    BottomNavigationView bottomNavigationView;

    //API KEY adda19627a474e23aa5bd9ffc486c3de

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        title = findViewById(R.id.title);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        openFragment(Dashboard.newInstance("", ""));

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()) {
                case R.id.menu_btn:
                    openFragment(Dashboard.newInstance("", ""));
                    title.setText("COVID-19");
                    return true;
                case R.id.profile:
                    openFragment(profile_fragment.newInstance("", ""));
                    title.setText("Contact");

                    return true;
            }
            return false;

        }
    };

}
