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
import org.ctdworld.propath.adapter.AdapterSubmitSurveyAdapter;
import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.presenter.PresenterGetAllSharedSurvey;

import java.util.ArrayList;
import java.util.List;

public abstract class ActivityViewSubmitSurvey extends AppCompatActivity implements ContractGetAllSharedSurvey.View {

   // private final String TAG = ActivitySubmitSurvey.class.getSimpleName();

    public static final String SURVEY_ID= "survey_id";

    Toolbar mToolbar;
    TextView mToolbarTitle;
    Context mContext;
    RecyclerView survey_questions_preview_recycler_view;
    String  get_survey_id;
    List<SurveyQusetions> questionsList = new ArrayList<>();
    TextView surveyDesc, surveyTitle, surveyType;
    AdapterSubmitSurveyAdapter adapter;

    ProgressBar mProgressBar;
    ContractGetAllSharedSurvey.Presenter mPresenter;



        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_submit_survey);

            init();
            setToolBar();
            setSurveyAdapter();
        }



    // for initialization
    private void init() {
        mContext = this;
        mPresenter = new PresenterGetAllSharedSurvey(mContext, this) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        survey_questions_preview_recycler_view = findViewById(R.id.survey_questions_preview_recycler_view);
        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);
        mProgressBar=findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle !=  null) {


            get_survey_id = mBundle.getString(SURVEY_ID);

        }
//        surveyTitle.setText(get_survey_title);
//        surveyType.setText(get_survey_type);
//        surveyDesc.setText(get_survey_desc_);
       mPresenter.getAllSubmittedSurvey(get_survey_id);

    }
    //for set adapter
    private void setSurveyAdapter() {


        adapter = new AdapterSubmitSurveyAdapter(mContext, questionsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        survey_questions_preview_recycler_view.setLayoutManager(layoutManager);
        survey_questions_preview_recycler_view.setAdapter(adapter);
    }

    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_preview);
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
    public void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions, List<SurveyQusetions> qusetionsList) {

        surveyTitle.setText(surveyQusetions.getSurveyTitle());
        surveyType.setText(surveyQusetions.getSurveyQusetionType());
        surveyDesc.setText(surveyQusetions.getSurveyDesc());

        questionsList = qusetionsList;

        adapter.updateList(questionsList);
    }

    @Override
    public void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList) {

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
