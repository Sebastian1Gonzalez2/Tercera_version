package com.example.appcalendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionScene;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class actualizarPassword extends AppCompatActivity {

    TextView lbRutMostrar, lbPasswordMostrar;
    EditText PasswordNuevo, PasswordActual;
    Button btnCambiarPassword;
    DatabaseReference USUARIOS;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actualizar Contraseña");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        lbRutMostrar = findViewById(R.id.RutMostrar);
        lbPasswordMostrar = findViewById(R.id.PasswordMostrar);
        PasswordActual = findViewById(R.id.editPassword);
        PasswordNuevo = findViewById(R.id.editConfirmPassword);
        btnCambiarPassword = findViewById(R.id.btnActualizarPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        USUARIOS = FirebaseDatabase.getInstance().getReference("USUARIOS");

        progressDialog = new ProgressDialog(actualizarPassword.this);


        Query query = USUARIOS.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String rut = ""+ds.child("rut").getValue();
                    String pass = ""+ds.child("password").getValue();

                    lbRutMostrar.setText(rut);
                    lbPasswordMostrar.setText(pass);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PASS_ANTERIOR = PasswordActual.getText().toString().trim();
                String NUEVO_PASS = PasswordNuevo.getText().toString().trim();
                
                if (TextUtils.isEmpty(PASS_ANTERIOR)){
                    Toast.makeText(actualizarPassword.this, "Rellene el campo con su contraseña actual", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(NUEVO_PASS)){
                    Toast.makeText(actualizarPassword.this, "Rellene el campo de la nueva constraseña", Toast.LENGTH_SHORT).show();
                }
                if (!NUEVO_PASS.equals("")&& NUEVO_PASS.length() >= 6){
                    Cambio_de_Password(PASS_ANTERIOR,NUEVO_PASS);

                }else{
                    PasswordNuevo.setError("La contraseña debe ser mayor a 6 caracteres");
                    PasswordNuevo.setFocusable(true);
                }
            }
        });
    }
    private void Cambio_de_Password(String pass_anterior, String nuevo_pass){
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),pass_anterior);
   user.reauthenticate(authCredential)
           .addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   user.updatePassword(nuevo_pass)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   progressDialog.dismiss();
                                   String value = PasswordNuevo.getText().toString().trim();
                                   HashMap<String, Object> result = new HashMap<>();
                                   result.put("password", value);

                                   USUARIOS.child(user.getUid()).updateChildren(result)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   Toast.makeText(actualizarPassword.this, "Cambiado con exito, inicie sesion con la nueva contraseña", Toast.LENGTH_SHORT).show();
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           progressDialog.dismiss();
                                       }
                                   });
                                   firebaseAuth.signOut();
                                   startActivity(new Intent(actualizarPassword.this, Login.class));
                                   finish();
                               }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {

                                   progressDialog.dismiss();
                               }
                           });
               }
           }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
           progressDialog.dismiss();
           Toast.makeText(actualizarPassword.this, "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
       }
   });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}