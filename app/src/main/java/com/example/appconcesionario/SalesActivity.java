package com.example.appconcesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

public class SalesActivity extends AppCompatActivity {

    EditText etuser, etnumberplate, etmodel, etbrand, etcolor, etprice;
    Button btnbuycar, btnCancel;
    ImageView ivsearchcar;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_sales_act);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etuser = findViewById(R.id.etBuyUser);
        etnumberplate = findViewById(R.id.etBuyPlate);
        etmodel = findViewById(R.id.etBuyModel);
        etbrand = findViewById(R.id.etBuyBrand);
        etcolor = findViewById(R.id.etBuyColor);
        etprice = findViewById(R.id.etBuyPrice);
        btnbuycar = findViewById(R.id.btnBuyCar);
        btnCancel = findViewById(R.id.btnBuyCancel);
        ivsearchcar = findViewById(R.id.ivSearchCar);
        String user = getIntent().getStringExtra("userlogin");
        etuser.setText(user);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void buyCar(View v) {
        SQLiteDatabase db = Admin.getWritableDatabase();
        String user, numberPlate, model, brand, color, price;
        user = etuser.getText().toString();
        numberPlate = etnumberplate.getText().toString().toUpperCase();
        model = etmodel.getText().toString();
        brand = etbrand.getText().toString();
        color = etcolor.getText().toString();
        price = etprice.getText().toString();

        if (numberPlate.isEmpty() || model.isEmpty() || brand.isEmpty() || price.isEmpty()) {
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.check_plate);
            etnumberplate.requestFocus();
        } else {
            if (color.isEmpty()) {
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.enter_color);
                etcolor.requestFocus();
            } else {
                ContentValues data = new ContentValues();
                data.put("usuario", user);
                data.put("placa", numberPlate);
                data.put("modelo", model);
                data.put("marca", brand);
                data.put("color", color);
                data.put("precio", price);
                long respond = db.insert("venta", null, data);
                if (respond > 0) {
                    inflateToast(R.layout.toast_success, R.id.toast_container_success, R.string.info_saved);
                    db.execSQL("UPDATE auto SET estado=1 WHERE placa='" + numberPlate + "'");
                } else {
                    inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.info_updated);
                }
                limpiarCampos();
            }
        }
        db.close();
    }

    public void cancelBuy(View v) {
        limpiarCampos();
    }

    @SuppressLint("ResourceAsColor")
    public void searchCarBuy(View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String numberPlate = etnumberplate.getText().toString().toUpperCase();
        if (numberPlate.isEmpty()) {
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.plate_required);
            limpiarCampos();
        } else {
            @SuppressLint("Recycle") Cursor row = db.rawQuery("SELECT * FROM auto WHERE placa='" + numberPlate + "'", null);
            if (row.moveToFirst()) {
                int estado = row.getInt(4);
                if (estado == 0) {
                    etmodel.setText(row.getString(1));
                    etbrand.setText(row.getString(2));
                    etprice.setText(row.getString(3));
                    etcolor.requestFocus();
                }else{
                    inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.sold);
                    limpiarCampos();
                    etnumberplate.requestFocus();
                }
            } else {
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.car_no_found);
                limpiarCampos();
            }
        }
        db.close();
    }

    public void limpiarCampos() {
        etnumberplate.setText("");
        etmodel.setText("");
        etbrand.setText("");
        etcolor.setText("");
        etprice.setText("");
        etnumberplate.requestFocus();
    }

    //Procesamiento toast personalizados
    public void inflateToast(int id_toast, int id_layout, int message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(id_toast,
                (ViewGroup) findViewById(id_layout));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 160);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}