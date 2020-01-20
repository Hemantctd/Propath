package org.ctdworld.propath.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.fragment.FragmentCoachAthleteListSelfReviewed;
import org.ctdworld.propath.fragment.FragmentCoachFeedbackChooseAthlete;
import org.ctdworld.propath.fragment.FragmentCoachSelfReview;
import org.ctdworld.propath.fragment.FragmentCoachViewFeedback;
import org.ctdworld.propath.fragment.FragmentReviewedCoachFeedbackChooseAthlete;


public class ActivityCoachFeedback extends AppCompatActivity
{

    Toolbar mToolbar;
    TextView mToolbarTitle;

    public static final String KEY_IS_FRAGMENT_SINGLE = "key is single";
    private boolean mIsFragmentSingle = false;


    public static final String KEY_FRAGMENT_TYPE = "fragment type";
    public static final int FRAGMENT_COACH_VIEW_FEEDBACK = 1;
    public static final int FRAGMENT_COACH_VIEW_FEEDBACK_CHOOSE_ATHLETE = 2;
    public static final int FRAGMENT__FOR_OTHER_ROLE = 3;  // IT WILL BE PASSED FROM DASHBOARD HELPER
    //public static final int FRAGMENT_COACH_SELF_REVIEW_FEEDBACK = 4;

    int mFragmentType = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_feedback);


        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        setToolbar();  // setting toolbar

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mIsFragmentSingle = bundle.getBoolean(KEY_IS_FRAGMENT_SINGLE);
            mFragmentType = bundle.getInt(KEY_FRAGMENT_TYPE);
        }

        TabLayout tabLayout = findViewById(R.id.activity_coach_feedback_tab_layout);
        ViewPager viewPager = findViewById(R.id.activity_coach_feedback_view_pager);
        tabLayout.setupWithViewPager(viewPager);
        AdapterFragmentPager pagerAdapter = new AdapterFragmentPager(getSupportFragmentManager());

        // if there is only one fragment then tab layout will be hidden, and toolbar title will be changed
        if (mIsFragmentSingle)
        {
            tabLayout.setVisibility(View.GONE);
            if (mFragmentType == FRAGMENT_COACH_VIEW_FEEDBACK_CHOOSE_ATHLETE) {
                pagerAdapter.addItem(new FragmentReviewedCoachFeedbackChooseAthlete() {
                    @Override
                    public void onSuccess() {

                    }
                }, "");
                mToolbarTitle.setText(R.string.coach_feedback_title);

            }
            else if (mFragmentType == FRAGMENT__FOR_OTHER_ROLE) {
                pagerAdapter.addItem(new FragmentReviewedCoachFeedbackChooseAthlete() {
                    @Override
                    public void onSuccess() {

                    }
                }, "View Feedback");
                mToolbarTitle.setText(R.string.view_coach_feedback_title);

            }
            else if (mFragmentType == FRAGMENT_COACH_VIEW_FEEDBACK)
            {
                pagerAdapter.addItem(new FragmentCoachViewFeedback() {
                    @Override
                    public void onSuccess() {

                    }
                }, "");
                mToolbarTitle.setText(R.string.view_coach_feedback_title);
            }


        }
        else
        {
            if (mFragmentType == FRAGMENT_COACH_VIEW_FEEDBACK)
            {
                pagerAdapter.addItem(new FragmentCoachSelfReview() {
                    @Override
                    public void onSuccess() {

                    }
                }, "Self Review");
                pagerAdapter.addItem(new FragmentCoachViewFeedback() {
                    @Override
                    public void onSuccess() {

                    }
                }, "Match Feedback");
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                mToolbarTitle.setText(R.string.coach_feedback_title);
            }
            else if(mFragmentType == FRAGMENT__FOR_OTHER_ROLE)
            {
                pagerAdapter.addItem(new FragmentCoachAthleteListSelfReviewed() {
                    @Override
                    public void onSuccess() {

                    }
                },"Athlete Review" );
                pagerAdapter.addItem(new FragmentReviewedCoachFeedbackChooseAthlete() {
                    @Override
                    public void onSuccess() {

                    }
                }, "View Feedback");
                tabLayout.setTabMode(TabLayout.MODE_FIXED);
                mToolbarTitle.setText(R.string.view_coach_feedback_title);
            }
            else {
                pagerAdapter.addItem(new FragmentCoachFeedbackChooseAthlete() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed() {

                    }
                }, "Match Feedback");
                pagerAdapter.addItem(new FragmentReviewedCoachFeedbackChooseAthlete() {
                    @Override
                    public void onSuccess() {

                    }
                }, "View Feedback");
                pagerAdapter.addItem(new FragmentCoachAthleteListSelfReviewed() {
                    @Override
                    public void onSuccess() {

                    }
                },"Athlete Review" );
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                mToolbarTitle.setText(R.string.coach_feedback_title);
            }

        }

        viewPager.setAdapter(pagerAdapter);



    }



    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.coach_feedback_title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
