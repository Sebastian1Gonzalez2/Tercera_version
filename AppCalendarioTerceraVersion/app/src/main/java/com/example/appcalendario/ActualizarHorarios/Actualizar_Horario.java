package com.example.appcalendario.ActualizarHorarios;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcalendario.R;
import com.example.appcalendario.horario.agregar_horario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Actualizar_Horario extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView txtIdHorario, txtUidUsuarioAC, txtCorreoUsuarioAC, txtFechaHoraAC, txtFechaHorarioAC, txtestadoAC, estado_nuevo, txthoraHorario;
    EditText editSesionAC, editPacienteAC, editInformacionAC;
    Button btnActualizarFechaAC;
    Spinner SpHoraAc, spinner_estado;

    int dia, mes, anio;

    ArrayList<String> spinnerArray;

    final int STARTHOUR = 9;

    String id_horario_AC, uid_usuario_AC, correo_usuario_AC, fecha_registro_AC, sesion_AC, paciente_AC,
            informacion_AC, fechaHorario_AC, HoraHorario_AC, estadoAC;

    ImageView horario_finalizado, horario_pendiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_horario);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actualizar Horario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        inicializarVariable();
        RecuperarDatos();
        setearDatos();
        HoraActualizar();
        comprobarestadodelhorario();
        spinner_estado();
    }

    private void inicializarVariable() {

        txtIdHorario = findViewById(R.id.txtidHorario);
        txtFechaHorarioAC = findViewById(R.id.txtFechaHorarioAC);
        txtUidUsuarioAC = findViewById(R.id.txtUidUsuarioAC);
        txtCorreoUsuarioAC = findViewById(R.id.txtCorreoIdAC);
        txtFechaHoraAC = findViewById(R.id.txtFechaHoraActualAC);
        txtestadoAC = findViewById(R.id.txtEstadoAC);
        editSesionAC = findViewById(R.id.EditSesionAC);
        editPacienteAC = findViewById(R.id.EditPacienteAC);
        editInformacionAC = findViewById(R.id.EditInformacionAC);
        btnActualizarFechaAC = findViewById(R.id.btnFechaHorarioAC);
        txthoraHorario = findViewById(R.id.txtHoraHorario);
        SpHoraAc = findViewById(R.id.SPHoraHorarioAC);

        estado_nuevo = findViewById(R.id.estado_nuevo);
        spinner_estado = findViewById(R.id.Spinner_estado);
        horario_finalizado = findViewById(R.id.HorarioTerminado);
        horario_pendiente = findViewById(R.id.HorarioPendiente);

        btnActualizarFechaAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Horario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int AnioSeleccionado, int MesSeleccionado, int DiaSeleccionado) {

                        String diaformateado, mesFormateado;

                        if(DiaSeleccionado < 10){
                            diaformateado = "0"+String.valueOf(DiaSeleccionado);
                        }else{
                            diaformateado = String.valueOf(DiaSeleccionado);
                        }
                        int Mes = MesSeleccionado + 1;

                        if(Mes < 10){
                            mesFormateado = "0"+String.valueOf(Mes);
                        }else{
                            mesFormateado = String.valueOf(Mes);
                        }
                        txtFechaHorarioAC.setText(diaformateado + "/" + mesFormateado + "/" + AnioSeleccionado);
                    }
                }
                        ,anio,mes,dia);
                datePickerDialog.show();
            }
        });
    }

    private void RecuperarDatos() {
        Bundle intent = getIntent().getExtras();

        id_horario_AC = intent.getString("id_horario");
        uid_usuario_AC = intent.getString("uid_usuario");
        correo_usuario_AC = intent.getString("correo_usuario");
        fecha_registro_AC = intent.getString("fecha_hora_actual");
        sesion_AC = intent.getString("sesion");
        paciente_AC = intent.getString("paciente");
        informacion_AC = intent.getString("informacion");
        fechaHorario_AC = intent.getString("fecha_horario");
        HoraHorario_AC = intent.getString("hora_horario");
        estadoAC = intent.getString("estado");
    }

    private void setearDatos() {

        txtIdHorario.setText(id_horario_AC);
        txtUidUsuarioAC.setText(uid_usuario_AC);
        txtCorreoUsuarioAC.setText(correo_usuario_AC);
        txtFechaHoraAC.setText(fecha_registro_AC);
        editSesionAC.setText(sesion_AC);
        editPacienteAC.setText(paciente_AC);
        editInformacionAC.setText(informacion_AC);
        txtFechaHorarioAC.setText(fechaHorario_AC);
        txthoraHorario.setText(HoraHorario_AC);
        txtestadoAC.setText(estadoAC);
    }
    private void HoraActualizar() {

        spinnerArray = new ArrayList<>();

        spinnerArray.add(HoraHorario_AC);

        for (int i = STARTHOUR; i <= 20; i++) {
            String hour = format("%02d", i) + ":00 hrs";
            spinnerArray.add(hour);
        }

        SpHoraAc = findViewById(R.id.SPHoraHorarioAC);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinnerArray
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        SpHoraAc.setAdapter(adapter);
    }

    private void ActualizarHorarioBD(){
        String sesionActualizar = editSesionAC.getText().toString();
        String pacienteActualizar = editPacienteAC.getText().toString();
        String informacionActualizar = editInformacionAC.getText().toString();
        String fechaActualizar = txtFechaHorarioAC.getText().toString();
        String horaActualizar = SpHoraAc.getSelectedItem().toString();
        String estadoActualizar = estado_nuevo.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Horarios");

        Query query = databaseReference.orderByChild("id_horario").equalTo(id_horario_AC);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("sesion").setValue(sesionActualizar);
                    ds.getRef().child("paciente").setValue(pacienteActualizar);
                    ds.getRef().child("informacion").setValue(informacionActualizar);
                    ds.getRef().child("fecha_horario").setValue(fechaActualizar);
                    ds.getRef().child("hora_horario").setValue(horaActualizar);
                    ds.getRef().child("estado").setValue(estadoActualizar);
                }
                Toast.makeText(Actualizar_Horario.this, "Horario actualizado con exito", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void comprobarestadodelhorario(){
        String estado_horario = txtestadoAC.getText().toString();

        if (estado_horario.equals("Pendiente")){
            horario_pendiente.setVisibility(View.VISIBLE);
        }
        if (estado_horario.equals("Finalizado")){
            horario_finalizado.setVisibility(View.VISIBLE);
        }
    }

    private void spinner_estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Estados_horario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapter);
        spinner_estado.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String estado_actual = txtestadoAC.getText().toString();

        String posicion_1 = adapterView.getItemAtPosition(1).toString();


        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();
        estado_nuevo.setText(estado_seleccionado);

        if (estado_actual.equals("Finalizado")){
            estado_nuevo.setText(posicion_1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actualizar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actualizar_agenda_bd:
                ActualizarHorarioBD();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}