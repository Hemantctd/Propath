package org.ctdworld.propath.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNoteDocumentEdit;
import org.ctdworld.propath.adapter.AdapterNoteLinkEdit;
import org.ctdworld.propath.adapter.AdapterNoteMediaEdit;
import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentSpeechRecognition;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.FileHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNotes;

import java.util.List;



public class ActivityNoteEdit extends AppCompatActivity implements ContractNotes.View
{
    private static final String TAG = ActivityNoteEdit.class.getSimpleName();

    Context mContext;


    // # permission helper object to check and request permission
    PermissionHelper mPermissionHelper;
    DialogLoader mLoader;
    ContractNotes.Presenter mPresenterNote;


    // # toolbar views
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    ImageView mToolbarImgSave;


    //views
   // TextView mTxtChangeCategory;
    TextView mTxtCategory;
    EditText mEditTitle, mEditDescription;


    // # views and variables for media list
    RecyclerView mRecyclerMedia;
    AdapterNoteMediaEdit mAdapterMedia;
    ImageView mImgAddMedia;  // to add media(image)


    // # views and variables for document list
    RecyclerView mRecyclerDocument;
    AdapterNoteDocumentEdit mAdapterDocument;
    ImageView mImgAddDocument;

    // # views and variables for link list
    RecyclerView mRecyclerLink;
    AdapterNoteLinkEdit mAdapterLink;
    ImageView mImgAddLink;
    EditText mEditAddLink;
    ImageView mImgMicTitle, mImgMicDescription;

    Notes mNote = null;

    /* to check if note is edited successfully.
    * If note is edited successfully then new data is loaded from server and set in setResult to send back.*/
    private boolean mIsNoteEditedSuccessfully;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        init();
        setToolbar();
        setAllAdapters();   // # setting all adapters
        requestNoteData();  // getting note data from server, and setting to edit
        showCreatedDataDetailsFragment();  // showing creator details and created or updated details
        setListener();

    }


    // to initialize
    private void init()
    {
        mContext = this;
        mPermissionHelper = new PermissionHelper(mContext);
        mPresenterNote = new PresenterNotes(mContext, this) {
            @Override
            public void onSuccess(String msg) {

            }

            @Override
            public void onHideProgress() {

            }

            @Override
            public void onSetViewsEnabledOnProgressBarGone() {

            }

            @Override
            public void onShowMessage(String connection_error) {

            }
        };
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbarImgSave = findViewById(R.id.toolbar_img_1_customisable);

        mImgMicTitle = findViewById(R.id.activity_note_edit_img_mic_title);
        mImgMicDescription = findViewById(R.id.activity_note_edit_img_mic_description);
        mTxtCategory = findViewById(R.id.activity_note_edit_txt_category);
        mTxtCategory = findViewById(R.id.activity_note_edit_txt_category);
        mEditTitle = findViewById(R.id.activity_note_edit_title);
        mEditDescription = findViewById(R.id.activity_note_edit_description);
      //  mTxtChangeCategory = findViewById(R.id.activity_note_edit_txt_change_category);
        mRecyclerMedia = findViewById(R.id.activity_note_edit_recycler_images);
        mRecyclerDocument = findViewById(R.id.activity_note_edit_recycler_document);
        mImgAddDocument = findViewById(R.id.activity_note_edit_img_add_docuemnt);
        mRecyclerLink = findViewById(R.id.activity_note_edit_recycler_link);
        mImgAddLink = findViewById(R.id.activity_note_edit_img_add_add_link);
        mEditAddLink = findViewById(R.id.activity_note_edit_edit_link);

        mImgAddMedia = findViewById(R.id.activity_note_edit_img_add_media);

        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                mNote = (Notes) bundle.getSerializable(ConstHelper.Key.DATA_MODAL);
        }

    }


    private void setListener()
    {

        mImgAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                selectImageFromDevice();
            }
        });



        mImgAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectDocumentFromDevice();
            }
        });


        /*adding link to note*/
        mImgAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String link = mEditAddLink.getText().toString().trim();
                if (!link.isEmpty())
                {
                    if (Patterns.WEB_URL.matcher(link).matches())
                    {
                        Log.i(TAG,"adding link to list");
                        Notes.Link addLink = new Notes.Link();
                        addLink.setLinkUrl(link);
                        addLink.setLinkFromServer(false);
                        mAdapterLink.addLink(addLink);

                        mEditAddLink.setText("");
                    }
                    else
                        Snackbar.make(mImgAddMedia,"Enter valid web link", Snackbar.LENGTH_SHORT).show();
                }
                else
                    Snackbar.make(mImgAddLink, "Please enter link", Snackbar.LENGTH_SHORT).show();

            }
        });



        mToolbarImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked();
            }
        });


        mTxtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(mContext, ActivityNoteSelectCategory.class),ConstHelper.RequestCode.SELECT);
            }
        });



        mImgMicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else
                {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty())
                                mEditTitle.setText(spokenText);
                            mEditTitle.requestFocus(EditText.FOCUS_RIGHT);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    fragmentSpeechRecognition.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.SPEECH_RECOGNITION);
                }

            }
        });


        mImgMicDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else
                {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty())
                                mEditDescription.setText(spokenText);
                            mEditDescription.requestFocus(EditText.FOCUS_RIGHT);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    fragmentSpeechRecognition.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.SPEECH_RECOGNITION);
                }

            }
        });

    }


    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }



    // setting data to its object, to show creator details and date time
    public CreatedDataDetails getCreatedDataDetails()
    {
        User user = SessionHelper.getInstance(mContext).getUser();
        if (user == null)
            return null;

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        createdDataDetails.setName(user.getName());
        createdDataDetails.setRoleId(user.getRoleId());
        createdDataDetails.setUserPicUrl(user.getUserPicUrl());
        createdDataDetails.setCreatedDate(DateTimeHelper.getCurrentSystemDateTime());
        createdDataDetails.setUpdatedDate(null);

        return createdDataDetails;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }



    // setting toolbar data
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        //    mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        if (mNote != null)
        {
            mToolbarTxtTitle.setText(getString(R.string.notes_edit));
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgSave.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mToolbarImgSave.setVisibility(View.VISIBLE);
        mToolbarImgSave.setLayoutParams(params);
        mToolbarImgSave.setImageResource(R.drawable.ic_notes_save);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // to get note data from server
    private void requestNoteData()
    {
        Log.i(TAG,"requesting note data");
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            mLoader = DialogLoader.getInstance("Loading...");
            mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
            try
            {
                //  mNote.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                mPresenterNote.getNoteData(mNote);

            }
            catch (Exception e)
            {
                hideLoader();
                Log.e(TAG,"Error while requesting for contacts , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            //  DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.no_connection),getString(R.string.connection_message));
            hideLoader();
        }
    }



    // saving edited note
    private void saveButtonClicked()
    {
        Log.i(TAG, "saveButtonClicked() method called");
        Notes addNoteData = new Notes();
        Notes deleteNoteData = new Notes();

        List<Notes.Link> editLinkList = null;

        // #fetching list of images to add new images and delete images
        if (mAdapterMedia != null)
        {
            // # fetching image list which will be added in note
            List<Notes.Media> addMediaList = mAdapterMedia.getNewAddedMediaList();
            if (addMediaList != null)
            {
                Log.i(TAG,"addMediaList size = "+addMediaList.size());
                addNoteData.setListMedia(addMediaList);
            }

            // # fetching image list which will be deleted from note
            List<Notes.Media> deleteImageList = mAdapterMedia.getRemovedMediaListCameFromServer();
            if (deleteImageList != null)
            {
                Log.i(TAG,"deleteImageList size = "+deleteImageList.size());
                deleteNoteData.setListMedia(deleteImageList);
            }
        }


        // #fetching list of documents to add new documents and delete document in note
        if (mAdapterDocument != null)
        {
            // # fetching document list which will be added in note
            List<Notes.Document> addDocumentList = mAdapterDocument.getNewAddedDocumentList();
            if (addDocumentList != null)
            {
                Log.i(TAG,"addDocumentList size = "+addDocumentList.size());
                addNoteData.setListDocument(addDocumentList);
            }

            // # fetching document list which will be deleted in note
            List<Notes.Document> deleteDocumentList = mAdapterDocument.getRemovedDocumentListCameFromServer();
            if (deleteDocumentList != null)
            {
                Log.i(TAG,"deleteDocumentList size = "+deleteDocumentList.size());
                deleteNoteData.setListDocument(deleteDocumentList);
            }
        }

        // #adding list of link to add in note
        if (mAdapterLink != null)
        {
            // # fetching link list which will be added in note
            List<Notes.Link> addLinkList = mAdapterLink.getNewAddedLinkList();
            if (addLinkList != null)
            {
                Log.i(TAG,"addLinkList size = "+addLinkList.size());
                addNoteData.setListLinks(addLinkList);
            }

            // # fetching link list which will be deleted in note
            List<Notes.Link> deleteLinkList = mAdapterLink.getRemovedDocumentListCameFromServer();
            if (deleteLinkList != null)
            {
                Log.i(TAG,"deleteLinkList size = "+deleteLinkList.size());
                deleteNoteData.setListLinks(deleteLinkList);
            }

            // # fetching link list which will be edited in note
            editLinkList = mAdapterLink.getEditedLinkList();
            if (editLinkList != null)
            {
                Log.i(TAG,"editLinkList size = "+editLinkList.size());
            }
        }


        // saving note data
        mLoader = DialogLoader.getInstance("Saving data...");
        mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);

        // # adding edited title and description to save
        if (mEditTitle != null)
            mNote.setTitle(mEditTitle.getText().toString().trim());
        if (mEditDescription != null)
            mNote.setDescription(mEditDescription.getText().toString().trim());

        mPresenterNote.editNote(mNote, addNoteData, deleteNoteData, editLinkList);   // # saving note
    }


    private void setAllAdapters()
    {
        //# setting media adapter
        Log.i(TAG,"setting media adapter");
        mAdapterMedia = new AdapterNoteMediaEdit(mContext, getSupportFragmentManager());
        mRecyclerMedia.stopScroll();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        layoutManager.canScrollHorizontally();
        mRecyclerMedia.setLayoutManager(layoutManager);
        mRecyclerMedia.setAdapter(mAdapterMedia);






        //# setting document adapter*********************************************************************************************
        Log.i(TAG,"setting document adapter");
        mAdapterDocument = new AdapterNoteDocumentEdit(mContext);
        LinearLayoutManager layoutManagerDocument = new LinearLayoutManager(mContext);
        mRecyclerDocument.stopScroll();
        mRecyclerDocument.setLayoutManager(layoutManagerDocument);
        mRecyclerDocument.setAdapter(mAdapterDocument);





        //# setting link adapter*********************************************************************************************************
        Log.i(TAG,"setting document adapter");
        mAdapterLink = new AdapterNoteLinkEdit(mContext);
        LinearLayoutManager layoutManagerLink = new LinearLayoutManager(mContext);
        mRecyclerLink.setLayoutManager(layoutManagerLink);
        mRecyclerLink.setAdapter(mAdapterLink);
    }



    // this method selects images from device
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void selectImageFromDevice()
    {
        String storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (mPermissionHelper.isPermissionGranted(storagePermission))
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, ConstHelper.RequestCode.DEVICE_IMAGE );
        }
        else
            mPermissionHelper.requestPermission(storagePermission,"Storage permission is required to upload files...");
    }


    // # this method selects documents form device
    private void selectDocumentFromDevice()
    {
        String storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (mPermissionHelper.isPermissionGranted(storagePermission))
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/*");
            // Intent createChooser = Intent.createChooser(intent,"Select File To Upload");
            startActivityForResult(intent,ConstHelper.RequestCode.DOCUMENT);
        }
        else
            mPermissionHelper.requestPermission(storagePermission,"Storage permission is required to upload files...");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() called fetching data");

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == ConstHelper.RequestCode.DEVICE_IMAGE)
                handleSelectedImagesFromDevice(data);

            if (requestCode == ConstHelper.RequestCode.DOCUMENT)
                handleSelectedDocumentFromDevice(data);

            if (requestCode == ConstHelper.RequestCode.SELECT && data.getExtras() != null) {
                Notes noteCategory = (Notes) data.getExtras().getSerializable(ConstHelper.Key.SELECT);
                if (noteCategory != null)
                {
                    mTxtCategory.setText(noteCategory.getCategoryName());
                    if (mNote != null)
                    {
                        mNote.setCategoryId(noteCategory.getCategoryId());
                        mNote.setCategoryName(noteCategory.getCategoryName());
                    }

                }
            }
        }

    }


    // this method fetches images from intent received in onActivityResult
    private void handleSelectedImagesFromDevice(Intent data)
    {
        if (data.getData() != null)
        {
            Uri uri = data.getData();
            int fileSize = (int) FileHelper.getFileSizeInMb(mContext, uri);
            Log.i(TAG,"selected image size = "+fileSize+"mb");

            String imagePath = FileHelper.getFilePath(mContext, uri);
            Log.i(TAG,"image path = "+imagePath);

            if (mAdapterMedia != null)
            {
                Notes.Media media = new Notes.Media();
                media.setMediaUrl(imagePath);
                media.setMediaFromServer(false);
                mAdapterMedia.addMedia(media);
                mRecyclerMedia.smoothScrollToPosition(0);
            }
            else
                Log.e(TAG,"mAdapterMedia is null in onActivityResult");

        }
        else // if more than 1 files have been selected
        {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item clipItem = clipData.getItemAt(i);
                    Uri uriMedia = clipItem.getUri();

                    int fileSize = (int) FileHelper.getFileSizeInMb(mContext, uriMedia);
                    Log.i(TAG, "clip data selected image size = " + fileSize + "mb");

                    String imagePath = FileHelper.getFilePath(mContext, uriMedia);
                    if (mAdapterMedia != null) {
                        Notes.Media media = new Notes.Media();
                        media.setMediaUrl(imagePath);
                        mAdapterMedia.addMedia(media);
                        mRecyclerMedia.smoothScrollToPosition(0);
                    } else
                        Log.e(TAG, "mAdapterMedia is null in onActivityResult");

                }
            } else
                Log.e(TAG, "clipData is null in handleSelectedVideos() method");

        }
    }


    // this method fetches document from intent received in onActivityResult
    private void handleSelectedDocumentFromDevice(Intent data)
    {

        if (data == null || data.getData() == null)
        {
            Log.e(TAG,"data is null in handleSelectedFilesFromDevice()");
            return;
        }

        Uri uri = data.getData();
        if (uri == null)
        {
            Log.e(TAG,"uri is null in handleSelectedDocumentFromDevice() method");
            return;
        }

        String fileType = FileHelper.getFileExtension(mContext, uri);
        String fileName = FileHelper.getFileName(mContext, uri);
        String filePath = FileHelper.getFilePath(mContext, uri);
        double fileSize =  FileHelper.getFileSizeInKB(mContext, uri);

        if (".pdf".contains(fileType) || ".doc".contains(fileType) || ".docx".contains(fileType) || ".txt".contains(fileType))
        {
            //checkFileSize(data.getData());
            if (fileSize > (1024*5)) // # checking file size
            {
                DialogHelper.showSimpleCustomDialog(mContext, "File is large...","Please select document less than 5 mb", "OK", null, new DialogHelper.ShowSimpleDialogListener() {
                    @Override
                    public void onOkClicked() {
                        selectDocumentFromDevice();
                    }
                });
            }
            else // if file size is greater than required
            {
                String message = "File Name : " + fileName + " \nFile Size : " + fileSize+"KB"/* + "\nFile Type : " + fileType*/;
                Log.i(TAG," document details = "+message);
                // showSelectedFileAndUpload(message,fileUri);  // this method will show file details and upload

                if (mAdapterDocument != null)
                {
                    Notes.Document document = new Notes.Document();
                    document.setDocumentTitle(fileName);
                    document.setDocumentUrl(filePath);
                    document.setDocumentFromServer(false);
                    mAdapterDocument.addDocument(document);
                }
            }

        }
        else
        {
            DialogHelper.showSimpleCustomDialog(mContext, "File not supported...", " Supported files: .pdf, .doc, .docx,  .txt", "Select", null, new DialogHelper.ShowSimpleDialogListener() {
                @Override
                public void onOkClicked() {
                    selectDocumentFromDevice();
                }
            });
        }

    }





    // setting data on views
    private void setViews()
    {
        //requestNoteData();  // getting note data from server

        showCreatedDataDetailsFragment();  // showing creator details and created or updated details

        // setting views from data came from calling component
        if (mNote != null)
        {
            if (mNote.getTitle() != null && mEditTitle != null)
                mEditTitle.setText(mNote.getTitle());
            if (mNote.getDescription() != null && mEditDescription != null)
                mEditDescription.setText(mNote.getDescription());
            if (mNote.getCategoryName() != null && mNote.getCategoryName() != null && mTxtCategory != null)
                mTxtCategory.setText(mNote.getCategoryName());



          /*  // # updating images adapter
            if (mNote.getNoteData().getListMedia() != null)
                if (mAdapterMedia != null)
                    mAdapterMedia.addMediaList(mNote.getNoteData().getListMedia());


            // # updating document adapter
            if (mNote.getNoteData().getListDocument() != null)
                if (mAdapterDocument != null)
                    mAdapterDocument.addDocumentList(mNote.getNoteData().getListDocument());
       

            // # updating link adapter
            if (mNote.getNoteData().getListLinks()!= null)
                if (mAdapterLink != null)
                    mAdapterLink.addLinkList(mNote.getNoteData().getListLinks());*/
            // # updating images adapter
            if (mNote.getListMedia() != null) {
                if (mAdapterMedia != null)
                    mAdapterMedia.addMediaList(mNote.getListMedia());
            } else {
                Log.e(TAG, "notes.getListMedia() is nul in onReceivedNotedData() method");
            }

            // # updating document adapter
            if (mNote.getListDocument() != null) {
                if (mAdapterDocument != null)
                    mAdapterDocument.addDocumentList(mNote.getListDocument());
            } else {
                Log.e(TAG, "notes.getListDocument() is nul in onReceivedNotedData() method");
            }

            // # updating link adapter
            if (mNote.getListLinks() != null) {
                if (mAdapterLink != null)
                    mAdapterLink.addLinkList(mNote.getListLinks());
            } else {
                Log.e(TAG, "notes.getListLinks() is nul in onReceivedNotedData() method");
            }
        } else
            Log.e(TAG, "notes is null in onReceivedNoteData() method");


    }
    


    // # hiding loader
    private void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }




    @Override
    public void onCategoryCreated(Notes note) {
        hideLoader();
    }

    @Override
    public void onCategoryDeleted(Notes note) {
        hideLoader();
    }

    @Override
    public void onReceivedAllCategory(Notes categoryList) {
        hideLoader();
    }

    @Override
    public void onNoteCreated(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteDeleted(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteShared(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteCopy(String notes_id, String title) {
        hideLoader();
    }


    @Override
    public void onReceivedAllNotesOfCategory(Notes note) {
        hideLoader();
    }


    // setting note data
    @Override
    public void onReceivedNoteData(Notes note) {
        hideLoader();

        if (note != null)
        {
            if (mIsNoteEditedSuccessfully) // if data has been loaded after note has been updated successfully.
            {
                Intent intent = new Intent();
                intent.putExtra(ConstHelper.Key.IS_EDITED, true);
                intent.putExtra(ConstHelper.Key.DATA_MODAL, note);
                setResult(RESULT_OK, intent);
                finish();
            }
            else
            {
                mNote = note;   // # assigning new Note object to global note for this page
                setViews();
            }
        }
        else
            Log.e(TAG,"note is null in onReceivedNoteData() method");
    }

    @Override
    public void onReceivedAllNotes(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteEdited(Notes note)
    {
        Log.i(TAG,"onNoteEdited() method called");
        hideLoader();
        mIsNoteEditedSuccessfully = true;
    }

    @Override
    public void onFailed(String message) {
        hideLoader();
        if (message != null && !message.isEmpty())
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title), message);
        else
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title));
    }
}
