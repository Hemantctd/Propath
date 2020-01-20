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


public class FragmentDashTrainer extends Fragment implements View.OnClickListener
{
    private final String TAG = FragmentDashTrainer.class.getSimpleName();

    Context mContext;
    ActivityMain mActivityMain;
    DashboardHelper mDashboardHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_trainer, container, false);

        mDashboardHelper = new DashboardHelper(getContext());
        view.findViewById(R.id.dash_training_plan).setOnClickListener(this);
        view.findViewById(R.id.dash_match_analysis).setOnClickListener(this);
        /*view.findViewById(R.id.dash_statistics).setOnClickListener(this);
        view.findViewById(R.id.dash_video_analysis).setOnClickListener(this);*/

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dash_training_plan:
                //trainingPlanClicked();
                mDashboardHelper.trainingPlanClicked();
                break;

            case R.id.dash_match_analysis:
                //statisticsClicked();
                mDashboardHelper.matchAnalysisClicked();
                break;

               /* case R.id.dash_statistics:
                //statisticsClicked();
                mDashboardHelper.statisticsClicked();
                break;

            case R.id.dash_video_analysis:
                //videoAnalysisClicked();
                mDashboardHelper.videoAnalysisClicked();
                break;*/
        }

    }


   /* private void  statisticsClicked()
    {
        DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
    }*/

 /*   private void trainingPlanClicked()
    {
        DialogDashboard dialogDashboard = DialogDashboard.getInstance("Provide","Create");
        dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
            @Override
            public void onButtonClicked(int buttonType) {
                if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    startActivity(new Intent(mContext,FragmentTrainingPlanCreated.class));
                if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                    startActivity(new Intent(mContext,FragmentTrainingPlanCreated.class));
            }
        });
        dialogDashboard.show(getChildFragmentManager(),ConstHelper.Tag.Fragment.DASHBOARD_DIALOG);
    }*/


    private void  videoAnalysisClicked()
    {
      //  startActivity(new Intent(mContext, ActivitySurvey.class));
    }


}
