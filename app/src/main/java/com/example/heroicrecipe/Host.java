package com.example.heroicrecipe;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.heroicrecipe.Fragments.All_foods;
import com.example.heroicrecipe.Fragments.Bookmarks;
import com.example.heroicrecipe.Fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Host extends AppCompatActivity {

//    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foods);
        bottomNavigationView=findViewById(R.id.navigationic);

        getSupportFragmentManager().beginTransaction().replace(R.id.ffframelayout1, new All_foods()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.orders:
                        replacefrag(new All_foods());
                        break;
                    case R.id.income:
                        replacefrag(new Bookmarks());
                        break;
                    case R.id.profile:
//                        Toast.makeText(Foods.this, "my profile", Toast.LENGTH_SHORT).show();
                        replacefrag(new Profile());
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemidd = item.getItemId();
        if (itemidd == R.id.Talk) {
            Toast.makeText(this, "talk to us", Toast.LENGTH_LONG).show();
            return true;
        } else if (itemidd == R.id.profile) {
            replacefrag(new Profile());

        } else if (itemidd == R.id.signout) {
            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.remove("user_id");
            editor.clear();
            editor.commit();
            Toast.makeText(this, "successfully logged out", Toast.LENGTH_LONG).show();

        }
        return true;
    }

    private void replacefrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }




}

