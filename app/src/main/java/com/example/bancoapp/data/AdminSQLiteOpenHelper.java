package com.example.bancoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BANCOAPP";
    public static final String TABLE_NAME1 = "USER_DATA";
    public static final String TABLE_NAME2 = "USER_HISTORY";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "CEDULA";
    public static final String COL_3 = "NOMBRE";
    public static final String COL_4 = "CELULAR";
    public static final String COL_5 = "CONTRASENA";
    public static final String COL_6 = "SALDO";

    public static final String COL_7 = "ID";
    public static final String COL_8 = "REF_USER";
    public static final String COL_9 = "CELULAR";
    public static final String COL_10 = "CELULAR_DESTINO";
    public static final String COL_11 = "MONTO";

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CEDULA INT, NOMBRE TEXT, CELULAR TEXT, CONTRASENA INT, SALDO INT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, REF_USER INT, CELULAR TEXT, CELULAR_DESTINO TEXT, MONTO INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }

    public boolean registrarUsuario(int cedula, String nombre, String celular, int contrasena, int saldo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, cedula);
        values.put(COL_3, nombre);
        values.put(COL_4, celular);
        values.put(COL_5, contrasena);
        values.put(COL_6, saldo);
        long result = db.insert(TABLE_NAME1, null, values);
        if (result == -1)
            return false; //Error, no se guardaron
        else
            return true; //Se guardaron
    }

    public boolean checkUsuarioLogin(int cedula, int contrasena) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] columnas = {COL_1};
        String seleccion = COL_2 + "=?" + " and " + COL_5 + " =? ";
        String strCedula = Integer.toString(cedula);
        String strContrasena = Integer.toString(contrasena);
        String[] selecciongumentos = {strCedula, strContrasena};
        Cursor cursor = db.query(TABLE_NAME1, columnas, seleccion, selecciongumentos, null, null, null);
        int cont = cursor.getCount();
        db.close();
        cursor.close();
        if (cont > 0)
            return true;
        else
            return false;
    }

    public boolean checkUsuarioRegistrado(int cedula) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columnas = {COL_1};
        String seleccion = COL_2 + "=?";
        String strcedula = Integer.toString(cedula);
        String[] selecciongumentos = {strcedula};
        Cursor cursor = db.query(TABLE_NAME1, columnas, seleccion, selecciongumentos, null, null, null);
        int cont = cursor.getCount();
        db.close();
        cursor.close();
        if (cont > 0)
            return true;
        else
            return false;
    }

    public boolean checkNroCuenta(String celular) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] columnas = {COL_1};
        String seleccion = COL_4 + "=?";
        String[] selecciongumentos = {celular};
        Cursor cursor = db.query(TABLE_NAME1, columnas, seleccion, selecciongumentos, null, null, null);
        int cont = cursor.getCount();
        db.close();
        cursor.close();
        if (cont > 0)
            return true;
        else
            return false;
    }

    public boolean registrarHistorial(int ref_user, String celular, String celular_destino, int monto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_8, ref_user);
        values.put(COL_9, celular);
        values.put(COL_10, celular_destino);
        values.put(COL_11, monto);
        long result = db.insert(TABLE_NAME2, null, values);
        if (result == -1)
            return false; //Error, no se guardo
        else
            return true; //Se guardardo
    }

    public void editarSaldo(int nuevomonto, int id_usuario) {
        SQLiteDatabase bd = this.getWritableDatabase();
        if (bd != null) {
            bd.execSQL("UPDATE " + TABLE_NAME1 + " SET SALDO='" + nuevomonto + "' WHERE ID='" + id_usuario + "'");
            bd.close();
        }
    }
}
