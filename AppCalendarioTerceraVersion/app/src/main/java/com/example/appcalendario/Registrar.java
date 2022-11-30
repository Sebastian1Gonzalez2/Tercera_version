package com.example.appcalendario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcalendario.dialog.FragmentosdeDatos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registrar extends AppCompatActivity {

    EditText txtCorreoR, TxtRutR, TxtNombreR, TxtApellidoR, TxtFechaR, TxtPasswordR, TxtConfirmedPass;
    Button RegistrarUsuario;
    TextView TengoUnaCuentaTXT;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String rut = " ", nombre = " ", apellido = " ", fecha_nacimiento = " ", correo = " ", password = " ", confirmarcontraseña = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zona de registro");

        TxtRutR = findViewById(R.id.txtRutR);
        txtCorreoR = findViewById(R.id.txtCorreoR);
        TxtNombreR = findViewById(R.id.txtNombreR);
        TxtApellidoR = findViewById(R.id.txtapellidoR);
        TxtFechaR = findViewById(R.id.txtfechaR);
        TxtPasswordR = findViewById(R.id.txtContraseñaR);
        TxtConfirmedPass = findViewById(R.id.txtConfirmarContraseñaR);
        RegistrarUsuario = findViewById(R.id.btnRegistrarR);
        TengoUnaCuentaTXT = findViewById(R.id.TengoUnaCuentaTXT);

        TengoUnaCuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registrar.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidarDatos();
            }
        });
    }
    private void ValidarDatos(){
        rut = TxtRutR.getText().toString();
        nombre = TxtNombreR.getText().toString();
        apellido = TxtApellidoR.getText().toString();
        fecha_nacimiento = TxtFechaR.getText().toString();
        correo = txtCorreoR.getText().toString();
        password = TxtPasswordR.getText().toString();
        confirmarcontraseña = TxtConfirmedPass.getText().toString();

        if (TextUtils.isEmpty(rut)){
            Toast.makeText(this, "Ingrese un Rut", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(apellido)){
            Toast.makeText(this, "Ingrese un apellido", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(fecha_nacimiento)){
            Toast.makeText(this, "Ingrese una fecha de nacimiento", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmarcontraseña)){
            Toast.makeText(this, "Confirme la contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmarcontraseña)){
            Toast.makeText(this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        }
        else {
            CrearCuenta();
        }

    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        GuardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener(){
                 @Override
                 public void onFailure(@NonNull Exception e){
                    progressDialog.dismiss();
                     Toast.makeText(Registrar.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
                });
    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando la informacion");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("rut", rut);
        Datos.put("nombre", nombre);
        Datos.put("apellido", apellido);
        Datos.put("fecha_de_nacimiento", fecha_nacimiento);
        Datos.put("correo", correo);
        Datos.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USUARIOS");
        databaseReference.child(uid).setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registrar.this, "La Cuenta ha sido creada con exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registrar.this, MenuAgenda.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registrar.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void FechaNacimiento(View v) {
        FragmentosdeDatos newFragment = FragmentosdeDatos.newInstance((datePicker, year, month, day) -> {
            final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
            TxtFechaR.setText(selectedDate);
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

}