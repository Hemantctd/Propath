package org.ctdworld.propath.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractAddCoachFeedback;
import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;

import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterAddCoachFeedback;
import org.ctdworld.propath.presenter.PresenterCoachFeedback;

import java.util.List;

public class ActivityCoachFeedbackCoachView extends AppCompatActivity implements View.OnClickListener, ContractAddCoachFeedback.View , ContractCoachFeedback.View{
    private static final String TAG = ActivityCoachFeedbackCoachView.class.getSimpleName();


    private final int REQ_CODE_STRENGTH = 102;
    private final int REQ_CODE_SUGGESTION = 103;
    private final int REQ_CODE_ASSISTANCE = 104;
    private final int REQ_CODE_EVENT = 105;
    private final int REQ_CODE_IMPROVEMENTS= 107;


    Context mContext;
    Toolbar mToolbar;
    TextView mTxtToolbarTitle;


    String mAthleteId,mReviewId,athlete_name,athlete_image;
    PermissionHelper mPermissionHelper;
    ProgressBar mProgressBar;
    ContractAddCoachFeedback.Presenter mPresenter;
    ContractCoachFeedback.Presenter mPresenters;

    EditText mEventsEdit;
    EditText mStrengthsEdit;
    EditText mImprovementsEdit;
    EditText mSuggestionEdit;
    EditText mAssistanceEdit;

    ImageView mImgUpdate;
    ImageView mEventMic;
    ImageView mSrengthMic;
    ImageView mSuggestionMic;
    ImageView mAssistanceMic;
    ImageView mImprovementsMic;




    // mode crete new review or edit
    public static final String KEY_MODE_CREATE_OR_EDIT = "create or edit";
    // mode value review or edit
   // public static final int VALUE_MODE_CREATE = 1;
    public static final int VALUE_MODE_EDIT = 2;

    int MODE_CRETE_OR_EDIT;

    // keys
    public static final String KEY_REVIEW_ID = "review_id";
    public static final String KEY_ATHLETE_ID = "athlete_id";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_feedback_coach_view);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {

                mAthleteId =  bundle.getString(KEY_ATHLETE_ID);
                mReviewId =  bundle.getString(KEY_REVIEW_ID);
                MODE_CRETE_OR_EDIT =  bundle.getInt(KEY_MODE_CREATE_OR_EDIT);
                athlete_name = bundle.getString("athlete_name");
                athlete_image = bundle.getString("athlete_image");


        }

        init();
        prepareToolbar();
        setFragmentDataValue();




        // if mode is edit
        if (MODE_CRETE_OR_EDIT == VALUE_MODE_EDIT)
        {
            loadReviewDataAndSetViews();

        }


    }

    private void setFragmentDataValue() {



        CoachData coachData = new CoachData();
        coachData.setAthleteName(athlete_name);
        coachData.setAthleteImg(athlete_image);

        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails(coachData));
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }




    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(CoachData coachData)
    {
        if (coachData == null )
            return null;

        String roleId = SessionHelper.getInstance(mContext).getUser().getRoleId();

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(coachData.getAthleteName());
        createdDataDetails.setRoleId(roleId);
        createdDataDetails.setUserPicUrl(coachData.getAthleteImg());
        createdDataDetails.setCreatedDate(DateTimeHelper.getCurrentSystemDateTime());
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }



    private void init()
    {
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        mPermissionHelper = new PermissionHelper(mContext);
        mPresenters = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mPresenter = new PresenterAddCoachFeedback(mContext) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mProgressBar = findViewById(R.id.progress_bar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);
        /*mRole = findViewById(R.id.role);
        mCreatedDate = findViewById(R.id.createdDate);
*/

        mEventsEdit = findViewById(R.id.event_game);
      //  mWorkonsEdit = findViewById(R.id.workons);
        mStrengthsEdit = findViewById(R.id.strengths);
        mSuggestionEdit = findViewById(R.id.suggestion_edit);
       // mCommentsEdit = findViewById(R.id.comment);
        mAssistanceEdit = findViewById(R.id.assistance_edit);
        mImprovementsEdit = findViewById(R.id.improvements_edit);

        mImgUpdate = findViewById(R.id.toolbar_img_options_menu);
      /*  coach_feedback_img_profile = findViewById(R.id.coach_feedback_img_profile);
        coach_feedback_txt_user_name = findViewById(R.id.coach_feedback_txt_user_name);*/
       // submit_feedback = findViewById(R.id.submit_feedback);


       // mWorkonsMic = findViewById(R.id.workons_voice);
        mSrengthMic = findViewById(R.id.strengths_voice);
        mEventMic = findViewById(R.id.event_game_voice);
     //   mCommentMic = findViewById(R.id.comment_voice);
        mSuggestionMic = findViewById(R.id.suggestion_mic);
        mImprovementsMic = findViewById(R.id.improvents_mic);
        mAssistanceMic = findViewById(R.id.assistance_mic);



        mEventMic.setOnClickListener(this);
        mSrengthMic.setOnClickListener(this);
      //  mWorkonsMic.setOnClickListener(this);
      //  submit_feedback.setOnClickListener(this);
        mImprovementsMic.setOnClickListener(this);
        mAssistanceMic.setOnClickListener(this);
        mSuggestionMic.setOnClickListener(this);
        mImgUpdate.setOnClickListener(this);


    }


    private void prepareToolbar()
    {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.coach_feedback_title);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.toolbar_img_options_menu :

                mProgressBar.setVisibility(View.VISIBLE);
                if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT)
                    mPresenter.requestToGiveFeedback(addCoachFeedback());

                else
                  mPresenters.editCoachFeedback(editCoachFeedback());

                    break;


            case R.id.event_game_voice :
                permissionRequest(REQ_CODE_EVENT);
                break;
            case R.id.strengths_voice :
                permissionRequest(REQ_CODE_STRENGTH);
                break;
            /*case R.id.workons_voice :
                permissionRequest(REQ_CODE_WORKONS);
                break;*/

            case R.id.suggestion_mic :
                permissionRequest(REQ_CODE_SUGGESTION);
                break;

            case R.id.improvents_mic :
                permissionRequest(REQ_CODE_IMPROVEMENTS);
                break;

            case R.id.assistance_mic :
                permissionRequest(REQ_CODE_ASSISTANCE);
                break;

//            case R.id.comment_voice :
//                permissionRequest(REQ_CODE_COMMENT);
//                break;
        }
    }


    private CoachData addCoachFeedback()
    {
        CoachData coachData = new CoachData();
        coachData.setEvents(mEventsEdit.getText().toString().trim());
        coachData.setWorkons("test");
        coachData.setStrenths(mStrengthsEdit.getText().toString().trim());
        coachData.setAthleteID(mAthleteId);
        coachData.setImprovements(mImprovementsEdit.getText().toString().trim());
        coachData.setAssistanceOffered(mAssistanceEdit.getText().toString().trim());
        coachData.setSuggestions(mSuggestionEdit.getText().toString().trim());
        coachData.setAssistanceRequired("");

        return coachData;
    }


    private CoachData editCoachFeedback()
    {
        CoachData coachData = new CoachData();
        coachData.setEvents(mEventsEdit.getText().toString().trim());
        coachData.setWorkons("test");
        coachData.setStrenths(mStrengthsEdit.getText().toString().trim());
        coachData.setAthleteID(mAthleteId);
        coachData.setReviewID(mReviewId);
        coachData.setImprovements(mImprovementsEdit.getText().toString().trim());
        coachData.setAssistanceOffered(mAssistanceEdit.getText().toString().trim());
        coachData.setSuggestions(mSuggestionEdit.getText().toString().trim());
        coachData.setAssistanceRequired("");

        return coachData;
    }

    public void permissionRequest(int req)
    {
        if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            voiceToText(req);
        else
            mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
    }

    private void voiceToText(final int request)
    {
        Log.i(TAG,"voiceToText() method called " + request);
        FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();

        PermissionHelper permissionHelper = new PermissionHelper(mContext);
        if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
        else
        {
            fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                @Override
                public void onReceiveText(String spokenText)
                {
                    if(request == REQ_CODE_EVENT)
                    {
                        mEventsEdit.setText(spokenText);
                        mEventsEdit.requestFocus();
                    }
                    else if (request == REQ_CODE_STRENGTH)
                    {
                        mStrengthsEdit.setText(spokenText);
                        mStrengthsEdit.requestFocus();

                    }
                   /* else if (request == REQ_CODE_WORKONS)
                    {
                        mWorkonsEdit.setText(spokenText);
                        mWorkonsEdit.requestFocus();

                    }*/

                  /*  else if (request == REQ_CODE_COMMENT)
                    {
                        mCommentsEdit.setText(spokenText);
                        mCommentsEdit.requestFocus();

                    }*/

                    else if (request == REQ_CODE_SUGGESTION)
                    {
                        mSuggestionEdit.setText(spokenText);
                        mSuggestionEdit.requestFocus();

                    }
                    else if (request == REQ_CODE_IMPROVEMENTS)
                    {
                        mImprovementsEdit.setText(spokenText);
                        mImprovementsEdit.requestFocus();

                    }
                    else if (request == REQ_CODE_ASSISTANCE)
                    {
                        mAssistanceEdit.setText(spokenText);
                        mAssistanceEdit.requestFocus();

                    }

                }

                @Override
                public void onError() {
                   /* if(request == 101)
                    {
                        event_game.requestFocus();

                    }
                    else if (request == 102)
                    {
                        strengths.requestFocus();

                    }
                    else if (request == 103)
                    {
                        workons.requestFocus();

                    }*/
                }
            });
            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");
        }


    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
        mEventsEdit.setText("");
        mStrengthsEdit.setText("");
       // mWorkonsEdit.setText("");

        startActivity(new Intent(mContext,ActivityCoachFeedback.class));
        finish();

    }

    @Override
    public void onCoachFeedbackDeleted(String id) {

    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachData) {

    }

    @Override
    public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

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
        mProgressBar.setVisibility(View.VISIBLE);


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

    private void loadReviewDataAndSetViews()
    {
        PresenterCoachFeedback mPresenter = new PresenterCoachFeedback(mContext, new ContractCoachFeedback.View() {
            @Override
            public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

            }

            @Override
            public void onReceivedCoachFeedbackDescription(CoachData coachData) {


                FragCreatingDetails fragment = (FragCreatingDetails) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS);
                if (fragment != null) {
                    CreatedDataDetails createdDataDetails = getCreatedDataDetails(coachData);
                    createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
                    createdDataDetails.setName(coachData.getAthleteName());
                    createdDataDetails.setUserPicUrl(coachData.getAthleteImg());
                    fragment.setViews(createdDataDetails);
                }
                mEventsEdit.setText(coachData.getEvents());
                mStrengthsEdit.setText(coachData.getStrenths());
                mAssistanceEdit.setText(coachData.getAssistanceOffered());
                mImprovementsEdit.setText(coachData.getImprovements());
                mSuggestionEdit.setText(coachData.getSuggestions());


            }

            @Override
            public void onFailed() {
                DialogHelper.showSimpleCustomDialog(mContext, "Failed");
            }

            @Override
            public void onSuccess(String msg) {
                DialogHelper.showSimpleCustomDialog(mContext, msg);

            }

            @Override
            public void onCoachFeedbackDeleted(String id) {

            }

            @Override
            public void onReceivedCoachSelfReview(List<CoachData> coachData) {

            }

            @Override
            public void onReceivedCoachSelfReviewDescription(CoachData coachData) {

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

        mPresenter.getCoachFeedbackDescription(mAthleteId, mReviewId);

    }
}
