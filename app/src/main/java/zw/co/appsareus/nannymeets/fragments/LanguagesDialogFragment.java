package zw.co.appsareus.nannymeets.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import zw.co.appsareus.nannymeets.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LanguagesDialogFragment extends DialogFragment {

    View view;

    public LanguagesDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setSingleChoiceItems(new String[]{"English","Shona","Ndebele"}, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] langs = new String[]{"English","Shona","Ndebele"};

                        String languageToLoad="en";

                        switch (langs[i]) {
                            case "English" :
                                languageToLoad = "en";
                                break;
                            case "Shona" :
                                languageToLoad = "sh";
                                break;

                            case "Ndebele" :
                                languageToLoad = "nd";
                                break;

                            default:
                                break;
                        }

                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

}
