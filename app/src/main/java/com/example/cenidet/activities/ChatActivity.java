package com.example.cenidet.activities;
//Comentario
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    EditText mEditTextRecipient, mEditTextSubject, mEditTextMessage;
    Button mBtnSendEmail;
    Spinner mSpinner4;
    CardView mCardViewEmailAdminitrador;
    CircleImageView mCircleImageBack;

    String nombre = "";
    String matricula = "";
    String recuperarTitulo = "";
    String recuperarTituloCorreo = "";
    int recuperarDepartamento = 0;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    String mTipoCuenta = "";
    ArrayAdapter <CharSequence> adDepExterno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mEditTextRecipient = findViewById(R.id.editTextRecipient);
        mEditTextSubject= findViewById(R.id.editTextSubject);
        mEditTextMessage= findViewById(R.id.editTextMessage);
        mBtnSendEmail = findViewById(R.id.buttonSendEmail);
        mSpinner4= findViewById(R.id.spinner4);
        mCardViewEmailAdminitrador = findViewById(R.id.cardViewEmailAdministrador);
        mCircleImageBack = findViewById(R.id.cricleimageback2);

        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        getUserTipocuenta();

        recuperarTituloCorreo = "";
        recuperarTitulo = getIntent().getStringExtra("variable_Titulo");
        recuperarTituloCorreo = getIntent().getStringExtra("variable_TipoCorreo");


        //Recupera el departamento al que se enviara el correo
        recuperarDepartamento = getIntent().getIntExtra("departamento", 0);

        mSpinner4.setSelection(recuperarDepartamento);


        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (recuperarTitulo == null) {
            mEditTextSubject.setText("Informacion sobre:... (Desde la app)");
        } else {
            mEditTextSubject.setText(recuperarTitulo + " (Desde la app)");
            mEditTextSubject.setEnabled(false);
            if(recuperarTituloCorreo != null){
                getUser();
            }
        }

        mSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Direccion")){
                    mEditTextRecipient.setText("acad_cenidet@tecnm.mx");
                }else{
                    if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Subdireccion Academico")){
                        mEditTextRecipient.setText("acad_cenidet@tecnm.mx");
                    }else{
                        if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Ingenieria Electronica")){
                            mEditTextRecipient.setText("die@cenidet.tecnm.mx");
                        }else{
                            if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Ingenieria Mecanica")){
                                mEditTextRecipient.setText("dim@cenidet.tecnm.mx");
                            }else{
                                if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Ciencias Computacionales")){
                                    mEditTextRecipient.setText("dcc@cenidet.tecnm.mx");
                                }else{
                                    if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Dess. Academico e Idiomas")){
                                        mEditTextRecipient.setText("dda_cenidet@tecnm.mx");
                                    }else{
                                        if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Org. y Seguimiento de Estudios")){
                                            mEditTextRecipient.setText("depi_cenidet@tecnm.mx");
                                        }else{
                                            if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Subdir. de Planeacion y Vincluacion")){
                                                mEditTextRecipient.setText("plan_cenidet@tecnm.mx");
                                            }else{
                                                if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Oficina de Centro de Com. y Telec.")){
                                                    mEditTextRecipient.setText("cc_cenidet@tecnm.mx");
                                                }else{
                                                    if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Centro de Informacion")){
                                                        mEditTextRecipient.setText("ci_cenidet@tecnm.mx");
                                                    }else{
                                                        if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Plan., Prog. y Presupestacion")){
                                                            mEditTextRecipient.setText("pl_cenidet@tecnm.mx");
                                                        }else{
                                                            if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Gest. Tecn. y Vinculacion")){
                                                                mEditTextRecipient.setText("vin_cenidet@tecnm.mx");
                                                            }else{
                                                                if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Comunicacion y Eventos")){
                                                                    mEditTextRecipient.setText("cyd_cenidet@tecnm.mx");
                                                                }else{
                                                                    if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Subdir. de Servicios Administrativos")){
                                                                        mEditTextRecipient.setText("admon_cenidet@tecnm.mx");
                                                                    }else{
                                                                        if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Servicios Escolares")){
                                                                            mEditTextRecipient.setText("se_cenidet@tecnm.mx");
                                                                        }else{
                                                                            if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Recursos Materiales y Servicios")){
                                                                                mEditTextRecipient.setText("rm_cenidet@tecnm.mx");
                                                                            }else{
                                                                                if(mSpinner4.getSelectedItem().toString().equalsIgnoreCase("Dep. de Recursos Humanos")){
                                                                                    mEditTextRecipient.setText("rh_cenidet@tecnm.mx");
                                                                                }else{
                                                                                    mEditTextRecipient.setText("rf_cenidet@tecnm.mx");
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBtnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = mEditTextRecipient.getText().toString().trim();
                String subject = mEditTextSubject.getText().toString().trim();
                String message = mEditTextMessage.getText().toString().trim();

                sendEmail(recipient, subject, message);

            }
        });

    }
    ///////////////////////////////////////////////------------------------------------------------------------

    private void sendEmail(String recipient, String subject, String message) {
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plain");

        mEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mEmailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(mEmailIntent, "Choose an Email Client"));
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("matricula")) {
                        String matricula2 = documentSnapshot.getString("matricula");
                        matricula = matricula2;
                    }
                    if (documentSnapshot.contains("username")) {
                        String nombre2 = documentSnapshot.getString("username");
                        nombre = nombre2;
                    }
                    if(recuperarTitulo.equals("Solicitud de Constancia")){
                        Toast.makeText(getApplicationContext(), "Por favor agregue la información necesaria para el tipo de constancia", Toast.LENGTH_LONG).show();
                        mEditTextMessage.setText("A quien corresponda.\n" +
                                "\n" +
                                "Por medio del presente, solicito atentamente la emisión de constancia (Expecificar el tipo de constancia)"+"\n" +
                                "Nombre: "+ nombre + "\n" +
                                "Matrícula: "+ matricula +  "\n" +
                                "Departamento: " +  "\n" +
                                "Generacion: " +  "\n" +
                                "\n" +
                                "Agradezco de antemano la atención al presente." +
                                "\n" + "\n");

                    }
                    if(recuperarTitulo.equals("Solicitud de Revisión de Expediente")){
                        mEditTextMessage.setText("A quien corresponda.\n" +
                                "\n" +
                                "Por medio del presente, solicito atentamente la revisión de mi expediente, pues deseo presentar mi examen de grado en próximas fechas."+"\n" +
                                "\n" +
                                "Agradezco de antemano la atención al presente." +
                                "\n" + "\n" +
                                "Nombre: "+ nombre + "\n" +
                                "Matrícula: "+ matricula );
                    }
                }
            }
        });
    }
    private void getUserTipocuenta(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if(documentSnapshot.contains("tipocuenta")){
                        mTipoCuenta = documentSnapshot.getString("tipocuenta");
                        //Toast.makeText(getApplicationContext(), "Usuario: " + mTipoCuenta, Toast.LENGTH_LONG).show();
                        if(mTipoCuenta.equals("Externo")){
                            //Toast.makeText(getApplicationContext(), "Todo bien", Toast.LENGTH_LONG).show();
                            adDepExterno = ArrayAdapter.createFromResource(ChatActivity.this, R.array.departamento_externos, android.R.layout.simple_spinner_item);
                            adDepExterno.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mSpinner4.setAdapter(adDepExterno);
                        }
                    }
                }
            }
        });
    }
}