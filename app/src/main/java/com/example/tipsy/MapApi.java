package com.example.tipsy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import ted.gun0912.clustering.naver.TedNaverClustering;
import ted.gun0912.clustering.naver.demo.NaverItem;

public class MapApi extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempmap);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap){
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        //naver map 객체 받아서 NaverMap 객체에 위치 소스 지정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);//나침반
        uiSettings.setScaleBarEnabled(true);//거리
        uiSettings.setZoomControlEnabled(true);//줌
        uiSettings.setLocationButtonEnabled(true);//내가 있는 곳

        naverMap.moveCamera(
                CameraUpdate.toCameraPosition(
                        new CameraPosition(NaverMap.DEFAULT_CAMERA_POSITION.target, NaverMap.DEFAULT_CAMERA_POSITION.zoom))
        );

        TedNaverClustering.with(this, naverMap)
                .items(getItems())
                .make();

    }



    private List<NaverItem> getItems() {
        LatLngBounds bounds = naverMap.getContentBounds();
        ArrayList<NaverItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            NaverItem temp = new NaverItem((bounds.getNorthLatitude() - bounds.getSouthLatitude()) * Math.random() + bounds.getSouthLatitude(),
                    (bounds.getEastLongitude() - bounds.getWestLongitude()) * Math.random() + bounds.getWestLongitude()
            );
            items.add(temp);
        }
        return items;

    }
}
