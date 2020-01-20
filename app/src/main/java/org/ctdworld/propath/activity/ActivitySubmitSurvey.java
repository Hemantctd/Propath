package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSubmitSurvey;
import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.presenter.PresenterGetAllSharedSurvey;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubmitSurvey extends AppCompatActivity implements View.OnClickListener, ContractGetAllSharedSurvey.View {
    //private final String TAG = ActivitySubmitSurvey.class.getSimpleName();

    public static final String SURVEY_ID = "survey_id";
    //
    public static final String SURVEY_TITLE = "survey_title";
    public static final String SURVEY_QUESTION_TYPE = "qusetion_type";
    public static final String SURVEY_DESCRIPTION = "survey_desc";
    public static final String SURVEY_NO_OF_QUESTIONS = "survey_no_of_questions";
    public static final String SURVEY_QUESTION_LIST = "survey_qusetions_list";

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private Button btnSurveySave;
    private Context mContext;
    private RecyclerView survey_questions_preview_recycler_view;
    private String get_survey_title, get_survey_no_of_questions, get_survey_desc_, get_survey_type, get_survey_id;
    private ArrayList<SurveyQusetions> questionsList = new ArrayList<>();
    private TextView surveyDesc, surveyTitle, surveyType;
    private AdapterSubmitSurvey adapter;

    private ArrayList<SurveyQusetions> answerList = new ArrayList<>();
    private ContractGetAllSharedSurvey.Presenter mPresenter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_survey);

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
        btnSurveySave = findViewById(R.id.btn_survey_save);
        survey_questions_preview_recycler_view = findViewById(R.id.survey_questions_preview_recycler_view);
        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);
        mProgressBar = findViewById(R.id.progress_bar);

        btnSurveySave.setOnClickListener(this);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {

            get_survey_title = mBundle.getString(SURVEY_TITLE);
            get_survey_type = mBundle.getString(SURVEY_QUESTION_TYPE);
            get_survey_desc_ = mBundle.getString(SURVEY_DESCRIPTION);
            get_survey_no_of_questions = mBundle.getString(SURVEY_NO_OF_QUESTIONS);
            get_survey_id = mBundle.getString(SURVEY_ID);

            questionsList = mBundle.getParcelableArrayList(SURVEY_QUESTION_LIST);


        }
        surveyTitle.setText(get_survey_title);
        surveyType.setText(get_survey_type);
        surveyDesc.setText(get_survey_desc_);


//        for (int i=0 ; i<questionsList.size() ; i++)
//        {
//            SurveyQusetions surveyQusetions = new SurveyQusetions();
//            String questions = surveyQusetions.getQuestions();
//            Log.d(TAG,"QUESTIONS : " +questions);
//
//        }
//        Log.i(TAG,"list.size() = "+list.size());
//        for (int i = 0 ; i < list.size() ; i ++) {
//            surveyCommon = list.get(i);
//            Log.i(TAG, "questions = " + surveyCommon.getQuestions());
//
//        }

        //   Log.i(TAG, "title = " + surveyCommon.getQuestions());

    }

    //for set adapter
    private void setSurveyAdapter() {

        adapter = new AdapterSubmitSurvey(mContext, get_survey_no_of_questions, questionsList, answerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        survey_questions_preview_recycler_view.setLayoutManager(layoutManager);
        survey_questions_preview_recycler_view.setAdapter(adapter);
    }

    // button clickable
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_survey_save) {
            saveSurveyData();
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }


    // to save survey
    public void saveSurveyData() {

        List<SurveyQusetions> list = adapter.getSurveyAnswers();
//        for (int i =0 ; i<list.size();i ++)
//        {
//            SurveyQusetions surveyQusetions = list.get(i);
//            Log.d(TAG,"survey_answers : " +surveyQusetions.getAnswers());
//        }
//        SurveyQusetions surveyQusetions = new SurveyQusetions();
//        surveyQusetions.setSurveyTitle(get_survey_title);
//        surveyQusetions.setSurveyDesc(get_survey_desc_);
//        surveyQusetions.setSurveyQusetionType(get_survey_type);
//        surveyQusetions.setSurvey_no_of_question(get_survey_no_of_questions);

        mPresenter.submitSurvey(get_survey_id, questionsList, list);
        //mPresenter.saveSurvey(list,surveyQusetions);
    }

    // set tool bar
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.survey_submit);

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

    }

    @Override
    public void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList) {

    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg);
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg, new DialogHelper.ShowSimpleDialogListener() {
            @Override
            public void onOkClicked() {
                mContext.startActivity(new Intent(mContext, ActivityGetAllSharedSurvey.class));
            }
        });

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
    public void onSuccess() { mProgressBar.setVisibility(View.GONE);


    }

    @Override
    public void onFailed() {
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
        DialogHelper.showSimpleCustomDialog(mContext, message);

    }
}

