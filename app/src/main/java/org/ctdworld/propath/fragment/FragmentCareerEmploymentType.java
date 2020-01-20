package org.ctdworld.propath.fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCareerEmploymentType extends Fragment
{
    private final String TAG = FragmentCareerEmploymentType.class.getSimpleName();


    TextView mTxtApplicationLetter, mTxtCurriculumVitae, mTxtInterviewCheckList;
    Context mContext;
    private DownloadManager mDownloadManager;
    String docFile = "";
    private String mStrDownloadedFilePath = "";
    private long mEnqueueId = 0;
    private BroadcastReceiver mReceiver;

    public FragmentCareerEmploymentType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_career_employment_types, container, false);

        init(view);
        createDownloadReceiver();

        mTxtApplicationLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                String url = "https://ctdworld.co/athlete/uploads/career/5c7902c639628Pro-Path-Letter-of-Application.doc";
                if (permissionHelper.isPermissionGranted(permissionStorage))
                {
                    Toast.makeText(mContext, "Starting Download...", Toast.LENGTH_SHORT).show();

                    mStrDownloadedFilePath = FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER;
                    String fileExtension = "."+MimeTypeMap.getFileExtensionFromUrl(url);


                    mDownloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDescription("Downloading...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER,"Application Letter"+fileExtension);
                   // Long reference = mDownloadManager.enqueue(request);

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
                else
                {
                    permissionHelper.requestPermission(permissionStorage, "Storage Permission Required To Download FIle.");
                }


            }
        });

        mTxtCurriculumVitae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                String url = "https://ctdworld.co/athlete/uploads/career/5c79033345004Pro-Path-Resume.doc";
                if (permissionHelper.isPermissionGranted(permissionStorage))
                {
                    Toast.makeText(mContext, "Starting Download...", Toast.LENGTH_SHORT).show();

                    String fileExtension = "."+MimeTypeMap.getFileExtensionFromUrl(url);


                    mDownloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER,"Curriculum Vitae"+fileExtension);


                  //  request.setMimeType("application/docx");
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
                else
                {
                    permissionHelper.requestPermission(permissionStorage, "Storage Permission Required To Download FIle.");
                }

            }
        });

        mTxtInterviewCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                String permissionStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                String url = "https://ctdworld.co/athlete/uploads/career/5c7903c734524My-employment-checklist.pdf";
                if (permissionHelper.isPermissionGranted(permissionStorage))
                {
                    Toast.makeText(mContext, "Starting Download...", Toast.LENGTH_SHORT).show();

                    String fileExtension = "."+MimeTypeMap.getFileExtensionFromUrl(url);
                    mDownloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(FileHelper.PUBLIC_DIRECTORY_WITH_APP_FOLDER,"Interview Checklist"+fileExtension);
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
                else
                {
                    permissionHelper.requestPermission(permissionStorage, "Storage Permission Required To Download FIle.");
                }

            }
        });

        return view;
    }

    private void init(View view) {
        mContext = getContext();
        mTxtApplicationLetter = view.findViewById(R.id.fragment_employment_tools_txt_application_letter);
        mTxtCurriculumVitae = view.findViewById(R.id.fragment_employment_tools_txt_curriculum_vitae);
        mTxtInterviewCheckList = view.findViewById(R.id.fragment_employment_tools_txt_interview_checklist);
    }

    // creating and registering receiver for download complete
    private void createDownloadReceiver() {
        // BroadcastReceiver te be notified when file is downloaded
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Uri uri = intent.getData();
                if (intent.getAction() == null) {
                    Toast.makeText(mContext, "Downloading Failed...", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "downloading file failed.....");
                    return;
                }

                if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    Log.i(TAG, "file downloading completed......");
                  //  Toast.makeText(mContext, "file downloading completed.....", Toast.LENGTH_LONG).show();

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(mEnqueueId);


                    Cursor cursor = mDownloadManager.query(query);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {

                                Log.i(TAG, "File downloaded successfully , " + mStrDownloadedFilePath);
                                Toast.makeText(mContext, mStrDownloadedFilePath, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Log.i(TAG, "cursor is null in downloadFile() method ");
                        Toast.makeText(mContext, "Downloading Failed...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "action is not DownloadManager.ACTION_DOWNLOAD_COMPLETE in downloadFile() method ");
                    Toast.makeText(mContext, "Downloading Failed...", Toast.LENGTH_SHORT).show();
                }

            }
        };

    }

    }
