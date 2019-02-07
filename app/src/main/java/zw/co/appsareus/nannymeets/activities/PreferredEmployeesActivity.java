package zw.co.appsareus.nannymeets.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.util.ArrayList;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.adapters.PostAdapter;
import zw.co.appsareus.nannymeets.fragments.PurchaseDialogFragment;
import zw.co.appsareus.nannymeets.interfaces.OnGetEmployeesListener;
import zw.co.appsareus.nannymeets.models.Employee;

public class PreferredEmployeesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,OnSmsCatchListener, OnGetEmployeesListener, PostAdapter.OnPostClickedlistener {

    private static final String TAG = "PreferredEActivity";
    private TextView tvNoEmployees, tvCredit;
    private RecyclerView mRecyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Employee> mEmployees = new ArrayList<>();
    private Button btnBuyMore;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SmsVerifyCatcher smsVerifyCatcher;
    private String[] permissions = {PermissionUtils.Manifest_CALL_PHONE,PermissionUtils.Manifest_READ_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_maids);

        setup();
    }

    private void setup() {
        tvCredit = findViewById(R.id.tv_credit);
        tvNoEmployees = findViewById(R.id.tv_no_maids);
        btnBuyMore = findViewById(R.id.btn_buy);
        smsVerifyCatcher = new SmsVerifyCatcher(this,this);

        btnBuyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show the buy dialog
                askCompactPermissions(permissions, new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        new PurchaseDialogFragment().show(getSupportFragmentManager(),TAG);
                    }

                    @Override
                    public void permissionDenied() {
                        Toast.makeText(PreferredEmployeesActivity.this, "Unable to complete process, permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionForeverDenied() {
                        Toast.makeText(PreferredEmployeesActivity.this, "Unable to complete process, permission denied forever", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mFirebaseMethods.setOnGetEmployeesListener(this);
        postAdapter = new PostAdapter(this, mEmployees);
        postAdapter.setOnPostClickedlistener(this);

        mRecyclerView = findViewById(R.id.rv_maids_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(postAdapter);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_maid_list);
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


                tvNoEmployees.setVisibility(View.GONE);
                // Fetching data from server
                loadData();
            }
        });
    }

    private void loadData(){
        mFirebaseMethods.getPreferredEmployees();
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


    @Override
    public void onRefresh() {

    }

    @Override
    public void onSmsCatch(String message) {
        Toast.makeText(this, "Payment Verified", Toast.LENGTH_SHORT).show();
        //Add 5 * the amount paid
    }


    @Override
    public void onSuccessGetEmployees(ArrayList<Employee> maids) {
        tvNoEmployees.setVisibility((maids.size()>0) ? View.GONE : View.VISIBLE);
        mEmployees.clear();
        mEmployees.addAll(maids);
        postAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailGetEmployees(String error) {
        mSwipeRefreshLayout.setRefreshing(false);
        tvNoEmployees.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Failed to get Employees. Please try again later.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onFailGetEmployees: " + error);
    }

    @Override
    public void getPostClicked(Employee mPost) {
        Intent intent = new Intent(this,ViewProfileActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(EMPLOYEE,mPost);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
