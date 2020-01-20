package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityNutritionComment;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Nutrition;
import org.ctdworld.propath.model.NutritionFeed;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterNutritionComments extends RecyclerView.Adapter<AdapterNutritionComments.MyViewHolder> {

    private static final String TAG = AdapterNutritionComments.class.getSimpleName();
    private Context mContext;  // context
    private ActivityNutritionComment mActivityNutritionComments;  // activity object to call it's methods
    private List<NutritionFeed.PostData.PostComment> mPostCommentList;  // comment list

    // # Constructor
    public AdapterNutritionComments(Context mContext) {
        this.mContext = mContext;
        mActivityNutritionComments = (ActivityNutritionComment) mContext;
        mPostCommentList = new ArrayList<>();
    }


    // # Creating ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_news_feed_comments, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final NutritionFeed.PostData.PostComment postComment = mPostCommentList.get(holder.getAdapterPosition());

        holder.txtComment.setText(postComment.getCommentMessage());
        holder.txtCommentDateTime.setText(DateTimeHelper.getDateTime(postComment.getCommentDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
        holder.txtCommentUserName.setText(postComment.getCommentUserName());


        // # setting user pic who has commented
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        UtilHelper.loadImageWithGlide(mContext, postComment.getCommentUserProfilePic(), picDimen, picDimen, holder.imgCommentUserPic);


        // # showing options menu icon if comment has been done by the currently logged in user
        if (SessionHelper.getInstance(mContext).getUser().getUserId().equals(postComment.getCommentUserId()))
            holder.imgOptionsMenu.setVisibility(View.VISIBLE);
        else
            holder.imgOptionsMenu.setVisibility(View.GONE);


        // setting listener on options menu icon to delete and edit comment
        holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivityNutritionComments.onCommentOptionsMenuClicked(postComment);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPostCommentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtComment;   // comment message
        private TextView txtCommentDateTime;  // comment data
        private TextView txtCommentUserName;  // user name of the user who has commented
        private ImageView imgCommentUserPic;  // user profile pic who has commented
        private ImageView imgOptionsMenu;  // options menu to delete and edit comment
        private View layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            txtComment = view.findViewById(R.id.comments);
            txtCommentDateTime = view.findViewById(R.id.comments_date);
            txtCommentUserName = view.findViewById(R.id.user_profile_name);
            imgCommentUserPic = view.findViewById(R.id.user_profile_icon);
            imgOptionsMenu = view.findViewById(R.id.optionsImg);
        }


    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }



    // adding comment list
    public void addPostCommentList(List<NutritionFeed.PostData.PostComment> commentList) {
        mPostCommentList = commentList;
        notifyDataSetChanged();
    }



    // updating comment
    public void onCommentDeleted(NutritionFeed.PostData.PostComment postComment) {
        if (postComment == null)
            return;

        int position = getPosition(postComment.getCommentId());

        if (position == ConstHelper.NOT_FOUND)
            return;

        mPostCommentList.remove(position);
        notifyItemRemoved(position);
    }



    // updating comment
    public void onCommentUpdated(NutritionFeed.PostData.PostComment postComment) {
        if (postComment == null)
            return;

        int position = getPosition(postComment.getCommentId());

        if (position == ConstHelper.NOT_FOUND)
            return;

        mPostCommentList.set(position, postComment);
        notifyItemChanged(position, position);
    }




    // adding new comment. For now it's not being used because we are not getting comment id from server so comment list is loaded again from server
    public void onCommentAdded(NutritionFeed.PostData.PostComment postComment) {
        if (postComment == null)
        {
            mPostCommentList.add(postComment);
            notifyItemInserted(getItemCount() - 1);
        }
    }




    // getting item position in comment list to update comment in adapter
    private int getPosition(String commentId) {
        for (int i = 0; i < mPostCommentList.size(); i++) {
            if (commentId.equals(mPostCommentList.get(i).getCommentId()))
                return i;
        }

        return ConstHelper.NOT_FOUND;
    }
}
