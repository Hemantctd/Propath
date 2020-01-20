package org.ctdworld.propath.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
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
import org.ctdworld.propath.adapter.AdapterNoteDocumentCreate;
import org.ctdworld.propath.adapter.AdapterNoteLinkCreate;
import org.ctdworld.propath.adapter.AdapterNoteMediaCreate;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNotes;
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


public class ActivityNoteCreate extends BaseActivity implements ContractNotes.View {
    private static final String TAG = ActivityNoteCreate.class.getSimpleName();

    Context mContext;


    // # permission helper object to check and request permission
    PermissionHelper mPermissionHelper;

    ContractNotes.Presenter mPresenterNote;

    // # toolbar views
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;
    ImageView mToolbarImgSave;


    //views
    // TextView mTxtChangeCategory;
    TextView mTxtCategory;
    EditText mEditTitle, mEditDescription;
    ImageView mImgMicTitle, mImgMicDescription;


    // # views and variables for media list
    RecyclerView mRecyclerMedia;
    AdapterNoteMediaCreate mAdapterMedia;
    ImageView mImgAddMedia;  // to add media(image)


    // # views and variables for document list
    RecyclerView mRecyclerDocument;
    AdapterNoteDocumentCreate mAdapterDocument;
    ImageView mImgAddDocument;

    // # views and variables for link list
    RecyclerView mRecyclerLink;
    AdapterNoteLinkCreate mAdapterLink;
    ImageView mImgAddLink;
    EditText mEditAddLink;


    public static final String KEY_NOTES_DATA = "notes data";
    private Notes mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        init();
        setToolbar();
        setAllAdapters();
        setListener();
        showCreatedDataDetailsFragment();  // showing creator details and created or updated details

        if (mNote != null && mNote.getCategoryName() != null) {
            mTxtCategory.setText(mNote.getCategoryName());
        }
    }


    // to initialize
    private void init() {
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

        mImgMicTitle = findViewById(R.id.activity_note_create_img_mic_title);
        mImgMicDescription = findViewById(R.id.activity_note_create_img_mic_description);
        mTxtCategory = findViewById(R.id.activity_note_create_txt_category);
        // mTxtChangeCategory = findViewById(R.id.activity_note_create_txt_change_category);
        mEditTitle = findViewById(R.id.activity_note_create_edit_title);
        mEditDescription = findViewById(R.id.activity_note_create_edit_description);
        mRecyclerMedia = findViewById(R.id.activity_note_create_recycler_images);
        mRecyclerDocument = findViewById(R.id.activity_note_create_recycler_document);
        mImgAddDocument = findViewById(R.id.activity_note_create_img_add_docuemnt);
        mRecyclerLink = findViewById(R.id.activity_note_create_recycler_link);
        mImgAddLink = findViewById(R.id.activity_note_create_img_add_add_link);
        mEditAddLink = findViewById(R.id.activity_note_create_edit_link);

        mImgAddMedia = findViewById(R.id.activity_note_create_img_add_media);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                mNote = (Notes) bundle.getSerializable(KEY_NOTES_DATA);
        }

    }


    private void setListener() {

        mImgAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            public void onClick(View view) {
                String link = mEditAddLink.getText().toString().trim();
                if (!link.isEmpty()) {
                    if (Patterns.WEB_URL.matcher(link).matches()) {
                        mEditAddLink.setText("");
                        Log.i(TAG, "adding link to list");
                        Notes.Link addLink = new Notes.Link();
                        addLink.setLinkUrl(link);
                        mAdapterLink.addLink(addLink);
                    } else
                        Snackbar.make(mImgAddMedia, "Enter valid web link", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(mImgAddLink, "Please enter link", Snackbar.LENGTH_SHORT).show();

            }
        });


        mToolbarImgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedToInternet(mContext))
                    createNote();
                else
                    DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));
            }
        });


        mTxtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(mContext, ActivityNoteSelectCategory.class), ConstHelper.RequestCode.SELECT);
            }
        });


        mImgMicTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty()) {
                                mEditTitle.setText(mEditTitle.getText().toString() + " " + spokenText);
                                mEditTitle.setSelection(mEditTitle.getText().toString().length());
                                mEditTitle.requestFocus();
                            }
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
            public void onClick(View view) {
                PermissionHelper permissionHelper = new PermissionHelper(mContext);
                if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
                else {
                    FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                    fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onReceiveText(String spokenText) {
                            if (spokenText != null && !spokenText.isEmpty()) {
                                mEditDescription.setText(mEditDescription.getText().toString() + " " + spokenText);
                                mEditDescription.setSelection(mEditDescription.getText().toString().length());
                                mEditDescription.requestFocus();
                            }
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
        FragCreatingDetails fragCreatingDetails = FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }


    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails() {

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

        mToolbarTxtTitle.setText(getString(R.string.create_note));


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgSave.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mToolbarImgSave.setVisibility(View.VISIBLE);
        mToolbarImgSave.setLayoutParams(params);
        mToolbarImgSave.setImageResource(R.drawable.ic_notes_save);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    // creating new note
    private void createNote() {
        Log.i(TAG, "creating Note");
        if (UtilHelper.isConnectedToInternet(mContext)) {
            showLoaderOrChangeTitle(getString(R.string.message_saving));
            Notes uploadNote = new Notes();

            List<Notes.Media> listMedia = mAdapterMedia.getMediaList();
            List<Notes.Document> listDocument = mAdapterDocument.getDocumentList();
            List<Notes.Link> listLink = mAdapterLink.getLinkList();

            if (mNote != null && mNote.getCategoryId() != null && !mNote.getCategoryId().isEmpty())   // setting category id
                uploadNote.setCategoryId(mNote.getCategoryId());
            else
                uploadNote.setCategoryId(Notes.DEFAULT_CATEGORY_UNCATEGORISED);  // setting default category id

            uploadNote.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadNote.setTitle(mEditTitle.getText().toString().trim());
            uploadNote.setDescription(mEditDescription.getText().toString().trim());
            if (listMedia != null)
                uploadNote.setListMedia(listMedia);
            if (listDocument != null)
                uploadNote.setListDocument(listDocument);
            if (listLink != null)
                uploadNote.setListLinks(listLink);

            mPresenterNote.createNote(uploadNote);

        } /*else {
            hideLoader();
           // DialogHelper.showSimpleCustomDialog(mContext, strNoConnectTitle, strNoConnectionMessage);
        }
        */

       /* if (UtilHelper.isConnectedToInternet(mContext)) {
            showLoader(getString(R.string.message_saving));

            Notes uploadNote = new Notes();

            List<Note.Media> listMedia = mAdapterMedia.getMediaList();
            List<Note.Document> listDocument = mAdapterDocument.getDocumentList();
            List<Note.Link> listLink = mAdapterLink.getLinkList();

            if (mNote != null && mNote.getCategoryId() != null && !mNote.getCategoryId().isEmpty())   // setting category id
                uploadNote.setCategoryId(mNote.getCategoryId());
            else
                uploadNote.setCategoryId(Note.DEFAULT_CATEGORY_UNCATEGORIZED);  // setting default category id

            uploadNote.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
            uploadNote.setTitle(mEditTitle.getText().toString().trim());
            uploadNote.setDescription(mEditDescription.getText().toString().trim());
            if (listMedia != null)
                uploadNote.getNoteData().setListMedia(listMedia);
            if (listDocument != null)
                uploadNote.getNoteData().setListDocument(listDocument);
            if (listLink != null)
                uploadNote.getNoteData().setListLinks(listLink);

            mPresenterNote.createNote(uploadNote);*/

        else {
            hideLoader();
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.title_no_connection), getString(R.string.message_no_connection));
        }

    }


    // setting adapter for images, document and links
    private void setAllAdapters() {
        //# setting media adapter
        Log.i(TAG, "setting media adapter");
        mAdapterMedia = new AdapterNoteMediaCreate(mContext, getSupportFragmentManager());

        mRecyclerMedia.stopScroll();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        layoutManager.canScrollHorizontally();
        mRecyclerMedia.setLayoutManager(layoutManager);
        mRecyclerMedia.setAdapter(mAdapterMedia);


        //# setting document adapter*********************************************************************************************
        Log.i(TAG, "setting document adapter");
        mAdapterDocument = new AdapterNoteDocumentCreate(mContext);
        LinearLayoutManager layoutManagerDocument = new LinearLayoutManager(mContext);
        mRecyclerDocument.stopScroll();
        mRecyclerDocument.setLayoutManager(layoutManagerDocument);
        mRecyclerDocument.setAdapter(mAdapterDocument);


        //# setting link adapter*********************************************************************************************************
        Log.i(TAG, "setting link adapter");
        mAdapterLink = new AdapterNoteLinkCreate(mContext);
        LinearLayoutManager layoutManagerLink = new LinearLayoutManager(mContext);
        mRecyclerLink.setLayoutManager(layoutManagerLink);
        mRecyclerLink.setAdapter(mAdapterLink);
    }


    // this method selects images from device
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void selectImageFromDevice() {
        String storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (mPermissionHelper.isPermissionGranted(storagePermission)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, ConstHelper.RequestCode.DEVICE_IMAGE);
        } else
            mPermissionHelper.requestPermission(storagePermission, "Storage permission is required to upload files...");
    }


    // # this method selects documents form device
    private void selectDocumentFromDevice() {
        String storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (mPermissionHelper.isPermissionGranted(storagePermission)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/*");
            // Intent createChooser = Intent.createChooser(intent,"Select File To Upload");
            startActivityForResult(intent, ConstHelper.RequestCode.DOCUMENT);
        } else
            mPermissionHelper.requestPermission(storagePermission, "Storage permission is required to upload files...");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult() called fetching data");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ConstHelper.RequestCode.DEVICE_IMAGE)
                handleSelectedImagesFromDevice(data);

            if (requestCode == ConstHelper.RequestCode.DOCUMENT)
                handleSelectedDocumentFromDevice(data);

            // checking if selected categoryid
            if (requestCode == ConstHelper.RequestCode.SELECT && data.getExtras() != null) {
                Notes noteCategory = (Notes) data.getExtras().getSerializable(ConstHelper.Key.SELECT);
                if (noteCategory != null) {
                    mTxtCategory.setText(noteCategory.getCategoryName());
                    if (mNote != null) {
                        mNote.setCategoryId(noteCategory.getCategoryId());
                        mNote.setCategoryName(noteCategory.getCategoryName());
                    }

                }
            }
        }

    }


    // this method fetches images from intent received in onActivityResult
    private void handleSelectedImagesFromDevice(Intent data) {
        if (data.getData() != null) {
            Uri uri = data.getData();
            int fileSize = (int) FileHelper.getFileSizeInMb(mContext, uri);
            Log.i(TAG, "selected image size = " + fileSize + "mb");


            String imagePath = FileHelper.getFilePath(mContext, uri);
            Log.i(TAG, "image path = " + imagePath);
            if (imagePath == null)
                return;

            if (mAdapterMedia != null) {
                Notes.Media media = new Notes.Media();
                media.setMediaUrl(imagePath);
                mAdapterMedia.addMedia(media);
                mRecyclerMedia.smoothScrollToPosition(0);
            } else
                Log.e(TAG, "mAdapterMedia is null in onActivityResult");

        } else // if more than 1 files have been selected
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
    private void handleSelectedDocumentFromDevice(Intent data) {

        if (data == null || data.getData() == null) {
            Log.e(TAG, "data is null in handleSelectedFilesFromDevice()");
            return;
        }

        Uri uri = data.getData();
        if (uri == null) {
            Log.e(TAG, "uri is null in handleSelectedDocumentFromDevice() method");
            return;
        }

        String fileType = FileHelper.getFileExtension(mContext, uri);
        String fileName = FileHelper.getFileName(mContext, uri);
        String filePath = FileHelper.getFilePath(mContext, uri);
        double fileSize = FileHelper.getFileSizeInKB(mContext, uri);

        //   mStrSelectedFileName = fileName;
        //   mSelectedFileUri = data.getData();

        Log.i(TAG, "fileType = " + fileType);
        Log.i(TAG, "filePath = " + filePath);
        Log.i(TAG, "file name = " + fileName);
        Log.i(TAG, "file size = " + fileSize);

        if (".pdf".contains(fileType) || ".doc".contains(fileType) || ".docx".contains(fileType) || ".txt".contains(fileType)) {
            //checkFileSize(data.getData());
            if (fileSize > (1024 * 5)) // # checking file size
            {
                DialogHelper.showSimpleCustomDialog(mContext, "File is large...", "Please select document less than 5 mb", "OK", null, new DialogHelper.ShowSimpleDialogListener() {
                    @Override
                    public void onOkClicked() {
                        selectDocumentFromDevice();
                    }
                });
            } else // if file size is not greater than required
            {
                String message = "File Name : " + fileName + " \nFile Size : " + fileSize + "KB"/* + "\nFile Type : " + fileType*/;
                Log.i(TAG, " document details = " + message);
                // showSelectedFileAndUpload(message,fileUri);  // this method will show file details and upload

                if (mAdapterDocument != null) {
                    Notes.Document document = new Notes.Document();
                    document.setDocumentTitle(fileName);
                    document.setDocumentUrl(filePath);

                    mAdapterDocument.addDocument(document);
                }
            }

        } else {
            DialogHelper.showSimpleCustomDialog(mContext, "File not supported...", " Supported files: .pdf, .doc, .docx,  .txt", "Select", null, new DialogHelper.ShowSimpleDialogListener() {
                @Override
                public void onOkClicked() {
                    selectDocumentFromDevice();
                }
            });
        }

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
        //  DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.success_title),"Note created successfully...");

        Intent intent = new Intent();
        intent.putExtra(KEY_NOTES_DATA, note);
        setResult(RESULT_OK, intent);
        finish();
        // # checking if any component has requested to get category
        //  if (mAdapterMedia != null)
        //  mAdapterMedia.addMedia(note.getMedia());

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

    @Override
    public void onReceivedNoteData(Notes note) {
        hideLoader();
    }

    @Override
    public void onReceivedAllNotes(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteEdited(Notes note) {
        hideLoader();
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
