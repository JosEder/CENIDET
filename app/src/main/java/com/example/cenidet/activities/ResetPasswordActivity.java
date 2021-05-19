 package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText mTextInputEmail;
    private Button mButtonResetPassword;
    private String email = "";

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        mTextInputEmail = (TextInputEditText) findViewById(R.id.textInputEmail);
        mButtonResetPassword = (Button) findViewById(R.id.btnResetPassword);

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mTextInputEmail.getText().toString();
                if(!email.isEmpty()){
                    if(isEmailValid(email)){
                        mDialog.setMessage("Espere un momento...");
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                        resetPassword();
                    }else {
                        Toast.makeText(ResetPasswordActivity.this,"Por favor ingrese un correo valido", Toast.LENGTH_LONG).show();
                        //mostrarToast("Coreo electrónico inválido");
                    }
                }else {
                    Toast.makeText(ResetPasswordActivity.this,"Por favor ingrese un correo", Toast.LENGTH_LONG).show();
                    //mostrarToast("Por favor ingrese un correo electrónico");
                }
            }
        });
    }

    private void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this,"Se ha enviado un correo para reestablecer su contraseña", Toast.LENGTH_LONG).show();
                    //mostrarToast("Se ha enviado un correo para reestablecer su contraseña");
                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ResetPasswordActivity.this,"No se ha podido enviar el correo de restablecer contraseña", Toast.LENGTH_LONG).show();
                    //mostrarToast("No se ha podido enviar el correo de restablecer contraseña");
                }
                mDialog.dismiss();
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void mostrarToast(String s){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toastLayout));
        TextView tv=layout.findViewById(R.id.textview);
        tv.setText(s);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}