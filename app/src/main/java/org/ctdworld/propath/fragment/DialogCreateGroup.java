package org.ctdworld.propath.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterGroupChatInjury;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogCreateGroup extends DialogFragment implements ContractGroupChatInjury.View
{
    private final String TAG = DialogCreateGroup.class.getSimpleName();
  //  CreateGroupListener mListener;
    ContractGroupChatInjury.Presenter mPresenter;
    Context mContext;
    Dialog mDialog;
    DialogLoader mDialogLoader;
    OnGroupCreateListener mOnGroupCreateListener;


    // listener for creating group
    public interface OnGroupCreateListener{ void onGroupCreated(Chat chat);}

    private static final String KEY_GROUP_TYPE = "group_type" ; // group chat or injury management
    private String mGroupType = null;  // contains group type sent from as parameter in getInstance

    public DialogCreateGroup() {
    }    // Required empty public constructor



    public static DialogCreateGroup getInstance(String groupType)
    {
        DialogCreateGroup dialogCreateGroup = new DialogCreateGroup();
        Bundle bundle = new Bundle();

        // modal class to retain CreateGroupListener object
        bundle.putString(KEY_GROUP_TYPE, groupType);
        dialogCreateGroup.setArguments(bundle);

        return dialogCreateGroup;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mGroupType =  bundle.getString(KEY_GROUP_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.dialog_create_group, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);

        mDialog = getDialog();
        mContext = getContext();
        TextView btnSave = view.findViewById(R.id.btn_save);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        final EditText editText = view.findViewById(R.id.create_group_edit_group_name);
         mPresenter = new PresenterGroupChatInjury(getContext(), this);
        mDialogLoader = DialogLoader.getInstance("Creating Group...");


        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!editText.getText().toString().trim().isEmpty())
                {
                    if (UtilHelper.isConnectedToInternet(mContext))
                    {
                        if (mGroupType != null)
                        {
                            mDialogLoader.show(getChildFragmentManager(), "");
                            Log.i(TAG,"group type = "+mGroupType);
                            Chat chat = new Chat();
                            chat.setGroupAdminId(SessionHelper.getInstance(mContext).getUser().getUserId()); // setting admin id
                            chat.setChattingToName(editText.getText().toString().trim());
                            chat.setGroupType(mGroupType); // setting group type group chat or injury management

                            mPresenter.createGroup(chat);
                        }
                        else
                            Log.e(TAG,"group type is null");

                    }
                    else
                        DialogHelper.showSimpleCustomDialog(mContext, "No Connection...");

               /* if (mListener != null)
                    mListener.onGroupNameReceived(editText.getText().toString().trim());*/
                    // getDialog().dismiss();
                }
                else
                    DialogHelper.showSimpleCustomDialog(mContext,"Please enter group name...");
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }


    public void setOnGroupCreateListener(OnGroupCreateListener onGroupCreateListener)
    {
        this.mOnGroupCreateListener = onGroupCreateListener;
    }



    // dismissing dialog
    private void dismissDialog()
    {
        if (mDialog != null)
            mDialog.dismiss();

        if (mDialogLoader != null)
            mDialogLoader.dismiss();
    }








    @Override
    public void onGroupCreated(final Chat groupChat)
    {
        Log.i(TAG,"onGroupCreated() method called");
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    if (mOnGroupCreateListener != null)
                        mOnGroupCreateListener.onGroupCreated(groupChat);
            /*Intent intent = new Intent(mContext,ActivityChatAddParticipantToGroup.class);
            intent.putExtra(ActivityChatAddParticipantToGroup.KEY_CHAT_DATA, groupChat);
            startActivity(intent);*/
                }
            });
        }

    }



    @Override
    public void onShowMessage(final String message)
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    DialogHelper.showSimpleCustomDialog(mContext, message);
                }
            });
        }

    }

    @Override
    public void onFailed() {
        Activity activity = getActivity();
        if (activity != null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    DialogHelper.showSimpleCustomDialog(mContext, "Creating group failed...");
                }
            });
        }

    }

    @Override
    public void onEditGroupNameSuccessful(Chat chat) {

    }

    @Override
    public void onEditGroupImageSuccessful(Chat chat) {

    }

    @Override
    public void onRemoveParticipantSuccess(Chat chat, int positionInAdapter) {

    }

    @Override
    public void onGroupExitSuccess(Chat chat) {

    }


    @Override
    public void onReceivedGroup(Chat chat) {

    }

    @Override
    public void onReReceivedGroupMember(Chat chat) {

    }




}
