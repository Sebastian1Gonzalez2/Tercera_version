package com.example.appcalendario.horario;


import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcalendario.Objetos.horario;
import com.example.appcalendario.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;


public class agregar_horario extends AppCompatActivity {

    TextView txtfechahorario, estado, UidUsuario, CorreoUsuario, FechaHoraActual;
    Button btnfechaH;
    EditText editSesion, editPaciente, editInformacion;

    Spinner SPHora;

    int dia, mes, anio;

    DatabaseReference BD_FireBase;

    ArrayList<String> spinnerArray;
    final int STARTHOUR = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_horario);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agregar Horario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        inicializarVariable();
        ObtenerDatos();
        Horas();
        obtenerhoraActual();

    }

    private void inicializarVariable(){

        txtfechahorario = findViewById(R.id.txtFechaHorario);
        UidUsuario = findViewById(R.id.txtUidUsuario);
        CorreoUsuario = findViewById(R.id.txtCorreoId);
        FechaHoraActual = findViewById(R.id.txtFechaHoraActual);
        estado = findViewById(R.id.txtEstado);
        editSesion = findViewById(R.id.EditSesion);
        editPaciente = findViewById(R.id.EditPaciente);
        editInformacion = findViewById(R.id.EditInformacion);
        btnfechaH = findViewById(R.id.btnFechaHorario);
        SPHora = findViewById(R.id.SPHoraHorario);

        BD_FireBase = FirebaseDatabase.getInstance().getReference();

        btnfechaH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(agregar_horario.this, new DatePickerDialog.OnDateSetListener() {
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
                        txtfechahorario.setText(diaformateado + "/" + mesFormateado + "/" + AnioSeleccionado);
                    }
                }
                ,anio,mes,dia);
                datePickerDialog.show();
            }
        });
    }

    private void ObtenerDatos(){
        String Uid_recuperado = getIntent().getStringExtra("uid");
        String Correo_recuperado = getIntent().getStringExtra("correo");

        UidUsuario.setText(Uid_recuperado);
        CorreoUsuario.setText(Correo_recuperado);
    }

    private void Horas() {
        spinnerArray = new ArrayList<>();

        spinnerArray.add(" < Seleccione la hora >");

        for (int i = STARTHOUR; i <= 20; i++) {
            String hour = format("%02d", i) + ":00 hrs";
            spinnerArray.add(hour);
        }

        SPHora = findViewById(R.id.SPHoraHorario);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                spinnerArray
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        SPHora.setAdapter(adapter);
    }

    private void obtenerhoraActual(){
        String fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a",
                Locale.getDefault()).format(System.currentTimeMillis());

        FechaHoraActual.setText(fecha_hora_registro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void AgregarHorario(){

        String uid_usuario = UidUsuario.getText().toString();
        String correo_usuario = CorreoUsuario.getText().toString();
        String fecha_hora_actual = FechaHoraActual.getText().toString();
        String sesion = editSesion.getText().toString();
        String paciente = editPaciente.getText().toString();
        String informacion = editInformacion.getText().toString();
        String fecha = txtfechahorario.getText().toString();
        String hora = SPHora.getSelectedItem().toString();
        String Estado = estado.getText().toString();

        if (!uid_usuario.equals("") && !correo_usuario.equals("") && !fecha_hora_actual.equals("") && !sesion.equals("") && !paciente.equals("") && !informacion.equals("") && !fecha.equals("")
        && !hora.equals("") && !Estado.equals("")){

            horario Horario = new horario(correo_usuario+"/"+fecha_hora_actual,
                    uid_usuario,
                    correo_usuario,
                    fecha_hora_actual,
                    sesion,
                    paciente,
                    informacion,
                    fecha,
                    hora,
                    Estado);

            String Horario_usuario = BD_FireBase.push().getKey();

            String Nombre_BD = "Horarios";

            BD_FireBase.child(Nombre_BD).child(Horario_usuario).setValue(Horario);

            Toast.makeText(this, "Se ha agregado el horario correctamente", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else{
            Toast.makeText(this, "Tiene que llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Agregar_agenda_bd:
                AgregarHorario();
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


