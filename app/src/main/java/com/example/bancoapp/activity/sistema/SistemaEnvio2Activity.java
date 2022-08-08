package com.example.bancoapp.activity.sistema;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancoapp.data.AdminSQLiteOpenHelper;
import com.example.bancoapp.databinding.ActivitySistemaEnvio2Binding;

public class SistemaEnvio2Activity extends AppCompatActivity {

    private int numid_usuario;
    private String saldo;
    private String celulardestino;
    private String idCuentaDestino;

    private ActivitySistemaEnvio2Binding binding;
    private AdminSQLiteOpenHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySistemaEnvio2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDB = new AdminSQLiteOpenHelper(this);

        volver();
        confirmar();
        cancelar();
    }

    private void confirmar() {
        binding.btnConfirmar.setOnClickListener(view -> {
            celulardestino = binding.etCeluarDestino.getText().toString();
            if (celulardestino.equals("")) {
                Toast.makeText(SistemaEnvio2Activity.this, "Digite número de cuenta", Toast.LENGTH_SHORT).show();
            } else if (celulardestino.length() < 11) {
                Toast.makeText(SistemaEnvio2Activity.this, "La cuenta debe ser minimo de 11 digitos", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = getIntent().getExtras();
                String monto = bundle.getString("etMonto");
                // celulardestino

                //
                // = REF_USUARIO
                SharedPreferences preferences = getSharedPreferences("USUARIO", Context.MODE_PRIVATE);
                String id_usuario = (preferences.getString("id_usuario", ""));
                //
                confirmacionEnvio(id_usuario, monto, celulardestino);


            }

        });
    }

    private void cancelar() {
        binding.btnCancelar.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaEnvio2Activity.this, SistemaInicioActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void volver() {
        binding.btnSistemaenvioVolver.setOnClickListener(view -> {
            Intent intent = new Intent(SistemaEnvio2Activity.this, SistemaEnvio1Activity.class);
            startActivity(intent);
            finish();
        });
    }

    public void confirmacionEnvio(String id_usuario, String monto, String celulardestino) {

        String mensajeAlerta = msgAlert(id_usuario, monto, celulardestino);

        final AlertDialog.Builder alertaConfirmacion = new AlertDialog.Builder(SistemaEnvio2Activity.this);
        alertaConfirmacion.setMessage(mensajeAlerta)
                .setCancelable(false)
                .setPositiveButton("CONFIRMAR", (dialogInterface, i) -> {

                    //VALIDACIONES DE CUENTA , SALDO DISPONIBLE Y SI ES SU PROPIA CUENTA

                    boolean checkCelularDestino = myDB.checkNroCuenta(celulardestino);
                    if (checkCelularDestino) {

                        //SI LA CUENTA DESTINO EXISTE PASA

                        SQLiteDatabase db = myDB.getReadableDatabase();
                        String[] parametros = {id_usuario};
                        String[] campo = {"SALDO", "CELULAR"};

                        Cursor cursor = db.query("USER_DATA", campo, "ID" + "=?", parametros, null, null, null);
                        cursor.moveToFirst();
                        String saldoactual = cursor.getString(0);
                        String celularorigen = cursor.getString(1);
                        cursor.close();
                        db.close();
                        //SI LA CUENTA ORIGEN TIENE FONDO SUFICIENTE PASA
                        if (Integer.parseInt(saldoactual) >= Integer.parseInt(monto)) {
                            if (celulardestino.equals(celularorigen)) {
                                Toast.makeText(this, "No puedes enviar dinero a tu propia cuenta", Toast.LENGTH_SHORT).show();
                                binding.etCeluarDestino.setText("");

                            } else {

                                // CODIGO DESPUES DE CONFIRMAR ENVIO

                                try {
                                    SQLiteDatabase db1 = myDB.getReadableDatabase();
                                    String[] parametros1 = {id_usuario};
                                    String[] campo1 = {"CELULAR", "SALDO"};

                                    Cursor cursor1 = db1.query("USER_DATA", campo1, "ID" + "=?", parametros1, null, null, null);
                                    cursor1.moveToFirst();
                                    celularorigen = cursor1.getString(0);
                                    saldo = cursor1.getString(1);
                                    cursor1.close();
                                    db1.close();

                                    numid_usuario = Integer.parseInt(id_usuario);
                                    int nummonto = Integer.parseInt(monto);

                                    boolean regHistorial = myDB.registrarHistorial(numid_usuario, celularorigen, celulardestino, nummonto);
                                    if (regHistorial) {
                                        Toast.makeText(SistemaEnvio2Activity.this, "Envio Realizado con Exito", Toast.LENGTH_SHORT).show();


                                        int numsaldo = Integer.parseInt(saldo);
                                        int nuevomonto = numsaldo - nummonto;
                                        int nuevoMontoCuentaDestino = sumarSaldo(celulardestino, nummonto);

                                        // ACTUALIZAMOS MONTOS
                                        myDB.editarSaldo(nuevomonto, numid_usuario);
                                        myDB.editarSaldo(nuevoMontoCuentaDestino, (Integer.parseInt(idCuentaDestino)));

                                        Intent intent = new Intent(SistemaEnvio2Activity.this, SistemaInicioActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SistemaEnvio2Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                                //

                            }
                        } else {
                            Toast.makeText(this, "Transacción cancelada, fondos insuficientes", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SistemaEnvio2Activity.this, SistemaInicioActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {

                        Toast.makeText(this, "El Nro. de cuenta no existe", Toast.LENGTH_SHORT).show();
                        binding.etCeluarDestino.setText("");
                    }


                })
                .setNegativeButton("CANCELAR", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog titulo = alertaConfirmacion.create();
        titulo.setTitle("Verifica el envío");
        titulo.show();

    }

    public String msgAlert(String id_usuario, String monto, String celulardestino) {
        // PARA CONSULTAR LA DB E IMPRIMIR
        SQLiteDatabase db = myDB.getReadableDatabase();
        String[] parametros = {id_usuario};
        String[] campo = {"CELULAR"};

        Cursor cursor = db.query("USER_DATA", campo, "ID" + "=?", parametros, null, null, null);
        cursor.moveToFirst();
        String celularorigen = cursor.getString(0);
        cursor.close();

        String sl = "\n";
        return ("PRODUCTO ORIGEN" + sl + celularorigen + sl + sl + "PRODUCTO DESTINO" + sl + celulardestino + sl + sl + "VALOR A ENVIAR" + sl + monto);
    }

    public int sumarSaldo(String celulardestino, int nummonto) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        String[] parametros = {celulardestino};
        String[] campo = {"SALDO", "ID"};

        Cursor cursor = db.query("USER_DATA", campo, "CELULAR" + "=?", parametros, null, null, null);
        cursor.moveToFirst();
        String montoCuentaDestino = cursor.getString(0);
        idCuentaDestino = cursor.getString(1);
        cursor.close();
        int numMontoCuentaDestino = Integer.parseInt(montoCuentaDestino);
        return numMontoCuentaDestino + nummonto;
    }

}