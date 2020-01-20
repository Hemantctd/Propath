package org.ctdworld.propath.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterProgressReportChooseAthleteName;
import org.ctdworld.propath.contract.ContractReviewGotAllAthletes;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.presenter.PresenterGetAReviewedAthletes;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentEducationSchoolReviewChooseAthlete extends Fragment implements ContractReviewGotAllAthletes.View, FragmentSearch.SearchListener
{
    // # constants
    private final String TAG = FragmentEducationSchoolReviewChooseAthlete.class.getSimpleName();


    // # views
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mTxtNoConnection;
    private View mLayoutForDetails;


    // # other variables
    private Context mContext;
    private ContractReviewGotAllAthletes.Presenter mPresenter;
    private List<GetAthletes> mFilteredAthleteList = new ArrayList<>();
    private AdapterProgressReportChooseAthleteName mAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view  = inflater.inflate(R.layout.fragment_education_school_review_choose_athlete, container, false);
        init(view);
        //setToolbar();
        getChildFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();

        setContactsAdapter();
        getAthletesReviewed();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            mAdapter.clearOldData();
            if (getFragmentSearch() != null)
                getFragmentSearch().clearSearchedText();

            getAthletesReviewed();
            }
        });

        return view;
    }


    private void init(View view)
    {


        mContext = getContext();
        mPresenter = new PresenterGetAReviewedAthletes(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };
        mRecycler = view.findViewById(R.id.athlete_name_recycler_view);
        mLayoutForDetails = view.findViewById(R.id.contact_search_layout_for_details);
        mTxtNoConnection = view.findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = view.findViewById(R.id.athlete_name_search_refresh_layout);

    }


    // requesting to get all registered users from server
    private void getAthletesReviewed()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mRefreshLayout.setRefreshing(true);
                mPresenter.getAthletesReviewed();
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for users , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            mLayoutForDetails.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void setContactsAdapter()
    {
        mAdapter = new AdapterProgressReportChooseAthleteName(mContext,new ArrayList<GetAthletes>());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }







    @Override
    public void onGotReviewedAthletes(List<GetAthletes> athletesList) {

        mFilteredAthleteList.clear();
        mFilteredAthleteList = athletesList;

        // # data will be loaded only when user type text in search box
      // mAdapter.addUserList(athletesList);

    }



    @Override
    public void onFailed() {
    }

    @Override
    public void onShowProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onHideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mRefreshLayout.setEnabled(false);

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mRefreshLayout.setEnabled(true);
    }

    @Override
    public void onShowMessage(String message)
    {
    }



    @Override
    public void onSearchToFilter(final String searchedText) {
        // # filtering on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<GetAthletes> filterdUser = new ArrayList<>();

                for (GetAthletes user : mFilteredAthleteList)
                {
                    String userName = user.getName();
                    if (userName != null && searchedText != null && !searchedText.isEmpty())
                        if (userName.toLowerCase().contains(searchedText.toLowerCase()))
                            filterdUser.add(user);
                }

                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.filterList(filterdUser);

                    }
                });

            }
        }).start();
    }


    private FragmentSearch getFragmentSearch()
    {
        return (FragmentSearch) getChildFragmentManager().findFragmentByTag(ConstHelper.Tag.Fragment.SEARCH);
    }

}
