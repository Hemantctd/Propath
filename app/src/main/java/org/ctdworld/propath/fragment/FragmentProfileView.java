package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityChat;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.activity.ActivityWebView;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractProfileView;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.Profile;
import org.ctdworld.propath.presenter.PresenterProfileView;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileView extends BaseFragment implements ContractProfileView.View
{
    private final String TAG = FragmentProfileView.class.getSimpleName();


    // # views
    private View mViewShowRepAchievement, mViewShowAthleteHighlightReels, mViewShowAthletePlaylist, mLayoutDetails;
    private TextView mTxtName, mTxtSportName, mTxtSportAddress, mTxtAthleteBio, mTxtNoConnection, mStatusText;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView mImgProfilePic,mStatusIcon;
    private LinearLayout mBtnSendRequest, mBtnMessage;


    // # other variables
    private Profile mProfile; // # it contains profile received from server
    private ActivityProfileView mActivityProfileView;
    private ContractProfileView.Presenter mPresenter;
    private String sHighlightReelUrl, sPlaylistUrl;
    private Listener mListener;
    private String USER_ID = ""; //  string to contain user id, to get profile of this userId


    // constructor
    public FragmentProfileView() {
        // Required empty public constructor
    }


    public interface  Listener{ void onUserFriendStatusUpdated(String userId, String friendStatus);}


    // returns instance and sets profile type to argument
    public static FragmentProfileView getInstance(String userId)
    {
        FragmentProfileView fragmentProfileView = new FragmentProfileView();
        Bundle bundle = new Bundle();

        Log.i(FragmentProfileView.class.getSimpleName(), "user id = "+userId);

        bundle.putString(ConstHelper.Key.ID,userId);
        fragmentProfileView.setArguments(bundle);
        return fragmentProfileView;
    }




    // initializing PROFILE_TYPE from arguments
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            USER_ID = bundle.getString(ConstHelper.Key.ID);  // setting userId to get profile
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        // requesting  presenter to get profile
        requestProfileDataFromServer(USER_ID);
        // setting listener on swipe refresh layout to refresh data
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
                requestProfileDataFromServer(USER_ID);     // requesting  presenter to get profile
            }
        });
    }




    private void init(View view)
    {
        mContext = getContext();
        mStatusText = view.findViewById(R.id.status_text);
        mStatusIcon = view.findViewById(R.id.status_Image);
        mPresenter = new PresenterProfileView(mContext,this);
        mViewShowRepAchievement = view.findViewById(R.id.profile_view_layout_rep_achievement);
        mViewShowAthleteHighlightReels = view.findViewById(R.id.profile_view_layout_highlight_reels);
        mViewShowAthletePlaylist = view.findViewById(R.id.profile_view_layout_playlist);
        mImgProfilePic = view.findViewById(R.id.profile_view_img_profile_pic);
        mBtnMessage = view.findViewById(R.id.profile_view_message);
        mTxtName = view.findViewById(R.id.profile_view_txt_name);
        mTxtSportName = view.findViewById(R.id.profile_view_txt_sport_name);
        mTxtSportAddress = view.findViewById(R.id.profile_view_txt_address);
        mTxtAthleteBio = view.findViewById(R.id.profile_view_txt_athlete_bio);
        mBtnSendRequest = view.findViewById(R.id.profile_view_btn_send_request);
        //  mProgressbar = view.findViewById(R.id.profile_view_progress_bar);
        mRefreshLayout = view.findViewById(R.id.profile_view_refresh_layout);
        mTxtNoConnection = view.findViewById(R.id.profile_view_txt_no_connection);
        mLayoutDetails = view.findViewById(R.id.profile_view_layout_details);
    }


    public Profile getProfileData()
    {
        return mProfile;  // returning Profile object;
    }


    // requesting  presenter to get profile
    public void requestProfileDataFromServer(String userId)
    {
        USER_ID = userId;
        if (UtilHelper.isConnectedToInternet(mContext))
        {

            showLoader(getString(R.string.message_loading));
            setListeners();
            boolean mIsSelfProfile = userId.equals(SessionHelper.getUserId(mContext));
            if (mActivityProfileView != null) {
                if (mIsSelfProfile)
                {
                    mActivityProfileView.enableOptionsMenu();  // showing edit icon if user is looking self profile
                    mBtnSendRequest.setVisibility(View.GONE);
                    mBtnMessage.setVisibility(View.GONE);
                }
                else
                    mActivityProfileView.disableOptionsMenu();

                mActivityProfileView.showSearchView();
                mActivityProfileView.hideToolbarTitle();
            }



            mLayoutDetails.setVisibility(View.VISIBLE);
            mTxtNoConnection.setVisibility(View.GONE);
            mPresenter.requestProfile(userId);

            if (!mIsSelfProfile)
            {
                String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
                mPresenter.checkFriendStatus(loginUserId, userId);
            }

        }
        else
        {
            hideLoader();
            mLayoutDetails.setVisibility(View.GONE);
            mTxtNoConnection.setVisibility(View.VISIBLE);
            if (mActivityProfileView != null)
                mActivityProfileView.disableOptionsMenu();
        }
    }



    private void setListeners()
    {
        // showing representative achievement
        mViewShowRepAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentProfileViewRepAchievement fragmentProfileViewRepAchievement = FragmentProfileViewRepAchievement.getInstance(/*PROFILE_TYPE,*/USER_ID);
                mActivityProfileView.changeFragment(fragmentProfileViewRepAchievement, ConstHelper.Tag.Fragment.PROFILE_VIEW_REP_ACHIEVEMENT);
            }
        });




        // setting listener to send connection request, if user is looking other's profile then Send
        mBtnSendRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG,"Sending connection request...........................................");
                if (isConnectedToInternet())
                {
                    showLoader(getString(R.string.message_sending));
                    sendFriendRequest();
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));

            }
        });



        // showing HighLight reel in WebView in other Activity
        mViewShowAthleteHighlightReels.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (UtilHelper.isConnectedToInternet(mContext))
                {
                    if (sHighlightReelUrl != null && !sHighlightReelUrl.isEmpty())
                    {
                        if (Patterns.WEB_URL.matcher(sHighlightReelUrl).matches())
                        {
                            // starting new activity to open url in WebView
                            Intent intent = new Intent(getActivity(), ActivityWebView.class);
                            // setting extras
                            intent.putExtra(ActivityWebView.KEY_TOOLBAR,"Highlight Reels");
                            intent.putExtra(ActivityWebView.KEY_WEB_URL,sHighlightReelUrl);
                            startActivity(intent);
                        }
                        else
                            DialogHelper.showSimpleCustomDialog(mContext,"Enter Valid HighLight Reel Url...");
                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,"Please Enter HighLight Reel Url...");
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");


            }
        });



        // showing PlayList in WebView in other Activity
        mViewShowAthletePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (UtilHelper.isConnectedToInternet(mContext))
                {
                    if (sPlaylistUrl != null && !sPlaylistUrl.isEmpty())
                    {
                        if (Patterns.WEB_URL.matcher(sPlaylistUrl).matches())
                        {
                            Intent intent = new Intent(getActivity(), ActivityWebView.class);
                            intent.putExtra(ActivityWebView.KEY_TOOLBAR,"Playlist");
                            intent.putExtra(ActivityWebView.KEY_WEB_URL,sPlaylistUrl);
                            startActivity(intent);
                        }
                        else
                            DialogHelper.showSimpleCustomDialog(mContext,"Enter Valid PlayList Url...");
                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,"Please Enter PlayList Url...");
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext,"No Internet Connection...");

            }
        });


        mImgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfile == null)
                {
                    showToastShort("Data not loaded...");
                    return;
                }
                if (mProfile.getId() != null && !mProfile.getId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
                {
                    Chat chat = new Chat();
                    chat.setChattingToId(mProfile.getId());
                    chat.setChattingToName(mProfile.getName());
                    chat.setChattingToPicUrl(mProfile.getPicUrl());
                    chat.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);

                    Intent intent = new Intent(mContext , ActivityChat.class);
                    intent.setAction(ActivityChat.KEY_CHAT_ACTION);
                    intent.putExtra(ActivityChat.KEY_CHAT, chat);

                    startActivity(intent);
                    startActivity(intent);
                }
            }
        });



        mBtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfile == null)
                {
                    showToastShort("Data not loaded...");
                    return;
                }
                if (mProfile.getId() != null && !mProfile.getId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
                {
                    Chat chat = new Chat();
                    chat.setChattingToId(mProfile.getId());
                    chat.setChattingToName(mProfile.getName());
                    chat.setChattingToPicUrl(mProfile.getPicUrl());
                    chat.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);

                    Intent intent = new Intent(mContext , ActivityChat.class);
                    intent.setAction(ActivityChat.KEY_CHAT_ACTION);
                    intent.putExtra(ActivityChat.KEY_CHAT, chat);

                    startActivity(intent);
                }
            }
        });


    }

    private void sendFriendRequest() {

        // checking if user is not looking his own profile
        if (!USER_ID.contains(SessionHelper.getInstance(mContext).getUser().getUserId()))
        {
            ContractRequest.Presenter presenter = new PresenterRequest(mContext, new ContractRequest.View() {
                @Override
                public void onRequestSentSuccessfully()
                {
                    hideLoader();

                    setFriendStatus(false, true);
                    if (mListener != null)
                        mListener.onUserFriendStatusUpdated(USER_ID, Request.REQUEST_STATUS_PENDING);

                    DialogHelper.showSimpleCustomDialog(mContext,"Request Sent Successfully....");
                }

                @Override
                public void onReceivedAllRequests(List<Request.Data> requestDataList) {
                    hideLoader();
                }

                @Override
                public void onRespondedSuccessfully() {
                    hideLoader();
                }

                @Override
                public void onFailed(String message) {
                    hideLoader();
                    if (message != null && !message.isEmpty())
                        DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title), message);
                    else
                        DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.failed_title));

                    setFriendStatus(false, false);
                    if (mListener != null)
                        mListener.onUserFriendStatusUpdated(USER_ID, Request.REQUEST_STATUS_REJECT);
                }


                @Override
                public void onShowMessage(String message) {
                   hideLoader();
                    mBtnSendRequest.setEnabled(true);
                    DialogHelper.showSimpleCustomDialog(mContext,message);
                }
            });

            showLoader(getString(R.string.message_sending));
            presenter.sendRequest(USER_ID, Request.REQUEST_FOR_FRIEND);

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityProfileView = (ActivityProfileView) context;
        if (context != null)
            mListener = (Listener) context;
    }




    @Override
    public void onProfileReceived(Profile profile)
    {
        try
        {
            hideLoader();

            mProfile = profile;
            sHighlightReelUrl = profile.getHighlightReel();  // setting highlight reel url in String, it will be used in webview;
            sPlaylistUrl = profile.getPlaylist();  // setting playlist  url in String, it will be used in webview ;
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
            mViewShowAthletePlaylist.setAnimation(animation);
            mViewShowAthleteHighlightReels.setAnimation(animation);
            mViewShowRepAchievement.setAnimation(animation);

            if (!profile.getName().isEmpty()) {
                mTxtName.setText(profile.getName());
                mTxtName.setAnimation(animation);

            }
            else {
                mTxtName.setVisibility(View.GONE);
            }

            if (!profile.getSportName().isEmpty()) {
                mTxtSportName.setText(profile.getSportName());
                mTxtSportName.setAnimation(animation);

            }
            else {
                mTxtSportName.setVisibility(View.GONE);
            }

            if (!profile.getAddress().isEmpty()) {
                mTxtSportAddress.setText(profile.getAddress());
                mTxtSportAddress.setAnimation(animation);
            }

            else {
                mTxtSportAddress.setVisibility(View.GONE);
            }

            if (!profile.getAthleteBio().isEmpty()) {
                mTxtAthleteBio.setText(profile.getAthleteBio());
                mTxtAthleteBio.setAnimation(animation);

            }
            else {
                mTxtAthleteBio.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(profile.getPicUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                    .apply(new RequestOptions().error(R.drawable.ic_profile))
                    .apply(new RequestOptions().override(UtilHelper.convertDpToPixel(mContext, 140), UtilHelper.convertDpToPixel(mContext, 120)))
                    .into(mImgProfilePic);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    // sets friend status
    private void setFriendStatus(boolean isFriend, boolean isRequestSent)
    {
        mBtnMessage.setVisibility(View.VISIBLE);
        mBtnSendRequest.setVisibility(View.VISIBLE);
        if (isFriend)

        {
            mStatusIcon.setImageResource(R.drawable.ic_friend_accepted);
            mStatusText.setText(getString(R.string.friends));
            mBtnSendRequest.setEnabled(false);
        }
        else
        {
            if (isRequestSent)
            {
                mStatusIcon.setImageResource(R.drawable.ic_add_contact);
                mStatusText.setText(getString(R.string.pending));
                mBtnSendRequest.setEnabled(false);
            }
            else
            {
                mStatusIcon.setImageResource(R.drawable.ic_add_contact);
                mStatusText.setText(getString(R.string.add_contact_single));
                mBtnSendRequest.setEnabled(true);
            }
        }

    }




    @Override
    public void hideLoader()
    {
        super.hideLoader();
        mRefreshLayout.setRefreshing(false);
    }




    @Override
    public void showLoader(String title) {
        super.showLoader(title);
    }



    // this method is called to show mBtnSendRequest if login user is not friend of user whom profile logged in user is looking
    @Override
    public void onReceivedFriendStatus(final String friendStatus)
    {
        hideLoader();
        Log.i(TAG,"onReceivedFriendStatus , status = "+friendStatus);
        if (mActivityProfileView != null)
        {
            mActivityProfileView.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    switch (friendStatus) {
                        case Request.REQUEST_STATUS_ACCEPT:
                            setFriendStatus(true, false);
                            break;
                        case Request.REQUEST_STATUS_REJECT:
                            setFriendStatus(false, false);
                            break;
                        case Request.REQUEST_STATUS_PENDING:
                            setFriendStatus(false, true);
                            break;
                    }
                }
            });
        }
    }



    @Override
    public void onFailed(String message) {
        hideLoader();
        if (message != null && !message.isEmpty())
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title), message);
        else
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title));
    }
}
