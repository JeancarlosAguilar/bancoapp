package com.example.bancoapp.activity.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bancoapp.R;

public class MenuInicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
    }

    public void menuRegistro (View view){
        Intent intent = new Intent(MenuInicioActivity.this, MenuRegistroActivity.class);
        startActivity(intent);
        finish();
    }

    public void menuLogin (View view){
        Intent intent = new Intent(MenuInicioActivity.this, MenuLoguin1Activity.class);
        startActivity(intent);
        finish();
    }

}