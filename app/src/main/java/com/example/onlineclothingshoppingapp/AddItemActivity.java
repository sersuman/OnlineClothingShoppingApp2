package com.example.onlineclothingshoppingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import api.UserAPI;
import model.ImageResponse;
import model.Items;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import url.Url;

public class AddItemActivity extends AppCompatActivity {
    private EditText etItemtName, etItemPrice, etItemDescription;
    private Button btnSave;
    private ImageView imgProfile;
    private String imageName;
    String imagepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemtName = findViewById(R.id.etItemName);
        etItemPrice = findViewById(R.id.etItemPrice);
        etItemDescription = findViewById(R.id.etItemDescription);
        btnSave = findViewById(R.id.btnSave);
        imgProfile = findViewById(R.id.imgProfile);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                Intent intent = new Intent(AddItemActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK); //to browse image
        intent.setType("image/*"); //user now can only select the image
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data==null){
                Toast.makeText(this,"Please Select an image",Toast.LENGTH_LONG).show();
            }
        }
        Uri uri = data.getData();
        imagepath = getRealPathFromUri(uri);
        previewImage(imagepath); //after getting imagepath display it in imageview
    }



    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();;
        return result;
    }


    private void previewImage(String imagepath) {
        File imgFlie = new File(imagepath);
        if(imgFlie.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFlie.getAbsolutePath());
            imgProfile.setImageBitmap(myBitmap);

        }
    }

    private void Save() {
        SaveImageOnly();
        String itemName = etItemtName.getText().toString();
        String itemPrice = etItemPrice.getText().toString();

        String itemDescription = etItemDescription.getText().toString();


        Items items = new Items(itemName,itemPrice,imageName,itemDescription);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAPI onlineClothingAPI = retrofit.create(UserAPI.class);

        Call<Void> itemsCall = onlineClothingAPI.addItem(items);

        itemsCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){

                    Toast.makeText(AddItemActivity.this,"Code" + response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(AddItemActivity.this, "Successfully Added",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddItemActivity.this,"Error " + t.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }

    private void SaveImageOnly() {
        File file = new File(imagepath);
        Toast.makeText(this,""+file,Toast.LENGTH_LONG).show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",file.getName(),requestBody);

        UserAPI onlineClothingAPI = Url.getInstance().create(UserAPI.class);
        Call<ImageResponse> responseBodyCall = onlineClothingAPI.uploadImage(body);

        StrictMode();

        try {
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            //After saving an image, retrieve the current name of the image
            imageName = imageResponseResponse.body().getFilename();
            Toast.makeText(this,""+imageName,Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void StrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


}
