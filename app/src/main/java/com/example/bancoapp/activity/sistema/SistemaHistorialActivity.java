package com.example.bancoapp.activity.sistema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bancoapp.R;
import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.databinding.ActivitySistemaHistorialBinding;

import java.util.ArrayList;

public class SistemaHistorialActivity extends AppCompatActivity {

    public ActivitySistemaHistorialBinding binding;
    private AdminSQLiteOpenHelper myDB;

    private ArrayList<String> historial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySistemaHistorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);

        historial = new ArrayList<String>();

        logicaCargarEnviado();

        volver();
        cargarHistorialEnviado();
        cargarHistorialRecido();
    }

    private void volver() {
        binding.btnSistemahistorialVovler.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaHistorialActivity.this, SistemaInicioActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void cargarHistorialEnviado() {
        binding.btnCargarenviado.setOnClickListener(view -> {

            logicaCargarEnviado();
        });
    }

    private void cargarHistorialRecido() {
        binding.btnCargarrecibido.setOnClickListener(view -> {
            binding.btnCargarrecibido.setBackgroundResource(R.drawable.btn);
            binding.btnCargarenviado.setBackgroundResource(R.color.transparent);
            limpiarHistorial();
            SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
            String id = (preferences.getString("id_usuario", ""));

            try {

                SQLiteDatabase db = myDB.getReadableDatabase();
                String[] parametros1 = {id};
                String[] campo1 = {"CELULAR"};

                Cursor cursor1 = db.query("USER_DATA", campo1, "ID" + "=?", parametros1, null, null, null);
                cursor1.moveToFirst();
                String celular_origen = cursor1.getString(0);
                cursor1.close();

                //

                String[] parametros2 = {celular_origen};
                String[] campo2 = {"CELULAR", "MONTO"};

                Cursor cursor2 = db.query("USER_HISTORY", campo2, "CELULAR_DESTINO" + "=?", parametros2, null, null, null);
                cursor2.moveToFirst();
                if (cursor2.moveToFirst()) {
                    do {
                        String H1 = cursor2.getString(0);
                        String H2 = cursor2.getString(1);
                        String SL = "\n";
                        String nuevoHistorial = (SL + "Nro. CUENTA: " + H1 + SL + "$ " + H2 + SL);
                        historial.add(nuevoHistorial);
                    } while (cursor2.moveToNext());
                }

                cursor2.close();
                db.close();

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historial);
                binding.listviewpersonas.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "ERROR AL TRAER EL CELULAR DE ORIGEN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarHistorial() {
        historial.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historial);
        binding.listviewpersonas.setAdapter(adapter);
    }

    private void logicaCargarEnviado() {

        binding.btnCargarenviado.setBackgroundResource(R.drawable.btn);
        binding.btnCargarrecibido.setBackgroundResource(R.color.transparent);

        limpiarHistorial();
        SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
        String id = (preferences.getString("id_usuario", ""));

        SQLiteDatabase db = myDB.getReadableDatabase();
        String[] parametros = {id};
        String[] campo = {"CELULAR_DESTINO", "MONTO"};

        Cursor cursor = db.query("USER_HISTORY", campo, "REF_USER" + "=?", parametros, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String H1 = cursor.getString(0);
                    String H2 = cursor.getString(1);
                    String SL = "\n";
                    String nuevoHistorial = (SL + "Nro. CUENTA: " + H1 + SL + "$ " + H2 + SL);
                    historial.add(nuevoHistorial);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR AL CARGAR LOS DATOS", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historial);
        binding.listviewpersonas.setAdapter(adapter);

        cursor.close();
        db.close();

    }

}