package org.ctdworld.propath.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTrainingPlanResult extends Fragment {


    public FragmentTrainingPlanResult() {
        // Required empty public constructor
    }



    public static FragmentTrainingPlanResult getInstance()
    {
        FragmentTrainingPlanResult fragmentTrainingPlanResult = new FragmentTrainingPlanResult();
       /* Bundle bundle = new Bundle();

        fragmentTrainingPlanList.setArguments(bundle);*/

        return fragmentTrainingPlanResult;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
           // mTypeReceivedOrCreated = bundle.getInt(ConstHelper.Key.TYPE_RECEIVED_OR_CREATED);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training_plan_result, container, false);
    }

}
