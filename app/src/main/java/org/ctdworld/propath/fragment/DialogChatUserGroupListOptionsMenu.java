package org.ctdworld.propath.fragment;


import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.ctdworld.propath.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogChatUserGroupListOptionsMenu extends DialogFragment
{
    private final String TAG = DialogChatUserGroupListOptionsMenu.class.getSimpleName();

    public DialogChatUserGroupListOptionsMenu() {
        // Required empty public constructor
    }


    //View mCreateGroup, mAddContact;
    View mRequests;
    ContactOptionsMenuListener mListener;
    Dialog mDialog;

   /* public static int ITEM_TYPE_CRATE_GROUP = 3;
    public static int ITEM_TYPE_ADD_CONTACT = 4;*/
    public static int ITEM_TYPE_REQUESTS = 5;



    public interface ContactOptionsMenuListener
    {
        void onOptionsMenuItemClicked(int itemType);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_chat_user_group_list_options_menu, container, false);
        getDialog().getWindow().setGravity(Gravity.RIGHT|Gravity.TOP);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.y = 100;
        window.setAttributes(layoutParams);

        init(view);
        setListeners();
        return view;
    }


    private void init(View view)
    {
        mDialog = getDialog();
      /*  mCreateGroup = view.findViewById(R.id.contact_options_menu_create_group);
        mAddContact = view.findViewById(R.id.contact_options_menu_add_contact);*/
       mRequests = view.findViewById(R.id.chat_user_group_list_options_menu_create_group);
    }




    public void setListeners()
    {
       /* mCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOptionsMenuItemClicked(ITEM_TYPE_CRATE_GROUP);
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });


        mAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOptionsMenuItemClicked(ITEM_TYPE_ADD_CONTACT);
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });
*/

        mRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOptionsMenuItemClicked(ITEM_TYPE_REQUESTS);
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });

    }


    // TO SET LISTENER FORM CALLING COMPONENT
    public void setOnItemClickListener(ContactOptionsMenuListener listener)
    {
        this.mListener = listener;
    }

}
