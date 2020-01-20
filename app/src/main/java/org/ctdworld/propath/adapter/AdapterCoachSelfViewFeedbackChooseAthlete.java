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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityCoachSelfReviewList;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

public class AdapterCoachSelfViewFeedbackChooseAthlete extends RecyclerView.Adapter<AdapterCoachSelfViewFeedbackChooseAthlete.MyViewHolder> {

    private final String TAG = AdapterCoachFeedbackChooseAthleteName.class.getSimpleName();
    private Context mContext;
    private List<GetAthletes> mUserList;

    public AdapterCoachSelfViewFeedbackChooseAthlete(Context context, List<GetAthletes> userList) {
        this.mUserList = userList;
        this.mContext=context;
    }


    public void addUserList(List<GetAthletes> users)
    {
        // list will be shown only when letters are type in search box
        this.mUserList = users;
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<GetAthletes> filterdNames) {
        this.mUserList = filterdNames;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public AdapterCoachSelfViewFeedbackChooseAthlete.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_athlete_name, parent, false);

        return new AdapterCoachSelfViewFeedbackChooseAthlete.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCoachSelfViewFeedbackChooseAthlete.MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);

        final GetAthletes user = mUserList.get(position);

        holder.txtName.setText(user.getName());
        if (user.getUserId().equals(SessionHelper.getInstance(mContext).getUser().getUserId()))
            holder.relativeLayout.setVisibility(View.GONE);

        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);
        int picHeight = UtilHelper.convertDpToPixel(mContext,picDimen);

        Glide.with(mContext)
                .load(user.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picHeight))
                .into(holder.imgUserPic);

        holder.imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
              //  intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                intent.putExtra(ConstHelper.Key.ID,user.getUserId());
                mContext.startActivity(intent);
            }
        });


         /*   holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ActivityCoachSelfReviewList.class);
                    intent.putExtra("athlete_id",user.getUserId());
                    mContext.startActivity(intent);
                }
            });
*/




        switch (user.getRequestStatus())
        {
            case Request.REQUEST_STATUS_ACCEPT:

                holder.img_send_request.setVisibility(View.VISIBLE);  // making visible to show done icon
                holder.img_send_request.setImageResource(R.drawable.ic_done);
                holder.txtRequestStatus.setVisibility(View.GONE);

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = (new Intent(mContext, ActivityCoachSelfReviewList.class));
                        i.putExtra("athlete_id", user.getUserId());
                        i.putExtra("athlete_image", user.getUserPicUrl());
                        i.putExtra("athlete_name", user.getName());

                        mContext.startActivity(i);
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
                    String messagePermission = "Without permission you can not see progress report";

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


   // ActivityCoachFeedbackCoachView
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }



    // ViewHolder class
    public class MyViewHolder  extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtRequestStatus;
        private ImageView imgUserPic;
        private ImageView img_send_request;
        private ProgressBar progressBar;
        RelativeLayout relativeLayout;

        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtName = itemView.findViewById(R.id.txt_name);
            imgUserPic = itemView.findViewById(R.id.img_user);
            progressBar = itemView.findViewById(R.id.recycler_contact_search_progress_bar);
            relativeLayout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
            img_send_request = itemView.findViewById(R.id.recycler_already_get_review_img);
            txtRequestStatus = itemView.findViewById(R.id.recycler_school_review_txt_status);
        }

    }








    // sending connection request
    private void sendRequest(final AdapterCoachSelfViewFeedbackChooseAthlete.MyViewHolder holder, final String userId)
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
                holder.progressBar.setVisibility(View.GONE);
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
            presenter.sendRequest(userId, Request.REQUEST_FOR_COACH_SELF_REVIEW);
        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
    }

    }
