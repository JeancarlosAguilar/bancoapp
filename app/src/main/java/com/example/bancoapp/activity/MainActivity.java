package com.example.bancoapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bancoapp.R;
import com.example.bancoapp.activity.menu.MenuInicioActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MenuInicioActivity.class);
            startActivity(intent);
            finish();
        }, 5500);

    }
}