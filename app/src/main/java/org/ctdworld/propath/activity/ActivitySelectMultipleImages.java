package org.ctdworld.propath.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.ctdworld.propath.R;



/**
 * 029
 *
 * @author Paresh Mayani (@pareshmayani)
 * 030
 */

public class ActivitySelectMultipleImages extends AppCompatActivity
{
    private static final String TAG = ActivitySelectMultipleImages.class.getCanonicalName();

    private ArrayList<String> imageUrls;

    private DisplayImageOptions options;

    private ImageAdapter imageAdapter;

    GridView gridView;

    ProgressBar mProgressBar;


    private ImageLoader imageLoader;


    @Override

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multiple_images);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));


        gridView = findViewById(R.id.gridview);
        this.imageUrls = new ArrayList<String>();
        mProgressBar = findViewById(R.id.select_images_progress_bar);

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        //Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        Cursor imageCursor = null;
        try
        {
//            imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy+" DESC");
            imageCursor = getContentResolver().query(android.provider.MediaStore.Files.getContentUri("external"), columns, null, null, orderBy+" DESC");

        }
        catch (SecurityException e)
        {
            Log.e(TAG,"Security exception in onCreate() method , "+e.getMessage());
            e.printStackTrace();
        }

        imageAdapter = new ImageAdapter(ActivitySelectMultipleImages.this, imageUrls);


        try
        {
            for (int i = 0; i < imageCursor.getCount(); i++)
            {
                imageCursor.moveToPosition(i);
                int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageUrls.add(imageCursor.getString(dataColumnIndex));

                Log.i(TAG,"=====> Array path => " + imageUrls.get(i));
            }

            imageCursor.close();
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error in for loop while adding image to imgUrls , "+e.getMessage());
            e.printStackTrace();
        }




        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_default_black)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnFail(R.drawable.img_default_black)
                .showImageOnLoading(R.drawable.img_default_black)
                .build();




        gridView.setAdapter(imageAdapter);
        //gridView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // startImageGalleryActivity(position);
        // }
        //});
    }


    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {

        mProgressBar.setVisibility(View.VISIBLE);
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
     //   Toast.makeText(ActivitySelectMultipleImages.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
     //   Log.d(TAG, "Selected Items: " + selectedItems.toString());

        Intent intent = new Intent();
        intent.putStringArrayListExtra("data",selectedItems);
        setResult(RESULT_OK, intent);

        finish();

    }

 /*private void startImageGalleryActivity(int position) {
  Intent intent = new Intent(this, ImagePagerActivity.class);
  intent.putExtra(Extra.IMAGES, imageUrls);
  intent.putExtra(Extra.IMAGE_POSITION, position);
  startActivity(intent);
 }*/

    public class ImageAdapter extends BaseAdapter {
        ArrayList<String> mImageList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mImageList = new ArrayList<String>();
            this.mImageList = imageList;
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArray = new ArrayList<String>();

            for (int i = 0; i < mImageList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArray.add(mImageList.get(i));
                }
            }

            return mTempArray;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.adapter_select_multiple_images, null);
            }

            final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCheckBox.isChecked())
                        mCheckBox.setChecked(false);
                    else
                        mCheckBox.setChecked(true);
                }
            });

            imageLoader.displayImage("file://" + imageUrls.get(position), imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    Animation anim = AnimationUtils.loadAnimation(ActivitySelectMultipleImages.this, android.R.anim.fade_in);
                    imageView.setAnimation(anim);
                    anim.start();
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });


            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

            return convertView;

        }


        OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}

