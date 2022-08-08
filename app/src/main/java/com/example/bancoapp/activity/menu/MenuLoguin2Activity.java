package com.example.bancoapp.activity.menu;

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
import com.example.bancoapp.activity.sistema.SistemaInicioActivity;
import com.example.bancoapp.databinding.ActivityMenuLoguin2Binding;

public class MenuLoguin2Activity extends AppCompatActivity {

    private ActivityMenuLoguin2Binding binding;

    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuLoguin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);

        ingresar();
    }

    public void volver(View view) {
        Intent intent = new Intent(MenuLoguin2Activity.this, MenuLoguin1Activity.class);
        startActivity(intent);
        finish();
    }

    private void ingresar() {
        binding.btnIngresar.setOnClickListener(view -> {

            String contrasena = binding.etContrasenaCheck.getText().toString();

            if (contrasena.equals("")) {
                Toast.makeText(MenuLoguin2Activity.this, "Digite su contraseña", Toast.LENGTH_SHORT).show();
            } else {

                Bundle bundle = getIntent().getExtras();
                String cedula = bundle.getString("etCedulaCheckP");

                int numCedula = Integer.parseInt(cedula);
                int numContrasena = Integer.parseInt(contrasena);

                boolean reg = myDB.checkUsuarioLogin(numCedula, numContrasena);
                if (reg) {
                    SQLiteDatabase db = myDB.getReadableDatabase();
                    String[] parametros = {cedula};
                    String[] campo = {"ID"};

                    Cursor cursor = db.query("USER_DATA", campo, "CEDULA" + "=?", parametros, null, null, null);
                    cursor.moveToFirst();
                    String id_usuario = (cursor.getString(0));
                    cursor.close();

                    SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id_usuario", id_usuario);
                    editor.commit();

                    Toast.makeText(MenuLoguin2Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MenuLoguin2Activity.this, SistemaInicioActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MenuLoguin2Activity.this, "ERROR > Verifica tus datos e intenta nuevamente", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

}