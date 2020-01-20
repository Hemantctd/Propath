package org.ctdworld.propath.activity;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.fragment.FragmentMatchIndividualStatistics;
import org.ctdworld.propath.fragment.FragmentMatchVideoAnalysis;

public class ActivityMatchAnalysisSetUpCreate extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_set_up_create);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_txt_title);

        // setting toolbar
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Match Set Up");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }


        // setting tab and viewpager adapter
        tabLayout.setupWithViewPager(viewPager);

        AdapterFragmentPager fragmentPager = new AdapterFragmentPager(getSupportFragmentManager());
        fragmentPager.addItem(new FragmentMatchVideoAnalysis(), getString(R.string.match_video_analysis));
        fragmentPager.addItem(new FragmentMatchIndividualStatistics(), getString(R.string.match_individual_statistics));

        viewPager.setAdapter(fragmentPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;

    }
}
