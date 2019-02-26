package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.fragments.EmployeeHomeFragment;
import zw.co.appsareus.nannymeets.fragments.EmployerHomeFragment;
import zw.co.appsareus.nannymeets.fragments.LanguagesDialogFragment;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.utils.Helper;

public class ContentActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "ContentActivity";
    private String accountType;
    private Helper mHelper;

    private EmployeeHomeFragment employeeHomeFragment;
    private EmployerHomeFragment employerHomeFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        accountType = getIntent().getStringExtra(USER_TYPE);

        mHelper = new Helper(this);
        mHelper.setAccountType(accountType);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ImageView ivProfilePic = navigationView.getHeaderView(0).findViewById(R.id.iv_profile_pic);
        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        ImageView ivProfile = navigationView.getHeaderView(0).findViewById(R.id.iv_profile_pic);

        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (accountType.equals(EMPLOYEE)) {
            toolbar.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            Employee m = mHelper.getEmployeeProfile();
            Glide.with(this).load(m.getProfilePic() != null ? m.getProfilePic().getPath() : "").into(ivProfilePic);
            tvName.setText(m.getUsername());
            tvEmail.setText(m.getEmail());
            Glide.with(this).load(m.getProfilePic() != null ? m.getProfilePic().getPath() : "").into(ivProfile);

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_content_drawer_employee);

            employeeHomeFragment = EmployeeHomeFragment.newInstance();
            fragmentTransaction.replace(R.id.my_content, employeeHomeFragment);
            employeeHomeFragment.setOnOpenProfileListener(new EmployeeHomeFragment.OnOpenProfileListener() {
                @Override
                public void onOpenProfile() {
                    startActivity(new Intent(ContentActivity.this, SettingsActivity.class));

                }
            });

        } else  {
            accountType=EMPLOYER;
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);

            Employer e = mHelper.getEmployerProfile();
            Glide.with(this).load(e.getProfilePic() != null ? e.getProfilePic().getPath() : "").into(ivProfilePic);
            tvName.setText(e.getUsername());
            tvEmail.setText(e.getEmail());
            Glide.with(this).load(e.getProfilePic() != null ? e.getProfilePic().getPath() : "").into(ivProfile);

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_content_drawer_employer);

            employerHomeFragment = EmployerHomeFragment.newInstance();
            fragmentTransaction.replace(R.id.my_content, employerHomeFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onSignedOut() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onSignedIn() {
        //do nothing
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Bundle extras = new Bundle();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            //Open the filter screen
            extras.putString("setting_screen", "filter");
            Intent intent = new Intent(ContentActivity.this, SettingsActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_preferred_employees) {
            // Handle the camera action
            startActivity(new Intent(ContentActivity.this, PreferredEmployeesActivity.class));
        } else if (id == R.id.nav_log_out) {
            mHelper.logout();
            startActivity(new Intent(ContentActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(ContentActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please check out this Nanny Meets app that can help you link up with maids from anywhere. " + Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName()));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share App..."));
        } else if (id == R.id.nav_send) {
            Helper.openSupportMail(this);
        } else if (id == R.id.nav_language) {
            //new PurchaseDialogFragment().show(getSupportFragmentManager(),TAG);
            new LanguagesDialogFragment().show(getSupportFragmentManager(), TAG);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
