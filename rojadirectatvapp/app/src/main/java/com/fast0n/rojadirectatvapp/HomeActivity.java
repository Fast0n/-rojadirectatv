package com.fast0n.rojadirectatvapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.fast0n.rojadirectatvapp.bottomsheetdialog.BottomSheetDialogSettings;
import com.fast0n.rojadirectatvapp.fragment.HomeFragment.HomeFragment;
import com.fast0n.rojadirectatvapp.fragment.LegalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ImageButton imageButton;
    String theme;
    SharedPreferences settings;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = getSharedPreferences("sharedPreferences", 0);
        editor = settings.edit();
        theme = settings.getString("toggleTheme", null);
        try {
            if (theme.equals("0"))
                setTheme(R.style.AppTheme);
            else
                setTheme(R.style.DarkTheme);
        } catch (Exception ignored) {
            editor.putString("toggleTheme", "0");
            editor.apply();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(view -> {
            BottomSheetDialogSettings bottomSheetDialog = new BottomSheetDialogSettings();
            bottomSheetDialog.show(getSupportFragmentManager(), "show");

            navView.getMenu().findItem(R.id.uncheckedItem).setChecked(true);
            navView.findViewById(R.id.uncheckedItem).setVisibility(View.GONE);
        });

        loadFragment(new HomeFragment());


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_legal:
                fragment = new LegalFragment();
                break;
        }


        return loadFragment(fragment);
    }


}
