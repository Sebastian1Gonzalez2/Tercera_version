package com.example.appcalendario.Detalles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcalendario.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

public class Detalle_horario extends AppCompatActivity {

    TextView txtid_horarioD, txtUid_usuarioD, txtCorreo_usuarioD, txtFechaRegistroD, txtSesionD, txtPacienteD, txtinformacionD, txtFechaD, txtHoraD, txtEstadoD;

    String id_horario_D, uid_usuario_D, correo_usuario_D, fecha_registro_D, sesion_D, paciente_D,
            informacion_D, fechaHorario_D, HoraHorario_D, estadoD;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button btn_importante;

    boolean ComprobarHorarioImportante = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_horario);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalles");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

       InicializarVariables();
       RecuperarDatos();
       SetearDatos();
       verificarHorarioImportante();

       btn_importante.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(ComprobarHorarioImportante){
                   Eliminar_horario_importante();
               }else {
                   agregar_horarios_importantes();
               }
           }
       });
    }

    private void InicializarVariables(){

        txtid_horarioD = findViewById(R.id.Id_horario_Detalle);
        txtUid_usuarioD = findViewById(R.id.Uid_usuario_Detalle);
        txtCorreo_usuarioD = findViewById(R.id.Correo_usuario_Detalle);
        txtFechaRegistroD = findViewById(R.id.Fecha_Registro_Detalle);
        txtSesionD = findViewById(R.id.Sesion_Detalle);
        txtPacienteD = findViewById(R.id.Paciente_Detalle);
        txtinformacionD = findViewById(R.id.informacion_Detalle);
        txtFechaD = findViewById(R.id.fecha_horario_Detalle);
        txtHoraD = findViewById(R.id.Hora_horario_Detalle);
        txtEstadoD = findViewById(R.id.Estado_Detalle);

        btn_importante = findViewById(R.id.btn_importante);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


    }

    private void RecuperarDatos() {
        Bundle intent = getIntent().getExtras();

        id_horario_D = intent.getString("id_horario");
        uid_usuario_D = intent.getString("uid_usuario");
        correo_usuario_D = intent.getString("correo_usuario");
        fecha_registro_D = intent.getString("fecha_hora_actual");
        sesion_D = intent.getString("sesion");
        paciente_D = intent.getString("paciente");
        informacion_D = intent.getString("informacion");
        fechaHorario_D = intent.getString("fecha_horario");
        HoraHorario_D = intent.getString("hora_horario");
        estadoD = intent.getString("estado");
    }

    private void SetearDatos(){

        txtid_horarioD.setText(id_horario_D);
        txtUid_usuarioD.setText(uid_usuario_D);
        txtCorreo_usuarioD.setText(correo_usuario_D);
        txtFechaRegistroD.setText(fecha_registro_D);
        txtSesionD.setText(sesion_D);
        txtPacienteD.setText(paciente_D);
        txtinformacionD.setText(informacion_D);
        txtFechaD.setText(fechaHorario_D);
        txtHoraD.setText(HoraHorario_D);
        txtEstadoD.setText(estadoD);

    }

    private void agregar_horarios_importantes(){
        if(user == null){
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else{
            Bundle intent = getIntent().getExtras();

            id_horario_D = intent.getString("id_horario");
            uid_usuario_D = intent.getString("uid_usuario");
            correo_usuario_D = intent.getString("correo_usuario");
            fecha_registro_D = intent.getString("fecha_hora_actual");
            sesion_D = intent.getString("sesion");
            paciente_D = intent.getString("paciente");
            informacion_D = intent.getString("informacion");
            fechaHorario_D = intent.getString("fecha_horario");
            HoraHorario_D = intent.getString("hora_horario");
            estadoD = intent.getString("estado");

            String identificador_horario_importante = uid_usuario_D+paciente_D;

            HashMap<String, String> horario_importante = new HashMap<>();
            horario_importante.put("id_horario", id_horario_D);
            horario_importante.put("uid_usuario", uid_usuario_D);
            horario_importante.put("correo_usuario", correo_usuario_D);
            horario_importante.put("fecha_hora_actual", fecha_registro_D);
            horario_importante.put("sesion", sesion_D);
            horario_importante.put("paciente", paciente_D);
            horario_importante.put("informacion", informacion_D);
            horario_importante.put("fecha_horario", fechaHorario_D);
            horario_importante.put("hora_horario", HoraHorario_D);
            horario_importante.put("estado", estadoD);
            horario_importante.put("id_horario_importante", identificador_horario_importante);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
            reference.child(firebaseAuth.getUid()).child("Horarios importantes").child(identificador_horario_importante)
                    .setValue(horario_importante)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_horario.this, "Se ha añadido a horarios importantes", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Detalle_horario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void Eliminar_horario_importante(){
        if (user == null){
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else{
            Bundle intent = getIntent().getExtras();
            id_horario_D = intent.getString("id_horario");
            paciente_D = intent.getString("paciente");

            String identificador_horario_importante = uid_usuario_D+paciente_D;

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
            reference.child(firebaseAuth.getUid()).child("Horarios importantes").child(identificador_horario_importante)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_horario.this, "El horario ya no es importante", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Detalle_horario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void verificarHorarioImportante(){
        if (user == null){
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else{
            Bundle intent = getIntent().getExtras();
            uid_usuario_D = intent.getString("uid_usuario");
            paciente_D = intent.getString("paciente");

            String identificador_horario_importante = uid_usuario_D+paciente_D;

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
            reference.child(firebaseAuth.getUid()).child("Horarios importantes").child(identificador_horario_importante)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ComprobarHorarioImportante = snapshot.exists();
                            if (ComprobarHorarioImportante){
                                String importante = "Importante";
                                btn_importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.importante_icono,0,0);
                                btn_importante.setText(importante);
                            }else{
                                String no_importante = "No importante";
                                btn_importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.noimportante_icono,0,0);
                                btn_importante.setText(no_importante);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}