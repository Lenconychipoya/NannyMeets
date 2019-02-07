package zw.co.appsareus.nannymeets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.models.Country;
import zw.co.appsareus.nannymeets.models.User;
import zw.co.appsareus.nannymeets.utils.Helper;
import zw.co.appsareus.nannymeets.utils.KeyboardUtil;

public class PhoneLoginActivity extends BaseActivity {

    private AppCompatSpinner spinnerCountryCodes;
    private EditText etPhone;
    private static final int REQUEST_CODE_SMS = 123;
//    private DatabaseReference userRef;
    private EditText otpCode;
    private KeyboardUtil keyboardUtil;
    private String phoneNumberInPrefs = null, verificationCode = null;
    private ProgressDialog progressDialog;
    private TextView verificationMessage, retryTimer;
    private CountDownTimer countDownTimer;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private boolean authInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone_login);

//init vars
        phoneNumberInPrefs = mHelper.getPhoneNumberForVerification();
        keyboardUtil = KeyboardUtil.getInstance(this);
        progressDialog = new ProgressDialog(this);

        //if there is number to authenticate in preferences then initiate
        setContentView(TextUtils.isEmpty(phoneNumberInPrefs) ? R.layout.activity_sign_in_1 : R.layout.activity_sign_in_2);
        if (TextUtils.isEmpty(phoneNumberInPrefs)) {
            //setup number selection
            spinnerCountryCodes = findViewById(R.id.countryCode);
            etPhone = findViewById(R.id.phoneNumber);
            setupCountryCodes();
        } else {
            //initiate authentication
            mAuth = FirebaseAuth.getInstance();
            retryTimer = findViewById(R.id.resend);
            verificationMessage = findViewById(R.id.verificationMessage);
            otpCode = findViewById(R.id.otp);
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    back();
                }
            });
            findViewById(R.id.changeNumber).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHelper.clearPhoneNumberForVerification();
                    recreate();
                }
            });
            initiateAuth(phoneNumberInPrefs);
        }
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phoneNumberInPrefs)) {
                    submit();
                } else {
                    //force authenticate
                    String otp = otpCode.getText().toString();
                    if (!TextUtils.isEmpty(otp) && !TextUtils.isEmpty(mVerificationId))
                        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, otp));
                    //verifyOtp(otpCode[0].getText().toString() + otpCode[1].getText().toString() + otpCode[2].getText().toString() + otpCode[3].getText().toString());
                }
            }
        });
    }

    @Override
    public void onSignedOut() {

    }

    @Override
    public void onSignedIn() {

    }

    private void showProgress(int i) {
        String title = (i == 1) ? "Sending otp" : "Verifying otp";
        String message = (i == 1) ? ("One time password is being send to:\n" + phoneNumberInPrefs) : "Verifying otp...";
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void initiateAuth(String phone) {
        showProgress(1);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressDialog.dismiss();
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        authInProgress = false;
                        progressDialog.dismiss();
                        countDownTimer.cancel();
                        verificationMessage.setText("Something went wrong" + ((e.getMessage() != null) ? ("\n" + e.getMessage()) : ""));
                        retryTimer.setVisibility(View.VISIBLE);
                        retryTimer.setText("Resend");
                        retryTimer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initiateAuth(phoneNumberInPrefs);
                            }
                        });
                        Toast.makeText(PhoneLoginActivity.this, "Something went wrong" + e.getMessage() != null ? "\n" + e.getMessage() : "", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        authInProgress = true;
                        progressDialog.dismiss();
                        mVerificationId = verificationId;
                        mResendToken = forceResendingToken;

                        verificationMessage.setText(String.format("Please type the verification code\nsent to %s", phoneNumberInPrefs));
                        retryTimer.setVisibility(View.GONE);
                    }
                });
        startCountdown();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        showProgress(2);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.setMessage("Logging you in!");
                login();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneLoginActivity.this, "Something went wrong" + e.getMessage() != null ? "\n" + e.getMessage() : "", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                authInProgress = false;
            }
        });
    }

    private void login() {
        authInProgress = true;

        //Take them to the register screen after they have logged in
        startActivity(new Intent(this, RegisterActivity.class));
        finish();




//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        userRef = firebaseDatabase.getReference(Helper.REF_USER).child(phoneNumberInPrefs);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                progressDialog.dismiss();
//                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        helper.setLoggedInUser(user);
//                        done();
//                    } else {
//                        Toast.makeText(SignInActivity.this, "Something went wrong, unable to create user.", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    final User newUser = new User(phoneNumberInPrefs, phoneNumberInPrefs, "My Case!", "");
//                    userRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            helper.setLoggedInUser(newUser);
//                            done();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }


    private void back() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel verification");
        builder.setMessage("Verification is in progress! do you want to cancel and go back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mHelper.clearPhoneNumberForVerification();
                recreate();
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        if (progressDialog.isShowing() || authInProgress) {
            builder.create().show();
        } else {
            mHelper.clearPhoneNumberForVerification();
            recreate();
        }
    }

    private void startCountdown() {
        retryTimer.setOnClickListener(null);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (retryTimer != null) {
                    retryTimer.setText(String.valueOf(l / 1000));
                }
            }

            @Override
            public void onFinish() {
                if (retryTimer != null) {
                    retryTimer.setText("Resend");
                    retryTimer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initiateAuth(phoneNumberInPrefs);
                        }
                    });
                }
            }
        }.start();
    }

    private void setupCountryCodes() {
        ArrayList<Country> countries = getCountries();
        if (countries != null) {
            ArrayAdapter<Country> myAdapter = new ArrayAdapter<Country>(this, R.layout.item_country_spinner, countries);
            spinnerCountryCodes.setAdapter(myAdapter);
            //spinnerCountryCodes.setSelection(239);
        }
    }

    private ArrayList<Country> getCountries() {
        ArrayList<Country> toReturn = new ArrayList<>();
//        toReturn.add(new Country("RU", "Russia", "+7"));
//        toReturn.add(new Country("TJ", "Tajikistan", "+992"));
        toReturn.add(new Country("ZW", "Zimbabwe", "+263"));
        return toReturn;

        /*try {
            JSONArray countrArray = new JSONArray(readEncodedJsonString(this));
            toReturn = new ArrayList<>();
            for (int i = 0; i < countrArray.length(); i++) {
                JSONObject jsonObject = countrArray.getJSONObject(i);
                String countryName = jsonObject.getString("name");
                String countryDialCode = jsonObject.getString("dial_code");
                String countryCode = jsonObject.getString("code");
                Country country = new Country(countryCode, countryName, countryDialCode);
                toReturn.add(country);
            }
            Collections.sort(toReturn, new Comparator<Country>() {
                @Override
                public int compare(Country lhs, Country rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;*/
    }

    private String readEncodedJsonString(Context context) throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    //Go to main activity
    private void done() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void submit() {
        //Validate and confirm number country codes selected
        if (spinnerCountryCodes.getSelectedItem() == null) {
            Toast.makeText(this, "Select country code!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            Toast.makeText(this, "Enter phone number!", Toast.LENGTH_LONG).show();
            return;
        }
        final String phoneNumber = ((Country) spinnerCountryCodes.getSelectedItem()).getDialCode() + etPhone.getText().toString().replaceAll("\\s+", "");
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(phoneNumber);
        builder.setMessage("One time password will be sent on this number! Continue with this number?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mHelper.setPhoneNumberForVerification(phoneNumber);
                recreate();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etPhone.requestFocus();
                keyboardUtil.openKeyboard();
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
