package com.example.appcalendario.HorariosImportantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appcalendario.ActualizarHorarios.Actualizar_Horario;
import com.example.appcalendario.Detalles.Detalle_horario;
import com.example.appcalendario.ListaHorarios.Listar_Horarios;
import com.example.appcalendario.MenuAgenda;
import com.example.appcalendario.Objetos.horario;
import com.example.appcalendario.R;
import com.example.appcalendario.ViewHolder.VieHolder_Horario_Importante;
import com.example.appcalendario.ViewHolder.ViewHolder_Horario;
import com.example.appcalendario.horario.agregar_horario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class horarios_importantes extends AppCompatActivity {

    RecyclerView RecyclerViewHorariosImportantes;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference mis_usuario;
    DatabaseReference Horarios_importantes;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<horario, VieHolder_Horario_Importante> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<horario> firebaseRecyclerOptions;

    LinearLayoutManager linearLayoutManager;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios_importantes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de horarios importantes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerViewHorariosImportantes = findViewById(R.id.RecycleViewHorariosImportantes);
        RecyclerViewHorariosImportantes.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        mis_usuario = firebaseDatabase.getReference("USUARIOS");
        Horarios_importantes = firebaseDatabase.getReference("Horarios importantes");

        dialog = new Dialog(horarios_importantes.this);

        ComprobarUsuario();

    }

    private void ComprobarUsuario(){
        if (user == null){
            Toast.makeText(com.example.appcalendario.HorariosImportantes.horarios_importantes.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else{
            ListaHorariosImportantes();
        }
    }

    private void ListaHorariosImportantes() {

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<horario>().setQuery(mis_usuario.child(user.getUid()).child("Horarios importantes"), horario.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<horario, VieHolder_Horario_Importante>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull VieHolder_Horario_Importante vieHolder_horario_importante, int position, @NonNull horario Horario) {
                vieHolder_horario_importante.SetearDatos(
                        getApplicationContext(),
                        Horario.getId_horario(),
                        Horario.getUid_usuario(),
                        Horario.getCorreo_usuario(),
                        Horario.getFecha_hora_actual(),
                        Horario.getSesion(),
                        Horario.getPaciente(),
                        Horario.getInformacion(),
                        Horario.getFecha_horario(),
                        Horario.getHora_horario(),
                        Horario.getEstado()
                );

            }
            @Override
            public VieHolder_Horario_Importante onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario_importante,parent,false);
                VieHolder_Horario_Importante viewHolder_horario_importante = new VieHolder_Horario_Importante(view);
                viewHolder_horario_importante.setmClickListener(new VieHolder_Horario_Importante.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                    }
                    @Override
                    public void onItemLongClick(View view, int position) {

                        Button EliminarHorario , EliminarHorarioCancelar;

                        dialog.setContentView(R.layout.cuadro_dialogo_eliminar_horario_importante);

                        EliminarHorario = dialog.findViewById(R.id.EliminarHorarioImportante);
                        EliminarHorarioCancelar = dialog.findViewById(R.id.EliminarHorarioCancelar);

                        EliminarHorario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id_horario = getItem(position).getId_horario();
                                String uid_usuario = getItem(position).getUid_usuario();
                                String correo_usuario = getItem(position).getCorreo_usuario();
                                String fecha_registro = getItem(position).getFecha_hora_actual();
                                String sesion = getItem(position).getSesion();
                                String paciente = getItem(position).getPaciente();
                                String informacion = getItem(position).getInformacion();
                                String fecha_horario = getItem(position).getFecha_horario();
                                String hora_horario = getItem(position).getHora_horario();
                                String estado = getItem(position).getEstado();

                                Intent intent = new Intent(horarios_importantes.this,Detalle_horario.class);
                                intent.putExtra("id_horario", id_horario);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_hora_actual", fecha_registro);
                                intent.putExtra("sesion", sesion);
                                intent.putExtra("paciente", paciente);
                                intent.putExtra("informacion", informacion);
                                intent.putExtra("fecha_horario", fecha_horario);
                                intent.putExtra("hora_horario", hora_horario);
                                intent.putExtra("estado", estado);
                                startActivity(intent);
                                Toast.makeText(horarios_importantes.this, "Apreta el boton de abajo para eliminarlo", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        EliminarHorarioCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(horarios_importantes.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                return viewHolder_horario_importante;
            }
        };
        linearLayoutManager = new LinearLayoutManager(horarios_importantes.this, LinearLayoutManager.VERTICAL, false);

        RecyclerViewHorariosImportantes.setLayoutManager(linearLayoutManager);
        RecyclerViewHorariosImportantes.setAdapter(firebaseRecyclerAdapter);

    }
    private void EliminarHorario(String id_horario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(horarios_importantes.this);
        builder.setTitle("Eliminar horario importante");
        builder.setMessage("¿Desea eliminar el horario importante?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
                        reference.child(id_horario).child("Horarios importantes").child(firebaseAuth.getUid())
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(horarios_importantes.this, "El horario ya no es importante", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(horarios_importantes.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(horarios_importantes.this, "Su eliminacion ha sido cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {

        if(firebaseRecyclerAdapter !=null){
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}