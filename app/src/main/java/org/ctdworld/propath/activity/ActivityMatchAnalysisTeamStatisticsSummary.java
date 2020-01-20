package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.helper.ConstHelper;

public class ActivityMatchAnalysisTeamStatisticsSummary extends AppCompatActivity implements View.OnClickListener{

    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    Button mViewByInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_analysis_team_statistics_summary);
        init();
        setToolbar();


    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mViewByInterval = findViewById(R.id.btn_view_by_interval);
        mViewByInterval.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_view_by_interval :


                final DialogEditText dialogEditText = DialogEditText.getInstance("", "Enter Interval Number", "Save", false);
                dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                    @Override
                    public void onButtonClicked(String enteredValue) {

                        dialogEditText.dismiss();

                    }
                });
                dialogEditText.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);


                break;

        }

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
