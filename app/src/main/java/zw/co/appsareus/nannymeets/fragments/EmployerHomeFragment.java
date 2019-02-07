package zw.co.appsareus.nannymeets.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.activities.ViewProfileActivity;
import zw.co.appsareus.nannymeets.adapters.PostAdapter;
import zw.co.appsareus.nannymeets.interfaces.OnGetEmployeesListener;
import zw.co.appsareus.nannymeets.models.Employee;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerHomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnGetEmployeesListener, PostAdapter.OnPostClickedlistener {

    private static final String TAG = "EmployerHomeFragment";

    private TextView tvNoMaids;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Employee> mMaids = new ArrayList<>();



    public EmployerHomeFragment() {
        // Required empty public constructor
    }


    public static EmployerHomeFragment newInstance() {
        EmployerHomeFragment fragment = new EmployerHomeFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        mFirebaseMethods.setOnGetEmployeesListener(this);
        postAdapter = new PostAdapter(v.getContext(), mMaids);
        postAdapter.setOnPostClickedlistener(this);

        tvNoMaids = v.findViewById(R.id.tv_no_maids);
        tvNoMaids.setVisibility(View.GONE);

        mRecyclerView = v.findViewById(R.id.rv_maids_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        mRecyclerView.setAdapter(postAdapter);
        //mProgressBar = v.findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_refresh_maid_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);


                tvNoMaids.setVisibility(View.GONE);
                // Fetching data from server
                loadData();
            }
        });

    }

    @Override
    public void onRefresh() {
        //call the get mMaids method
        loadData();
    }

    private void loadData(){
        mFirebaseMethods.getEmployees();
    }

    @Override
    public void onSuccessGetEmployees(ArrayList<Employee> maids) {
        tvNoMaids.setVisibility((maids.size()>0) ? View.GONE : View.VISIBLE);
        mMaids.clear();
        mMaids.addAll(maids);
        postAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailGetEmployees(String error) {
        mSwipeRefreshLayout.setRefreshing(false);
        tvNoMaids.setVisibility(View.VISIBLE);
        Toast.makeText(v.getContext(), "Failed to get Employees. Please try again later.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onFailGetEmployees: " + error);
    }

    @Override
    public void getPostClicked(Employee mPost) {
        Intent intent = new Intent(v.getContext(),ViewProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(MAID,mPost);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
