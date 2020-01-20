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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterYesOrNoSurveyQuestionsPreview;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;
import java.util.List;

public abstract class ActivitySurveyYesOrNoQuestionsPreview extends AppCompatActivity implements View.OnClickListener, ContractSurvey.View {

    public static final String TAG =  ActivitySurveyYesOrNoQuestionsPreview.class.getSimpleName();

   /* public static final String SURVEY_QUESTIONS_LIST =  "survey_questions_items";
    public static final String SURVEY_YES_NO =  "survey_yes_no_items";
    public static final String KEY_SURVEY_CREATE_EDIT =  "type";*/

    Toolbar mToolbar;
    TextView mToolbarTitle;
    RecyclerView mRecyclerView;
   // Button mEditBtn, mSaveBtn;
    Context mContext;
    Survey.SurveyData.YesNo surveyYesNo;
    List<Survey.SurveyData.YesNo.Questions> surveyQuestionsArrayList;
    AdapterYesOrNoSurveyQuestionsPreview adapterYesOrNoSurveyQuestionsPreview;
    String mSurveyType;
    ContractSurvey.Presenter mPresenter;
    LinearLayout mCommonDataLayout;
    TextView surveyDesc, surveyTitle, surveyType;
    ImageView mToolbarImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_yes_or_no_questions_preview);

        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null)
        {
            mSurveyType = mBundle.getString(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT);
            surveyYesNo = (Survey.SurveyData.YesNo) mBundle.getSerializable(ActivityCreateSurvey.SURVEY_YES_NO);
            if (surveyYesNo != null) {
                surveyQuestionsArrayList = surveyYesNo.getSurveyYesNoQuestionList();
            }
        }


        init();
        setToolbar();
        prepareRecyclerView();
    }

    private void init()
    {
        mContext = this;
        mPresenter = new PresenterCreateSurvey(mContext,this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mRecyclerView = findViewById(R.id.recycler_yes_no_questions);
      /*  mEditBtn = findViewById(R.id.edit_btn);
        mSaveBtn = findViewById(R.id.save_btn);*/
        mToolbarImageOptions = findViewById(R.id.toolbar_img_options_menu);
        mToolbarImageOptions.setVisibility(View.VISIBLE);
        mToolbarImageOptions.setOnClickListener(this);


    /*    mSaveBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);*/


        surveyTitle = findViewById(R.id.set_survey_title);
        surveyType = findViewById(R.id.set_survey_type);
        surveyDesc = findViewById(R.id.set_survey_desc);
        mCommonDataLayout = findViewById(R.id.common_data_layout);




            if (surveyYesNo != null) {
                mCommonDataLayout.setVisibility(View.VISIBLE);
               /* mEditBtn.setVisibility(View.GONE);
                mSaveBtn.setVisibility(View.GONE);*/

                surveyTitle.setText(surveyYesNo.getTitle());
                surveyType.setText(surveyYesNo.getQuestionType());
                surveyDesc.setText(surveyYesNo.getDescription());
            }


        }

    private void prepareRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterYesOrNoSurveyQuestionsPreview = new AdapterYesOrNoSurveyQuestionsPreview(this, surveyQuestionsArrayList);
        mRecyclerView.setAdapter(adapterYesOrNoSurveyQuestionsPreview);
    }
    private void setToolbar()
    {
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.yes_no_preview);
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
    public void onClick(View v) {

        if (v.getId() == R.id.toolbar_img_options_menu)
        {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit");
            builder.addOption(BottomSheetOption.OPTION_SAVE, "Save");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {
                    switch (option) {
                        case BottomSheetOption.OPTION_EDIT:

                            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                                Intent intentEdit = new Intent(ActivitySurveyYesOrNoQuestionsPreview.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_YES_NO);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                                startActivity(intentEdit);
                                finish();
                            } else {
                                Intent intentEdit = new Intent(ActivitySurveyYesOrNoQuestionsPreview.this, ActivityCreateSurvey.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT);
                                intentEdit.putExtra(ActivityCreateSurvey.CREATE_SURVEY_TYPE, ActivityCreateSurvey.SURVEY_TYPE_YES_NO);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, surveyYesNo);
                                startActivity(intentEdit);
                                finish();
                            }
                            break;

                        case BottomSheetOption.OPTION_SAVE:
                            List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList = adapterYesOrNoSurveyQuestionsPreview.getSurveyQusetions();

                            if (mSurveyType.equals(ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER)) {
                                mPresenter.editYesNoSurvey(surveyQuestionsList, surveyYesNo);
                            } else {
                                mPresenter.saveYesNoSurvey(surveyQuestionsList, surveyYesNo);
                            }
                            break;
                    }

                }
            });

            try {
                options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

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
        DialogHelper.showSimpleCustomDialog(mContext, msg);


    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {

    }
}
