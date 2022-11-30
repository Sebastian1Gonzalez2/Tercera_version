package com.example.appcalendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.appcalendario.dialog.FragmentosdeDatos;
import com.example.appcalendario.horario.agregar_horario;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class actualizarDatos extends AppCompatActivity {

    EditText EditRut, EditNombre, EditApellido, EditFecha;
    TextView lbRutEdit, lbNombreEdit, lbApellidoEdit, lbFechaEdit, lbCorreoEdit;
    Button BtnActualizarDatos;

    DatabaseReference USUARIOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actualizar Datos");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditRut = findViewById(R.id.EditTxtRut);
        EditNombre = findViewById(R.id.EditTxtNombre);
        EditApellido = findViewById(R.id.EditTxtApellido);
        EditFecha = findViewById(R.id.EditTxtFecha);

        lbRutEdit = findViewById(R.id.RutMostrarEdit);
        lbNombreEdit = findViewById(R.id.NombreMostrarEdit);
        lbApellidoEdit = findViewById(R.id.ApellidoMostrarEdit);
        lbFechaEdit = findViewById(R.id.FechaMostrarEdit);
        lbCorreoEdit = findViewById(R.id.CorreoMostrarEdit);

        BtnActualizarDatos = findViewById(R.id.btnActualizarDatos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        USUARIOS = FirebaseDatabase.getInstance().getReference("USUARIOS");

        progressDialog = new ProgressDialog(actualizarDatos.this);


        Query query = USUARIOS.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String rut = "" + ds.child("rut").getValue();
                    String nombre = "" + ds.child("nombre").getValue();
                    String apellido = "" + ds.child("apellido").getValue();
                    String fecha = "" + ds.child("fecha_de_nacimiento").getValue();
                    String correo = "" + ds.child("correo").getValue();

                    lbRutEdit.setText(rut);
                    lbNombreEdit.setText(nombre);
                    lbApellidoEdit.setText(apellido);
                    lbFechaEdit.setText(fecha);
                    lbCorreoEdit.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BtnActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NUEVO_RUT = EditRut.getText().toString().trim();
                String NUEVO_NOMBRE = EditNombre.getText().toString().trim();
                String NUEVO_APELLIDO = EditApellido.getText().toString().trim();
                String NUEVO_FECHA = EditFecha.getText().toString().trim();

                if (TextUtils.isEmpty(NUEVO_RUT)) {
                    Toast.makeText(actualizarDatos.this, "Rellene el campo rut", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_NOMBRE)) {
                    Toast.makeText(actualizarDatos.this, "Rellene el campo nombre", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_APELLIDO)) {
                    Toast.makeText(actualizarDatos.this, "Rellene el campo apellido", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_FECHA)) {
                    Toast.makeText(actualizarDatos.this, "Rellene el campo fecha", Toast.LENGTH_SHORT).show();

                }else{
                    Cambio_de_datos();
                }
            }
        });
    }

    private void Cambio_de_datos() {
        String A_Rut =  EditRut.getText().toString().trim();
        String A_Nombre = EditNombre.getText().toString().trim();
        String A_Apellido = EditApellido.getText().toString().trim();
        String A_Fecha = EditFecha.getText().toString().trim();

        HashMap<String, Object> Datos_Actualizar = new HashMap<>();

        Datos_Actualizar.put("rut", A_Rut);
        Datos_Actualizar.put("nombre", A_Nombre);
        Datos_Actualizar.put("apellido", A_Apellido);
        Datos_Actualizar.put("fecha_de_nacimiento", A_Fecha);

        USUARIOS.child(user.getUid()).updateChildren(Datos_Actualizar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(actualizarDatos.this, "Los datos han sido actualizados", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(actualizarDatos.this, mostrarDatos.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(actualizarDatos.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void FechaNacimientoEdit(View v) {
        FragmentosdeDatos newFragment = FragmentosdeDatos.newInstance((datePicker, year, month, day) -> {
            final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
            EditFecha.setText(selectedDate);
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}