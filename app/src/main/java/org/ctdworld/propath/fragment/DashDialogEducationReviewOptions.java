package org.ctdworld.propath.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashDialogEducationReviewOptions extends DialogFragment implements View.OnClickListener {


    public DashDialogEducationReviewOptions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dash_dialog_education_review_options,container,false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.dash_dialog_education_review_btn_self_review).setOnClickListener(this);
        view.findViewById(R.id.dash_dialog_education_review_btn_school_review).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.dash_dialog_education_review_btn_self_review:

                break;

            case R.id.dash_dialog_education_review_btn_school_review:

                break;
        }

    }
}
