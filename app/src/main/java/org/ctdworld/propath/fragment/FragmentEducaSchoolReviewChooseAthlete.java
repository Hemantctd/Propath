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
import org.ctdworld.propath.adapter.AdapterSchoolReviewChooseAthleteName;
import org.ctdworld.propath.contract.ContractGetAllAthletes;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.presenter.PresenterGetAllAthletes;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentEducaSchoolReviewChooseAthlete extends Fragment implements ContractGetAllAthletes.View, FragmentSearch.SearchListener
{
    // # constants
    private final String TAG = FragmentEducaSchoolReviewChooseAthlete.class.getSimpleName();

    // # views
    private RecyclerView mRecycler;
 //   private EditText mEditSearchAthleteNames;
 //   private ImageView mImgSearch;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mTxtNoConnection;
    private View mLayoutForDetails;

    // # other variables
    private Context mContext;
    private AdapterSchoolReviewChooseAthleteName mAdapter;
    private List<GetAthletes> mFilteredAthleteList = new ArrayList<>();
    private ContractGetAllAthletes.Presenter mPresenter;
    private List<GetAthletes> mAthleteNameList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view  = inflater.inflate(R.layout.fragment_school_review_choose_athlete, container, false);

        try
        {
            init(view);
            setContactsAdapter();
            // setListeners();
            Log.d(TAG,"fragment_school_review_choose_athlete called");

            mRefreshLayout.setRefreshing(true);
            requestAllAthletes();
            getChildFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();

            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                   // mEditSearchAthleteNames.setText("");
                    if (getFragmentSearch() != null)
                        getFragmentSearch().clearSearchedText();
                    requestAllAthletes();
                }
            });

        }
        catch (Exception e)
        {
            Log.e(TAG,"error , "+e.getMessage());
            e.printStackTrace();
        }


        return view;
    }



    private void init(View view) {

        mContext = getContext();
        mPresenter = new PresenterGetAllAthletes(mContext, this) {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        };


        //mImgSearch = view.findViewById(R.id.athlete_name_search);
        mRecycler = view.findViewById(R.id.athlete_name_recycler_view);
       // mEditSearchAthleteNames = view.findViewById(R.id.athlete_search_name);
        mLayoutForDetails = view.findViewById(R.id.contact_search_layout_for_details);
        mTxtNoConnection = view.findViewById(R.id.contact_search_txt_no_connection);
        mRefreshLayout = view.findViewById(R.id.athlete_name_search_refresh_layout);

    }

    private void requestAllAthletes()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            try {
                mPresenter.requestAllAthletes();
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



    private void setContactsAdapter() {

        mAdapter = new AdapterSchoolReviewChooseAthleteName(mContext,mAthleteNameList, mPresenter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
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






    /*private void voiceToText()
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

    }
*/

    @Override
    public void onGetAllAthletes(List<GetAthletes> athletesList)
    {
        mFilteredAthleteList.clear();
        mAthleteNameList = athletesList;
       // mAdapter.addUserList(athletesList);
        mFilteredAthleteList = athletesList;

       // mAdapter.notifyDataSetChanged();  // list will be shown only when user types letters in search box

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
        DialogHelper.showSimpleCustomDialog(mContext,message);
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
                    Log.d(TAG,"athlete name : " +userName);
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
