package com.example.appconcesionario;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class CarActivity extends AppCompatActivity {

    EditText etnumberplate, etmodel, etbrand, etprice;
    Button btnsave, btnsearch, btndelete, btncancel;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.btn_add_car);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etnumberplate = findViewById(R.id.etAddPlate);
        etmodel = findViewById(R.id.etAddModel);
        etbrand = findViewById(R.id.etAddBrand);
        etprice = findViewById(R.id.etAddPrice);
        btnsave = findViewById(R.id.btnSaveCar);
        btnsearch = findViewById(R.id.btnSearchCar);
        btndelete = findViewById(R.id.btnDeleteCar);
        btncancel = findViewById(R.id.btnCancelCar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void addCar(View v){
        SQLiteDatabase db = Admin.getWritableDatabase();
        String numberPlate, model, brand, price;
        numberPlate = etnumberplate.getText().toString().toUpperCase();
        model = etmodel.getText().toString();
        brand = etbrand.getText().toString();
        price = etprice.getText().toString();

        if (numberPlate.isEmpty() || model.isEmpty() || brand.isEmpty() || price.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.full_datas);
            etnumberplate.requestFocus();
        }else{
            ContentValues data = new ContentValues();
            data.put("placa", numberPlate);
            data.put("modelo", model);
            data.put("marca", brand);
            data.put("valor", price);
            SQLiteDatabase db_alt = Admin.getReadableDatabase();
            Cursor row = db_alt.rawQuery("SELECT * FROM auto WHERE placa='" + numberPlate + "'", null);
            if (row.moveToFirst()){
                long respond = db.update ("auto", data,"placa='" + numberPlate + "'",null);
                if (respond > 0){
                    inflateToast(R.layout.toast_success, R.id.toast_container_success, R.string.info_updated);
                }else{
                    inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.update_failed);
                }
            }else{
                long respond = db.insert("auto", null, data);
                if (respond > 0){
                    inflateToast(R.layout.toast_success, R.id.toast_container_success, R.string.info_saved);
                }else{
                    inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.save_failed);
                }
            }
            limpiarCampos();
        }
        db.close();
    }

    public void searchCar(View v){
        SQLiteDatabase db = Admin.getReadableDatabase();
        String numberPlate;
        numberPlate = etnumberplate.getText().toString().toUpperCase();
        if (numberPlate.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.plate_required);
            etnumberplate.requestFocus();
        }else{
            Cursor row = db.rawQuery("SELECT * FROM auto WHERE placa='" + numberPlate + "'", null);
            if (row.moveToFirst()){
                etmodel.setText(row.getString(1));
                etbrand.setText(row.getString(2));
                etprice.setText(row.getString(3));
                etnumberplate.requestFocus();
                /*ÚLTIMOS CAMBIOS*/
                Cursor rowSales = db.rawQuery("SELECT * FROM venta WHERE placa='" + numberPlate + "'", null);
                if (rowSales.moveToFirst()){
                    etnumberplate.setTextColor(getResources().getColor(R.color.colorFailed));
                }else {
                    etnumberplate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }else{
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.car_no_found);
                limpiarCampos();
            }
            db.close();
        }
    }

    public void deleteCar(View v){
        SQLiteDatabase db = Admin.getWritableDatabase();
        String numberPlate;
        numberPlate = etnumberplate.getText().toString().toUpperCase();
        if (numberPlate.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.plate_required);
            etnumberplate.requestFocus();
        }else{
            long respond = db.delete("auto", "placa='" + numberPlate + "'", null);
            if (respond > 0){
                inflateToast(R.layout.toast_success, R.id.toast_container_success, R.string.info_deleted);
            }else{
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.delete_failed);
            }
            limpiarCampos();
        }
        db.close();
    }

    public void cancelar(View v){
        limpiarCampos();
    }

    public void limpiarCampos(){
        etnumberplate.setText("");
        etmodel.setText("");
        etbrand.setText("");
        etprice.setText("");
        etnumberplate.requestFocus();
        etnumberplate.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    //Procesamiento toast personalizados
    public void inflateToast(int id_toast, int id_layout, int message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(id_toast,
                (ViewGroup) findViewById(id_layout));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 160);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}