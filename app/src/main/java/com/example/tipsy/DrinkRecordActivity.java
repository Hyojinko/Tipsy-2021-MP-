package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;


public class DrinkRecordActivity extends AppCompatActivity {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    public String fileName;
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drinkrecordactivity);

        this.InitializeView();
        this.InitializeListener();
    }

    //select date
    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.textView);
    }
    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                month = Integer.toString(monthOfYear+1);
                String day = Integer.toString(dayOfMonth);

                if(monthOfYear+1<=10){
                    month = "0"+month;
                }
                if(dayOfMonth<10){
                    day = "0"+day;
                }

                EditText beer = (EditText)findViewById(R.id.checkBeer);
                EditText soju = (EditText)findViewById(R.id.checkSoju);
                EditText wine = (EditText)findViewById(R.id.checkWine);

                //enter the number of drinks glass
                textView_Date.setText(year + "/" + month + "/" + day);
                fileName = "tipsy" + year + month + day + ".txt";
                String monthFile = "tipsy" + month + ".txt";

                //create time
                try{
                    String fName;
                    String si;
                    for(int i=1; i<32; i++){
                        if(i<10) si = "0" + i;
                        else si = String.valueOf(i);
                        fName = "tipsy" + year + month + si + ".txt";
                        FileOutputStream f = openFileOutput(fName, Context.MODE_APPEND);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

                //enter contents in file
                try {
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    FileOutputStream fos2 = openFileOutput(monthFile, Context.MODE_APPEND);
                    PrintWriter out = new PrintWriter(fos, false);
                    PrintWriter out2 = new PrintWriter(fos2, false);

                    // case if user don't insert no any value
                    if(beer.getText().length()==0 )
                    {
                        beer.setText("0");
                    }
                    if(soju.getText().length()==0)
                    {
                        soju.setText("0");
                    }
                    if(wine.getText().length()==0)
                    {
                        wine.setText("0");
                    }
                    out.println(beer.getText());
                    out.println(soju.getText());
                    out.println(wine.getText());
                    out.close();

                    FileInputStream fis = openFileInput(monthFile);//파일명
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
                    String str = buffer.readLine();
                    boolean non = true;
                    while(str != null){
                        if(str.equals(day)){
                            non = false;
                        }
                        str = buffer.readLine();
                    }
                    if(non){
                        out2.println(day);
                    }

                    buffer.close();
                    out2.close();

                } catch (FileNotFoundException e) {
                    System.out.println("FILE error");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("IO error");
                    System.exit(0);
                }

                Log.v("Files",getFileStreamPath(fileName).exists()+"");
                Log.v("Files", String.valueOf(getFileStreamPath(fileName)));
                Log.v("Files",getFileStreamPath(fileName).isDirectory()+"");
                Log.v("Files",getFileStreamPath(fileName).listFiles()+"");
                Log.v("Files",getFileStreamPath(fileName).getName()+" !");
                Log.v("Files", Environment.getExternalStorageDirectory().getPath()+"");


            }
        };

        //click '날짜입력' button
        Button btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog dialog = new DatePickerDialog(DrinkRecordActivity.this, callbackMethod, 2021,1,1);
                dialog.show();
            }
        });

        //click '저장' button
        Button bLoad = (Button) findViewById(R.id.btnSubmit);
        bLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 파일의 내용을 읽어서 TextView 에 보여주기

                Log.v("Files","Hola"+"");

                try {
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    PrintWriter out = new PrintWriter(fos, false);

                    EditText beer = (EditText)findViewById(R.id.checkBeer);
                    EditText soju = (EditText)findViewById(R.id.checkSoju);
                    EditText wine = (EditText)findViewById(R.id.checkWine);

                    out.println(beer.getText());
                    out.println(soju.getText());
                    out.println(wine.getText());
                    out.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //click '이번달 평균' button
        Button btngoToavg = (Button)findViewById(R.id.btngoToavg);
        btngoToavg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AvgActivity.class);
                intent.putExtra("month", month);
                startActivity(intent);
            }
        });

    }
}
