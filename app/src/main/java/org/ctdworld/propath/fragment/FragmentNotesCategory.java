package org.ctdworld.propath.fragment;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import org.ctdworld.propath.R;
import org.ctdworld.propath.adapter.AdapterNoteCategory;
import org.ctdworld.propath.base.BaseFragment;
import org.ctdworld.propath.contract.ContractNotes;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.PermissionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.BottomSheetOption;
import org.ctdworld.propath.model.Notes;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterNotes;

import java.util.ArrayList;
import java.util.List;


public class FragmentNotesCategory extends BaseFragment implements ContractNotes.View
{
    Context mContext;
    private static final String TAG = FragmentNotesCategory.class.getSimpleName();
     private FloatingActionButton mFAB;
    private RecyclerView mRecyclerViewCategory;
    AdapterNoteCategory mAdapter;

    // #listener to send selected category back if any page has requested to get selected category
    private OnNotesCategorySelectedListener mListenerSelectedLCategory;

    // # note presenter
    private ContractNotes.Presenter mPresenterNote;


    // # views
  //  View mLayoutData;
    private TextView mTxtMessage; // this is displayed in center if screen
    SwipeRefreshLayout mRefreshLayout;

    // # for filter
    private EditText mEditSearch;
    private ImageView mImgMic;
    private List<Notes> mListNoteCategory;

    public FragmentNotesCategory()
    {

    }




    // listener to be used when a category is selected to send data to which has requested to select category
    public interface OnNotesCategorySelectedListener
    {
        void onCategorySelected(Notes note);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notes_category, container, false);


        init(view);
        setAdapterForCategory();
        setFloatingButton();

        mRefreshLayout.setRefreshing(true);
        requestAllCategories(); // to get all categories from server

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestAllCategories();
            }
        });
        mEditSearch.addTextChangedListener(onSearchTextChanged);  // filtering list by title
        mImgMic.setOnClickListener(onImgIconClicked);  // to filter list with voice text

        return view;
    }


    // to initialize variables
    private void init(View view)
    {
        mContext = getContext();
        mListNoteCategory = new ArrayList<>();
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
        //mLayoutData = view.findViewById(R.id.fragment_notes_category_layout_data);
        mRefreshLayout = view.findViewById(R.id.fragment_notes_category_refresh_layout);
        mFAB = view.findViewById(R.id.fragment_notes_category_float_btn);
        mRecyclerViewCategory = view.findViewById(R.id.fragment_notes_category_recycler_category);
        mTxtMessage = view.findViewById(R.id.fragment_notes_category_txt_no_connection);
        mImgMic = view.findViewById(R.id.img_mic);
        mEditSearch = view.findViewById(R.id.edit_search);

    }


    // to get all categories from server
    private void requestAllCategories()
    {
        if (UtilHelper.isConnectedToInternet(mContext))
        {
            mTxtMessage.setVisibility(View.GONE);
            mRecyclerViewCategory.setVisibility(View.VISIBLE);
            try
            {
                mAdapter.clearCategoryList();

                if (mListNoteCategory != null)
                    mListNoteCategory.clear();
                Notes note = new Notes();
               note.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());
               mPresenterNote.getAllCategory(note);

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
            mRecyclerViewCategory.setVisibility(View.GONE);
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtMessage.setText(getString(R.string.no_connection));
        }
    }



    // to filter list with typing text
    private TextWatcher onSearchTextChanged = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //new array list that will hold the filtered data
            ArrayList<Notes> filteredList = new ArrayList<>();

            //looping through existing elements
            for (Notes noteCategory : mListNoteCategory)
            {
                //if the existing elements contains the search input
                String category = noteCategory.getCategoryName();

                    if (category.toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        //adding the element to filtered list
                        filteredList.add(noteCategory);
                    }

            }
            mAdapter.filterList(filteredList);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //after the change calling the method and passing the search input

        }
    };


    // to filter list with voice text
    private View.OnClickListener onImgIconClicked = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            PermissionHelper permissionHelper = new PermissionHelper(mContext);
            if (permissionHelper.isPermissionGranted(Manifest.permission.RECORD_AUDIO))
            {
                Log.i(TAG,"voiceToText() method called ");
                FragmentSpeechRecognition fragmentSpeechRecognition = new FragmentSpeechRecognition();
                fragmentSpeechRecognition.setListener(new FragmentSpeechRecognition.SpeechListener() {
                    @Override
                    public void onReceiveText(String spokenText)
                    {
                        mEditSearch.setText(spokenText);
                        mEditSearch.requestFocus();
                    }

                    @Override
                    public void onError() {
                        mEditSearch.requestFocus();
                    }
                });

                fragmentSpeechRecognition.show(getChildFragmentManager(),"");
            }
            else
                permissionHelper.requestPermission(Manifest.permission.RECORD_AUDIO,"Permission required voice feature");
        }
    };



    // # setting adapter to show category
    private void setAdapterForCategory()
    {
        mAdapter = new AdapterNoteCategory(getActivity(), new ArrayList<Notes>(), adapterListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewCategory.setLayoutManager(layoutManager);
        mRecyclerViewCategory.setAdapter(mAdapter);


        /*# if mListenerSelectedLCategory is null then it means no component has requested to get category so not setting
        mAdapter.setOnNotesCategorySelectedListener*/
        if (mListenerSelectedLCategory != null)
        {
            //# setting OnNotesCategorySelectedListener listener to get back selected category
            mAdapter.setOnNotesCategorySelectedListener(new OnNotesCategorySelectedListener() {
                @Override
                public void onCategorySelected(Notes category) {
                    Log.i(TAG,"onCategorySelected() method called");
                    if (mListenerSelectedLCategory != null)
                    {
                        mListenerSelectedLCategory.onCategorySelected(category);
                        mListenerSelectedLCategory = null;
                    }
                    else
                        Log.e(TAG,"mListenerSelectedLCategory is null in setAdapterForCategory");
                }
            });
        }
        else
            Log.e(TAG,"listener is null in setAdapterForCategory");

    }



    // setting floating button
    private void setFloatingButton()
    {
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //# to create new category
                onCreateCategory();
            }
        });
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        /*// setting listener to be notified when plus icon in toolbar in ActivityNoteContainer is clicked
        if (context instanceof ActivityNoteContainer)
        {
            ActivityNoteContainer activityNotesContainer = (ActivityNoteContainer) context;
            activityNotesContainer.setOnCreateCategoryListener(this); // setting listener
        }
        else
            Log.e(TAG,"context is not instance of ActivityNoteContainer in onAttach() method");*/





        // # initializing mListenerSelectedLCategory to send back selected category to page which has requested to get category
        if (context instanceof OnNotesCategorySelectedListener)
            mListenerSelectedLCategory = (OnNotesCategorySelectedListener) context;
        else
            Log.e(TAG,"listener is not instance of AdapterNoteCategory.OnNotesCategorySelectedListener");

    }


    // initializing AdapterNoteCategory.AdapterNoteCategoryListener. deleting category
    private AdapterNoteCategory.AdapterNoteCategoryListener adapterListener = new AdapterNoteCategory.AdapterNoteCategoryListener()
    {
        @Override
        public void onBottomSheetOptionSelected(int option, final Notes category, final int position) {
            if (mPresenterNote != null)
            {
                if (option == BottomSheetOption.OPTION_DELETE) {
                    Log.i(TAG, "item long pressed");
                    String message = "All notes in this category will be uncategorised.";
                    DialogHelper.showCustomDialog(mContext, "Delete Category", message, "Delete", "Cancel", new DialogHelper.ShowDialogListener() {
                        @Override
                        public void onOkClicked() {
                            Log.i(TAG, "deleting category");
                            category.setPositionInAdapter(position);   // # setting category position to refresh list if category gets deleted
                            mPresenterNote.deleteCategory(category);

                            showLoader(getString(R.string.deleting));
                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                }
                else if (option == BottomSheetOption.OPTION_EDIT)
                {

                }
                else if (option == BottomSheetOption.OPTION_SHARE)
                {

                    DialogHelper.showCustomDialog(mContext, "Are you sure want to share this category ?", "Share", "Cancel", new DialogHelper.ShowDialogListener() {
                        @Override
                        public void onOkClicked() {

                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                }
            }
            else
            Log.e(TAG,"mPresenterNote is null in adapterListener");
        }
    };

    // creating new category
    /*@Override*/
    private void onCreateCategory() {
        Log.i(TAG,"add category clicked");

        //setAdapterForCategory();

        DialogEditText dialogEditText = DialogEditText.getInstance("Create Category","Enter category name", "create",true);
        dialogEditText.setOnButtonClickListener(new DialogEditText.OnButtonClickListener()
        {
            @Override
            public void onButtonClicked(String enterValue)
            {
                Log.i(TAG,"creating category");
                showLoader(getString(R.string.message_saving));

                Notes category = new Notes();

                category.setCategoryName(enterValue);
                category.setCreatedByUserId(SessionHelper.getInstance(mContext).getUser().getUserId());

                mPresenterNote.createCategory(category);
            }
        });

        dialogEditText.show(getChildFragmentManager(),ConstHelper.Tag.Fragment.DIALOG_EDIT_TEXT);
    }


    @Override
    public void hideLoader() {
        super.hideLoader();
        mRefreshLayout.setRefreshing(false);
    }

    // this method is called when category is created successfully
    @Override
    public void onCategoryCreated(Notes category)
    {
        Log.i(TAG,"onCategoryCreated() method called");

        hideLoader();
        mTxtMessage.setVisibility(View.GONE);
        mRecyclerViewCategory.setVisibility(View.VISIBLE);

        // # checking if any component has requested to get category
        if (mListenerSelectedLCategory != null)
            mListenerSelectedLCategory.onCategorySelected(category);
        else  // # updating category list
        {
            Log.i(TAG,"updating category list");
            if (mAdapter != null)
                mAdapter.addCategory(category);
            else
                Log.e(TAG,"mAdapter is null in onCategoryCreated() method");
        }
    }

    @Override
    public void onCategoryDeleted(Notes category)
    {
        Log.i(TAG,"onCategoryDeleted() method called");
        hideLoader();
        if (mAdapter != null)
        {
            Log.i(TAG,"onCategoryDeleted() method called, removing category form list in adapter");
            mAdapter.onCategoryDeleted(category.getPositionInAdapter());

            // initializing new note list
            mListNoteCategory = mAdapter.getNotesCategoryList();
        }
        else
            Log.e(TAG,"mAdapter is null");
    }

    @Override
    public void onReceivedAllCategory(Notes categoryList)
    {
       /* hideLoader();

        Collections.reverse(categoryList);
        // # data will be loaded in adapter only when user types text in search box
        if (mAdapter != null)
            mAdapter.addCategory(categoryList);  // updating category list


      //  if (mAdapter != null && note != null && mListNoteCategory != null)
            mListNoteCategory =  categoryList;*/


        hideLoader();
        if (mAdapter != null)
            mAdapter.addCategory(categoryList);  // updating category list

        if (mAdapter != null && categoryList != null && mListNoteCategory != null)
            mListNoteCategory.add(categoryList);
    }











    @Override
    public void onNoteCreated(Notes note)
    {
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
    public void onFailed(String message)
    {
        Log.e(TAG,"onFailed() method called");
        hideLoader();
        mRecyclerViewCategory.setVisibility(View.GONE);
        mTxtMessage.setVisibility(View.VISIBLE);
        if (message != null && !message.isEmpty())
            mTxtMessage.setText(message);



          //  DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title), message);
      /*  else
            mTxtMessage.setText(getString(R.string.failed_title));*/
        //DialogHelper.showSimpleCustomDialog(mContext, getString(R.string.failed_title));

    }

}
