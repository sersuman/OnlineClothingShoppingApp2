package com.example.onlineclothingshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import api.UserAPI;
import model.LoginSignupResponse;
import model.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class Login extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        etUsername = findViewById(R.id.etUsernameLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLoginLogin);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });
    }

    private void authenticate() {
    String username = etUsername.getText().toString();
    String password = etPassword.getText().toString();

        Users users = new Users(username,password);

        UserAPI userAPI = Url.getInstance().create(UserAPI.class);
        Call<LoginSignupResponse> loginCall = userAPI.checkUser(users);

        loginCall.enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(Login.this, "Code" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }else {
                    if (response.body().getSuccess()){
                        Intent intent = new Intent(Login.this,Dashboard.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Login.this, "Username and password donot match", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Code" + "Error"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
