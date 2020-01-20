package org.ctdworld.propath.helper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

public class DownloadHelper
{
    private static final String TAG = DownloadHelper.class.getSimpleName();
    private Context mContext;
    private static final String SDF_YOUTH_FOLDER = Environment.DIRECTORY_DOWNLOADS+"/SDF Youth/";
    private DownloadManager mDownloadManager;
    private long mEnqueueId;
    private BroadcastReceiver mReceiver;
    private DownloadHelperListener mDownloadHelperListener;
    private String mDownloadedFilePath = "";
    // this method downloads file


    // constructor
    public DownloadHelper(Context context, DownloadHelperListener listener) {
        this.mContext = context;
        this.mDownloadHelperListener = listener;
        this.mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.mEnqueueId = 0;
        createDownloadReceiver();
    }

    private String getMimeFromFileName(String fileName) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
        return map.getMimeTypeFromExtension(ext);
    }


    public interface DownloadHelperListener{void onDownloadSuccess(String path); void onDownloadFailed();}


    // method to download file from server using DownloadManager
    public void downloadFile(String fileUrl, String fileName)
    {
        // fileName = "testing file....";
        //   fileUrl = "http://www.africau.edu/images/default/sample.pdf"; // for pdf  testing
       // fileUrl = "https://www.radiantmediaplayer.com/media/bbb-360p.mp4";  // for video testing
        Log.i(TAG,"downloadFile() method called, url = "+fileUrl);

       // String fileName = FileHelper.getFileName(mContext, fileUrl);
        String fileExtension = "."+MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        mDownloadedFilePath = SDF_YOUTH_FOLDER+fileName+fileExtension;
        Log.i(TAG,"downloadFile() method called, url = "+fileUrl+" ,  extension = "+fileExtension);



       /* File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName+fileExtension);
        Log.i(TAG,"file = "+file);
        if (file !=null)
        {
            Log.i(TAG,"file path = "+file.getPath());
            Log.i(TAG,"file absolute path = "+file.getAbsolutePath());
            Log.i(TAG,"file exist = "+file.exists());
            Log.i(TAG,"file directory = "+file.isDirectory());
            Log.i(TAG,"file file = "+file.isFile());
        }
        else
            Log.e(TAG,"file is null");*/

    //    Log.i(TAG,"downloaded file path = "+mDownloadedFilePath);





        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(SDF_YOUTH_FOLDER,fileName+fileExtension);
        request.setDescription(fileName+" is downloading");

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


    //# this method download file from server
    private void download()
    {

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
                    //Toast.makeText(mContext,"Downloading Failed...", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"downloading file failed.....");
                    if (mDownloadHelperListener !=  null)
                        mDownloadHelperListener.onDownloadFailed();
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
                                Log.i(TAG,"File downloaded successfully , "+mDownloadedFilePath);
                                if (mDownloadHelperListener != null)
                                    mDownloadHelperListener.onDownloadSuccess(mDownloadedFilePath);

                                try {
                                    mContext.unregisterReceiver(mReceiver);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
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
        mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
