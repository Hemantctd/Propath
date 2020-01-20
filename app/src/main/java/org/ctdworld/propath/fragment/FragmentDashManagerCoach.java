package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMain;
import org.ctdworld.propath.helper.DashboardHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashManagerCoach extends Fragment implements View.OnClickListener{

    private final String TAG = FragmentDashManagerCoach.class.getSimpleName();

    Context mContext;
    ActivityMain mActivityMain;
    DashboardHelper mDashboardHelper;


    public FragmentDashManagerCoach() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dash_coach, container, false);
        mContext = getContext();
        mDashboardHelper = new DashboardHelper(mContext);
      //  view.findViewById(R.id.dash_career).setOnClickListener(this);
        view.findViewById(R.id.dash_training_plan).setOnClickListener(this);
        view.findViewById(R.id.dash_coach_feedback).setOnClickListener(this);
        view.findViewById(R.id.dash_nutrition).setOnClickListener(this);
        view.findViewById(R.id.dash_match_analysis).setOnClickListener(this);
        /*view.findViewById(R.id.dash_video_analysis).setOnClickListener(this);
        view.findViewById(R.id.dash_statistics).setOnClickListener(this);*/
        view.findViewById(R.id.dash_injury_management).setOnClickListener(this);
        view.findViewById(R.id.dash_match_day).setOnClickListener(this);



        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityMain = (ActivityMain) context;
    }

    @Override
    public void onClick(View view) {

        if (mDashboardHelper == null)
            return;

        switch (view.getId()) {

           /* case R.id.dash_career:
                //careerClicked();
                mDashboardHelper.careerClicked();
                break;*/


            case R.id.dash_training_plan:
                //trainingPlanClicked();
                mDashboardHelper.trainingPlanClicked();
                break;

            case R.id.dash_match_analysis:
                //DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
                mDashboardHelper.matchAnalysisClicked();
                break;

               /* case R.id.dash_statistics:
                //DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
                mDashboardHelper.statisticsClicked();
                break;

            case R.id.dash_video_analysis:
                //videoAnalysisClicked();
                mDashboardHelper.videoAnalysisClicked();
                break;*/

            case R.id.dash_injury_management:
                //DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
                mDashboardHelper.injuryManagementClicked();
                break;

            case R.id.dash_coach_feedback:
                //coachFeedback();
                mDashboardHelper.coachFeedbackClicked(
                );
                break;

            case R.id.dash_nutrition:
                //nutritionClicked();
                mDashboardHelper.nutritionClicked();
                break;
            case R.id.dash_match_day:
                //nutritionClicked();
                mDashboardHelper.matchDayClicked();
                break;
        }
    }
}
