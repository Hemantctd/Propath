package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationForAthlete;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewCreate;
import org.ctdworld.propath.adapter.AdapterEducationSelfReviewList;
import org.ctdworld.propath.contract.ContractSelfReview;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.SelfReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterSelfReview;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEducationSelfReviewList extends Fragment implements ContractSelfReview.View {

   // private final String TAG = FragmentEducationSelfReviewList.class.getSimpleName();

    // # views
    private ProgressBar mProgressBar;
    private RecyclerView recycler_athlete_progress_list;
    private FloatingActionButton mFloatButton;


    // # other variables
    private Context mContext;
    private ContractSelfReview.Presenter mPresenter;
    private String athlete_ids;
    private AdapterEducationSelfReviewList mAdapter;


    public FragmentEducationSelfReviewList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_education_self_review_list, container, false);
        init(view);
        setListeners();
        setProgressReportAdapter();

        ActivityEducationForAthlete activity = (ActivityEducationForAthlete) getActivity();

        if (activity != null)
        athlete_ids = activity.getAthleteId();

        onShowProgress();
        mPresenter.getSelfReview(SessionHelper.getInstance(mContext).getUser().getUserId());

        return view;
    }

    public void init(View view)
    {
        mContext = getContext();
       // mAdapter = new AdapterEducationSelfReviewList(mContext,mProgressList,SessionHelper.getInstance(mContext).getUser().getUserId(), adapterListener);
        mPresenter = new PresenterSelfReview(mContext,this);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mFloatButton = view.findViewById(R.id.add_progress_report);
        recycler_athlete_progress_list = view.findViewById(R.id.recycler_athlete_progress_list);
        mProgressBar.setVisibility(View.VISIBLE);

    }




    private void setProgressReportAdapter()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterEducationSelfReviewList(mContext,adapterListener);
        recycler_athlete_progress_list.setLayoutManager(mLayoutManager);
        recycler_athlete_progress_list.setAdapter(mAdapter);
    }



    public void setListeners()
    {
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ActivityEducationSelfReviewCreate.class));
            }
        });
    }

    private AdapterEducationSelfReviewList.Listener adapterListener = new AdapterEducationSelfReviewList.Listener() {
        @Override
        public void deleteSelfReview(final String id) {
            BottomSheetOption.Builder builder = new BottomSheetOption.Builder()

                    .addOption(BottomSheetOption.OPTION_DELETE, "Delete")
                    .addOption(BottomSheetOption.OPTION_EDIT, "Edit");

            FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
            options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(int option) {

                    switch (option)
                    {
                        case BottomSheetOption.OPTION_DELETE:
                            String title = getString(R.string.are_you_sure);
                            String message = "Your self review will be deleted";
                            DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    mPresenter.deleteReview(id);
                                }

                                @Override
                                public void onCancelClicked() {

                                }
                            });
                            break;

                        case BottomSheetOption.OPTION_EDIT:

                            Intent intent = new Intent(mContext, ActivityEducationSelfReviewCreate.class);
                            intent.putExtra(ActivityEducationSelfReviewCreate.KEY_REVIEW_ID, id);
                            intent.putExtra(ActivityEducationSelfReviewCreate.KEY_MODE_CREATE_OR_EDIT, ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT );
                            startActivity(intent);
                            break;
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
    public void onSelfListDeleted(String id) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedSelfReportList(List<SelfReview> selfReviewList) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.setData(selfReviewList,athlete_ids);

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

    }
}
