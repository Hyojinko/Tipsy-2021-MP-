package com.example.tipsy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MapFrame extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(saved InstanceState);

        naverMapBasicSettings();
    }

    public void naverMapBasicSettings() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {

        // 지도 유형 위성사진으로 설정
        naverMap.setMapType(NaverMap.MapType.Basic);

    }
}




      
