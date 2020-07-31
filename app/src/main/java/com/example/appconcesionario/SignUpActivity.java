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

public class SignUpActivity extends AppCompatActivity {

    EditText etname, etuser, etpassword, etcity;
    Button btnSave, btnSearch, btnDelete, btnCancel;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.btn_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etname = findViewById(R.id.etAddName);
        etuser = findViewById(R.id.etAddUserName);
        etpassword = findViewById(R.id.etAddPassword);
        etcity = findViewById(R.id.etAddCity);
        btnSave = findViewById(R.id.btnSaveUser);
        btnSearch = findViewById(R.id.btnSearchUser);
        btnDelete = findViewById(R.id.btnDeleteUser);
        btnCancel = findViewById(R.id.btnCancelUser);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void addUser(View v){
        SQLiteDatabase db = Admin.getWritableDatabase();
        String name, username, password, city;
        name = etname.getText().toString();
        username = etuser.getText().toString().toLowerCase();
        password = etpassword.getText().toString();
        city = etcity.getText().toString();
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || city.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.full_datas);
            etname.requestFocus();
        }else{
            ContentValues data = new ContentValues();
            data.put("nombre", name);
            data.put("usuario", username);
            data.put("clave", password);
            data.put("ciudad", city);
            SQLiteDatabase db_alt = Admin.getReadableDatabase();
            Cursor row = db_alt.rawQuery("SELECT * FROM cliente WHERE usuario='" + username + "'", null);
            if (row.moveToFirst()){
                long respond = db.update ("cliente", data,"usuario='" + username + "'",null);
                if (respond > 0){
                    inflateToast(R.layout.toast_success, R.id.toast_container_success, R.string.info_updated);
                }else{
                    inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.update_failed);
                }
            }else{
                long respond = db.insert("cliente", null, data);
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

    public void searchUser(View v){
        SQLiteDatabase db = Admin.getReadableDatabase();
        String username;
        username = etuser.getText().toString().toLowerCase();
        if (username.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.user_required);
            etuser.requestFocus();
        }else{
            Cursor row = db.rawQuery("SELECT * FROM cliente WHERE usuario='" + username + "'", null);
            if (row.moveToFirst()){
                etname.setText(row.getString(1));
                etpassword.setText(row.getString(2));
                etcity.setText(row.getString(3));
                etuser.requestFocus();
            }else{
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.user_no_found);
                limpiarCampos();
            }
            db.close();
        }
    }

    public void deleteUser(View v){
        SQLiteDatabase db = Admin.getWritableDatabase();
        String username;
        username = etuser.getText().toString().toLowerCase();
        if (username.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.user_required);
            etuser.requestFocus();
        }else{
            long respond = db.delete("cliente", "usuario='" + username + "'", null);
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
        etname.requestFocus();
    }

    public void limpiarCampos(){
        etname.setText("");
        etuser.setText("");
        etpassword.setText("");
        etcity.setText("");
        etname.requestFocus();
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