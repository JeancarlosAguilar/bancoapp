package com.example.bancoapp.activity.sistema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.databinding.ActivitySistemaEnvio1Binding;

public class SistemaEnvio1Activity extends AppCompatActivity {

    private ActivitySistemaEnvio1Binding binding;
    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySistemaEnvio1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);

        SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
        String id_usuario = (preferences.getString("id_usuario", ""));

        try {

            SQLiteDatabase db = myDB.getReadableDatabase();
            String[] parametros = {id_usuario};
            String[] campo = {"CELULAR", "SALDO"};

            Cursor cursor = db.query("USER_DATA", campo, "ID" + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            binding.sistemaEnvioNumeroCuenta.setText(cursor.getString(0));
            String cadenaSaldo = "$ " + cursor.getString(1);
            binding.sistemaEnvioSaldo.setText(cadenaSaldo);
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR AL CARGAR LOS DATOS", Toast.LENGTH_SHORT).show();
        }

        volver();
        siguiente();
    }

    private void volver() {
        binding.btnSistemaenvioVolver.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaEnvio1Activity.this, SistemaInicioActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void siguiente() {
        binding.btnSiguiente.setOnClickListener(view -> {
            String monto = binding.etMonto.getText().toString();
            if (monto.equals("")) {
                Toast.makeText(SistemaEnvio1Activity.this, "Digite un monto", Toast.LENGTH_SHORT).show();
            } else if (monto.length() < 4) {
                Toast.makeText(SistemaEnvio1Activity.this, "Monto minimo a enviar 1000", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SistemaEnvio1Activity.this, SistemaEnvio2Activity.class);
                intent.putExtra("etMonto", monto);
                startActivity(intent);
                finish();
            }
        });
    }


}