package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.models.User;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mcircleImageViewBack;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    TextInputEditText getmTextInputMatricula;
    LinearLayout mLinearLayoutMatricula;
    Spinner mSpinner;
    Button mButtonRegister;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mcircleImageViewBack=findViewById(R.id.cricleimageback);
        mTextInputEmail=findViewById(R.id.textInputEmail);
        mTextInputUsername=findViewById(R.id.textInputUsername2);
        mTextInputPassword=findViewById(R.id.textInputPassword);
        mTextInputConfirmPassword=findViewById(R.id.textInputConfirmPassword);
        getmTextInputMatricula=findViewById(R.id.textInputMatricula);
        mSpinner = findViewById(R.id.spinner4);
        mLinearLayoutMatricula = findViewById(R.id.linearLayoutMatricula);
        mButtonRegister=findViewById(R.id.btnRegister2);
        getmTextInputMatricula.setHint("Matricula");

        mAuthProvider=new AuthProvider();
        mUsersProvider= new UsersProvider();

        mDialog= new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinner.getSelectedItem().toString().equalsIgnoreCase("Estudiante")){
                    //getmTextInputMatricula.setHint("");
                    getmTextInputMatricula.setHint("Matricula");
                }else{
                    //getmTextInputMatricula.setHint("");
                    getmTextInputMatricula.setHint("Id Maestro");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private  void register(){
        String username = mTextInputUsername.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        String confirmPassword = mTextInputConfirmPassword.getText().toString();
        String matricula = getmTextInputMatricula.getText().toString();
        String tipocuenta = mSpinner.getSelectedItem().toString();


        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !matricula.isEmpty()){
            if(isEmailValid(email)){
                if(password.equals(confirmPassword)){
                    if(password.length()>=6){
                        createUser(username, email, password, matricula,tipocuenta);
                    }else{
                        //mostrarToast("Error\nLa contraseña debe tener al menos 6 caracteres");
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    //mostrarToast("Error\nLas contraseñas no coinciden");
                }
            }else{
                Toast.makeText(this, "Has insertado todos los campos pero el correo no es valido", Toast.LENGTH_LONG).show();
                //mostrarToast("Correo electrónico no valido");
            }
        }else{
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_LONG).show();
            //mostrarToast("Para continuar inserte todos los campos");
        }
    }

    private void createUser(final String username, final String email, final String password, final  String matricula, final String tipocuenta){
        mDialog.show();
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id =mAuthProvider.getUid();
                    //El spinner es para saber si es alumno o maestro
                    mSpinner.getSelectedItem().toString();
                    User user= new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setMatricula(matricula);
                    user.setTipocuenta(tipocuenta);
                    user.setTimestamp(new Date().getTime());
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                //Se envia el correo de verificacion de correo
                                mAuthProvider.getUserSesion().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente\nPor favor revise su correo para verificación", Toast.LENGTH_LONG).show();
                                            //mostrarToast("Usuario registrado correctamente\\nPor favor revise su correo para verificación");
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            //mostrarToast("Error\n" + task.getException().getMessage());
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usario en la base de datos", Toast.LENGTH_LONG).show();
                                //mostrarToast("No se pudo almacenar el usario en la base de datos");
                            }
                        }
                    });

                }else{
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                    //mostrarToast("No se pudo registrar el usuario");
                }
            }
        });
    }

    /*
        VERIFICAR QUE SEA UN EMAIL VALIDO
     */
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@cenidet.tecnm.mx";
        //String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
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
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}