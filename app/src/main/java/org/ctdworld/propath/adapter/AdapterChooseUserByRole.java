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
import org.ctdworld.propath.activity.ActivityChooseUserByRole;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterChooseUserByRole extends RecyclerView.Adapter<AdapterChooseUserByRole.MyViewHolder>
{
    private final String TAG = AdapterChooseUserByRole.class.getSimpleName();
    Context context;
    private List<User> mUserList = new ArrayList<>();

    public AdapterChooseUserByRole(Context context, List<User> items) {
        Log.i(TAG,"adapter constructor");
        this.mUserList = items;
        this.context=context;
    }


    public void addUserList(List<User> userList)
    {
        Log.i(TAG,"addContactList() method called , contactList size = "+userList.size());
        this.mUserList = userList;
    }


    // filtering contact list according to search in FragmentContact
    public void filterList(List<User> filterUsers) {
        this.mUserList = filterUsers;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_user_by_role, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        /*holder.bind(mContactList.get(position));*/
        final User user = mUserList.get(position);
        if (user != null)
        {
            int picDimen = UtilHelper.convertDpToPixel(context, (int) context.getResources().getDimension(R.dimen.adapterUserPicSize));
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
                   // intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE, ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                    context.startActivity(intent);
                }
            });

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    /* Intent intent = new Intent(context,ActivityChat.class);
                    intent.putExtra(ActivityChat.KEY_USER_DATA,user);
                    context.startActivity(intent);*/

                    ActivityChooseUserByRole activityChooseUserByRole = (ActivityChooseUserByRole) context;
                    if (activityChooseUserByRole != null)
                    {
                        // to send user to calling component
                        activityChooseUserByRole.onUserSelected(user);
                    }
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
        return mUserList.size();
    }






    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView txtUserName;
        private ImageView imgUserPic;
        View layout;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            txtUserName =  itemView.findViewById(R.id.recycler_user_list_by_role_txt_name);
            imgUserPic = itemView.findViewById(R.id.recycler_choose_user_by_role_img_user_pic);
        }

      //  public void bind(String text) {textView.setText(text); }


    }




}
