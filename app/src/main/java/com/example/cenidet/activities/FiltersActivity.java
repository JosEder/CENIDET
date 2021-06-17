package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.adapters.PostsAdapter;
import com.example.cenidet.models.Post;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.PostProvider;
import com.example.cenidet.utils.ViewedMessageHelper;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FiltersActivity extends AppCompatActivity {

    String mExtraCategory;
    String mExtraTipocuenta;

    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;

    TextView mTextViewNumberFilter;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mRecyclerView = findViewById(R.id.recycleViewFilter);
        mToolbar = findViewById(R.id.toolbar);
        mTextViewNumberFilter = findViewById(R.id.textViewNumberFilter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(FiltersActivity.this,2));

        mExtraCategory = getIntent().getStringExtra("category");
        mExtraTipocuenta = getIntent().getStringExtra("tipocuenta");

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

    }


    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Departamento: " + mExtraCategory + " Usuario: " + mExtraTipocuenta, Toast.LENGTH_LONG).show();
        if(mExtraTipocuenta.equals("ADMINISTRADOR")){
            Toast.makeText(getApplicationContext(), "Usuario: " + mExtraTipocuenta, Toast.LENGTH_LONG).show();
            Query query = mPostProvider.getPostByCategoryAndTimeStamp(mExtraCategory);
            FirestoreRecyclerOptions<Post> options =
                    new FirestoreRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post.class)
                            .build();
            mPostsAdapter = new PostsAdapter(options, FiltersActivity.this, mTextViewNumberFilter);
            mRecyclerView.setAdapter(mPostsAdapter);
            mPostsAdapter.startListening();

            ViewedMessageHelper.upDateOnline(true, FiltersActivity.this);
        }else{
            switch(mExtraTipocuenta){
                case "Administrativo":
                    mExtraTipocuenta = "isforAdministrative";
                    break;
                case "Docente":
                    mExtraTipocuenta = "isforTeacher";
                    break;
                case "Estudiante":
                    mExtraTipocuenta = "isforStudent";
                    break;
                case "Externo":
                    mExtraTipocuenta = "isforExternal";
                    break;
            }
            Query query = mPostProvider.getPostByCategoryAndTipocuenta(mExtraCategory, mExtraTipocuenta);

            //Query query = mPostProvider.getPostByCategoryAndTimeStamp(mExtraCategory);
            FirestoreRecyclerOptions<Post> options =
                    new FirestoreRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post.class)
                            .build();
            mPostsAdapter = new PostsAdapter(options, FiltersActivity.this, mTextViewNumberFilter);
            mRecyclerView.setAdapter(mPostsAdapter);
            mPostsAdapter.startListening();

            ViewedMessageHelper.upDateOnline(true, FiltersActivity.this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.upDateOnline(true, FiltersActivity.this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}