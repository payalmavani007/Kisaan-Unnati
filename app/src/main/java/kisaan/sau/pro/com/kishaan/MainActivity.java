package kisaan.sau.pro.com.kishaan;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_profile);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("status", MODE_PRIVATE);
        android.support.v4.app.Fragment fragment = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.navigation_profile:
                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 1) {
                    fragment = new AdminProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, new AdminProfileFragment()).commit();
                }
                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 2) {
                    fragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment()).commit();
                }
                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 3) {
                    fragment = new TalukaProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, new TalukaProfileFragment()).commit();
                }
                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 4) {
                    fragment = new TalukaProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_container, new VillageProfileFragment()).commit();
                }
                if (Integer.parseInt(preferences.getString("rolemanagement_id", "")) == 5) {
                    fragment = new EmployeeProfileFragment(Integer.parseInt(preferences.getString("id", "")));
                    fragmentTransaction.replace(R.id.fragment_container, new EmployeeProfileFragment(Integer.parseInt(preferences.getString("id", "")))).commit();
                }
//                fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
                break;
            case R.id.navigation_form:
                fragment = new FormFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                break;


            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();

                break;

            case R.id.navigation_aboutus:
                fragment = new AboutusFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();

                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
