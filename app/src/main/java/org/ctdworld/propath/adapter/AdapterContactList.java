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
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.MyViewHolder>
{
    private final String TAG = AdapterContactList.class.getSimpleName();
    private Context context;
    private List<User> mContactList;
    private OnOptionsMenuClickedListener mListener;


    public AdapterContactList(Context context,List<User> items, OnOptionsMenuClickedListener listener) {
        this.mContactList = items;
        this.context=context;
        mListener = listener;
    }


    public interface  OnOptionsMenuClickedListener{void onAdapterOptionsMenuClicked(User user);}

    public void addContactList(List<User> contactList)
    {
        Log.i(TAG,"addContactList() method called , contactList size = "+contactList.size());
        this.mContactList = contactList;
       // notifyDataSetChanged();
    }

    // removing contact from list after deleting contact
    public void onContactDeleted(String userId)
    {
        if (userId == null)
            return;

        int position = getPositionInList(userId);
        if (position == ConstHelper.NOT_FOUND)
            return;

        try {
            mContactList.remove(position);
            notifyItemRemoved(position);
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final User user = mContactList.get(holder.getAdapterPosition());
        if (user != null)
        {
            int picDimen = UtilHelper.convertDpToPixel(context, (int) context.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
            holder.txtUserName.setText(user.getName());
            Glide.with(context)
                    .load(user.getUserPicUrl())
                    .apply(new RequestOptions().error(R.drawable.ic_profile))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                    .apply(new RequestOptions().override(picDimen,picDimen))
                    .into(holder.imgUserPic);

            holder.imgUserPic.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityProfileView.class);
                   // intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                    intent.putExtra(ConstHelper.Key.ID, user.getUserId());
                    context.startActivity(intent);
                }
            });

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context,ActivityChat.class);
                    Chat chattingTo = new Chat();
                    chattingTo.setChattingToId(user.getUserId());
                    chattingTo.setChattingToName(user.getName());
                    chattingTo.setChattingToPicUrl(user.getUserPicUrl());
                    chattingTo.setMessageChatType(Chat.CHAT_TYPE_ONE_TO_ONE);
                    intent.putExtra(ActivityChat.KEY_CHATTING_TO_DATA,chattingTo);  //putting data of the person to whom logged in user is going to chat
                    context.startActivity(intent);
                }
            });

            holder.imgOptionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onAdapterOptionsMenuClicked(user);
                }
            });

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }


    // filtering contact list according to search in FragmentContact
    public void filterList(ArrayList<User> filterdNames) {
        this.mContactList = filterdNames;
        notifyDataSetChanged();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView txtUserName;
        private ImageView imgUserPic, imgOptionsMenu;
        View layout;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            txtUserName =  itemView.findViewById(R.id.list_item);
            imgUserPic = itemView.findViewById(R.id.recycler_contact_img_user_pic);
            imgOptionsMenu = itemView.findViewById(R.id.img_options_menu);
        }

      //  public void bind(String text) {textView.setText(text); }


    }


    // returns user position in list
    private int getPositionInList(String userId)
    {
        int position = ConstHelper.NOT_FOUND;


        if (userId == null || userId.isEmpty())
            return position;

        for (int i=0; i<mContactList.size(); i++)
        {
            String fetchedUserId = mContactList.get(i).getUserId();
            if (fetchedUserId != null && fetchedUserId.equals(userId))
            {
                position = i;
                break;
            }

        }

        return position;
    }


}
