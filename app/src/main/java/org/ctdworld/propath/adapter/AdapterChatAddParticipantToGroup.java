package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Chat;
import org.ctdworld.propath.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatAddParticipantToGroup extends RecyclerView.Adapter<AdapterChatAddParticipantToGroup.MyViewHolder>
{
    private final String TAG = AdapterChatAddParticipantToGroup.class.getSimpleName();
    private Context mContext;
    private Chat mChat;
    private List<User> mListUsers;
  //  private List<User> mSelectedUsersList;
    SparseBooleanArray mSparseBooleanArray;

    public AdapterChatAddParticipantToGroup(Context context, List<User> usersList) {
        this.mContext = context;
        this.mListUsers = usersList;
     //   this.mSelectedUsersList = new ArrayList<>();
        mSparseBooleanArray = new SparseBooleanArray();
    }


    // to add new user list
    public void addUserList(List<User> userList)
    {
        mListUsers = userList;
        notifyDataSetChanged();
    }


    public void refreshUserList()
    {

        notifyDataSetChanged();

    }

    // to clear old data to refresh users list
    public void clearOldUserList()
    {
        if (mSparseBooleanArray != null)
            mSparseBooleanArray.clear();
        if (mListUsers != null)
            mListUsers.clear();

        notifyDataSetChanged();
    }


    // this method returns selected users list
    public List<User> getSelectedUsersList()
    {
       // return mSelectedUsersList;

        ArrayList<User> mTempArray = new ArrayList<>();

        for (int i = 0; i < mListUsers.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                Log.i(TAG,"selected user = "+mListUsers.get(i).getName());
                mTempArray.add(mListUsers.get(i));
            }
        }
        return mTempArray;
    }


  /*  // creating custom listener for selected users
    public interface OnSelectedUserReceived{void onReceivedSelectedUsers(List<User> userList);}*/


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_participant_to_group, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        User user = mListUsers.get(position);

        holder.txtUserName.setText(user.getName());

        // setting user pic
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.adapterUserPicSize);
        int picSize = UtilHelper.convertDpToPixel(mContext,picDimen);
        Glide.with(mContext)
                .load(user.getUserPicUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picSize, picSize))
                .apply(new RequestOptions().centerCrop())
                .into(holder.imgUserPic);


        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mSparseBooleanArray.get(position));

        // handling checkbox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                mSparseBooleanArray.put((Integer) compoundButton.getTag(), isChecked);
                /* if (status)
                {
                    Log.i(TAG, "adding user to checked list , position = "+position );
                   mSelectedUsersList.add(mListUsers.get(position));
                }
                else
                {
                    Log.i(TAG, "removing user to checked list , position = "+position );
                    mSelectedUsersList.remove(position);
                }*/
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mListUsers.size();
    }


    public void filterList(ArrayList<User> filterdNames) {
        this.mListUsers = filterdNames;
        notifyDataSetChanged();
    }



    // ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtUserName;
        private CircleImageView imgUserPic;
        private CheckBox checkBox;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.recycler_add_participant_to_group_txt_user_name);
            imgUserPic = itemView.findViewById(R.id.recycler_add_participant_to_group_img_user_pic);
            checkBox = itemView.findViewById(R.id.recycler_add_participant_to_group_check_box);

        }

    }




}
