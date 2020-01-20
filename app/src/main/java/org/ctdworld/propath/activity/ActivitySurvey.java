package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyFragmentPager;
import org.ctdworld.propath.fragment.FragmentSurveyCreate;
import org.ctdworld.propath.fragment.FragmentSurveyReceived;

public class ActivitySurvey extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mImgToolbarOptionsMenu;
    private TextView mToolbarTitle;
    Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        init();
        setToolbar();
        setViewPagerAdapter();
    }


    // for initialization
    private void init() {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);
        mImgToolbarOptionsMenu.setOnClickListener(this);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

    }


    private void setViewPagerAdapter() {
        // setting tab and viewpager adapter
        tabLayout.setupWithViewPager(viewPager);

        AdapterSurveyFragmentPager fragmentPager = new AdapterSurveyFragmentPager(getSupportFragmentManager());
        fragmentPager.addItem(new FragmentSurveyReceived(), "Received");
        fragmentPager.addItem(new FragmentSurveyCreate(), "Created");

        viewPager.setAdapter(fragmentPager);

    }


    // setting toolbar
    private void setToolbar() {
        mImgToolbarOptionsMenu.setVisibility(View.GONE);
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }


    // button listeners
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.toolbar_img_options_menu) {
            startActivity(new Intent(ActivitySurvey.this, ActivitySurveySettings.class));  // to create new group
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
