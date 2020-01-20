package org.ctdworld.propath.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterMatchAnalysisIntervalSummary;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.helper.ConstHelper;

public class ActivityMatchAnalysisIntervalSummary extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    TextView toolbarTitle;
    TextView mBtnSaveMatchInterval;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analysis_interval_summary);
        init();
        showPopUp();
        setToolbar();
        preapareRecyclerView();
    }

    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerView = findViewById(R.id.recycler_match_analysis_interval_summary);
        mBtnSaveMatchInterval = findViewById(R.id.btn_save_match_interval);
        mBtnSaveMatchInterval.setOnClickListener(this);
    }

    private void showPopUp()
    {
        final DialogEditText dialogEditText = DialogEditText.getInstance("", "Enter Interval Number", "Save", false);
        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
            @Override
            public void onButtonClicked(String enteredValue) {

                dialogEditText.dismiss();

            }
        });
        dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_match_interval:

                startActivity(new Intent(getApplicationContext(),ActivityMatchStatistics.class));
                break;
        }

    }


    public void preapareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        AdapterMatchAnalysisIntervalSummary adapterMatchAnalysisIntervalSummary = new AdapterMatchAnalysisIntervalSummary(getApplicationContext());
        mRecyclerView.setAdapter(adapterMatchAnalysisIntervalSummary);

    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        toolbarTitle.setText("Interval Summary");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }


}
