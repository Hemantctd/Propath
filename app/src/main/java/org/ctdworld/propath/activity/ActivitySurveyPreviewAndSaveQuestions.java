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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterPastSurvey;
import org.ctdworld.propath.adapter.AdapterSurveyPreviewQuestions;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

public abstract class ActivitySurveyPreviewAndSaveQuestions extends AppCompatActivity implements View.OnClickListener, ContractSurvey.View {

  //  private final String TAG = ActivitySurveyPreviewAndSaveQuestions.class.getSimpleName();
    Toolbar mToolbar;
    TextView mToolbarTitle;
   // Button btnSurveySave, btnSurveyEdit;
    Context mContext;
    RecyclerView survey_questions_preview_recycler_view;
    LinearLayout mCommonDataLayout;
    TextView surveyDesc, surveyTitle, surveyType;
    AdapterSurveyPreviewQuestions adapter;
    ContractSurvey.Presenter mPresenter;
    String msurveyType;
    ProgressBar mProgressBar;
    Bundle mBundle = null;
    Survey.SurveyData.FreeResponse surveyFreeResponse;
    List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsArrayList;
    ImageView mToolbarImageOptions;
    List<Survey.SurveyData.FreeResponse.Questions> listQuestions  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_preview_questions);



        init();
        setToolBar();
        setSurveyAdapter();

    }

    //for set adapter
    private void setSurveyAdapter() {

        adapter = new AdapterSurveyPreviewQuestions(ActivitySurveyPreviewAndSaveQuestions.this,surveyQuestionsArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        survey_questions_preview_recycler_view.setLayoutManager(layoutManager);
        survey_questions_preview_recycler_view.setAdapter(adapter);
    }

    // for initialization
    private void init() {
        mContext = this;
        mPresenter = new PresenterCreateSurvey(mContext,this);
        mProgressBar = findViewById(R.id.progress_bar);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
       /* btnSurveyEdit = findViewById(R.id.btn_survey_edit);
        btnSurveySave = findViewById(R.id.btn_survey_save);*/
        survey_questions_preview_recycler_view = findViewById(R.id.survey_questions_preview_recycler_view);
        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);
        mCommonDataLayout = findViewById(R.id.common_data_layout);
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);

/*
        btnSurveySave.setOnClickListener(this);
        btnSurveyEdit.setOnClickListener(this);*/

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
        {
            msurveyType = mBundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyFreeResponse = (Survey.SurveyData.FreeResponse) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_FREE_RESPONSE);
            if (surveyFreeResponse != null) {
                surveyQuestionsArrayList = surveyFreeResponse.getQuestionsList();//(ArrayList<Survey.SurveyData.FreeResponse.Questions>) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_QUESTION_LIST);
            }
        }


            if (surveyFreeResponse != null) {
               /* btnSurveyEdit.setVisibility(View.GONE);
                btnSurveySave.setVisibility(View.GONE);*/

                surveyTitle.setText(surveyFreeResponse.getTitle());
                surveyType.setText(surveyFreeResponse.getQuestionType());
                surveyDesc.setText(surveyFreeResponse.getDescription());
            }




    }


// button clickable
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.toolbar_img_options_menu) {

            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
            builder.addOption(BottomSheetOption.OPTION_SAVE, "Save");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    switch (option) {
                        case BottomSheetOption.OPTION_EDIT:

                            if (msurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                                Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                                startActivity(intentEdit);
                            } else {
                                Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                                startActivity(intentEdit);
                            }


                            break;

                        case BottomSheetOption.OPTION_SAVE:
                            saveSurveyData();
                            mProgressBar.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

           /* case R.id.btn_survey_save:
                saveSurveyData();
                mProgressBar.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_survey_edit:

                   if (msurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
                    {
                        Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                        intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                        intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                        startActivity(intentEdit);
                    }
                   else {
                       Intent intentEdit = new Intent(ActivitySurveyPreviewAndSaveQuestions.this, ActivityCreateSurvey.class);
                       intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                       intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, surveyFreeResponse);
                       intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_FREE_RESPONSE);
                       startActivity(intentEdit);
                   }


                break;*/
        }
    }

// to save survey
    public void saveSurveyData() {

        List<Survey.SurveyData.FreeResponse.Questions> list =  adapter.getSurveyQusetions();

        if (msurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER))
        {
            /*Survey.SurveyData.FreeResponse.Questions questions = new Survey.SurveyData.FreeResponse.Questions();

            for (int i = 0; i<list.size(); i++)
            {
                questions  = list.get(i);
                questions.setQuestion(questions.getQuestion());

                File f = new File(questions.getQuestionImg());
                String mimetype= new MimetypesFileTypeMap().getContentType(f);
                String type = mimetype.split("/")[0];
                if(type.equals("image")) {
                    questions.setQuestionImg(questions.getQuestionImg());
                }
                else
                    questions.setQuestionImg("blank_image");



                listQuestions.add(questions);


                }*/

            mPresenter.editSurvey(list, surveyFreeResponse);
        }
        else {
            mPresenter.saveSurvey(list,surveyFreeResponse);

        }
    }

    @Override
    public void onSuccess(String msg) {
      DialogHelper.showSimpleCustomDialog(mContext, msg, new DialogHelper.ShowSimpleDialogListener() {
          @Override
          public void onOkClicked() {
              mContext.startActivity(new Intent(mContext, ActivitySurvey.class));
              finish();
          }
      });
    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);

    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {

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
