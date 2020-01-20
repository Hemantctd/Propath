package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;

import org.ctdworld.propath.R;

import org.ctdworld.propath.activity.ActivityNutritionComment;
import org.ctdworld.propath.activity.ActivityNutritionFeedPost;
import org.ctdworld.propath.activity.ActivityNutritionShare;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractNutritionFeed;
import org.ctdworld.propath.database.RetrofitClient;
import org.ctdworld.propath.database.RetrofitHelper;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.fragment.FragmentNutrition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.NewsFeedDeleteResponse;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AdapterNutritionFeed extends RecyclerView.Adapter<AdapterNutritionFeed.MyViewHolder> {

    private static final String TAG = AdapterNutritionFeed.class.getSimpleName();
    private Context mContext;  // activity context
    private List<NutritionFeed.PostData> mListPostData;  // contains post data list
    private ContractNutritionFeed.Presenter mPresenterNutritionFeed;  // presenter to request to server
    private boolean mShowAnimation = true; //
    private List<AdapterNutritionFeed.MyViewHolder> mHolderList;
    private FragmentNutrition fragmentNutrition;

    public AdapterNutritionFeed(Context context, ContractNutritionFeed.Presenter presenter, FragmentNutrition fragmentNutrition) {
        mContext = context;
        mListPostData = new ArrayList<>();
        mPresenterNutritionFeed = presenter;
        mHolderList = new ArrayList<>();
        this.fragmentNutrition = fragmentNutrition;
    }


    public void addNutritionFeedList(List<NutritionFeed.PostData> listPostData) {
        mListPostData = listPostData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_news_feed, parent, false);

        return new MyViewHolder(view);
    }

    public void filterList(List<NutritionFeed.PostData> listPostData) {
        mListPostData = listPostData;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        // handleAnimation(holder.layout);
        NutritionFeed.PostData postData = mListPostData.get(holder.getAdapterPosition());

        holder.txtProfileName.setText(postData.getPostByUserName());
        holder.txtDate.setText(DateTimeHelper.getDateTime(postData.getPostDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        setPostMessage(postData, holder); // setting post message
        setLikeCommentCount(postData, holder); // setting total number of likes and comments on post
        setLikePost(postData, holder); // # setting data if post is liked or not, and listener
        setProfileImage(postData, holder); // # setting profile image
        setOptionsMenu(postData, holder);   // # showing or hiding option menu icon, setting click listener
        setCommentLayoutClickListener(postData, holder);  // to make and see comments
        setOnShareClicked(postData, holder.layoutShare); // setting click listener on share layout to share post
        setSharedPostData(postData, holder);  // setting data for shared post
        setPostMedia(postData, holder);  // setting post media
        setOnProfileImageClickListener(postData, holder);   // # setting click listener on profile image
    }


    @Override
    public int getItemCount() {
        return mListPostData.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;//super.getItemViewType(position);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mHolderList.add(holder);
        Log.i(TAG, "onViewAttachedToWindow , position = " + holder.getAdapterPosition() + " , holder count = " + mHolderList.size());

    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mHolderList.remove(holder);
        Log.i(TAG, "onViewDetachedFromWindow , position = " + holder.getAdapterPosition() + " , holder count = " + mHolderList.size());
    }


    // # ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {
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
        private ImageView mPlay;
        private View layout;
        private VideoView mVideoView;
        private ImageView mute, unmute;
        // private YouTubePlayerView mYoutubePlayerView;
        //  private View mYoutubeFragmentContainer;
        private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView mYoutubePlayer;


        public MyViewHolder(View view) {
            super(view);
            layout = view;
            imgOptionsMenu = view.findViewById(R.id.img_options_menu);
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
            // playerView = view.findViewById(R.id.you_tube_link);
            //  mPlayerLayout = view.findViewById(R.id.you_tube_layout);
            mPlay = view.findViewById(R.id.play_btn);
            unmute = view.findViewById(R.id.unmute);
            mute = view.findViewById(R.id.mute);
            mVideoView = view.findViewById(R.id.video_view);
//            mYoutubePlayerView = view.findViewById(R.id.you_tube_link);
//            mYoutubeFragmentContainer = view.findViewById(R.id.fragment_youtube_container);
            mYoutubePlayer = view.findViewById(R.id.youtube_player);
        }
    }


    private void handleAnimation(View layout) {
        if (mShowAnimation) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
            layout.setAnimation(animation);
        }

        mShowAnimation = false;
    }


    // # setting click listener on profile image
    private void setOnProfileImageClickListener(final NutritionFeed.PostData postData, MyViewHolder holder) {
        holder.imgProfilePic.setOnClickListener(new View.OnClickListener() {
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


    // called after like is updated on server, refreshing data
    public void onPostUpdated(NutritionFeed.PostData postData) {
        int adapterPosition = getPostPosition(postData.getPostId());
        if (adapterPosition == ConstHelper.NOT_FOUND)
            return;

        mListPostData.set(adapterPosition, postData);
        notifyItemChanged(adapterPosition, postData);
    }


    // called after like is updated on server, refreshing data
    public void addPost(NutritionFeed.PostData postData) {
        if (mListPostData != null) {
            mListPostData.add(0, postData);
            notifyItemInserted(0);
        }
    }


    private void deletePost(final String postId) {
        Log.i(TAG, "deletePost() method called, deleting post id = " + postId);
        RetrofitClient client = RetrofitHelper.getClient();
        Call<NewsFeedDeleteResponse> responseCall = client.deleteNewsFeed("1", postId);
        responseCall.enqueue(new Callback<NewsFeedDeleteResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsFeedDeleteResponse> call, @NonNull retrofit2.Response<NewsFeedDeleteResponse> response) {
                NewsFeedDeleteResponse response1 = response.body();
                if (response1 != null) {
                    Log.i(TAG, "deleted post id = " + response1.getPostId());
                    if (NewsFeedDeleteResponse.STATUS_SUCCESS.equals(response1.getResponseStatus(response1))) {
                        int positionInAdapter = getPostPosition(response1.getPostId());
                        Log.i(TAG, "delete, positionInAdapter = " + positionInAdapter);
                        if (positionInAdapter > -1) {
                            mListPostData.remove(positionInAdapter);
                            notifyItemRemoved(positionInAdapter);
                        }
                    }
                } else
                    Log.e(TAG, "response1 is null in deletePost()");
            }

            @Override
            public void onFailure(@NonNull Call<NewsFeedDeleteResponse> call, @NonNull Throwable t) {
                Log.i(TAG, "retrofit, onFailure() method called = " + t.getMessage());
                t.printStackTrace();

            }
        });
    }


    // # this method returns position of the post
    private int getPostPosition(String postId) {
        int postPosition = ConstHelper.NOT_FOUND;

        for (int i = 0; i < mListPostData.size(); i++) {
            Log.i(TAG, "post id in getPostPosition = " + mListPostData.get(i).getPostId());
            if (postId.equals(mListPostData.get(i).getPostId()))
                return i;
        }


        return postPosition;
    }


    // to share post
    private void setOnShareClicked(final NutritionFeed.PostData postData, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ActivityNutritionShare.class);
                i.putExtra(ActivityNutritionShare.KEY_POST_DATA, postData);
                fragmentNutrition.startActivityForResult(i, ConstHelper.RequestCode.ACTIVITY_NUTRITION_FEED_SHARE);
            }
        });
    }


    // # setting if post is liked or not
    private void setLikePost(NutritionFeed.PostData postData, MyViewHolder holder) {
        if (postData.getLikeStatus().equals(NutritionFeed.POST_NOT_LIKED)) {
            holder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
            holder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
            holder.txtLike.setText(R.string.like);  // like text if post is not liked

        } else if (postData.getLikeStatus().equals(NutritionFeed.POST_LIKED)) {
            holder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
            holder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
            holder.txtLike.setText(R.string.liked);  // Liked text if post is liked
        }

        setLikeClickListener(postData, holder); // setting click listener to like the post or remove like if already liked
    }


    // # setting total number of likes and comments on post
    private void setPostMessage(NutritionFeed.PostData postData, MyViewHolder holder) {
        if (postData.getPostMessage() == null || postData.getPostMessage().isEmpty())
            holder.txtPostMessage.setVisibility(View.GONE);
        else {
            holder.txtPostMessage.setVisibility(View.VISIBLE);
            holder.txtPostMessage.setText(postData.getPostMessage());
        }
    }


    // # setting total number of likes and comments on post
    private void setLikeCommentCount(NutritionFeed.PostData postData, MyViewHolder holder) {
        String likeText = "Like"; // like text if post is not liked
        String commentText = "Comment"; // comment text if there is only 1 comment

        if (Integer.parseInt(postData.getPostLikeCount()) > 1)
            likeText = "Likes";  // comment text if there are more than 1 comment

        if (Integer.parseInt(postData.getPostCommentCount()) > 1)
            commentText = "Comments";  // comment text if there are more than 1 comment

        holder.txtCountLikeComment.setText(String.format("%s %s | %s %s", postData.getPostLikeCount(), likeText, postData.getPostCommentCount(), commentText));
    }


    // setting profile image
    private void setProfileImage(NutritionFeed.PostData postData, MyViewHolder holder) {
        if (postData == null)
            return;

        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        Glide.with(mContext)
                .load(postData.getPostByUserProfilePic())
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen, picDimen))
                .into(holder.imgProfilePic);
    }


    // showing or hiding option menu icon, setting click listener
    private void setOptionsMenu(NutritionFeed.PostData postData, AdapterNutritionFeed.MyViewHolder holder) {

        if (postData.getPostUserId().equals(SessionHelper.getUserId(mContext))) {
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
            setOptionsMenuClickListener(postData, holder);
        } else
            holder.imgOptionsMenu.setVisibility(View.GONE);

    }


    // setting listener on option menu icon to delete and edit post
    private void setOptionsMenuClickListener(final NutritionFeed.PostData postData, final AdapterNutritionFeed.MyViewHolder holder) {
        // checking if this is shared post or not if this is shared post then the post can not be edited
        final boolean sharedPost = postData.getPostSharedBy() != null && !postData.getPostSharedBy().isEmpty() && !postData.getPostSharedBy().equals(NutritionFeed.POST_NOT_SHARED);

        holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetOption.Builder builder = new BottomSheetOption.Builder();
                builder.addOption(BottomSheetOption.OPTION_DELETE, "Delete Post");
                if (!sharedPost)  // if this is not shared post
                {
                    builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit Post");
                    builder.addOption(BottomSheetOption.OPTION_HIDE, "Hide Post");
                }


                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
                        switch (option) {
                            case BottomSheetOption.OPTION_EDIT:
                                Intent i = new Intent(mContext, ActivityNutritionFeedPost.class);
                                i.putExtra(ActivityNutritionFeedPost.KEY_POST_DATA, postData);
                                i.putExtra(ActivityNutritionFeedPost.KEY_EDIT_OR_CREATE, ActivityNutritionFeedPost.ACTION_EDIT_POST);
                                fragmentNutrition.startActivityForResult(i, ConstHelper.RequestCode.CREATE_EDIT_NUTRITION_FEED_POST);

                                break;


                            case BottomSheetOption.OPTION_DELETE:
                                String title = mContext.getString(R.string.are_you_sure);
                                String message = "Your post will be deleted";
                                DialogHelper.showCustomDialog(mContext, title, "Coming Soon...", "Delete", "Close", new DialogHelper.ShowDialogListener() {
                                    @Override
                                    public void onOkClicked() {

                                    }

                                    @Override
                                    public void onCancelClicked() {

                                    }
                                });
                                break;


                         /*   case BottomSheetOption.OPTION_HIDE:
                                DialogHelper.showSimpleCustomDialog(mContext, "Coming Soon..");
                                break;*/
                        }
                    }
                });

                try {
                    options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // setting listener on comment layout at bottom to make comment and see comment
    private void setCommentLayoutClickListener(final NutritionFeed.PostData postData, MyViewHolder holder) {
        holder.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityNutritionComment.class);
                intent.putExtra(ActivityNutritionComment.KEY_POST_DATA, postData);
                AppCompatActivity appCompatActivity = (AppCompatActivity) mContext;
                if (appCompatActivity != null)
                    fragmentNutrition.startActivityForResult(intent, ConstHelper.RequestCode.ACTIVITY_NUTRITION_FEED_COMMENT);
            }
        });
    }


    // setting data if it's shared post
    private void setSharedPostData(NutritionFeed.PostData postData, MyViewHolder holder) {
        if (!postData.getPostSharedMessage().isEmpty() && !postData.getPostSharedMessage().equals("0")) {
            holder.txtSharedPostMessage.setVisibility(View.VISIBLE);
            holder.txtSharedPostMessage.setText(postData.getPostSharedMessage());
        } else
            holder.txtSharedPostMessage.setVisibility(View.GONE);


        // if post is shared
        if (!postData.getPostSharedBy().isEmpty() && !postData.getPostSharedBy().equals("0")) {
            holder.txtSharedByName.setVisibility(View.VISIBLE);
            holder.txtSharedByName.setText(String.format("%s  shared this post", postData.getPostSharedBy()));
        } else
            holder.txtSharedByName.setVisibility(View.GONE);

    }


    // # setting post media
    private void setPostMedia(final NutritionFeed.PostData postData, final MyViewHolder holder) {
        String mediaUrl = postData.getPostMediaUrl();
        if (mediaUrl == null || mediaUrl.equals("")) {
            holder.imgPost.setVisibility(View.GONE);
            holder.mVideoView.setVisibility(View.GONE);
            holder.mute.setVisibility(View.GONE);
            holder.unmute.setVisibility(View.GONE);
            holder.mYoutubePlayer.setVisibility(View.GONE);
            holder.mPlay.setVisibility(View.GONE);
        } else {
            holder.mPlay.setVisibility(View.GONE);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = MimeTypeMap.getFileExtensionFromUrl(mediaUrl);

            if (mimeTypeMap != null && extension != null) {
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                if (mimeType != null && mimeType.contains("video"))  // showing video
                {
                    showVideo(holder, postData);
                } else // showing image
                {
                    Log.i(TAG, "showing image");

                    holder.imgPost.setVisibility(View.VISIBLE);
                    holder.mVideoView.setVisibility(View.GONE);
                    holder.mPlay.setVisibility(View.GONE);
                    holder.mute.setVisibility(View.GONE);
                    holder.unmute.setVisibility(View.GONE);
                    holder.mYoutubePlayer.setVisibility(View.GONE);
                    // holder.mYoutubePlayerView.setVisibility(View.GONE);

                    Glide.with(mContext)
                            .load(postData.getPostMediaUrl())
                            .apply(new RequestOptions().override(UtilHelper.getScreenWidth(mContext), 230))
                            .into(holder.imgPost);
                }
            }
        }

        if (UtilHelper.isYoutubeUrl(postData.getPostMessage())) {
            showYouTubeVideo(holder, postData);
        }


    }


    // setting click listener to like the post or remove like if already liked
    private void setLikeClickListener(final NutritionFeed.PostData postData, final MyViewHolder holder) {
        holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilHelper.isConnectedToInternet(mContext))
                    likeDislikePost(postData);
                else
                    DialogHelper.showSimpleCustomDialog(mContext, mContext.getString(R.string.title_no_connection), mContext.getString(R.string.message_no_connection));
            }
        });
    }


    // called when user clicks on like/dislike layout
    private void likeDislikePost(NutritionFeed.PostData postData) {
        if (postData == null || mPresenterNutritionFeed == null)
            return;

        int likeCount = Integer.parseInt(postData.getPostLikeCount());

        if (postData.getLikeStatus().equals(NutritionFeed.POST_NOT_LIKED)) {
            postData.setPostLikeCount(String.valueOf(likeCount + 1));
            postData.setLikeStatus(NutritionFeed.POST_LIKED);
        } else if (postData.getLikeStatus().equals(NutritionFeed.POST_LIKED)) {
            postData.setPostLikeCount(String.valueOf(likeCount - 1));
            postData.setLikeStatus(NutritionFeed.POST_NOT_LIKED);
        }

        onPostUpdated(postData);

        mPresenterNutritionFeed.updateLikeOnPost(postData);  // updating like on server
    }


    // # playing video which was uploaded from device. managing video volume etc...
    private void showVideo(final AdapterNutritionFeed.MyViewHolder holder, NutritionFeed.PostData postData) {
        Log.i(TAG, "showing video");
        holder.imgPost.setVisibility(View.GONE);
        holder.mVideoView.setVisibility(View.VISIBLE);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        HashMap<String, String> map = new HashMap<String, String>();
        retriever.setDataSource(String.valueOf(Uri.parse(postData.getPostMediaUrl())), map);
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        retriever.release();


        Log.i(TAG, "video height and width" + height + "&" + width);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        holder.mVideoView.setLayoutParams(params);

        holder.mVideoView.setVideoURI(Uri.parse(postData.getPostMediaUrl()));
        holder.mVideoView.requestFocus();
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        holder.unmute.setVisibility(View.VISIBLE);
        holder.mute.setVisibility(View.GONE);
        // holder.mVideoView.start();
        holder.unmute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mVideoView.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                    holder.unmute.setVisibility(View.GONE);
                    holder.mute.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(holder.mVideoView, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        holder.mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mVideoView.isPlaying()) {
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    holder.unmute.setVisibility(View.VISIBLE);
                    holder.mute.setVisibility(View.GONE);
                } else {
                    Snackbar.make(holder.mVideoView, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        holder.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                Log.i(TAG, "onPreparedListener called");

                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                holder.unmute.setVisibility(View.VISIBLE);
                holder.mute.setVisibility(View.GONE);
                holder.mVideoView.start();
              //  mp.setLooping(true);

            }
        });

        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                holder.mVideoView.start();
            }
        });

        holder.mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });


    }


    // # showing youtube video thumbnail and playing video
    private void showYouTubeVideo(final MyViewHolder holder, final NutritionFeed.PostData postData) {
        Log.i(TAG, "you tube url : " + postData.getPostMessage());
        final String url = UtilHelper.getYoutubeVideoThumbnailUrl(postData.getPostMessage());
        Log.i(TAG, "you tube image : " + url);
        if (url.equals(ConstHelper.NOT_FOUND_STRING))
            return;


        holder.imgPost.setVisibility(View.GONE);
        holder.mPlay.setVisibility(View.GONE);
        holder.mVideoView.setVisibility(View.GONE);
        holder.unmute.setVisibility(View.GONE);
        holder.mute.setVisibility(View.GONE);
        //   holder.mYoutubeFragmentContainer.setVisibility(View.GONE);
        //   holder.mYoutubePlayerView.setVisibility(View.GONE);
        holder.mYoutubePlayer.setVisibility(View.VISIBLE);

      /*  holder.mYoutubePlayer.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(postData.getPostMessage(), 0);
            }
        });*/

        holder.mYoutubePlayer.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {

                String videoId = UtilHelper.getYoutubeVideoID(postData.getPostMessage());
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                    // youTubePlayer.play();
                }
            }
        });


        //   holder.mYoutubePlayer.release();



        /*Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                // .apply(new RequestOptions().override(picDimen1,picDimen1))
                .into(holder.imgPost);*/

        // playYouTubeVideo(holder, postData.getPostMessage());
        // ((Activity)mContext).getFragmentManager().beginTransaction().add(R.id.fragment_youtube_container, FragmentYoutubePlayer.newInstance(postData.getPostMessage())).commit();


        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(mContext, ActivityYouTubePlayer.class);
                intent.putExtra(ConstHelper.Key.URL, postData.getPostMessage());
                mContext.startActivity(intent);*/


                //   ((Activity)mContext).getFragmentManager().beginTransaction().add(R.id.fragment_youtube_container, FragmentYoutubePlayer.newInstance(postData.getPostMessage())).commit();
            }
        });
    }


    private void playYouTubeVideo(AdapterNutritionFeed.MyViewHolder holder, final String link) {
        /*holder.mYoutubePlayerView.initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                if (!b) {
                    try {
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        if (UtilHelper.getYoutubeVideoID(link) != null)
                        {
                            youTubePlayer.loadVideo(UtilHelper.getYoutubeVideoID(link));
                            youTubePlayer.play();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AppController.getContext(),"Something Went Wrong with this video ",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog((Activity) mContext, 1).show();

                } else {

                    Toast.makeText(mContext, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });*/
    }


    public void onScrollingStopped() {
        for (AdapterNutritionFeed.MyViewHolder holder : mHolderList) {
            setPostMedia(mListPostData.get(holder.getAdapterPosition()), holder);
        }
    }

}
