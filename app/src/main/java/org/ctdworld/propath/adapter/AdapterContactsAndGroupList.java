package org.ctdworld.propath.adapter;

import android.content.Context;
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
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.ContactAndGroup;

import java.util.List;

public class AdapterContactsAndGroupList extends RecyclerView.Adapter<AdapterContactsAndGroupList.MyViewHolder>
{
    private final String TAG = AdapterContactsAndGroupList.class.getSimpleName();
    private Context context;
    private List<ContactAndGroup> mListUser;
    private AdapterContactsAndGroupListListener mListener;

    public AdapterContactsAndGroupList(Context context, List<ContactAndGroup> items, AdapterContactsAndGroupListListener listener)
    {
        this.mListUser = items;
        this.context=context;
        this.mListener = listener;
    }


   public interface AdapterContactsAndGroupListListener{void onSelected(ContactAndGroup contactAndGroup);}


    public void addUserList(List<ContactAndGroup> contactList)
    {
        Log.i(TAG,"addContactList() method called , contactList size = "+contactList.size());
        this.mListUser = contactList;
       // notifyDataSetChanged();  // list will appear only when user types letter in search box
    }


    public void filterList(List<ContactAndGroup> contactList)
    {
        Log.i(TAG,"addContactList() method called , contactList size = "+contactList.size());
        this.mListUser = contactList;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_and_groups_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        /*holder.bind(mContactList.get(position));*/
        final ContactAndGroup contactAndGroup = mListUser.get(position);
        if (contactAndGroup != null)
        {
            int picDimen = UtilHelper.convertDpToPixel(context, (int) context.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize));
            holder.txtUserName.setText(contactAndGroup.getName());
            Glide.with(context)
                    .load(contactAndGroup.getPicUrl())
                    .apply(new RequestOptions().error(R.drawable.ic_profile))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                    .apply(new RequestOptions().override(picDimen,picDimen))
                    .into(holder.imgUserPic);

         /*   holder.imgUserPic.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ActivityProfileView.class);
                    intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                    intent.putExtra(ConstHelper.Key.ID, user.getUserId());
                    context.startActivity(intent);
                }
            });*/

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (mListener != null)
                        mListener.onSelected(contactAndGroup);
                    else
                        Log.e(TAG,"mListener is null");
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
        return mListUser.size();
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
            txtUserName =  itemView.findViewById(R.id.list_item);
            imgUserPic = itemView.findViewById(R.id.recycler_contact_img_user_pic);
        }

    }




}
