package org.ctdworld.propath.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogLoader extends DialogFragment
{
    private static final String TAG = DialogLoader.class.getSimpleName();
    CreateGroupListener mListener;
    private static DialogLoader dialogLoader;
    private TextView mTxtProgressTitle;


    private static final String KEY_PROGRESS_TITLE = "progress title";
    private String mStrProgressTitle = "Wait...";

    public DialogLoader() {
        // Required empty public constructor
    }  // constructor

    public interface CreateGroupListener{void onGroupNameReceived(String groupName);}


    public static DialogLoader getInstance(String progressTitle)
    {
       // Log.i(TAG,"title = "+progressTitle);
        if (dialogLoader == null)
            dialogLoader =  new DialogLoader();

        Bundle bundle = new Bundle();

        // modal class to retain CreateGroupListener object
        bundle.putString(KEY_PROGRESS_TITLE, progressTitle);
        dialogLoader.setArguments(bundle);

        return dialogLoader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
           mStrProgressTitle = bundle.getString(KEY_PROGRESS_TITLE);
          //  Log.i(TAG,"mStrProgressTitle = "+mStrProgressTitle);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.dialog_progress, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        mTxtProgressTitle = view.findViewById(R.id.dialog_progress_txt_message);
        mTxtProgressTitle.setText(mStrProgressTitle);

        return view;
    }


    public void changeProgressTitle(String title)
    {
       mTxtProgressTitle.setText(title);
    }

/*

    public void setListener(CreateGroupListener listener)
    {
        this.mListener = listener;
    }


    // to set CreateGroupListener listener
    private static class ListenerObject implements Serializable {
        public CreateGroupListener getListener() {
            return listener;
        }

        public void setListener(CreateGroupListener listener) {
            this.listener = listener;
        }

        CreateGroupListener listener;
    }
*/

}
