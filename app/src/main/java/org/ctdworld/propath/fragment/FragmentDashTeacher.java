package org.ctdworld.propath.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DashboardHelper;


public class FragmentDashTeacher extends Fragment implements View.OnClickListener
{
    private final String TAG = FragmentDashTeacher.class.getSimpleName();
    Context mContext;

    DashboardHelper mDashboardHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_teacher, container, false);
        mContext = getContext();
        mDashboardHelper = new DashboardHelper(mContext);
        view.findViewById(R.id.dash_teacher_education).setOnClickListener(this);
        view.findViewById(R.id.dash_teacher_career).setOnClickListener(this);
       // view.findViewById(R.id.dash_video_analysis).setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.dash_teacher_education:
                //educationClicked();
                Log.i(TAG,"calling educationClicked() method ");
                mDashboardHelper.educationClicked();
                break;

            case R.id.dash_teacher_career:
                //careerClicked();
                mDashboardHelper.careerClicked();
                break;

//            case R.id.dash_video_analysis:
//               // startActivity(new Intent(getContext(), ActivitySurvey.class));
//                break;
        }
    }

    /*private void educationClicked()
    {
        Log.d(TAG," Role ID" +SessionHelper.getInstance(mContext).getUser().getRoleId());
        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.TEACHER_ROLE_ID))
        {
            DialogDashboard dialogDashboard = DialogDashboard.getInstance("School Review", "Progress Report");
            dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener() {
                @Override
                public void onButtonClicked(int buttonType) {
                    if (buttonType == DialogDashboard.BUTTON_TYPE_1)
                    {
                        Log.d(TAG,"school review"+"school review");
                        startActivity(new Intent(mContext, FragmentEducaSchoolReviewChooseAthlete.class));

                    }

                    if (buttonType == DialogDashboard.BUTTON_TYPE_2) {
                        startActivity(new Intent(mContext, FragmentEducationSchoolReviewChooseAthlete.class));
                    }
                }
            });

            dialogDashboard.show(getChildFragmentManager(), "");
        }
//        DialogDashboard dialogDashboard = DialogDashboard.getInstance("School Review", "Progress Report");
//        dialogDashboard.setOnDialogClickListener(new DialogDashboard.DialogDashboardClickListener()
//        {
//            @Override
//            public void onButtonClicked(int buttonType) {
//               if (buttonType == DialogDashboard.BUTTON_TYPE_1)
//                   startActivity(new Intent(getContext(), ActivityEducationSchoolReviewCreate.class));
//               if (buttonType == DialogDashboard.BUTTON_TYPE_2)
//                   startActivity(new Intent(getContext(), ActivityEducationProgressReport.class));
//            }
//        });
//
//        dialogDashboard.show(getChildFragmentManager(),"");
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
    }
*/

}
