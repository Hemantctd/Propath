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
import org.ctdworld.propath.fragment.FragmentEducaSchoolReviewChooseAthlete;
import org.ctdworld.propath.fragment.FragmentEducationAthleteReviewChooseAthlete;
import org.ctdworld.propath.fragment.FragmentEducationSchoolReviewChooseAthlete;

public class ActivityEducationForTeacher extends AppCompatActivity
{

    Toolbar mToolbar;
    TextView mToolbarTitle;

    public static final int FRAGMENT_SCHOOL_REVIEW = 0;
    public static final int FRAGMENT_PROGRESS_REPORT = 1;
    public static final int FRAGMENT_ATHLETE_REVIEW = 2;

    public static final String KEY_IS_FRAGMENT_SINGLE = "key is single";
    private boolean mIsFragmentSingle = false;

    private ViewPager mViewPager;
    private AdapterFragmentPager mPagerAdapter;
    private String mSearchedText;
            
            
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_for_teacher);


        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        setToolbar();  // setting toolbar

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mIsFragmentSingle = bundle.getBoolean(KEY_IS_FRAGMENT_SINGLE);

        TabLayout tabLayout = findViewById(R.id.activity_education_tab_layout);
        mViewPager = findViewById(R.id.activity_education_view_pager);
        tabLayout.setupWithViewPager(mViewPager);
        mPagerAdapter = new AdapterFragmentPager(getSupportFragmentManager());

        // if there is only one fragment then tab layout will be hidden, and toolbar title will be changed
        if (mIsFragmentSingle)
        {
            tabLayout.setVisibility(View.GONE);
            mPagerAdapter.addItem(new FragmentEducationSchoolReviewChooseAthlete() {
                @Override
                public void onSuccess() {

                }
            }, "");
            mToolbarTitle.setText(R.string.progress_report);
        }
        else
        {
            mPagerAdapter.addItem(new FragmentEducaSchoolReviewChooseAthlete() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed() {

                }
            }, "School Review");
            mPagerAdapter.addItem(new FragmentEducationSchoolReviewChooseAthlete() {
                @Override
                public void onSuccess() {

                }
            }, "Progress Report");
            mPagerAdapter.addItem(new FragmentEducationAthleteReviewChooseAthlete() {
                @Override
                public void onSuccess() {

                }
            }, "Athlete Review");
        }
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.education);
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
