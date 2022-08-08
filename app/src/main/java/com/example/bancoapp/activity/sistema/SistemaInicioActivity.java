package com.example.bancoapp.activity.sistema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.activity.menu.MenuInicioActivity;
import com.example.bancoapp.databinding.ActivitySistemaInicioBinding;

public class SistemaInicioActivity extends AppCompatActivity {

    private ActivitySistemaInicioBinding binding;
    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySistemaInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);

        SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
        String id = (preferences.getString("id_usuario", ""));

        try {

            SQLiteDatabase db = myDB.getReadableDatabase();
            String[] parametros = {id};
            String[] campo = {"NOMBRE", "CELULAR", "SALDO"};

            Cursor cursor = db.query("USER_DATA", campo, "ID" + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            binding.sistemaNombreUsuario.setText(cursor.getString(0));
            binding.sistemaNumeroCuenta.setText(cursor.getString(1));
            String cadenaSaldo = "$ " + cursor.getString(2);
            binding.sistemaSaldo.setText(cadenaSaldo);
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR AL CARGAR LOS DATOS", Toast.LENGTH_SHORT).show();
        }
        enviarPlata();
        historial();
    }

    public void cerrarSesion(View view) {
        Intent intent = new Intent(SistemaInicioActivity.this, MenuInicioActivity.class);
        startActivity(intent);
        finish();
    }

    private void enviarPlata() {
        binding.btnEnviarplata.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaInicioActivity.this, SistemaEnvio1Activity.class);
            startActivity(intent);
            finish();
        });
    }

    private void historial() {
        binding.btnHistorial.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaInicioActivity.this, SistemaHistorialActivity.class);
            startActivity(intent);
            finish();
        });
    }

}