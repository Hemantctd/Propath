package org.ctdworld.propath.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNewsFeed;
import org.ctdworld.propath.helper.ActivityHelper;
import org.ctdworld.propath.prefrence.SessionHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboardContainer extends Fragment
{
    private final String TAG = FragmentDashboardContainer.class.getSimpleName();


    public FragmentDashboardContainer()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard_container, container, false);


        int roleId = Integer.valueOf(SessionHelper.getInstance(getContext()).getUser().getRoleId());
        final int COACH_ROLE_ID = 1;
        final int MANAGER_ROLE_ID =  2;
        final int TRAINER_ROLE_ID =  3;
        final int PHYSIO_ROLE_ID =  4;
        final int DOCTOR_ROLE_ID =  5;
        final int CLUB_MANAGEMENT_ROLE_ID =  6;
        final int PLAYER_AGENT_ROLE_ID =  7;
        final int NUTRITIONIST_ROLE_ID =  8;
        final int STATISTICIAN_ROLE_ID =  9;
        final int TEACHER_ROLE_ID =  10;
        final int PLAYER_WELFARE_ROLE_ID =  11;
        final int PARENT_GUARDIAN_ROLE_ID =  12;
        final int ATHLETE_ROLE_ID =  13;
        final int MENTOR_ROLE_ID =  14;

        switch (roleId)
        {
            case COACH_ROLE_ID:
                //showDashboard(new FragmentDashManagerCoach());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;
            case MANAGER_ROLE_ID:
                //showDashboard(new FragmentDashManagerCoach());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;
            case TRAINER_ROLE_ID:
                //showDashboard(new FragmentDashTrainer());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;

            case PHYSIO_ROLE_ID:
                //showDashboard(new FragmentDashPhysioDoctor());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;

            case DOCTOR_ROLE_ID:
                //showDashboard(new FragmentDashPhysioDoctor());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;


           /* case CLUB_MANAGEMENT_ROLE_ID:
                showDashboard(new FragmentDashManger());
                break;*/

            /*case PLAYER_AGENT_ROLE_ID:
                showDashboard(new FragmentDashStatistician());

                break;*/

            case NUTRITIONIST_ROLE_ID:
                //showDashboard(new FragmentDashNutrition());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;
            case STATISTICIAN_ROLE_ID:
                //showDashboard(new FragmentDashStatistician());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;

            case TEACHER_ROLE_ID:
                //showDashboard(new FragmentDashTeacher());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                break;

            default:
                    //showDashboard(new FragmentDashboard());
                if ( !ActivityHelper.getInstance().isNewsFeedVisible())
                    startActivity(new Intent(getActivity(), ActivityNewsFeed.class));
                    break;

        }



        return view;
    }


    private void showDashboard(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_dashboard,fragment,"").commit();
    }

}
