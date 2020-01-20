package org.ctdworld.propath.activity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.fragment.FragmentMealPlanner;
import org.ctdworld.propath.fragment.FragmentNutrition;
import org.ctdworld.propath.fragment.FragmentStatistics;
import org.ctdworld.propath.helper.DialogHelper;

public class ActivityNutrition extends BaseActivity {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;
    //  public static final String KEY_IS_FRAGMENT_SINGLE = "key is single";
    // private boolean mIsFragmentSingle = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        setToolbar();  // setting toolbar

        //  Bundle bundle = getIntent().getExtras();
      /*  if (bundle != null)
        {
            mIsFragmentSingle = bundle.getBoolean(KEY_IS_FRAGMENT_SINGLE);
        }*/

        TabLayout tabLayout = findViewById(R.id.activity_nutrition_tab_layout);
        ViewPager viewPager = findViewById(R.id.activity_nutrition_view_pager);
        tabLayout.setupWithViewPager(viewPager);
        AdapterFragmentPager pagerAdapter = new AdapterFragmentPager(getSupportFragmentManager());

        // if there is only one fragment then tab layout will be hidden, and toolbar title will be changed
       /* if (mIsFragmentSingle)
        {
          *//*  tabLayout.setVisibility(View.GONE);
            pagerAdapter.addItem(new FragmentEducationSchoolReviewChooseAthlete(), "");
            mToolbarTitle.setText("Progress Report");*//*
        }
        else
        {*/
        pagerAdapter.addItem(new FragmentMealPlanner(), "Meal Planner");
        pagerAdapter.addItem(new FragmentNutrition(), "Food Diary");
        pagerAdapter.addItem(new FragmentStatistics(), "Statistics");
        /*}*/

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // tabLayout.setOnTabSelectedListener(onTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    DialogHelper.showSimpleCustomDialog(mContext, "Coming Soon...");
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
//                if (state == 1)
//                {
//                    DialogHelper.showSimpleCustomDialog(mContext,"Coming Soon...");
//                }
            }
        });

    }


    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.nutrition);

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
