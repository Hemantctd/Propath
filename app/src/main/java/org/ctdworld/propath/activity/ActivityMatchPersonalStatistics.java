package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class ActivityMatchPersonalStatistics extends AppCompatActivity {

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    LinearLayout mTeamLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_personal_statistics);
        init();
        setToolbar();
    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mTeamLayout = findViewById(R.id.layout_team);

        mTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mContext,ActivityMatchAnalysisTeamStatisticsSummary.class));

            }
        });
    }


    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText("Personal Statistics");
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
