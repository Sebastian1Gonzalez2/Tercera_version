package com.example.appcalendario.ListaHorarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
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
import com.example.appcalendario.Objetos.horario;
import com.example.appcalendario.R;
import com.example.appcalendario.ViewHolder.ViewHolder_Horario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Horarios extends AppCompatActivity {

    RecyclerView recyclerViewhorarios;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_horarios;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<horario, ViewHolder_Horario> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<horario> options;

    Dialog dialog;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_horarios);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de horarios");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewhorarios = findViewById(R.id.Listahorarios);
        recyclerViewhorarios.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_horarios = firebaseDatabase.getReference("Horarios");

        dialog = new Dialog(Listar_Horarios.this);
        ListaDeHorarios();
    }

    private void ListaDeHorarios(){
        Query query = BD_horarios.orderByChild("uid_usuario").equalTo(user.getUid());
        options = new FirebaseRecyclerOptions.Builder<horario>().setQuery(query, horario.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<horario, ViewHolder_Horario>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Horario viewHolder_Horario, int position, @NonNull horario Horario) {
                viewHolder_Horario.SetearDatos(
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
            public ViewHolder_Horario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
                ViewHolder_Horario viewHolder_horario = new ViewHolder_Horario(view);
                viewHolder_horario.setmClickListener(new ViewHolder_Horario.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
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

                        Intent intent = new Intent(Listar_Horarios.this,Detalle_horario.class);
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
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

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


                        Button btn_eliminarH, btn_actualizarH;

                        dialog.setContentView(R.layout.dialogo_opciones);

                        btn_eliminarH = dialog.findViewById(R.id.btnEliminarHorario);
                        btn_actualizarH = dialog.findViewById(R.id.btnActualizarHorario);

                        btn_eliminarH.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EliminarHorario(id_horario);
                                dialog.dismiss();
                            }
                        });

                        btn_actualizarH.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Listar_Horarios.this, "Actualizar horario", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Listar_Horarios.this, Actualizar_Horario.class));
                                Intent intent = new Intent(Listar_Horarios.this,Actualizar_Horario.class);
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
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                return viewHolder_horario;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Listar_Horarios.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerViewhorarios.setLayoutManager(linearLayoutManager);
        recyclerViewhorarios.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarHorario(String id_horario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Horarios.this);
        builder.setTitle("Eliminar horario");
        builder.setMessage("¿Desea eliminar el horario?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = BD_horarios.orderByChild("id_horario").equalTo(id_horario);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Horarios.this, "Horario eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Horarios.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Horarios.this, "Su eliminacion ha sido cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}