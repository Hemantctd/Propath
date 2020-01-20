package org.ctdworld.propath.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchAnalysisScore;

public class ActivityMatchAnalysisScore extends AppCompatActivity implements View.OnClickListener{

    RecyclerView mRecyclerView;
    Button mBtnSave, mBtnUndo;
    Toolbar mToolbar;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analysis_score);
        init();
        prepareRecyclerView();
        setToolBar();
    }

    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerView = findViewById(R.id.interval_min_recycler_min);
        mBtnSave = findViewById(R.id.btn_save);
        mBtnUndo = findViewById(R.id.btn_undo);
        mBtnSave.setOnClickListener(this);
        mBtnUndo.setOnClickListener(this);

    }

    private void prepareRecyclerView()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchAnalysisScore adapterMatchAnalysisScore = new AdapterMatchAnalysisScore(getApplicationContext());
        mRecyclerView.setAdapter(adapterMatchAnalysisScore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.btn_save :

                startActivity(new Intent(getApplicationContext(),ActivityMatchAnalysisIntervalSummary.class));

                break;

            case R.id.btn_undo :

                break;
        }
    }

    private void setToolBar()
    {
        setSupportActionBar(mToolbar);
        toolbarTitle.setText("Score");
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

}
