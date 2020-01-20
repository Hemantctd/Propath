package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNoteDocument;
import org.ctdworld.propath.adapter.AdapterNoteLink;
import org.ctdworld.propath.adapter.AdapterNoteMedia;
import org.ctdworld.propath.base.BaseActivity;
import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.FragCreatingDetails;
import org.ctdworld.propath.fragment.FragmentBottomSheetOptions;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.ContactAndGroup;
import org.ctdworld.propath.model.CreatedDataDetails;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNotes;

import java.util.ArrayList;
import java.util.List;

public class ActivityNoteView extends BaseActivity implements ContractNotes.View
{
    private static final String TAG = ActivityNoteView.class.getSimpleName();

    // # Views
    private Toolbar mToolbar;
    private TextView mToolbarTxtTitle;
    private ImageView mImgToolbarOptionsMenu;
    private TextView mTxtTitle, mTxtCategory, mTxtDescription;
    private View mLayoutImage, mLayoutDocument, mLayoutLink;
    private RecyclerView mRecyclerMedia;
    private RecyclerView mRecyclerDocument;   //document list
    private RecyclerView mRecyclerLink;   // link list


    // # Other variables
    private Context mContext;
    private ContractNotes.Presenter mPresenterNote;
    private AdapterNoteMedia mAdapterMedia;
    private AdapterNoteDocument mAdapterDocument;
    private AdapterNoteLink mAdapterLink;

    // # key to send response to calling component to know if note was deleted, edited. calling component will refresh list if changed something
   // public static final String KEY_NOTES_DATA = "notes data";
    private Notes mNote = null;   // note data


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        init();
        setToolbar();
        setUpAdapters();  //# setting media, document and link adapters
        setViews();
        showCreatedDataDetailsFragment();  // showing creator details and created or updated details
        requestNoteData();  // getting note data from server

        // # setting listeners
        setListeners();

    }



    // to initialize
    private void init()
    {
        mContext = this;
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
        mImgToolbarOptionsMenu = findViewById(R.id.toolbar_img_options_menu);


        mTxtTitle = findViewById(R.id.activity_note_txt_title);
        mTxtCategory = findViewById(R.id.activity_note_txt_category);
        mTxtDescription = findViewById(R.id.activity_note_txt_description);
        mLayoutImage = findViewById(R.id.activity_note_layout_images);
        mLayoutDocument = findViewById(R.id.activity_note_layout_document);
        mLayoutLink = findViewById(R.id.activity_note_layout_link);
        mRecyclerMedia = findViewById(R.id.activity_note_recycler_images);
        mRecyclerDocument = findViewById(R.id.activity_note_recycler_document);
        mRecyclerLink = findViewById(R.id.activity_note_recycler_link);

        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                mNote = (Notes) bundle.getSerializable(ConstHelper.Key.DATA_MODAL);
                Log.i(TAG,"mnote"+mNote);
            }
        }

    }




    // setting data on views
    private void setViews()
    {
        // setting views from data came from calling component
        if (mNote != null)
        {
            if (mNote.getTitle() != null)
                mTxtTitle.setText(mNote.getTitle());   // title
            if (mNote.getCategoryName() != null && mNote.getCategoryName() != null)
                mTxtCategory.setText(mNote.getCategoryName());  // note name
            if (mNote.getDescription() != null)
                mTxtDescription.setText(mNote.getDescription());  // note description
        }
    }



    private void setViewsFromServer()
    {

        // # setting Media, Document and Link
        if (mLayoutImage != null)
            mLayoutImage.setVisibility(View.VISIBLE);
        if (mLayoutDocument != null)
            mLayoutDocument.setVisibility(View.VISIBLE);
        if (mLayoutLink != null)
            mLayoutLink.setVisibility(View.VISIBLE);


        // # updating images adapter
        if (mNote.getListMedia() != null)
        {
            if (mAdapterMedia != null)
                mAdapterMedia.addMediaList(mNote.getListMedia());
        }
        else
        {
            Log.e(TAG,"mNote.getNoteData().getListMedia() is nul in onReceivedNotedData() method");
            mLayoutImage.setVisibility(View.GONE);
        }


        // # updating document adapter
        if (mNote.getListDocument() != null)
        {
            if (mAdapterDocument != null)
                mAdapterDocument.addDocumentList(mNote.getListDocument());
        }
        else
        {
            Log.e(TAG,"mNote.getNoteData().getListDocument() is nul in onReceivedNotedData() method");
            mLayoutDocument.setVisibility(View.GONE);
        }


        // # updating link adapter
        if (mNote.getListLinks()!= null)
        {
            if (mAdapterLink != null)
                mAdapterLink.addLinkList(mNote.getListLinks());
        }
        else
        {
            Log.e(TAG,"mNote.getNoteData().getListLinks() is nul in onReceivedNotedData() method");
            mLayoutLink.setVisibility(View.GONE);
        }

    }


    // showing creator details and created or updated details
    private void showCreatedDataDetailsFragment() {
        FragCreatingDetails fragCreatingDetails =  FragCreatingDetails.newInstance(getCreatedDataDetails());
        String tag = ConstHelper.Tag.Fragment.CREATED_DATA_DETAILS;
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_creator_details, fragCreatingDetails, tag).commit();
    }



    // setting data to its object
    public CreatedDataDetails getCreatedDataDetails() {
        if (mNote == null)
            return null;
        /*User user = SessionHelper.getInstance(mContext).getUser();
        if (user == null)
            return null;*/

        CreatedDataDetails createdDataDetails = new CreatedDataDetails();
        //createdDataDetails.setName(user.getName());
        //createdDataDetails.setRoleId(user.getRoleId());
        //createdDataDetails.setUserPicUrl(user.getUserPicUrl())
        Log.d(TAG,"NOTEDATA "+mNote.getCreatedByUserPic());
        createdDataDetails.setName(mNote.getCreatedByUserName());
        createdDataDetails.setRoleId(RoleHelper.getInstance(mContext).getRoleIdByName(mNote.getCreatorRoleName()));
        createdDataDetails.setUserPicUrl(mNote.getCreatedByUserPic());
        createdDataDetails.setCreatedDate(DateTimeHelper.getDateTime(mNote.getUpdatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
        createdDataDetails.setUpdatedDate(DateTimeHelper.getDateTime(mNote.getCreatedDateTime(), DateTimeHelper.FORMAT_DATE_TIME));

        return createdDataDetails;
    }



    // # setting listeners
    private void setListeners()
    {
      mImgToolbarOptionsMenu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view)
          {
              if (mNote == null)
                  return;

              BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                      .addOption(BottomSheetOption.OPTION_DELETE, "Delete Note");
              if (mNote.getCreatedByUserId().equals(SessionHelper.getUserId(mContext)))
              {
                  builder.addOption(BottomSheetOption.OPTION_SHARE,"Share Note");
                  builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit Note");
                  builder.addOption(BottomSheetOption.OPTION_COPY, "Copy Note");
              }

              ArrayList<BottomSheetOption> bottomSheetOptions = builder.build();

              Log.i(TAG,"bottom sheet options size = "+bottomSheetOptions.size());


              // List<BottomSheetOption> bottomSheetOptions = builder.build();
              FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
              options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener()
              {
                  @Override
                  public void onOptionSelected(int option)
                  {
                      Log.i(TAG,"bottom sheet options selected, option = "+option);
                      switch (option)
                      {
                          case BottomSheetOption.OPTION_SHARE:
                              DialogHelper.showCustomDialog(mContext, "Are you sure ", "want to share this note ?","Ok", "Cancel", new DialogHelper.ShowDialogListener() {
                                  @Override
                                  public void onOkClicked() {
                                      startActivityForResult(new Intent(mContext, ActivityContactsAndGroups.class) , ConstHelper.RequestCode.SHARE);
                                  }

                                  @Override
                                  public void onCancelClicked() {

                                  }
                              });
                              break;

                          case BottomSheetOption.OPTION_EDIT:
                              Intent intent = new Intent(mContext, ActivityNoteEdit.class);
                              intent.putExtra(ConstHelper.Key.DATA_MODAL, mNote);
                              startActivityForResult(intent, ConstHelper.RequestCode.EDIT);

                              break;

                          case BottomSheetOption.OPTION_COPY:
                              DialogEditText dialogEditText = DialogEditText.getInstance("Copy Notes",
                                      "Enter Title", "Save", false);
                              dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                                  @Override
                                  public void onButtonClicked(String enteredValue) {
//                                mProgressBar.setVisibility(View.VISIBLE);
                                      //showLoader(getString(R.string.copying));

                                      if (enteredValue.equals("")) {
                                          DialogHelper.showSimpleCustomDialog(mContext, "Plz fill the title");
                                      } else {
                                          Log.d(TAG, "note id" + mNote.getNoteId());

                                          mPresenterNote.copyNote(mNote.getNoteId(), enteredValue);
                                      }

                                  }
                              });
                              dialogEditText.show(getSupportFragmentManager(),
                                      ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);

                              break;

                          case BottomSheetOption.OPTION_DELETE:
                              String title = getString(R.string.are_you_sure);
                              String message = getString(R.string.confirm_msg_delete_note);
                              DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
                                  @Override
                                  public void onOkClicked() {
                                      showLoader(getString(R.string.deleting));
                                      mPresenterNote.deleteNote(mNote);
                                  }

                                  @Override
                                  public void onCancelClicked() {

                                  }
                              });

                              break;
                      }
                  }
              });
              options.show(getSupportFragmentManager(), "options");

          }
      });

    }


    // to get note data from server
    private void requestNoteData()
    {
        Log.i(TAG,"requesting note data");
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            showLoader("Loading...");

            try
            {
                mNote.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
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


    // setting toolbar data
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        mToolbarTxtTitle.setText(getString(R.string.files_notes));
        mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);

        //    mImgToolbarOptionsMenu.setVisibility(View.VISIBLE);
        /*if (mNote != null)
        {
            // # checking if note has been created by logged in user, show edit icon if create by self otherwise not
            if (mNote.isSelfCreated())
            {
                //# edit icon
                mToolbarImgEdit.setVisibility(View.VISIBLE);
                mToolbarImgEdit.setImageResource(R.drawable.ic_edit);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarImgEdit.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, mToolbarImgDelete.getId());
                params.rightMargin = (int) getResources().getDimension(R.dimen.toolbarMarginIconText);
                mToolbarImgEdit.setLayoutParams(params);

                //# share icon
                mToolbarImgShare.setVisibility(View.VISIBLE);
                mToolbarImgShare.setImageResource(R.drawable.ic_share);
                RelativeLayout.LayoutParams paramsShare = (RelativeLayout.LayoutParams) mToolbarImgShare.getLayoutParams();
                paramsShare.addRule(RelativeLayout.LEFT_OF, mToolbarImgEdit.getId());
                paramsShare.rightMargin = (int) getResources().getDimension(R.dimen.toolbarMarginIconText);
                mToolbarImgShare.setLayoutParams(paramsShare);
            }

            //# delete icon icon
            RelativeLayout.LayoutParams paramsDelete = (RelativeLayout.LayoutParams) mToolbarImgDelete.getLayoutParams();
            paramsDelete.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            mToolbarImgDelete.setVisibility(View.VISIBLE);
            mToolbarImgDelete.setImageResource(R.drawable.ic_delete);
            mToolbarImgDelete.setLayoutParams(paramsDelete);
        }*/

        if (getSupportActionBar() == null)
            return;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);
    }


    //# setting media, document and link adapter to show list of media
    private void setUpAdapters()
    {
        // # setting media adapter
        mAdapterMedia = new AdapterNoteMedia(mContext, getSupportFragmentManager());
        LinearLayoutManager mediaLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mediaLayoutManager.canScrollHorizontally();
        mRecyclerMedia.setLayoutManager(mediaLayoutManager);
        mRecyclerMedia.setAdapter(mAdapterMedia);





        // # setting document adapter
        Log.i(TAG,"setting document adapter");
        mAdapterDocument = new AdapterNoteDocument(mContext);
        LinearLayoutManager documentLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerDocument.stopScroll();
        mRecyclerDocument.setLayoutManager(documentLayoutManager);
        mRecyclerDocument.setAdapter(mAdapterDocument);





        // # setting link adapter
        Log.i(TAG,"setting document adapter");
        mAdapterLink = new AdapterNoteLink(mContext);
        mRecyclerLink.setNestedScrollingEnabled(false);
        LinearLayoutManager linkLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerLink.setLayoutManager(linkLayoutManager);
        mRecyclerLink.setAdapter(mAdapterLink);
    }

    


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    // setting Note to result to send back
    @Override
    public void onBackPressed() {
        Log.i(TAG,"setting result to send to calling component");
        Intent intent = new Intent();
        intent.putExtra(ConstHelper.Key.DATA_MODAL, mNote);
        mNote.setNoteDeleted(true);
        setResult(RESULT_OK,intent);
        finish();
    }

    // checking if  note is edited, note is refreshed here if note is edited
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult() method called");

        if (resultCode == RESULT_OK)
        {
            if (requestCode == ConstHelper.RequestCode.EDIT && data.getExtras() != null)
            {
                boolean isNoteEdited = data.getExtras().getBoolean(ConstHelper.Key.IS_EDITED);
                Notes note = (Notes) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (isNoteEdited && note != null)
                {
                    Log.i(TAG,"refreshing note data after editing note");
                    mNote = note;
                    mNote.setNoteEdited(true);
                    setViews();
                    setViewsFromServer();
                }
            }

            // checking if data is to be shared, first we select friends or groups in other activity the come back to this activity
            if (requestCode == ConstHelper.RequestCode.SHARE && data.getExtras() != null)
            {
                ContactAndGroup contactAndGroup = (ContactAndGroup) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                shareNote(contactAndGroup);  // sharing note
            }
        }
    }

   /* // to hide loader
    private void hideLoader()
    {
        if (mLoader != null && mLoader.isAdded())
            mLoader.dismiss();
    }*/



   // # sharing note with individual friends or groups
   private void shareNote(ContactAndGroup contactAndGroup)
   {
       String[] userArr = new String[]{};    // array of users whom note will be shared
       String[] groupArr = new String[]{};   // group of users whom note will be shared
       if (contactAndGroup != null)
       {
           if (ContactAndGroup.TYPE_FRIEND.equalsIgnoreCase(contactAndGroup.getType()))
               userArr = new String[]{contactAndGroup.getId()};   // creating user array with single friend
           else if (ContactAndGroup.TYPE_GROUP.equalsIgnoreCase(contactAndGroup.getType()))
               groupArr = new String[]{contactAndGroup.getId()};  // creating group array with single group id

           // sharing
           mPresenterNote.shareNote(mNote, groupArr, userArr);
           showLoader("Sharing...");
       } else
           Log.e(TAG, "contactAndGroup is null");
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
    public void onNoteDeleted(Notes note)
    {
        Log.i(TAG,"onNoteDeleted() method called");
        hideLoader();

        if (mNote != null)
        {
            mNote.setNoteDeleted(true);
            onBackPressed();
        }

    }

    @Override
    public void onNoteShared(Notes note) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext,"Success","Note shared successfully...");
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
    public void onReceivedNoteData(Notes note)
    {
        Log.i(TAG,"note data received, updating all adapters");
        hideLoader();
        if (note != null)
        {

            mNote = note;   // # assigning new Note object to global note for this page
            setViewsFromServer();
          /*  if (note.getTitle() != null && mTxtTitle != null)
                mTxtTitle.setText(mNote.getTitle());
            if (note.getDescription() != null && mTxtDescription != null)
                mTxtDescription.setText(note.getDescription());
            if (note.getCategory() != null && note.getCategory().getCategoryName() != null && mTxtCategory != null)
                mTxtCategory.setText(note.getCategory().getCategoryName());
            */
        }
        else
            Log.e(TAG,"note is null in onReceivedNoteData() method");

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
    }
}
