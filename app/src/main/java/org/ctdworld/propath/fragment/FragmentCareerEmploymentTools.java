package org.ctdworld.propath.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.prefrence.SessionHelper;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class FragmentCareerEmploymentTools extends Fragment
{

    // categories of employment tools
    public static final int CATEGORY_APPLICATION_LETTER = 1;
    public static final int CATEGORY_CURRICULUM_VITAE = 2;
    public static final int CATEGORY_INTERVIEW_CHECKLIST = 3;
    public static final int CATEGORY_NOTHING = 4;

    private static final int REQUEST_CODE_SELECT_FILE = 100;


    private final String TAG = FragmentCareerEmploymentTools.class.getSimpleName();

    Context mContext;
/*    Toolbar mToolbar;
    TextView mTxtToolbarTitle;*/
    FloatingActionButton mFloatingButton;
    String mStrSelectedCategory = "";  // this contains selected category from dialog
    DialogLoader mDialogLoader;


   // PresenterEmploymentTools mPresenter;

    String mStrSelectedFileName = null;
    Uri mSelectedFileUri = null;



  /*  @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_career_employment_tools);
    }*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_career_employment_tools, container, false);


        init(view);  // initializing views
        //prepareToolbar();  // preparing toolbar
        setListeners();     // setting listeners on views

        // showing FragmentCareerEmploymentType, to show data
        getChildFragmentManager().beginTransaction().add(R.id.employment_tools_fragment_container,new FragmentCareerEmploymentType(),ConstHelper.Tag.Fragment.CAREER_EMPLOYMENT_TOOLS).commit();

        // checking if user is logged in as Welfare Manger then floating button will be shown to upload file.  by default floating button is gone


        if (SessionHelper.getInstance(mContext).getUser().getRoleId().equals(RoleHelper.PLAYER_WELFARE_ROLE_ID))
            mFloatingButton.setVisibility(View.VISIBLE);
        else
            Log.i(TAG,"you are not logged in as welfare");



        return view;
    }

    // setting listeners on views
    private void setListeners()
    {

        // showing Dialog to choose employment tools category to upload file into a particular category, categories are mentioned at top
        mFloatingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                String storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
                String permissionMessage = "Please give read storage permission to upload file...";
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(storagePermission)) // checking for storage permission
                {
                    permissionHelper.requestPermission(storagePermission, permissionMessage);
                }
                else
                {
                    DialogChooseEmploymentToolsCategory dialogChooseEmploymentToolsCategory = new DialogChooseEmploymentToolsCategory();
                    dialogChooseEmploymentToolsCategory.setCategorySelectedListener(new DialogChooseEmploymentToolsCategory.OnCategorySelectedListener() {
                        @Override
                        public void onCategorySelected(int selectedCategory)
                        {
                            switch (selectedCategory)
                            {
                                case CATEGORY_APPLICATION_LETTER:
                                    Log.i(TAG,"selected category = "+"application letter");
                                    //  Toast.makeText(mContext,"application letter",Toast.LENGTH_LONG).show();
                                    mStrSelectedCategory = "Application";
                                    selectFileFromDevice();
                                    break;

                                case CATEGORY_CURRICULUM_VITAE:
                                    Log.i(TAG,"selected category = "+"curriculum vitae");
                                    //  Toast.makeText(mContext,"curriculum vitae",Toast.LENGTH_LONG).show();
                                    mStrSelectedCategory = "Curriculum";
                                    selectFileFromDevice();
                                    break;

                                case CATEGORY_INTERVIEW_CHECKLIST:
                                    Log.i(TAG,"selected category = "+"interview checklist");
                                    // Toast.makeText(mContext,"interview checklist",Toast.LENGTH_LONG).show();
                                    mStrSelectedCategory = "Interview";
                                    selectFileFromDevice();

                                    break;

                                case CATEGORY_NOTHING:
                                    Log.i(TAG,"no category has been selected from dialog");
                                    //   Toast.makeText(mContext,"No category has been selected",Toast.LENGTH_LONG).show();

                                    break;

                                default:
                                    Log.i(TAG,"no category has been selected from dialog");
                                    break;
                            }

                        }
                    });

                    dialogChooseEmploymentToolsCategory.show(getChildFragmentManager(),"");
                }
            }
        });
    }


    // initializing views
    private void init(View view)
    {
        mContext = getContext();
        mDialogLoader = DialogLoader.getInstance("Uploading...");
        //mPresenter = new PresenterEmploymentTools(mContext, this);
      /*  mToolbar = view.findViewById(R.id.career_plan_toolbar);
        mTxtToolbarTitle = view.findViewById(R.id.toolbar_txt_title);*/
        mFloatingButton = view.findViewById(R.id.career_employment_tools_floating_button);
    }


  /*  // preparing toolbar
    private void prepareToolbar()
    {
      *//*  setSupportActionBar(mToolbar);
        mTxtToolbarTitle.setText("Career");*//*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }*/





    // selecting file to upload as employment tools, called from setListeners() method
    private void selectFileFromDevice()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        // Intent createChooser = Intent.createChooser(intent,"Select File To Upload");
        startActivityForResult(intent,REQUEST_CODE_SELECT_FILE);

    }


    // contains selected file to upload as employment tools
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_FILE && data != null)
        {
            String fileType = FileHelper.getFileExtension(mContext, data.getData());
            String fileName = FileHelper.getFileName(mContext,data.getData());

            mStrSelectedFileName = fileName;
            mSelectedFileUri = data.getData();

            Log.i(TAG,"fileType = "+fileType);
            Log.i(TAG,"file name = "+fileName);
            if (fileType ==  null)  // checking if file type is null or not,
            {
                DialogHelper.showSimpleCustomDialog(mContext, "Please Select right file...", new DialogHelper.ShowSimpleDialogListener()
                {
                    @Override
                    public void onOkClicked() {
                        selectFileFromDevice();
                    }
                });
            }
            else
            {
                if (".pdf".contains(fileType) || ".docs".contains(fileType) || ".doc".contains(fileType) || ".docs".contains(fileType) || ".txt".contains(fileType))
                {
                    checkFileSize(data.getData());
                }
                else
                {
                    {
                        DialogHelper.showSimpleCustomDialog(mContext, "File not supported...", " Supported files: .pdf, .doc, .docx,  .txt", "Select", null, new DialogHelper.ShowSimpleDialogListener() {
                            @Override
                            public void onOkClicked() {
                                selectFileFromDevice();
                            }
                        });
                    }
                }
            }

        }
        else
            Log.e(TAG,"Some problem occurred in onActivityResult() method");
    }



    // this method checks file size, file size should be less than 5 mb, if size is more than 5 mb then a dialog will be shown
    private void checkFileSize(Uri fileUri)
    {
        String strFileSize = "";
        double fileSize =  FileHelper.getFileSizeInKB(mContext, fileUri);
        String fileName = FileHelper.getFileName(mContext, fileUri);
        String fileType = FileHelper.getFileExtension(mContext, fileUri);


        Log.i(TAG,"fileSize kb double = "+fileSize);
        if (fileSize > 1024)  // if file size is greater than 1 mb
        {
            fileSize = fileSize/1024;   // changing kb to mb
            strFileSize = (int)fileSize+" mb";
            Log.i(TAG,"fileSize mb double = "+fileSize);
            if (fileSize > 5)
            {
                DialogHelper.showSimpleCustomDialog(mContext, "File is large...","Please select file lest than 5 mb", "OK", null, new DialogHelper.ShowSimpleDialogListener() {
                    @Override
                    public void onOkClicked() {
                        selectFileFromDevice();
                    }
                });
            }
            else
            {
                strFileSize = (int)fileSize+" mb";
                String message = "File Name : "+fileName+" \nFile Size : "+strFileSize/*+"\nFile Type : "+fileType*/;
                showSelectedFileAndUpload(message,fileUri);  // this method will show file details and upload
            }
        }
        else {
            strFileSize = (int)fileSize + " kb";
            String message = "File Name : " + fileName + " \nFile Size : " + strFileSize/* + "\nFile Type : " + fileType*/;
            showSelectedFileAndUpload(message,fileUri);  // this method will show file details and upload
        }


    }


    // this method shows file details and uploads to server
    private void showSelectedFileAndUpload(String dialogMessage, final Uri uriSelectedFile)
    {
        DialogHelper.showCustomDialog(mContext, "Selected File...", dialogMessage, "Upload", "Cancel", new DialogHelper.ShowDialogListener()
        {
            @Override
            public void onOkClicked()  // in dialog ok text has been replace with Upload text
            {
                Log.i(TAG,"uploading file");
                uploadFile(uriSelectedFile);
            }

            @Override
            public void onCancelClicked()
            {
                Toast.makeText(mContext,"File not uploading", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // this method converts file into base64 and then upload, sets required parameters to EmploymentTools object to pass to presenter
    private void uploadFile(final Uri uri)
    {
        Log.i(TAG,"uploadFile() method called");
        if (mSelectedFileUri == null)
        {
            Log.e(TAG,"mSelectedFileUri is null in uploadFile() method ");
            return;
        }

        if (mDialogLoader != null)
            mDialogLoader.show(getChildFragmentManager(),"");

        // setting data to employment object to send on server
        String filePath = FileHelper.getFilePath(mContext, mSelectedFileUri);
        Log.i(TAG,"Selected path = "+filePath);
        if (filePath == null)
        {
            Log.e(TAG,"file path is null in uploadFile() method ");
            return;
        }


      /*  final CareerEmploymentData employmentTools = new CareerEmploymentData();
        employmentTools.setFileName(mStrSelectedFileName);
        employmentTools.setFilePathInStorage(filePath);*/

      Log.i(TAG,"selected file name = "+mStrSelectedFileName);

        String uuid = UUID.randomUUID().toString();
         try
        {
            new MultipartUploadRequest(mContext, uuid, "https://ctdworld.co/athlete/jsondata.php")
                    .addFileToUpload(filePath, "file")
                    .addParameter("welfare_add_docs","1")
                    .addParameter("file_name",mStrSelectedFileName)
                    .addParameter("welfare_id",SessionHelper.getInstance(mContext).getUser().getUserId())
                    .addParameter("categroy",mStrSelectedCategory)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.i(TAG,"onProgress() method called");

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e(TAG,"error while uploading file , "+exception.getMessage());
                            exception.printStackTrace();
                            DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            if (mDialogLoader != null)
                                mDialogLoader.dismiss();
                            Log.i(TAG,"file uploaded successfully");
                            DialogHelper.showSimpleCustomDialog(mContext,"File Uploaded Successfully...");

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            DialogHelper.showSimpleCustomDialog(mContext,"Cancelled...");
                        }
                    })
                    .startUpload();



        }
       catch (FileNotFoundException e)
        {
            Log.e(TAG,"FileNotFoundException in uploadFile() method = "+e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e)
        {
            Log.e(TAG,"MalformedURLException in uploadFile() method = "+e.getMessage());
            e.printStackTrace();
        }






       /* //Toast.makeText(mContext,"Under Process...", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"uploadFile() method called ");
        FileHelper fileHelper = new FileHelper();
        fileHelper.requestBase64File(mContext, uri, new FileHelper.Base64FileListener() {
            @Override
            public void onBase64FileReceived(String base64File)
            {
                if (base64File != null)
                {
                    Log.i(TAG,"fileBase64 = "+base64File);
                    employmentTools.setFileBase64(base64File);
                    mPresenter.uploadEmployment(employmentTools);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDialogLoader != null)
                                        mDialogLoader.dismiss();
                                }
                            },3000);

                        }
                    });

                }
                else
                {
                    if (mDialogLoader != null)
                        mDialogLoader.dismiss();
                    DialogHelper.showSimpleCustomDialog(mContext,"Error Occurred...");
                }
            }
        });*/
    }







/*    // this method will not be used because here we are not showing data
    @Override
    public void onReceivedEmploymentList(List<CareerEmploymentData> employmentToolsList) {
    }

    @Override
    public void onUploadSuccess()
    {
        if (mDialogLoader != null)
            mDialogLoader.dismiss();

        DialogHelper.showSimpleCustomDialog(mContext,"Uploaded Successfully...");
    }

    @Override
    public void onFailed(String message)
    {
        DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
    }

    // this method will not be used here
    @Override
    public void registerReceiver(BroadcastReceiver receiver) {

    }*/
}
