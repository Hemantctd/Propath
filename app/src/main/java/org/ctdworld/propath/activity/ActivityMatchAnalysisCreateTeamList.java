package org.ctdworld.propath.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class ActivityMatchAnalysisCreateTeamList extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    TextView toolbarTitle;
    Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analysis_create_team_list);
        init();
        setToolBar();

    }

    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_txt_title);
        mBtnSave = findViewById(R.id.btn_save);
        mBtnSave.setOnClickListener(this);

    }

    private void setToolBar()
    {
        setSupportActionBar(mToolbar);
        toolbarTitle.setText("Match Set Up");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_save :
                startActivity(new Intent(getApplicationContext(),ActivityMatchAnalysisScore.class));
                break;
        }

    }
}
