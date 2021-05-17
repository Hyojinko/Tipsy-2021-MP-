package com.example.tipsy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.overlay.Marker;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MapApi extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Geocoder geocoder;
    private InfoWindow mInfoWindow;
    EditText searchPlace;
    Button Button;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempmap);
        searchPlace = findViewById(R.id.searchPlace);
        Button = findViewById(R.id.button);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap){
        this.naverMap = naverMap;
        geocoder = new Geocoder(this);
        naverMap.setLocationSource(locationSource);
        naverMap.getUiSettings().setLocationButtonEnabled(true);
        LatLng initialPosition = new LatLng(37.506855,127.066242);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);
        markersPosition = new Vector<LatLng>();
        for(int x= 0;x<100;++x){
            for(int y = 0;y < 100; ++y){
                markersPosition.add(new LatLng(initialPosition.latitude-(REFERANCE_LAT*x),initialPosition.longitude+(REFERANCE_LNG*y)));
                markersPosition.add(new LatLng(initialPosition.latitude+(REFERANCE_LAT*x),initialPosition.longitude-(REFERANCE_LNG*y)));
                markersPosition.add(new LatLng(initialPosition.latitude+(REFERANCE_LAT*x),initialPosition.longitude+(REFERANCE_LNG*y)));
                markersPosition.add(new LatLng(initialPosition.latitude-(REFERANCE_LAT*x),initialPosition.longitude-(REFERANCE_LNG*y)));
            }
        }
        Marker marker = new Marker();
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener(){
            @Override
            public void onCameraChange(int reason, boolean animated){
                freeActiveMarkers();
                LatLng currentPosition = getCurrentPosition(naverMap);
                for(LatLng markerPosition: markersPosition){
                    if(!withinSightMarker(currentPosition, markerPosition))
                        continue;
                    marker.setPosition(markerPosition);
                    marker.setMap(naverMap);
                    activeMarkers.add(marker);
                }
            }
        });
        Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = searchPlace.getText().toString();
                List<Address> addressList = null;
                try{
                    addressList = geocoder.getFromLocationName(str,10);
                }catch(IOException e){
                    e.printStackTrace();
                }
                try {

                    System.out.println(addressList.get(0).toString());
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\\")+1,splitStr[0].length()-2);
                    System.out.println(address);
                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=")+1);
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=")+1);
                    System.out.println(latitude);
                    System.out.println(longitude);
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    Marker marker = new Marker();
                    marker.setPosition(point);
                    marker.setMap(naverMap);
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(point);
                    naverMap.moveCamera(cameraUpdate);
                } catch (IndexOutOfBoundsException e)
                {

                } catch (NumberFormatException e)
                {

                }
            }
        });
        mInfoWindow = new InfoWindow();
        mInfoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this){
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow){
                Marker marker = infoWindow.getMarker();
                PlaceInfo info = (PlaceInfo) marker.getTag();
                String str = searchPlace.getText().toString();
                View view = View.inflate(MapApi.this, R.layout.view_info_window, null);
                ((TextView)view.findViewById(R.id.title)).setText(str);
                ((TextView)view.findViewById(R.id.details)).setText("Info Window ");
                return view;
            }
        });
    }

    private Vector<LatLng> markersPosition;
    private Vector<Marker> activeMarkers;
    public LatLng getCurrentPosition(NaverMap naverMap){
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        return new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }
    public final static double REFERANCE_LAT = 1/109.9584;
    public final static double REFERANCE_LNG=1/88.74;
    public final static double REFERANCE_LAT_X3 = 3/109.958489;
    public final static double REFERANCE_LNG_X3 = 3/88.74;
    public boolean withinSightMarker(LatLng currentPosition, LatLng markerPosition){
        boolean withinSightMarkerLat = Math.abs(currentPosition.latitude-markerPosition.latitude)<=REFERANCE_LAT_X3;
        boolean withinSightMarkerLng = Math.abs(currentPosition.longitude-markerPosition.longitude) <= REFERANCE_LNG_X3;
        return withinSightMarkerLat && withinSightMarkerLng;
    }
    private void freeActiveMarkers(){
        if(activeMarkers==null){
            activeMarkers = new Vector<Marker>();
            return;
        }
        for (Marker activeMarker:activeMarkers){
            activeMarker.setMap(null);
        }
        activeMarkers=new Vector<Marker>();
    }
}
