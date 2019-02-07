package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnRegisterListener;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.ProfilePicture;
import zw.co.appsareus.nannymeets.utils.DataEncryption;

public class RegisterActivity extends BaseActivity implements OnRegisterListener {

    private static final String TAG = "RegisterActivity";

    private Spinner accType;
    private TextInputLayout llName,llSurname,llUsername,llEmail,llPhone,llPassword1,llPassword2;
    private TextInputEditText etName, etSurname, etUsername, etEmail, etPhone, etPassword1, etPassword2;
    private Button btnRegister;
    private Employer mEmployer;
    private Employee mEmployee;
    private String accountType;
    private ArrayAdapter<String> adapter;
    private String[] accs = {EMPLOYER,EMPLOYEE};
    private LinearLayout llLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setup();
    }

    @Override
    public void onSignedOut() {
        //do nothing
    }

    @Override
    public void onSignedIn() {
        //do nothing
    }

    private void setup(){
        mFirebaseMethods.setOnRegisterListener(this);

        accType = findViewById(R.id.spinner_acc_type);
        btnRegister = findViewById(R.id.btn_register);
        llName = findViewById(R.id.ll_name);
        llSurname = findViewById(R.id.ll_surname);
        llUsername = findViewById(R.id.ll_username);
        llEmail = findViewById(R.id.ll_email);
        llPhone = findViewById(R.id.ll_phone_number);
        llPassword1 = findViewById(R.id.ll_password1);
        llPassword2 = findViewById(R.id.ll_password2);

        etName = findViewById(R.id.et_name);
        etSurname = findViewById(R.id.et_surname);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword1 = findViewById(R.id.et_password1);
        etPassword2 = findViewById(R.id.et_password2);

        llLoader = findViewById(R.id.ll_progress);
        llLoader.setVisibility(View.GONE);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,accs);
        accType.setAdapter(adapter);
        accType.setSelection(0);
        accType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accountType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        llPassword2.setErrorEnabled(false);
        llPassword1.setErrorEnabled(false);
        llUsername.setErrorEnabled(false);
        llSurname.setErrorEnabled(false);
        llName.setErrorEnabled(false);
        llEmail.setErrorEnabled(false);
        llPhone.setErrorEnabled(false);

        if (etName.getText().toString().equals("")){
            llName.setError("Please enter a valid Name");
            return;
        }
        if (etSurname.getText().toString().equals("")){
            llSurname.setError("Please Enter a valid Surname");
            return;
        }
        if (etUsername.getText().toString().equals("")){
            llUsername.setError("Please enter a valid Username");
            return;
        }
        if (etEmail.getText().toString().equals("")){
            llEmail.setError("Please enter a valid Email");
            return;
        }
        if (etPhone.getText().toString().equals("")){
            llPhone.setError("Please enter a valid phone number");
            return;
        }
        if (!etPassword1.getText().toString().equals(etPassword2.getText().toString())){
            llPassword1.setError("Passwords do not match");
            llPassword2.setError("Passwords do not match");
            etPassword1.setText("");
            etPassword2.setText("");
            return;
        }

        if (accType.getSelectedItem().toString().equals(EMPLOYER)){
            accountType = EMPLOYER;
            mEmployer = new Employer(
                    etName.getText().toString(),
                    etSurname.getText().toString(),
                    "",
                    "",
                    "",
                    etEmail.getText().toString(),
                    etPhone.getText().toString(),
                    "",
                    "",
                    DataEncryption.hashPassword(etPassword1.getText().toString()),
                    accountType,
                    etUsername.getText().toString(),
                    "",
                    "",
                    2
            );
            mEmployer.setProfilePic(new ProfilePicture(
                    "1",
                    "default",
                    "",
                    ""
            ));
            llLoader.setVisibility(View.VISIBLE);
            mFirebaseMethods.registerUser(mEmployer,etEmail.getText().toString(),etPassword1.getText().toString());
        }else{
            accountType = EMPLOYEE;
            mEmployee = new Employee(
                    etName.getText().toString(),
                    etSurname.getText().toString(),
                    "",
                    "",
                    "",
                    etEmail.getText().toString(),
                    etPhone.getText().toString(),
                    "",
                    "",
                    DataEncryption.hashPassword(etPassword1.getText().toString()),
                    accountType,
                    etUsername.getText().toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0f,
                    "",
                    "Available",
                    "",
                    "",
                    ""
            );
            mEmployee.setProfilePic(new ProfilePicture(
                    "1",
                    "default",
                    "",
                    ""
            ));
            llLoader.setVisibility(View.VISIBLE);
            mFirebaseMethods.registerUser(mEmployee,etEmail.getText().toString(),etPassword1.getText().toString());
        }


    }
    /**
     * Begin of Implemented Methods
     * */

    @Override
    public void onUserRegistered() {
        llLoader.setVisibility(View.GONE);
        mHelper.setAccountType(accountType);
        Toast.makeText(this,"Registration Successful.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this,ContentActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserRegistrationFailed(String error) {
        llLoader.setVisibility(View.GONE);
        Toast.makeText(this, "Failed to register, please try again later. " + error, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onUserRegistrationFailed: "+error);
    }

    /**
     * End of Implemented Methods
     * */
}
