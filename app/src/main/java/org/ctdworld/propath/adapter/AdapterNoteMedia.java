package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentFullImage;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNoteMedia extends RecyclerView.Adapter<AdapterNoteMedia.MyViewHolder>
{
    private static final String TAG = AdapterNoteMedia.class.getSimpleName();
    private Context mContext;
    private List<Notes.Media> mListMedia = new ArrayList<>();
    private FragmentManager mFragmentManager;


    public AdapterNoteMedia(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
    }


    // to add new media
    public void addMedia(Notes.Media media)
    {
        if (mListMedia!= null && media != null)
        {
            mListMedia.add(media);
            notifyItemInserted(getItemCount()-1);
        }
    }


    // to add Media list
    public void addMediaList(List<Notes.Media> mediaList)
    {
        if (mediaList != null)
            {
            this.mListMedia = mediaList;
            notifyDataSetChanged();
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_media,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final Notes.Media media = mListMedia.get(position);

        // # setting data to views
        if (media != null)
        {
            Log.i(TAG,"loading image");
            int imageDimen = (int) mContext.getResources().getDimension(R.dimen.notesMediaImgSize);
            int imageSize = UtilHelper.convertDpToPixel(mContext, imageDimen);
            Glide.with(mContext)
                    .load(media.getMediaUrl())
                    .apply(new RequestOptions().override(imageSize, imageSize))
                    .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                    .apply(new RequestOptions().error(R.drawable.img_default_black))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.image);

        }

        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG,"showing image in full size******************************************************************");
                FragmentFullImage fragmentFullImage = FragmentFullImage.getInstance(createImageList(mListMedia), position);
                fragmentFullImage.show(mFragmentManager, "");

            }
        });



    }


    @Override
    public int getItemCount() {
        return mListMedia.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
       CircleImageView image;
        public MyViewHolder(View view)
        {
            super(view);
            image = view.findViewById(R.id.recycler_note_media_img);
        }
    }



    // this method creates and returns a list image path or url
    private List<String> createImageList(List<Notes.Media> mediaList)
    {
        List<String> listImage = new ArrayList<>();
        for (Notes.Media media: mediaList)
        {
            listImage.add(media.getMediaUrl());
        }

        return listImage;
    }

}
