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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSurveyMultipleChoiceQuestions;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;


import java.util.ArrayList;
import java.util.List;

public class ActivitySurveyMultipleChoiceQuestions extends AppCompatActivity implements View.OnClickListener {

 //   private final String TAG = ActivitySurveyMultipleChoiceQuestions.class.getSimpleName();


    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView mRecyclerView;
    Button mNextBtn;
    ImageView imgAddQuestions;
    String survey_type;
    List<Survey.SurveyData.MultipleChoice.Questions> questionList = new ArrayList<>();
    AdapterSurveyMultipleChoiceQuestions adapterSurveyMultipleChoiceQuestions;
    Survey.SurveyData.MultipleChoice surveyMultipleChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_multiple_choice_questions);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null ) {
            survey_type =   bundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);

                surveyMultipleChoice = (Survey.SurveyData.MultipleChoice) bundle.getSerializable(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE);
                if (surveyMultipleChoice != null) {
                    questionList = surveyMultipleChoice.getSurveyMultipleQuestionList();
                }

        }

        init();
        setToolbar();  // setting toolbar
        prepareRecyclerView();

      /*  if (survey_type.equals("edit_first_time"))
        {
            adapterSurveyMultipleChoiceQuestions.addQuestionList(questionList);
        }
        else*/
            if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT) || survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
            {
                adapterSurveyMultipleChoiceQuestions.addQuestionList(questionList);
            }
            else
            {
                int noOfQuestions = Integer.parseInt(surveyMultipleChoice.getQuestionNo());
                for (int i = 0; i < noOfQuestions; i++) {
                    adapterSurveyMultipleChoiceQuestions.addQuestion(new Survey.SurveyData.MultipleChoice.Questions());
                }
            }

    }

    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mNextBtn = findViewById(R.id.next_btn);
        imgAddQuestions = findViewById(R.id.img_add_questions);
        mRecyclerView = findViewById(R.id.recycler_multiple_choice_questions);
        mNextBtn.setOnClickListener(this);
        imgAddQuestions.setOnClickListener(this);

        if (Integer.parseInt(surveyMultipleChoice.getQuestionNo()) == 20)
        {
            imgAddQuestions.setVisibility(View.GONE);
        }
    }


    private void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterSurveyMultipleChoiceQuestions = new AdapterSurveyMultipleChoiceQuestions(getApplicationContext());
        mRecyclerView.setAdapter(adapterSurveyMultipleChoiceQuestions);

      /*  int noOfQuestions = Integer.parseInt(surveyMultipleChoice.getSurveyNoOfQuestions());

        for (int i=0; i<noOfQuestions;i++)
        {
            adapterSurveyMultipleChoiceQuestions.addQuestion(new SurveyMultipleChoice.SurveyQuestions());
        }*/

    }

    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.multiple_choice_title);
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
            case R.id.img_add_questions :

                adapterSurveyMultipleChoiceQuestions.addQuestion(new Survey.SurveyData.MultipleChoice.Questions());
                if(adapterSurveyMultipleChoiceQuestions.getItemCount() == 20)
                {
                    imgAddQuestions.setVisibility(View.GONE);
                }

                break;


            case R.id.next_btn :

                List<Survey.SurveyData.MultipleChoice.Questions> list =  adapterSurveyMultipleChoiceQuestions.getSurveyQusetions();

                surveyMultipleChoice.setSurveyMultipleQuestionList(list);
                if (list == null)
                {
                    DialogHelper.showSimpleCustomDialog(mContext,"Please fill questions ");
                }
           /*     if (survey_type.equals("edit_first_time"))
                {
                    Intent surveyDataSend = new Intent(this, ActivitySurveyMultipleChoiceQuestionsWithOptions.class);
                    surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE,surveyMultipleChoice);
                    surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_QUESTION_LIST, (Serializable) list);
                    surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT,"edit_first_time");
                    startActivity(surveyDataSend);


                }
                else*/
           else {
                    if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT)) {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyMultipleChoiceQuestionsWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                        startActivity(surveyDataSend);
                    }
                   else if (survey_type.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyMultipleChoiceQuestionsWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        startActivity(surveyDataSend);
                    }
                    else {
                        Intent surveyDataSend = new Intent(this, ActivitySurveyMultipleChoiceQuestionsWithOptions.class);
                        surveyDataSend.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, surveyMultipleChoice);
                        surveyDataSend.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_NOT_EDIT);
                        startActivity(surveyDataSend);
                    }
                }

                break;
        }
    }
}
