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
public class FragmentDashboard extends Fragment implements View.OnClickListener {
    private final String TAG = FragmentDashboard.class.getSimpleName();

    Context mContext;
    ActivityMain mActivityMain;
    DashboardHelper mDashboardHelper;


    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mContext = getContext();
        mDashboardHelper = new DashboardHelper(mContext);
        /*view.findViewById(R.id.dash_education).setOnClickListener(this);
        view.findViewById(R.id.dash_career).setOnClickListener(this);
        view.findViewById(R.id.dash_training_plan).setOnClickListener(this);
        view.findViewById(R.id.dash_coach_feedback).setOnClickListener(this);
        view.findViewById(R.id.dash_nutrition).setOnClickListener(this);
        view.findViewById(R.id.dash_match_analysis).setOnClickListener(this);
        view.findViewById(R.id.dash_match_day).setOnClickListener(this);

       *//* view.findViewById(R.id.dash_video_analysis).setOnClickListener(this);
        view.findViewById(R.id.dash_statistics).setOnClickListener(this);*//*
        view.findViewById(R.id.dash_injury_management).setOnClickListener(this);*/


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityMain = (ActivityMain) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dash_education:
                /// educationClicked();
                mDashboardHelper.educationClicked();
                break;

            case R.id.dash_career:
                //careerClicked();
                mDashboardHelper.careerClicked();
                break;


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


    /*private void educationClicked() {
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.TEACHER_ROLE_ID))
        {
            DialogDashboard dialogDashboard = DialogDashboard.getInstance("School Review", "Progress Report");
            dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
                @Override
                public void onButtonClicked(int buttonType) {
                    if (buttonType == DialogDashboard.BUTTON_TYPE_1) {
                        startActivity(new Intent(mContext, FragmentEducaSchoolReviewChooseAthlete.class));
                    }
                    if (buttonType == DialogDashboard.BUTTON_TYPE_2) {
                        startActivity(new Intent(mContext, FragmentEducationSchoolReviewChooseAthlete.class));
                    }
                }
            });

            dialogDashboard.show(getChildFragmentManager(), "");
        }

            else if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
            {
                Intent i = new Intent(mContext,ActivityEducationProgressReport.class);
                i.putExtra("athlete_id",SessionHelper.getInstance(mContext).getUser().getUserId());
                startActivity(i);

            }
            else
            {
                startActivity(new Intent(mContext,FragmentEducationSchoolReviewChooseAthlete.class));
            }
    }*/




  /*  private void careerClicked()
    {
        // role contains the role id which user is logged in with.
        int role = Integer.parseInt(SessionHelper.getInstance(mContext).getUser().getRoleId());
        int roleAthlete = Integer.parseInt(RoleHelper.ATHLETE_ROLE_ID);
        int roleWelfare = Integer.parseInt(RoleHelper.PLAYER_WELFARE_ROLE_ID);
        if (role == roleAthlete || role == roleWelfare)
        {
             DialogDashboard dialogDashboard = DialogDashboard.getInstance("Employment Tools", "Career PlanData");
             dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener()
             {
                 @Override
                 public void onButtonClicked(int buttonType)
                 {
                     if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                         startActivity(new Intent(getContext(), FragmentCareerEmploymentTools.class));
                     if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                         startActivity(new Intent(getContext(), FragmentCareerPlan.class));
                 }
             });
             dialogDashboard.show(getChildFragmentManager(),"");
        }
        else
            startActivity(new Intent(mContext,FragmentCareerPlan.class));
    }*/


  /*  private void trainingPlanClicked()
    {
        DialogDashboard dialogDashboard = DialogDashboard.getInstance("Provided", "Create");
        dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
            @Override
            public void onButtonClicked(int buttonType) {
                if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    startActivity(new Intent(mContext, FragmentTrainingPlanCreated.class));
                if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                    startActivity(new Intent(mContext, FragmentTrainingPlanCreated.class));
            }
        });
        dialogDashboard.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DASHBOARD_DIALOG);
    }
*/



    /*private void coachFeedback()
    {
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.COACH_ROLE_ID) )
        {

            DialogDashboard dialogDashboard = DialogDashboard.getInstance("Coach Feedback", "View Feedback");
            dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
                @Override
                public void onButtonClicked(int buttonType) {
                    if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    {
                        startActivity(new Intent(mContext, FragmentCoachFeedbackChooseAthlete.class));
                    }
                    if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                    {
                        startActivity(new Intent(mContext, FragmentReviewedCoachFeedbackChooseAthlete.class));
                    }
                }
            });
//
            dialogDashboard.show(getChildFragmentManager(), "");
        }
        else if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
            {
               startActivity(new Intent(mContext, FragmentCoachViewFeedback.class));
            }
            else
           {
               startActivity(new Intent(mContext,FragmentReviewedCoachFeedbackChooseAthlete.class));
           }
       // startActivity(new Intent(mContext, ActivityCoachFeedbackCoachView.class));
        //dash_coach_feedback
    }*/

  /*  private void nutritionClicked()
    {
        DialogDashboard dialogDashboard = DialogDashboard.getInstance("News","Statistics");
        dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
            @Override
            public void onButtonClicked(int buttonType) {
                if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    startActivity(new Intent(mContext,FragmentNutrition.class));
                if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                    startActivity(new Intent(mContext,FragmentNutrition.class));
            }
        });
        dialogDashboard.show(getChildFragmentManager(),ConstHelper.Tag.Fragment.DASHBOARD_DIALOG);
    }
*/


  /*  private void  videoAnalysisClicked()
    {
       // startActivity(new Intent(mContext, ActivitySurvey.class));
    }*/

}
