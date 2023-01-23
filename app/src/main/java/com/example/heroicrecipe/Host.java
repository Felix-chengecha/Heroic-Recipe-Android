package com.example.heroicrecipe;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.heroicrecipe.Fragments.All_foods;
import com.example.heroicrecipe.Fragments.Bookmarks;
import com.example.heroicrecipe.Fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Host extends AppCompatActivity {

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

    private void replacefrag(@NonNull Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ffframelayout1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }




}

