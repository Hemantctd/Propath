package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchAnalysisSetUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMatchTeamAnalysis extends Fragment implements View.OnClickListener {

    Context mContext;
    FloatingActionButton mFloatingIcon;
    RecyclerView mRecyclerView;

    public FragmentMatchTeamAnalysis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_match_team_analysis, container, false);
        init(view);

        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mFloatingIcon = view.findViewById(R.id.float_icon);
        mRecyclerView = view.findViewById(R.id.recycler_create_statistics);
        mFloatingIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.float_icon :
               mContext.startActivity(new Intent(new Intent(mContext, ActivityMatchAnalysisSetUp.class)));
                break;
        }
    }
}
