package zw.co.appsareus.nannymeets.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.interfaces.OnImageUploadListener;
import zw.co.appsareus.nannymeets.interfaces.OnProfileUpdateListener;
import zw.co.appsareus.nannymeets.models.Country;
import zw.co.appsareus.nannymeets.models.Employee;
import zw.co.appsareus.nannymeets.models.Employer;
import zw.co.appsareus.nannymeets.models.ProfilePicture;
import zw.co.appsareus.nannymeets.models.User;
import zw.co.appsareus.nannymeets.utils.FirebaseMethods;
import zw.co.appsareus.nannymeets.utils.Helper;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements OnProfileUpdateListener, OnImageUploadListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProfileFragment";

    private static final int REQUEST_CODE_PICKER = 1234;

    // TODO: Rename and change types of parameters
    private String[] maritalStatus = {"SINGLE","MARRIED","DIVORCED","WIDOWED"};
    private String[] occupationType = {"STAY","STAY OUT","PART TIME"};
    private String[] professionType = {"MAID","GARDENER"};
    private String[] sex = {"MALE","FEMALE"};
    ArrayList<Country> countries = new ArrayList<>();
    private String mParam1;
    private String mParam2;
    private Image myProfile = new Image(1,"default","");
    private FrameLayout flimage;
    private View v;
    private User mUser;
    private String accountType;
    private EditText etExperience,etQualifications,etQassport,etDrivers_licence,etReligion,etChurch,etSalary,etType_of_employment,etExpectedSalary,etAvailabilityStatus,etCity,etLanguages;
    private TextInputLayout tilExperience,tilQualifications,tilQassport,tilDrivers_licence,tilReligion,tilChurch,tilType_of_employment,tilExpectedSalary,tilAvailabilityStatus,tilCity,tilLanguages;
    private EditText etName,etSurname,etUsername,etDob,etEmail,etPhone,etAddress,etNatID;
    private LinearLayout llCountry, llMaritalStatus, llOcupation, llProfession;
    private LinearLayout llLoader;
    private Spinner spCountry, spMaritalStatus, spOccupation, spProfession, spGender;
    private Button btnSave;
    private CircleImageView civProfilePic;
    private TextView tvNoPhotoText;
    private String ms,country,occupation,profession,gender;


    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        if (mHelper.getAccountType().equals(EMPLOYEE)){
            mUser = mHelper.getEmployeeProfile();
        }else{
            mUser = mHelper.getEmployerProfile();
        }


        llLoader = v.findViewById(R.id.ll_progress);
        llLoader.setVisibility(View.GONE);

        //common fields
        etName = v.findViewById(R.id.et_name);
        etSurname = v.findViewById(R.id.et_surname);
        etUsername = v.findViewById(R.id.et_username);
        etDob = v.findViewById(R.id.et_dob);
        etEmail = v.findViewById(R.id.et_email);
        etPhone = v.findViewById(R.id.et_phone);
        etAddress = v.findViewById(R.id.et_address);
        etNatID = v.findViewById(R.id.et_nat_id);


        //maid fields
        civProfilePic = v.findViewById(R.id.profile_image);
        tvNoPhotoText = v.findViewById(R.id.tv_no_photo);
        etExperience = v.findViewById(R.id.et_experience);
        etQualifications = v.findViewById(R.id.et_qualifications);
        etQassport = v.findViewById(R.id.et_passport);
        etDrivers_licence = v.findViewById(R.id.et_drivers_licence);
        etReligion = v.findViewById(R.id.et_religion);
        etChurch = v.findViewById(R.id.et_church);
        etSalary = v.findViewById(R.id.et_expected_salary);
        etType_of_employment = v.findViewById(R.id.et_type_of_employment);
        etExpectedSalary = v.findViewById(R.id.et_expected_salary);
        etLanguages = v.findViewById(R.id.et_languages);
        etCity = v.findViewById(R.id.et_city);
        etAvailabilityStatus = v.findViewById(R.id.et_availability_status);

        tilExperience = v.findViewById(R.id.til_experience);
        tilQualifications = v.findViewById(R.id.til_qualifications);
        tilQassport = v.findViewById(R.id.til_passport);
        tilDrivers_licence = v.findViewById(R.id.til_drivers_licence);
        tilReligion = v.findViewById(R.id.til_religion);
        tilChurch = v.findViewById(R.id.til_church);
        tilType_of_employment = v.findViewById(R.id.til_type_of_employment);
        tilExpectedSalary = v.findViewById(R.id.til_expected_salary);
        tilAvailabilityStatus = v.findViewById(R.id.til_availability_status);
        tilLanguages = v.findViewById(R.id.til_languages);
        tilCity = v.findViewById(R.id.til_city);

        llCountry =v.findViewById(R.id.ll_country);
        llMaritalStatus = v.findViewById(R.id.ll_marital_status);
        llOcupation = v.findViewById(R.id.ll_occupation);
        llProfession = v.findViewById(R.id.ll_profession);

        spCountry = v.findViewById(R.id.spinner_country);
        spOccupation = v.findViewById(R.id.spinner_occupation_type);
        spMaritalStatus = v.findViewById(R.id.spinner_marital_status);
        spProfession = v.findViewById(R.id.spinner_profession);
        spGender = v.findViewById(R.id.spinner_gender);

        mFirebaseMethods.setOnProfileUpdateListener(this);
        mFirebaseMethods.setOnImageUploadListener(this);
        accountType = mHelper.getAccountType();
        setUpViews(accountType, mHelper.getProfile());
        setupCountryCodes();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, maritalStatus);
        spMaritalStatus.setAdapter(myAdapter);
        spMaritalStatus.setSelection(0);
        spMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ms = adapterView.getItemAtPosition(i).toString();
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, occupationType);
        spOccupation.setAdapter(myAdapter2);
        spOccupation.setSelection(0);
        spOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                occupation = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, professionType);
        spOccupation.setAdapter(myAdapter3);
        spOccupation.setSelection(0);
        spOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                profession = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> myAdapter4 = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, sex);
        spGender.setAdapter(myAdapter4);
        spGender.setSelection(0);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave = v.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                llLoader.setVisibility(View.VISIBLE);

                if (accountType.equals(EMPLOYEE)){
                    mUser = new Employee(
                            etName.getText().toString(),
                            etSurname.getText().toString(),
                            etDob.getText().toString(),
                            gender,
                            mHelper.getEmployeeProfile().getId(),
                            etEmail.getText().toString(),
                            etPhone.getText().toString(),
                            etAddress.getText().toString(),
                            etNatID.getText().toString(),
                            "",
                            accountType,
                            etUsername.getText().toString(),
                            etCity.getText().toString(),
                            country,
                            etExperience.getText().toString(),
                            etQualifications.getText().toString(),
                            etQassport.getText().toString(),
                            etDrivers_licence.getText().toString(),
                            etReligion.getText().toString(),
                            etChurch.getText().toString(),
                            Float.parseFloat(etExpectedSalary.getText().toString()),
                            occupation,
                            etAvailabilityStatus.getText().toString(),
                            ms,
                            etLanguages.getText().toString(),
                            profession
                    );
                    mUser.setProfilePic(new ProfilePicture(
                            "1",
                            "default",
                            "",
                            ""
                    ));
                } else{
                    mUser = new Employer(
                            etName.getText().toString(),
                            etSurname.getText().toString(),
                            etDob.getText().toString(),
                            gender,
                            mHelper.getEmployerProfile().getId(),
                            etEmail.getText().toString(),
                            etPhone.getText().toString(),
                            etAddress.getText().toString(),
                            etNatID.getText().toString(),
                            "",
                            accountType,
                            etUsername.getText().toString(),
                            etCity.getText().toString(),
                            country
                    );
                    mUser.setProfilePic(new ProfilePicture(
                            "1",
                            "default",
                            "",
                            ""
                    ));
                }

                //save profile
                mFirebaseMethods.updateUserProfile(myProfile,mUser);
            }
        });

        flimage = v.findViewById(R.id.fl_image);
        flimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(ProfileFragment.this)
                        .folderMode(true)
                        .showCamera(true)
                        .imageDirectory("nanny_meets")
                        .theme(R.style.AppTheme)
                        .single()
                        .returnAfterFirst(true)
                        .start(REQUEST_CODE_PICKER);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PICKER:
                    if (data != null) {
                        ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
                        //mPost.setPics(myImages);

                        for (Image im : images) {
                            myProfile = im;
                            mUser.setProfilePic(new ProfilePicture(
                                    mUser.getId(),
                                    im.getName(),
                                    im.getPath(),
                                    ""
                            ));
                            Glide.with(v).load(im.getPath()).into(civProfilePic);
                            tvNoPhotoText.setVisibility(View.GONE);
//                            mFirebaseMethods.
                        }
                        Snackbar.make(v,images.size() + " image selected.",Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void setUpViews(String acc,User user){
        //Setup common views
        if (user.getProfilePic() != null){
            if (!user.getProfilePic().equals("")) {
                Glide.with(v).load(user.getProfilePic().getPath()).into(civProfilePic);
                tvNoPhotoText.setVisibility(View.GONE);
            }
        }

        etName.setText(user.getName());
        etSurname.setText(user.getSurname());
        etUsername.setText(user.getUsername());
        etDob.setText(user.getDob());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        etNatID.setText(user.getNatID());
        spGender.setSelection(getIndexOf(sex,user.getGender()));

        spCountry.setSelection(getIndexOf(countries,user.getCountry()));

        switch (acc){
            case EMPLOYEE :
                Employee mMaid = (Employee)user;
                //Show views
                tilExperience.setVisibility(View.VISIBLE);
                tilQualifications.setVisibility(View.VISIBLE);
                tilQassport.setVisibility(View.VISIBLE);
                tilDrivers_licence.setVisibility(View.VISIBLE);
                tilReligion.setVisibility(View.VISIBLE);
                tilChurch.setVisibility(View.VISIBLE);
                tilType_of_employment.setVisibility(View.VISIBLE);
                tilExpectedSalary.setVisibility(View.VISIBLE);
                tilAvailabilityStatus.setVisibility(View.VISIBLE);
                tilLanguages.setVisibility(View.VISIBLE);

                llMaritalStatus.setVisibility(View.VISIBLE);
                llOcupation.setVisibility(View.VISIBLE);
                llProfession.setVisibility(View.VISIBLE);

                spOccupation.setSelection(getIndexOf(occupationType,((Employee) user).getOccupation()));
                spMaritalStatus.setSelection(getIndexOf(maritalStatus,((Employee) user).getMaritalStatus()));
                spProfession.setSelection(getIndexOf(professionType,((Employee) user).getProfession()));


                //Setup values
                etExperience.setText(mMaid.getExperience());
                etQualifications.setText(mMaid.getQualifications());
                etQassport.setText(mMaid.getPassport());
                etDrivers_licence.setText(mMaid.getDriversLicence());
                etReligion.setText(mMaid.getReligion());
                etChurch.setText(mMaid.getChurch());
                etSalary.setText(""+mMaid.getExpectedSalary());
                etType_of_employment.setText(mMaid.getOccupation());
                etAvailabilityStatus.setText(mMaid.getJobStatus());


                break;
            case EMPLOYER :
                //Hide views
                tilExperience.setVisibility(View.GONE);
                tilQualifications.setVisibility(View.GONE);
                tilQassport.setVisibility(View.GONE);
                tilDrivers_licence.setVisibility(View.GONE);
                tilReligion.setVisibility(View.GONE);
                tilChurch.setVisibility(View.GONE);
                tilType_of_employment.setVisibility(View.GONE);
                tilExpectedSalary.setVisibility(View.GONE);
                tilAvailabilityStatus.setVisibility(View.GONE);
                tilLanguages.setVisibility(View.GONE);

                llCountry.setVisibility(View.GONE);
                llMaritalStatus.setVisibility(View.GONE);
                llOcupation.setVisibility(View.GONE);
                llProfession.setVisibility(View.GONE);

                break;
        }
    }

    private void setupCountryCodes() {
        countries = mHelper.getCountries(v.getContext());
        if (countries != null) {
            ArrayAdapter<Country> myAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, countries);
            spCountry.setAdapter(myAdapter);
            spCountry.setSelection(239);
            spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    country = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private int getIndexOf(String[] array, String search){
        int i;
        for (i=0; i< array.length;i++){
            if (search.equals(array[i])){
                return i;
            }
        }
        return i;
    }
    private int getIndexOf(ArrayList<Country> array, String search){
        int i;
        for (i=0; i< array.size();i++){
            if (search.equals(array.get(i))){
                return i;
            }
        }
        return i;
    }

    @Override
    public void onSuccessfulProfileUpdate() {
        llLoader.setVisibility(View.GONE);
        Toast.makeText(v.getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailedProfileUpdate(String error) {
        llLoader.setVisibility(View.GONE);
        Toast.makeText(v.getContext(), "Profile Updated Failed. " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfulImageUpload() {
        Log.i(TAG, "onSuccessfulImageUpload: Image Uploaded");
    }

    @Override
    public void onFailureImageUpload(String error) {
        Log.e(TAG, "onFailureImageUpload: " +error);
    }
}
