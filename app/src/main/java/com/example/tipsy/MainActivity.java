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
    ViewFlipper v_fllipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate : Do when created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button roomButton = findViewById(R.id.roombutton);
        Button communityButton = findViewById(R.id.communitybutton);
        int images[] = {
                R.drawable.event_img1,
                R.drawable.event_img2,
                R.drawable.event_img3
        };
        v_fllipper = findViewById(R.id.image_slide);
        for(int image:images){
            fllipperImages(image);
        }

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
    public void fllipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        v_fllipper.setFlipInterval(4000);
        v_fllipper.setAutoStart(true);

        //animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this, android.R.anim.slide_out_right);
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
