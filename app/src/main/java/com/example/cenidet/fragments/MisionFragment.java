    package com.example.cenidet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.cenidet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisionFragment extends Fragment implements View.OnTouchListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /*TextView textView;
    View mView;

    final static float move = 200;
    float ratio = 1.0f;
    int baseDist;
    float baseRatio;*/


    public MisionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisionFragment newInstance(String param1, String param2) {
        MisionFragment fragment = new MisionFragment();
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
        return inflater.inflate(R.layout.fragment_mision, container, false);
        /*
        mView =  inflater.inflate(R.layout.fragment_mision, container, false);
        textView = mView.findViewById(R.id.text_view);
        textView.setTextSize(ratio+15);
        return mView;
        */
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /*public boolean onTouchEvent (MotionEvent event) {
        if(event.getPointerCount() == 2){
            int action = event.getAction();
            int mainaction = action&MotionEvent.ACTION_MASK;
            if(mainaction == MotionEvent.ACTION_POINTER_DOWN){
                baseDist = getDistance(event);
                baseRatio = ratio;
            }else {
                float scale  = (getDistance(event)-baseDist)/move;
                float factor = (float) Math.pow(2,scale);
                ratio = Math.min(1024.0f,Math.max(0.1f,baseRatio*factor));
                textView.setTextSize(ratio+15);
            }
        }
        return true;
    }*/

    private int getDistance(MotionEvent event){
        int dx = (int) (event.getX(0)-event.getX(1));
        int dy = (int) (event.getY(0)-event.getY(1));
        return (int) (Math.sqrt(dx*dx+dy*dy));
    }
}