package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractChat;
import org.ctdworld.propath.activity.ActivityChatShowImageAndVideo;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.model.FullSizeImageVideo;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;

import java.util.ArrayList;
import java.util.List;


public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder>
{
    private static final String TAG = AdapterChat.class.getSimpleName();
    Context context;
    private List<Chat> mChatList;  // it will contain Chat
    private DateTimeHelper mDateTimeHelper;
 //   private User userOther; // this contains user whom logged in user is chatting to, to show user name and user pic
 //   private User userMyself; // this contains user who is logged in, , to show user name and user pic
    private ContractChat.Presenter mPresenterChat;
    private FragmentManager mFragmentManager;


    MyViewHolder mHolder;
    String mClickedItemId;

    public AdapterChat(Context context, ContractChat.Presenter presenterChat, FragmentManager fragmentManager)
    {
        this.context = context;
        this.mChatList = new ArrayList<>();
        this.mDateTimeHelper = new DateTimeHelper();
   //     this.userMyself = SessionHelper.getInstance(context).getUser();
   //     this.userOther = userOther;
        this.mPresenterChat = presenterChat;
        this.mFragmentManager = fragmentManager;
    }


    // updating chat in the list one by one
    public void addChatToList(Chat chat)
    {
        Log.i(TAG,"addChatToList method called, isSendingMessage = "+chat.isSendingMessage());
        if (mChatList != null)
        {
            mChatList.add(chat);
            notifyItemInserted(mChatList.size()-1);
        }
    }

    // this method updated a particular chat in list
    public void updateChat(Chat chat)
    {
        Log.i(TAG," updateChat method called, isSendingMessage = "+chat.isSendingMessage());
        if (mChatList != null && chat != null)
        {
            mChatList.set(chat.getPositionInAdapter(), chat);
            notifyItemChanged(chat.getPositionInAdapter());
        }
    }

    // this method removes a particular chat from list
    public void removeChatFromList(Chat chat)
    {
        Log.i(TAG,"removeChatFromList() method called");

        if (mChatList != null && chat != null)
        {
            Log.i(TAG,"removing chat , position = "+chat.getPositionInAdapter());
            mChatList.remove(chat.getPositionInAdapter());
            notifyItemRemoved(chat.getPositionInAdapter());
        }
        else
            Log.e(TAG,"removeChatFromList() method called mChatList or chat is null");
    }


    // updating chat item after file is uploaded successfully
    public void onFilesUploaded(Chat chat, int positionStart)
    {
        Log.i(TAG,"onFilesUploaded() method called");
        if (chat != null && mChatList != null)
        {
            Log.i(TAG,"onFilesUploaded() method , updating chat , position = "+positionStart);
            mChatList.set(positionStart, chat);
            notifyItemChanged(positionStart);
            Log.i(TAG,"onFilesUploaded() method, adapter position = "+positionStart+" image Url = "+chat.getMessage());
        }
        else
            Log.e(TAG,"mChatList or chat is null in onFileUploaded() method");
    }


    // showing previous messages
    public void showPreviousMessages(Chat chat)
    {
        if (mChatList != null)
        {
            mChatList.add(0,chat);
            notifyItemInserted(0);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_chat,null);

       /* RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT;
        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;

        view.setLayoutParams(layoutParams);*/

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        mHolder = holder;
        // from chat object only message, image, video andi date time being shown
        final Chat chat = mChatList.get(position);
        String myUserId = SessionHelper.getInstance(context).getUser().getUserId();
        showDate(position, holder.date, chat); // to show date for both sender and receiver


        // showing messages that were sent by me
        if (myUserId.equals(chat.getMessageFromUserId()))
        {
            Log.i(TAG,"showing my message");
            holder.layoutOtherMessage.setVisibility(View.GONE);  // the chat layout for other user is being put gone

            // checking if message is for group chat then user name will be shown otherwise user name will be hidden
            showOrHideUserName(holder.myUserName, chat);
            showTime(holder.myTime, DateTimeHelper.getDateTime(chat.getMessageDateTime(), DateTimeHelper.FORMAT_DATE_TIME)); // to show message time
            setProfilePic(myUserId, holder.myPic, chat.getMessageFromUserPicUrl()); // setting profile pic

            // checking message type if it's text message, image or video
            switch (chat.getMessageType())
            {
                case Chat.MESSAGE_TYPE_MESSAGE:
                   // Log.i(TAG,"showing my message***************************************************************************************************************");
                    showMessage(holder.myMessage, holder.myImage , holder.myProgressBar, chat);
                    break;

                case Chat.MESSAGE_TYPE_IMAGE:
                    showMessageImage(holder.myImage, holder.myMessage, holder.myProgressBar, chat);
                    break;

                case Chat.MESSAGE_TYPE_VIDEO:
                    showMessageVideo(holder.myImage, holder.myVideoIcon, holder.myMessage, holder.myProgressBar, chat);
                    break;
            }
        }


        // showing messages that were sent by other
        if (!myUserId.equals(chat.getMessageFromUserId()))
        {
            Log.i(TAG,"showing other message");

            holder.layoutMyMessage.setVisibility(View.GONE);  // the chat layout for my messages is being put gone
            showOrHideUserName(holder.otherUserName, chat);   // showing user name if chat type is group
            holder.otherDateTime.setText(DateTimeHelper.getDateTime(chat.getMessageDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
            setProfilePic(chat.getMessageFromUserId(), holder.otherPic, chat.getMessageFromUserPicUrl()); // setting profile pic


            // checking message type if it's text message, image or video
            switch (chat.getMessageType())
            {
                case Chat.MESSAGE_TYPE_MESSAGE:
                        showMessage(holder.otherMessage, holder.otherImage, holder.otherProgressBar, chat); // showing other's message
                    break;

                case Chat.MESSAGE_TYPE_IMAGE:
                        showMessageImage(holder.otherImage, holder.otherMessage, holder.otherProgressBar, chat); // showing other's message
                    break;

                case Chat.MESSAGE_TYPE_VIDEO:
                        showMessageVideo(holder.otherImage, holder.otherVideoIcon, holder.otherMessage, holder.otherProgressBar, chat); // showing other's message
                    break;
            }

        }

    }


    @Override
    public int getItemCount()
    {
        return mChatList.size();
    }




    @Override
    public int getItemViewType(int position) {
        return position;//super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;// super.getItemId(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView otherUserName, otherMessage, otherDateTime;
        TextView myUserName, myMessage, myTime;
        ImageView otherPic, myPic, otherImage, myImage, otherVideoIcon, myVideoIcon;
        View layoutMyMessage, layoutOtherMessage;
        ProgressBar otherProgressBar, myProgressBar;

        // date will be common for both sender and receiver
        TextView date;  // to show date on top

        public MyViewHolder(View view)
        {
            super(view);

            date = view.findViewById(R.id.chat_message_txt_date);

            otherUserName = view.findViewById(R.id.chat_message_received_txt_user_name);
            otherMessage = view.findViewById(R.id.chat_message_received_txt_message);
            otherDateTime = view.findViewById(R.id.chat_message_received_txt_time);
            otherPic = view.findViewById(R.id.chat_message_received_img_sender_pic);
            otherImage = view.findViewById(R.id.chat_message_received_img_image);
            otherVideoIcon = view.findViewById(R.id.chat_message_received_img_play_icon);
            otherProgressBar = view.findViewById(R.id.chat_message_received_progressbar_on_file);

            myUserName = view.findViewById(R.id.chat_message_sent_txt_user_name);
            myMessage = view.findViewById(R.id.chat_message_sent_txt_message);
            myTime = view.findViewById(R.id.chat_message_sent_txt_time);
            myPic = view.findViewById(R.id.chat_message_sent_img_sender_pic);
            myImage = view.findViewById(R.id.chat_message_sent_img_image);
            myVideoIcon = view.findViewById(R.id.chat_message_sent_img_play_icon);
            myProgressBar = view.findViewById(R.id.chat_message_sent_progressbar);

            layoutOtherMessage = view.findViewById(R.id.chat_layout_message_received);
            layoutMyMessage = view.findViewById(R.id.chat_layout_message_sent);
        }
    }



    private void setProfilePic(final String userId, ImageView imgPic, String url)
    {
        imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilePage(userId);
            }
        });
        int picDimen = (int) context.getResources().getDimension(R.dimen.imgSizeUserPicChat);
        int picSize = UtilHelper.convertDpToPixel(context,picDimen);

        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picSize,picSize))
                .into(imgPic);
    }

    // showing image
    private void setImageWithGlide(ImageView imageView, String url)
    {
       // Log.i(TAG,"loadImageWithGlide()method called, Url = "+url);
        int picVideoWidthDimen = (int) context.getResources().getDimension(R.dimen.chatImgVideoWidth);
        int picVideoHeightDimen = (int) context.getResources().getDimension(R.dimen.chatImgVideoHeight);
        int picVideoWidth = UtilHelper.convertDpToPixel(context,picVideoWidthDimen);
        int picVideoHeight = UtilHelper.convertDpToPixel(context,picVideoHeightDimen);

        if (imageView == null)
        {
            Log.e(TAG,"imageView is null in loadImageWithGlide() method");
            return;
        }

        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.img_default_black))
                .apply(new RequestOptions().error(R.drawable.img_default_black))
                .apply(new RequestOptions().override(picVideoWidth, picVideoHeight))
                .apply(new RequestOptions().centerCrop())
                .into(imageView);
    }



    //# showing date for both sender and receiver
    private void showDate(int position, TextView txtDate, Chat chat)
    {
        if (position == 0)
        {
         //   String date = DateTimeHelper.getDateTime(chat.getMessageDate(),DateTimeHelper.FORMAT_DATE_TIME);
            txtDate.setVisibility(View.VISIBLE);
            txtDate.setText(DateTimeHelper.getDate(chat.getMessageDateTime(),DateTimeHelper.FORMAT_DATE));
        }
        int previousIndex = position-1; // to compare with date at current position
        if (previousIndex >= 0)  // to make sure previous index is greater or equal to 0
        {
                long longDateTimeCurrentIndex = DateTimeHelper.getLongDateTime(chat.getMessageDateTime());
                long longDateTimePreviousIndex = DateTimeHelper.getLongDateTime(mChatList.get(previousIndex).getMessageDateTime());
            if ( longDateTimeCurrentIndex == longDateTimePreviousIndex)
                txtDate.setVisibility(View.GONE);
            else
            {
                if (chat.isSendingMessage())
                {
                    txtDate.setVisibility(View.VISIBLE);
                    txtDate.setText(DateTimeHelper.getDate(chat.getMessageDateTime(),DateTimeHelper.FORMAT_DATE));
                }
                else
                    txtDate.setVisibility(View.GONE);
            }
        }
    }


    // #to show or hide user name
    // #checking if message is for group chat then user name will be shown otherwise user name will be hidden
    private void showOrHideUserName(TextView textView, Chat chat)
    {
      //  Log.i(TAG,"showOrHideUserName() method called....... chat type = "+chat.getMessageChatType());
        if (Chat.CHAT_TYPE_GROUP_CHAT.equals(chat.getMessageChatType()))
        {
        //    Log.i(TAG,"showing user name.............");
            textView.setVisibility(View.VISIBLE);
            textView.setText(chat.getMessageFromUserName());
        }
        else
            textView.setVisibility(View.GONE); // hiding user name in chat
    }


    // to show time
    private void showTime(TextView txtTime, String time) {
        txtTime.setText(time);
    }


    // to show message
    private void showMessage(TextView txtMessage, ImageView image, ProgressBar progress, final Chat chat)
    {
        Log.i(TAG,"showMessage() method called is message sending = "+chat.isSendingMessage());
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.setText(chat.getMessage());
        image.setVisibility(View.GONE); // setting image gone

        if (chat.isSendingMessage())
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }

    // to show image or video
    private void showMessageImage(ImageView imageView, TextView txtMessage, ProgressBar progress, final Chat chat)
    {
      //  Log.i(TAG,"showing my image***************************************************************************************************************");
        Log.i(TAG,"showMessageImage() method called is message sending = "+chat.isSendingMessage());

        imageView.setVisibility(View.VISIBLE);  // setting visibility visible, by default it's gone
        setImageWithGlide(imageView, chat.getMessage());
        txtMessage.setVisibility(View.GONE);
        if (chat.isSendingMessage())
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
            //  Log.i(TAG,"file not uploading****************************************************");
        //setting listener to open image in next activity
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // FragmentFullImage fragmentFullImage = FragmentFullImage.getInstance(getImageVideoList(), mHolder.getAdapterPosition());
              // fragmentFullImage.show(mFragmentManager, ConstHelper.Tag.Fragment.FULL_IMAGE);

                mClickedItemId = chat.getId();
                showFullSizeImageVideo();

                Log.i(TAG,"mClickedItemId = "+mClickedItemId);



                /* Bundle bundle = new Bundle();
                bundle.putString(ActivityChatShowImageAndVideo.KEY_TYPE_IMAGE_OR_VIDEO, Chat.MESSAGE_TYPE_IMAGE);
                bundle.putString(ActivityChatShowImageAndVideo.KEY_URL, chat.getMessage());
                bundle.putString(ActivityChatShowImageAndVideo.KEY_TOOLBAR_TITLE,chat.getMessageFromUserName());
                Intent intent = new Intent(context,ActivityChatShowImageAndVideo.class);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
            }
        });
    }


    // to show video
    private void showMessageVideo(ImageView imageView, ImageView imgVideoIcon, TextView txtMessage, ProgressBar progress, final Chat chat)
    {
        Log.i(TAG,"showMessageVideo() method called is message sending = "+chat.isSendingMessage());
        //  Log.i(TAG,"showing my video***************************************************************************************************************");
        imageView.setVisibility(View.VISIBLE);
        imgVideoIcon.setVisibility(View.VISIBLE);  // showing video icon on image
        setImageWithGlide(imageView, chat.getMessage());
        txtMessage.setVisibility(View.GONE);  // hiding text message because here we are show ImageView to show video
        // if
        if (chat.isSendingMessage())
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClickedItemId = chat.getId();
                showFullSizeImageVideo();

                Log.i(TAG,"mClickedItemId = "+mClickedItemId);

               /* Bundle bundle = new Bundle();
                bundle.putString(ActivityChatShowImageAndVideo.KEY_TYPE_IMAGE_OR_VIDEO, Chat.MESSAGE_TYPE_VIDEO);
                bundle.putString(ActivityChatShowImageAndVideo.KEY_URL, chat.getMessage());
                bundle.putString(ActivityChatShowImageAndVideo.KEY_TOOLBAR_TITLE,chat.getMessageFromUserName());

                Intent intent = new Intent(context,ActivityChatShowImageAndVideo.class);
                intent.putExtras(bundle);
                context.startActivity(intent);*/
            }
        });
    }





    // returns list which contains url of images and videos
    private void showFullSizeImageVideo()
    {
        ArrayList<FullSizeImageVideo> list = new ArrayList<>();
        int selectedImagePosition = 0;  // to show selected item in adapter
        int increment = 0;
        for (Chat chat : mChatList)
        {

            if (chat.getMessageType().equals(Chat.MESSAGE_TYPE_IMAGE))
            {
                if (mClickedItemId.equals(chat.getId()))
                    selectedImagePosition = increment;
                increment++;

                list.add(new FullSizeImageVideo(chat.getMessage(), FullSizeImageVideo.TYPE_IMAGE));

            }
            else if (chat.getMessageType().equals(Chat.MESSAGE_TYPE_VIDEO))
            {
                if (mClickedItemId.equals(chat.getId()))
                    selectedImagePosition = increment;
                increment++;

                list.add(new FullSizeImageVideo(chat.getMessage(), FullSizeImageVideo.TYPE_VIDEO));

            }

        }

        Log.i(TAG,"selected item position = "+selectedImagePosition);
        Log.i(TAG,"increment = "+increment);
        Bundle bundle = new Bundle();
        bundle.putInt(ActivityChatShowImageAndVideo.KEY_SELECTED_IMAGE_POSITION, selectedImagePosition);
        bundle.putParcelableArrayList(ActivityChatShowImageAndVideo.KEY_URL_LIST, list);
        //   bundle.putString(ActivityChatShowImageAndVideo.KEY_TOOLBAR_TITLE,chat.getMessageFromUserName());
        Intent intent = new Intent(context,ActivityChatShowImageAndVideo.class);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    private void openProfilePage(String userId)
    {
        Intent intent = new Intent(context, ActivityProfileView.class);
        intent.putExtra(ConstHelper.Key.ID, userId);
       // intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE, ActivityProfileView.VALUE_PROFILE_TYPE_SELF)
        context.startActivity(intent);
    }

}
