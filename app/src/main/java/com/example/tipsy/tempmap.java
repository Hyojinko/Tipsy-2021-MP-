package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class tempmap extends AppCompatActivity
{

    Button backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempmap);


        backbutton = findViewById(R.id.fcbackbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "돌아가기버튼이 눌렸어요", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                Toast.makeText(this, "설정 메뉴가 선택 됨 / 향후 구현예정", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
