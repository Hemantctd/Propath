package org.ctdworld.propath.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentFullImage;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.model.RepAchievement;

import java.util.ArrayList;
import java.util.List;

public class AdapterRepAchievementImages extends  RecyclerView.Adapter<AdapterRepAchievementImages.MyViewHolder>
{
    private static final String TAG = AdapterRepAchievementImages.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;
    private List<RepAchievement> mListRepAchievement;
    private ArrayList<String> mListPicUrl;
    private FragmentManager mFragmentManger;


    public AdapterRepAchievementImages(Context context, FragmentManager fragmentManager, Handler handler)
    {
        this.mContext = context;
        this.mListRepAchievement= new ArrayList<>();
        mHandler = handler;
        mListPicUrl = new ArrayList<>();
        this.mFragmentManger = fragmentManager;

    }

    public void addListRepAchievement(List<RepAchievement> listRepAchievement)
    {
        this.mListRepAchievement = listRepAchievement;
        createPicUrl(listRepAchievement);  // putting Pic URL in mPicUrlList object of List
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_rep_achievement_images,null);

       /* RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);*/
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        Log.i(TAG,"onBindViewHolder  called ");



        RepAchievement repAchievement = mListRepAchievement.get(position);
        final String imageUrl = repAchievement.getRepMediaUrl();

        int imageSize =  mContext.getResources().getDimensionPixelSize(R.dimen.repAchievementAdapterImageSize);
       // int imageSize = UtilHelper.convertDpToPixel(mContext,R.dimen.repAchievementAdapterImageSize);
       // Log.i(TAG,"image url = "+imageUrl);
       // Log.i(TAG,"image size = "+imageSize);


        Glide.with(mContext)
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
               // .apply(new RequestOptions().error(R.drawable.img_default_image))
                .apply(new RequestOptions().override(imageSize))
                .apply(new RequestOptions().centerCrop())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.imgRepAchievement.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imgRepAchievement);



        holder.imgRepAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentFullImage fragmentFullImage = FragmentFullImage.getInstance(mListPicUrl,position);
                fragmentFullImage.show(mFragmentManger, ConstHelper.Tag.Fragment.FULL_IMAGE);
            }
        });



        /* new Thread(new Runnable() {
            @Override
            public void run()
            {
                RepAchievement repAchievement = mListRepAchievement.get(position);
                final String team = repAchievement.getRepTeam();
                final String location = repAchievement.getRepLocation();
                final String fromMonth = repAchievement.getRepFromMonth();
                final String fromYear = repAchievement.getRepFromYear();
                final String toMonth = repAchievement.getRepToMonth();
                final String toYear = repAchievement.getRepToYear();
                final String rolo_position = repAchievement.getRepRole();

                Log.i(TAG,"Team = "+team);

                mHandler.post(new Runnable()
                {
                    @Override
                    public void run() {
                        holder.txtTeam.setText(team);
                        holder.txtLocation.setText(location);
                        holder.txtFrom.setText(fromMonth+"-"+fromYear);
                        holder.txtTo.setText(toMonth+"-"+toYear);
                        holder.txtRolePosition.setText(rolo_position);
                    }
                });

            }
        });*/
    }

    @Override
    public int getItemCount()
    {
      //  Log.i(TAG,"total rep achievement = "+mListRepAchievement.size());
        if (mListRepAchievement != null)
            return mListRepAchievement.size();
        else return 0;
    }




    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgRepAchievement;
        public MyViewHolder(View view)
        {
            super(view);
            imgRepAchievement = view.findViewById(R.id.recycler_rep_achievement_image);
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


   /* private void loadImages(String imageUrl, final ImageView imageView, final int imageSize)
    {
        RemoteServer remoteServer = new RemoteServer(mContext);
        remoteServer.getBitmap(imageUrl, new RemoteServer.BitamapListener() {
            @Override
            public void onBitmapResponse(Bitmap bitmap) {
                if (bitmap != null)
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapErrorResponse(VolleyError error)
            {
                imageView.setVisibility(View.GONE);
            }
        },imageSize,imageSize);
    }*/



}
