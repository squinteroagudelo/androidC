package com.example.appconcesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MainSQLiteOpenHelper extends SQLiteOpenHelper {

    public MainSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cliente(usuario text primary key, nombre text, clave text, ciudad text)");
        db.execSQL("CREATE TABLE auto(placa text primary key, modelo text, marca text, valor text, estado integer default(0))");
        db.execSQL("CREATE TABLE venta(numventa integer primary key autoincrement, usuario text, placa text, modelo text, marca text, color text, precio text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE cliente");
        onCreate(db);
        db.execSQL("DROP TABLE auto");
        onCreate(db);
        db.execSQL("DROP TABLE venta");
        onCreate(db);
    }
}
