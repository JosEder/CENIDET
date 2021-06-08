package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.models.User;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputMatricula;
    Button mButtonRegister;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextInputUsername = findViewById(R.id.textInputUsername2);
        //mTextInputMatricula = findViewById(R.id.textInputMatricula2);
        mButtonRegister = findViewById(R.id.btnRegister2);

        mAuthProvider = new AuthProvider();
        mUsersProvider= new UsersProvider();

        mDialog= new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        //Toast.makeText(this, "Se entro en la actividad CompleteProfileActivity", Toast.LENGTH_SHORT).show();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Se preciono el boton *Confirmar*", Toast.LENGTH_SHORT).show();
                register();
            }
        });
    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        //String matricula = mTextInputMatricula.getText().toString();
        if(!username.isEmpty()) {
            //Toast.makeText(this, "se registro usuario exitosamente", Toast.LENGTH_SHORT).show();
            updateUser(username);
        }
        else {
            Toast.makeText(this, "Para continuar ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username) {
        String id = mAuthProvider.getUid();
        User user= new User();
        user.setUsername(username);
        user.setId(id);
        //user.setMatricula(matricula);
        user.setTimestamp(new Date().getTime());
        user.setTipocuenta("Externo");
        //Toast.makeText(getApplicationContext(), "Se actualizo las informacion del usuario", Toast.LENGTH_SHORT).show();
        mDialog.show();
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}