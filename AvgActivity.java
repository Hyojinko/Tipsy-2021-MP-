package com.example.drinkcheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class AvgActivity extends AppCompatActivity {

    int beerCnt;
    int sojuCnt;
    int wineCnt;
    TextView targetMonth, tvdaySum, beerAvg, sojuAvg, wineAvg;
    int daySum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinkday);

        //calendarview
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());

        OneDayDecorator oneDayDecorator = new OneDayDecorator();
        calendarView.addDecorators(oneDayDecorator);

        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));
        calendarView.addDecorators(new SaturdayDecorator());
        calendarView.addDecorators(new SundayDecorator());


        targetMonth = (TextView)findViewById(R.id.targetMonth);
        tvdaySum = (TextView) findViewById(R.id.daySum);
        beerAvg = (TextView) findViewById(R.id.beerAvg);
        sojuAvg = (TextView) findViewById(R.id.sojuAvg);
        wineAvg = (TextView) findViewById(R.id.wineAvg);


        Intent intent = getIntent();
        String month = intent.getExtras().getString("month");
        String dayfileName = "tipsy" + month + ".txt";
        String set = month + " 월 평균";
        targetMonth.setText(set);

        daySum = 0;
        try {
            FileInputStream fis = openFileInput(dayfileName);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String str = buffer.readLine();
            daySum++;

            while (str != null) {
                str = buffer.readLine();
                daySum++;
            }
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        beerCnt = 0;
        sojuCnt = 0;
        wineCnt = 0;
        String fileName;
        String si;
        for(int i=1; i<32; i++){
            if(i<10){
                si = "0" + i;
            }
            else si = String.valueOf(i);
            fileName = "tipsy" + "2021" + month + si + ".txt";
            Log.i("file", fileName);

            try {
                FileInputStream fis2 = openFileInput(fileName);
                BufferedReader buffer2 = new BufferedReader(new InputStreamReader(fis2));

                Log.i("test", "test1");
                String str2 = buffer2.readLine();
                int loop = 0;
                while(str2 != null){
                    if(loop == 0) beerCnt += Integer.parseInt(str2);
                    else if(loop == 1) sojuCnt += Integer.parseInt(str2);
                    else if(loop == 2) wineCnt += Integer.parseInt(str2);
                    Log.i("test", str2);
                    str2 = buffer2.readLine();
                    loop++;
                }

                buffer2.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        String ds = String.valueOf(daySum-1);
        String bcnt = String.valueOf(beerCnt);
        String scnt = String.valueOf(sojuCnt);
        String wcnt = String.valueOf(wineCnt);

        Log.i("test",bcnt);
        Log.i("test",scnt);
        Log.i("test",wcnt);


        tvdaySum.setText(ds);
        beerAvg.setText(bcnt);
        sojuAvg.setText(scnt);
        wineAvg.setText(wcnt);

    }
}
