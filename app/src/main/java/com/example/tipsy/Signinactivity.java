package com.example.tipsy;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

public class Signinactivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignup;
    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;


    //네아로
    LinearLayout ll_naver_login;
    Button btn_logout;
    OAuthLogin mOAuthLoginModule;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)); //추가해 줄 ProfileActivity
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewSingin = (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textViewMessage);
        buttonSignup = (Button) findViewById(R.id.btnSignup);
        progressDialog = new ProgressDialog(this);

        //button click event
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);


        //네아로
        mContext = getApplicationContext();

        ll_naver_login = findViewById(R.id.ll_naver_login);
        btn_logout = findViewById(R.id.btn_logout);

        ll_naver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(
                        mContext
                        ,getString(R.string.naver_client_id)
                        ,getString(R.string.naver_client_secret)
                        ,getString(R.string.naver_client_name)
                        //,OAUTH_CALLBACK_INTENT
                        // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
                );

                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                            String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                            String tokenType = mOAuthLoginModule.getTokenType(mContext);

                            Log.i("LoginData","accessToken : "+ accessToken);
                            Log.i("LoginData","refreshToken : "+ refreshToken);
                            Log.i("LoginData","expiresAt : "+ expiresAt);
                            Log.i("LoginData","tokenType : "+ tokenType);

                        } else {
                            String errorCode = mOAuthLoginModule
                                    .getLastErrorCode(mContext).getCode();
                            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                            Toast.makeText(mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                        }
                    };
                };

                mOAuthLoginModule.startOauthLoginActivity(com.example.tipsy.Signinactivity.this, mOAuthLoginHandler);
            }
        });
    }

    //Firebse creating a new user
    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), SetupActivity.class));
                        } else {
                            //에러발생시
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(com.example.tipsy.Signinactivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    //button click event
    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            //TODO
            registerUser();
        }

        if (view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }
    }


}