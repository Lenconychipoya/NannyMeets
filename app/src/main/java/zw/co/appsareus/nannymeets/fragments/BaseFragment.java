package zw.co.appsareus.nannymeets.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zw.co.appsareus.nannymeets.R;
import zw.co.appsareus.nannymeets.utils.FirebaseMethods;
import zw.co.appsareus.nannymeets.utils.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected static final String MAID = "Employee";
    protected static final String GARDENER = "Gardener";
    protected static final String EMPLOYER = "Employer";
    protected static final String EMPLOYEE = "Employee";
    protected View v;

    protected Helper mHelper;
    protected FirebaseMethods mFirebaseMethods;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseMethods = new FirebaseMethods(view.getContext());
        mHelper = new Helper(view.getContext());
    }
}
