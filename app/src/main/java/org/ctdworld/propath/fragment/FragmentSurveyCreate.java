package org.ctdworld.propath.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCreateSurvey;
import org.ctdworld.propath.adapter.AdapterPastSurvey;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractSurvey;
import org.ctdworld.propath.contract.ContractGetAllSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Survey;
import org.ctdworld.propath.presenter.PresenterCreateSurvey;



public  class FragmentSurveyCreate extends BaseFragment implements View.OnClickListener, ContractGetAllSurvey.View, ContractSurvey.View {
    private FloatingActionButton mCreateBtn;
    private RecyclerView pastSurveys;
    Context mContext;
   // List<SurveyQusetions> surveyList = new ArrayList<>();
    ProgressBar mProgressBar;
    private AdapterPastSurvey adapterPastSurvey;
    LinearLayoutManager mLayoutManager;
    ContractSurvey.Presenter mPresenter;
   // private SwipeRefreshLayout mRefreshLayout;

    public FragmentSurveyCreate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_survey_create, container, false);
        init(view);
        setSurveyAdapter();


        requestSurvey();




       /* mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requestSurvey();
            }
        });*/

        return view;
    }



    private void requestSurvey()
    {
        if (isConnectedToInternet())
        {
           // mRefreshLayout.setRefreshing(true);
            mPresenter.requestSurvey();
        }
        else
        {
            pastSurveys.setVisibility(View.GONE);
            //mRefreshLayout.setRefreshing(false);
        }
    }

    private void init(View view)
    {
        mContext = getContext();
        //appCompatActivity = getFragmentManager();
        mPresenter = new PresenterCreateSurvey(mContext,this);
        mProgressBar= view.findViewById(R.id.progress_bar);
        mCreateBtn=view.findViewById(R.id.createSurvey);
        pastSurveys=view.findViewById(R.id.past_surveys_recycler_view);
        mCreateBtn.setOnClickListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        //mRefreshLayout = view.findViewById(R.id.refresh_layout);

    }
    // setting adapter
    private void setSurveyAdapter()
    {

        adapterPastSurvey = new AdapterPastSurvey(mContext, mProgressBar, getFragmentManager()) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };
        mLayoutManager = new LinearLayoutManager(getContext());
        pastSurveys.setLayoutManager(mLayoutManager);
        pastSurveys.setAdapter(adapterPastSurvey);

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createSurvey) {
            Intent intent = new Intent(mContext, ActivityCreateSurvey.class);
            intent.putExtra(ActivityCreateSurvey.KEY_SURVEY_CREATE_EDIT, "not edit");
            startActivity(intent);
        }



    }

    @Override
    public void onReceivedGetAllSurvey(Survey survey) {
      //  adapterPastSurvey.updateSurvey(survey);

    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
    }

    @Override
    public void onReceivedSurvey(Survey.SurveyData surveyData) {
        if (surveyData != null)
        {
            mProgressBar.setVisibility(View.GONE);
          //  mRefreshLayout.setRefreshing(false);
            adapterPastSurvey.updateSurvey(surveyData);

        }

    }

    @Override
    public void onSuccess(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);

    }

    @Override
    public void onShowProgress() {
      //  mRefreshLayout.setRefreshing(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {

      //  mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
      //  mRefreshLayout.setRefreshing(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
       // mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onShowMessage(String message) {
       // mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
        //DialogHelper.showSimpleCustomDialog(mContext,message);

    }
}
