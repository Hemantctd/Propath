package org.ctdworld.propath.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityContactsAndGroups;
import org.ctdworld.propath.activity.ActivityNoteCreate;
import org.ctdworld.propath.activity.ActivityNoteEdit;
import org.ctdworld.propath.activity.ActivityNoteView;
import org.ctdworld.propath.adapter.AdapterNoteAll;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.ContactAndGroup;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNotes;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotesAll extends BaseFragment implements ContractNotes.View {
    // # constants
    private static final String TAG = FragmentNotesAll.class.getSimpleName();


    // # views
    private FloatingActionButton mFAB;
    private RecyclerView mRecyclerNotesAll;
    private TextView mTxtMessage; // this is displayed in center if screen
    private SwipeRefreshLayout mRefreshLayout;
    private EditText mEditSearch;  // to filter list
    private ImageView mImgMic;
    ProgressBar mProgressBar;


    // # other variables
    private ContractNotes.Presenter mPresenterNote;
    private List<Notes> mListNotes;
    private AdapterNoteAll mAdapterNoteAll;
    private Context mContext;
    private Notes mNoteAdapter; // note selected in adapter


    public FragmentNotesAll() {
    } //# default empty constructor


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes_all, container, false);
        init(view);
        attachAdapterNotesAll();
        setFloatingButton();

        requestAllNotes(); // to get all categories from server

        // # listeners
        mRefreshLayout.setOnRefreshListener(onRefreshListener);  // to refresh layout
        mEditSearch.addTextChangedListener(onSearchTextChanged);  // filtering list by title
        mImgMic.setOnClickListener(onImgMicIconClicked);  // to filter list with voice text

        return view;
    }


    // initializing variables
    private void init(View view) {
        mContext = getContext();
        mListNotes = new ArrayList<>();
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
        mRefreshLayout = view.findViewById(R.id.fragment_notes_all_refresh_layout);
        mRecyclerNotesAll = view.findViewById(R.id.unitNotesRecyclerView);
        mFAB = view.findViewById(R.id.fragment_notes_all_float_btn);
        mTxtMessage = view.findViewById(R.id.fragment_notes_all_txt_message);
        mImgMic = view.findViewById(R.id.img_mic);
        mEditSearch = view.findViewById(R.id.edit_search);
    }


    // to get all categories from server
    private void requestAllNotes() {
        Log.i(TAG, "requesting all notes");
        if (isConnectedToInternet()) {
            mRefreshLayout.setRefreshing(true);
            mTxtMessage.setVisibility(View.GONE);
            mRecyclerNotesAll.setVisibility(View.VISIBLE);
            try {
                mAdapterNoteAll.clearNotesList();
                if (mListNotes != null) // clearing previous data
                    mListNotes.clear();

                Notes note = new Notes();
                note.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
                mPresenterNote.getAllNotes(note);

            } catch (Exception e) {
                hideLoader();
                Log.e(TAG, "Error while requesting for contacts , " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            //  DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.no_connection),getString(R.string.connection_message));
            hideLoader();
            mRecyclerNotesAll.setVisibility(View.GONE);
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtMessage.setText(getString(R.string.no_connection));
        }
    }


    // this method attaches adapter on recycler view to show all notes
    private void attachAdapterNotesAll() {
        mAdapterNoteAll = new AdapterNoteAll(mContext, listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerNotesAll.setLayoutManager(layoutManager);
        mRecyclerNotesAll.setAdapter(mAdapterNoteAll);
    }


    // to filter list with typing text
    private TextWatcher onSearchTextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //new array list that will hold the filtered data
            ArrayList<Notes> filteredList = new ArrayList<>();

            //looping through existing elements
            for (Notes note : mListNotes) {
                //if the existing elements contains the search input
                String title = note.getTitle();

                if (title.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    //adding the element to filtered list
                    filteredList.add(note);
                }

            }
            mAdapterNoteAll.filterList(filteredList);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //after the change calling the method and passing the search input

        }
    };


    // to filter list with voice text
    private View.OnClickListener onImgMicIconClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PermissionHelper permissionHelper = new PermissionHelper(mContext);
            if (!permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
                permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO, getString(R.string.message_microphone_permission));
            else {
                FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                    @Override
                    public void onReceiveText(String spokenText) {
                        mEditSearch.setText(mEditSearch.getText().toString() + " " + spokenText);
                        mEditSearch.setSelection(mEditSearch.getText().toString().length());
                        mEditSearch.requestFocus();
                    }

                    @Override
                    public void onError() {
                        mEditSearch.requestFocus();
                    }
                });

                fragmentSpeechRecognition.show(getChildFragmentManager(), "");
            }
        }
    };


    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestAllNotes();
        }
    };


    // receiving selected notes category
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult() method called");
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode && data != null) {

            // checking if data is to be shared, first we select friends or groups in other activity then come back to this activity
            if (requestCode == ConstHelper.RequestCode.SHARE && data.getExtras() != null) {
                ContactAndGroup contactAndGroup = (ContactAndGroup) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                shareNote(contactAndGroup);  // sharing note
            }

            // for
            if (requestCode == ConstHelper.RequestCode.VIEW && data.getExtras() != null) {
                Notes note = (Notes) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (note != null) {
                    Log.i(TAG, "refreshing list , note is delete = " + note.isNoteDeleted() + " ,  note is edited = " + note.isNoteEdited());
                    if (note.isNoteDeleted() && mAdapterNoteAll != null)
                        mAdapterNoteAll.onNoteDeleted(note);

                    else if (note.isNoteEdited() && mAdapterNoteAll != null)
                        mAdapterNoteAll.onNoteUpdated(note);
                }
            }


            // # for creating note
            if (requestCode == ConstHelper.RequestCode.CREATE && data.getExtras() != null) {
                Log.i(TAG, "note created successfully");
                Notes note = (Notes) data.getExtras().getSerializable(ActivityNoteCreate.KEY_NOTES_DATA);
                if (note != null) {
                    Log.i(TAG, "note created successfully refreshing list");
                    requestAllNotes();  // refreshing all note list to show newly created note
                }
            }


            // # for editing note
            if (requestCode == ConstHelper.RequestCode.EDIT && data.getExtras() != null) {
                boolean isNoteEdited = data.getExtras().getBoolean(ConstHelper.Key.IS_EDITED);
                Notes note = (Notes) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (isNoteEdited && note != null) {
                    Log.i(TAG, "refreshing note data after editing note");
                    if (mAdapterNoteAll != null)
                        mAdapterNoteAll.onNoteUpdated(note);
                }
            }

            // # for copy note
            if (requestCode == ConstHelper.RequestCode.COPY && data.getExtras() != null) {
                boolean isNoteCopied = data.getExtras().getBoolean(ConstHelper.Key.IS_COPIED);
                Notes note = (Notes) data.getExtras().getSerializable(ConstHelper.Key.DATA_MODAL);
                if (isNoteCopied && note != null) {
                    Log.i(TAG, "refreshing note data after copying note");
                    if (mAdapterNoteAll != null)
                        mAdapterNoteAll.onNoteUpdated(note);

                }
            }
        }
    }


    // # sharing note with individual friends or groups
    private void shareNote(ContactAndGroup contactAndGroup) {
        String[] userArr = new String[]{};    // array of users whom note will be shared
        String[] groupArr = new String[]{};   // group of users whom note will be shared
        if (contactAndGroup != null) {
            if (ContactAndGroup.TYPE_FRIEND.equalsIgnoreCase(contactAndGroup.getType()))
                userArr = new String[]{contactAndGroup.getId()};   // creating user array with single friend
            else if (ContactAndGroup.TYPE_GROUP.equalsIgnoreCase(contactAndGroup.getType()))
                groupArr = new String[]{contactAndGroup.getId()};  // creating group array with single group id

            // sharing
            mPresenterNote.shareNote(mNoteAdapter, groupArr, userArr);
            showLoader(getString(R.string.message_sharing));
        } else
            Log.e(TAG, "contactAndGroup is null");
    }


    // deleting note selected in adapter
    private void deleteNote() {
        String title = getString(R.string.are_you_sure);
        String message = getString(R.string.confirm_msg_delete_note);
        DialogHelper.showCustomDialog(mContext, title, message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
            @Override
            public void onOkClicked() {
                showLoader(getString(R.string.deleting));
                mPresenterNote.deleteNote(mNoteAdapter);
            }

            @Override
            public void onCancelClicked() {

            }
        });
    }


    // listener in adapter
    private AdapterNoteAll.Listener listener = new AdapterNoteAll.Listener() {
        @Override
        public void onNoteClicked(Notes note, int position) {
            Intent intent = new Intent(mContext, ActivityNoteView.class);
            note.setPositionInAdapter(position);
            intent.putExtra(ConstHelper.Key.DATA_MODAL, note);
            Log.i(TAG, "note" + note);
            startActivityForResult(intent, ConstHelper.RequestCode.VIEW);
        }

        @Override
        public void onOptionMenuClicked(Notes note) {
            mNoteAdapter = note;
            showBottomSheet(note);
        }
    };


    // showing bottom sheet when user clicks on option menu icon, to delete, edit and share note
    private void showBottomSheet(final Notes note) {
        if (note == null)
            return;

        BottomSheetOption.Builder builder = new BottomSheetOption.Builder()
                .addOption(BottomSheetOption.OPTION_DELETE, "Delete Note");
        if (note.getCreatedByUserId().equals(SessionHelper.getUserId(mContext))) {
            builder.addOption(BottomSheetOption.OPTION_SHARE, "Share Note");
            builder.addOption(BottomSheetOption.OPTION_EDIT, "Edit Note");
            builder.addOption(BottomSheetOption.OPTION_COPY, "Copy Note");
        }

        ArrayList<BottomSheetOption> bottomSheetOptions = builder.build();

        Log.i(TAG, "bottom sheet options size = " + bottomSheetOptions.size());


        // List<BottomSheetOption> bottomSheetOptions = builder.build();
        FragmentBottomSheetOptions options = FragmentBottomSheetOptions.getInstance(bottomSheetOptions);
        options.setOnBottomSheetOptionSelectedListener(new FragmentBottomSheetOptions.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(int option) {
                Log.i(TAG, "bottom sheet options selected, option = " + option);
                switch (option) {
                    case BottomSheetOption.OPTION_COPY:
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
                        dialogEditText.show(getChildFragmentManager(),
                                ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
                        break;
                    case BottomSheetOption.OPTION_SHARE:
                        DialogHelper.showCustomDialog(mContext, "Are you sure ", "want to share this note ?", "Ok", "Cancel", new DialogHelper.ShowDialogListener() {
                            @Override
                            public void onOkClicked() {
                                startActivityForResult(new Intent(mContext, ActivityContactsAndGroups.class), ConstHelper.RequestCode.SHARE);
                            }

                            @Override
                            public void onCancelClicked() {

                            }
                        });
                        break;

                    case BottomSheetOption.OPTION_EDIT:
                        Intent intent = new Intent(mContext, ActivityNoteEdit.class);
                        intent.putExtra(ConstHelper.Key.DATA_MODAL, note);
                        startActivityForResult(intent, ConstHelper.RequestCode.EDIT);

                        break;

                    case BottomSheetOption.OPTION_DELETE:
                        deleteNote();
                        break;
                }
            }
        });
        options.show(getChildFragmentManager(), ConstHelper.Tag.Fragment.BOTTOM_SHEET_OPTIONS);
    }


    // setting floating button onClickListener to create new note
    private void setFloatingButton() {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(mContext, ActivityNoteCreate.class), ConstHelper.RequestCode.CREATE);
            }
        });
    }


    @Override
    public void hideLoader() {
        super.hideLoader();
        mRefreshLayout.setRefreshing(false);
    }

    // this method is called when category is created successfully
    @Override
    public void onCategoryCreated(Notes category) {
        Log.i(TAG, "onCategoryCreated() method called");

        hideLoader();
        mTxtMessage.setVisibility(View.GONE);
    }

    @Override
    public void onCategoryDeleted(Notes category) {

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
        if (mAdapterNoteAll != null)
            mAdapterNoteAll.onNoteDeleted(mNoteAdapter);
    }


    @Override
    public void onNoteShared(Notes note) {
        hideLoader();
        DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_success), "Note shared successfully...");
    }

    @Override
    public void onNoteCopy(String notes_id, String title) {
        hideLoader();
        mRefreshLayout.setRefreshing(false);
        if (mAdapterNoteAll != null)
            mAdapterNoteAll.onNoteUpdated(mNoteAdapter);
        DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.message_success), "Note Copy successfully...");
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
        /*hideLoader();
        if (note == null)
            return;


        if (mAdapterNoteAll != null)
            mAdapterNoteAll.addNoteList(note);

        //  hideLoader();
        if (mListNotes != null)
            mListNotes = note.getNoteList();*/

        hideLoader();
        if (mAdapterNoteAll != null)
            mAdapterNoteAll.addNoteList(note);
        mRecyclerNotesAll.scrollToPosition(0);

        if (mListNotes != null && note != null)
            mListNotes.add(note);
    }

    @Override
    public void onNoteEdited(Notes note) {
        hideLoader();
    }

    @Override
    public void onFailed(String message) {
        hideLoader();
        mRecyclerNotesAll.setVisibility(View.GONE);
        mTxtMessage.setVisibility(View.VISIBLE);
        if (message != null && !message.isEmpty())
            mTxtMessage.setText(message);

    }

}
