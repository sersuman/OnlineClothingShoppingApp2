package com.example.onlineclothingshoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import api.UserAPI;
import model.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class Register extends AppCompatActivity {
    private EditText etFirstName, etSecondName, etUsername, etPassword;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = findViewById(R.id.etFirstName);
        etSecondName = findViewById(R.id.etSecondName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString();
        String lastName = etSecondName.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        Users users = new Users(firstName,lastName,username,password);

        UserAPI userAPI = Url.getInstance().create(UserAPI.class);
        Call<Void> voidCall = userAPI.addUser(users);

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(Register.this, "Code" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(Register.this, "Successfuly registered", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Register.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });



    }
}
