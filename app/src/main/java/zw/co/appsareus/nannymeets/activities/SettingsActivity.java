package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.fragments.FilterFragment;
import zw.co.appsareus.nannymeets.fragments.ProfileFragment;

public class SettingsActivity extends BaseActivity {


    private ProfileFragment pf;
    private FragmentManager fm;
    private FilterFragment ff;
    private String settingsScreen = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (getIntent().getExtras() != null){
            settingsScreen = getIntent().getExtras().getString("setting_screen");
        }
        switch (settingsScreen){
            case "filter" :
                ff = FilterFragment.newInstance();
                fragmentTransaction.replace(R.id.content, ff);
                break;
            case "profile" :
                pf = ProfileFragment.newInstance();
                fragmentTransaction.replace(R.id.content, pf);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSignedOut() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void onSignedIn() {
        //do nothing
    }
}
