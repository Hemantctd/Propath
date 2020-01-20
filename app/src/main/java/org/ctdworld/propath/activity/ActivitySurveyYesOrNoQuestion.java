package org.ctdworld.propath.activity;

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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterYesOrNoSurveyQuestions;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class ActivitySurveyYesOrNoQuestion extends AppCompatActivity implements View.OnClickListener{
    //private final String TAG = ActivityCreateSurvey.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView mRecyclerView;
    Button mNextBtn;
    Survey.SurveyData.YesNo surveyYesNo ;
    AdapterYesOrNoSurveyQuestions adapterSurveyYesNoQuestions;
    ImageView mAddQuestions;
    List<Survey.SurveyData.YesNo.Questions> mSurveyQuestionList;
    String mSurveyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_yes_or_no);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mSurveyType = bundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyYesNo = (Survey.SurveyData.YesNo) bundle.getSerializable(ActivityCreateSurvey.SURVEY_YES_NO);
            if (surveyYesNo != null) {
                mSurveyQuestionList = surveyYesNo.getSurveyYesNoQuestionList();
            }
        }


        init();
        setToolbar();  // setting toolbar
        prepareRecyclerView();


        if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)|| mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
        {
                adapterSurveyYesNoQuestions.addQuestionList(mSurveyQuestionList);
        }
        else
        {
            int noOfQuestions = Integer.parseInt(surveyYesNo.getQuestionNo());
            for (int i = 0; i < noOfQuestions; i++) {
                adapterSurveyYesNoQuestions.addQuestion(new Survey.SurveyData.YesNo.Questions());
            }
        }

    }

    private void init()
    {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mNextBtn = findViewById(R.id.next_btn);
        mRecyclerView = findViewById(R.id.recycler_yes_or_no_questions);
        mAddQuestions = findViewById(R.id.add_questions);
        mAddQuestions.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

       if (Integer.parseInt(surveyYesNo.getQuestionNo()) == 20)
       {
           mAddQuestions.setVisibility(View.GONE);
       }
    }


    private void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterSurveyYesNoQuestions = new AdapterYesOrNoSurveyQuestions(getApplicationContext());
        mRecyclerView.setAdapter(adapterSurveyYesNoQuestions);

      //  int noOfQuestions = Integer.parseInt(surveyYesNo.getSurveyNoOfQuestion());



      /*  for (int i=0; i<noOfQuestions;i++)
        {
            adapterSurveyYesNoQuestions.addQuestion(new SurveyYesNo.SurveyQuestions());

        }*/
    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.yes_no);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.add_questions :
                adapterSurveyYesNoQuestions.addQuestion(new Survey.SurveyData.YesNo.Questions());
                if(adapterSurveyYesNoQuestions.getItemCount() == 20)
                {
                    mAddQuestions.setVisibility(View.GONE);
                }


                break;

            case R.id.next_btn :
                List<Survey.SurveyData.YesNo.Questions> list =  adapterSurveyYesNoQuestions.getSurveyQuestions();

                surveyYesNo.setSurveyYesNoQuestionList(list);
                if (list == null)
                {
                    DialogHelper.showSimpleCustomDialog(this,"Please fill questions ");
                }
                else {
                    if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyYesOrNoWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                        startActivity(surveyDataSend);
                        finish();
                    }
                    else if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyYesOrNoWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        startActivity(surveyDataSend);
                        finish();
                    }
                    else {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyYesOrNoWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                        startActivity(surveyDataSend);
                        finish();
                    }
                }

                break;
        }
    }
}
