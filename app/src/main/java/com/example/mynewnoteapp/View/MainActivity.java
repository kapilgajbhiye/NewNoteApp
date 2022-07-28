package com.example.mynewnoteapp.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.mynewnoteapp.Adapter.NoteListAdapter;
import com.example.mynewnoteapp.Model.Note;
import com.example.mynewnoteapp.R;
import com.example.mynewnoteapp.ViewModel.SharedViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private NavigationView nave;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SharedViewModel sharedViewModel;
    ArrayList<Note> noteArrayList;
    NoteListAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new NoteListAdapter(this,noteArrayList);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nave = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

//        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
//        sharedViewModel.set_goToLoginPageStatus(true);
//        observeAppNavigation();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
        nave.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.reminder:
                        temp = new ReminderFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.archive:
                        Toast.makeText(MainActivity.this, "archive ", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.trash:
                        Toast.makeText(MainActivity.this, "trash ", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, temp).commit();
                return true;
            }
        });
    }

    private void observeAppNavigation(){

        sharedViewModel.goToLoginPageStatus.observe(MainActivity.this, status -> {
            if(status == true){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                toolbar.setVisibility(View.GONE);

            }
        });

        sharedViewModel.goToRegistrationPageStatus.observe(MainActivity.this, status -> {
            if(status == true){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RegistrationFragment()).commit();
                toolbar.setVisibility(View.GONE);

            }
        });

        sharedViewModel.goToHomePageStatus.observe(MainActivity.this, status -> {
            if(status == true){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
                toolbar.setVisibility(View.VISIBLE);

            }
        });
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}