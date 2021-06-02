package com.example.tipsy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.overlay.Marker;

import java.io.IOException;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,Overlay.OnClickListener{
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
        setContentView(R.layout.activity_map);
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
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        Marker marker1 = new Marker();
        marker1.setPosition(new LatLng(37.45478351626994, 127.12869252935397));
        marker1.setMap(naverMap);

        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng( 37.448716926560486, 127.12722228105848));
        marker2.setMap(naverMap);

        Marker marker3 = new Marker();
        marker3.setPosition(new LatLng( 37.4488809522522, 127.12711288891323));
        marker3.setMap(naverMap);

        Marker marker4 = new Marker();
        marker4.setPosition(new LatLng( 37.447797506247035, 127.1274278177489));
        marker4.setMap(naverMap);

        Marker marker5 = new Marker();
        marker5.setPosition(new LatLng( 37.4556908622406, 127.12712493877979));
        marker5.setMap(naverMap);

        Marker marker6 = new Marker();
        marker6.setPosition(new LatLng( 37.44582663831137, 127.13260851774889));
        marker6.setMap(naverMap);

        Marker marker7 = new Marker();
        marker7.setPosition(new LatLng( 37.45621814211817, 127.12767885333109));
        marker7.setMap(naverMap);

        Marker marker8 = new Marker();
        marker8.setPosition(new LatLng( 37.45865728946744, 127.12647338609044));
        marker8.setMap(naverMap);

        Marker marker9 = new Marker();
        marker9.setPosition(new LatLng( 37.45824714980705, 127.12652647253495));
        marker9.setMap(naverMap);

        Marker marker10 = new Marker();
        marker10.setPosition(new LatLng( 37.45840469202917, 127.12691538924386));
        marker10.setMap(naverMap);

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
                View view = View.inflate(MapActivity.this, R.layout.view_info_window, null);
                ((TextView)view.findViewById(R.id.title)).setText(str);
                ((TextView)view.findViewById(R.id.details)).setText("Info Window ");
                return view;
            }
        });
    }



    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {
                mInfoWindow.close();
                Toast.makeText(this.getApplicationContext(), "InfoWindow Close.", Toast.LENGTH_LONG).show();
            }
            else {
                mInfoWindow.open(marker);
                Toast.makeText(this.getApplicationContext(), "InfoWindow Open.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return false;
    }

}
