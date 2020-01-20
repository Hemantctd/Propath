package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchAnalysisCreateTeamList;
import org.ctdworld.propath.helper.DialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchIndividualStatistics extends Fragment implements View.OnClickListener {

    Context mContext;
    Button mCreateTeamA, mCreateTeamB, mNextBtn;

    public FragmentMatchIndividualStatistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match_individual_statistics, container, false);
        init(view);
        showPopUp();
        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mCreateTeamA = view.findViewById(R.id.create_team_a);
        mCreateTeamB = view.findViewById(R.id.create_team_b);
        mNextBtn = view.findViewById(R.id.next_btn);

        mCreateTeamA.setOnClickListener(this);
        mCreateTeamB.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

    }
    private void showPopUp()
    {

        DialogHelper.showSimpleCustomDialog(mContext,"Congratulations!","You're ready to go. Your match code is XYZ12.");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.create_team_a :
                startActivity(new Intent(mContext, ActivityMatchAnalysisCreateTeamList.class));
                break;
            case R.id.create_team_b :
                startActivity(new Intent(mContext, ActivityMatchAnalysisCreateTeamList.class));

                break;
            case R.id.next_btn :
                startActivity(new Intent(mContext, ActivityMatchAnalysisCreateTeamList.class));

                break;
        }

    }
}
