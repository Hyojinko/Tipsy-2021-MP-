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

    private EditText checkupPassword;
    //define firebase object
    FirebaseAuth firebaseAuth;


    //Login with Naver ID
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
        Log.i("LoginData","watch me ");

        if (firebaseAuth.getCurrentUser() != null) {
            //If log-in, then open pro
            Log.i("LoginData","MORE ");


            startActivity(new Intent(getApplicationContext(), MainActivity.class)); //추가해 줄 ProfileActivity
            finish();
            //그리고 profile 액티비티를 연다.
                   }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewSingin = (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textViewMessage);
        buttonSignup = (Button) findViewById(R.id.btnSignup);
        progressDialog = new ProgressDialog(this);
        checkupPassword = findViewById(R.id.checkTextPassword);
        //button click event
        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);


        //Login with Naver ID
        mContext = getApplicationContext();

        ll_naver_login = findViewById(R.id.ll_naver_login);
        btn_logout = findViewById(R.id.btn_logout);

        ll_naver_login.setOnClickListener(new View.OnClickListener() { // naver login listener button's listener
            @Override
            public void onClick(View v) {
                mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(
                        mContext
                        ,getString(R.string.naver_client_id)
                        ,getString(R.string.naver_client_secret)
                        ,getString(R.string.naver_client_name)
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
        // gets email and passwords user input.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String checkPassword = checkupPassword.getText().toString().trim();

        //check if email and pw is correct or not.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        return;
        }
        if(TextUtils.isEmpty((checkPassword))){
            Toast.makeText(this, "check Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(checkPassword))
        {
            Toast.makeText(this, "check password가 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
        return;
        }

        //progressed if email and pw are correct.
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
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일이 있는 경우  \n -6자리 미만의 암호 \n - 서버 에러");
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