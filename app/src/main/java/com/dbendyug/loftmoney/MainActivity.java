package com.dbendyug.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "appPreferences";
    public static final String AUTH_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView helloWorldView = findViewById(R.id.hello_world);
        helloWorldView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BudgetActivity.class));
            }
        });


        LoftApp loftApp = (LoftApp) getApplication();

        LoftApi loftApi = loftApp.getLoftApi();

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        Call<AuthResponse> authCall = loftApi.auth(androidId);
        authCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AUTH_TOKEN, response.body().getAuthToken());
                editor.apply();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }
}

