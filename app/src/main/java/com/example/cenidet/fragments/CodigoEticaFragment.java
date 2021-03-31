package com.example.cenidet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.cenidet.R;
import com.example.cenidet.providers.PDFStreamProvider;
import com.github.barteksc.pdfviewer.PDFView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodigoEticaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodigoEticaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View mView;
    PDFView pdfView;
    ProgressBar progressBar;

    public CodigoEticaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodigoEticaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CodigoEticaFragment newInstance(String param1, String param2) {
        CodigoEticaFragment fragment = new CodigoEticaFragment();
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

        mView =  inflater.inflate(R.layout.fragment_codigo_etica, container, false);
        pdfView = mView.findViewById(R.id.pdf_viewer);
        progressBar = mView.findViewById(R.id.progressBar);

        String urlPdf = "https://cenidet.tecnm.mx/docs/Codigos-de-Etica-TecNM.pdf";
        new PDFStreamProvider(pdfView, progressBar).execute(urlPdf);

        return mView;
    }
}