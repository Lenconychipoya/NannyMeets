package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnAddPreferredEmployee;
import zw.co.appsareus.nannymeets.models.Employee;

public class ViewProfileActivity extends BaseActivity implements OnAddPreferredEmployee {

    private static final String TAG = "ViewProfileActivity";

    private TextView tvExperience,tvQualifications,tvQassport,tvDrivers_licence,tvReligion,tvChurch,tvSalary,tvType_of_employment;
    private TextView tvName,tvSurname,tvUsername, tvAge,tvGender,tvEmail,tvPhone,tvAddress,tvNatID;
    private TextView tvAddedEmployee;
    private Button btnAddEmployee;
    private FrameLayout frameAddPrefferedEmployee;
    private Toolbar mToolbar;
    private Bundle extras;
    private Employee mEmployee;
    private Boolean preview = true;
    private LinearLayout llLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getIntent().getExtras() != null){
            extras = getIntent().getExtras();
            preview = extras.getBoolean("preview",true);
        }

        mEmployee = (Employee) extras.getSerializable(EMPLOYEE);


        setupViews();
        //mark profile as viewed
        mFirebaseMethods.markViewedProfile(mEmployee.getId());


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

    private void setupViews(){
        llLoader = findViewById(R.id.ll_progress);
        llLoader.setVisibility(View.GONE);

        //Firebase Listeners
        mFirebaseMethods.setOnAddPreferredEmployee(this);

        getSupportActionBar().setTitle(mEmployee.getName()+" "+ mEmployee.getSurname());
        //mToolbar.setTitle();
        tvType_of_employment = findViewById(R.id.tv_employment_type);
        tvAge = findViewById(R.id.tv_age);
        tvQualifications = findViewById(R.id.tv_qualifications);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);
        tvNatID = findViewById(R.id.tv_nat_id);
        tvQassport = findViewById(R.id.tv_passport);
        tvDrivers_licence = findViewById(R.id.tv_drivers_licence);
        tvReligion = findViewById(R.id.tv_religion);
        tvChurch = findViewById(R.id.tv_church);
        tvSalary = findViewById(R.id.tv_salary);
        tvExperience = findViewById(R.id.tv_experience);
        tvGender = findViewById(R.id.tv_gender);

        btnAddEmployee = findViewById(R.id.btn_add_employee);
        tvAddedEmployee = findViewById(R.id.tv_added_employee);
        frameAddPrefferedEmployee = findViewById(R.id.frame_preferred_add_button);

        tvType_of_employment.setText(mEmployee.getOccupation());
        tvAge.setText(mEmployee.getDob());
        tvQualifications.setText(mEmployee.getQualifications());
        tvEmail.setText(mEmployee.getEmail());
        tvPhone.setText(mEmployee.getPhone());
        tvAddress.setText(mEmployee.getAddress());
        tvNatID.setText(mEmployee.getNatID());
        tvQassport.setText(mEmployee.getPassport());
        tvDrivers_licence.setText(mEmployee.getDriversLicence());
        tvReligion.setText(mEmployee.getReligion());
        tvChurch.setText(mEmployee.getChurch());
        tvSalary.setText(""+ mEmployee.getExpectedSalary());
        tvExperience.setText(mEmployee.getExperience());
        tvGender.setText(mEmployee.getGender());

        if (preview){
            //in normal view
            tvEmail.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);

            //Check if employee has already been added
            tvAddedEmployee.setVisibility(View.GONE);
            btnAddEmployee.setVisibility(View.VISIBLE);
            btnAddEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Add Employee to preference List
                    llLoader.setVisibility(View.VISIBLE);
                    //Add to Preferred List and Mark as Shortlisting
                    mFirebaseMethods.addEmployeeToPreferredList(mEmployee.getId());
                    //Hide button and show label
                    btnAddEmployee.setVisibility(View.GONE);
                    tvAddedEmployee.setVisibility(View.VISIBLE);
                }
            });
            frameAddPrefferedEmployee.setVisibility(View.VISIBLE);
        }else{
            //When person has added the employee to preferred employees

            tvEmail.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);

            tvEmail.setText(mEmployee.getEmail());
            tvPhone.setText(mEmployee.getPhone());
            tvAddress.setText(mEmployee.getAddress());
            frameAddPrefferedEmployee.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSuccessfulAddEmployee() {
        llLoader.setVisibility(View.GONE);
        Log.i(TAG, "onSuccessfulAddEmployee: Employee Added Successfully");
        Toast.makeText(this, "Employee Added to profile", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedAddEmployee(String error) {
        llLoader.setVisibility(View.GONE);
        Log.e(TAG, "onFailedAddEmployee: " +error);
        Toast.makeText(this, "Failed to added Employee. " + error, Toast.LENGTH_SHORT).show();
    }
}
