package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cenidet.R;
import com.example.cenidet.fragments.ChatFragment;
import com.example.cenidet.fragments.FiltersFragment;
import com.example.cenidet.fragments.HomeFragment;
import com.example.cenidet.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsuariosActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId()==R.id.itemHome){
                        //FRAGTMENT DE LOS CHATS
                        openFragment(new HomeFragment());
                    }
                    else if(item.getItemId()==R.id.itemChats){
                        //FRAGMENTS DE LOS CHATS
                        openFragment(new ChatFragment());
                    }
                    else if(item.getItemId()==R.id.itemFilters){
                        //FRAGMENTS DE LOS FILTERS
                        openFragment(new FiltersFragment());
                    }
                    else if(item.getItemId()==R.id.itemProfile){
                        //FRAGMENTS DE LOS PROFILE
                        openFragment(new ProfileFragment());
                    }
                    return true;
                }
            };
}