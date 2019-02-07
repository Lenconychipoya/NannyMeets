package zw.co.appsareus.nannymeets.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import zw.co.appsareus.nannymeets.interfaces.OnAddPreferredEmployee;
import zw.co.appsareus.nannymeets.interfaces.OnGetEmployeesListener;
import zw.co.appsareus.nannymeets.interfaces.OnImageUploadListener;
import zw.co.appsareus.nannymeets.interfaces.OnLoginListener;
import zw.co.appsareus.nannymeets.interfaces.OnProfileUpdateListener;
import zw.co.appsareus.nannymeets.interfaces.OnRegisterListener;
import zw.co.appsareus.nannymeets.interfaces.OnSignInStatusListener;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.ProfilePicture;
import zw.co.appsareus.nannymeets.models.User;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";
    private static final String ROOT = "root";
    private static final String NANNY_MEETS = "nannymeets";
    private static final String EMPLOYEE = "Employee";
    private static final String EMPLOYER = "Employer";
    private static final String PREFERRED_EMPLOYEES = "preferredemployees";
    private static final String SHORT_LISTED = "shortlisted";
    private static final String DATE = "date";

    private Context mContext;
    private Helper mHelper;

    //Listeners
    private OnLoginListener onLoginListener;
    private OnRegisterListener onRegisterListener;
    private OnGetEmployeesListener onGetEmployeesListener;
    private OnImageUploadListener onImageUploadListener;
    private OnProfileUpdateListener onProfileUpdateListener;
    private OnAddPreferredEmployee onAddPreferredEmployee;


    private OnSignInStatusListener onSignInStatusListener;


    public FirebaseMethods(Context mContext) {
        this.mContext = mContext;
        this.mHelper = new Helper(mContext);
    }

    public void login(final String username, String password, final String user_type) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(username, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseFirestore.getInstance()
                                .collection(ROOT)
                                .document(NANNY_MEETS)
                                .collection(user_type)
                                .whereEqualTo("email", username)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            onLoginListener.onFailuredLogin("Account does not exists as " + user_type);
                                            return;
                                        }
                                        if (user_type.equals(EMPLOYEE)) {
                                            Employee employee = queryDocumentSnapshots.toObjects(Employee.class).get(0);
                                            onLoginListener.onSuccessfulLoginEmployee(employee);
                                        } else {
                                            Employer employer = queryDocumentSnapshots.toObjects(Employer.class).get(0);
                                            onLoginListener.onSuccessfulLoginEmployer(employer);
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onLoginListener.onFailuredLogin(e.getMessage());
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        //showProgress(2);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressDialog.setMessage("Logging you in!");
                //login();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(SignInActivity.this, "Something went wrong" + e.getMessage() != null ? "\n" + e.getMessage() : "", Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
                //authInProgress = false;
            }
        });
    }

    private void initiateAuth(String phone) {
//        showProgress(1);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, (Activity) mContext,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                        progressDialog.dismiss();
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
//                        authInProgress = false;
//                        progressDialog.dismiss();
//                        countDownTimer.cancel();
//                        verificationMessage.setText("Something went wrong" + ((e.getMessage() != null) ? ("\n" + e.getMessage()) : ""));
//                        retryTimer.setVisibility(View.VISIBLE);
//                        retryTimer.setText("Resend");
//                        retryTimer.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                initiateAuth(phoneNumberInPrefs);
//                            }
//                        });
//                        Toast.makeText(SignInActivity.this, "Something went wrong" + e.getMessage() != null ? "\n" + e.getMessage() : "", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
//                        authInProgress = true;
//                        progressDialog.dismiss();
//                        mVerificationId = verificationId;
//                        mResendToken = forceResendingToken;
//
//                        verificationMessage.setText(String.format("Please type the verification code\nsent to %s", phoneNumberInPrefs));
//                        retryTimer.setVisibility(View.GONE);
                    }
                });
//        startCountdown();
    }
    public void registerUser(final User user, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        createUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onRegisterListener.onUserRegistrationFailed(e.getMessage());
                    }
                });
    }

    private void createUser(final User user) {

        Map<String, Object> temp = new HashMap<>();
        temp.put("temp", "temp");
        //Store in Firestore
        FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(user.getAccountType())
                .add(temp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        final String key = documentReference.getId();
                        user.setId(key);

                        FirebaseFirestore.getInstance()
                                .collection(ROOT)
                                .document(NANNY_MEETS)
                                .collection(user.getAccountType())
                                .document(key)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //User Created
                                        mHelper.setProfile(user);
                                        onRegisterListener.onUserRegistered();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Failed to create User
                                        onRegisterListener.onUserRegistrationFailed(e.getMessage());
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onRegisterListener.onUserRegistrationFailed(e.getMessage());
                    }
                });

    }

    private void uploadProfilePic(final Image im, final User user) {
        FirebaseStorage.getInstance()
                .getReference(ROOT)
                .child(NANNY_MEETS)
                .child(user.getAccountType())
                .child(user.getId())
                .child(im.getName())
                .putFile(Uri.fromFile(new File(im.getPath())))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ProfilePicture profilePicture = new ProfilePicture();
                        profilePicture.setBucketReference(taskSnapshot.getStorage().getPath());
                        profilePicture.setId(user.getId());
                        profilePicture.setPath(taskSnapshot.getStorage().getDownloadUrl().toString());
                        profilePicture.setName(im.getName());
                        user.setProfilePic(profilePicture);
                        FirebaseFirestore.getInstance()
                                .collection(ROOT)
                                .document(NANNY_MEETS)
                                .collection(user.getAccountType())
                                .document(user.getId())
                                .set(user, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onProfileUpdateListener.onSuccessfulProfileUpdate();
                                        onImageUploadListener.onSuccessfulImageUpload();
                                        //Save to local
                                        mHelper.setProfile(user);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        onImageUploadListener.onFailureImageUpload(e.getMessage());
                                        onProfileUpdateListener.onFailedProfileUpdate(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onProfileUpdateListener.onFailedProfileUpdate(e.getMessage());
                    }
                });
    }

    public void updateUserProfile(Image image, User user) {
        //Update user profile on and offline record
        uploadProfilePic(image, user);
    }

    public void getEmployees() {
        HashMap<String, Object> queryValues = new HashMap<>();
        queryValues.put("use_filter", "false");

        Query mQuery = FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(EMPLOYEE);
        //.orderBy("timestamp");
        if (queryValues.get("use_filter").toString().equals("true")) {
            for (Map.Entry pair : queryValues.entrySet()) {
                switch ((pair.getKey().toString().substring(0, 2))) {
                    case "min":
                        mQuery.whereGreaterThanOrEqualTo(pair.getKey().toString(), pair.getValue());
                        break;
                    case "max":
                        mQuery.whereLessThanOrEqualTo(pair.getKey().toString(), pair.getValue());
                    default:
                        mQuery.whereEqualTo((String) pair.getKey(), pair.getValue());
                }
            }
        }

        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Employee> employees = new ArrayList<>();
                if (e != null) {
                    Log.e(TAG, "onEvent: Get employees", e);
                    onGetEmployeesListener.onFailGetEmployees(e.getMessage());
                    return;
                }

                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    employees.add(document.toObject(Employee.class));
                }
                onGetEmployeesListener.onSuccessGetEmployees(employees);
            }
        });

    }


    public void getPreferredEmployees() {
        mHelper = new Helper(mContext);
        final ArrayList<String> recordIds = new ArrayList<>();

        //Get the selected Employees List
        FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(EMPLOYER)
                .document(mHelper.getUserId())
                .collection(PREFERRED_EMPLOYEES)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Get preferred employees", e);
                            onGetEmployeesListener.onFailGetEmployees(e.getMessage());
                            return;
                        }

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            recordIds.add(snapshot.getId());
                        }

                        //Retrieve the Employees data
                        FirebaseFirestore.getInstance()
                                .collection(ROOT)
                                .document(NANNY_MEETS)
                                .collection(EMPLOYEE)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        ArrayList<Employee> employees = new ArrayList<>();
                                        if (e != null) {
                                            Log.e(TAG, "onEvent: Get employees", e);
                                            onGetEmployeesListener.onFailGetEmployees(e.getMessage());
                                            return;
                                        }

                                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                                            for (String id : recordIds) {
                                                if (document.get("id").toString().equals(id)) {
                                                    employees.add(document.toObject(Employee.class));
                                                    return;
                                                }
                                            }
                                        }
                                        onGetEmployeesListener.onSuccessGetEmployees(employees);
                                    }
                                });
                    }
                });


    }

    public void addEmployeeToPreferredList(final String employeeId) {
        Map<String, Object> data = new HashMap<>();
        data.put(employeeId, employeeId);
        FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(EMPLOYER)
                .document(mHelper.getUserId())
                .collection(PREFERRED_EMPLOYEES)
                .document(employeeId)
                .set(data,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Increment Shortlisting counter for employee
                        Map<String, Object> data = new HashMap<>();
                        data.put(EMPLOYER, mHelper.getUserId());
                        data.put(DATE,Helper.getFormattedDate());
                        FirebaseFirestore.getInstance()
                                .collection(ROOT)
                                .document(NANNY_MEETS)
                                .collection(EMPLOYEE)
                                .document(employeeId)
                                .collection(SHORT_LISTED)
                                .add(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onAddPreferredEmployee.onFailedAddEmployee(e.getMessage());
                    }
                });
    }

    public void markViewedProfile(String key) {
        mHelper = new Helper(mContext);

        Map<String, Object> data = new HashMap<>();
        data.put(EMPLOYER, mHelper.getEmployerProfile().getId());

        FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(EMPLOYEE)
                .document(key)
                .collection("views")
                .document(mHelper.getEmployerProfile().getId())
                .set(data);
    }

    public void markAsEmployeedProfile(String key) {
        mHelper = new Helper(mContext);

        Map<String, Object> data = new HashMap<>();
        data.put(EMPLOYER, mHelper.getEmployerProfile().getId());

        FirebaseFirestore.getInstance()
                .collection(ROOT)
                .document(NANNY_MEETS)
                .collection(EMPLOYEE)
                .document(key)
                .collection("employeed")
                .document(mHelper.getEmployerProfile().getId())
                .set(data);
    }



    /**
     * Set Listener Methods*
     */

    public void setOnLoginListener(OnLoginListener listener) {
        this.onLoginListener = listener;
    }

    public void setOnRegisterListener(OnRegisterListener onRegisterListener) {
        this.onRegisterListener = onRegisterListener;
    }

    public void setOnGetEmployeesListener(OnGetEmployeesListener onGetEmployeesListener) {
        this.onGetEmployeesListener = onGetEmployeesListener;
    }

    public void setOnImageUploadListener(OnImageUploadListener onImageUploadListener) {
        this.onImageUploadListener = onImageUploadListener;
    }

    public void setOnProfileUpdateListener(OnProfileUpdateListener onProfileUpdateListener) {
        this.onProfileUpdateListener = onProfileUpdateListener;
    }

    public void setOnSignInStatusListener(OnSignInStatusListener onSignInStatusListener) {
        this.onSignInStatusListener = onSignInStatusListener;
    }

    public FirebaseMethods setOnAddPreferredEmployee(OnAddPreferredEmployee onAddPreferredEmployee) {
        this.onAddPreferredEmployee = onAddPreferredEmployee;
        return this;
    }
}
