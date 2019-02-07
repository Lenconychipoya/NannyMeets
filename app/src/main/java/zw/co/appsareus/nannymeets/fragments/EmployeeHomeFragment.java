package zw.co.appsareus.nannymeets.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.models.Employee;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeHomeFragment extends BaseFragment{

    private static final String TAG = "EmployeeHomeFragment";

    private TextView tvExperience,tvQualifications,tvQassport,tvDrivers_licence,tvReligion,tvChurch,tvSalary,tvType_of_employment,tvStatus;
    private TextView tvName,tvSurname,tvUsername, tvAge,tvGender,tvEmail,tvPhone,tvAddress,tvNatID;
    private Toolbar mToolbar;
    private Bundle extras;
    private Employee mMaid;
    private Button btnEditProfile;
    private OnOpenProfileListener onOpenProfileListener;

    private View v;




    public EmployeeHomeFragment() {
        // Required empty public constructor
    }

    public static EmployeeHomeFragment newInstance() {
        EmployeeHomeFragment fragment = new EmployeeHomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maid_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        mToolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);


        //extras = getIntent().getExtras();
        mMaid = mHelper.getEmployeeProfile();


        setupViews();
        mFirebaseMethods.markViewedProfile(mMaid.getId());
    }

    private void setupViews(){

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mMaid.getName()+" "+mMaid.getSurname());
        //mToolbar.setTitle();
        tvType_of_employment = v.findViewById(R.id.tv_employment_type);
        tvAge = v.findViewById(R.id.tv_age);
        tvQualifications = v.findViewById(R.id.tv_qualifications);
        tvEmail = v.findViewById(R.id.tv_email);
        tvPhone = v.findViewById(R.id.tv_phone);
        tvAddress = v.findViewById(R.id.tv_address);
        tvNatID = v.findViewById(R.id.tv_nat_id);
        tvQassport = v.findViewById(R.id.tv_passport);
        tvDrivers_licence = v.findViewById(R.id.tv_drivers_licence);
        tvReligion = v.findViewById(R.id.tv_religion);
        tvChurch = v.findViewById(R.id.tv_church);
        tvSalary = v.findViewById(R.id.tv_salary);
        tvExperience = v.findViewById(R.id.tv_experience);
        tvGender = v.findViewById(R.id.tv_gender);
        tvStatus = v.findViewById(R.id.tv_job_status);

        tvType_of_employment.setText(mMaid.getOccupation());
        tvAge.setText(mMaid.getDob());
        tvQualifications.setText(mMaid.getQualifications());
        tvEmail.setText(mMaid.getEmail());
        tvPhone.setText(mMaid.getPhone());
        tvAddress.setText(mMaid.getAddress());
        tvNatID.setText(mMaid.getNatID());
        tvQassport.setText(mMaid.getPassport());
        tvDrivers_licence.setText(mMaid.getDriversLicence());
        tvReligion.setText(mMaid.getReligion());
        tvChurch.setText(mMaid.getChurch());
        tvSalary.setText(""+mMaid.getExpectedSalary());
        tvExperience.setText(mMaid.getExperience());
        tvGender.setText(mMaid.getGender());
        tvStatus.setText(mMaid.getJobStatus());

        btnEditProfile = v.findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenProfileListener.onOpenProfile();
            }
        });
    }

    public interface OnOpenProfileListener{
        void onOpenProfile();
    }

    public EmployeeHomeFragment setOnOpenProfileListener(OnOpenProfileListener onOpenProfileListener) {
        this.onOpenProfileListener = onOpenProfileListener;
        return this;
    }
}
