package com.example.bancoapp.activity.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.databinding.ActivityMenuLoguin1Binding;

public class MenuLoguin1Activity extends AppCompatActivity {

    private ActivityMenuLoguin1Binding binding;
    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuLoguin1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);
    }

    public void volver(View view) {
        Intent intent = new Intent(MenuLoguin1Activity.this, MenuInicioActivity.class);
        startActivity(intent);
        finish();
    }

    public void menuLoguinContinuar(View view) {
        String cedula = binding.etCedulaCheck.getText().toString();
        if (!cedula.equals("")) {
            Intent intent = new Intent(MenuLoguin1Activity.this, MenuLoguin2Activity.class);
            intent.putExtra("etCedulaCheckP", cedula);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Digite su cedula", Toast.LENGTH_SHORT).show();
        }
    }

    public void menuRegistro(View view) {
        Intent intent = new Intent(MenuLoguin1Activity.this, MenuRegistroActivity.class);
        startActivity(intent);
        finish();
    }

}