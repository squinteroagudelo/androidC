package com.example.appconcesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etusuario, etclave;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        etusuario = findViewById(R.id.etuser);
        etclave = findViewById(R.id.etpassword);
    }

    public void signUpInt(View v){
        Intent sign_up = new Intent(this, SignUpActivity.class);
        startActivity(sign_up);
    }

    public void carInt(View v){
        SQLiteDatabase db = Admin.getReadableDatabase();
        String usuario, clave;
        usuario = etusuario.getText().toString().toLowerCase();
        clave = etclave.getText().toString();
        if (usuario.isEmpty() || clave.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.empty_login_data);
        }else{
            Cursor row = db.rawQuery("SELECT * FROM cliente WHERE usuario='" + usuario + "' AND clave='" + clave + "'", null);
            if (row.moveToFirst()){
                Intent car = new Intent(this, CarActivity.class);
                startActivity(car);
            }else{
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.invalid_login);
                etclave.setText("");
            }
        }
        etusuario.setText("");
        etclave.setText("");
        etusuario.requestFocus();
        db.close();
    }

    public void salesInt(View v){
        SQLiteDatabase db = Admin.getReadableDatabase();
        String usuario, clave;
        usuario = etusuario.getText().toString().toLowerCase();
        clave = etclave.getText().toString();
        if (usuario.isEmpty() || clave.isEmpty()){
            inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.empty_login_data);
        }else{
            Cursor row = db.rawQuery("SELECT * FROM cliente WHERE usuario='" + usuario + "' AND clave='" + clave + "'", null);
            if (row.moveToFirst()){
                Intent sales = new Intent(this, SalesActivity.class);
                sales.putExtra("userlogin", usuario);
                startActivity(sales);
            }else{
                inflateToast(R.layout.toast_failed, R.id.toast_container_failed, R.string.invalid_login);
                etclave.setText("");
            }
        }
        etusuario.setText("");
        etclave.setText("");
        etusuario.requestFocus();
        db.close();
    }

    //Procesamiento toast personalizados
    public void inflateToast(int id_toast, int id_layout, int message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(id_toast,
                (ViewGroup) findViewById(id_layout));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        /*Toast toast= Toast.makeText(getApplicationContext(),"Digite datos de ingreso", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 160);
        toast.show();*/

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 160);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}