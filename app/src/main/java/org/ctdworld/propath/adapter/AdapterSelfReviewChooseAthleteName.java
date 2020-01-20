package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewReport;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.activity.ActivitySelfReviewListForOtherRoles;
import org.ctdworld.propath.contract.ContractSelfReviewedAthletes;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

public class AdapterSelfReviewChooseAthleteName extends RecyclerView.Adapter<AdapterSelfReviewChooseAthleteName.MyViewHolder>
{
    private final String TAG = AdapterSelfReviewChooseAthleteName.class.getSimpleName();
    private Context mContext;
    private List<GetAthletes> mAthleteList;
    private ContractSelfReviewedAthletes.Presenter mPresenterGetReviewAthletes;


    public AdapterSelfReviewChooseAthleteName(Context context, List<GetAthletes> userList, ContractSelfReviewedAthletes.Presenter presenter)
    {
        this.mAthleteList = userList;
        this.mContext = context;
        this.mPresenterGetReviewAthletes = presenter;

    }

    public void addAthleteList(List<GetAthletes> athletes)
    {
        this.mAthleteList = athletes;
        notifyDataSetChanged();   // # list will be shown only when letters are typed in search box
    }

    public void filterList(ArrayList<GetAthletes> filterdNames)
    {
        this.mAthleteList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public AdapterSelfReviewChooseAthleteName.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_athlete_name, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterSelfReviewChooseAthleteName.MyViewHolder holder, int position) {

        final GetAthletes user = mAthleteList.get(position);


        holder.txtName.setText(user.getName());
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext, picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext, picDimen);

        Glide.with(mContext)
                .load(user.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth, picHeight))
                .into(holder.imgUserPic);

        holder.imgUserPic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
                //  intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE, ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                intent.putExtra(ConstHelper.Key.ID, user.getUserId());
                mContext.startActivity(intent);
            }
        });


        switch (user.getRequestStatus())
        {
            case Request.REQUEST_STATUS_ACCEPT:

                holder.img_send_request.setVisibility(View.VISIBLE);  // making visible to show done icon
                holder.img_send_request.setImageResource(R.drawable.ic_done);
                holder.txtRequestStatus.setVisibility(View.GONE);

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mContext, ActivitySelfReviewListForOtherRoles.class);
                        intent.putExtra(ActivityEducationSelfReviewReport.ATHLETE_ID,user.getUserId());
                        mContext.startActivity(intent);

                    }
                });

                break;

            case Request.REQUEST_STATUS_PENDING:
                holder.txtRequestStatus.setVisibility(View.VISIBLE);
                holder.img_send_request.setVisibility(View.GONE);
                break;

            default:
                Log.i(TAG, "request status = " + "send request , " + " code = " + user.getRequestStatus());
                holder.img_send_request.setVisibility(View.VISIBLE);
                holder.img_send_request.setImageResource(R.drawable.ic_add_contact);
                holder.txtRequestStatus.setVisibility(View.GONE);
        }


        // sending connection request
        holder.img_send_request.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // user can send connection request if request has been rejected or never sent
                if (user.getRequestStatus().equals(Request.REQUEST_STATUS_REJECT))
                {
                    String messagePermission = "Without permission you can not see athlete self review";

                    DialogHelper.showCustomDialog(mContext, "Permission Required", messagePermission, "Send Request", "Cancel", new DialogHelper.ShowDialogListener() {
                        @Override
                        public void onOkClicked() {
                            sendRequest(holder,user.getUserId());
                        }

                        @Override
                        public void onCancelClicked() {

                        }
                    });
                }
            }
        });


    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return mAthleteList.size();
    }



    // ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtName,txtRequestStatus;
        ImageView imgUserPic,img_send_request;
        ProgressBar progressBar;
        RelativeLayout relativeLayout;

        View layout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imgUserPic = itemView.findViewById(R.id.img_user);
            progressBar = itemView.findViewById(R.id.recycler_contact_search_progress_bar);
            //layout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
            relativeLayout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
            img_send_request = itemView.findViewById(R.id.recycler_already_get_review_img);
            txtRequestStatus = itemView.findViewById(R.id.recycler_school_review_txt_status);

        }
    }


    // sending connection request
    private void sendRequest(final AdapterSelfReviewChooseAthleteName.MyViewHolder holder, final String userId)
    {
        holder.progressBar.setVisibility(View.VISIBLE);
        ContractRequest.Presenter presenter = new PresenterRequest(mContext, new ContractRequest.View() {
            @Override
            public void onRequestSentSuccessfully() {
                Log.i(TAG,"onSuccess");
                // showing send icon
                holder.img_send_request.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.txtRequestStatus.setVisibility(View.VISIBLE);
                holder.txtRequestStatus.setText(R.string.pending);
            }

            @Override
            public void onReceivedAllRequests(List<Request.Data> requestDataList) {

            }

            @Override
            public void onRespondedSuccessfully() {

            }

            @Override
            public void onFailed(String message) {
                Log.i(TAG,"onFailed");

                holder.img_send_request.setImageResource(R.drawable.ic_add_contact);
                holder.txtRequestStatus.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                DialogHelper.showSimpleCustomDialog(mContext,"Failed...");
            }


            @Override
            public void onShowMessage(String message) {
                Log.i(TAG,"onShowMessage , message = "+message);
                holder.progressBar.setVisibility(View.GONE);
                if ("All ready Added".equals(message))
                {
                    holder.img_send_request.setVisibility(View.VISIBLE);
                    holder.img_send_request.setImageResource(R.drawable.ic_done);
                    DialogHelper.showSimpleCustomDialog(mContext,"Request Already Sent...");
                }
                else
                {
                    holder.img_send_request.setVisibility(View.VISIBLE);
                    holder.img_send_request.setImageResource(R.drawable.ic_add_contact);
                    DialogHelper.showSimpleCustomDialog(mContext,message);
                }
            }
        });

        if (UtilHelper.isConnectedToInternet(mContext))
        {
            holder.img_send_request.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
            presenter.sendRequest(userId, Request.REQUEST_FOR_SELF_REVIEW);
        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
    }


}