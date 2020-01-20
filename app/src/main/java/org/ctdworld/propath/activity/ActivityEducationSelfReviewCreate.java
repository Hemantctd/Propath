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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSelfReview;
import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.contract.ContractSelfReviewReport;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.SelfReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterSelfReview;
import org.ctdworld.propath.presenter.PresenterSelfReviewReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityEducationSelfReviewCreate extends AppCompatActivity implements ContractSelfReview.View, View.OnClickListener{

    private static final String TAG = ActivityEducationSelfReviewCreate.class.getSimpleName();

   /* private static final int COMMENT_MIC = 1;
    private static final int SUMMARY_MIC = 2;*/
    private static final int STRENGTHS_MIC = 3;
    private static final int IMPROVEMENTS_MIC = 4;
    private static final int SUGGESTIONS_MIC = 5;
    private static final int ASSISTANCE_MIC = 6;

    Toolbar mToolbar;
    TextView mTxtToolbarTitle,self_review_txt_user_name;
    Context mContext;
    ImageView self_review_img_profile;
    RecyclerView recyclerSelfReviewReport;
    AdapterSelfReview adapter;
    TextView mDate;
    TextView mRole;

    EditText qualification_text;
    ContractSelfReview.Presenter mPresenter;
    ProgressBar mProgressBar;
    PermissionHelper mPermissionHelper;


   // EditText mCommentsEdit;
    EditText mStrengthsEdit;
    EditText mSuggestionsEdit;
    EditText mImprovementsEdit;
    EditText mAssistanceEdit;
  //  EditText mSummaryEdit;


    //ImageView mCommentMic;
    ImageView mSuggestionMic;
    ImageView mStrengthMic;
    ImageView mImprovementMic;
    ImageView mAssistanceMic;
   // ImageView mSummaryMic;
   private ImageView mImgUpdate;



    // mode crete new review or edit
    public static final String KEY_MODE_CREATE_OR_EDIT = "create or edit";

    String mReviewId;

    // mode value review or edit
    public static final int VALUE_MODE_EDIT = 2;

    int MODE_CRETE_OR_EDIT;

    // keys
    public static final String KEY_REVIEW_ID = "review_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_self_review);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            mReviewId =  bundle.getString(KEY_REVIEW_ID);
            MODE_CRETE_OR_EDIT =  bundle.getInt(KEY_MODE_CREATE_OR_EDIT);
        }

        init();
        prepareToolbar();
        setSelfReviewAdapterReport();

        // if mode is edit
        if (MODE_CRETE_OR_EDIT == VALUE_MODE_EDIT)
        {
            loadReviewDataAndSetViews();
        }
    }

    private void init()
    {
        mContext = this;
        mProgressBar = findViewById(R.id.progress_bar);
        mPermissionHelper = new PermissionHelper(mContext);
        mImgUpdate = findViewById(R.id.toolbar_img_options_menu);

       // mCommentsEdit = findViewById(R.id.comments_given_by_athlete_self);
        mAssistanceEdit = findViewById(R.id.assistance_edit);
        mStrengthsEdit = findViewById(R.id.strength_edit);
       // mSummaryEdit = findViewById(R.id.summary_edit);
        mImprovementsEdit = findViewById(R.id.improvement_edit);
        mSuggestionsEdit = findViewById(R.id.suggestion_edit);
        qualification_text = findViewById(R.id.qualification_text);


        mDate =  findViewById(R.id.created_date);
        mRole =  findViewById(R.id.role);

        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        mPresenter = new PresenterSelfReview(mContext,this);


       // mCommentMic = findViewById(R.id.img_mic);
        mStrengthMic = findViewById(R.id.img_mic_strength);
        mSuggestionMic = findViewById(R.id.img_mic_suggestion);
        mImprovementMic = findViewById(R.id.img_mic_improvement);
       // mSummaryMic = findViewById(R.id.img_mic_summary);
        mAssistanceMic = findViewById(R.id.img_mic_assistance);


        recyclerSelfReviewReport = findViewById(R.id.recycler_self_review_report);
        self_review_img_profile = findViewById(R.id.self_review_img_profile);
        self_review_txt_user_name = findViewById(R.id.self_review_txt_user_name);
        //submit = findViewById(R.id.submit);


        mImgUpdate.setOnClickListener(this);
       // mCommentMic.setOnClickListener(this);
        mStrengthMic.setOnClickListener(this);
        mSuggestionMic.setOnClickListener(this);
        mImprovementMic.setOnClickListener(this);
    //    mSummaryMic.setOnClickListener(this);
        mAssistanceMic.setOnClickListener(this);



        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        Glide.with(mContext)
                .load(SessionHelper.getInstance(mContext).getUser().getUserPicUrl())
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen,picDimen))
                .into(self_review_img_profile);

        self_review_txt_user_name.setText(SessionHelper.getInstance(mContext).getUser().getName());
        String roleName = RoleHelper.getInstance(mContext).getRoleNameById(SessionHelper.getInstance(mContext).getUser().getRoleId());
        String role = "Role" + " : "+ roleName;
        mRole.setText(role);
        mDate.setText(DateTimeHelper.getCurrentSystemDateTime());

    }

    public void setSelfReviewAdapterReport()
    {
        ArrayList<SelfReview> selfReportProgressList = new ArrayList<>();

        for (int i=0 ;i< 7; i++)
        {
            SelfReview selfReview = new SelfReview();
            selfReview.setSubject("");
            selfReportProgressList.add(selfReview);
        }

        adapter = new AdapterSelfReview(mContext,selfReportProgressList,MODE_CRETE_OR_EDIT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerSelfReviewReport.setLayoutManager(layoutManager);
        recyclerSelfReviewReport.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }




    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.self_review);

        // showing save icon
        mImgUpdate.setImageResource(R.drawable.ic_notes_save);
        mImgUpdate.setVisibility(View.VISIBLE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
    }

    @Override
    public void onSuccess(String msg) {
        Log.d("messages ",msg);
        DialogHelper.showSimpleCustomDialog(mContext, msg);
        mContext.startActivity(new Intent(mContext, ActivityEducationForAthlete.class));
          finish();
    }

    @Override
    public void onFailed(String msg) {
        Log.d("messages ",msg);
        DialogHelper.showSimpleCustomDialog(mContext, msg);
    }

    @Override
    public void onSelfListDeleted(String id) {

    }

    @Override
    public void onReceivedSelfReportList(List<SelfReview> selfReviewList) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.toolbar_img_options_menu :

                if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT)
                createSelfReview();

                else
                    editSelfReview();

                    break;


              /*  case R.id.img_mic :
                  requestPermissions(COMMENT_MIC);
                  break;*/


         /*   case R.id.img_mic_summary :
                requestPermissions(SUMMARY_MIC);

                break;
*/
            case R.id.img_mic_strength :
                requestPermissions(STRENGTHS_MIC);

                break;

            case R.id.img_mic_suggestion :
                requestPermissions(SUGGESTIONS_MIC);

                break;

            case R.id.img_mic_assistance :
                requestPermissions(ASSISTANCE_MIC);

                break;

            case R.id.img_mic_improvement :
                requestPermissions(IMPROVEMENTS_MIC);

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


    // creating new self review
    private void createSelfReview()
    {
        List<SelfReview> selfReviews = adapter.getReviewList();
        StringBuilder gradesBuffer = new StringBuilder();
        String[] subjects = new String[selfReviews.size()];

        for(int i = 0; i < selfReviews.size() ; i++)
        {
            SelfReview selfReview = selfReviews.get(i);
            subjects[i] = selfReview.getSubject();

        }

        String sub = Arrays.toString(subjects);

        for (int j=0; j<selfReviews.size(); j++)
        {
            if (j == selfReviews.size()-1)
            {

                SelfReview selfReview ;
                selfReview = selfReviews.get(j);
                gradesBuffer.append(selfReview.getGrade());
            }
            else
            {

                SelfReview selfReview ;
                selfReview = selfReviews.get(j);
                gradesBuffer.append(selfReview.getGrade());
                gradesBuffer.append(",");
            }
        }
        String gradeData = gradesBuffer.toString();

        Log.d(TAG, "Subject Listing   : " + sub);
        Log.d(TAG, "final Grades : " + gradeData);

        SelfReview selfReview = new SelfReview();
        selfReview.setSubject(sub);
        selfReview.setGrade(gradeData);
        selfReview.setAssistance(mAssistanceEdit.getText().toString().trim());
        selfReview.setSuggestions(mSuggestionsEdit.getText().toString().trim());
        selfReview.setStrengths(mStrengthsEdit.getText().toString().trim());
        selfReview.setImprovements(mImprovementsEdit.getText().toString().trim());
       // selfReview.setComments(mCommentsEdit.getText().toString().trim());
        selfReview.setQualification(qualification_text.getText().toString().trim());
        List<SelfReview> list1 = new ArrayList<>();
        list1.add(selfReview);


        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.saveSelfReview(list1);
    }



    // edit self review
    private void editSelfReview()
    {
        List<SelfReview> selfReviewList = adapter.getReviewList();
        StringBuilder gradesBuffer = new StringBuilder();
        String[] subjects = new String[selfReviewList.size()];

        for(int i = 0; i < selfReviewList.size() ; i++)
        {
            SelfReview selfReview = selfReviewList.get(i);
            subjects[i] = selfReview.getSubject();
            Log.d(TAG, "final Grades 1111 : " + selfReview.getSubjectID());

        }

        String sub = Arrays.toString(subjects);

        for (int j=0; j<selfReviewList.size(); j++)
        {
            if (j == selfReviewList.size()-1)
            {

                SelfReview selfReview ;
                selfReview = selfReviewList.get(j);
                gradesBuffer.append(selfReview.getGrade());
            }
            else
            {

                SelfReview selfReview ;
                selfReview = selfReviewList.get(j);
                gradesBuffer.append(selfReview.getGrade());
                gradesBuffer.append(",");
            }
        }
        String gradeData = gradesBuffer.toString();

        Log.d(TAG, "Subject Listing   : " + sub);
        Log.d(TAG, "final Grades : " + gradeData);

        SelfReview selfReview = new SelfReview();
        selfReview.setSubject(sub);
        selfReview.setGrade(gradeData);
        selfReview.setAssistance(mAssistanceEdit.getText().toString().trim());
        selfReview.setSuggestions(mSuggestionsEdit.getText().toString().trim());
        selfReview.setStrengths(mStrengthsEdit.getText().toString().trim());
        selfReview.setImprovements(mImprovementsEdit.getText().toString().trim());
        selfReview.setComments("test");
        selfReview.setQualification(qualification_text.getText().toString().trim());
        selfReview.setReviewId(mReviewId);

        /*List<SelfReview> list1 = new ArrayList<>();
        list1.add(selfReview);*/


        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.editSelfReview(selfReviewList,selfReview);
    }

    private void voiceToText(final int req)
    {
        Log.i(TAG,"voiceToText() method called ");
        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else
        {
            FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText)
                {
                   /* if (req == COMMENT_MIC)
                    {
                        mCommentsEdit.setText(spokenText);
                        mCommentsEdit.requestFocus();
                    }*/
                    if (req == STRENGTHS_MIC)
                    {
                        mStrengthsEdit.setText(spokenText);
                        mStrengthsEdit.requestFocus();
                    }
                   else if (req == SUGGESTIONS_MIC)
                    {
                         mSuggestionsEdit.setText(spokenText);
                         mSuggestionsEdit.requestFocus();
                    }
                    else if (req == IMPROVEMENTS_MIC)
                    {
                        mImprovementsEdit.setText(spokenText);
                        mImprovementsEdit.requestFocus();
                    }
                    else if (req == ASSISTANCE_MIC)
                    {
                        mAssistanceEdit.setText(spokenText);
                        mAssistanceEdit.requestFocus();
                    }
                  /*  if (req == SUMMARY_MIC)
                    {
                        mSummaryEdit.setText(spokenText);
                        mSummaryEdit.requestFocus();
                    }*/
                }

                @Override
                public void onError() {

              /*      comments_given_by_athlete_self.requestFocus();*/
                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");

        }

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




    public void loadReviewDataAndSetViews()
    {
        PresenterSelfReviewReport presenter = new PresenterSelfReviewReport(mContext, new ContractSelfReviewReport.View() {
            @Override
            public void getOnReceivedSelfReview(SelfReview selfReview, List<SelfReview> selfReviewList) {
                int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
                int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
                int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);

                Glide.with(mContext)
                        .load(selfReview.getUserPicUrl())
                        .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                        .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                        .apply(new RequestOptions().centerCrop())
                        .apply(new RequestOptions().override(picWidth,picHeight))
                        .into(self_review_img_profile);

                self_review_txt_user_name.setText(selfReview.getAthleteName());
                mAssistanceEdit.setText(selfReview.getAssistance());
                mSuggestionsEdit.setText(selfReview.getSuggestions());
                mStrengthsEdit.setText(selfReview.getStrengths());
                mImprovementsEdit.setText(selfReview.getImprovements());
                mDate.setText(DateTimeHelper.getDateTime(selfReview.getDate(),DateTimeHelper.FORMAT_DATE_TIME));
             //   mCommentsEdit.setText(selfReview.getComments());
                qualification_text.setText(selfReview.getQualification());

                adapter.updateSelfReviewList(selfReviewList);

             /*   selfReviewList = selfReviewListReport;

                adapter.updateList(selfReviewList);*/
            }

            @Override
            public void onFailed() {
                DialogHelper.showSimpleCustomDialog(mContext,"Failed");
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
        });

        presenter.getSelfReview(SessionHelper.getInstance(mContext).getUser().getUserId(),mReviewId);
    }
}
