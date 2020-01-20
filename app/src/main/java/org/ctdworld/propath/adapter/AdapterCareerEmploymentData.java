package org.ctdworld.propath.adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityWebView;
import org.ctdworld.propath.contract.ContractEmploymentTools;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.model.CareerEmploymentData;

import java.util.ArrayList;
import java.util.List;

public class AdapterCareerEmploymentData extends RecyclerView.Adapter<AdapterCareerEmploymentData.MyViewHolder>
{
    private static final String TAG = AdapterCareerEmploymentData.class.getSimpleName();
    private Context mContext;
    private List<CareerEmploymentData> mListData;
    private long mEnqueueId = 0;
    private BroadcastReceiver mReceiver;
    private ContractEmploymentTools.Presenter mPresenterEmployment;
    private DownloadManager mDownloadManager;

    private String mStrDownloadedFilePath = "";





    public AdapterCareerEmploymentData(Context context, ContractEmploymentTools.Presenter presenter, ArrayList<CareerEmploymentData> documentList)
    {
        this.mContext = context;
        this.mListData=documentList;
        mPresenterEmployment = presenter;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        createDownloadReceiver(); // creating and registering receiver for download complete
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_career_employment_data,null);

        return new MyViewHolder(view);
    }



    public void updateList(List<CareerEmploymentData> listData)
    {
        this.mListData = listData;
        notifyDataSetChanged();
    }

    public void onFileDeleted(int positionDeletedFile)
    {
        Log.i(TAG,"onFileDeleted , position = "+positionDeletedFile);
        mListData.remove(positionDeletedFile);
        notifyItemRemoved(positionDeletedFile);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        Log.i(TAG,"onBindViewHolder called*************************************************************************************************************************");
        Log.i(TAG,"onBindViewHolder position = "+position);
        Log.i(TAG,"onBindViewHolder getAdapterPosition = "+holder.getAdapterPosition());
        Log.i(TAG,"onBindViewHolder getLayoutPosition = "+holder.getLayoutPosition());




        final CareerEmploymentData employmentData = mListData.get(position);

        Log.i(TAG,"Employment File Url = "+employmentData.getFileUrl());



        holder.fileName.setText(employmentData.getFileName());
        holder.date.setText(employmentData.getFileDate());
        holder.time.setText(employmentData.getFileTime());


        holder.imgLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityWebView.class);
                intent.putExtra(ActivityWebView.KEY_TOOLBAR,employmentData.getFileName());

                String driveLinkDocViewer = "http://drive.google.com/viewerng/viewer?embedded=true&url=";
                String finalUrl = driveLinkDocViewer+employmentData.getFileUrl();
                Log.i(TAG,"finalFileUrl = "+finalUrl);
                intent.putExtra(ActivityWebView.KEY_WEB_URL,finalUrl);

                mContext.startActivity(intent);

             //  String url = "http://www.africau.edu/images/default/sample.pdf";

             /* String fileType = "application/"+FileHelper.getFileTypeWithoutExtension(mContext,employmentData.getFileUrl());
               Log.i(TAG,"file type = "+fileType);
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setDataAndType(Uri.parse(employmentData.getFileUrl()), fileType );
               Intent intentChooser = Intent.createChooser(intent,"Open with...");
               mContext.startActivity(intentChooser);*/

            }
        });


        // downloading file
        holder.imgDownload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (permissionHelper.isPermissionGranted(permissionStorage))
                    downloadFile(employmentData.getFileUrl(),employmentData.getFileName());
                else
                {
                    permissionHelper.requestPermission(permissionStorage, "Storage Permission Required To Download FIle.");
                }

            }
        });

        holder.layoutDetails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view)
            {
                DialogHelper.showCustomDialog(mContext, "Delete File...", "This File Will be deleted", "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked()
                    {
                        Log.i(TAG,"onBindViewHolder position = "+position);
                        Log.i(TAG,"onBindViewHolder getAdapterPosition = "+holder.getAdapterPosition());
                        Log.i(TAG,"onBindViewHolder getLayoutPosition = "+holder.getLayoutPosition());
                        mPresenterEmployment.deleteFile(employmentData.getFileId(),holder.getLayoutPosition());
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }





    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView fileName, date, time;
        ImageView imgDownload, imgLeft;
        View view, layoutDetails;

        public MyViewHolder(View view)
        {
            super(view);
            this.view = view;
            fileName = view.findViewById(R.id.recycler_career_employment_data_txt_file_name);
            date = view.findViewById(R.id.recycler_career_employment_data_txt_date);
            time = view.findViewById(R.id.recycler_career_employment_data_txt_time);
            imgDownload = view.findViewById(R.id.recycler_career_employment_data_img_download);
            imgLeft = view.findViewById(R.id.recycler_career_employment_data_img_left);
            layoutDetails = view.findViewById(R.id.recycler_career_employment_data_layout);
        }
    }




    // this method downloads file
    private void downloadFile(String fileUrl, String fileName)
    {
       // fileName = "testing file....";
     //   fileUrl = "http://www.africau.edu/images/default/sample.pdf"; // for pdf  testing
      //  fileUrl = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";  // for video testing

        Log.i(TAG,"downloadFile() method called.......................................");


        String fileType = FileHelper.getFileExtension(mContext, String.valueOf(fileUrl));
        Log.i(TAG,"fileType = "+fileType);


        String fileDownload = fileName/*+fileType*/;
        //String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        mStrDownloadedFilePath = FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER/*+fileName+fileType*/;
        Log.i(TAG,"mStrDownloadedFilePath = "+mStrDownloadedFilePath);
        Log.i(TAG,"fileDownload = "+fileDownload);


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(fileName);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER,fileDownload);
        request.setDescription("Athlete Life....");

        if (mDownloadManager != null)
        {
            mEnqueueId = mDownloadManager.enqueue(request);
            // registering BroadcastReceiver te be notified when file is downloaded
            mContext.registerReceiver(mReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        else
        {
            Log.i(TAG,"downloadManager is null in downloadFile() method ");
            Toast.makeText(mContext,"Downloading Failed...", Toast.LENGTH_SHORT).show();
        }

    }



    // creating and registering receiver for download complete
    private void createDownloadReceiver()
    {
        // BroadcastReceiver te be notified when file is downloaded
        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Uri uri = intent.getData();
                if (intent.getAction() == null)
                {
                    Toast.makeText(mContext,"Downloading Failed...", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"downloading file failed.....");
                    return;
                }

                if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                {
                    Log.i(TAG,"file downloading completed......");

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(mEnqueueId);


                    Cursor cursor = mDownloadManager.query(query);
                    if (cursor != null)
                    {
                        if (cursor.moveToFirst())
                        {
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL)
                            {

                                Log.i(TAG,"File downloaded successfully , "+mStrDownloadedFilePath);
                                Toast.makeText(mContext, mStrDownloadedFilePath, Toast.LENGTH_LONG).show();

                                /* Uri uriDownloadedFile = mDownloadManager.getUriForDownloadedFile(mEnqueueId);
                                Log.i(TAG,"File downloaded successfully = "+uriDownloadedFile);
                                if (uriDownloadedFile != null)
                                    Log.i(TAG,"File downloaded successfully , "+uriDownloadedFile.getFilePath());


                                File file = new File(uriDownloadedFile.getFilePath());
                                if (file != null)
                                    Log.i(TAG,"file = "+file.getAbsolutePath());

                                if (uriDownloadedFile != null)
                                    Toast.makeText(mContext,"downloaded\nPath : "+uriDownloadedFile.getFilePath(), Toast.LENGTH_LONG).show();*/
                            }
                        }
                    }
                    else
                    {
                        Log.i(TAG,"cursor is null in downloadFile() method ");
                        Toast.makeText(mContext,"Downloading Failed...", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.i(TAG,"action is not DownloadManager.ACTION_DOWNLOAD_COMPLETE in downloadFile() method ");
                    Toast.makeText(mContext,"Downloading Failed...", Toast.LENGTH_SHORT).show();
                }

            }
        };



        //registering receiver for download complete
        mPresenterEmployment.registerReceiver(mReceiver);
    }

}
