package org.ctdworld.propath.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCreateSurvey;
import org.ctdworld.propath.activity.ActivityStatSurvey;
import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.activity.ActivitySurveyGroup;
import org.ctdworld.propath.activity.ActivitySurveyMultipleChoiceQuestionsPreview;
import org.ctdworld.propath.activity.ActivitySurveyPreviewAndSaveQuestions;
import org.ctdworld.propath.activity.ActivitySurveySettings;
import org.ctdworld.propath.activity.ActivitySurveyYesOrNoQuestionsPreview;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.contract.ContractStatSurvey;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;
import org.ctdworld.propath.presenter.PresenterStatSurvey;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterPastSurvey extends RecyclerView.Adapter<AdapterPastSurvey.MyViewHolder>  implements ContractSurvey.View, ContractStatSurvey.View{
    private static final String TAG = AdapterPastSurvey.class.getSimpleName();
    Context mContext;
/*
    private List<List<List<SurveyFreeResponse.SurveyQuestions>>> mSurveyList;
*/
     private static final String TYPE_FREE_RESPONSE = "Free Response";
     private static final String TYPE_MULTIPLE_CHOICE = "Multi-Choice";
     private static final String TYPE_YES_NO = "Yes-No";
     public static final String VIEW_DATA_KEY = "view_survey";



    Intent intent;
    private ProgressBar mProgressBar;
    private FragmentManager mFragmentManager;
    private ContractSurvey.Presenter mPresenter;
    private ContractStatSurvey.Presenter mPresenterStat;

    // survey lists
    private List<Survey.SurveyData.FreeResponse> mFreeResponseList ;
    private List<Survey.SurveyData.MultipleChoice> mMultipleChoiceList ;
    private List<Survey.SurveyData.YesNo> mYesNoList ;


    private ArrayList<Survey.SurveyData.CommonData> mSurveyList ;

    // constructor
    public AdapterPastSurvey(Context context, ProgressBar progressBar, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFreeResponseList = new ArrayList<>();
        this.mMultipleChoiceList = new ArrayList<>();
        this.mYesNoList = new ArrayList<>();
        mSurveyList = new ArrayList<>();
      //  this.mSurveyList = new ArrayList<>();

        this.mProgressBar = progressBar;
        this.mFragmentManager = fragmentManager;
        mPresenter = new PresenterCreateSurvey(mContext,this);
        mPresenterStat = new PresenterStatSurvey(mContext, this) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };

    }


    // update the survey
    public  void updateSurvey(Survey.SurveyData surveyData)
    {
        //  Log.d(TAG,"DESC : " + mSurveyCommonData.getSurveyTitle());
        //  this.mSurveyCommonData = surveyQuestions;
        if (surveyData != null)
        {

            mFreeResponseList = surveyData.getFreeResponseList();
            mMultipleChoiceList = surveyData.getMultipleChoiceList();
            mYesNoList = surveyData.getYesNoList();

            createCommonDataList();
            notifyDataSetChanged();

            // mSurveyList.add(mSurveyQuestions);
        }


    }

//
//    // update the survey
//    public  void updateSurvey(List <SurveyQusetions>surveyQusetions)
//    {
//        this.mSurveyList = surveyQusetions;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.survey_past_list_item, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final Survey.SurveyData.CommonData commonData = mSurveyList.get(holder.getAdapterPosition());

        setViews(holder.pastSurveyText, holder.pastHitsText, holder.show_type,holder.mImgOptionsMenu, commonData);

        holder.date.setText(DateTimeHelper.getDateTime(commonData.getDateTime(),DateTimeHelper.FORMAT_DATE_TIME));

        holder.survey_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (commonData.getQuestionType()) {
                    case TYPE_FREE_RESPONSE: {
                        Intent intent = new Intent(mContext, ActivitySurveyPreviewAndSaveQuestions.class);
                        intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        intent.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, getFreeResponseObject(commonData.getId()));
                        intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);

                        mContext.startActivity(intent);
                        break;
                    }

                    case TYPE_MULTIPLE_CHOICE: {
                        Intent intent = new Intent(mContext, ActivitySurveyMultipleChoiceQuestionsPreview.class);
                        intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        intent.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, getMultipleChoiceObject(commonData.getId()));
                        mContext.startActivity(intent);
                        break;
                    }


                    case TYPE_YES_NO: {
                        Intent intent = new Intent(mContext, ActivitySurveyYesOrNoQuestionsPreview.class);
                        intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                        intent.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, getYesNoObject(commonData.getId()));
                        mContext.startActivity(intent);
                        break;
                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        return mSurveyList.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder {

        View layout;
        TextView pastSurveyText, pastHitsText, show_type,date;
        ImageView mImgOptionsMenu;
        LinearLayout survey_layout;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            pastSurveyText = itemView.findViewById(R.id.pastSurveyText);
            pastHitsText = itemView.findViewById(R.id.hitsText);
            mImgOptionsMenu = itemView.findViewById(R.id.options);
            survey_layout = itemView.findViewById(R.id.survey_layout);
            show_type = itemView.findViewById(R.id.show_type);
            date = itemView.findViewById(R.id.date);
        }
    }


    private void setUpOptions(final Survey.SurveyData.CommonData commonData)
    {

        ArrayList<BottomSheetOption> bottomSheetOptions  = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_SHARE,"Share")
                .addOption(BottomSheetOption.OPTION_STATISTICS, "Statistics")
                .addOption(BottomSheetOption.OPTION_SETTING, "Settings")
                .addOption(BottomSheetOption.OPTION_EDIT, "Edit")
                .addOption(BottomSheetOption.OPTION_COPY, "Copy")
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete").build();

        Log.i(TAG,"bottom sheet options size = "+bottomSheetOptions.size());

        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
        {

            @Override
            public void onOptionSelected(int option)
            {
                Log.i(TAG,"bottom sheet options selected, option = "+option);
                switch (option)
                {
                    case BottomSheetOption.OPTION_SHARE:
                        Intent intent = new Intent(mContext, ActivitySurveyGroup.class);
                        intent.putExtra("survey_type",commonData.getQuestionType());
                        intent.putExtra("survey_id",commonData.getId());
                        mContext.startActivity(intent);

                        break;

                    case BottomSheetOption.OPTION_STATISTICS:
                        mPresenterStat.statSurvey(commonData.getId());
                        break;

                    case BottomSheetOption.OPTION_SETTING:

                        Intent intentSetting = new Intent(mContext, ActivitySurveySettings.class);
                        intentSetting.putExtra("survey_type",commonData.getQuestionType());
                        intentSetting.putExtra("survey_id",commonData.getId());
                        intentSetting.putExtra("survey_enable_disable", commonData.getStatus());
                        intentSetting.putExtra("survey_anonymous", commonData.getShowAnonymous());
                        mContext.startActivity(intentSetting);

                        break;

                    case BottomSheetOption.OPTION_EDIT:
                        switch (commonData.getQuestionType())
                        {
                            case TYPE_FREE_RESPONSE: {
                                Intent intentEdit = new Intent(mContext, ActivitySurveyPreviewAndSaveQuestions.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_FREE_RESPONSE, getFreeResponseObject(commonData.getId()));
                                mContext.startActivity(intentEdit);
                                break;
                            }
                            case TYPE_MULTIPLE_CHOICE: {
                                Intent intentEdit = new Intent(mContext, ActivitySurveyMultipleChoiceQuestionsPreview.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_MULTIPLE_CHOICE, getMultipleChoiceObject(commonData.getId()));
                                mContext.startActivity(intentEdit);
                                break;
                            }
                            case TYPE_YES_NO: {
                                Intent intentEdit = new Intent(mContext, ActivitySurveyYesOrNoQuestionsPreview.class);
                                intentEdit.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, ActivityCreateSurvey.SURVEY_TYPE_EDIT_SERVER);
                                intentEdit.putExtra(ActivityCreateSurvey.SURVEY_YES_NO, getYesNoObject(commonData.getId()));
                                mContext.startActivity(intentEdit);
                                break;
                            }
                        }

                        break;


                    case BottomSheetOption.OPTION_COPY:
                        DialogEditText dialogEditText = DialogEditText.getInstance("Copy Survey", "Enter Title", "Save", false);
                        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                            @Override
                            public void onButtonClicked(String enteredValue) {
                                mProgressBar.setVisibility(View.VISIBLE);

                                if (enteredValue.equals(""))
                                {
                                    DialogHelper.showSimpleCustomDialog(mContext,"Plz fill the title");
                                }
                                else
                                {
                                    switch (commonData.getQuestionType()) {
                                        case TYPE_FREE_RESPONSE: {
                                            mPresenter.copySurvey(commonData.getId(), enteredValue, "single");
                                            break;
                                        }
                                        case TYPE_MULTIPLE_CHOICE: {
                                            mPresenter.copySurvey(commonData.getId(), enteredValue, "multiple");
                                            break;
                                        }
                                        case TYPE_YES_NO: {
                                            mPresenter.copySurvey(commonData.getId(), enteredValue, "yes_no");
                                            break;
                                        }
                                    }
                                }

                            }
                        });
                        dialogEditText.show(mFragmentManager, ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                        break;

                        case BottomSheetOption.OPTION_DELETE:
                           DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this survey ?",
                            new DialogHelper.ShowDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                //    mPresenter.deleteSurvey(commonData.getId(),"yes_no");

                                    switch (commonData.getQuestionType()) {
                                        case TYPE_FREE_RESPONSE: {
                                            mPresenter.deleteSurvey(commonData.getId(),"single");
                                            mContext.startActivity(new Intent(mContext, ActivitySurvey.class));                                            break;
                                        }
                                        case TYPE_MULTIPLE_CHOICE: {
                                            mPresenter.deleteSurvey(commonData.getId(),"multiple");
                                            mContext.startActivity(new Intent(mContext, ActivitySurvey.class));
                                            break;
                                        }
                                        case TYPE_YES_NO: {
                                            mPresenter.deleteSurvey(commonData.getId(),"yes_no");
                                            mContext.startActivity(new Intent(mContext, ActivitySurvey.class));
                                            break;
                                        }
                                    }
                                  /*  mPresenter.deleteSurvey(commonData.getId());
                                    mContext.startActivity(new Intent(mContext, ActivitySurvey.class));*/
                                }

                                @Override
                                public void onCancelClicked() {

                                }
                            });

                    break;
                }
            }
        });
        options.show(mFragmentManager, "options");

    }





    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {

    }

    @Override
    public void onGetReceivedStatExcel(String data) {
        Log.d("onGetReceivedStatExcel", data);
        Intent intent = new Intent(mContext, ActivityStatSurvey.class);
        intent.putExtra(ActivityStatSurvey.KEY_TOOLBAR,data);

        String driveLinkDocViewer = "http://drive.google.com/viewerng/viewer?embedded=true&url=";
        String finalUrl = driveLinkDocViewer + data;
        Log.i("finalFileUrl = ",finalUrl);
        intent.putExtra(ActivityStatSurvey.KEY_WEB_URL,finalUrl);

        mContext.startActivity(intent);
    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext, msg, new DialogHelper.ShowSimpleDialogListener() {
            @Override
            public void onOkClicked() {
                mContext.startActivity(new Intent(mContext,ActivitySurvey.class));

            }
        });
        ((Activity)mContext).finish();

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


    private void setViews(TextView surveyTitle, TextView hitCount, TextView anonymousName, ImageView optionsImg, final Survey.SurveyData.CommonData commonData)
    {
        surveyTitle.setText(commonData.getTitle());
        hitCount.setText(commonData.getHitCount());

        if (commonData.getShowAnonymous().equals("0")) {
            anonymousName.setText(R.string.txt_anonymous);
        }
        else {
            anonymousName.setText(SessionHelper.getInstance(mContext).getUser().getName());
        }


        optionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpOptions(commonData);

            }
        });

    }







    private void createCommonDataList()
    {
        for (int i = 0; i < mFreeResponseList.size(); i++)
        {
            Survey.SurveyData.CommonData commonData = new Survey.SurveyData.CommonData();
            commonData.setTitle(mFreeResponseList.get(i).getTitle());
            commonData.setHitCount(mFreeResponseList.get(i).getHitCount());
            commonData.setShowAnonymous(mFreeResponseList.get(i).getShowAnonymous());
            commonData.setStatus(mFreeResponseList.get(i).getStatus());
            commonData.setQuestionType(mFreeResponseList.get(i).getQuestionType());
            commonData.setId(mFreeResponseList.get(i).getId());
            commonData.setDescription(mFreeResponseList.get(i).getDescription());
            commonData.setDateTime(mFreeResponseList.get(i).getDateTime());

            mSurveyList.add(commonData);
        }
        for (int i = 0; i < mMultipleChoiceList.size(); i++)
        {
            Survey.SurveyData.CommonData commonData = new Survey.SurveyData.CommonData();
            commonData.setTitle(mMultipleChoiceList.get(i).getTitle());
            commonData.setHitCount(mMultipleChoiceList.get(i).getHitCount());
            commonData.setShowAnonymous(mMultipleChoiceList.get(i).getShowAnonymous());
            commonData.setQuestionType(mMultipleChoiceList.get(i).getQuestionType());
            commonData.setId(mMultipleChoiceList.get(i).getId());
            commonData.setStatus(mMultipleChoiceList.get(i).getStatus());
            commonData.setDescription(mMultipleChoiceList.get(i).getDescription());
            commonData.setDateTime(mMultipleChoiceList.get(i).getDateTime());

            mSurveyList.add(commonData);
        }

        for (int i = 0; i < mYesNoList.size(); i++)
        {
            Survey.SurveyData.CommonData commonData = new Survey.SurveyData.CommonData();
            commonData.setTitle(mYesNoList.get(i).getTitle());
            commonData.setHitCount(mYesNoList.get(i).getHitCount());
            commonData.setShowAnonymous(mYesNoList.get(i).getShowAnonymous());
            commonData.setQuestionType(mYesNoList.get(i).getQuestionType());
            commonData.setId(mYesNoList.get(i).getId());
            commonData.setStatus(mYesNoList.get(i).getStatus());
            commonData.setDescription(mYesNoList.get(i).getDescription());
            commonData.setDateTime(mYesNoList.get(i).getDateTime());

            mSurveyList.add(commonData);

        }
    }




    private Survey.SurveyData.FreeResponse getFreeResponseObject(String surveyID)
    {

        for (int i = 0; i <mFreeResponseList.size(); i++)
        {
              Survey.SurveyData.FreeResponse freeResponse = mFreeResponseList.get(i);
              if (surveyID.equals(freeResponse.getId()))
              return freeResponse;
        }
        return null;
    }


    private Survey.SurveyData.MultipleChoice getMultipleChoiceObject(String surveyID)
    {

        for (int i = 0; i <mMultipleChoiceList.size(); i++)
        {
            Survey.SurveyData.MultipleChoice multipleChoice = mMultipleChoiceList.get(i);
            if (surveyID.equals(multipleChoice.getId()))
                return multipleChoice;
        }
        return null;
    }

    private Survey.SurveyData.YesNo getYesNoObject(String surveyID)
    {

        for (int i = 0; i <mYesNoList.size(); i++)
        {
            Survey.SurveyData.YesNo yesNo = mYesNoList.get(i);
            if (surveyID.equals(yesNo.getId()))
                return yesNo;
        }
        return null;
    }

}