package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityChat;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AdapterChatUserGroupList extends RecyclerView.Adapter<AdapterChatUserGroupList.MyViewHolder>
{
    private final String TAG = AdapterChatUserGroupList.class.getSimpleName();
    private Context mContext;
    private List<Chat> mUserList;
    private AdapterChatUserGroupList.MyViewHolder mHolder;

    public AdapterChatUserGroupList(Context context, List<Chat> userList) {
        this.mUserList = userList;
        this.mContext = context;
    }


    public void clearOldList()
    {
        mUserList.clear();
        notifyDataSetChanged();
    }

   public void updateChatUserList(Chat chat)
    {
        this.mUserList.add(0,chat);

        //sort the list according to date
        Collections.sort(mUserList, byDate);


//        notifyItemInserted(0);
    }

    public void filterList(ArrayList<Chat> filteredNames)
    {
        this.mUserList = filteredNames;
        notifyDataSetChanged();
    }

    static final Comparator<Chat> byDate = new Comparator<Chat>() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        public int compare(Chat ord1, Chat ord2) {
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = sdf.parse(ord1.getMessageDateTime());
                d2 = sdf.parse(ord2.getMessageDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (d1.getTime() > d2.getTime()) {
                return -1;
            } else if (d1.getTime() < d2.getTime()) {
                return 1;
            } else {
                return 1;
            }
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_messages, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        mHolder = holder;
        final Chat chat = mUserList.get(position);

        holder.txtName.setText(chat.getChattingToName());
        setMessage(chat);
        setDateTime(chat);
        setUserPic(chat);
        setRowClickListener(chat);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }



    public class MyViewHolder  extends RecyclerView.ViewHolder
    {
        private TextView txtName, txtTime, txtCount, txtMessage;
        ImageView image;
        private ImageView imgUserPic;
        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.recycler_chat_users_txt_user_name);
            imgUserPic = itemView.findViewById(R.id.recycler_chat_users_img_user_pic);
            txtTime = itemView.findViewById(R.id.recycler_chat_users_txt_time);
            txtCount = itemView.findViewById(R.id.recycler_chat_users_txt_message_count);
            txtMessage = itemView.findViewById(R.id.recycler_chat_users_txt_message);
            image = itemView.findViewById(R.id.recycler_chat_users_img);
            layout  = itemView;/*= itemView.findViewById(R.id.recycler_chat_users_layout);*/
        }

    }



    // # setting date time
    private void setDateTime(Chat chat) {
        //showing last message time
        //String date = DateTimeHelper.getDateTime(chat.getMessageDateTime(), DateTimeHelper.FORMAT_DATE);
        //String time = DateTimeHelper.getDateTime(chat.getMessageDateTime(), DateTimeHelper.FORMAT_TIME);
        //mHolder.txtTime.setText(String.format("%s\n%s", date, time));
        String dateTime = DateTimeHelper.getDateTime(chat.getMessageDateTime(), DateTimeHelper.FORMAT_DATE_TIME);
        mHolder.txtTime.setText(dateTime);
    }

    private void setMessage(Chat chat)
    {
        if (chat.getMessage() == null || chat.getMessage().isEmpty())
        {
            mHolder.txtMessage.setVisibility(View.GONE);
            mHolder.image.setVisibility(View.GONE);
        }
        else
        {
            if (chat.getMessageType() != null && chat.getMessageType().contains(Chat.MESSAGE_TYPE_IMAGE))
            {
                mHolder.image.setImageResource(R.drawable.ic_image);
                mHolder.txtMessage.setText(mContext.getString(R.string.image));
            }
            else if (chat.getMessageType() != null && chat.getMessageType().contains(Chat.MESSAGE_TYPE_VIDEO))
            {
                mHolder.image.setImageResource(R.drawable.ic_video_play);
                mHolder.txtMessage.setText(mContext.getString(R.string.video));
            }
            else
            {
                mHolder.image.setVisibility(View.GONE);
                mHolder.txtMessage.setText(chat.getMessage());
            }

            // if message count is 0 then hiding txtCount else showing count on txtCount
            if (chat.getMessageCount() != null && Integer.parseInt(chat.getMessageCount())  == 0 )
                mHolder.txtCount.setVisibility(View.GONE);
            else
                mHolder.txtCount.setText(chat.getMessageCount());

        }
    }


    private void setRowClickListener(final Chat chat) {

        mHolder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Chat.CHAT_TYPE_ONE_TO_ONE.contains(chat.getMessageChatType()))
                {
                    Log.i(TAG,"inside one to one");
                    Intent intent = new Intent(mContext, ActivityChat.class);
                    intent.putExtra(ActivityChat.KEY_CHATTING_TO_DATA,chat);  //putting data of the person to whom logged in user is going to chat
                    mContext.startActivity(intent);
                }
                else if (Chat.CHAT_TYPE_GROUP_CHAT.contains(chat.getMessageChatType()))
                {
                    Log.i(TAG,"inside group chat");

                    Intent intent = new Intent(mContext,ActivityChat.class);
                    intent.putExtra(ActivityChat.KEY_CHATTING_TO_DATA,chat);  //putting data of the person to whom logged in user is going to chat
                    mContext.startActivity(intent);
                }

            }
        });
    }

    private void setUserPic(Chat chat) {

        //getting dimension  of user pic to get same size icon
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);

        // showing user pic
        Glide.with(mContext)
                .load(chat.getChattingToPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth))
                .into(mHolder.imgUserPic);

    }

}
