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

public class AdapterNoteMediaEdit extends RecyclerView.Adapter<AdapterNoteMediaEdit.MyViewHolder>
{
    private static final String TAG = AdapterNoteMediaEdit.class.getSimpleName();
    private Context mContext;
    private List<Notes.Media> mListMedia;

    // # this List contains Note.Media which have come from server and have been removed from list
    private ArrayList<Notes.Media> mListMediaRemovedCameFromServer;
    private FragmentManager mFragmentManager;


    public AdapterNoteMediaEdit(Context context, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mListMedia = new ArrayList<>();
        this.mListMediaRemovedCameFromServer = new ArrayList<>();

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

    // to add media list
    public void addMediaList(List<Notes.Media> mediaList)
    {
        if (mediaList != null)
        {
            this.mListMedia = mediaList;
            notifyDataSetChanged();
        }
    }


     /* # this method returns new added Media(image) list. this does not include media which have come from server. it will be used to
    save on server in existing note*/
    public List<Notes.Media> getNewAddedMediaList()
    {
        ArrayList<Notes.Media> filteredMediaList = new ArrayList<>();
        for (Notes.Media media : mListMedia)
        {
            if (!media.isMediaFromServer())    // if image is from server it's set true while getting data from server
                filteredMediaList.add(media);
        }
        return filteredMediaList;
    }


    // this method returns List of media which have come from server and have been removed from list
    public List<Notes.Media> getRemovedMediaListCameFromServer()
    {
        return mListMediaRemovedCameFromServer;
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
                ArrayList<String> imageList = (ArrayList<String>) createImagePathList(mListMedia);
                FragmentFullSizeImageRemoveUnwanted fragment = FragmentFullSizeImageRemoveUnwanted.getInstance(imageList, "Images", "Done", imagePosition);
                fragment.setOnSendClickListener(new FragmentFullSizeImageRemoveUnwanted.OnSendClickListner() {
                    @Override
                    public void onSelectedImagesReceived(List<String> selectedImagesPath, List<String> removedImagesList)
                    {
                        Log.i(TAG,"final selected images count = "+selectedImagesPath.size());
                        handleRemovedMediaFromList(removedImagesList);
                       /* ArrayList<Note.Media> mediaList = (ArrayList<Note.Media>) createMediaList(selectedImagesPath);
                        Log.i(TAG,"mediaList count = "+mediaList.size());
                         mListMedia = mediaList;
                         notifyDataSetChanged();*/
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
    private List<String> createImagePathList(List<Notes.Media> mediaList)
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


    // this method creates List<Note.Media> from List of images in which some images have been removed
    private void handleRemovedMediaFromList(List<String> removedImagesList)
    {
        for (String removedImagePath : removedImagesList)  // fetching image path from removedImageList
        {
            Log.i(TAG,"removed image = "+removedImagePath);

            for (int i=0; i<mListMedia.size(); i++)   // fetching Note.Media object from mListMedia
            {
                Notes.Media media = mListMedia.get(i);  // fetching image url(path) from mListMedia

                //# now checking if removed image and current media url(path) are same and media has come from server. If true then adding to mListMediaRemovedCameFromServer
                if (media.getMediaUrl().equals(removedImagePath))
                {
                  //  Log.i(TAG,"matching removed and current list image , server status = "+media.isMediaFromServer());

                    // removing removed image from mListMedia list
                    mListMedia.remove(i);
                    notifyItemRemoved(i);

                    // adding removed image to mListMediaRemovedCameFromServer
                    if (media.isMediaFromServer())
                    {
                        Log.i(TAG,"adding removed item in list , and media is from server");
                        mListMediaRemovedCameFromServer.add(media);
                    }
                }
            }
        }
    }

}
