package org.ctdworld.propath.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.fragment.FragmentTrainingPlanList;
import org.ctdworld.propath.fragment.FragmentTrainingPlanResult;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;

public class ActivityTrainingPlan extends AppCompatActivity
{
    private static final String TAG = ActivityTrainingPlan.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
   /* ImageView mToolbarImgDelete;
    ImageView mToolbarImgEdit;
    ImageView mToolbarImgShare;*/

    public static final String KEY_IS_FRAGMENT_SINGLE = "key is single";
    public static final int MENU_ID_DELETE = 1;
    private boolean mIsFragmentSingle = false; // to check if only one fragment is to be shown

    ViewPager mViewPager;
    AdapterFragmentPager mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan);


        init();
        setToolbar();  // setting toolbar
        setUpViewPagerAndTabs();  // setting up tab layout and view page
    }



    // # initialing variables
    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mIsFragmentSingle = bundle.getBoolean(KEY_IS_FRAGMENT_SINGLE);
        }
    }




    // # setting up view pager and adapter
    private void setUpViewPagerAndTabs() {

        TabLayout tabLayout = findViewById(R.id.activity_training_plan_tab_layout);
        mViewPager = findViewById(R.id.activity_training_plan_view_pager);
        tabLayout.setupWithViewPager(mViewPager);
        mPagerAdapter = new AdapterFragmentPager(getSupportFragmentManager());

        // if there is only one fragment then tab layout will be hidden, and toolbar title will be changed
        if (mIsFragmentSingle)
        {
           /* tabLayout.setVisibility(View.GONE);
            pagerAdapter.addItem(new FragmentEducationSchoolReviewChooseAthlete(), "");
            mToolbarTitle.setText("Progress Report");*/
        }
        else
        {
            mPagerAdapter.addItem(FragmentTrainingPlanList.getInstance(ConstHelper.Type.RECEIVED), "Received");
            mPagerAdapter.addItem(FragmentTrainingPlanList.getInstance(ConstHelper.Type.CREATED), "Created");
            mPagerAdapter.addItem(FragmentTrainingPlanResult.getInstance(), getString(R.string.result));
        }

        mViewPager.setAdapter(mPagerAdapter);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // tabLayout.setOnTabSelectedListener(onTabSelectedListener(viewPager));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if (position == 2)
                {
                    DialogHelper.showSimpleCustomDialog(ActivityTrainingPlan.this,"Coming Soon...");
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





    // setting up toolbar
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(getString(R.string.training_plan));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
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

        else if (item.getItemId() == MENU_ID_DELETE)
        {
            FragmentTrainingPlanList fragmentTrainingPlanList = (FragmentTrainingPlanList) mPagerAdapter.getItem(mViewPager.getCurrentItem());
            if (fragmentTrainingPlanList != null)
                fragmentTrainingPlanList.onDeleteOptionClicked();
            else
                Log.e(TAG, "fragmentTrainingPlanList is null in onOptionsItemSelected() method");
        }

        return super.onOptionsItemSelected(item);
    }
}
