package com.example.cenidet.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cenidet.R;
import com.example.cenidet.activities.FormularioActivity;
import com.example.cenidet.activities.MainActivity;
import com.example.cenidet.activities.PostActivity;
import com.example.cenidet.adapters.PostsAdapter;
import com.example.cenidet.models.Post;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab=mView.findViewById(R.id.fab);
        mRecycleView = mView.findViewById(R.id.recycleViewHome);
        mSearchBar = mView.findViewById(R.id.searchBar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);


        setHasOptionsMenu(true);
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.itemFormulario:
                         goform();
                         break;
                    case R.id.itemLogout:
                        logout();
                        break;
                }
                /*if(item.getItemId() == R.id.itemFormulario)
                {
                    form();
                }
                else(item.getItemId() == R.id.itemLogout)
                {
                    logout();
                }*/
                return true;
            }
        });


        //Esta condicion determina de manera fija si es el ID del administrador para realizar publicaciones.
        if(mAuthProvider.getUid().equals("tmtpCi4o3IeB0TsiCirkPpnMYJ52")){
            mFab.setVisibility(View.VISIBLE);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToPost();
                }
            });
        }else{

        }


        return mView;
    }

    private void searchByTitle(String title){
        Query query = mPostProvider.getPostByTitle(title);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mPostAdapterSearch = new PostsAdapter(options, getContext());
        mPostAdapter.notifyDataSetChanged();
        mRecycleView.setAdapter(mPostAdapterSearch);
        mPostAdapterSearch.startListening();
    }
    private void getAllPost(){
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        mPostAdapter = new PostsAdapter(options, getContext());
        mPostAdapter.notifyDataSetChanged();
        mRecycleView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
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

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goform() {
        Intent intent = new Intent(getContext(), FormularioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if(!enabled){
            getAllPost();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString().toLowerCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}