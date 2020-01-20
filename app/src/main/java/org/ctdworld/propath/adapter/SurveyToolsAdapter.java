package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityStatSurvey;
import org.ctdworld.propath.activity.ActivitySurvey;
import org.ctdworld.propath.activity.ActivitySurveyGroup;
import org.ctdworld.propath.activity.ActivitySurveySettings;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.contract.ContractStatSurvey;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;
import org.ctdworld.propath.presenter.PresenterStatSurvey;

import java.util.ArrayList;
import java.util.List;

public abstract class SurveyToolsAdapter extends BaseAdapter implements ContractSurvey.View, ContractStatSurvey.View {

    Context mContext;
    ArrayList<Integer> tools = new ArrayList<>();
    LayoutInflater inflater;
    String msurvey_id;
    AppCompatActivity mappCompatActivity;
    ContractSurvey.Presenter mPresenter;
    List<List<SurveyQusetions>> mListObject;
    ProgressBar mProgressBar;
    ContractStatSurvey.Presenter mPresenterStat;

    public  SurveyToolsAdapter(AppCompatActivity appCompatActivity, Context context, ArrayList<Integer> tools, List<List<SurveyQusetions>> listObject, ProgressBar progressBar)
    {

        this.mappCompatActivity = appCompatActivity;
        this.mContext = context;
        this.tools = tools;
        this.mListObject = listObject;
        this.mProgressBar = progressBar;
        inflater = (LayoutInflater.from(context));
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
    @Override
    public int getCount() {
        Log.d("count image : " , String.valueOf(tools.size()));
        return tools.size() ;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        view = inflater.inflate(R.layout.recycler_spin_tools_images, null);
       ImageView icon = (ImageView) view.findViewById(R.id.tools_images);
       Integer toolsItem = tools.get(i);
       icon.setImageResource(toolsItem);

        final List<SurveyQusetions> listCommonData = mListObject.get(0);
        final SurveyQusetions surveyCommon = listCommonData.get(0);


        final List<SurveyQusetions> listQuestions = mListObject.get(1);
        Log.i("listQuestions  ", String.valueOf(listQuestions.size()));

           icon.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   // if position is 0
                   if (i == 1) {
                       Intent intent = new Intent(mContext, ActivitySurveyGroup.class);
                       intent.putExtra("survey_id",surveyCommon.getSurvey_id());
                       mContext.startActivity(intent);
                   }
                   if (i == 2) {

                       mPresenterStat.statSurvey(surveyCommon.getSurvey_id());

                   }
                   if (i == 3) {
                       Intent intent = new Intent(mContext, ActivitySurveySettings.class);
                       intent.putExtra("survey_id",surveyCommon.getSurvey_id());
                       intent.putExtra("survey_enable_disable", surveyCommon.getSurvey_enable_type());
                       intent.putExtra("survey_anonymous", surveyCommon.getSurvey_anonymous_type());
                       mContext.startActivity(intent);
                   }

                   if (i == 4)
                   {
                      /* Intent intent = new Intent(mContext, ActivityCreateSurvey.class);
                       intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT,"edit");
                       intent.putExtra(ActivityCreateSurvey.SURVEY_TITLE,surveyCommon.getSurveyTitle());
                       intent.putExtra(ActivityCreateSurvey.SURVEY_QUESTION_TYPE,surveyCommon.getSurveyQusetionType());
                       intent.putExtra(ActivityCreateSurvey.SURVEY_NO_OF_QUESTIONS,surveyCommon.getSurvey_no_of_question());
                       intent.putExtra(ActivityCreateSurvey.SURVEY_DESCRIPTION,surveyCommon.getSurveyDesc());
                       intent.putExtra(ActivityCreateSurvey.SURVEY_ID,surveyCommon.getSurvey_id());
                       intent.putParcelableArrayListExtra(ActivityCreateSurvey.SURVEY_QUESTION_LIST, (ArrayList<? extends Parcelable>) listQuestions);                       mContext.startActivity(intent);
                       mContext.startActivity(intent);
*/
                   }

                   if (i == 5)
                   {
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
                                   //mPresenter.copySurvey(surveyCommon.getSurvey_id(),enteredValue);
                               }

                           }
                       });
                       dialogEditText.show(mappCompatActivity.getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                   }
                   // if position is 5

                   if(i == 6)
                   {
                       DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this survey ?",
                                new DialogHelper.ShowDialogListener() {
                                   @Override
                                   public void onOkClicked() {
                                       mProgressBar.setVisibility(View.VISIBLE);

                                      // mPresenter.deleteSurvey(surveyCommon.getSurvey_id());
                                       mContext.startActivity(new Intent(mContext, ActivitySurvey.class));
                                   }

                                   @Override
                                   public void onCancelClicked() {

                                   }
                               });

                   {
               }
           }
               }

       });
        return view;
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
        Log.d("dattaaaa", data);
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
        DialogHelper.showSimpleCustomDialog(mContext,msg);

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
