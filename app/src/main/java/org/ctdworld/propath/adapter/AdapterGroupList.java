package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityChat;
import org.ctdworld.propath.activity.ActivityContact;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.prefrence.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterGroupList extends RecyclerView.Adapter<AdapterGroupList.TextItemViewHolder>
{
    private final String TAG = AdapterGroupList.class.getSimpleName();
    private Context mContext;
    private static List<Chat> mListChat;
    private AdapterGroupList.OnOptionsMenuItemClickedListener mListener;



    public AdapterGroupList(Context context, OnOptionsMenuItemClickedListener listener)
    {
        Log.i(TAG,"AdapterGroupList() method called");
        mListChat = new ArrayList<>();
        this.mContext = context;
        mListener = listener;
    }

    public interface  OnOptionsMenuItemClickedListener{void onAdapterContactListOptionsMenuClicked(Chat chat);}




    // to filter list by name
    public void filterList(ArrayList<Chat> filterdNames) {
        mListChat = filterdNames;
        notifyDataSetChanged();
    }

    public void clearOldList()
    {
        mListChat.clear();
        notifyDataSetChanged();
    }



    public void onExitedFromGroup(Chat chat)
    {
        if (chat == null)
            return;

        int position = getPosition(chat.getChattingToId());
        if (position >-1)
        {
            try {
                mListChat.remove(position);
                notifyItemRemoved(position);

            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void updateGroupList(Chat chat)
    {
        Log.i(TAG,"updateGroupList() method called");
        if (chat == null)
            return;

        mListChat.add(chat);
        notifyItemInserted(getItemCount()-1);
    }



    @NonNull
    @Override
    public TextItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_group_list, parent, false);
        }
        catch (Exception e)
        {
            Log.e(TAG,"error in onBindViewHolder() , "+e.getMessage());
            e.printStackTrace();
        }

        return new TextItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextItemViewHolder holder, int position)
    {
       try {
           Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
           holder.layout.setAnimation(animation);



           final Chat chat = mListChat.get(position);
           Log.i(TAG,"group name = "+chat.getChattingToName());
           Log.i(TAG,"group pic = "+chat.getChattingToPicUrl());
           Log.i(TAG,"admin id = "+chat.getGroupAdminId());

           holder.txtName.setText(chat.getChattingToName());
           if (!chat.getGroupAdminId().equals(SessionHelper.getUserId(mContext)))
               holder.imgOptionsMenu.setVisibility(View.GONE);
           else
               holder.imgOptionsMenu.setVisibility(View.VISIBLE);

           int picWidthHeight = (int) mContext.getResources().getDimension(R.dimen.adapterUserPicSize);
           int widthHeight = UtilHelper.convertDpToPixel(mContext,picWidthHeight);

           Glide.with(mContext)
                   .load(chat.getChattingToPicUrl())
                   .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                   .apply(new RequestOptions().error(R.drawable.ic_profile))
                   .apply(new RequestOptions().override(widthHeight, widthHeight))
                   .apply(new RequestOptions().centerCrop())
                   .into(holder.imgGroupPic);


           holder.layoutGoTONextPage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Log.i(TAG,"group");
                   Intent intent = new Intent(mContext,ActivityChat.class);
                   //Chat chattingTo = new Chat();
                   chat.setGroupType(Chat.GROUP_TYPE_GROUP_CHAT);
                   chat.setMessageChatType(Chat.CHAT_TYPE_GROUP_CHAT);
                   intent.putExtra(ActivityChat.KEY_CHATTING_TO_DATA,chat);  //putting data of the person to whom logged in user is going to chat
                   ActivityContact activityContact = (ActivityContact)mContext;
                   if (activityContact != null)
                       activityContact.startActivityForResult(intent,ActivityContact.REQUEST_CODE_ACTIVITY_CHAT);
               }
           });


           holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (mListener != null)
                       mListener.onAdapterContactListOptionsMenuClicked(chat);
               }
           });
       }
       catch (Exception e)
       {
           Log.e(TAG,"error in onBindViewHolder() , "+e.getMessage());
           e.printStackTrace();
       }
    }

   /* @Override
    public long getItemId(int position) {
        return position;
    }
*/
    @Override
    public int getItemCount() {
        return mListChat.size();
    }





    // ViewHolder
    class TextItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtName;
        private ImageView imgGroupPic, imgOptionsMenu;
        View layoutGoTONextPage;
        View layout;

        TextItemViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            layoutGoTONextPage = itemView.findViewById(R.id.group_list_layout_go_to_next_page);
            txtName = itemView.findViewById(R.id.recycler_group_list_txt_group_name);
            imgGroupPic = itemView.findViewById(R.id.recycler_group_list_img_user_pic);
            imgOptionsMenu = itemView.findViewById(R.id.img_options_menu);

        }

    }


    private int getPosition(String groupId)
    {
        Log.i(TAG,"getPosition() method called, groupId = "+groupId+" , mListChat = "+mListChat);

        int position = -1;
        if (mListChat == null || groupId == null)
            return position;

        for(int i=0; i<mListChat.size(); i++)
        {
            Chat chat = mListChat.get(i);
            if (chat == null)
                return position;

            if (groupId.equals(chat.getChattingToId()))
            {
                position = i;
                break;
            }

        }

        return position;
    }


}
