package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    event_1 event1;
    private community community;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        event1= new event_1();
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
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        communityButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, AChatActivity.class);
                intent.putExtra("chatName", "익명 채널".toString());
                intent.putExtra("userName", "".toString());
                startActivity(intent);
            }
        });
        //임시
        Button btn3=findViewById(R.id.임시버튼);
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), HomeCommunity.class);
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

            case R.id.temp_map:
                Toast.makeText(this, "Map on", Toast.LENGTH_LONG).show();

                Intent intentm = new Intent(getApplicationContext(),MapApi.class);
                startActivity(intentm);
                break;
            case R.id.Logout:
                Toast.makeText(this, "Map on", Toast.LENGTH_LONG).show();

                Intent intentl = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intentl);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
