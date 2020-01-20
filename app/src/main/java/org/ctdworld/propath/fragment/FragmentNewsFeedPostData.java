package org.ctdworld.propath.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNewsFeedComments;
import org.ctdworld.propath.activity.ActivityNewsFeedPost;
import org.ctdworld.propath.activity.ActivityNewsFeedShare;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeedPostData extends Fragment {

    private static final String TAG = FragmentNewsFeedPostData.class.getSimpleName();

    public FragmentNewsFeedPostData() {
        // Required empty public constructor
    }



    // # views
    private View layoutLike;   // layout at bottom row to like or remove like if post is already liked
    private View layoutComment; // layout at bottom row to make comment or remove see comment
    private View layoutShare;  // layout at bottom row to share post
    private ImageView imgOptionsMenu;  // overflow icon to show options(delete, edit...)
    private ImageView imgProfilePic;  // profile image of user who has posted or shared the post
    private ImageView imgPost;  // to show posted image
    private ImageView imgLike;  // like icon at bottom inside like layout
    private TextView txtSharedByName;  // name of the user who has shared post. "like developer has shared this post"
    private TextView txtSharedPostMessage;  // message added while sharing post
    private TextView txtProfileName;   // user name who has posted feed
    private TextView txtDate;   // posted date
    private TextView txtPostMessage;  // message while posting
    private TextView txtCountLikeComment; // shows like and comment count below posted image
    private TextView txtLike;  // like text in layoutLike at bottom



    // # variables
    private Context mContext;  // activity context
    private NewsFeed.PostData postData;
  //  private ContractNewsFeed.Presenter mPresenterNewsFeed;
    private int mContainerActivity;  // to check which Activity is using this fragment
    private Listener mListener;



    // # constant for container activity
    public static final int ACTIVITY_NEWS_FEED_LIST = 1;  //  ActivityNewsFeed
    public static final int ACTIVITY_NEWS_FEED_COMMENT = 2;  //  ActivityNewsFeedComments
    public static final int ACTIVITY_NEWS_FEED_SHARE = 2;  //  ActivityNewsFeedShare



    public interface Listener extends Serializable {void onPostOptionsMenuClicked(NewsFeed.PostData postData);}


    public static FragmentNewsFeedPostData getInstance(NewsFeed.PostData postData, Listener listener, int containerActivity)
    {
        FragmentNewsFeedPostData fragment = new FragmentNewsFeedPostData();
        Bundle bundle = new Bundle();
        bundle.putSerializable("postData", postData);
        //bundle.putSerializable("presenter", presenter);
        bundle.putSerializable("listener", listener);
        bundle.putInt("containerActivity", containerActivity);
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            postData = (NewsFeed.PostData) bundle.getSerializable("postData");
          //  mPresenterNewsFeed = (ContractNewsFeed.Presenter) bundle.getSerializable("presenter");
            mListener = (Listener) bundle.getSerializable("listener");
            mContainerActivity = bundle.getInt("containerActivity");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed_post_data, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);

        if (postData == null)
            return;

        mContext = getContext();
        txtCountLikeComment.setText(postData.getPostLikeCount()+" Like | " + postData.getPostCommentCount()+" Comments ");
        txtProfileName.setText(postData.getPostByUserName());
        txtPostMessage.setText(postData.getPostMessage());
        setLikePost(); // # setting data if post is liked or not, and listener
        setProfileImage(); // # setting profile image
        setOptionsMenu();   // # showing or hiding option menu icon, setting click listener
        setCommentLayoutClickListener();  // to make and see comments
        setOnShareClicked(postData, layoutShare); // setting click listener on share layout to share post
        setPostShared();  // setting data for shared post
        setPostMedia();  // setting post media
        setPostDate();  // # setting post date
        setOnProfileImageClickListener();   // # setting click listener on profile image

    }

    // # initializing
    private void init(View view)
    {
        imgOptionsMenu= view.findViewById(R.id.img_options_menu);
        txtSharedPostMessage = view.findViewById(R.id.share_by_text);
        txtSharedByName = view.findViewById(R.id.shared_by);
        imgProfilePic = view.findViewById(R.id.profile_image);
        txtProfileName = view.findViewById(R.id.profile_name);
        txtDate = view.findViewById(R.id.date);
        txtPostMessage = view.findViewById(R.id.message);
        imgPost = view.findViewById(R.id.news_feed_image);
        txtCountLikeComment = view.findViewById(R.id.count);
        layoutLike = view.findViewById(R.id.layout_like);
        layoutComment = view.findViewById(R.id.comment_layout);
        layoutShare = view.findViewById(R.id.share_layout);
        imgLike = view.findViewById(R.id.like_btn);
        txtLike = view.findViewById(R.id.like_text);
    }


    // to share post
    private void setOnShareClicked(final NewsFeed.PostData postData, View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, ActivityNewsFeedShare.class);
                i.putExtra(ActivityNewsFeedShare.KEY_POST_DATA, postData);
                mContext.startActivity(i);
            }
        });
    }


    // # setting if post is liked or not
    private void setLikePost()
    {
        if (postData.getLikeStatus().equals(NewsFeed.POST_NOT_LIKED))
        {
            imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
            txtLike.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
        }
        else if(postData.getLikeStatus().equals(NewsFeed.POST_LIKED))
        {
            imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
            txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
        }

        setLikeClickListener(); // setting click listener to like the post or remove like if already liked

    }


    // setting profile image
    private void setProfileImage()
    {
        if (postData == null)
            return;

        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        Glide.with(mContext)
                .load(postData.getPostByUserProfilePic())
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen,picDimen))
                .into(imgProfilePic);
    }


    // showing or hiding option menu icon, setting click listener
    private void setOptionsMenu()
    {
        if (mContainerActivity == ACTIVITY_NEWS_FEED_LIST ) // if this fragment is inside ActivityNewsFeed (showing news feed list)
        {

        }
        if (postData.getPostUserId().equals(SessionHelper.getUserId(mContext)))
        {
            imgOptionsMenu.setVisibility(View.VISIBLE);
            setOptionsMenuClickListener();
        }
    }



    // setting listener on option menu icon to delete and edit post
    private void  setOptionsMenuClickListener()
    {
        imgOptionsMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_EDIT,"Edit Post")
                        .addOption(BottomSheetOption.OPTION_DELETE,"Delete Post");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
                {
                    @Override
                    public void onOptionSelected(int option)
                    {
                        switch (option)
                        {
                            case BottomSheetOption.OPTION_EDIT:
                                Intent i = new Intent(mContext, ActivityNewsFeedPost.class);
                                i.putExtra(ActivityNewsFeedPost.KEY_POST_DATA,postData);
                                i.putExtra(ActivityNewsFeedPost.KEY_EDIT_OR_CREATE,ActivityNewsFeedPost.ACTION_EDIT_POST);
                                ((AppCompatActivity)mContext).startActivityForResult(i, ConstHelper.RequestCode.CREATE_EDIT_NEWS_FEED_POST);

                                break;

                            case BottomSheetOption.OPTION_DELETE:
                               // deletePost(getAdapterPosition(), postData.getPostId());
                                break;
                        }
                    }
                });

                try {
                    options.show(((AppCompatActivity)mContext).getSupportFragmentManager(),   ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
                }catch (ClassCastException e){}
            }
        });
    }


    // setting listener on comment layout at bottom to make comment and see comment
    private void setCommentLayoutClickListener()
    {
        layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityNewsFeedComments.class);
                intent.putExtra(ActivityNewsFeedComments.KEY_POST_DATA, postData);
                mContext.startActivity(intent);
            }
        });
    }


    // setting data if it's shared post
    private void setPostShared()
    {
        if (!postData.getPostSharedMessage().equals(NewsFeed.POST_NOT_SHARED)) {
            txtSharedByName.setVisibility(View.VISIBLE);
            txtSharedPostMessage.setVisibility(View.VISIBLE);
            txtSharedByName.setText(postData.getPostByUserName()+ "  shared this post");
            txtSharedPostMessage.setText(postData.getPostSharedMessage());
        }
    }



    // # setting post media
    private void setPostMedia()
    {
        String mediaUrl = postData.getPostMediaUrl();
        if (mediaUrl.equals(""))
            imgPost.setVisibility(View.GONE);
        else
        {
            imgPost.setVisibility(View.VISIBLE);
            //int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
            Glide.with(mContext)
                    .load(mediaUrl)
                    .apply(new RequestOptions().error(R.drawable.img_default_black))
                    .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                    // .apply(new RequestOptions().override(picDimen1,picDimen1))
                    .into(imgPost);
        }
    }


    // setting click listener to like the post or remove like if already liked
    private void setLikeClickListener() {
        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (postData == null || mListener == null)
                    return;

                if(postData.getLikeStatus().equals(NewsFeed.POST_NOT_LIKED))
                {
                    postData.setLikeStatus(NewsFeed.POST_LIKED);
                    imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
                    txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                }
                else if (postData.getLikeStatus().equals(NewsFeed.POST_LIKED))
                {
                    postData.setLikeStatus(NewsFeed.POST_NOT_LIKED);
                    imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
                    txtLike.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
                }

                mListener.onPostOptionsMenuClicked(postData);
                //mPresenterNewsFeed.updateLikeOnPost(postData);  // updating like on server
            }
        });
    }



    // setting posted date
    private void setPostDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = postData.getPostDateTime();
        Date date1=null;
        try {
            date1 = simpleDateFormat.parse(date);
            SimpleDateFormat newformat = new SimpleDateFormat("dd-MM-yyyy   hh:mm a", Locale.getDefault());
            String dateInReqFormat = newformat.format(date1);
            txtDate.setText(dateInReqFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    // # setting click listener on profile image
    private void setOnProfileImageClickListener()
    {
        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Map<String, String> post = news_feed_data_list.get(position);
                if (postData == null || mContext == null)
                    return;

                Intent intent = new Intent(mContext, ActivityProfileView.class);
                intent.putExtra(ConstHelper.Key.ID, postData.getPostUserId());
                mContext.startActivity(intent);
            }
        });
    }


    /*private void deletePost(final int position, final String postId)
    {
        Log.i(TAG,"deletePost() method called, deleting post id = "+postId);
        RetrofitClient client = RetrofitHelper.getClient();
        Call<Response> responseCall  = client.deleteNewsFeed("1", postId);
        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Response response1 = response.body();
                if (response1 != null)
                {
                    Log.i(TAG,"deleted post id = "+response1.getPostId());
                    if (Response.STATUS_SUCCESS.equals(response1.getResponseStatus()))
                    {
                        int positionInAdapter = getPostPosition(response1.getPostId());
                        Log.i(TAG,"delete, positionInAdapter = "+positionInAdapter);
                        if (positionInAdapter > -1)
                        {
                            mListPostData.remove(positionInAdapter);
                            notifyItemRemoved(positionInAdapter);
                        }
                    }
                }
                else
                    Log.e(TAG,"response1 is null in deletePost()");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.i(TAG,"retrofit, onFailure() method called = "+t.getMessage());
                t.printStackTrace();

            }
        });
    }*/



}
