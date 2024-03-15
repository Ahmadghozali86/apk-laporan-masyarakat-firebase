package com.example.civilreportapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FragmentLaporan fragmentLaporan;
    FragmentView fragmentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentLaporan = new FragmentLaporan();
        fragmentView = new FragmentView();
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_laporan){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentLaporan).commit();
                }
                if(item.getItemId()==R.id.menu_view){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentView).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_laporan);
    }
}