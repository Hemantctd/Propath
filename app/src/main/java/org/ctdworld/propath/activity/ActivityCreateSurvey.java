package org.ctdworld.propath.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class ActivityCreateSurvey extends AppCompatActivity
{
    private final String TAG = ActivityCreateSurvey.class.getSimpleName();
    public static final String SURVEY_FREE_RESPONSE= "free_response_data";
    public static final String SURVEY_YES_NO =  "survey_yes_no_data";
    public static final String SURVEY_MULTIPLE_CHOICE=  "survey_multiple_choice_data";
    public static final String SURVEY_RATING_SCALE =  "survey_rating_scale_data";


    //public static final String SURVEY_QUESTION_LIST =  "survey_qusetions_list";

    public static final String KEY_SURVEY_CREATE_EDIT = "type";

    //survey type value
    public static final String SURVEY_TYPE_EDIT= "edit";
    public static final String SURVEY_TYPE_EDIT_SERVER = "edited";
    public static final String SURVEY_TYPE_NOT_EDIT= "not edit";
    public static final int SURVEY_TYPE_VIEW_SURVEY= 3 ;

    // create survey type key
    public static final String CREATE_SURVEY_TYPE= "create survey type";
    public static final int SURVEY_TYPE_FREE_RESPONSE= 1 ;
    public static final int SURVEY_TYPE_MULTIPLE_CHOICE= 2 ;
    public static final int SURVEY_TYPE_YES_NO= 3 ;
    public static final int SURVEY_TYPE_RATING_SCALE= 4 ;

    int mSurveyType;


    Spinner mNoOfQuestionsSpinner,mQuestionTypeSpinner;
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    Button addSurveyQuestions;
    EditText surveyTitle, surveyDesc;
    int selected_no_of_questions;
    int mBundleTotalQuestion;
    String selected_qusetion_type;
    Context mContext;
    int questionPosition=0,questionTypePosition = 0;
    String survey_type,mSurveyID;
    List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsArrayList;
    List<Survey.SurveyData.RatingScale.Questions> surveyRatingScaleQuestionList;
    List<Survey.SurveyData.MultipleChoice.Questions> surveyMultipleChoiceQuestionsList;
    List<Survey.SurveyData.YesNo.Questions> surveyYesNoQuestionList;
    Survey.SurveyData.FreeResponse surveyFreeResponse;
    Survey.SurveyData.MultipleChoice surveyMultipleChoice;
    Survey.SurveyData.YesNo surveyYesNo;
    Survey.SurveyData.RatingScale surveyRatingScale;
    LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

        init();
        setToolbar();
        setListeners();


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            survey_type = mBundle.getString(KEY_SURVEY_CREATE_EDIT);
            mSurveyType = mBundle.getInt(CREATE_SURVEY_TYPE);

            if (mSurveyType == SURVEY_TYPE_FREE_RESPONSE)
            {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

                onFreeResponseTypeBundleReceived(mBundle);
            }
            else if (mSurveyType == SURVEY_TYPE_MULTIPLE_CHOICE)
            {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

                onMultiChoiceTypeBundleReceived(mBundle);
            }
            else if (mSurveyType == SURVEY_TYPE_YES_NO)
            {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

                onYesNoTypeBundleReceived(mBundle);
            }
            else
            {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
                onRatingScaleTypeBundleReceived(mBundle);

            }

        }

    }
    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        addSurveyQuestions=findViewById(R.id.addSurveyQuestions);
        mNoOfQuestionsSpinner = findViewById(R.id.select_no_of_questions);
        mQuestionTypeSpinner = findViewById(R.id.selectQuestionType);
        surveyTitle = findViewById(R.id.surveyTitle);
        surveyDesc = findViewById(R.id.surveyDesc);
        mLayout = findViewById(R.id.layout);

    }


    private void onFreeResponseTypeBundleReceived(Bundle mBundle)
    {
        surveyFreeResponse = (Survey.SurveyData.FreeResponse) mBundle.getSerializable(SURVEY_FREE_RESPONSE);
        if (surveyFreeResponse != null) {
            surveyQuestionsArrayList = surveyFreeResponse.getQuestionsList();
        }
        String[] noOfQuestionsArray = getResources().getStringArray(R.array.entries_question_numbers);
        String[] questionTypeArray = getResources().getStringArray(R.array.entries_question_type_data);

        mSurveyID = surveyFreeResponse.getId();

        if (survey_type.equals(SURVEY_TYPE_EDIT)|| survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }
     /*   else if (survey_type.equals("edit_first_time"))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }*/

    }


    private void onRatingScaleTypeBundleReceived(Bundle mBundle)
    {
        surveyRatingScale = (Survey.SurveyData.RatingScale) mBundle.getSerializable(SURVEY_RATING_SCALE);
        if (surveyRatingScale != null) {
            surveyRatingScaleQuestionList = surveyRatingScale.getSurveRatingScaleQuestionList();
        }
        String[] noOfQuestionsArray = getResources().getStringArray(R.array.entries_question_numbers);
        String[] questionTypeArray = getResources().getStringArray(R.array.entries_question_type_data);

///        mSurveyID = surveyRatingScale.getId();

        if (survey_type.equals(SURVEY_TYPE_EDIT)|| survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }
     /*   else if (survey_type.equals("edit_first_time"))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }*/

    }


    private void onMultiChoiceTypeBundleReceived(Bundle mBundle)
    {
        surveyMultipleChoice = (Survey.SurveyData.MultipleChoice)mBundle.getSerializable(SURVEY_MULTIPLE_CHOICE);
        if (surveyMultipleChoice != null) {
            surveyMultipleChoiceQuestionsList = surveyMultipleChoice.getSurveyMultipleQuestionList();
        }
        String[] noOfQuestionsArray = getResources().getStringArray(R.array.entries_question_numbers);
        String[] questionTypeArray = getResources().getStringArray(R.array.entries_question_type_data);


        mSurveyID = surveyMultipleChoice.getId();
        if (survey_type.equals(SURVEY_TYPE_EDIT) || survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }
      /*  else if (survey_type.equals("edit_first_time"))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }*/

    }



    private void onYesNoTypeBundleReceived(Bundle mBundle)
    {
        surveyYesNo = (Survey.SurveyData.YesNo) mBundle.getSerializable(SURVEY_YES_NO);
        if (surveyYesNo != null) {
            surveyYesNoQuestionList = surveyYesNo.getSurveyYesNoQuestionList();
        }

        if (surveyYesNo != null) {
            mSurveyID = surveyYesNo.getId();
        }
        String[] noOfQuestionsArray = getResources().getStringArray(R.array.entries_question_numbers);
        String[] questionTypeArray = getResources().getStringArray(R.array.entries_question_type_data);
        if (survey_type.equals(SURVEY_TYPE_EDIT) || survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            setSelectNoOfQuestions(noOfQuestionsArray,questionTypeArray);
        }
    }



    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTxtTitle.setText(R.string.create_survey);
        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(mToolbar);
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }

    private void setListeners()
    {
        mQuestionTypeSpinner.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
                return false;
            }
        }) ;

        mNoOfQuestionsSpinner.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
                return false;
            }
        }) ;
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
            }
        });
        addSurveyQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (surveyTitle.getText().toString().trim().equals(""))
                {
                    DialogHelper.showSimpleCustomDialog(mContext, "Please fill the title");
                }
                else if (surveyDesc.getText().toString().trim().equals(""))
                {
                    DialogHelper.showSimpleCustomDialog(mContext, "Please fill the description");
                }
                else if (selected_qusetion_type.equals("Type"))
                {
                    UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
                    DialogHelper.showSimpleCustomDialog(mContext, "Please select question type");
                }
                else {

                    UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

                    switch (selected_qusetion_type) {
                        case "Free Response":
                            sendFreeResponseData();
                            break;
                        case "Rating Scale":
                            sendRatingScaleData();//startActivity(new Intent(mContext, ActivityRatingScaleQuestions.class));
                            break;
                        case "Multi-Choice":
                            sendMultipleChoiceData();
                            break;
                        default:
                            sendYesNoTypeData();
                            break;
                    }
                }

            }

        });


        mNoOfQuestionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

                if (survey_type.equals(SURVEY_TYPE_EDIT)|| survey_type.equals(SURVEY_TYPE_EDIT_SERVER)) {
                    setSelectedEditNoOfQuestions(Integer.parseInt((String) adapterView.getItemAtPosition(i)));
                }
           /*     else if (survey_type.equals("edit_first_time"))
                {
                    setSelectedEditNoOfQuestions(Integer.parseInt((String) adapterView.getItemAtPosition(i)));
                }*/
                else
                {

                    selected_no_of_questions = Integer.parseInt((String) adapterView.getItemAtPosition(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

            }
        });

         mQuestionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);
                selected_qusetion_type = (String) adapterView.getItemAtPosition(i);
                Log.d("selectedQuestionType",selected_qusetion_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;

    }

    void setSelectNoOfQuestions(String[] noOfQuestionsArray, String[] questionTypeArray)
    {
        UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

        if (mSurveyType == SURVEY_TYPE_FREE_RESPONSE) {
            mBundleTotalQuestion = surveyQuestionsArrayList.size();
            surveyTitle.setText(surveyFreeResponse.getTitle());
            surveyDesc.setText(surveyFreeResponse.getDescription());
            for (int x = 0; x < noOfQuestionsArray.length; x++) {
                if (String.valueOf(surveyQuestionsArrayList.size()).contains(noOfQuestionsArray[x])) {
                    questionPosition = x;
                    break;
                }
            }

            for (int i = 0; i < questionTypeArray.length; i++) {
                if (surveyFreeResponse.getQuestionType().contains(questionTypeArray[i])) {
                    questionTypePosition = i;
                    break;
                }
            }
            mNoOfQuestionsSpinner.setSelection(questionPosition);
            mQuestionTypeSpinner.setSelection(questionTypePosition);
        }
        else if (mSurveyType == SURVEY_TYPE_MULTIPLE_CHOICE) {
            mBundleTotalQuestion = surveyMultipleChoiceQuestionsList.size();
            surveyTitle.setText(surveyMultipleChoice.getTitle());
            surveyDesc.setText(surveyMultipleChoice.getDescription());
            for (int x = 0; x < noOfQuestionsArray.length; x++) {
                if (String.valueOf(surveyMultipleChoiceQuestionsList.size()).contains(noOfQuestionsArray[x]))
                {
                    questionPosition = x;
                    break;
                }
            }

            for (int i = 0; i < questionTypeArray.length; i++) {
                if (surveyMultipleChoice.getQuestionType().contains(questionTypeArray[i])) {
                    questionTypePosition = i;
                    break;
                }
            }
            mNoOfQuestionsSpinner.setSelection(questionPosition);
            mQuestionTypeSpinner.setSelection(questionTypePosition);
        }
        else if (mSurveyType == SURVEY_TYPE_YES_NO) {
            mBundleTotalQuestion = surveyYesNoQuestionList.size();
            surveyTitle.setText(surveyYesNo.getTitle());
            surveyDesc.setText(surveyYesNo.getDescription());
            for (int x = 0; x < noOfQuestionsArray.length; x++) {
                if (String.valueOf(surveyYesNoQuestionList.size()).contains(noOfQuestionsArray[x])) {
                    questionPosition = x;
                    break;
                }
            }

            for (int i = 0; i < questionTypeArray.length; i++) {
                if (surveyYesNo.getQuestionType().contains(questionTypeArray[i])) {
                    questionTypePosition = i;
                    break;
                }
            }
            mNoOfQuestionsSpinner.setSelection(questionPosition);
            mQuestionTypeSpinner.setSelection(questionTypePosition);
        }
        else if (mSurveyType == SURVEY_TYPE_RATING_SCALE) {
            mBundleTotalQuestion = surveyRatingScaleQuestionList.size();
            surveyTitle.setText(surveyRatingScale.getTitle());
            surveyDesc.setText(surveyRatingScale.getDescription());
            for (int x = 0; x < noOfQuestionsArray.length; x++) {
                if (String.valueOf(surveyRatingScaleQuestionList.size()).contains(noOfQuestionsArray[x])) {
                    questionPosition = x;
                    break;
                }
            }

            for (int i = 0; i < questionTypeArray.length; i++) {
                if (surveyRatingScale.getQuestionType().contains(questionTypeArray[i])) {
                    questionTypePosition = i;
                    break;
                }
            }
            mNoOfQuestionsSpinner.setSelection(questionPosition);
            mQuestionTypeSpinner.setSelection(questionTypePosition);
        }
    }


    public void setSelectedEditNoOfQuestions(int position) {

        UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

        if (mSurveyType == SURVEY_TYPE_FREE_RESPONSE) {
            int oldTotalQuestions = mBundleTotalQuestion;
            int countNewAddedQuestion = position - oldTotalQuestions;


            // # this removes newly added question which was selected in spinner, before user selects other number in spinner
            int earlierAddedQuestion = surveyQuestionsArrayList.size() - mBundleTotalQuestion;
            for (int i = 0; i < earlierAddedQuestion; i++) {
                surveyQuestionsArrayList.remove(surveyQuestionsArrayList.size() - 1);
            }


            if (position > oldTotalQuestions) {
                for (int j = 0; j < countNewAddedQuestion; j++) {
                    Survey.SurveyData.FreeResponse.Questions surveyQuestions = new Survey.SurveyData.FreeResponse.Questions();
                    surveyQuestions.setQuestion("");
                    surveyQuestionsArrayList.add(surveyQuestions);
                }
            } else if (position < oldTotalQuestions) {
                for (int j = countNewAddedQuestion; j < 0; j++) {
                    surveyQuestionsArrayList.remove(surveyQuestionsArrayList.size() - 1);

                }
            }

            selected_no_of_questions = position;
            Log.d(TAG, "selected no " + selected_no_of_questions);
            Log.d(TAG, "Question list size : " + surveyQuestionsArrayList.size());
        }
        else if (mSurveyType == SURVEY_TYPE_MULTIPLE_CHOICE)
        {
            int oldTotalQuestions = mBundleTotalQuestion;
            int countNewAddedQuestion = position - oldTotalQuestions;


            // # this removes newly added question which was selected in spinner, before user selects other number in spinner
            int earlierAddedQuestion = surveyMultipleChoiceQuestionsList.size() - mBundleTotalQuestion;
            for (int i = 0; i < earlierAddedQuestion; i++) {
                surveyMultipleChoiceQuestionsList.remove(surveyMultipleChoiceQuestionsList.size() - 1);
            }


            if (position > oldTotalQuestions) {
                for (int j = 0; j < countNewAddedQuestion; j++) {
                    Survey.SurveyData.MultipleChoice.Questions surveyQuestions = new Survey.SurveyData.MultipleChoice.Questions();
                    surveyQuestions.setQuestion("");
                    surveyMultipleChoiceQuestionsList.add(surveyQuestions);
                }
            } else if (position < oldTotalQuestions) {
                for (int j = countNewAddedQuestion; j < 0; j++) {
                    surveyMultipleChoiceQuestionsList.remove(surveyMultipleChoiceQuestionsList.size() - 1);

                }
            }

            selected_no_of_questions = position;
            Log.d(TAG, "selected no " + selected_no_of_questions);
            Log.d(TAG, "surveyMultipleChoiceQuestionsList Question list size : " + surveyMultipleChoiceQuestionsList.size());
        }
        else if (mSurveyType == SURVEY_TYPE_YES_NO)
        {
            UtilHelper.hideKeyboard(ActivityCreateSurvey.this);

            int oldTotalQuestions = mBundleTotalQuestion;
            int countNewAddedQuestion = position - oldTotalQuestions;


            // # this removes newly added question which was selected in spinner, before user selects other number in spinner
            int earlierAddedQuestion = surveyYesNoQuestionList.size() - mBundleTotalQuestion;
            for (int i = 0; i < earlierAddedQuestion; i++) {
                surveyYesNoQuestionList.remove(surveyYesNoQuestionList.size() - 1);
            }


            if (position > oldTotalQuestions) {
                for (int j = 0; j < countNewAddedQuestion; j++) {
                    Survey.SurveyData.YesNo.Questions surveyQuestions = new Survey.SurveyData.YesNo.Questions();
                    surveyQuestions.setQuestion("");
                    surveyYesNoQuestionList.add(surveyQuestions);
                }
            } else if (position < oldTotalQuestions) {
                for (int j = countNewAddedQuestion; j < 0; j++) {
                    surveyYesNoQuestionList.remove(surveyYesNoQuestionList.size() - 1);

                }
            }

            selected_no_of_questions = position;
            Log.d(TAG, "selected no " + selected_no_of_questions);
            Log.d(TAG, "surveyYesNoQuestionList Question list size : " + surveyYesNoQuestionList.size());
        }
    }



    // send free response data to next activity
    public void sendFreeResponseData()
    {
        Survey.SurveyData.FreeResponse surveyFreeResponse = new Survey.SurveyData.FreeResponse();

        surveyFreeResponse.setTitle(surveyTitle.getText().toString().trim());
        surveyFreeResponse.setDescription(surveyDesc.getText().toString().trim());
        surveyFreeResponse.setQuestionNo(String.valueOf(selected_no_of_questions));
        surveyFreeResponse.setQuestionType(selected_qusetion_type);
        surveyFreeResponse.setQuestionsList(surveyQuestionsArrayList);
        if (survey_type.equals(SURVEY_TYPE_EDIT)) {

            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyQuestionsActivity.class);
            i.putExtra(SURVEY_FREE_RESPONSE,surveyFreeResponse);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT);
            startActivity(i);
            finish();
        }
        else if (survey_type.equals(SURVEY_TYPE_EDIT_SERVER)) {
            surveyFreeResponse.setId(mSurveyID);

            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyQuestionsActivity.class);
            i.putExtra(SURVEY_FREE_RESPONSE,surveyFreeResponse);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT_SERVER);
            startActivity(i);
            finish();
        }
        else
        {

            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyQuestionsActivity.class);
            i.putExtra(SURVEY_FREE_RESPONSE,surveyFreeResponse);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_NOT_EDIT);
            startActivity(i);

        }
    }


    // send multiple choice data to next activity
    public void sendMultipleChoiceData()
    {
        Survey.SurveyData.MultipleChoice surveyMultipleChoice = new Survey.SurveyData.MultipleChoice();
        surveyMultipleChoice.setTitle(surveyTitle.getText().toString().trim());
        surveyMultipleChoice.setDescription(surveyDesc.getText().toString().trim());
        surveyMultipleChoice.setQuestionNo(String.valueOf(selected_no_of_questions));
        surveyMultipleChoice.setQuestionType(selected_qusetion_type);
        surveyMultipleChoice.setSurveyMultipleQuestionList(surveyMultipleChoiceQuestionsList);



        if (survey_type.equals(SURVEY_TYPE_EDIT)) {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyMultipleChoiceQuestions.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT);
            i.putExtra(SURVEY_MULTIPLE_CHOICE,surveyMultipleChoice);
            startActivity(i);
        }
        else if (survey_type.equals(SURVEY_TYPE_EDIT_SERVER)) {
            surveyMultipleChoice.setId(mSurveyID);

            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyMultipleChoiceQuestions.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT_SERVER);
            i.putExtra(SURVEY_MULTIPLE_CHOICE,surveyMultipleChoice);
            startActivity(i);
        }

        else
        {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyMultipleChoiceQuestions.class);
            i.putExtra(SURVEY_MULTIPLE_CHOICE,  surveyMultipleChoice);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_NOT_EDIT);
            startActivity(i);
            finish();
        }
    }




    // send yes/no type data to next activity
    public void sendYesNoTypeData()
    {
        Survey.SurveyData.YesNo surveyYesNo = new Survey.SurveyData.YesNo();
        surveyYesNo.setTitle(surveyTitle.getText().toString().trim());
        surveyYesNo.setDescription(surveyDesc.getText().toString().trim());
        surveyYesNo.setQuestionNo(String.valueOf(selected_no_of_questions));
        surveyYesNo.setQuestionType(selected_qusetion_type);
        surveyYesNo.setSurveyYesNoQuestionList(surveyYesNoQuestionList);

        if (survey_type.equals(SURVEY_TYPE_EDIT))
        {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyYesOrNoQuestion.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT);
            i.putExtra(SURVEY_YES_NO, surveyYesNo);
            startActivity(i);
        }
        else if (survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            surveyYesNo.setId(mSurveyID);

            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyYesOrNoQuestion.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT_SERVER);
            i.putExtra(SURVEY_YES_NO, surveyYesNo);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivitySurveyYesOrNoQuestion.class);
            i.putExtra(SURVEY_YES_NO,  surveyYesNo);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_NOT_EDIT);
            startActivity(i);
            finish();
        }

    }


    private void sendRatingScaleData(){
        Survey.SurveyData.RatingScale surveyRatingScale = new Survey.SurveyData.RatingScale();
        surveyRatingScale.setTitle(surveyTitle.getText().toString().trim());
        surveyRatingScale.setDescription(surveyDesc.getText().toString().trim());
        surveyRatingScale.setQuestionNo(String.valueOf(selected_no_of_questions));
        surveyRatingScale.setQuestionType(selected_qusetion_type);
        surveyRatingScale.setSurveyRatingScaleQuestionList(surveyRatingScaleQuestionList);

        if (survey_type.equals(SURVEY_TYPE_EDIT))
        {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivityRatingScaleQuestions.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT);
            i.putExtra(SURVEY_RATING_SCALE, surveyRatingScale);
            startActivity(i);
        }
        else if (survey_type.equals(SURVEY_TYPE_EDIT_SERVER))
        {
            surveyYesNo.setId(mSurveyID);

            Intent i = new Intent(ActivityCreateSurvey.this, ActivityRatingScaleQuestions.class);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_EDIT_SERVER);
            i.putExtra(SURVEY_RATING_SCALE, surveyRatingScale);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(ActivityCreateSurvey.this, ActivityRatingScaleQuestions.class);
            i.putExtra(SURVEY_RATING_SCALE,  surveyRatingScale);
            i.putExtra(KEY_SURVEY_CREATE_EDIT, SURVEY_TYPE_NOT_EDIT);
            startActivity(i);
            finish();
        }
    }

}
