package org.ctdworld.propath.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.ctdworld.propath.activity.ActivityCareer;
import org.ctdworld.propath.activity.ActivityChatUserGroupList;
import org.ctdworld.propath.activity.ActivityCoachFeedback;
import org.ctdworld.propath.activity.ActivityEducationForTeacher;
import org.ctdworld.propath.activity.ActivityEducationForAthlete;
import org.ctdworld.propath.activity.ActivityEducationForOtherFourRoles;
import org.ctdworld.propath.activity.ActivityMatchAnalysis;
import org.ctdworld.propath.activity.ActivityMatchDay;
import org.ctdworld.propath.activity.ActivityNutrition;
import org.ctdworld.propath.activity.ActivityTrainingPlan;
import org.ctdworld.propath.prefrence.SessionHelper;


// this is helper class for dashboards (there are multiple dashboard depending or user role), when a method is clicked in any dashboard
// then one method from this helper class is called accordingly.
public class DashboardHelper
{
    private final String TAG = DashboardHelper.class.getSimpleName();
    private Context mContext;

    public DashboardHelper(Context mContext) {
        this.mContext = mContext;
    }



    // this method is called when education is clicked in any dashboard (there are multiple dashboard depending on user role)
    public void educationClicked()
    {
        int role = Integer.parseInt(SessionHelper.getInstance(mContext).getUser().getRoleId());
        int roleParent = Integer.parseInt(RoleHelper.PARENT_GUARDIAN_ROLE_ID);
        int rolePlayerAgent = Integer.parseInt(RoleHelper.PLAYER_AGENT_ROLE_ID);
        int roleWelfare = Integer.parseInt(RoleHelper.PLAYER_WELFARE_ROLE_ID);
        int roleMentor = Integer.parseInt(RoleHelper.MENTOR_ROLE_ID);
        int roleClub = Integer.parseInt(RoleHelper.CLUB_MANAGEMENT_ROLE_ID);

        try
        {
            Log.i(TAG,"education clicked");
            if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.TEACHER_ROLE_ID))
            {
                Log.i(TAG, "education clicked , starting education activity ");
                Intent intent = new Intent(mContext, ActivityEducationForTeacher.class);
                //# setting whether only one fragment will be added or multiple fragments will be added as tabs
                intent.putExtra(ActivityEducationForTeacher.KEY_IS_FRAGMENT_SINGLE, false);

                mContext.startActivity(intent);
            }

            else if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
            {
                Intent intent = new Intent(mContext, ActivityEducationForAthlete.class);
                intent.putExtra(ActivityEducationForAthlete.KEY_ATHLETE_ID,SessionHelper.getInstance(mContext).getUser().getUserId());
                mContext.startActivity(intent);
            }
            else if (role == roleMentor || role == roleParent || role == rolePlayerAgent || role == roleWelfare || role == roleClub)
            {
                Intent intent = new Intent(mContext, ActivityEducationForOtherFourRoles.class);
                mContext.startActivity(intent);
            }

        }
        catch (Exception e)
        {
            Log.e(TAG,"error , "+e.getMessage());
            e.printStackTrace();
        }


    }


    // this method is called when career is clicked in any dashboard (there are multiple dashboard depending on user role)
    public void careerClicked()
    {
        // role contains the role id which user is logged in with.
        int role = Integer.parseInt(SessionHelper.getInstance(mContext).getUser().getRoleId());
        int roleAthlete = Integer.parseInt(RoleHelper.ATHLETE_ROLE_ID);
        int roleWelfare = Integer.parseInt(RoleHelper.PLAYER_WELFARE_ROLE_ID);
        if (role == roleAthlete || role == roleWelfare)
        {
            Intent intent = new Intent(mContext, ActivityCareer.class);
            //# setting whether only one fragment will be added or multiple fragments will be added as tabs
            intent.putExtra(ActivityCareer.KEY_IS_FRAGMENT_SINGLE, false);
            mContext.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(mContext, ActivityCareer.class);
            //# setting whether only one fragment will be added or multiple fragments will be added as tabs
            intent.putExtra(ActivityEducationForTeacher.KEY_IS_FRAGMENT_SINGLE, true);
            mContext.startActivity(intent);
        }

    }


    // this method is called when training plan is clicked in any dashboard (there are multiple dashboard depending or user role)
    public void trainingPlanClicked()
    {

        Intent intent = new Intent(mContext, ActivityTrainingPlan.class);
        //# setting whether only one fragment will be added or multiple fragments will be added as tabs
        intent.putExtra(ActivityEducationForTeacher.KEY_IS_FRAGMENT_SINGLE, false);

        mContext.startActivity(intent);

       /* DialogDashboard dialogDashboard = DialogDashboard.getInstance("Provided", "Create");
        dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
            @Override
            public void onButtonClicked(int buttonType) {
                if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    startActivity(new Intent(mContext, FragmentTrainingPlanCreated.class));
                if (buttonType == DialogDashboard.BUTTON_TYPE_2)
                    startActivity(new Intent(mContext, FragmentTrainingPlanCreated.class));
            }
        });
        dialogDashboard.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.DASHBOARD_DIALOG);*/
    }


    // this method is called when statistics is clicked in any dashboard (there are multiple dashboard depending or user role)
   /* public void statisticsClicked()
    {
        DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
    }*/

    // this method is called when Match Analysis is clicked in any dashboard (there are multiple dashboards depending or user role)
    public void matchAnalysisClicked()
    {
        //DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
        if (mContext != null)
            mContext.startActivity(new Intent(mContext, ActivityMatchAnalysis.class));
    }


    // this method is called when injury management is clicked in any dashboard (there are multiple dashboard depending or user role)
    public void injuryManagementClicked()
    {
        Intent intent = new Intent(mContext, ActivityChatUserGroupList.class);
        intent.putExtra(ActivityChatUserGroupList.KEY_TYPE_CHAT_LIST, ActivityChatUserGroupList.VALUE_INJURY_MANAGEMENT);
        if (mContext != null )
            mContext.startActivity(intent);
        //DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
    }



    // this method is called when coach feedback is clicked in any dashboard (there are multiple dashboard depending or user role)
    public void coachFeedbackClicked()
    {
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.COACH_ROLE_ID) )
        {

            Intent intent = new Intent(mContext, ActivityCoachFeedback.class);
            //# setting whether only one fragment will be added or multiple fragments will be added as tabs
            intent.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, false);

            mContext.startActivity(intent);

        }
        else if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.ATHLETE_ROLE_ID))
        {
            Intent intent = new Intent(mContext, ActivityCoachFeedback.class);
            //# setting whether only one fragment will be added or multiple fragments will be added as tabs
            intent.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, false);
            intent.putExtra(ActivityCoachFeedback.KEY_FRAGMENT_TYPE, ActivityCoachFeedback.FRAGMENT_COACH_VIEW_FEEDBACK);
            mContext.startActivity(intent);
           // mContext.startActivity(new Intent(mContext, FragmentCoachViewFeedback.class));
        }
        else
        {
            Intent intent = new Intent(mContext, ActivityCoachFeedback.class);
            //# setting whether only one fragment will be added or multiple fragments will be added as tabs
            intent.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, false);
            intent.putExtra(ActivityCoachFeedback.KEY_FRAGMENT_TYPE, ActivityCoachFeedback.FRAGMENT__FOR_OTHER_ROLE);
            mContext.startActivity(intent);
           // mContext.startActivity(new Intent(mContext,FragmentReviewedCoachFeedbackChooseAthlete.class));
        }
        // startActivity(new Intent(mContext, ActivityCoachFeedbackCoachView.class));
        //dash_coach_feedback
    }



    // this method is called when nutrition is clicked in any dashboard (there are multiple dashboard depending or user role)
    public void nutritionClicked()
    {
       // DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
        Intent intent = new Intent(mContext, ActivityNutrition.class);
        //# setting whether only one fragment will be added or multiple fragments will be added as tabs
        intent.putExtra(ActivityEducationForTeacher.KEY_IS_FRAGMENT_SINGLE, false);
        mContext.startActivity(intent);
    }


    // this method is called when nutrition is clicked in any dashboard (there are multiple dashboard depending or user role)
    public void matchDayClicked()
    {
        // DialogHelper.showSimpleCustomDialog(mContext,"Coming soon...");
        Intent intent = new Intent(mContext, ActivityMatchDay.class);
        mContext.startActivity(intent);
    }


}
