package org.ctdworld.propath.fragment;


import android.app.Activity;
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
import org.ctdworld.propath.adapter.AdapterGroupList;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterGroupChatInjury;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGroupChatInjuryManagement extends BaseFragment implements ContractGroupChatInjury.View, AdapterGroupList.OnOptionsMenuItemClickedListener
{
    // # constants
    private final String TAG = FragmentGroupChatInjuryManagement.class.getSimpleName();


    // # views
    private RecyclerView mContactsRecyclerView;
/*    private ImageView mImgSearch;
    private EditText mEditSearch;*/
    private SwipeRefreshLayout mRefreshLayout;
    private View mLayoutMainData;
    private View mLayoutMessage;
    private TextView mTxtMessage;


    // other varables
    private Context mContext;
   // private PermissionHelper mPermissionHelper;
    private AdapterGroupList mAdapterGroupList;
    private List<Chat> mListGroup = new ArrayList<>();
    private ContractGroupChatInjury.Presenter mPresenter;


    public FragmentGroupChatInjuryManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group_chat_injury_management,container,false);

        init(view);
        setContactsAdapter();
        setListeners();

        getChildFragmentManager().beginTransaction().add(R.id.fragment_search_container, new FragmentSearch(), ConstHelper.Tag.Fragment.SEARCH).commit();

        requestGroupList();
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              //  mEditSearch.setText("");
                requestGroupList();
            }
        });


        return view;
    }

    private void init(View view)
    {
        mContext = getContext();
        mPresenter = new PresenterGroupChatInjury(mContext, this);
        //mPermissionHelper = new PermissionHelper(mContext);
        mRefreshLayout = view.findViewById(R.id.group_chat_injury_management_refresh_layout);
        mContactsRecyclerView = view.findViewById(R.id.group_chat_injury_management_recyclerview);
       /* mImgSearch = view.findViewById(R.id.group_chat_injury_management_img_search_icon);
        mEditSearch = view.findViewById(R.id.group_chat_injury_management_edit_search);
        mLayoutMainData = view.findViewById(R.id.layout_main_data);*/
        mLayoutMessage = view.findViewById(R.id.layout_message);
        mTxtMessage = view.findViewById(R.id.txt_message);
    }


    private void requestGroupList()
    {
        Log.i(TAG,"requesting for group list");
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            activateLayout(mLayoutMainData, mLayoutMessage);
            mRefreshLayout.setRefreshing(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mAdapterGroupList.clearOldList();  // clearing old list before refreshing data
                    mListGroup.clear();  // clearing old list before refreshing data

                    Chat chat = new Chat();
                    chat.setUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                    chat.setGroupType(Chat.GROUP_TYPE_GROUP_CHAT);
                    mPresenter.requestGroupList(chat);
                }
            }).start();

        }
        else
        {
            mRefreshLayout.setRefreshing(false);
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
        }
    }


    private void setContactsAdapter()
    {
        mAdapterGroupList = new AdapterGroupList(mContext, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mContactsRecyclerView.setLayoutManager(layoutManager);
        mContactsRecyclerView.setAdapter(mAdapterGroupList);
    }


    private void setListeners()
    {
       /* mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    voiceToText();
                else
                    mPermissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
            }
        });



        // to filter names
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


 /*   // to filter name with voice text
    private void voiceToText()
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


    // this shows and hides layout
    private void activateLayout(View showLayout, View hideLayout)
    {
        if (showLayout != null)
            showLayout.setVisibility(View.VISIBLE);

        if (hideLayout != null)
            hideLayout.setVisibility(View.GONE);
    }


    // filter list by receiving text
    public void filterList(final String searchedText)
    {
        //new array list that will hold the filtered data

        new Thread(new Runnable() {
            @Override
            public void run() {
              //  final ArrayList<NewsFeed.PostData> filteredList = new ArrayList<>();
                final ArrayList<Chat> filteredNames = new ArrayList<>();

                //looping through existing elements
                for (Chat chat : mListGroup) {
                    //if the existing elements contains the search input
                    if (chat.getChattingToName().toLowerCase().contains(searchedText.toLowerCase())) {
                        //adding the element to filtered list
                        filteredNames.add(chat);
                    }
                }

                if (getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterGroupList.filterList(filteredNames);

                    }
                });

            }
        }).start();

    }






    @Override
    public void onReceivedGroup(final Chat chat)
    {
        Log.i(TAG, "group name received , "+chat.getChattingToName());
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                    mListGroup.add(chat);  // adding to main list

                    // # data will not be loaded in adapter as long as user doesn't type text in search box
                  mAdapterGroupList.updateGroupList(chat);
                }
            });
        }
        else
            Log.e(TAG,"activity is null");
    }



    @Override
    public void onFailed() {
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);

                    activateLayout(mLayoutMessage, mLayoutMainData);
                    mTxtMessage.setText(getString(R.string.message_failed));
                    //DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
                }
            });
        }
    }



    @Override
    public void onShowMessage(final String message) {
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                    activateLayout(mLayoutMessage, mLayoutMainData);
                    mTxtMessage.setText(message);
                   // DialogHelper.showSimpleCustomDialog(mContext, message);
                }
            });
        }
    }


    // this method is not being used for create group instead other method has been overridden form ActivityContact.FragmentGroupChatInjuryManagementListener
    @Override
    public void onGroupCreated(final Chat chat)
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                    activateLayout(mLayoutMainData, mLayoutMessage);
                    mListGroup.add(chat);  // adding to main list

                   // mAdapterGroupList.onNewGroupCreated(chat); // # data will not be loaded in adapter as long as user doesn't type letter in search box
                }
            });
        }
        else
            Log.e(TAG,"activity is null");
    }

    @Override
    public void onEditGroupNameSuccessful(Chat chat) {

    }

    @Override
    public void onEditGroupImageSuccessful(Chat chat) {

    }

    @Override
    public void onRemoveParticipantSuccess(Chat chat, int positionInAdapter) {

    }

    @Override
    public void onGroupExitSuccess(Chat chat) {
        Log.i(TAG,"onGroupExitSuccess() called , chat = "+chat+" , mAdapterGroupList = "+mAdapterGroupList);
        hideLoader();
        if (mAdapterGroupList != null)
            mAdapterGroupList.onExitedFromGroup(chat);
    }



    @Override
    public void onReReceivedGroupMember(Chat chat) {

    }


    // this method is called when, create group item is clicked in DialogContactOptionMenu
    public void createGroup(int itemType)
    {
        if (BottomSheetOption.OPTION_CREATE_GROUP != itemType)
        {
            Log.i(TAG,"selected contact options menu is not to create group ,  so returning");
            return;
        }

        Log.i(TAG,"createGroup() method called");
        DialogCreateGroup dialogCreateGroup = DialogCreateGroup.getInstance(Chat.GROUP_TYPE_GROUP_CHAT);
        dialogCreateGroup.setOnGroupCreateListener(new DialogCreateGroup.OnGroupCreateListener() {
            @Override
            public void onGroupCreated(Chat chat) {
                if (chat != null)
                {
                    activateLayout(mLayoutMainData, mLayoutMessage);
                    if (mAdapterGroupList != null)
                    {
                        mListGroup.add(chat);
                        mAdapterGroupList.updateGroupList(chat);
                        mContactsRecyclerView.smoothScrollToPosition(mAdapterGroupList.getItemCount()-1);  // scrolling to last item
                    }
                    else
                        Log.e(TAG,"mAdapterGroupList is  null in click create group method");

                }else
                    Log.e(TAG,"groupChatInjury is null in click create group() method");

            }
        });
        dialogCreateGroup.show(getChildFragmentManager(),"");
    }


    // this method is called from ActivityContact, onActivityResult() method
    public void updateGroupList()
    {
        Log.i(TAG,"updateGroupList() method called");
        if (this.isAdded())
        {
            Log.i(TAG,"updating group list");
            requestGroupList();
        }
    }



    // # deleting(exiting) group
    @Override
    public void onAdapterContactListOptionsMenuClicked(final Chat chat)
    {
        if (chat == null)
            return;

        BottomSheetOption .Builder builder = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete Group");

        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int option)
            {
                // setting userId as memberId to exit from group
                String title = getString(R.string.are_you_sure);
                String message = "\""+ chat.getChattingToName()+"\" will be deleted...";
                String positiveBtn = getString(R.string.delete);
                String negativeBtn = getString(R.string.cancel);
                DialogHelper.showCustomDialog(mContext, title, message, positiveBtn, negativeBtn, new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked() {
                        showLoader(getString(R.string.deleting));
                        chat.setUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                        mPresenter.exitGroup(chat);
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });

            }
        });
        options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }
}
