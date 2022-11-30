package com.example.appcalendario;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mostrarDatos extends AppCompatActivity {

    TextView MostrarRut, MostrarNombre, MostrarApellido, MostrarFecha, MostrarCorreo, MostrarPassword;
    Button btnEditDatos, btnEditPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference BaseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mis Datos");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        MostrarRut = findViewById(R.id.mostrarRut);
        MostrarNombre = findViewById(R.id.mostrarNombre);
        MostrarApellido = findViewById(R.id.mostrarApellido);
        MostrarFecha = findViewById(R.id.mostrarFecha);
        MostrarCorreo = findViewById(R.id.mostrarCorreo);
        MostrarPassword = findViewById(R.id.mostrarPassword);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnEditDatos = findViewById(R.id.btnEditDatos);

        btnEditDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mostrarDatos.this, actualizarDatos.class));
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mostrarDatos.this, actualizarPassword.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BaseDeDatos = FirebaseDatabase.getInstance().getReference("USUARIOS");

        BaseDeDatos.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String rut = "" + snapshot.child("rut").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String apellido = "" + snapshot.child("apellido").getValue();
                    String fecha = "" + snapshot.child("fecha_de_nacimiento").getValue();
                    String correo = "" + snapshot.child("correo").getValue();
                    String password = "" + snapshot.child("password").getValue();

                    MostrarRut.setText(rut);
                    MostrarNombre.setText(nombre);
                    MostrarApellido.setText(apellido);
                    MostrarFecha.setText(fecha);
                    MostrarCorreo.setText(correo);
                    MostrarPassword.setText(password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

