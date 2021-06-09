package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.cenidet.R;
import com.example.cenidet.fragments.AboutFragment;
import com.example.cenidet.fragments.ChatFragment;
import com.example.cenidet.fragments.CodigoEticaFragment;
import com.example.cenidet.fragments.DirectorioFragment;
import com.example.cenidet.fragments.FiltersFragment;
import com.example.cenidet.fragments.HistoriaFragment;
import com.example.cenidet.fragments.HomeFragment;
import com.example.cenidet.fragments.MensajeDirectoraFragment;
import com.example.cenidet.fragments.MisionFragment;
import com.example.cenidet.fragments.OrganigramaFragment;
import com.example.cenidet.fragments.ProfileFragment;
import com.example.cenidet.fragments.ValoresFragment;
import com.example.cenidet.fragments.VisionFragment;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.TokenProvider;
import com.example.cenidet.providers.UsersProvider;
import com.example.cenidet.utils.ViewedMessageHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.security.KeyStore;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigation;

    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    //Elementos para el Navigation Drawer
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    //ActionBarDrawerToggle toggle;
    //Toolbar toolbar;

    String mTipoCuenta;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        openFragment(new HomeFragment());
        creatToken();

        //UI
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //toolbar = findViewById(R.id.barraherramienta);



        //toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.start, R.string.close);
        //mDrawerLayout.addDrawerListener(toggle);
        //toggle.syncState();

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        //Setup toolbar
        //setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        getUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.upDateOnline(true, HomeActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.upDateOnline(false, HomeActivity.this);
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


    private void creatToken(){
        mTokenProvider.create(mAuthProvider.getUid());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        selectItemNav(menuItem);
        return true;
    }

    private void selectItemNav(MenuItem menuItem) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                openFragment(new HomeFragment());
                //ft.replace(R.id.content, new HomeFragment()).commit();
                break;
            case R.id.nav_form:
                goform();
                break;
            case R.id.nav_convocatoria:
                Intent intent = new Intent(getApplicationContext(), ConvocatoriaActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_JCyTA:
                Intent intent2 = new Intent(getApplicationContext(), JCyTA_Activity.class);
                startActivity(intent2);
                break;
            case R.id.nav_Coloquio_Virtual:
                Intent intent3 = new Intent(getApplicationContext(), ColoquioVirtualActivity.class);
                startActivity(intent3);
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_about:
                openFragment(new AboutFragment());
                //ft.replace(R.id.content, new MisionFragment()).commit();
                break;
            case R.id.nav_cod_etica:
                openFragment(new CodigoEticaFragment());
                break;
            case R.id.nav_directorio:
                openFragment(new DirectorioFragment());
                break;
            case R.id.nav_msj_directora:
                openFragment(new MensajeDirectoraFragment());
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goform() {
        Intent intent = new Intent(getApplicationContext(), FormularioActivity.class);
        startActivity(intent);
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        //super.onOptionsItemSelected(item)
        return true;
    }*/

   private void getUser(){
       mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if (documentSnapshot.exists()){
                   if(documentSnapshot.contains("tipocuenta")){
                       mTipoCuenta = documentSnapshot.getString("tipocuenta");
                       Toast.makeText(getApplicationContext(), "Usuario: " + mTipoCuenta, Toast.LENGTH_LONG).show();
                       if(mTipoCuenta.equals("ADMINISTRADOR")||mTipoCuenta.equals("Administrativo")||mTipoCuenta.equals("Docente")||mTipoCuenta.equals("Estudiante")){
                           menu = navigationView.getMenu();
                           MenuItem ItemConvocatoria = menu.findItem(R.id.nav_convocatoria);
                           ItemConvocatoria.setVisible(false);
                       }
                   }
               }
           }
       });
   }
}