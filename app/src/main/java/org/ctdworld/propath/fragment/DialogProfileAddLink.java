package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.ctdworld.propath.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogProfileAddLink extends DialogFragment
{
    private final String TAG = DialogProfileAddLink.class.getSimpleName();
    AddLinkListener mListener;


    Button btnSave;
    //  Button btnCancel;
    EditText editText;
    ImageView imgCancel;



    public DialogProfileAddLink() {
        // Required empty public constructor
    }

    public interface AddLinkListener{void onLinkReceived(String link);}


   /* public static DialogProfileAddLink getInstance(AddLinkListener listener)
    {
        DialogProfileAddLink dialogCreateGroup = new DialogProfileAddLink();
        Bundle bundle = new Bundle();

        // modal class to retain CreateGroupListener object
        ListenerObject listenerObject = new ListenerObject();
        listenerObject.setListener(listener);

        bundle.putSerializable("listener",listenerObject);

        return dialogCreateGroup;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Bundle bundle = getArguments();
        if (bundle != null)
        {
            ListenerObject listenerObject = (ListenerObject) bundle.getSerializable("listener");
           *//* if (listenerObject != null)
            mListener = listenerObject.getListener();*//*
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.dialog_profile_add_link, container, false);
        final Dialog dialog = getDialog();
        if (dialog !=null)
        {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

        }

        btnSave = view.findViewById(R.id.dialog_add_link_btn_save);
      //btnCancel = view.findViewById(R.id.btn_cancel);
        editText = view.findViewById(R.id.dialog_add_link_edit_link);
        imgCancel = view.findViewById(R.id.dialog_add_link_img_cancel);


        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (mListener != null)
                    mListener.onLinkReceived(editText.getText().toString().trim());
                if (dialog != null)
                    dialog.dismiss();
            }
        });


        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null)
                dialog.dismiss();
            }
        });

        return view;
    }


    public void setListener(AddLinkListener listener)
    {
        this.mListener = listener;
    }


   /* // to set CreateGroupListener listener
    private static class ListenerObject implements Serializable {
        public CreateGroupListener getListener() {
            return listener;
        }

        public void setListener(CreateGroupListener listener) {
            this.listener = listener;
        }

        CreateGroupListener listener;
    }*/

}
