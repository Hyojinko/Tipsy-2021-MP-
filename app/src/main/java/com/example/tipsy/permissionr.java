
// can't be done: because of NPE
package com.example.tipsy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class permissionr {

    private Context context;
    private Activity activity;

    private String[] permissions={
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    }   ; // 현재 요청 권한 :파일 쓰기, 읽기, 오디오, 위치
    private List permissionList;
    private String permitStr;
    private final int PList= 1023;

     permissionr(Activity tActivity, Context tContext)
    {
        this.activity=tActivity;
        this.context=tContext;
    }

    public boolean checkPermit(){
        int result;
        permissionList=new ArrayList<>();

        for(String pm : permissions) {
            result = ContextCompat.checkSelfPermission(context,pm);
            if(result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if(!permissionList.isEmpty())
        {
            return false;
        }

        return true;
    }
    public void requestPermission(){
        ActivityCompat.requestPermissions(activity, (String[]) permissionList.toArray(new  String[permissionList.size()]),PList);
    }
    public boolean permissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode==PList&&(grantResults.length >0)){
            for(int i=0; i<grantResults.length; i++)
            {
                if(grantResults[i]==-1)
                {
                    return false;
                }
            }

        }
        return true;
    }
}
