package com.example.bancoapp.activity.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.databinding.ActivityMenuRegistroBinding;

public class MenuRegistroActivity extends AppCompatActivity {

    private ActivityMenuRegistroBinding binding;

    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);
        insertarUsuario();
    }

    public void volver(View view) {
        Intent intent = new Intent(MenuRegistroActivity.this, MenuInicioActivity.class);
        startActivity(intent);
        finish();
    }


    private void insertarUsuario() {
        binding.btnValidar.setOnClickListener(view -> {

            String cedula = binding.etCedula.getText().toString();
            String nombre = binding.etNombre.getText().toString();
            String celular = binding.etCelular.getText().toString();
            String contrasena = binding.etContrasena.getText().toString();
            String confirmarContrasena = binding.etConfirmarContrasena.getText().toString();

            if (cedula.equals("") || nombre.equals("") || celular.equals("") || contrasena.equals("") || confirmarContrasena.equals("")) {
                Toast.makeText(MenuRegistroActivity.this, "Campos vacios", Toast.LENGTH_SHORT).show();
            } else if (cedula.length() < 4 || cedula.length() > 12) {
                Toast.makeText(MenuRegistroActivity.this, "CÉDULA: Min 4, Max 12 dígitos", Toast.LENGTH_SHORT).show();
            } else if (nombre.length() < 4 || nombre.length() > 40) {
                Toast.makeText(MenuRegistroActivity.this, "NOMBRE: Min 4, Max 40 caracteres", Toast.LENGTH_SHORT).show();
            } else if (celular.length() != 10) {
                Toast.makeText(MenuRegistroActivity.this, "CELULAR: Campo de 10 dígitos", Toast.LENGTH_SHORT).show();
            } else if (contrasena.length() != 4) {
                Toast.makeText(MenuRegistroActivity.this, "CONTRASEÑA: Min 4, Max 4 dígitos", Toast.LENGTH_SHORT).show();
            } else if (confirmarContrasena.length() != 4) {
                Toast.makeText(MenuRegistroActivity.this, "CONFIRMAR CONTRASEÑA: Min 4, Max 4 dígitos", Toast.LENGTH_SHORT).show();
            } else if (!contrasena.equals(confirmarContrasena)) {
                Toast.makeText(MenuRegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {

                int numCedula = Integer.parseInt(cedula);

                celular = ("0" + binding.etCelular.getText().toString());

                int numContrasena = Integer.parseInt(contrasena);

                int numSaldo = 1000000;

                boolean checkreg = myDB.checkUsuarioRegistrado(numCedula);
                if (checkreg) {
                    Toast.makeText(MenuRegistroActivity.this, "El usuario con cedula  " + numCedula + " ya existe", Toast.LENGTH_SHORT).show();
                } else {

                    //
                    boolean checkNumcuenta = myDB.checkNroCuenta(celular);
                    if (checkNumcuenta) {
                        Toast.makeText(MenuRegistroActivity.this, "Ya existe un usuario con ese celular", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean reg = myDB.registrarUsuario(numCedula, nombre, celular, numContrasena, numSaldo);
                        if (reg) {
                            Toast.makeText(MenuRegistroActivity.this, "Usuario Registrado con Exito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MenuRegistroActivity.this, MenuLoguin1Activity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MenuRegistroActivity.this, "Error Usuario no Registrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}