package com.example.cenidet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.activities.ChatActivity;
import com.example.cenidet.activities.ConvocatoriaActivity;
import com.example.cenidet.activities.ServicioTecnicoActivity;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    View mView;
    CardView mCardViewEmailAdminitrador;
    CardView mCardViewEmailNuevo;
    CardView mCardViewSolicitudCostancia;
    CardView mCardViewSolicitudServicioTecnico;
    TextView mTextViewSolicitudExpediente;
    TextView mTextViewNuevo;
    TextView mTextViewSolicitudServicioTecnico;
    TextView mTextViewSolicitudCostancia;
    String tituloCorreo = "";

    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    String mTipoCuenta;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        mCardViewEmailAdminitrador = mView.findViewById(R.id.cardViewEmailAdministrador);
        mCardViewEmailNuevo = mView.findViewById(R.id.cardViewEmailBlanco);
        mCardViewSolicitudCostancia = mView.findViewById(R.id.cardViewSolicitudConstancia);
        mCardViewSolicitudServicioTecnico = mView.findViewById(R.id.cardViewSolicitudServicioTecnico);
        mTextViewSolicitudExpediente = mView.findViewById(R.id.textViewSolicitudExpediente);
        mTextViewNuevo = mView.findViewById(R.id.textViewNuevo);
        mTextViewSolicitudCostancia = mView.findViewById(R.id.textViewSolicitudConstancia);
        mTextViewSolicitudServicioTecnico = mView.findViewById(R.id.textViewSolicitudServicioTecnico);


        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        getUser();

        try{
            mCardViewEmailNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    String tituloCorreo = mTextViewNuevo.getText().toString();
                    intent.putExtra("variable_TipoCorreo", tituloCorreo);
                    startActivity(intent);
                }
            });

            mCardViewSolicitudCostancia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tituloCorreo = mTextViewSolicitudCostancia.getText().toString();
                    //Toast.makeText(getContext(), "Prueba "+ tituloCorreo, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("variable_Titulo", "Solicitud de Constancia");
                    intent.putExtra("variable_TipoCorreo", tituloCorreo);
                    startActivity(intent);
                }
            });

            mCardViewEmailAdminitrador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tituloCorreo = mTextViewSolicitudExpediente.getText().toString();
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("variable_Titulo", "Solicitud de Revisión de Expediente");
                    intent.putExtra("variable_TipoCorreo", tituloCorreo);
                    startActivity(intent);
                }
            });

            mCardViewSolicitudServicioTecnico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"En desarrollo", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), ServicioTecnicoActivity.class);
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),"Error de tipo: "+  e, Toast.LENGTH_LONG).show();
        }

        return mView;
    }

    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if(documentSnapshot.contains("tipocuenta")){
                        mTipoCuenta = documentSnapshot.getString("tipocuenta");
                        if(mTipoCuenta.equals("ADMINISTRADOR")){
                            mCardViewEmailNuevo.setEnabled(true);
                            mCardViewEmailNuevo.setVisibility(View.VISIBLE);
                            mCardViewEmailAdminitrador.setEnabled(true);
                            mCardViewEmailAdminitrador.setVisibility(View.VISIBLE);
                            mCardViewSolicitudCostancia.setEnabled(true);
                            mCardViewSolicitudCostancia.setVisibility(View.VISIBLE);
                            mCardViewSolicitudServicioTecnico.setEnabled(true);
                            mCardViewSolicitudServicioTecnico.setVisibility(View.VISIBLE);
                        }
                        if(mTipoCuenta.equals("Administrativo")||mTipoCuenta.equals("Docente")){
                            mCardViewEmailNuevo.setEnabled(true);
                            mCardViewEmailNuevo.setVisibility(View.VISIBLE);
                            mCardViewSolicitudServicioTecnico.setEnabled(true);
                            mCardViewSolicitudServicioTecnico.setVisibility(View.VISIBLE);
                        }
                        if(mTipoCuenta.equals("Estudiante")){
                            mCardViewEmailNuevo.setEnabled(true);
                            mCardViewEmailNuevo.setVisibility(View.VISIBLE);
                            mCardViewEmailAdminitrador.setEnabled(true);
                            mCardViewEmailAdminitrador.setVisibility(View.VISIBLE);
                            mCardViewSolicitudCostancia.setEnabled(true);
                            mCardViewSolicitudCostancia.setVisibility(View.VISIBLE);
                        }
                        if(mTipoCuenta.equals("Externo")){
                            mCardViewEmailNuevo.setEnabled(true);
                            mCardViewEmailNuevo.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }
}