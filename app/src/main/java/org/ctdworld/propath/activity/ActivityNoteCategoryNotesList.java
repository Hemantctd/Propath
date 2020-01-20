package org.ctdworld.propath.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNoteAllOfCategory;
import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.fragment.DialogEditText;
import org.ctdworld.propath.fragment.DialogLoader;
import org.ctdworld.propath.fragment.FragmentNotesOptionsMenu;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.ContactAndGroup;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNotes;



// # this activity has been used to show all notes of a particular note
public class ActivityNoteCategoryNotesList extends AppCompatActivity implements FragmentNotesOptionsMenu.OnNotesOptionsListener, ContractNotes.View
{
    private static final String TAG = ActivityNoteCategoryNotesList.class.getSimpleName();
    private static final int REQUEST_CODE_SHARE_NOTE = 100;
    private static final int REQUEST_CODE_CREATE_NOTE = 200;
    private static final int REQUEST_CODE_EDIT_NOTE = 300;
    private static final int REQUEST_CODE_COPY_NOTE = 400;
    Context mContext;
    Toolbar mToolbar;
    TextView mToolbarTxtTitle;

    RecyclerView mRecyclerNotes;
    AdapterNoteAllOfCategory mAdapterNotes;
    FloatingActionButton mFAB;
    int mPositionSelectedNoteInAdapter = -1;  // this will contain position of note in adapter to delete, edit;
    //key to get Note object from calling component
    public static final String KEY_NOTES_DATA = "notes";
    private Notes mNote = null;


    ContractNotes.Presenter mPresenterNote;
    SwipeRefreshLayout mRefreshLayout;
    DialogLoader mLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_category_notes_list);

        init();
        setToolbar();
        setListeners();
        attachAdapterNotesAll();

        mRefreshLayout.setRefreshing(true);
        requestAllNotesOfCategory(); // to get all notes of a category from server

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAllNotesOfCategory();
            }
        });


    }


    // # initializing all variables
    private void init() {
        mToolbarTxtTitle = findViewById(R.id.toolbar_txt_title);
        mToolbar = findViewById(R.id.toolbar);

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

        mRecyclerNotes = findViewById(R.id.fragment_notes_all_notes_of_category_recycler);
        mFAB = findViewById(R.id.fragment_notes_all_notes_of_category_float_button);
        mRefreshLayout = findViewById(R.id.activity_note_category_notes_list_refresh_layout);


        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                mNote = (Notes) bundle.getSerializable(KEY_NOTES_DATA);
            }
        }
    }


    // to get all categories from server
    private void requestAllNotesOfCategory()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
           /* mTxtMessage.setVisibility(View.GONE);
            mRecyclerViewCategory.setVisibility(View.VISIBLE);*/
            try
            {
                mAdapterNotes.clearList();
                mNote.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                mPresenterNote.getAllNotesOfCategory(mNote);

            }
            catch (Exception e)
            {
                hideLoader();
                Log.e(TAG,"Error while requesting all notes of category , "+e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            //  DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.no_connection),getString(R.string.connection_message));
            hideLoader();
           /* mRecyclerViewCategory.setVisibility(View.GONE);
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtMessage.setText(getString(R.string.no_connection));*/
            DialogHelper.showSimpleCustomDialog(mContext,getString(R.string.title_no_connection), getString(R.string.message_no_connection));

        }
    }

    // setting listeners
    private void setListeners()
    {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityNoteCreate.class);
                intent.putExtra(ActivityNoteCreate.KEY_NOTES_DATA, mNote);
                startActivityForResult(intent, REQUEST_CODE_CREATE_NOTE);
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left);
        }
        else
            Log.e(TAG,"actionBar is null in setToolbar() method");


        if (mNote != null && mNote.getCategoryName() != null)
           mToolbarTxtTitle.setText(mNote.getCategoryName());
        else
            mToolbarTxtTitle.setText(getString(R.string.category));


    }


    // this method attaches adapter on recycler view to show all notes
    private void attachAdapterNotesAll()
    {
       // ArrayList<String> listNotes = new ArrayList<>();
        mAdapterNotes = new AdapterNoteAllOfCategory(mContext, getSupportFragmentManager(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerNotes.setLayoutManager(layoutManager);
        mRecyclerNotes.setAdapter(mAdapterNotes);
    }


    //# this method is called when user selects one option from options menu which is shown when user clicks on option menu icon in adapter
    @Override
    public void onNotesOptionSelected(int selectedItem, final Notes note, int selectedItemPosition)
    {
        mPositionSelectedNoteInAdapter = selectedItemPosition;
        switch (selectedItem)
        {
            case FragmentNotesOptionsMenu.OPTION_DELETE:

                // # below commented code is running

                Log.i(TAG,"delete clicked in note options menu = "+selectedItem);
                String title = "Are you sure...";
                String message = "want to delete this ?";
                DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked() {
                        if (note != null)
                        {
                            mLoader = DialogLoader.getInstance("Deleting...");
                            mLoader.show(getSupportFragmentManager(),ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
                            mPresenterNote.deleteNote(note);   // deleting note
                        }
                        else
                            Log.e(TAG,"note is null in onNotesOptionSelected");
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                break;

            case FragmentNotesOptionsMenu.OPTION_EDIT:
                Log.i(TAG,"edit clicked in note options menu = "+selectedItem);
                Intent intent = new Intent(mContext, ActivityNoteEdit.class);
                intent.putExtra(ConstHelper.Key.DATA_MODAL, note);
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
                break;

            case FragmentNotesOptionsMenu.OPTION_COPY:
                DialogEditText dialogEditText = DialogEditText.getInstance("Copy Note",
                        "Enter Title", "Save", false);
                dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener() {
                    @Override
                    public void onButtonClicked(String enteredValue) {
//                                mProgressBar.setVisibility(View.VISIBLE);
                        //showLoader(getString(R.string.copying));

                        if (enteredValue.equals("")) {
                            DialogHelper.showSimpleCustomDialog(mContext, "Plz fill the title");
                        } else {
                            Log.d(TAG, "note id" + note.getNoteId());

                            mPresenterNote.copyNote(note.getNoteId(), enteredValue);
                        }

                    }
                });
                dialogEditText.show(getSupportFragmentManager(),
                        ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                break;

            case FragmentNotesOptionsMenu.OPTION_SHARE:
                mNote = note;

                DialogHelper.showCustomDialog(mContext, "Are you sure ", "want to share this note ?","Ok", "Cancel", new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked() {
                        startActivityForResult(new Intent(mContext, ActivityContactsAndGroups.class) , REQUEST_CODE_SHARE_NOTE);
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {

            // checking if data is to be shared
            if (requestCode == REQUEST_CODE_SHARE_NOTE && data.getExtras() != null)
            {
                String[] userArr = new String[]{};
                String[] groupArr = new String[]{};
                ContactAndGroup contactAndGroup = (ContactAndGroup) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (contactAndGroup != null)
                {
                    Log.i(TAG," selected user = "+contactAndGroup.getName());

                    if (ContactAndGroup.TYPE_FRIEND.equalsIgnoreCase(contactAndGroup.getType()))
                        userArr = new String[]{contactAndGroup.getId()};
                    else if (ContactAndGroup.TYPE_GROUP.equalsIgnoreCase(contactAndGroup.getType()))
                       groupArr = new String[]{contactAndGroup.getId()};

                       mPresenterNote.shareNote(mNote, groupArr, userArr);
                    mLoader = DialogLoader.getInstance("Sharing...");
                    mLoader.show(getSupportFragmentManager(), ConstHelper.Tag.Fragment.DIALOG_PROGRESS);
                }
               else
                   Log.e(TAG, "contactAndGroup is null");
            }


            // checking if data is response of creating note
            if (requestCode == REQUEST_CODE_CREATE_NOTE && data.getExtras() != null)
            {
                Log.i(TAG,"note created successfully");
                Notes note = (Notes) data.getExtras().getSerializable(ActivityNoteCreate.KEY_NOTES_DATA);
                if (note != null)
                {
                    Log.i(TAG,"note created successfully , refreshing note list");
                    requestAllNotesOfCategory();

                }
            }

            if (requestCode == REQUEST_CODE_EDIT_NOTE && data.getExtras() != null)
            {
                boolean isNoteEdited = data.getExtras().getBoolean(ConstHelper.Key.IS_EDITED);
                if (isNoteEdited)
                    requestAllNotesOfCategory();
            }
            if (requestCode == REQUEST_CODE_COPY_NOTE && data.getExtras() != null)
            {
                boolean isNoteEdited = data.getExtras().getBoolean(ConstHelper.Key.IS_EDITED);
                if (isNoteEdited)
                    requestAllNotesOfCategory();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }





    // setting refresh false to hide
    private void hideLoader()
    {
        if (mRefreshLayout != null)
           mRefreshLayout.setRefreshing(false);

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
    public void onReceivedAllCategory(Notes notes) {
        hideLoader();

    }

   /* @Override
    public void onReceivedAllCategory(List<Note> categoryList) {
        hideLoader();
    }*/

    @Override
    public void onNoteCreated(Notes note) {
        hideLoader();
    }

    @Override
    public void onNoteDeleted(Notes note)
    {
        hideLoader();
        if (mAdapterNotes != null && mPositionSelectedNoteInAdapter>-1)
            mAdapterNotes.onNoteDeleted(mPositionSelectedNoteInAdapter);
    }

    @Override
    public void onNoteShared(Notes note)
    {
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
        if (mAdapterNotes != null)
            mAdapterNotes.addNotes(note);
    }

    @Override
    public void onReceivedNoteData(Notes note) {
        hideLoader();
    }


    // this method is called to update note list
    @Override
    public void onReceivedAllNotes(Notes note)
    {
        hideLoader();

    }

    @Override
    public void onNoteEdited(Notes note) {
        hideLoader();
    }

    @Override
    public void onFailed(String message)
    {
        hideLoader();
        if (message != null && !message.isEmpty())
              DialogHelper.showSimpleCustomDialog(mContext, message);
       /* else
            DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title));*/

    }

}
