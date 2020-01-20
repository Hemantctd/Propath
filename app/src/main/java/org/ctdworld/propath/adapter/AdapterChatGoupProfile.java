package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityChatAddParticipantToGroup;
import org.ctdworld.propath.activity.ActivityChatGroupProfile;
import org.ctdworld.propath.contract.ContractGroupChatInjury;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterChatGoupProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = AdapterChatGoupProfile.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PARTICIPANTS = 1;
    private static final int TYPE_FOOTER = 2;
    private Context mContext;
    private Chat mChat;  // it contains group details
    private List<Chat> mListParticipants;
    private ContractGroupChatInjury.Presenter mPresenterGroupProfile;

    private int totalParticipants = 1;  // initializing 1 for admin

    public AdapterChatGoupProfile(Context context, ContractGroupChatInjury.Presenter presenter, Chat chat)
    {
        this.mChat = chat;
        this.mContext=context;
        this.mListParticipants = new ArrayList<>();
        this.mPresenterGroupProfile = presenter;
    }



    public void clearOldList()
    {
        if (mListParticipants != null)
        {
            totalParticipants=0;
            mListParticipants.clear();
            notifyDataSetChanged();
        }
    }


    // adding member in list
    public void addParticipantInList(Chat chat)
    {
        String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
      //  Log.i(TAG,"loginUserId = "+loginUserId);
      //  Log.i(TAG,"member id = "+groupChatInjury.getUserId());
      //  Log.i(TAG,"get admin id = "+mGroupDetails.getAdminId());
        // if logged in user is not admin then this user will not be added in member list

        // group admin is not being added in mListParticipants
        if ( loginUserId.contains(chat.getGroupMemberId()) && !loginUserId.contains(mChat.getGroupAdminId()))
            return;

        if (mChat != null)
        {
            chat.setChattingToId(mChat.getChattingToId());  // adding group id, here chatting to is group id
            this.mListParticipants.add(chat);
            totalParticipants++;   // increasing participant count to show to members
         //   Log.i(TAG, "adding participant position = "+mListParticipants+" , "+ " member count = " + +totalParticipants);
            notifyItemInserted(mListParticipants.size()-1);
        }


    }

    // removing removed user
    public void onMemberRemoved(int positionInAdapter)
    {
        Log.i(TAG,"item removed position = "+positionInAdapter);
        if (mListParticipants != null)
            mListParticipants.remove(positionInAdapter);

        totalParticipants--;

        notifyItemRemoved(positionInAdapter+1);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        if (viewType == TYPE_PARTICIPANTS) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group_profile_participants, parent, false);
            return new ParticipantsViewHolder(itemView);
        }

        else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group_profile_header_layout, parent, false);
            return new HeaderViewHolder(itemView);
        }

        else if (viewType == TYPE_FOOTER) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group_profile_footer_layout, parent, false);
            return new FooterViewHolder(itemView);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        if (holder instanceof HeaderViewHolder)  // showing Header
        {
            Log.i(TAG, "showing header , position = "+position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            //   headerHolder.headerTitle.setText(totalParticipants);
            handleHeaderLayout(headerHolder, position);

        }

        else if (holder instanceof ParticipantsViewHolder)  // #showing Participants
        {
            Log.i(TAG,"showing participants , position = "+position);
            int index = holder.getLayoutPosition()-1; // subtracting 1 because at last index we are using footer, so position contains +1
            ParticipantsViewHolder participantHolder = (ParticipantsViewHolder) holder;  // Holder
            handleParticipantsLayout(participantHolder, index);
        }


        else if (holder instanceof FooterViewHolder) // showing Footer
        {
            Log.i(TAG, "showing footer , position = "+position);
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            handleFooter(footerHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0) {
            return TYPE_HEADER;  // header is at 0 position
        }
        else if (position == mListParticipants.size()+1) {  // footer is at bottm so adding 1 here to
            return TYPE_FOOTER;
        }
        else
        return TYPE_PARTICIPANTS;
    }

    @Override
    public int getItemCount() {
        return mListParticipants.size() + 2;  // adding 2 extra numbers in participants size, 1 is for header and 1 is for footer
    }




    // #ViewHolders



    //ViewHeader for Participants
    private class ParticipantsViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName, txtAdmin;
        ImageView imgUserPic, imgRemoveUser;
        View layout;

        private ParticipantsViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtName =  itemView.findViewById(R.id.recycler_group_profile_participants_txt_name);
            imgUserPic = itemView.findViewById(R.id.recycler_group_profile_participants_img_profile);
            imgRemoveUser = itemView.findViewById(R.id.recycler_group_profile_participants_img_remove_user);
            txtAdmin = itemView.findViewById(R.id.recycler_group_profile_participants_txt_admin);
        }
    }



    //ViewHeader for Header
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtMemberCount;
        View layoutAddParticipants;
        View layout;

        public HeaderViewHolder(View view) {
            super(view);
            layout = view;
            txtMemberCount = view.findViewById(R.id.recycler_group_profile_txt_total_participants);
            layoutAddParticipants = view.findViewById(R.id.recycler_group_profile_layout_add_participants);
        }
    }


    //ViewHeader for Footer
    private class FooterViewHolder extends RecyclerView.ViewHolder
    {
        View layoutExitGroup;

        private FooterViewHolder(View view) {
            super(view);
            layoutExitGroup = view.findViewById(R.id.recycler_group_profile_layout_exit_group);
        }
    }



    private void handleParticipantsLayout(ParticipantsViewHolder holder, final int position)
    {
        Log.i(TAG," handleParticipantsLayout() method called , position = "+position);
        final Chat chat = mListParticipants.get(position); // getting user object
        Log.i(TAG,"member role = "+chat.getGroupMemberRole()+", member id = "+chat.getGroupMemberId()+", member name = "+chat.getGroupMemberName());


        if (SessionHelper.getInstance(mContext).getUser().getUserId().equals(mChat.getGroupAdminId()))
            holder.imgRemoveUser.setVisibility(View.VISIBLE);
        else
        {
            holder.imgRemoveUser.setVisibility(View.GONE);
        }


            if (Chat.MEMBER_ROLE_ADMIN.contains(chat.getGroupMemberRole()))
            {
                Log.i(TAG,"showing admin , role = "+chat.getGroupMemberRole());
                holder.txtAdmin.setVisibility(View.VISIBLE);
                holder.imgRemoveUser.setVisibility(View.GONE);
            }
            else
            {
                holder.txtAdmin.setVisibility(View.GONE);
            }


            holder.txtName.setText(chat.getGroupMemberName());

            int userPicSize = UtilHelper.convertDpToPixel(mContext, (int) mContext.getResources().getDimension(R.dimen.adapterUserPicSize));
            Glide.with(mContext)
                    .load(chat.getChattingToPicUrl())
                    .apply(new RequestOptions().error(R.drawable.ic_profile))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                    .apply(new RequestOptions().override(userPicSize, userPicSize))
                    .into(holder.imgUserPic);

            // removing user
        holder.imgRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
                // checking whether login user is Group admin or not
                if (loginUserId.equals(mChat.getGroupAdminId())) {
                    // Log.i(TAG,"handleParticipantsLayout() method ,logged in user is admin of group = "+mGroupDetails.getGroupName());
                    // Log.i(TAG,"removing item , position = "+position+" , count = "+mListParticipants.size());
                    DialogHelper.showCustomDialog(mContext, "Are you sure...", chat.getGroupMemberName() + " will be removed from group", "Remove", "Close", new DialogHelper.ShowDialogListener() {
                        @Override
                        public void onOkClicked() {
                            if (mPresenterGroupProfile != null)
                                mPresenterGroupProfile.removeParticipant(mListParticipants.get(position), position);
                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                } else
                    Log.i(TAG, "login user is not admin");

            }
        });


        // removing user from group on long press
        /*holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view)
                {
                    String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
                    // checking whether login user is Group admin or not
                    if (loginUserId.equals(mChat.getGroupAdminId()))
                    {
                       // Log.i(TAG,"handleParticipantsLayout() method ,logged in user is admin of group = "+mGroupDetails.getGroupName());
                       // Log.i(TAG,"removing item , position = "+position+" , count = "+mListParticipants.size());
                        DialogHelper.showCustomDialog(mContext, "Are you sure...", chat.getGroupMemberName() + " will be removed from group", "Remove", "Close", new DialogHelper.ShowDialogListener() {
                            @Override
                            public void onOkClicked() {
                                if (mPresenterGroupProfile != null)
                                    mPresenterGroupProfile.removeParticipant(mListParticipants.get(position), position);
                            }

                            @Override
                            public void onCancelClicked() {

                            }
                        });
                    }
                    else
                        Log.i(TAG,"login user is not admin");

                    return true;
                }
            });*/

    }



    private void handleHeaderLayout(HeaderViewHolder holder, int position)
    {
        Log.i(TAG,"handleHeaderLayout() method called ");

        // setting count of member
        holder.txtMemberCount.setText(totalParticipants+" Members");

        String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
        // checking whether login user is Group admin or not
        if (!loginUserId.equals(mChat.getGroupAdminId()))
        {
            Log.i(TAG,"handleHeaderLayout() method ,logged in user is not admin of group = "+mChat.getChattingToName());
            holder.layoutAddParticipants.setVisibility(View.GONE);
            return;
        }
        Log.i(TAG,"handleHeaderLayout() method ,logged in user is admin");
        holder.layoutAddParticipants.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view) {
               if (mContext != null)
               {
                   Intent intent = new Intent(mContext, ActivityChatAddParticipantToGroup.class);
                   intent.putExtra(ActivityChatAddParticipantToGroup.KEY_CHAT_DATA, mChat);
                   ActivityChatGroupProfile activityChatGroupProfile = (ActivityChatGroupProfile) mContext;
                   if (activityChatGroupProfile != null)
                   activityChatGroupProfile.startActivityForResult(intent, ActivityChatGroupProfile.REQUEST_CODE_ADD_PARTICIPANTS);
               }
           }
       });


    }



    private void handleFooter(FooterViewHolder holder, final int position)
    {
        Log.i(TAG,"handleFooter() method called , position = "+position);
        final String loginUserId = SessionHelper.getInstance(mContext).getUser().getUserId();
      //  final String loginUserName = SessionHelper.getInstance(mContext).getUser().getUserId();


        holder.layoutExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"removing item , position = "+position+" , count = "+mListParticipants.size());
                DialogHelper.showCustomDialog(mContext, "Are you sure...", " You will be removed from group", "Exit", "Close", new DialogHelper.ShowDialogListener() {
                    @Override
                    public void onOkClicked()
                    {
                        mChat.setUserId(loginUserId);  // setting user id of logged in user to exit group
                       /* if (mPresenterGroupProfile != null) {
                            Toast.makeText(mContext,"exiting...",Toast.LENGTH_SHORT).show();
                        }*/
                            mPresenterGroupProfile.exitGroup(mChat);
                    }

                    @Override
                    public void onCancelClicked() {

                    }
                });
            }
        });
    }
}