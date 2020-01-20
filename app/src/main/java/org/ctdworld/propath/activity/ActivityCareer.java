package org.ctdworld.propath.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.fragment.FragmentCareerEmploymentTools;
import org.ctdworld.propath.fragment.FragmentCareerPlan;

public class ActivityCareer extends AppCompatActivity
{

    // # Views
    private Toolbar mToolbar;  // toolbar
    private TextView mToolbarTitle; // toolbar title
    private ViewPager mViewPager; // view pager to show fragments
    private TabLayout mTabLayout;  // tab layout to show fragments


    // Variables
    private final String TAG = ActivityCareer.class.getSimpleName();  // tag
    private boolean mIsFragmentSingle = false;  // this contains if there is only one fragment or more than one


    // # Keys
    public static final String KEY_IS_FRAGMENT_SINGLE = "key is single";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career);

        init();
        setToolbar();  // setting toolbar
        setupWithViewPager();
    }



    // # initializing variables
    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTabLayout = findViewById(R.id.activity_career_tab_layout);
        mViewPager = findViewById(R.id.activity_career_view_pager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mIsFragmentSingle = bundle.getBoolean(KEY_IS_FRAGMENT_SINGLE);
    }





    // setting up viewpager and tab layout
    private void setupWithViewPager()
    {
        mTabLayout.setupWithViewPager(mViewPager);
        // veiw pager adapter
        AdapterFragmentPager mPagerAdapter = new AdapterFragmentPager(getSupportFragmentManager());

        // if there is only one fragment then tab layout will be hidden, and toolbar title will be changed
        if (mIsFragmentSingle)
        {
            mTabLayout.setVisibility(View.GONE);
            mPagerAdapter.addItem(new FragmentCareerPlan(), "");
            mToolbarTitle.setText("Career PlanData");
        }
        else  // showing more than 1 fragments
        {
            mPagerAdapter.addItem(new FragmentCareerEmploymentTools() , "Employment Tools");
            mPagerAdapter.addItem(new FragmentCareerPlan() , "Career Plan");
        }

        mViewPager.setAdapter(mPagerAdapter);


    }






    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Career");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
