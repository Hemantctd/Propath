package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterGetAllSharedSurvey;
import org.ctdworld.propath.contract.ContractGetAllSharedSurvey;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.SurveyQusetions;
import org.ctdworld.propath.presenter.PresenterGetAllSharedSurvey;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSurveyReceived extends Fragment {
    Context mContext;
    List<SurveyQusetions> surveyList = new ArrayList<>();
    List<SurveyQusetions> questionsList = new ArrayList<>();
    AdapterGetAllSharedSurvey adapterSharedSurvey;
    SurveyQusetions surveyQusetions =  new SurveyQusetions();
    ContractGetAllSharedSurvey.Presenter mPresenter;
    ProgressBar mProgressBar;
    RecyclerView recycler_get_shared_survey;

    public FragmentSurveyReceived() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_received, container, false);
        init(view);
        setAdapter();
//        mPresenter.getAllSharedSurvey();
        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        //mPresenter=new PresenterGetAllSharedSurvey(mContext,this);
        recycler_get_shared_survey = view.findViewById(R.id.recycler_get_shared_survey);
        mProgressBar = view.findViewById(R.id.progress_bar);
      //  mProgressBar.setVisibility(View.VISIBLE);
    }




    public void setAdapter()
    {
        adapterSharedSurvey = new AdapterGetAllSharedSurvey(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_get_shared_survey.setLayoutManager(layoutManager);
        recycler_get_shared_survey.setAdapter(adapterSharedSurvey);
    }


/*
    @Override
    public void onGetReceivedAllSubmittedSurvey(SurveyQusetions surveyQusetions, List<SurveyQusetions> qusetionsList) {

    }

    @Override
    public void onGetReceivedAllSharedSurvey(SurveyQusetions surveyQusetions, List<List<List<SurveyQusetions>>> qusetionsList) {

        // surveyList = qusetionsList;
        adapterSharedSurvey.updateSurvey(surveyQusetions,qusetionsList);
    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
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

    }*/

}
