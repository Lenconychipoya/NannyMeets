package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnLoginListener;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.models.Employee;

public class LoginActivity extends BaseActivity implements OnLoginListener {


//    private Button btnEmployee, btnEmployer;
    private Button btnLogin;
//    private EditText etUsername, etPassword;
//    private TextInputLayout tilUsername, tilPassword;
//    private TextView tvSignUp;
//    private String mUserType;
//    private LinearLayout llLogin,llLogin2;
    private LinearLayout llLoader;

    //private EditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSignedOut() {
        setContentView(R.layout.activity_login_new);
        setup();
    }

    @Override
    public void onSignedIn() {
        Bundle extras = new Bundle();
        extras.putString("user_type", mHelper.getAccountType());
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void setup(){

        //btnEmployee = findViewById(R.id.btn_employee);
        //btnEmployer = findViewById(R.id.btn_employer);
        btnLogin = findViewById(R.id.btn_login);
//        tvSignUp = findViewById(R.id.tvSignUp);
//        llLogin2 = findViewById(R.id.ll_login2);
//        llLogin = findViewById(R.id.ll_login);
//        etPassword = findViewById(R.id.et_password);
//        etUsername = findViewById(R.id.et_username);
//        tilUsername = findViewById(R.id.til_username);
//        tilPassword = findViewById(R.id.til_password);


        llLoader = findViewById(R.id.ll_progress);
        llLoader.setVisibility(View.GONE);

        mFirebaseMethods.setOnLoginListener(this);

//        btnEmployee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mUserType = EMPLOYEE;
//                llLogin.setVisibility(View.GONE);
//                llLogin2.setVisibility(View.VISIBLE);
//            }
//        });
//
//        btnEmployer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mUserType = EMPLOYER;
//                llLogin.setVisibility(View.GONE);
//                llLogin2.setVisibility(View.VISIBLE);
//            }
//        });
//
//        tvSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//                finish();
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get account type from online and pass that into done after logging in

//                tilUsername.setErrorEnabled(false);
//                tilPassword.setErrorEnabled(false);
//                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
//                    tilUsername.setError("Please enter a username");
//                }
//                if (etPassword.getText().toString().equals("")){
//                    tilPassword.setError("Please enter a password");
//                }
//
//                llLoader.setVisibility(View.VISIBLE);
//                mFirebaseMethods.login(
//                        etUsername.getText().toString(),
//                        etPassword.getText().toString(),
//                        mUserType
//                        );
//                done(mUserType);

                startActivity(new Intent(LoginActivity.this,PhoneLoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (llLogin2.getVisibility() == View.VISIBLE){
//            llLogin2.setVisibility(View.GONE);
//            llLogin.setVisibility(View.VISIBLE);
//        }else{
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    private void done(String user_type){
        Toast.makeText(this, "Login successful.",Toast.LENGTH_SHORT).show();
        mHelper.setSignedInStatus(true);
        Bundle extras = new Bundle();
        extras.putString(USER_TYPE,user_type);
        Intent intent = new Intent(this,ContentActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void displayError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    /**
     * Begin of Implemented Methods
     * */


    @Override
    public void onSuccessfulLoginEmployee(Employee employee) {
        llLoader.setVisibility(View.GONE);
        mHelper.setEmployeeProfile(employee);
        //Open the Employee Home Screen
        done(EMPLOYEE);
    }

    @Override
    public void onSuccessfulLoginEmployer(Employer employer) {
        llLoader.setVisibility(View.GONE);
        mHelper.setEmployerProfile(employer);
        //Open the Employee Home Screen
        done(EMPLOYER);
    }

    @Override
    public void onFailuredLogin(String error) {
        llLoader.setVisibility(View.GONE);
        //Display error message
        displayError(error);
    }


    /**
     * End of Implemented Methods
     * */
}
