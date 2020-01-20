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
import org.ctdworld.propath.adapter.AdapterContactList;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractContact;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterContact;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContact extends BaseFragment implements ContractContact.View, AdapterContactList.OnOptionsMenuClickedListener
{
    // # constants
    private final String TAG = FragmentContact.class.getSimpleName();

    // # views
    private RecyclerView mRecyclerView;
/*    private ImageView mImgSearch;
    private EditText mEditSearch;*/
    private TextView mTxtNoConnection;  // to show no connection message
    private SwipeRefreshLayout mRefreshLayout;

    // # other variables
    private Context mContext;
    private PresenterContact mPresenter;
 //   private PermissionHelper mPermissionHelper;
    private AdapterContactList mAdapterContactList;
    private List<User> mListItems = new ArrayList<>();




    public FragmentContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);

        init(view);
        setUpAdapter();
        setListeners();
        requestAllContacts();

        getChildFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();

        return view;
    }



    // # initializing variables
    private void init(View view) {
        mContext = getContext();
      //  mPermissionHelper = new PermissionHelper(mContext);
        mPresenter = new PresenterContact(mContext, this);
        mRefreshLayout = view.findViewById(R.id.contact_refresh_layout);
        mRecyclerView = view.findViewById(R.id.contact_recyclerview);
        mTxtNoConnection = view.findViewById(R.id.contact_txt_no_connection);
      /*  mImgSearch = view.findViewById(R.id.contact_img_search_icon);
        mEditSearch = view.findViewById(R.id.contact_edit_search);*/
    }




    // # setting up adapter
    private void setUpAdapter()
    {
        mAdapterContactList = new AdapterContactList(mContext,mListItems, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterContactList);
    }



    // requesting to get all contacts (friends) from server
    private void requestAllContacts()
    {
        if (isConnectedToInternet())
        {
            mRefreshLayout.setRefreshing(true);
            mPresenter.requestAllContacts();
        }
        else
        {
            mRecyclerView.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
            mRefreshLayout.setRefreshing(false);
        }
    }



    // # setting listener
    private void setListeners()
    {

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAllContacts();
            }
        });

        /*mImgSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    searchWithVoiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });



        // to filter contacts
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());

            }
        });*/

    }


/*    // converting voice to text
    private void searchWithVoiceToText()
    {
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
                    mEditSearch.setText(spokenText);
                    mEditSearch.requestFocus();
                }

                @Override
                public void onError() {
                    mEditSearch.requestFocus();
                }
            });

            fragmentSpeechRecognition.show(getChildFragmentManager(),"");
        }

    }*/





    // # this method is called when contact is clicked in adapter
    @Override
    public void onAdapterOptionsMenuClicked(final User user)
    {
        BottomSheetOption .Builder builder = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete");

        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int option) {
                if (isConnectedToInternet())
                {
                    DialogHelper.showCustomDialog(mContext, "Are you sure want to delete this contact ?",
                            new DialogHelper.ShowDialogListener() {
                                @Override
                                public void onOkClicked() {
                                    showLoader(getString(R.string.deleting));
                                    mPresenter.deleteContact(user.getUserId());

                                }

                                @Override
                                public void onCancelClicked() {

                                }
                            });


                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.no_connection));
            }
        });
        options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }



    @Override
    public void onReceiveAllContacts(List<User> contactList)
    {
        Log.i(TAG,"onReceiveAllContacts() method called ");
         mListItems = contactList;  // initializing mListItems to filter contacts list

        // # data will not be loaded in adapter as long as user doesn't type text in search box
         mAdapterContactList.addContactList(contactList);
         mAdapterContactList.notifyDataSetChanged();
    }

    @Override
    public void onContactDeleted(String userId) {
        hideLoader();
        if (mAdapterContactList != null)
            mAdapterContactList.onContactDeleted(userId);
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
    public void onSuccess(){ mRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {
        DialogHelper.showSimpleCustomDialog(mContext,message);
    }





    // called from container activity to filter list
    public void filterList(final String searchedText)
    {
        // # filtering on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                final ArrayList<User> filterdNames = new ArrayList<>();

                for (User user : mListItems) {
                    if (user.getName().toLowerCase().contains(searchedText.toLowerCase()))
                        filterdNames.add(user);
                }

                if (getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterContactList.filterList(filterdNames);
                    }
                });

            }
        }).start();

    }


}
