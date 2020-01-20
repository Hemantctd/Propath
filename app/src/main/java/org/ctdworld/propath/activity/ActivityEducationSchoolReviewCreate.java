package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSchoolReview;
import org.ctdworld.propath.contract.ContractProgressReport;
import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterProgressReport;
import org.ctdworld.propath.presenter.PresenterSchoolReview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityEducationSchoolReviewCreate extends AppCompatActivity implements ContractSchoolReview.View, View.OnClickListener
{
    private static final String TAG = ActivityEducationSchoolReviewCreate.class.getSimpleName();


   /* private static final int COMMENT_MIC = 1;
    private static final int SUMMARY_MIC = 2;*/
    private static final int STRENGTHS_MIC = 3;
    private static final int IMPROVEMENTS_MIC = 4;
    private static final int SUGGESTIONS_MIC = 5;
    private static final int ASSISTANCE_MIC = 6;


    Toolbar mToolbar;
    TextView mTxtToolbarTitle,school_review_txt_user_name;//school_review_txt_profile_created_date;
    TextView mDate;
    TextView mRole;

    Context mContext;
    Button subjectAdd;
    RecyclerView recyclerSchoolReview;
    ImageView school_review_img_profile;
    //ArrayList<String> schoolReviewList = new ArrayList<>();
    AdapterSchoolReview adapter;
    String athlete_name,athlete_image;
    ContractSchoolReview.Presenter mPresenter;
    ProgressBar mProgressBar;
    PermissionHelper mPermissionHelper;


   // EditText mCommentsEdit;
    EditText mStrenthsEdit;
    EditText mImrovementsEdit;
   // EditText mSummaryEdit;
    EditText mAssistanceEdit;
    EditText mSuggestionsEdit;
    EditText mQualificationEdit;

    ImageView mSuggestionMic;
    ImageView mAssistanceMic;
  //  ImageView mSummaryMic;
   // ImageView mCommentMic;
    ImageView mImprovementMic;
    ImageView mStrengthMic;
    // mode crete new review or edit
    public static final String KEY_MODE_CREATE_OR_EDIT = "create or edit";

    String mAthleteId, mReviewId;

    // mode value review or edit
//    public static final int VALUE_MODE_CREATE = 1;
    public static final int VALUE_MODE_EDIT = 2;

    int MODE_CRETE_OR_EDIT;
    private ImageView mImgUpdate;
    // keys
    public static final String KEY_REVIEW_ID = "review_id";
    public static final String KEY_ATHLETE_ID = "athlete_id";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.d(TAG,"ActivityEducationSchoolReviewCreate" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_school_review);
        init();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            mAthleteId =  bundle.getString(KEY_ATHLETE_ID);
            mReviewId =  bundle.getString(KEY_REVIEW_ID);
            MODE_CRETE_OR_EDIT =  bundle.getInt(KEY_MODE_CREATE_OR_EDIT);
            athlete_name = bundle.getString("athlete_name");
            athlete_image = bundle.getString("athlete_image");

        }
        prepareToolbar();
        setSubjectsAdapter();
        setListeners();

        // if mode is edit
        if (MODE_CRETE_OR_EDIT == VALUE_MODE_EDIT)
        {
            loadReviewDataAndSetViews();

        }

        Log.d(TAG,"athlete_id" + mAthleteId);
    }


    private void init() {
        mContext = this;
        mProgressBar = findViewById(R.id.progress_bar);
        mPresenter = new PresenterSchoolReview(mContext,this);
        mToolbar = findViewById(R.id.toolbar);
        mPermissionHelper = new PermissionHelper(mContext);
        mImgUpdate = findViewById(R.id.toolbar_img_options_menu);

        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        school_review_txt_user_name = findViewById(R.id.school_review_txt_user_name);
        mRole = findViewById(R.id.role);
        mDate = findViewById(R.id.date);

        // mSummaryMic = findViewById(R.id.img_summary_mic);
        //mCommentMic = findViewById(R.id.img_mic);
        mSuggestionMic= findViewById(R.id.img_suggestions_mic);
        mImprovementMic = findViewById(R.id.img_improvements_mic);
        mStrengthMic = findViewById(R.id.img_strength_mic);
        mAssistanceMic = findViewById(R.id.img_assistance_mic);

       // mCommentsEdit = findViewById(R.id.comments_given_by_teacher);
      //  mSummaryEdit = findViewById(R.id.summary_edit);
        mStrenthsEdit = findViewById(R.id.strength_edit);
        mImrovementsEdit = findViewById(R.id.improvements_edit);
        mAssistanceEdit = findViewById(R.id.assistance_edit);
        mSuggestionsEdit = findViewById(R.id.suggestions_edit);
        mQualificationEdit = findViewById(R.id.qualification_text);
        //school_review_txt_profile_created_date = findViewById(R.id.school_review_txt_profile_created_date);
        school_review_img_profile = findViewById(R.id.school_review_img_profile);
        recyclerSchoolReview=findViewById(R.id.recycler_school_review);
        subjectAdd = findViewById(R.id.subjectAdd);
       // submitSchoolReview = findViewById(R.id.submitSchoolReview);

     //   mCommentMic.setOnClickListener(this);
      //  mSummaryMic.setOnClickListener(this);
        mStrengthMic.setOnClickListener(this);
        mImprovementMic.setOnClickListener(this);
        mSuggestionMic.setOnClickListener(this);
        mAssistanceMic.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId() == android.R.id.home)
             onBackPressed();

         return true;
    }

    private void setListeners()
    {
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);

        Glide.with(mContext)
                .load(athlete_image)
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picHeight))
                .into(school_review_img_profile);
        school_review_txt_user_name.setText(athlete_name);

        String roleID = SessionHelper.getInstance(mContext).getUser().getRoleId();
        String roleName = RoleHelper.getInstance(mContext).getRoleNameById(roleID);
        String role = "Role" + " : "+ roleName;
        mRole.setText(role);

        /*String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());*/
        mDate.setText(DateTimeHelper.getCurrentSystemDateTime());



        subjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    adapter.addLayout();
                    if(adapter.getItemCount() == 7)
                    {
                        subjectAdd.setVisibility(View.GONE);
                    }
            }
        });

        mImgUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT)
                    createSchoolReview();

                else
                    editSchoolReview();
            }
        });

    }



    // creating new school review
    private void createSchoolReview()
    {
        List<SchoolReview> list =  adapter.getReviewList();

        StringBuilder gradesBuffer = new StringBuilder();
        String[] subjects = new String[list.size()];

        for(int i = 0; i < list.size() ; i++)
        {
            SchoolReview schoolReview = list.get(i);
            subjects[i] = schoolReview.getSubject();

        }

        String sub = Arrays.toString(subjects);

        for (int j=0; j<list.size(); j++)
        {
            if (j == list.size()-1)
            {
                SchoolReview schoolReview ;
                schoolReview = list.get(j);
                gradesBuffer.append(schoolReview.getGrade());
            }
            else
            {
                SchoolReview schoolReview ;
                schoolReview = list.get(j);
                gradesBuffer.append(schoolReview.getGrade());
                gradesBuffer.append(",");
            }
        }

        String gradeData = gradesBuffer.toString();
        SchoolReview schoolReview = new SchoolReview();
        schoolReview.setAthleteID(mAthleteId);
        schoolReview.setSubject(sub);
        schoolReview.setGrade(gradeData);
        schoolReview.setAssistance(mAssistanceEdit.getText().toString().trim());
        schoolReview.setStrengths(mStrenthsEdit.getText().toString().trim());
        schoolReview.setImprovements(mImrovementsEdit.getText().toString().trim());
        schoolReview.setSuggestions(mSuggestionsEdit.getText().toString().trim());
        schoolReview.setComments("test");
        schoolReview.setQualification(mQualificationEdit.getText().toString().trim());


        List<SchoolReview> list1 = new ArrayList<>();
        list1.add(schoolReview);
//                Log.d(TAG, "final Subjects : " + sub);
//                Log.d(TAG, "final Grades : " + gradeData);

        mProgressBar.setVisibility(View.VISIBLE);

        mPresenter.saveSchoolReview(list1);
    }



    // editing existing school review
    private void editSchoolReview()
    {
        List<SchoolReview> list =  adapter.getReviewList();

        StringBuilder gradesBuffer = new StringBuilder();
        String[] subjects = new String[list.size()];

        for(int i = 0; i < list.size() ; i++)
        {
            SchoolReview schoolReview = list.get(i);
            subjects[i] = schoolReview.getSubject();

        }

        String sub = Arrays.toString(subjects);

        for (int j=0; j<list.size(); j++)
        {
            if (j == list.size()-1)
            {
                SchoolReview schoolReview ;
                schoolReview = list.get(j);
                gradesBuffer.append(schoolReview.getGrade());
            }
            else
            {
                SchoolReview schoolReview;
                schoolReview = list.get(j);
                gradesBuffer.append(schoolReview.getGrade());
                gradesBuffer.append(",");
            }
        }

        String gradeData = gradesBuffer.toString();
        SchoolReview schoolReview = new SchoolReview();
        schoolReview.setAthleteID(mAthleteId);
        schoolReview.setSubject(sub);
        schoolReview.setGrade(gradeData);
        schoolReview.setReviewID(mReviewId);
        schoolReview.setAssistance(mAssistanceEdit.getText().toString().trim());
        schoolReview.setStrengths(mStrenthsEdit.getText().toString().trim());
        schoolReview.setImprovements(mImrovementsEdit.getText().toString().trim());
        schoolReview.setSuggestions(mSuggestionsEdit.getText().toString().trim());
        schoolReview.setQualification(mQualificationEdit.getText().toString().trim());
        schoolReview.setComments("test");


       // List<SchoolReview> list1 = new ArrayList<>();
      //  list.add(schoolReview);
//                Log.d(TAG, "final Subjects : " + sub);
//                Log.d(TAG, "final Grades : " + gradeData);

        mProgressBar.setVisibility(View.VISIBLE);

        mPresenter.editSchoolReview(list,schoolReview);
    }




    private void setSubjectsAdapter()
    {
        adapter = new AdapterSchoolReview(ActivityEducationSchoolReviewCreate.this, MODE_CRETE_OR_EDIT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSchoolReview.setLayoutManager(layoutManager);
        recyclerSchoolReview.setAdapter(adapter);

    }


    private void prepareToolbar()
    {

        // showing save icon
        mImgUpdate.setImageResource(R.drawable.ic_notes_save);
        mImgUpdate.setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.school_review_title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }

    @Override
    public void onSuccess( String messages) {
        DialogHelper.showSimpleCustomDialog(mContext, messages);
        startActivity(new Intent(mContext, ActivityEducationForTeacher.class));
         finish();
    }

    @Override
    public void onFailed(String messages) {
        DialogHelper.showSimpleCustomDialog(mContext,messages);
        Log.d("messages ",messages);

        Log.d("onFailed ","Failed.......");
    }

    @Override
    public void onProgressListDeleted(String id) {

    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
           /* case R.id.img_mic :
                requestPermissions(COMMENT_MIC);
                break;*/


         /*   case R.id.img_summary_mic :
                requestPermissions(SUMMARY_MIC);

                break;*/

            case R.id.img_assistance_mic :
                requestPermissions(ASSISTANCE_MIC);

                break;

            case R.id.img_suggestions_mic :
                requestPermissions(SUGGESTIONS_MIC);

                break;

            case R.id.img_improvements_mic :
                requestPermissions(IMPROVEMENTS_MIC);

                break;

            case R.id.img_strength_mic :
                requestPermissions(STRENGTHS_MIC);

                break;
        }
    }




    private void requestPermissions(int req)
    {
        if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            voiceToText(req);
        else
            mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
    }

    private void voiceToText(final int req)
    {
        Log.i(TAG,"voiceToText() method called ");

            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText)
                {

                  /*  if (req == COMMENT_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mCommentsEdit.getText().toString().trim());
                        sb.append(spokenText+ " ");
                        mCommentsEdit.setText(spokenText);
                        mCommentsEdit.requestFocus();
                    }*/
                     if (req == STRENGTHS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mStrenthsEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mStrenthsEdit.setText(spokenText);
                        mStrenthsEdit.requestFocus();
                    }
                   /* else if (req == SUMMARY_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mSummaryEdit.getText().toString().trim());
                        sb.append(spokenText+ " ");
                        mSummaryEdit.setText(spokenText);
                        mSummaryEdit.requestFocus();
                    }*/
                    else if (req == IMPROVEMENTS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mImrovementsEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mImrovementsEdit.setText(spokenText);
                        mImrovementsEdit.requestFocus();
                    }
                    else if(req == ASSISTANCE_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mAssistanceEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mAssistanceEdit.setText(spokenText);
                        mAssistanceEdit.requestFocus();
                    }
                    else if (req == SUGGESTIONS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mSuggestionsEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mSuggestionsEdit.setText(spokenText);
                        mSuggestionsEdit.requestFocus();
                    }

                }

                @Override
                public void onError() {

                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");

    }



    private void loadReviewDataAndSetViews()
    {
        PresenterProgressReport presenter = new PresenterProgressReport(mContext, new ContractProgressReport.View() {
            @Override
            public void getOnReceivedProgressReport(SchoolReview progressReport, List<SchoolReview> progressReportList) {
                onHideProgress();
                int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
                int picWidth = UtilHelper.convertDpToPixel(mContext, picDimen);
                int picHeight = UtilHelper.convertDpToPixel(mContext, picDimen);

                Glide.with(mContext)
                        .load(progressReport.getUserPicUrl())
                        .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                        .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                        .apply(new RequestOptions().centerCrop())
                        .apply(new RequestOptions().override(picWidth, picHeight))
                        .into(school_review_img_profile);


                school_review_txt_user_name.setText(progressReport.getAthleteName());
                mAssistanceEdit.setText(progressReport.getAssistance());
                mStrenthsEdit.setText(progressReport.getStrengths());
                mImrovementsEdit.setText(progressReport.getImprovements());
                mSuggestionsEdit.setText(progressReport.getSuggestions());
                mQualificationEdit.setText(progressReport.getQualification());

                //  date.setVisibility(View.VISIBLE);
                mDate.setText(DateTimeHelper.getDateTime(progressReport.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
                // mCommentsEdit.setText(progressReport.getComments());
                adapter.updateProgressReportList(progressReportList);
                //  adapter.updateList(schoolReviewList);

                // school_review_teacher_name.setText(progressReport.getTeacher_name());
                // date.setText(new DateTimeHelper().getDateTime(progressReport.getDate()));

             /*   schoolReviewList = progressReportList;

                adapter.updateList(schoolReviewList);
*/
            }

            @Override
            public void onFailed() {
                DialogHelper.showSimpleCustomDialog(mContext, "Failed");
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
            public void onSuccess() {
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
        }) {
            @Override
            public void onSuccess() {

            }
        };

        onShowProgress();
        presenter.getProgressReport(mAthleteId, mReviewId);
    }

}
