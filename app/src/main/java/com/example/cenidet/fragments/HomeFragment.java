package com.example.cenidet.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.activities.FormularioActivity;
import com.example.cenidet.activities.HomeActivity;
import com.example.cenidet.activities.MainActivity;
import com.example.cenidet.activities.PostActivity;
import com.example.cenidet.activities.ResetPasswordActivity;
import com.example.cenidet.adapters.PostsAdapter;
import com.example.cenidet.models.Post;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.PostProvider;
import com.example.cenidet.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements MaterialSearchBar.OnSearchActionListener{


    View mView;
    FloatingActionButton mFab;
    MaterialSearchBar mSearchBar;

    AuthProvider mAuthProvider;
    RecyclerView mRecycleView;
    PostProvider mPostProvider;
    PostsAdapter mPostAdapter;
    PostsAdapter mPostAdapterSearch;

    UsersProvider mUsersProvider;
    String mTipoCuenta;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab=mView.findViewById(R.id.fab);
        mRecycleView = mView.findViewById(R.id.recycleViewHome);
        mSearchBar = mView.findViewById(R.id.searchBar);mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab=mView.findViewById(R.id.fab);
        mRecycleView = mView.findViewById(R.id.recycleViewHome);
        mSearchBar = mView.findViewById(R.id.searchBar);

        setHasOptionsMenu(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);


        setHasOptionsMenu(true);
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mUsersProvider = new UsersProvider();



        mSearchBar.setOnSearchActionListener(this);

        //mSearchBar.inflateMenu(R.menu.main_menu);

        /*mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Se agrego el item para el acceso al formulario de estudiantes
                int id = item.getItemId();
                switch(id){
                    case R.id.itemFormulario:
                         goform();
                         break;
                    case R.id.itemLogout:
                        logout();
                        break;
                }
                /*
                if(item.getItemId() == R.id.itemLogout)
                {
                    logout();
                }

                return true;
            }
        });*/

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });

        return mView;
    }

    private void searchByTitle(String title){
        //Toast.makeText(getActivity(), "Titulo" + title + "Tipo de cuenta: " + mTipoCuenta, Toast.LENGTH_SHORT).show();
        Query query = null;
        if(mTipoCuenta.equals("ADMINISTRADOR")){
            query = mPostProvider.getPostByTitle(title);
        }else{
            query = mPostProvider.getPostByTitleTipocuenta(mTipoCuenta,title);
        }
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mPostAdapterSearch = new PostsAdapter(options, getContext());
        mPostAdapter.notifyDataSetChanged();
        mRecycleView.setAdapter(mPostAdapterSearch);
        mPostAdapterSearch.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostAdapter.stopListening();
        if(mPostAdapterSearch!=null){
            mPostAdapterSearch.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPostAdapter.getListener() !=null){
            mPostAdapter.getListener().remove();
        }
    }

    private void goToPost() {
        Intent intent= new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if(!enabled){
            //getAllPost();
            getUser();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString().toLowerCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {
    }

    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if(documentSnapshot.contains("tipocuenta")){
                        mTipoCuenta = documentSnapshot.getString("tipocuenta");
                        //Toast.makeText(getActivity(), "Usuario: " + mTipoCuenta, Toast.LENGTH_LONG).show();
                        if(mTipoCuenta.equals("ADMINISTRADOR")){
                            //Toast.makeText(getActivity(), "Tipo de cuenta: " + mTipoCuenta, Toast.LENGTH_SHORT).show();
                            mFab.show();
                            Query query = mPostProvider.getAll();

                            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                                    .setQuery(query, Post.class)
                                    .build();
                            mPostAdapter = new PostsAdapter(options, getContext());
                            mPostAdapter.notifyDataSetChanged();
                            mRecycleView.setAdapter(mPostAdapter);
                            mPostAdapter.startListening();
                        }else{
                            mFab.hide();
                            switch(mTipoCuenta){
                                case "Administrativo":
                                    mTipoCuenta = "isforAdministrative";
                                    break;
                                case "Docente":
                                    mTipoCuenta = "isforTeacher";
                                    break;
                                case "Estudiante":
                                    mTipoCuenta = "isforStudent";
                                    break;
                                case "Externo":
                                    mTipoCuenta = "isforExternal";
                                    break;
                            }

                            Query query = mPostProvider.getPostTipoCuenta(mTipoCuenta);

                            FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                                    .setQuery(query, Post.class)
                                    .build();
                            mPostAdapter = new PostsAdapter(options, getContext());
                            mPostAdapter.notifyDataSetChanged();
                            mRecycleView.setAdapter(mPostAdapter);
                            mPostAdapter.startListening();
                        }
                    }
                }
            }
        });
    }
}