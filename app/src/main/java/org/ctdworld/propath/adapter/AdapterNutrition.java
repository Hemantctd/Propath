package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNutritionComment;
import org.ctdworld.propath.activity.ActivityNutritionPost;
import org.ctdworld.propath.activity.ActivityNutritionShare;
import org.ctdworld.propath.contract.ContractNutrition;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.NewsFeed;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNutrition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterNutrition extends RecyclerView.Adapter<AdapterNutrition.MyViewHolder> implements View.OnClickListener, ContractNutrition.View {
    private static final String TAG = AdapterNutrition.class.getSimpleName();
    private Context mContext;
    private List<Nutrition> mNutritionList;
    private MediaController mc;
    private ContractNutrition.Presenter mPresenter;
    private AdapterNutrition.MyViewHolder mHolder;
    private boolean mShowAnimation = true; //

    public AdapterNutrition(Context context, List<Nutrition> nutritionList) {
        this.mContext = context;
        this.mNutritionList = nutritionList;
        mPresenter = new PresenterNutrition(mContext, this);
    }


    public void updateNutrition(List<Nutrition> nutritionList) {
        this.mNutritionList = nutritionList;
        notifyDataSetChanged();
    }


    public void filterList(ArrayList<Nutrition> filterdNames) {
        this.mNutritionList = filterdNames;
        notifyDataSetChanged();
    }


    // called after like is updated on server, refreshing data
    public void onPostUpdated(Nutrition nutrition) {
        int adapterPosition = getPostPosition(nutrition.getPost_id());
        if (adapterPosition == ConstHelper.NOT_FOUND)
            return;

        mNutritionList.set(adapterPosition, nutrition);
        notifyItemChanged(adapterPosition, nutrition);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_nutrition_layout, null);

        return new MyViewHolder(view);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        mHolder = holder;
        if (mShowAnimation) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
            holder.layout.setAnimation(animation);
        }
        mShowAnimation = false;


        final Nutrition nutritionData = mNutritionList.get(position);
        setOptionsMenu();  // setting options menu
        setLikePost();
        handleMedia(nutritionData, holder);

        /*if (nutritionData.getLike().equals("1"))
        {
            holder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
            holder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
        }*/
        if (!nutritionData.getPost_share_text().isEmpty() && !nutritionData.getPost_share_text().equals("0")) {
            holder.txtSharedByName.setVisibility(View.VISIBLE);
            holder.txtSharedPostMessage.setVisibility(View.VISIBLE);
            holder.txtSharedByName.setText(String.format("%s  shared this post", nutritionData.getPost_share_by()));
            holder.txtSharedPostMessage.setText(nutritionData.getPost_share_text());
        }
        holder.txtSharedByName.setVisibility(View.GONE);
        holder.txtSharedPostMessage.setVisibility(View.GONE);


        holder.txtCountLikeComment.setText(String.format("%s Like | %s Comments", nutritionData.getLike_count(), nutritionData.getComment_count()));
        holder.txtProfileName.setText(nutritionData.getUser_name());
        holder.txtDateTime.setText(DateTimeHelper.getDateTime(nutritionData.getDate_time(), DateTimeHelper.FORMAT_DATE_TIME));
        holder.txtPostMessage.setText(nutritionData.getTitle());


        String picUrl = nutritionData.getUser_profile_image();
        int picDimen = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
        UtilHelper.loadImageWithGlide(mContext, picUrl, R.drawable.img_default_black, picDimen, picDimen, holder.imgProfilePic);

        holder.layoutComment.setOnClickListener(this);
        holder.layoutShare.setOnClickListener(this);

        
      /*  holder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nutritionData.getLike().equals("0"))
                {
                    nutritionData.setLike("1");
                    Log.d(TAG,"like_count_data " +nutritionData.getLike_count());
                    mPresenter.likeDislike(position,nutritionData);
                    holder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
                    holder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                }
                else
                {
                    nutritionData.setLike("0");
                    Log.d(TAG,"like_count_data_1_value  " +nutritionData.getLike_count());
                    mPresenter.likeDislike(position,nutritionData);
                    holder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
                    holder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
                }
            }
        });*/

       /* holder.edit_profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(mContext, ActivityNutritionPost.class);
                editIntent.putExtra(ActivityNutritionPost.POST_TYPE,ActivityNutritionPost.TYPE_EDIT);
                editIntent.putExtra("nutrition_data", (Serializable) nutritionData);
                editIntent.putExtra("media",nutritionData.getMedia_url());
                mContext.startActivity(editIntent);

            }
        });*/


        holder.layoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityNutritionComment.class);
               //   intent.putExtra(ActivityNutritionComment.NUTRITION_DATA, nutritionData);
                mContext.startActivity(intent);
            }
        });

        holder.layoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityNutritionShare.class);
                intent.putExtra(ActivityNutritionShare.NUTRITION_DATA, nutritionData);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mNutritionList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
       /* private VideoView mVideoView;
        private View mLayoutMedia;
        private FrameLayout videoViewWrapper;
        private RelativeLayout like_layout, comment_layout, share_layout, optionsLayout;
        private ImageView profile_image, nutrition_images, like_btn, options_img;
        private TextView profile_name, message, date, count, like_text, shared_by_text, shared_by;
        private View layout;*/


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
        private TextView txtDateTime;   // posted date
        private TextView txtPostMessage;  // message while posting
        private TextView txtCountLikeComment; // shows like and comment count below posted image
        private TextView txtLike;  // like text in layoutLike at bottom
        private ImageView mPlay, mute, unmute;
        private VideoView videoView;
        private FrameLayout videoViewWrapper;
        private View layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            imgOptionsMenu = view.findViewById(R.id.img_options_menu);
            txtSharedPostMessage = view.findViewById(R.id.share_by_text);
            txtSharedByName = view.findViewById(R.id.shared_by);
            imgProfilePic = view.findViewById(R.id.profile_image);
            txtProfileName = view.findViewById(R.id.profile_name);
            txtDateTime = view.findViewById(R.id.date);
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
            videoView = view.findViewById(R.id.video_view);
            videoViewWrapper = view.findViewById(R.id.videoViewWrapper);
            unmute = view.findViewById(R.id.unmute);
            mute = view.findViewById(R.id.mute);





            /*like_layout = view.findViewById(R.id.like_layout);
            comment_layout = view.findViewById(R.id.comment_layout);
            optionsLayout = view.findViewById(R.id.optionsLayout);
            share_layout = view.findViewById(R.id.share_layout);
            options_img = view.findViewById(R.id.options_img);
            profile_image = view.findViewById(R.id.profile_image);
            nutrition_images = view.findViewById(R.id.nutrition_images);
            like_btn = view.findViewById(R.id.like_btn);
            profile_name = view.findViewById(R.id.profile_name);
            message = view.findViewById(R.id.message);
            date = view.findViewById(R.id.date);
            count = view.findViewById(R.id.count);
            like_text = view.findViewById(R.id.like_text);
            shared_by_text = view.findViewById(R.id.share_by_text);
            shared_by = view.findViewById(R.id.shared_by);
            mLayoutMedia = view.findViewById(R.id.frame_layout);
            mVideoView = view.findViewById(R.id.nutrition_video_view);
            videoViewWrapper = view.findViewById(R.id.videoViewWrapper);*/
        }


    }


    // # setting if post is liked or not
    private void setLikePost() {
        Nutrition nutrition = mNutritionList.get(mHolder.getAdapterPosition());

        if (nutrition.getLike().equals(NewsFeed.POST_NOT_LIKED)) {
            mHolder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
            mHolder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorDarkGrey));
            mHolder.txtLike.setText(R.string.like);  // like text if post is not liked

        } else if (nutrition.getLike().equals(NewsFeed.POST_LIKED)) {
            mHolder.imgLike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));
            mHolder.txtLike.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
            mHolder.txtLike.setText(R.string.liked);  // Liked text if post is liked
        }

        setLikeClickListener(nutrition); // setting click listener to like the post or remove like if already liked
    }


    // setting click listener to like the post or remove like if already liked
    private void setLikeClickListener(final Nutrition nutrition) {

        mHolder.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilHelper.isConnectedToInternet(mContext))
                    likeDislikePost(nutrition);
                else
                    DialogHelper.showSimpleCustomDialog(mContext, mContext.getString(R.string.title_no_connection), mContext.getString(R.string.message_no_connection));
            }
        });
    }


    // called when user clicks on like/dislike layout
    private void likeDislikePost(Nutrition nutrition) {
        if (nutrition == null || mPresenter == null)
            return;

        int likeCount = Integer.parseInt(nutrition.getLike_count());
        if (nutrition.getLike().equals(NewsFeed.POST_NOT_LIKED)) {
            nutrition.setLike_count(String.valueOf(likeCount + 1));
            nutrition.setLike(NewsFeed.POST_LIKED);
        } else if (nutrition.getLike().equals(NewsFeed.POST_LIKED)) {
            nutrition.setLike_count(String.valueOf(likeCount - 1));
            nutrition.setLike(NewsFeed.POST_NOT_LIKED);
        }

        onPostUpdated(nutrition);

        //mPresenterNewsFeed.updateLikeOnPost(nutrition);  // updating like on server
        mPresenter.likeDislike(mHolder.getAdapterPosition(), nutrition);
    }


    // checking if media is video or image
    private void handleMedia(Nutrition nutritionData, final MyViewHolder holder) {
        Nutrition nutrition = mNutritionList.get(mHolder.getAdapterPosition());
        String media = nutrition.getMedia_url();

        if (media.equals("")) {
            mHolder.imgPost.setVisibility(View.GONE);
            mHolder.videoView.setVisibility(View.GONE);

        } else {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String extension = MimeTypeMap.getFileExtensionFromUrl(media);
            if (mimeTypeMap != null && extension != null) {
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
                if (mimeType != null && mimeType.contains("video"))  // showing video
                {
                    holder.imgPost.setVisibility(View.GONE);
                    holder.videoView.setVisibility(View.VISIBLE);

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    HashMap<String, String> map = new HashMap<String, String>();
                    retriever.setDataSource(String.valueOf(Uri.parse(nutritionData.getMedia_url())), map);
                    int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    retriever.release();

                    Log.i(TAG, "getting image height and width" + height + "&" + width);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    holder.videoView.setLayoutParams(params);

                    holder.videoView.setVideoURI(Uri.parse(nutritionData.getMedia_url()));
                    holder.videoView.requestFocus();
                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    holder.unmute.setVisibility(View.VISIBLE);
                    holder.mute.setVisibility(View.GONE);

                    holder.unmute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.videoView.isPlaying()) {
                                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
                                holder.unmute.setVisibility(View.GONE);
                                holder.mute.setVisibility(View.VISIBLE);
                            } else {
                                Snackbar.make(mHolder.videoView, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                    holder.mute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.videoView.isPlaying()) {
                                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                holder.unmute.setVisibility(View.VISIBLE);
                                holder.mute.setVisibility(View.GONE);
                            } else {
                                Snackbar.make(mHolder.videoView, "Video is not playing", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                    holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                            holder.unmute.setVisibility(View.VISIBLE);
                            holder.mute.setVisibility(View.GONE);
                            holder.videoView.start();
                            mp.setLooping(true);
                        }
                    });

                    holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.videoView.start();
                        }
                    });
                    holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {

                            Log.d("API123", "What " + what + " extra " + extra);
                            return false;
                        }
                    });

                    holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.videoView.start();
                        }
                    });
                    holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {

                            Log.d("API123", "What " + what + " extra " + extra);
                            return false;
                        }
                    });

                } else // showing image
                {
                    holder.imgPost.setVisibility(View.VISIBLE);
                    holder.videoView.setVisibility(View.GONE);
                    holder.unmute.setVisibility(View.GONE);
                    holder.mute.setVisibility(View.GONE);
                    Glide.with(mContext)
                            .load(nutritionData.getMedia_url())
                            .apply(new RequestOptions().override(UtilHelper.getScreenWidth(mContext), 230))
                            .into(holder.imgPost);
                }
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void handleVideo(Nutrition nutrition) {
        mHolder.videoView.setVisibility(View.VISIBLE);
        mHolder.videoViewWrapper.setVisibility(View.VISIBLE);

        mHolder.videoView.setMediaController(new MediaController(mContext));
        mHolder.videoView.setVideoURI(Uri.parse(nutrition.getMedia_url()));
        mHolder.videoView.requestFocus();

        mHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // mHolder.videoView.start();

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        mc = new MediaController(mContext);
                        mHolder.videoView.setMediaController(mc);
                        mc.setAnchorView(mHolder.videoView);
                        ((ViewGroup) mc.getParent()).removeView(mc);

                        mHolder.videoViewWrapper.addView(mc);
                        mc.setVisibility(View.INVISIBLE);
                    }
                });
                mHolder.videoView.start();

            }
        });

        mHolder.videoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mc != null) {
                    mc.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mc.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }

                return false;
            }


        });

    }


    private void setOptionsMenu() {
        final Nutrition nutrition = mNutritionList.get(mHolder.getAdapterPosition());

        // if post is not shared and created by logged in user
        //if (nutrition.getPost_share_by().equals("0") && nutrition.getUserId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
        if (nutrition.getUserId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
            mHolder.imgOptionsMenu.setVisibility(View.VISIBLE);
        else
            mHolder.imgOptionsMenu.setVisibility(View.GONE);


        mHolder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                        .addOption(BottomSheetOption.OPTION_EDIT, "Edit Post");

                FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(builder.build());
                options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
                    @Override
                    public void onOptionSelected(int option) {
                        Log.i(TAG, "bottom sheet options selected, option = " + option);
                        if (option == BottomSheetOption.OPTION_EDIT) {
                            Intent editIntent = new Intent(mContext, ActivityNutritionPost.class);
                            editIntent.putExtra(ActivityNutritionPost.POST_TYPE, ActivityNutritionPost.TYPE_EDIT);
                            editIntent.putExtra("nutrition_data", nutrition);
                            editIntent.putExtra("media", nutrition.getMedia_url());
                            mContext.startActivity(editIntent);
                        }
                    }
                });

                try {
                    options.show(((AppCompatActivity) mContext).getSupportFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
                } catch (ClassCastException ignored) {
                }
            }
        });

    }


    // # this method returns position of the post
    private int getPostPosition(String postId) {
        int postPosition = ConstHelper.NOT_FOUND;

        for (int i = 0; i < mNutritionList.size(); i++) {
            if (postId.equals(mNutritionList.get(i).getPost_id()))
                return i;
        }


        return postPosition;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.comment_layout:
                Intent intent = new Intent(mContext, ActivityNutritionComment.class);

                mContext.startActivity(intent);
                break;

            case R.id.share_layout:
                Intent intentShare = new Intent(mContext, ActivityNutritionShare.class);
                mContext.startActivity(intentShare);
                break;


        }
    }


    @Override
    public void onGetReceivedNutritionComments(List<Nutrition> nutritionList) {

    }

    @Override
    public void onGetReceivedNutritionData(List<Nutrition> nutritionList) {

    }

    @Override
    public void onSuccess(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }

    @Override
    public void onLikeDislike(int position, Nutrition nutrition) {
        Log.d(TAG, "position........" + position);
        Log.d(TAG, "nutrition........" + nutrition.getLike_count());

        mNutritionList.set(position, nutrition);
        notifyItemChanged(position, nutrition);
    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {

    }

    @Override
    public void onShowMessage(String message) {
    }

}

