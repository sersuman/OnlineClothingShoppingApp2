package com.example.onlineclothingshoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ItemDescriptionActivity extends AppCompatActivity {

    private ImageView ivDesProfile;
    private TextView tvDesItemName, tvDesItemPrice, tvDesItemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);
        ivDesProfile = findViewById(R.id.ivDesProfile);
        tvDesItemName = findViewById(R.id.tvDesItemName);
        tvDesItemPrice = findViewById(R.id.tvDesItemPrice);
        tvDesItemDescription = findViewById(R.id.tvDesItemDescription);

        StrictMode();
        URL url = null;
        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            ivDesProfile.setImageResource(bundle.getInt("image"));
            try {
                url = new URL("http://10.0.2.2:8000/uploads/"+bundle.getString("image"));
                ivDesProfile.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            tvDesItemName.setText(bundle.getString("name"));
            tvDesItemPrice.setText(bundle.getString("price"));
            tvDesItemDescription.setText(bundle.getString("description"));
        }
    }
    private void StrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
