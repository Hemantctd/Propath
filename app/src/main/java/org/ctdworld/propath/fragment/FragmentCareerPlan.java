package org.ctdworld.propath.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ctdworld.propath.R;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.helper.ConstHelper;

public class FragmentCareerPlan extends BaseFragment
{
    private final String TAG = FragmentCareerPlan.class.getSimpleName();


    Context mContext;
    FragmentManager mFragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_career_plan, container, false);
        init(view);
        showFragment();

        return view;
    }





    // initializing here
    private void init(View view)
    {
        mContext = getContext();
    }



    // # showing fragment depending on role
    private void showFragment()
    {
       FragmentCareerPlanUsers fragment = new FragmentCareerPlanUsers();
       mFragmentManager = getChildFragmentManager();
       mFragmentManager.beginTransaction().add(R.id.fragment_career_plan_fragment_container,fragment, ConstHelper.Tag.Fragment.CAREER_PLAN_VIEW).commit();  // showing fragment to see career plan created by athlete
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK)
        {
            // updating newly added CareerUser in FragmentCareerUser
            if (requestCode == ConstHelper.RequestCode.ACTIVITY_CAREER_PLAN_CREATE_UPDATE)
            {
                CareerPlan.CareerUser careerUser = (CareerPlan.CareerUser) data.getSerializableExtra(ConstHelper.Key.CAREER_USER);
                FragmentCareerPlanUsers fragmentCareerPlanUsers = (FragmentCareerPlanUsers) getChildFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.CAREER_USERS_LIST);
                if (fragmentCareerPlanUsers != null)
                    fragmentCareerPlanUsers.updateCareerUserList(careerUser);
            }
        }

    }*/
}
