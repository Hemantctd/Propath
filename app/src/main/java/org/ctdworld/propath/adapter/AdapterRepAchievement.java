package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentFullImage;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.RepAchievement;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterRepAchievement extends  RecyclerView.Adapter<AdapterRepAchievement.MyViewHolder>
{

    private Context mContext;
    private ArrayList<String> mListPicUrl;
    private List<RepAchievement> mListRepAchievement;
    private FragmentManager mFragmentManager;
    private OnOptionSelected mListener;
    private boolean mIsSelfProfile;
    private AdapterRepAchievement.MyViewHolder mHolder;

    // listener
    public interface OnOptionSelected{void  onRepOptionSelected(int positionInAdapter, RepAchievement repAchievement);}



    public AdapterRepAchievement(Context context, String userId, FragmentManager fragmentManager, OnOptionSelected listener)
    {
        mContext = context;
        mListRepAchievement= new ArrayList<>();
        mListPicUrl = new ArrayList<>();
        mFragmentManager = fragmentManager;
        mIsSelfProfile = userId.equals(SessionHelper.getInstance(mContext).getUser().getUserId());
        mListener = listener;
    }

    public void addListRepAchievement(List<RepAchievement> listRepAchievement)
    {
        Collections.reverse(listRepAchievement);
        this.mListRepAchievement = listRepAchievement;
        createPicUrl(listRepAchievement);  // putting Pic URL in mPicUrlList object of List
    }

    public void onItemDeleted(int adapterPosition)
    {
        mListRepAchievement.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_profile_view_rep_achievement,null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        mHolder = holder;
        setViews();
        setUpOptionsMenu();
        setClickListenerOnImage();
        showMedia();




    }

    private void showMedia() {

        final RepAchievement repAchievement = mListRepAchievement.get(mHolder.getAdapterPosition());
        int mediaPicSize = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.imgSizeViewAchievement));

        if (!repAchievement.getRepMediaUrl().isEmpty())
            UtilHelper.loadImageWithGlide(mContext, repAchievement.getRepMediaUrl(), mediaPicSize, mediaPicSize, mHolder.imgMediaPic);
        else
            mHolder.imgMediaPic.setVisibility(View.GONE);
    }


    // setting click listener on media pic
    private void setClickListenerOnImage() {
        mHolder.imgMediaPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentFullImage fragmentFullImage = FragmentFullImage.getInstance(mListPicUrl,mHolder.getAdapterPosition());
                fragmentFullImage.show(mFragmentManager,"");
            }
        });
    }


    // # setting data on views
    private void setViews() {

        RepAchievement repAchievement = mListRepAchievement.get(mHolder.getAdapterPosition());

        mHolder.txtTeam.setText(String.format("Team : %s", repAchievement.getRepTeam()));
        mHolder.txtLocation.setText(String.format("Team : %s", repAchievement.getRepLocation()));
        mHolder.txtFrom.setText(String.format("From : %s-%s", repAchievement.getRepFromMonth(), repAchievement.getRepFromYear()));
        mHolder.txtTo.setText(String.format("To : %s-%s", repAchievement.getRepToMonth(), repAchievement.getRepToYear()));
        mHolder.txtRolePosition.setText(String.format("Role : %s", repAchievement.getRepRole()));
        mHolder.txtLink.setText(String.format("Link : %s", repAchievement.getRepLink()));

    }


    // handling option menu
    private void setUpOptionsMenu() {
        // if user is looking self rep achievement then there will be options to edit or delete rep achievement
        if (mIsSelfProfile)
            mHolder.ic_options_menu.setVisibility(View.VISIBLE);
        else
            mHolder.ic_options_menu.setVisibility(View.GONE);


        final RepAchievement repAchievement = mListRepAchievement.get(mHolder.getAdapterPosition());


        mHolder.ic_options_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mListener.onRepOptionSelected(mHolder.getAdapterPosition(), repAchievement);
            }
        });

    }

    @Override
    public int getItemCount()
    {
      //  Log.i(TAG,"total rep achievement = "+mListRepAchievement.size());
         return mListRepAchievement.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder
    {
        View layout;
        TextView txtTeam, txtLocation, txtFrom, txtTo, txtRolePosition, txtLink;
        ImageView imgMediaPic,ic_options_menu;


        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            ic_options_menu = view.findViewById(R.id.ic_options_menu);
            txtTeam = view.findViewById(R.id.recycler_rep_achievement_view_txt_team);
            txtLocation = view.findViewById(R.id.recycler_rep_achievement_txt_location);
            txtFrom = view.findViewById(R.id.recycler_rep_achievement_txt_from);
            txtTo = view.findViewById(R.id.recycler_rep_achievement_txt_to);
            txtRolePosition = view.findViewById(R.id.recycler_rep_achievement_txt_role_position);
            txtLink = view.findViewById(R.id.recycler_rep_achievement_txt_link);
            imgMediaPic = view.findViewById(R.id.recycler_rep_achievement_img_media);
        }
    }



    // fetching Rep Achievement pic URL and adding in mPicUrlList to send to FragmentFullImage to show full image
    private void createPicUrl(List<RepAchievement> listRepAchievement)
    {
        if (listRepAchievement != null)
        {
            if (listRepAchievement.size()>0)
            {
                for (int i=0 ; i<listRepAchievement.size() ; i++)
                {
                    RepAchievement repAchievement = listRepAchievement.get(i);
                    String imageUrl = repAchievement.getRepMediaUrl();
                    if (!imageUrl.isEmpty())
                        mListPicUrl.add(imageUrl);
                }
            }
        }
    }




}
