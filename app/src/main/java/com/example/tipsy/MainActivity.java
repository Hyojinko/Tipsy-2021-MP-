package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class MainActivity extends AppCompatActivity {

    event_1 event1;
    makingroom makingroom;
    private community community;
    private ListView listview;
   public permissionr permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        event1= new event_1();
        makingroom = new makingroom();
        ImageButton eventButton = findViewById(R.id.eventbutton);
        Button roomButton = findViewById(R.id.roombutton);
        Button communityButton = findViewById(R.id.communitybutton);
        eventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), event_1.class);
                startActivity(intent);
            }
        });
        roomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), makingroom.class);
                startActivity(intent);
            }
        });
        communityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), community.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId){
            case R.id.menu_toaccount:
                Toast.makeText(this, "계정 메뉴로 갑니다.", Toast.LENGTH_LONG).show();
                Intent intenta = new Intent(getApplicationContext(),account.class);
                startActivity(intenta);
                break;
            case R.id.menu_search:
                Toast.makeText(this, "친구창 메뉴로 갑니다. ", Toast.LENGTH_LONG).show();
                Intent intentf = new Intent(getApplicationContext(),friendset.class);
                startActivity(intentf);
                break;
            case R.id.menu_friend:
                Intent intentl = new Intent(getApplicationContext(),Login.class);
                startActivity(intentl);
                break;

            case R.id.opus_test:
                Toast.makeText(this, "OPUS module On ", Toast.LENGTH_LONG).show();

                Intent intento = new Intent(getApplicationContext(),tempmap.class);
                startActivity(intento);
                break;

            case R.id.temp_map:
                Toast.makeText(this, "Map on", Toast.LENGTH_LONG).show();

                Intent intentm = new Intent(getApplicationContext(),MapApi.class);
                startActivity(intentm);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
