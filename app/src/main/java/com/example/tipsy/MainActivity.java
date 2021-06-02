package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity {

    private community community;
    private ListView listview;
    ViewFlipper viewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate : Do when created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button roomButton = findViewById(R.id.roombutton);
        Button communityButton = findViewById(R.id.communitybutton);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        viewFlipper.startFlipping();
        viewFlipper.setFlipInterval(4000);
        roomButton.setOnClickListener(new View.OnClickListener(){ // sets clicklistener on Room making button.
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        communityButton.setOnClickListener(new View.OnClickListener(){ // sets clicklistener on annomyous chatting button.
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, AChatActivity.class);
                intent.putExtra("chatName", "anonymous".toString());
                intent.putExtra("userName", "익명".toString());
                startActivity(intent);
            }
        });
        //community button
        Button btn3=findViewById(R.id.community);
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

            case R.id.map:
                Toast.makeText(this, "You entered Map activity.", Toast.LENGTH_LONG).show();

                Intent intentm = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intentm);
                break;
            case R.id.Logout:
                Toast.makeText(this, "You entered Logout activity.", Toast.LENGTH_LONG).show();

                Intent intentl = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intentl);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
