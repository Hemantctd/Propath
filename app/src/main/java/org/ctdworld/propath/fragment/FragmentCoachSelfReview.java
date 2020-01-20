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
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCoachCreateSelfReview;
import org.ctdworld.propath.adapter.AdapterCoachSelfReviewList;
import org.ctdworld.propath.contract.ContractCoachFeedback;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.CoachData;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterCoachFeedback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class FragmentCoachSelfReview extends Fragment implements View.OnClickListener, ContractCoachFeedback.View {

  //  private final String TAG = FragmentCoachSelfReview.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatButton;
    String mAthleteID;
    private TextView mNoDataFound;
    private ProgressBar mProgressBar;
    AdapterCoachSelfReviewList mAdapter;
    ContractCoachFeedback.Presenter mPresenter;

    public FragmentCoachSelfReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_coach_self_review, container, false);

        init(view);
        prepareRecyclerView();


        onShowProgress();
        mPresenter.getCoachSelfReview(SessionHelper.getInstance(mContext).getUser().getUserId());


        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mPresenter = new PresenterCoachFeedback(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mProgressBar = view.findViewById(R.id.progress_bar);
        mFloatButton = view.findViewById(R.id.add_self_review);
        mRecyclerView = view.findViewById(R.id.recyclerCoachSelfReview);
        mNoDataFound = view.findViewById(R.id.no_data_found);
        mFloatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_self_review) {
          startActivity(new Intent(mContext, ActivityCoachCreateSelfReview.class));
        }
    }

    private void prepareRecyclerView()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new AdapterCoachSelfReviewList(mContext,adapterListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private AdapterCoachSelfReviewList.Listener adapterListener = new AdapterCoachSelfReviewList.Listener() {
        @Override
        public void onCoachSelfReviewOptionClicked(final String reviewId, final String athleteId)
        {
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
                            DialogHelper.showSimpleCustomDialog(mContext, "Are you sure want to delete this ?", new DialogHelper.ShowSimpleDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    mPresenter.deleteCoachSelfReview(reviewId,athleteId);
                                }
                            });
                            break;

                        case BottomSheetOption.OPTION_EDIT:
                            Intent intent = new Intent(mContext, ActivityCoachCreateSelfReview.class);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_ATHLETE_ID, athleteId);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_REVIEW_ID, reviewId);
                            intent.putExtra(ActivityCoachCreateSelfReview.KEY_MODE_CREATE_OR_EDIT, ActivityCoachCreateSelfReview.VALUE_MODE_EDIT);
                            startActivity(intent);
                            break;
                    }

                }
            });
            options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
        }
    };

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

    }

    @Override
    public void onCoachFeedbackDeleted(String id) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.onDeletedSuccessfully(id);
    }

    @Override
    public void onReceivedCoachSelfReview(List<CoachData> coachDataList) {
        onHideProgress();
        if (mAdapter != null)
            mAdapter.setData(coachDataList);
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
