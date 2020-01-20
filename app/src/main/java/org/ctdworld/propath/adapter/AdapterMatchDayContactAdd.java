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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapterMatchDayContactAdd extends RecyclerView.Adapter<AdapterMatchDayContactAdd.MyViewHolder>
{
    // # constants
    private final String TAG = AdapterMatchDayContactAdd.class.getSimpleName();

    // # variables
    private Context mContext;
    private OnUserClickedListener mUserClickedListener;


    // # custom listener
    public interface OnUserClickedListener{void onUserClicked(User user);}


    public AdapterMatchDayContactAdd(Context context, OnUserClickedListener listener) {
        this.mContext=context;
        mUserClickedListener = listener;
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

        holder.imgSendRequest.setVisibility(View.VISIBLE);
        holder.txtRequestStatus.setVisibility(View.GONE);
        holder.txtName.setText("Athlete");


    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return 5;
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
    private void sendRequest(final AdapterMatchDayContactAdd.MyViewHolder holder, String userId) {
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





}
