package org.ctdworld.propath.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.database.RemoteServer;
import org.ctdworld.propath.helper.DateTimeHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder>
{
    private static final String TAG = AdapterNotification.class.getSimpleName();
    private Context mContext;
    private List<Request.Data> mRequestStatusList;

    public AdapterNotification(Context context) {
        this.mContext = context;
        mRequestStatusList = new ArrayList<>();
    }


    // adding request status list
    public  void addRequestDataList(List<Request.Data> requestDataList)
    {
        this.mRequestStatusList = requestDataList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recycler_notification,null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        view.setLayoutParams(params);

        return new MyViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        Request.Data requestData = mRequestStatusList.get(position);
        //hiding done icon, by default it's hidden, but when response to request is successful then done icon is made visible.
        holder.imgDone.setVisibility(View.GONE);
        setUserPic(holder, requestData);
        setViews(holder, requestData);
        //respondToConnectionRequest(holder, requestData);
        setRequestStatusWithButtons(holder, requestData);

    }

    private void setRequestStatusWithButtons(MyViewHolder holder, Request.Data requestData) {

        Log.i(TAG,"request status before= "+requestData.getRequestStatus());

        Log.i(TAG,"request status after= "+requestData.getRequestStatus());


        switch (requestData.getRequestStatus())
        {
            case Request.REQUEST_STATUS_PENDING:
                /*if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_FRIEND))
                    holder.txtMessage.setText(String.format("Friend request sent by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_EDUCATION))
                    holder.txtMessage.setText(String.format("Progress Report request sent by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_COACH_FEEDBACK))
                    holder.txtMessage.setText(String.format("Coach Feedback request sent by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equals(Request.REQUEST_FOR_CAREER_PLAN_VIEW))
                    holder.txtMessage.setText(String.format("View career plan request sent by %s", requestData.getRequestFromUserName()));*/

                holder.layoutButtons.setVisibility(View.VISIBLE);
                respondToConnectionRequest(holder, requestData);

                break;
            case Request.REQUEST_STATUS_ACCEPT:
                /*if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_FRIEND))
                    holder.txtMessage.setText(String.format("Friend request accepted by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_EDUCATION))
                    holder.txtMessage.setText(String.format("Progress Report request accepted by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_COACH_FEEDBACK))
                    holder.txtMessage.setText(String.format("Coach Feedback request accepted by %s", requestData.getRequestFromUserName()));
                else if (requestData.getRequestFor().equals(Request.REQUEST_FOR_CAREER_PLAN_VIEW))
                    holder.txtMessage.setText(String.format("View career plan request accepted by %s", requestData.getRequestFromUserName()));*/

                holder.layoutButtons.setVisibility(View.GONE);

                break;

            case Request.REQUEST_STATUS_REJECT:
                /*if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_FRIEND))
                    holder.txtMessage.setText("Friend request rejected by "+ requestData.getRequestFromUserName());
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_EDUCATION))
                    holder.txtMessage.setText("Progress Report request rejected by "+ requestData.getRequestFromUserName());
                else if (requestData.getRequestFor().equalsIgnoreCase(Request.REQUEST_FOR_COACH_FEEDBACK))
                    holder.txtMessage.setText("Coach Feedback request rejected by "+ requestData.getRequestFromUserName());
                else if (requestData.getRequestFor().equals(Request.REQUEST_FOR_CAREER_PLAN_VIEW))
                    holder.txtMessage.setText(String.format("View career plan request rejected by %s", requestData.getRequestFromUserName()));*/

                holder.layoutButtons.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public int getItemCount()
    {
        return mRequestStatusList.size();
    }


    @Override
    public long getItemId(int position)
    {
        return super.getItemId(position);
    }



    // Holder
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgUserPic, imgDone;
        TextView txtUserName, txtMessage, txtTime;
        Button btnAccept, btnReject;
        ProgressBar progressBar;
        View layoutButtons, layout;

        public MyViewHolder(View view)
        {
            super(view);
            layout = view;
            imgUserPic = view.findViewById(R.id.connection_request_status_img_user_pic);
            txtUserName = view.findViewById(R.id.connection_request_status_user_name);
            txtMessage = view.findViewById(R.id.connection_request_status_txt_message);
            txtTime = view.findViewById(R.id.connection_request_status_txt_time);
            btnAccept = view.findViewById(R.id.connection_request_status_btn_accept);
            btnReject = view.findViewById(R.id.connection_request_status_btn_reject);
            layoutButtons = view.findViewById(R.id.connection_request_status_buttons_layout);
            imgDone = view.findViewById(R.id.connection_request_status_img_done);
            progressBar = view.findViewById(R.id.connection_request_status_prgressbar);
// layoutRequest = view.findViewById(R.id.notification_request_layout);
        }
    }





    // responding to connection request
    private void respondToConnectionRequest(final AdapterNotification.MyViewHolder holder, final Request.Data requestData)
    {
      //  final Request.Data respondRequestData = new Request.Data();
    //    respondRequestData.setRequestToUserId(requestData.getRequestToUserId()); // setToId is id of user who who has sent request
    //    respondRequestData.setRequestFromUserId(SessionHelper.getInstance(mContext).getUser().getUserId()); // setting user id of request receiver
    //    respondRequestData.setRequestFor(requestData.getRequestFor());


        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData.setRequestStatus(Request.REQUEST_STATUS_ACCEPT); // setting response status, 1 means request accepted
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.layoutButtons.setVisibility(View.GONE);
                respond(holder, requestData);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData.setRequestStatus(Request.REQUEST_STATUS_REJECT); // setting response status, 2 means request rejected
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.layoutButtons.setVisibility(View.GONE);
                respond(holder, requestData);
            }
        });
    }



    // responding to request
    private void respond(final AdapterNotification.MyViewHolder holder, Request.Data respondRequestData) {
        //  holder.layoutButtons.setVisibility(View.GONE);
        ContractRequest.Presenter mPresenterRequest = new PresenterRequest(mContext, new ContractRequest.View() {
            @Override
            public void onRequestSentSuccessfully() {

            }

            @Override
            public void onReceivedAllRequests(List<Request.Data> requestDataList) {

            }

            @Override
            public void onRespondedSuccessfully() {
                Log.i(TAG, "responding to connection request successful");
                holder.layoutButtons.setVisibility(View.GONE);
                holder.imgDone.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String message) {
                Log.i(TAG, "responding to connection request failed");
                //  holder.layoutButtons.setVisibility(View.GONE);
                holder.layoutButtons.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onShowMessage(String message) {

            }
        });



        mPresenterRequest.respondToRequest(respondRequestData);

    }




    // profile pic
    private void setUserPic(MyViewHolder holder, Request.Data requestData) {
        int picSizeDp = (int) mContext.getResources().getDimension(R.dimen.adapterUserPicSize);
        int picDimen = UtilHelper.convertDpToPixel(mContext,picSizeDp);

        String picUrl = RemoteServer.BASE_IMAGE_URL+requestData.getRequestFromProfileImage();
        Glide.with(mContext)
                .load(picUrl)
                .apply(new RequestOptions().error(R.drawable.ic_profile))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile))
                .apply(new RequestOptions().override(picDimen,picDimen))
                .into(holder.imgUserPic);
    }


    // setting data on views
    private void setViews(MyViewHolder holder, Request.Data requestData) {

        /*if (requestData.getRequestFromUserName() == null || requestData.getRequestFromUserName().isEmpty())
            holder.txtUserName.setVisibility(View.GONE);
        else*/
            holder.txtUserName.setText(requestData.getRequestFromUserName());  // setting name who responded to request


       /* if (requestData.getRequestMessage() == null || requestData.getRequestMessage().isEmpty())
            holder.txtMessage.setVisibility(View.GONE);
        else*/
            holder.txtMessage.setText(requestData.getRequestMessage());


       /* if (requestData.getRequestDateTime() == null || requestData.getRequestDateTime().isEmpty())
            holder.txtTime.setVisibility(View.GONE);
        else*/
            holder.txtTime.setText(DateTimeHelper.getDateTime(requestData.getRequestDateTime(), DateTimeHelper.FORMAT_DATE_TIME));
    }


}
