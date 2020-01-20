package org.ctdworld.propath.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterEducationForAthlete;
import org.ctdworld.propath.fragment.FragmentEducationSchoolReviewChooseAthlete;

import org.ctdworld.propath.fragment.FragmentEducationAthleteReviewChooseAthlete;

public class ActivityEducationForOtherFourRoles extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_for_other_four_roles);
        init();
        viewPagerAdapter();
        setToolbar();
    }

    public void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        TabLayout tabLayout = findViewById(R.id.activity_education_tab_layout);
        viewPager = findViewById(R.id.activity_education_view_pager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void viewPagerAdapter()
    {
        AdapterEducationForAthlete pagerAdapter = new AdapterEducationForAthlete(getSupportFragmentManager());
        pagerAdapter.addItem(new FragmentEducationSchoolReviewChooseAthlete() {
            @Override
            public void onSuccess() {

            }
        }, "School Review");
        pagerAdapter.addItem(new FragmentEducationAthleteReviewChooseAthlete() {
            @Override
            public void onSuccess() {

            }
        }, "Athlete Review");
        viewPager.setAdapter(pagerAdapter);
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
