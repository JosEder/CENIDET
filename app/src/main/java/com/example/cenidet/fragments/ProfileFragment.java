package com.example.cenidet.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cenidet.R;
import com.example.cenidet.activities.EditProfileActivity;
import com.example.cenidet.adapters.MyPostsAdapter;
import com.example.cenidet.adapters.PostsAdapter;
import com.example.cenidet.models.Post;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.PostProvider;
import com.example.cenidet.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    View mView;
    LinearLayout mLinearLayoutEditProfile;
    LinearLayout mLineerLayoutTextPulicaciones;
    LinearLayout mLineerLayoutCantidadDePublicaciones;
    TextView mTextViewUsername;
    TextView mTextViewEmail;
    TextView mTextViewMatricula;
    TextView mTextViewPostNumber;
    TextView mTextViewPostExist;
    TextView mTextViewTipoCuenta;
    ImageView mImageViewCover;

    CircleImageView mCircleImageProfile;
    RecyclerView mRecycleView;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    MyPostsAdapter mAdapter;

    ListenerRegistration mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
       mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);
       mLineerLayoutTextPulicaciones = mView.findViewById(R.id.lineerLayoutTextPulicaciones);
       mLineerLayoutCantidadDePublicaciones = mView.findViewById(R.id.linearLayoutCantidadDePublicaciones);
       mTextViewEmail = mView.findViewById(R.id.textViewEmail);
       mTextViewUsername = mView.findViewById(R.id.textViewUserName);
       mTextViewMatricula = mView.findViewById(R.id.textViewMatricula);
       mTextViewPostNumber = mView.findViewById(R.id.textViewPostNumber);
       mTextViewPostExist = mView.findViewById(R.id.textViewPostExist);
       mTextViewTipoCuenta = mView.findViewById(R.id.textViewTipoCuenta);
       mCircleImageProfile = mView.findViewById(R.id.circleImageProfile);
       mImageViewCover = mView.findViewById(R.id.imageViewCover);
       mRecycleView = mView.findViewById(R.id.recycleViewMyPost);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);

       mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               goToEditProfile();
           }
       });
       mUsersProvider = new UsersProvider();
       mAuthProvider = new AuthProvider();
       mPostProvider = new PostProvider();


       getUser();
       getPosNumber();
       checkIfExistPost();
       tipoCuenta();

        return mView;
    }

    private void tipoCuenta() {
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    if (documentSnapshot.contains("tipocuenta")) {
                        String tipocuenta = documentSnapshot.getString("tipocuenta");
                        mTextViewTipoCuenta.setText(tipocuenta);

                        //Toast.makeText(getActivity(), "Usuario: " + mTipoCuenta, Toast.LENGTH_LONG).show();
                        if(tipocuenta.equals("ADMINISTRADOR")){
                            mLineerLayoutTextPulicaciones.setVisibility(View.VISIBLE);
                            mLineerLayoutCantidadDePublicaciones.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void checkIfExistPost() {
        mListener =  mPostProvider.getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(queryDocumentSnapshots!=null){
                    int numberPost = queryDocumentSnapshots.size();
                    if(numberPost>0){
                        mTextViewPostExist.setText("Publicaciones");
                        mTextViewPostExist.setTextColor(Color.RED);
                    }else{
                        mTextViewPostExist.setText("No hay publicaciones");
                        mTextViewPostExist.setTextColor(Color.GRAY);
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUserandTimestamp(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mAdapter = new MyPostsAdapter(options, getContext());
        mRecycleView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mListener!=null){
            mListener.remove();
        }
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPosNumber(){
        mPostProvider.getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if(documentSnapshot.contains("matricula")){
                        String matricula = documentSnapshot.getString("matricula");
                        mTextViewMatricula.setText(matricula);
                    }
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(imageProfile!=null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(getContext()).load(imageProfile).into(mCircleImageProfile);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");
                        if(imageCover!=null){
                            if(!imageCover.isEmpty()){
                                Picasso.with(getContext()).load(imageCover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }
}