package com.example.cenidet.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.adapters.SliderAdapter;
import com.example.cenidet.models.SliderItem;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.LikesProvider;
import com.example.cenidet.providers.NotificationProvider;
import com.example.cenidet.providers.PostProvider;
import com.example.cenidet.providers.TokenProvider;
import com.example.cenidet.providers.UsersProvider;
import com.example.cenidet.utils.RelativeTime;
import com.example.cenidet.utils.ViewedMessageHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();

    PostProvider mPostProvider;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    LikesProvider mLikesProvider;
    NotificationProvider mNotificationProvider;
    TokenProvider mTokenProvider;

    String mExtraPostId;

    TextView mtextViewTitle;
    TextView mtextViewDescripcion;
    TextView mtextViewNameCategory;
    TextView mTextViewRelativeTime;
    TextView mTextViewLikes;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    Toolbar mToolBar;

    String mIdUser = "";

    ListenerRegistration mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mSliderView = findViewById(R.id.imageSlider);
        mtextViewTitle = findViewById(R.id.textViewTitle);
        mtextViewDescripcion = findViewById(R.id.textViewDescripcion);
        mtextViewNameCategory = findViewById(R.id.textViewNameCategory);
        mTextViewRelativeTime = findViewById(R.id.textViewRelativeTime);
        mTextViewLikes = findViewById(R.id.textViewLikes);
        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);
        mToolBar = findViewById(R.id.toolBar);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPostProvider = new PostProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mLikesProvider = new LikesProvider();
        mNotificationProvider = new NotificationProvider();
        mTokenProvider = new TokenProvider();

        mExtraPostId = getIntent().getStringExtra("id");

        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, ChatActivity.class);
                intent.putExtra("idUser", mIdUser);
                String titulo = mtextViewTitle.getText().toString();
                intent.putExtra("variable_Titulo", titulo);
                startActivity(intent);

                //Agraga una variable al Bundle para el departamento
                String departamento = mtextViewNameCategory.getText().toString();
                intent.putExtra("departamento", departamento);
                //Toast.makeText(getApplicationContext(),departamento,Toast.LENGTH_LONG).show();
            }
        });

        getPost();
        getNumberLikes();
    }

    private void getNumberLikes() {
        mListener =  mLikesProvider.getLikesByPost(mExtraPostId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(queryDocumentSnapshots!=null){
                    int numberLikes = queryDocumentSnapshots.size();
                    if (numberLikes == 0){
                        mTextViewLikes.setText("Dale me gusta");
                    }else
                    if (numberLikes == 1){
                        mTextViewLikes.setText(numberLikes + " Me gusta");
                    }else{
                        mTextViewLikes.setText(numberLikes + " Me gustas");
                    }
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();

        ViewedMessageHelper.upDateOnline(true, PostDetailActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.upDateOnline(false, PostDetailActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener!=null){
            mListener.remove();
        }

    }

    private void instanceSlider(){
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.startAutoCycle();
    }

    private void getPost(){
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("image1")){
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image1);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("image1")){
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image2);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("title")){
                        String title = documentSnapshot.getString("title");
                        mtextViewTitle.setText(title.toUpperCase());
                    }
                    if(documentSnapshot.contains("description")){
                        String description = documentSnapshot.getString("description");
                        mtextViewDescripcion.setText(description);
                    }
                    if(documentSnapshot.contains("category")){
                        String category = documentSnapshot.getString("category");
                        mtextViewNameCategory.setText(category);

                    }
                    if(documentSnapshot.contains("idUser")){
                        mIdUser = documentSnapshot.getString("idUser");
                    }
                    if(documentSnapshot.contains("timestamp")){
                        long timestamp = documentSnapshot.getLong("timestamp");
                        String relativeTime = RelativeTime.getTimeAgo(timestamp, PostDetailActivity.this);
                        mTextViewRelativeTime.setText(relativeTime);
                    }

                    instanceSlider();
                }
            }
        });
    }

}