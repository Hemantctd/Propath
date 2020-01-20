package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractEnableDisableSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.presenter.PresenterEnableDisableSurvey;

public abstract class ActivitySurveySettings extends AppCompatActivity implements View.OnClickListener , ContractEnableDisableSurvey.View {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    Button settingsDone;
    CheckBox enableDisableSurvey,showAndHideNames;
    String status="",show_anonymous="";
    ContractEnableDisableSurvey.Presenter mPresenter;
    Context mContext;
    String survey_id,getSurvey_enable_type,getSurvey_anonymous_type;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_settings);
        init();
        setToolBar();

    }
    private void init()
    {
        mContext = this;
        mProgressBar = findViewById(R.id.progress_bar);
        mPresenter = new PresenterEnableDisableSurvey(mContext, this) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        settingsDone = findViewById(R.id.settingsDone);
        enableDisableSurvey = findViewById(R.id.enable_disable_survey);
        showAndHideNames = findViewById(R.id.hideAndShowName);
        showAndHideNames.setOnClickListener(this);
        enableDisableSurvey.setOnClickListener(this);
        settingsDone.setOnClickListener(this);


        Intent i = getIntent();
        survey_id = i.getStringExtra("survey_id");
        getSurvey_enable_type = i.getStringExtra("survey_enable_disable");
        getSurvey_anonymous_type = i.getStringExtra("survey_anonymous");
        Log.d("enable_disable" ,getSurvey_enable_type);
        Log.d("show_anonymous" ,getSurvey_anonymous_type);

        if (getSurvey_enable_type.equals("0"))
        {
                enableDisableSurvey.setChecked(true);
                Log.d("enable_disable 0", getSurvey_enable_type);
                status = "0";


        }
        else if (getSurvey_enable_type.equals("1"))
        {

            enableDisableSurvey.setChecked(false);
            Log.d("enable_disable 1", getSurvey_enable_type);
            status = "1";


        }
        if (getSurvey_anonymous_type.equals("1"))
        {
                showAndHideNames.setChecked(false);
                Log.d("show names 1", getSurvey_anonymous_type);
                show_anonymous = "1";
        }
        else if (getSurvey_anonymous_type.equals("0"))
        {
                showAndHideNames.setChecked(true);
                Log.d("show names 0", getSurvey_anonymous_type);
                show_anonymous = "0";

        }


    }

    private void setToolBar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.settingsDone :
                surveyDone();
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.enable_disable_survey:
                enableDisableCheck();
                break;
            case R.id.hideAndShowName :
                showAnonymous();
                break;
        }
    }



    public void enableDisableCheck()
    {
        if (enableDisableSurvey.isChecked())
        {
            status = "0";
        }
        else
        {
            status = "1";
        }

    }

    public void showAnonymous()
    {

        if (showAndHideNames.isChecked())
        {
            show_anonymous = "0";
        }
        else
        {
            show_anonymous = "1";
        }
    }

    public void surveyDone()
    {
        mPresenter.enabledisableSurvey(status,show_anonymous,survey_id);

    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);

    }

    @Override
    public void onShowProgress() {
      mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);

    }
}

