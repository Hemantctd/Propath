package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.fragment.FragmentFullSizeImageRemoveUnwanted;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Notes;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNoteMediaCreate extends RecyclerView.Adapter<AdapterNoteMediaCreate.MyViewHolder>
{
    private static final String TAG = AdapterNoteMediaCreate.class.getSimpleName();
    private Context mContext;
    private ArrayList<Notes.Media> mListMedia;

    private FragmentManager mFragmentManager;

    public AdapterNoteMediaCreate(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mListMedia = new ArrayList<>();
    }


    // to add new media
    public void addMedia(Notes.Media media)
    {
        if (mListMedia!= null && media != null)
        {
            mListMedia.add(0, media);
            notifyItemInserted(0);
        }
    }


     // this method returns list of Note.Media
    public List<Notes.Media> getMediaList()
    {
        return mListMedia;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_note_media_create_edit,null);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final Notes.Media media = mListMedia.get(position);


        // # setting data to views
        if (media != null)
        {
            Log.i(TAG,"loading image");
            int imageDimen = (int) mContext.getResources().getDimension(R.dimen.notesMediaImgSize);
            int imageSize = UtilHelper.convertDpToPixel(mContext, imageDimen);
            Glide.with(mContext)
                    .load(media.getMediaUrl() /*Integer.valueOf(media.getMediaUrl())*/)
                    .apply(new RequestOptions().override(imageSize, imageSize))
                    .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                    .apply(new RequestOptions().error(R.drawable.img_default_black))
                    .apply(new RequestOptions().centerInside())
                    .into(holder.image);

        }

        holder.image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG,"showing image in full size******************************************************************");

                // getting current position of image, position returns old position(position at the time of setting) that's why i am using  holder.getAdapterPosition()
                int imagePosition = holder.getAdapterPosition();
                ArrayList<String> imageList = (ArrayList<String>) createImageList(mListMedia);
                FragmentFullSizeImageRemoveUnwanted fragment = FragmentFullSizeImageRemoveUnwanted.getInstance(imageList, "Images", "Done", imagePosition);
                fragment.setOnSendClickListener(new FragmentFullSizeImageRemoveUnwanted.OnSendClickListner() {
                    @Override
                    public void onSelectedImagesReceived(List<String> selectedImagesPath, List<String> removedImagesList)
                    {
                        Log.i(TAG,"final selected images count = "+selectedImagesPath.size());
                        ArrayList<Notes.Media> mediaList = (ArrayList<Notes.Media>) createMediaList(selectedImagesPath);
                        Log.i(TAG,"mediaList count = "+mediaList.size());
                        mListMedia = mediaList;
                        notifyDataSetChanged();
                    }
                });
                fragment.show(mFragmentManager, "");

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
       ImageView imgRemove;
        public MyViewHolder(View view)
        {
            super(view);
            image = view.findViewById(R.id.recycler_note_media_create_edit_img);
            imgRemove = view.findViewById(R.id.recycler_note_media_create_edit_img_remove);
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

    // this method creates List<Note.Media> from image list received after removing unnecessary images and returns List<Note.Media>
    private List<Notes.Media> createMediaList(List<String> listImage)
    {
        List<Notes.Media> filterMediaList = new ArrayList<>();  // it will contain new filltered media list
        for (int i=0 ; i<listImage.size() ; i++)
        {
            Log.i(TAG,"updating url count = "+i);
            String image = listImage.get(i);
            Notes.Media media = mListMedia.get(i);
            media.setMediaUrl(image);

            filterMediaList.add(media);
        }

        return filterMediaList;
    }

}
