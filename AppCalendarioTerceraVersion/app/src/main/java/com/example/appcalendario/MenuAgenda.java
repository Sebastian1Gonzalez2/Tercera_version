package com.example.appcalendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcalendario.HorariosImportantes.horarios_importantes;
import com.example.appcalendario.ListaHorarios.Listar_Horarios;
import com.example.appcalendario.horario.agregar_horario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuAgenda extends AppCompatActivity {

    Button ingresarAgenda,horariosimportantes,listarAgenda,MostrarDatos;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BaseDeDatos;

    TextView UidPerfil,RutPerfil,CorreoPerfil,NombrePerfil,CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agenda);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Menu Principal");

        CerrarSesion = findViewById(R.id.txtCerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BaseDeDatos = firebaseDatabase.getReference("USUARIOS");

        ingresarAgenda = findViewById(R.id.btnAgendar);
        horariosimportantes = findViewById(R.id.btnConsulta);
        listarAgenda = findViewById(R.id.btnMostrarAgenda);
        MostrarDatos = findViewById(R.id.btnMostrarDatos);

        UidPerfil = findViewById(R.id.UidPerfil);
        RutPerfil = findViewById(R.id.rutPerfil);
        CorreoPerfil = findViewById(R.id.correoPerfil);
        NombrePerfil = findViewById(R.id.nombrePerfil);
        CargarDatosMenu();

        ingresarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid_usuario = UidPerfil.getText().toString();
                String correo_usuario = CorreoPerfil.getText().toString();

                Intent intent = new Intent(MenuAgenda.this, agregar_horario.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });
        listarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuAgenda.this, Listar_Horarios.class));
            }
        });
        MostrarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuAgenda.this, mostrarDatos.class));
            }
        });
        horariosimportantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuAgenda.this, horarios_importantes.class));
            }
        });
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });
    }
    private void CargarDatosMenu(){
        Query query = BaseDeDatos.orderByChild("uid").equalTo(firebaseUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                    String Uid = ""+ds.child("uid").getValue();
                    String Rut = ""+ds.child("rut").getValue();
                    String Correo = ""+ds.child("correo").getValue();
                    String Nombre = ""+ds.child("nombre").getValue();
                    String Apellido = ""+ds.child("apellido").getValue();

                    UidPerfil.setText(Uid);
                    RutPerfil.setText(Rut);
                    CorreoPerfil.setText(Correo);
                    NombrePerfil.setText(Nombre + " " + Apellido);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void SalirAplicacion() {

        firebaseAuth.signOut();
        startActivity(new Intent(MenuAgenda.this, Inicio.class));
        Toast.makeText(this, "Cerraste sesion exitosamente", Toast.LENGTH_SHORT).show();
    }
}