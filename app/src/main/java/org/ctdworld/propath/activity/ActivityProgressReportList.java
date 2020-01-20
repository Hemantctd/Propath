package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterProgressReportList;
import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.presenter.PresenterSchoolReview;
import java.util.List;

public class ActivityProgressReportList extends AppCompatActivity implements ContractSchoolReview.View
{
    private final String TAG = ActivityProgressReportList.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView recycler_progress_list;
    Context mContext;
    String athlete_id;
    String date;
    ProgressBar mProgressBar;
    AdapterProgressReportList mAdapter;
    ContractSchoolReview.Presenter mPresenter;
    List<SchoolReview> mSchoolReviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report_list);
        init();
        setProgressListAdapter();
        setToolBar();

        Intent i = getIntent();
        athlete_id = i.getStringExtra("athlete_id");
        Log.d(TAG,"athlete_id : " + athlete_id);

        onShowProgress();
        mPresenter.getSchoolReview(athlete_id);
    }

    private void init()
    {
        mContext = this;
        mPresenter = new PresenterSchoolReview(mContext,this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);


        recycler_progress_list = findViewById(R.id.recycler_progress_list);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);





    }

    public void setProgressListAdapter()
    {

        mAdapter = new AdapterProgressReportList(mContext, adapterListener);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recycler_progress_list.setLayoutManager(mLayoutManager);
        recycler_progress_list.setAdapter(mAdapter);




    }

    private void setToolBar()
    {

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.progress_report);
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

    AdapterProgressReportList.Listener adapterListener = new AdapterProgressReportList.Listener() {
        @Override
        public void onProgressReportOptionClicked(final String reviewId, final String athleteId) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                        .addOption(BottomSheetOption.OPTION_DELETE, "Delete");


                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());

                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {

                        switch (option) {
                            case BottomSheetOption.OPTION_DELETE:
                                mPresenter.deleteReview(reviewId);
                                break;

                            case BottomSheetOption.OPTION_EDIT:
                                Intent intent = new Intent(mContext, ActivityEducationSchoolReviewCreate.class);
                                intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_ATHLETE_ID, athleteId);
                                intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_REVIEW_ID, reviewId);
                                intent.putExtra(ActivityEducationSchoolReviewCreate.KEY_MODE_CREATE_OR_EDIT, ActivityEducationSchoolReviewCreate.VALUE_MODE_EDIT);
                                startActivity(intent);
                                break;
                        }

                    }
                });
                options.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };



    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onProgressListDeleted(String id) {
        onHideProgress();
            if (mAdapter != null)
                mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {
        onHideProgress();
        this.mSchoolReviewList = schoolReviewList;

        if (mAdapter != null)
            mAdapter.setData(schoolReviewList,athlete_id);

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

}
