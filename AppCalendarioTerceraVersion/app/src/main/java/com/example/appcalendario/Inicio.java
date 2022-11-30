package com.example.appcalendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    Button BtnLogin, BtnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuinicioapp);

        BtnLogin = findViewById(R.id.Btn_Login);
        BtnRegistro = findViewById(R.id.Btn_Registro);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.appcalendario.Login.class);
                startActivity(intent);
            }
        });
        BtnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.appcalendario.Registrar.class);
                startActivity(intent);
            }
        });
    }
}