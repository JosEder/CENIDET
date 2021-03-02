package com.example.cenidet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.cenidet.R;
import com.example.cenidet.activities.FiltersActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View mView;
    CardView mCardViewDireccion;
    Button mButtonBuscar;
    String mCategoria;

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_filters, container, false);
        mButtonBuscar = mView.findViewById(R.id.btnBuscar);


        String[] data = {"Dep. de Ingenieria Electronica", "Dep. de Ingenieria Mecanica", "Dep. de Ciencias Computacionales", "Dep. de Dess. Academico e Idiomas",
                "Dep. de Org. y Seguimiento de Estudios", "Oficina de Centro de Com. y Telec.", "Centro de Informacion", "Dep. de Plan., Prog. y Presupestacion.",
                "Dep. de Gest. Tecn. y Vinculacion", "Dep. de Comunicacion y Eventos", "Dep. de Servicios Escolares", "Dep. de Recursos Materiales y Servicios",
                "Dep. de Recursos Humanos", "Dep. de Recursos Financieros"};

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        final Spinner spinner =  mView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               mCategoria = spinner.getSelectedItem().toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        mButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilterActivity(mCategoria);
            }
        });

        return mView;
    }

    private void goToFilterActivity(String category){
        Intent intent = new Intent(getContext(), FiltersActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);

    }
}