package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnSignInStatusListener;
import zw.co.appsareus.nannymeets.utils.FirebaseMethods;
import zw.co.appsareus.nannymeets.utils.Helper;

public abstract class BaseActivity extends ActivityManagePermission implements OnSignInStatusListener {

    protected static final String MAID = "Employee";
    protected static final String GARDENER = "Gardener";
    protected static final String EMPLOYER = "Employer";
    protected static final String EMPLOYEE = "Employee";
    protected static final String USER_TYPE = "user_type";

    protected Helper mHelper;
    protected FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseMethods = new FirebaseMethods(this);
        mHelper = new Helper(this);
        mHelper.setOnSignInStatusListener(this);
        mHelper.checkSignInStatus();
    }

    @Override
    public abstract void onSignedOut();

    @Override
    public abstract void onSignedIn();
}
