package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationForAthlete;
import org.ctdworld.propath.adapter.AdapterProgressReportList;
import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterSchoolReview;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */


public class FragmentEducationProgressReportGivenByTeacher extends Fragment implements ContractSchoolReview.View {

    // # constants
    private final String TAG = FragmentEducationProgressReportGivenByTeacher.class.getSimpleName();


    // # views
    private RecyclerView recycler_progress_report_list_given_by_teacher;
    private String athlete_ids;
    private ProgressBar mProgressBar;
    private ContractSchoolReview.Presenter mPresenters;
    private AdapterProgressReportList mAdapter;


    // # other variables
    private Context mContext;


    public FragmentEducationProgressReportGivenByTeacher() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education_progress_report_given_by_teacher, container, false);
        init(view);
        //setToolbar();
        Log.d(TAG, "fragment_progress_report_athlete called");
       setProgressReportAdapter();

        ActivityEducationForAthlete activity = (ActivityEducationForAthlete) getActivity();
        if (activity != null) {
            athlete_ids = activity.getAthleteId();
        }
        Log.d(TAG,"Progress Report athlete_ids -----> " +athlete_ids);

        if (athlete_ids != null && athlete_ids.isEmpty() ) {
            onShowProgress();
            mPresenters.getSchoolReview(athlete_ids);
        }
        else
        {
            onShowProgress();
            mPresenters.getSchoolReview(SessionHelper.getInstance(mContext).getUser().getUserId());
        }

        return view;
    }


    public void init(View view)
    {
        mContext = getContext();
        mPresenters = new PresenterSchoolReview(mContext,this);
        mProgressBar = view.findViewById(R.id.progress_bar);
        recycler_progress_report_list_given_by_teacher=view.findViewById(R.id.recycler_progress_report_list_given_by_teacher);
        mProgressBar.setVisibility(View.VISIBLE);
    }



    private void setProgressReportAdapter()
    {
        mAdapter = new AdapterProgressReportList(mContext, adapterListener);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recycler_progress_report_list_given_by_teacher.setLayoutManager(mLayoutManager);
        recycler_progress_report_list_given_by_teacher.setAdapter(mAdapter);
    }


    private AdapterProgressReportList.Listener adapterListener = new AdapterProgressReportList.Listener() {
        @Override
        public void onProgressReportOptionClicked(final String reviewId, final String athleteId) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
            if (SessionHelper.getInstance(mContext).getUser().getUserId().equals(athleteId))
                    builder.addOption(BottomSheetOption.OPTION_DELETE, "Delete");


            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());

            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {

                    if (option == BottomSheetOption.OPTION_DELETE) {
                        onShowProgress();
                        mPresenters.deleteReview(reviewId);
                    }

                }
            });
            options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);

        }
    };


    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onProgressListDeleted(String id) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.setData(schoolReviewList,athlete_ids);
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
