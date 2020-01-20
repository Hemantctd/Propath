package org.ctdworld.propath.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterFragmentPager;
import org.ctdworld.propath.fragment.FragmentMatchDayFuture;
import org.ctdworld.propath.fragment.FragmentMatchDayPast;


public class ActivityMatchDay extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_day);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_txt_title);

        // setting toolbar
        setSupportActionBar(toolbar);
        toolbarTitle.setText(getString(R.string.dash_match_day));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }


        // setting tab and viewpager adapter
        tabLayout.setupWithViewPager(viewPager);

        AdapterFragmentPager fragmentPager = new AdapterFragmentPager(getSupportFragmentManager());
        fragmentPager.addItem(new FragmentMatchDayPast(), "Received");
        fragmentPager.addItem(new FragmentMatchDayFuture(), "Create");

        viewPager.setAdapter(fragmentPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }
}
