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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

public class AdapterContactAdd extends RecyclerView.Adapter<AdapterContactAdd.MyViewHolder>
{
    // # constants
    private final String TAG = AdapterContactAdd.class.getSimpleName();

    // # variables
    private Context mContext;
    private List<User> mUserList;
    private OnUserClickedListener mUserClickedListener;


    // # custom listener
    public interface OnUserClickedListener{void onUserClicked(User user);}


    public AdapterContactAdd(Context context, List<User> userList, OnUserClickedListener listener) {
        this.mUserList = userList;
        this.mContext=context;
        mUserClickedListener = listener;
    }



    public void updateUser(String userId, String friendStatus)
    {
        if (userId == null || friendStatus == null)
            return;

        int pos = getPosition(userId);

        if (pos == ConstHelper.NOT_FOUND)
            return;

        mUserList.set(pos, mUserList.get(pos));
        notifyItemChanged(pos, mUserList.get(pos));
    }


    public void addUserList(List<User> users)
    {
        this.mUserList = users;
        mUserList.addAll(users);
        //mUserList.add(users);
        //notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    public void filterList(ArrayList<User> filterdNames) {
        this.mUserList = filterdNames;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        final User user = mUserList.get(position);
        // making send request image disabled, it connection request is not pending or accepted then below it will be made clickable
        // showing connection request status
        switch (user.getConnectionRequestStatus())
        {

            case Request.REQUEST_STATUS_PENDING:
                Log.i(TAG,"request status = "+"pending , "+" code = "+user.getConnectionRequestStatus());
                holder.txtRequestStatus.setVisibility(View.VISIBLE);
                holder.imgSendRequest.setVisibility(View.GONE);

                break;

            case Request.REQUEST_STATUS_ACCEPT:
                Log.i(TAG,"request status = "+"accepted , "+" code = "+user.getConnectionRequestStatus());
                holder.imgSendRequest.setVisibility(View.VISIBLE);  // making visible to show done icon
                holder.imgSendRequest.setImageResource(R.drawable.ic_done);
                holder.txtRequestStatus.setVisibility(View.GONE);

                // disabling buttons to send
                break;

            default:
                Log.i(TAG,"request status = "+"send request , "+" code = "+user.getConnectionRequestStatus());
                holder.imgSendRequest.setVisibility(View.VISIBLE);
                holder.imgSendRequest.setImageResource(R.drawable.ic_add_contact);
                holder.txtRequestStatus.setVisibility(View.GONE);

        }

        holder.txtName.setText(user.getName());
        //getting dimension  of user pic to get same size icon
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);


      // showing user pic
        Glide.with(mContext)
                .load(user.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth))
                .into(holder.imgUserPic);


        // sending connection request
        holder.imgSendRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // user can send connection request if request has been rejected or never sent
                if (user.getConnectionRequestStatus().equals(Request.REQUEST_STATUS_REJECT))
                    sendRequest(holder,user.getUserId());
            }
        });

        holder.imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
                // putting profile type other, to show profile accordingly
               // intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                // putting user id to get profile
                intent.putExtra(ConstHelper.Key.ID,user.getUserId());
                mContext.startActivity(intent);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserClickedListener != null)
                    mUserClickedListener.onUserClicked(user);
            }
        });
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return mUserList.size();
    }



    // ViewHolder class
    public class MyViewHolder  extends RecyclerView.ViewHolder
    {
        private TextView txtName, txtRequestStatus;
        private ImageView imgUserPic, imgSendRequest;
        private ProgressBar progressBar;
        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
         //   txtEmail = itemView.findViewById(R.id.recycler_contact_search_txt_email);
            imgUserPic = itemView.findViewById(R.id.img_user);
            imgSendRequest = itemView.findViewById(R.id.recycler_contact_search_img_send_request);
            progressBar = itemView.findViewById(R.id.recycler_contact_search_progress_bar);
            txtRequestStatus = itemView.findViewById(R.id.recycler_contact_search_txt_status);
            layout = itemView.findViewById(R.id.recycler_contact_search_layout);

        }


    }





    // sending connection request
    private void sendRequest(final AdapterContactAdd.MyViewHolder holder, String userId) {
        {
            holder.progressBar.setVisibility(View.VISIBLE);
            ContractRequest.Presenter presenter = new PresenterRequest(mContext, new ContractRequest.View() {
                @Override
                public void onRequestSentSuccessfully() {
                    Log.i(TAG, "onSuccess");
                    // showing send icon
                    holder.imgSendRequest.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.txtRequestStatus.setVisibility(View.VISIBLE);
                    holder.txtRequestStatus.setText(mContext.getString(R.string.pending));
                }

                @Override
                public void onReceivedAllRequests(List<Request.Data> requestList) {

                }

                @Override
                public void onRespondedSuccessfully() {

                }

                @Override
                public void onFailed(String message) {
                    Log.i(TAG, "onFailed");

                    holder.imgSendRequest.setImageResource(R.drawable.ic_add_contact);
                    holder.txtRequestStatus.setVisibility(View.GONE);
                    holder.progressBar.setVisibility(View.GONE);
                    DialogHelper.showSimpleCustomDialog(mContext, "Failed...");
                }


                @Override
                public void onShowMessage(String message) {
                    Log.i(TAG, "onShowMessage , message = " + message);

                    if ("All ready Added".equals(message)) {
                        holder.imgSendRequest.setVisibility(View.VISIBLE);
                        holder.imgSendRequest.setImageResource(R.drawable.ic_done);
                        DialogHelper.showSimpleCustomDialog(mContext, "Request Already Sent...");
                    } else {
                        holder.imgSendRequest.setVisibility(View.VISIBLE);
                        holder.imgSendRequest.setImageResource(R.drawable.ic_add_contact);
                        DialogHelper.showSimpleCustomDialog(mContext, message);
                    }
                }
            });

            if (UtilHelper.isConnectedToInternet(mContext)) {
                holder.imgSendRequest.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                presenter.sendRequest(userId, Request.REQUEST_FOR_FRIEND);
            } else
                DialogHelper.showSimpleCustomDialog(mContext, "No Connection...");
        }

    }




    private int getPosition(String userId)
    {
        for (int i=0; i<mUserList.size(); i++)
        {
            User user = mUserList.get(i);
            if (userId.equals(user.getUserId()))
                return i;
        }

        return ConstHelper.NOT_FOUND;
    }

}
