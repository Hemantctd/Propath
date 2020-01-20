package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterSelfReviewChooseAthleteName;
import org.ctdworld.propath.contract.ContractSelfReviewedAthletes;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.presenter.PresenterSelfReviewedAthletes;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class FragmentEducationAthleteReviewChooseAthlete extends Fragment implements ContractSelfReviewedAthletes.View, FragmentSearch.SearchListener
{


    private final String TAG = FragmentEducationAthleteReviewChooseAthlete.class.getSimpleName();
    Context mContext;

    private RecyclerView mRecycler;
 //   private EditText mEditSearchAthleteNames;
 //   private ImageView mImgSearch;
    SwipeRefreshLayout mRefreshLayout;
    private TextView mTxtNoConnection;
    private View mLayoutForDetails;
    LinearLayoutManager mLayoutManager;
    AdapterSelfReviewChooseAthleteName mAdapter;

    private List<GetAthletes> mFilteredAthleteList = new ArrayList<>();
  //  List<GetAthletes> mAthleteNameList = new ArrayList<>();
    PermissionHelper mPermissionHelper;
    ContractSelfReviewedAthletes.Presenter mPresenter;

    public FragmentEducationAthleteReviewChooseAthlete() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_education_athlete_review_choose_athlete, container, false);

        init(view);
        getChildFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();
        setSelfReviewAdapter();
        //setListeners();

        mRefreshLayout.setRefreshing(true);
        getAthletesReviewed();


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // mEditSearchAthleteNames.setText("");
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
        mPresenter = new PresenterSelfReviewedAthletes(mContext, this) {
            @Override
            public void onSuccess() {

            }
        };

        mPermissionHelper = new PermissionHelper(mContext);
     //   mImgSearch = view.findViewById(R.id.athlete_name_search);
        mRecycler = view.findViewById(R.id.athlete_name_recycler_view);
    //    mEditSearchAthleteNames = view.findViewById(R.id.athlete_search_name);
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
                mPresenter.getAthletesReviewed();

            }
            catch (Exception e)
            {
                Log.e(TAG,"Error while requesting for athletes , "+e.getMessage());
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


    private void setSelfReviewAdapter()
    {
        mAdapter = new AdapterSelfReviewChooseAthleteName(mContext,new ArrayList<GetAthletes>(),mPresenter);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);

    }
/*    private  void setListeners()
    {
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });

        // to filter list
        mEditSearchAthleteNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterListByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }*/


  /*  private void filterListByName(String text)
    {

    }*/






  /*  private void voiceToText()
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
                    mEditSearchAthleteNames.setText(spokenText);
                    mEditSearchAthleteNames.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearchAthleteNames.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getChildFragmentManager(),"");
        }


    }*/

    @Override
    public void onGotReviewedAthletes(List<GetAthletes> athletesList)
    {
        mFilteredAthleteList.clear();
        mFilteredAthleteList = athletesList;

        //mAthleteNameList = athletesList;

        // # data will be loaded only when user type text in search box
      //  mAdapter.addAthleteList(athletesList);
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
    public void onShowMessage(String message) {

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
                    if (userName != null)
                    {
                        if (searchedText != null && !searchedText.isEmpty() && userName.toLowerCase().contains(searchedText.toLowerCase()))
                        {
                            filterdUser.add(user);
                        }
                    }

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
