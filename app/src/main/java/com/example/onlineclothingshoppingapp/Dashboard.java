package com.example.onlineclothingshoppingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.ItemsAdapter;
import api.UserAPI;
import model.Items;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class Dashboard extends AppCompatActivity {
    private Button btnAddItem;
    private RecyclerView rvItems;
    List<Items> itemsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnAddItem = findViewById(R.id.btnAddItem);
        rvItems = findViewById(R.id.rvItems);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AddItemActivity.class);
                startActivity(intent);
                finish();
            }
        });

        UserAPI userAPI = Url.getInstance().create(UserAPI.class);

        Call<List<Items>> listCall = userAPI.getAllItems();

        listCall.enqueue(new Callback<List<Items>>() {
            @Override
            public void onResponse(Call<List<Items>> call, Response<List<Items>> response) {
                itemsList = response.body();
                ItemsAdapter itemsAdapter = new ItemsAdapter(Dashboard.this,itemsList);
                rvItems.setAdapter(itemsAdapter);
                rvItems.setLayoutManager(new LinearLayoutManager(Dashboard.this));

            }

            @Override
            public void onFailure(Call<List<Items>> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
