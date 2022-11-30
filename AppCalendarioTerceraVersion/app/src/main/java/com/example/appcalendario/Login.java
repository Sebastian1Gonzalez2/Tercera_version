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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText CorreoLogin, PassLogin;
    Button btnLogin;
    TextView UsuarioIngresadoTXT;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    String correo = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zona de ingreso");

        CorreoLogin = findViewById(R.id.txtCorreoLogin);
        PassLogin = findViewById(R.id.txtContraseñaLogin);
        btnLogin = findViewById(R.id.btnIniciarSesion);
        UsuarioIngresadoTXT = findViewById(R.id.UsuarioIngresoTXT);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere un momento..");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        UsuarioIngresadoTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registrar.class));
            }
        });
    }

    private void ValidarDatos() {

        correo = CorreoLogin.getText().toString();
        password = PassLogin.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginDeUsuario();
        }

    }
    private void LoginDeUsuario() {
        progressDialog.setMessage("Ingresando la sesion...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          progressDialog.dismiss();
                          FirebaseUser user = firebaseAuth.getCurrentUser();
                          startActivity(new Intent(Login.this,MenuAgenda.class));
                          Toast.makeText(Login.this, "Bienvenido(a)", Toast.LENGTH_SHORT).show();
                          finish();
                      }
                      else {
                          progressDialog.dismiss();
                          Toast.makeText(Login.this, "Verifique si el rut y contraseña estan de forma correcta", Toast.LENGTH_SHORT).show();
                      }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}