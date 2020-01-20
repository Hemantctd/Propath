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
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterCoachSelfReviewList;
import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.presenter.PresenterCoachFeedback;

import java.util.List;

public abstract class ActivityCoachSelfReviewList extends AppCompatActivity implements ContractCoachFeedback.View {

    Toolbar mToolbar;
    TextView mToolbarTitle;
    private Context mContext;
    private RecyclerView mRecyclerView;
    String mAthleteID;
    TextView mNoDataFound;
    private ProgressBar mProgressBar;
    AdapterCoachSelfReviewList mAdapter;
    ContractCoachFeedback.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_self_review_list);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            mAthleteID = mBundle.getString("athlete_id");
        }

        init();
        prepareRecyclerView();
        setToolbar();

        onShowProgress();
        mPresenter.getCoachSelfReview(mAthleteID);
    }


    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recyclerCoachSelfReview);
        mNoDataFound = findViewById(R.id.no_data_found);
    }


    private void prepareRecyclerView()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterCoachSelfReviewList(mContext,null);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onCoachFeedbackDeleted(String id) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachDataList) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.setData(coachDataList);
    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

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

    }


    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.coach_feedback_title);
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
}
