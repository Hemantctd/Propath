package org.ctdworld.propath.activity;

import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterGetAllSharedSurvey;
import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.presenter.PresenterGetAllSharedSurvey;

import java.util.List;

public abstract class ActivityGetAllSharedSurvey extends AppCompatActivity implements ContractGetAllSharedSurvey.View
{
    Toolbar mToolbar;
    ImageView mImgToolbarOptionsMenu,toolbar_img_options_menu;
    TextView mToolbarTitle;
    Context mContext;
   /* List<SurveyQusetions> surveyList = new ArrayList<>();
    List<SurveyQusetions> questionsList = new ArrayList<>();*/
    AdapterGetAllSharedSurvey adapterSharedSurvey;
   // SurveyQusetions surveyQusetions =  new SurveyQusetions();
    ContractGetAllSharedSurvey.Presenter mPresenter;
    ProgressBar mProgressBar;
    RecyclerView recycler_get_shared_survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_get_all_shared_survey);
        init();
        setToolBar();
        setAdapter();
        mPresenter.getAllSharedSurvey();

    }

    public void init()
    {
        mContext = this;
        mPresenter= new PresenterGetAllSharedSurvey(mContext, this) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mToolbar = findViewById(R.id.toolbar);
        recycler_get_shared_survey = findViewById(R.id.recycler_get_shared_survey);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mImgToolbarOptionsMenu = mToolbar.findViewById(R.id.toolbar_img_options_menu);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    // setting toolbar
    private void setToolBar() {
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


    public void setAdapter()
    {
        adapterSharedSurvey = new AdapterGetAllSharedSurvey(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_get_shared_survey.setLayoutManager(layoutManager);
        recycler_get_shared_survey.setAdapter(adapterSharedSurvey);
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
    public void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions, List<SurveyQusetions> qusetionsList) {

    }

    @Override
    public void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList) {

       // surveyList = qusetionsList;
        adapterSharedSurvey.updateSurvey(surveyQusetions,qusetionsList);
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
