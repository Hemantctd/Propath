package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ctdworld.propath.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogEditText extends DialogFragment implements View.OnClickListener
{

    private static final String TAG = DialogEditText.class.getSimpleName();
    TextView mTxtTitle;
    EditText mEditText;
    Button mBtnEnter;

    private static final String KEY_POSITIVE_BUTTON_TEXT = "button text";
    private static final String KEY_TITLE = "title";
    private static final String KEY_EDIT_TEXT_OR_HINT = "edit hint";
    private static final String KEY_SHOW_TEXT_INSTEAD_HINT = "show text or hint";
    private static final String KEY_IS_CANCELABLE = "is_cancelable";

    String mStrPositiveButton;
    String mStrTitle;
    String mStrEditHint;
    boolean mIsCancelable;  // cancel dialog if user clicks outside of dialog box
    boolean mBoolShowTextInsteadHint = false; // to check if show hint or text

    private DialogEditText.OnButtonClickListener mListener;

    public static DialogEditText getInstance(String title, String editHint, String buttonText, boolean isCancelable)
    {
        DialogEditText filesNotesCreate = new DialogEditText();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE,title);
        bundle.putString(KEY_EDIT_TEXT_OR_HINT, editHint);
        bundle.putString(KEY_POSITIVE_BUTTON_TEXT,buttonText);
        bundle.putBoolean(KEY_IS_CANCELABLE, isCancelable);
        filesNotesCreate.setArguments(bundle);

        return filesNotesCreate;
    }

    public static DialogEditText getInstance(String title, String text, boolean showTextInsteadHint, String buttonText, boolean isCancelable)
    {
        DialogEditText filesNotesCreate = new DialogEditText();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE,title);
        bundle.putString(KEY_EDIT_TEXT_OR_HINT, text);
        bundle.putString(KEY_POSITIVE_BUTTON_TEXT,buttonText);
        bundle.putBoolean(KEY_SHOW_TEXT_INSTEAD_HINT, showTextInsteadHint);
        bundle.putBoolean(KEY_IS_CANCELABLE, isCancelable);

        filesNotesCreate.setArguments(bundle);

        return filesNotesCreate;
    }

    public DialogEditText() { }  // constructor



    public interface OnButtonClickListener   // listener
    {
        void onButtonClicked(String enteredValue);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            Bundle bundle = getArguments();
            try
            {
                mStrTitle = bundle.getString(KEY_TITLE);
                mStrEditHint = bundle.getString(KEY_EDIT_TEXT_OR_HINT);
                mStrPositiveButton = bundle.getString(KEY_POSITIVE_BUTTON_TEXT);
                mBoolShowTextInsteadHint = bundle.getBoolean(KEY_SHOW_TEXT_INSTEAD_HINT, false);
                mIsCancelable = bundle.getBoolean(KEY_IS_CANCELABLE, false);
            }
            catch (Exception e)
            {
                Log.e(TAG,"Error in onCreate() method , "+e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_edittext, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG,"DialogEditText fragment appeared ");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(mIsCancelable);


        mTxtTitle = view.findViewById(R.id.dialog_files_notes_txt_title);
        mEditText = view.findViewById(R.id.dialog_files_notes_edit_name);
        mBtnEnter =  view.findViewById(R.id.dialog_files_notes_btn_enter);

        setTextOnFieldsByType();

        mBtnEnter.setOnClickListener(this);   // setting click listener


    }

    private void setTextOnFieldsByType()
    {
       /* if (VALUE_TYPE == TYPE_CREATE_CATEGORY)
        {*/
            mTxtTitle.setText(mStrTitle);
            if (mBoolShowTextInsteadHint)
                mEditText.setText(mStrEditHint);
            else
                mEditText.setHint(mStrEditHint);
            mBtnEnter.setText(mStrPositiveButton);
        /*}*/

/*

        if (VALUE_TYPE == TYPE_CREATE_NOTE)
        {
            mTxtTitle.setText("Create New Note");
            mEditText.setHint("Enter Note Name");
            mBtnEnter.setText(VALUE_BUTTON_TEXT);
        }
*/


    }



    @Override
    public void onClick(View view)
    {
        Log.i(TAG,"onClick() method called... ");

        switch (view.getId())
        {
            case R.id.dialog_files_notes_btn_enter:
                if (mListener != null)
                    mListener.onButtonClicked(mEditText.getText().toString().trim());

                Dialog dialog = getDialog();
                if (dialog != null)
                    dialog.dismiss();
                break;

        }
    }

    public void setOnButtonClickListener(DialogEditText.OnButtonClickListener listener)
    {
        this.mListener = listener;
    }

}
