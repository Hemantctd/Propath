package org.ctdworld.propath.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Notes;


public class FragmentNotesOptionsMenu extends BottomSheetDialogFragment implements View.OnClickListener
{

    //tag to log
    private static final String TAG = FragmentNotesOptionsMenu.class.getSimpleName();

    private static Context mContext;

    // key to put Note object
    private static final String KEY_SELECTED_ITEM_POSITION = "selected item position";
    private static final String KEY_NOTE_DATA = "note data";

    //# Options in this fragment which will user select
    public static final int OPTION_SHARE = 1;
    public static final int OPTION_DELETE = 2;
    public static final int OPTION_EDIT = 3;
    public static final int OPTION_COPY= 4;
    //# data received from calling component
    private Notes mNote =  null;
    private int mSelectedItemPosition;

    //# Listener to send selected item
    private OnNotesOptionsListener mListener;


    // # views
    private View mViewShare, mViewDelete, mViewEdit,mViewCopy;

    //# current fragment dialog object
    Dialog mDialog;


    //# public constructor
    public FragmentNotesOptionsMenu() {
        // Required empty public constructor
    }


    // setting Note object as argument
    public static FragmentNotesOptionsMenu newInstance(Notes note, int selectedItemPosition) {
        FragmentNotesOptionsMenu fragment = new FragmentNotesOptionsMenu();
        Bundle args = new Bundle();
        args.putSerializable(KEY_NOTE_DATA, note);
        args.putInt(KEY_SELECTED_ITEM_POSITION, selectedItemPosition);
        fragment.setArguments(args);
        return fragment;
    }


    // Initializing mNote from getArguments
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNote = (Notes) getArguments().getSerializable(KEY_NOTE_DATA);
            mSelectedItemPosition = getArguments().getInt(KEY_SELECTED_ITEM_POSITION);
        }


    }


   /* @Override
    public void onResume() {
        super.onResume();
        mDialog = getDialog();
        if (mDialog != null)
        {
            Window window = mDialog.getWindow();
            if (window != null)
            {
                window.getAttributes().windowAnimations = R.style.DialogTheme;
                window.setGravity(Gravity.BOTTOM);

                //# setting width match parent. it must be used in onResume() method, it will not work in onCreateView() method
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes( params);
            }
            else
                Log.e(TAG,"window object is null in onResume() method");
        }
        else
            Log.e(TAG,"mDialog object is null in onResume() method");


    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_notes_options_menu, container, false);


        init(view);

        mViewShare.setOnClickListener(this);
        mViewDelete.setOnClickListener(this);
        mViewEdit.setOnClickListener(this);
        mViewCopy.setOnClickListener(this);

        Log.i(TAG,"starting animation");

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_out_dialog);
                view.startAnimation(animation);

                *//*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params. = Gravity.BOTTOM;
                view.setLayoutParams(params);*//*

            }
        }, 1000);*/

        return view;
    }

    private void init(View view) {
        mContext = getContext();
        mViewCopy=view.findViewById(R.id.fragment_notes_options_menu_layout_copy);
        mViewShare = view.findViewById(R.id.fragment_notes_options_menu_layout_share);
        mViewDelete = view.findViewById(R.id.fragment_notes_options_menu_layout_delete_note);
        mViewEdit = view.findViewById(R.id.fragment_notes_options_menu_layout_edit_note);
    }


    @Override
    public void onClick(View view)
    {
        Log.i(TAG,"option clicked , ");
        switch (view.getId())
        {
            case R.id.fragment_notes_options_menu_layout_share:
                onOptionSelected(OPTION_SHARE);
                break;

            case R.id.fragment_notes_options_menu_layout_delete_note:
                onOptionSelected(OPTION_DELETE);
                break;

            case R.id.fragment_notes_options_menu_layout_edit_note:
                onOptionSelected(OPTION_EDIT);

            case R.id.fragment_notes_options_menu_layout_copy:
                onOptionSelected(OPTION_COPY);

                    break;

        }

    }


    private void onOptionSelected(int selectedOption)
    {
        Log.i(TAG,"onOptionSelected , selectedOption = "+selectedOption);
        if (mListener != null)
            mListener.onNotesOptionSelected(selectedOption, mNote, mSelectedItemPosition);
        else
            Log.i(TAG,"mListener is null");

       /* if (mDialog != null)
            mDialog.dismiss();
        else
            Log.e(TAG,"mDialog is null in onOptionSelected() method");*/
       dismiss();

    }




    //# setting listener OnNotesOptionListener to send data to calling component
    public void setOnNotesOptionListener(OnNotesOptionsListener listener)
    {
        this.mListener = listener;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNotesOptionsListener {
        // TODO: Update argument type and name
        void onNotesOptionSelected(int selectedItem, Notes note, int selectedItemPosition);
    }
}
