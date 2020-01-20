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
import org.ctdworld.propath.presenter.PresenterCoachFeedback;

import java.util.List;


public class ActivityCoachCreateSelfReview extends AppCompatActivity implements View.OnClickListener, ContractCoachFeedback.View {


    private static final String TAG = ActivityEducationSelfReviewCreate.class.getSimpleName();

   // private static final int COMMENTS_MIC = 1;
    private static final int EVENTS_MATCH_MIC = 2;
    private static final int STRENGTHS_MIC = 3;
    private static final int IMPROVEMENTS_MIC = 4;
    private static final int SUGGESTION_MIC = 5;
    private static final int ASSISTANCE_MIC = 6;
    // mode crete new review or edit
    public static final String KEY_MODE_CREATE_OR_EDIT = "create or edit";
    // keys
    public static final String KEY_REVIEW_ID = "review_id";
    public static final String KEY_ATHLETE_ID = "athlete_id";
    // mode value review or edit
    public static final int VALUE_MODE_EDIT = 2;



    Toolbar mToolbar;
    TextView mTxtToolbarTitle;
    Context mContext;
    ImageView mImgSuggestionMic;
    ImageView mImgEventMic;
    ImageView mImgStrengthsMic;
    ImageView mImgImprovementseMic;
    ImageView mImgAssistanceMic;
    EditText mEventEdit;
    EditText mStrengthsEdit;
    EditText mImprovementsEdit;
    EditText mSuggestionEdit;
    EditText mAssistanceEdit;
    ProgressBar mProgressBar;
    PermissionHelper mPermissionHelper;
    ContractCoachFeedback.Presenter mPresenter;
    String mAthleteId;
    String mReviewId;
    int MODE_CRETE_OR_EDIT;
    private ImageView mImgUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_create_self_review);


        init();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            mAthleteId =  bundle.getString(KEY_ATHLETE_ID);
            mReviewId =  bundle.getString(KEY_REVIEW_ID);
            MODE_CRETE_OR_EDIT =  bundle.getInt(KEY_MODE_CREATE_OR_EDIT);

        }

        prepareToolbar();
        setFragmentDataValue();

        // if mode is edit
        if (MODE_CRETE_OR_EDIT == VALUE_MODE_EDIT)
        {
            loadReviewDataAndSetViews();

        }



    }









    // for initialization
    private void init()
    {
        mContext = this;
        mPermissionHelper = new PermissionHelper(mContext);
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mProgressBar = findViewById(R.id.progress_bar);
        //Text View initialization
        mToolbar = findViewById(R.id.toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbar_txt_title);

        mImgUpdate = findViewById(R.id.toolbar_img_options_menu);

        //Edit Text initialization
        mEventEdit = findViewById(R.id.event_edit);
        mImprovementsEdit = findViewById(R.id.improvements_edit);
        mStrengthsEdit = findViewById(R.id.strengths_edit);
        mSuggestionEdit = findViewById(R.id.suggestion_edit);
        mAssistanceEdit = findViewById(R.id.assistance_edit);

        //Image View and Button initialization
        mImgImprovementseMic = findViewById(R.id.img_mic_improvements);
        mImgSuggestionMic = findViewById(R.id.img_mic_suggestion);
        mImgAssistanceMic = findViewById(R.id.img_mic_assistance);
        mImgStrengthsMic = findViewById(R.id.img_mic_strength);
        mImgEventMic = findViewById(R.id.img_mic_event);

        //Image View Listeners
        mImgStrengthsMic.setOnClickListener(this);
        mImgAssistanceMic.setOnClickListener(this);
        mImgImprovementseMic.setOnClickListener(this);
        mImgEventMic.setOnClickListener(this);
        mImgSuggestionMic.setOnClickListener(this);
        mImgUpdate.setOnClickListener(this);
    }


    private void setFragmentDataValue() {



      /*  mRoleName.setText(String.format("Role : %s", roleName));

        mCreatedDate.setText(DateTimeHelper.getCurrentSystemDateTime());
*/


        CoachData coachData = new CoachData();
        coachData.setUserName(SessionHelper.getInstance(mContext).getUser().getName());
        coachData.setProfileImage(SessionHelper.getInstance(mContext).getUser().getUserPicUrl());


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
        createdDataDetails.setName(coachData.getUserName());
        createdDataDetails.setRoleId(roleId);
        createdDataDetails.setUserPicUrl(coachData.getProfileImage());
        createdDataDetails.setCreatedDate(DateTimeHelper.getCurrentSystemDateTime());
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }


    //setting toolbar
    private void prepareToolbar() {
        setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText(R.string.self_review);
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
        return super.onOptionsItemSelected(item);
    }









    // click listener method
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
        /*    case R.id.img_mic:
                requestPermission(COMMENTS_MIC);
                break;*/
            case R.id.img_mic_improvements:
                requestPermission(IMPROVEMENTS_MIC);
                break;

            case R.id.img_mic_suggestion:
                requestPermission(SUGGESTION_MIC);

                break;

            case R.id.img_mic_assistance:
                requestPermission(ASSISTANCE_MIC);

                break;

            case R.id.img_mic_strength:
                requestPermission(STRENGTHS_MIC);

                break;


            case R.id.img_mic_event:
                requestPermission(EVENTS_MATCH_MIC);

                break;


            case R.id.toolbar_img_options_menu:

                if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT) {
                    onShowProgress();
                    mPresenter.createCoachSelfReview(addCoachSelfReviewFeedback());
                }
                else
                {
                    onShowProgress();
                    mPresenter.editCoachSelfReviewFeedback(editCoachSelfReviewFeedback());
                }
                break;
        }




    }



    private CoachData addCoachSelfReviewFeedback()
    {
        CoachData coachData = new CoachData();
        coachData.setEvents(mEventEdit.getText().toString().trim());
        coachData.setStrenths(mStrengthsEdit.getText().toString().trim());
        coachData.setImprovements(mImprovementsEdit.getText().toString().trim());
        coachData.setSuggestions(mSuggestionEdit.getText().toString().trim());
        coachData.setAssistanceRequired(mAssistanceEdit.getText().toString().trim());

        return coachData;
    }


    private CoachData editCoachSelfReviewFeedback()
    {
        CoachData coachData = new CoachData();
        coachData.setEvents(mEventEdit.getText().toString().trim());
        coachData.setStrenths(mStrengthsEdit.getText().toString().trim());
        coachData.setImprovements(mImprovementsEdit.getText().toString().trim());
        coachData.setSuggestions(mSuggestionEdit.getText().toString().trim());
        coachData.setAssistanceRequired(mAssistanceEdit.getText().toString().trim());
        coachData.setReviewID(mReviewId);

        return coachData;
    }

    // to request permission for record audio
    private void requestPermission(int micClick)
    {
        if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            voiceToText(micClick);
        else
            mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
    }










    // for voice to text conversion and set into the edit text
    private void voiceToText(final int getMicClickOptions)
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
                    if (getMicClickOptions == STRENGTHS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mStrengthsEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mStrengthsEdit.setText(spokenText);
                        mStrengthsEdit.requestFocus();
                    }
                    else if (getMicClickOptions == EVENTS_MATCH_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mEventEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mEventEdit.setText(spokenText);
                        mEventEdit.requestFocus();
                    }
                    else if (getMicClickOptions == IMPROVEMENTS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mImprovementsEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mImprovementsEdit.setText(spokenText);
                        mImprovementsEdit.requestFocus();
                    }
                    else if (getMicClickOptions == SUGGESTION_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mSuggestionEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");
                        mSuggestionEdit.setText(spokenText);
                        mSuggestionEdit.requestFocus();
                    }
                    else if (getMicClickOptions == ASSISTANCE_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mAssistanceEdit.getText().toString().trim());
                        sb.append(spokenText).append(" ");

                        mAssistanceEdit.setText(spokenText);
                        mAssistanceEdit.requestFocus();
                    }
                  /*  else if (getMicClickOptions == COMMENTS_MIC)
                    {
                        StringBuilder sb = new StringBuilder(mCommentsEdit.getText().toString().trim());
                        sb.append(spokenText+ " ");

                        mCommentsEdit.setText(sb);
                        mCommentsEdit.requestFocus();
                    }*/


                }

                @Override
                public void onError() {

                }
            });

            fragmentSpeechRecognition.show(getSupportFragmentManager(),"");

        }

    }

    @Override
    public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

    }

    @Override
    public void onReceivedCoachFeedbackDescription(CoachData coachData) {

    }

    @Override
    public void onFailed() {
        DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
    }

    @Override
    public void onSuccess(String msg) {
        onHideProgress();
        DialogHelper.showSimpleCustomDialog(mContext, msg, new DialogHelper.ShowSimpleDialogListener() {
            @Override
            public void onOkClicked() {
                Intent intent = new Intent(mContext,ActivityCoachFeedback.class);
                intent.putExtra(ActivityCoachFeedback.KEY_IS_FRAGMENT_SINGLE, false);
                intent.putExtra(ActivityCoachFeedback.KEY_FRAGMENT_TYPE, ActivityCoachFeedback.FRAGMENT_COACH_VIEW_FEEDBACK);
                startActivity(intent);
                finish();
            }
        });


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
        DialogHelper.showSimpleCustomDialog(mContext,message);

    }

   /* // setting data to its object
    public CreatedDataDetails getCreatedDataDetails(CoachData coachData)
    {
        if (coachData == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(coachData.getUserName());
        createdDataDetails.setRoleId(coachData.getAthleteID());
        createdDataDetails.setRole("Athlete");
        createdDataDetails.setUserPicUrl(coachData.getProfileImage());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }
*/
    private void loadReviewDataAndSetViews()
    {
        PresenterCoachFeedback presenter = new PresenterCoachFeedback(mContext, new ContractCoachFeedback.View() {
            @Override
            public void onReceivedCoachFeedbackData(List<CoachData> coachDataList) {

            }

            @Override
            public void onReceivedCoachFeedbackDescription(CoachData coachData) {

            }

            @Override
            public void onFailed() {
                DialogHelper.showSimpleCustomDialog(mContext, "Failed");
            }

            @Override
            public void onSuccess(String msg) {

            }

            @Override
            public void onCoachFeedbackDeleted(String id) {

            }

            @Override
            public void onReceivedCoachSelfReview(List<CoachData> coachData) {

            }

            @Override
            public void onReceivedCoachSelfReviewDescription(CoachData coachData) {
                onHideProgress();

                FragCreatingDetails fragment = (FragCreatingDetails) getSupportFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS);
                if (fragment != null) {
                    CreatedDataDetails createdDataDetails = getCreatedDataDetails(coachData);
                    createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(coachData.getDate(), DateTimeHelper.FORMAT_DATE_TIME));
                    createdDataDetails.setName(coachData.getUserName());
                    createdDataDetails.setUserPicUrl(coachData.getProfileImage());
                    fragment.setViews(createdDataDetails);
                }


                mAssistanceEdit.setText(coachData.getAssistanceRequired());
                mStrengthsEdit.setText(coachData.getStrenths());
                mImprovementsEdit.setText(coachData.getImprovements());
                mSuggestionEdit.setText(coachData.getSuggestions());
                mEventEdit.setText(coachData.getEvents());

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
                DialogHelper.showSimpleCustomDialog(mContext, message);

            }
        }) {
            @Override
            public void onSuccess() {

            }
        };


        onShowProgress();
        presenter.getCoachSelfReviewDescription(mAthleteId, mReviewId);
    }
}
