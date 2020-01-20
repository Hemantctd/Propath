package org.ctdworld.propath.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterEducationForAthlete;
import org.ctdworld.propath.fragment.FragmentEducationProgressReportGivenByTeacher;
import org.ctdworld.propath.fragment.FragmentEducationSelfReviewList;


public class ActivityEducationForAthlete extends AppCompatActivity {

    private static final String TAG = ActivityEducationForAthlete.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdapterEducationForAthlete mPagerAdapter;
   /* ImageView mToolbarImgOptionsMenu;
    ImageView mToolbarImgDone;*/

    public static final String KEY_ATHLETE_ID = "athlete_id";

    String athlete_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_for_athlete);
        init();
        viewPagerAdapter();
        setToolbar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

            athlete_id = bundle.getString(KEY_ATHLETE_ID);

            Log.d(TAG,"athlete_id........." + athlete_id);
        }
    }

    public void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        tabLayout = findViewById(R.id.activity_education_tab_layout);
        viewPager = findViewById(R.id.activity_education_view_pager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void viewPagerAdapter()
    {
        mPagerAdapter = new AdapterEducationForAthlete(getSupportFragmentManager());
        mPagerAdapter.addItem(new FragmentEducationSelfReviewList(), "Self Review");
        mPagerAdapter.addItem(new FragmentEducationProgressReportGivenByTeacher() , "Progress Report");
        viewPager.setAdapter(mPagerAdapter);
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

    public String getAthleteId() {

        return athlete_id ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
