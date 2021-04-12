package com.example.tipsy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
        // 멀티 퍼미션 지정//

    Button lgnbtn;

              private String[] permissions = {
                      Manifest.permission.RECORD_AUDIO,
                      Manifest.permission.ACCESS_FINE_LOCATION,
                      Manifest.permission.READ_EXTERNAL_STORAGE,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        private static final int MULTIPLE_PERMISSIONS = 101;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
                lgnbtn=findViewById(R.id.lgnBtn);
                if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions();
            }
            lgnbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),
                            "임시 - 돌아가기버튼이 눌렸어요", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        private boolean checkPermissions() {
            int result;
            List<String> permissionList = new ArrayList<>();
            for (String pm : permissions) {
                result = ContextCompat.checkSelfPermission(this, pm);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(pm);
                }
            }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case MULTIPLE_PERMISSIONS: {
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++) {
                            if (permissions[i].equals(this.permissions[i])) {
                                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                    showToast_PermissionDeny();
                                }
                            }
                        }
                    } else {
                        showToast_PermissionDeny();
                    }
                    return;
                }
            }

        }

        private void showToast_PermissionDeny() {
            Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
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
                Toast.makeText(this, "로그인 메뉴로 대체.", Toast.LENGTH_LONG).show();
                Intent intentl = new Intent(getApplicationContext(),Login.class);
                startActivity(intentl);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}