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
import org.ctdworld.propath.activity.ActivityEducationSchoolReviewCreate;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.contract.ContractGetAllAthletes;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.GetAthletes;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.ArrayList;
import java.util.List;

public class AdapterSchoolReviewChooseAthleteName extends RecyclerView.Adapter<AdapterSchoolReviewChooseAthleteName.MyViewHolder> {

    private final String TAG = AdapterSchoolReviewChooseAthleteName.class.getSimpleName();
    private Context mContext;
    private List<GetAthletes> mUserList;
    String schoolReviewed;
    ContractGetAllAthletes.Presenter mPresenterGiveReviewAthletes;

    public AdapterSchoolReviewChooseAthleteName(Context context, List<GetAthletes> userList, ContractGetAllAthletes.Presenter presenter) {
        this.mUserList = userList;
        this.mContext=context;
        this.mPresenterGiveReviewAthletes = presenter;

    }


    public void addUserList(List<GetAthletes> users)
    {
        this.mUserList = users;
       notifyDataSetChanged();
       //list will be shown only when user types letters in search box
    }

    public void filterList(ArrayList<GetAthletes> filterdNames) {
        this.mUserList = filterdNames;
        notifyDataSetChanged();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_athlete_name, parent, false);
        Log.d(TAG,"school review : "+"school review athlete");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final GetAthletes user = mUserList.get(position);
        holder.txtName.setText(user.getName());
        schoolReviewed  = user.getSchoolreviewed();
//        if(schoolReviewed.equals("1"))
//        {
//            holder.recycler_already_get_review_img.setVisibility(View.VISIBLE);
//        }

        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext,picDimen);

        Glide.with(mContext)
                .load(user.getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth,picWidth))
                .into(holder.imgUserPic);

        holder.imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
             //   intent.putExtra(ActivityProfileView.KEY_PROFILE_TYPE,ActivityProfileView.VALUE_PROFILE_TYPE_OTHER);
                intent.putExtra(ConstHelper.Key.ID,user.getUserId());
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
                                        if (user.getSchool_recview_counter().equals("8"))
                                        {
                                            DialogHelper.showSimpleCustomDialog(mContext,"you already given 8 reviews in this year.");
                                        }
                                        else {
                                            Intent i = (new Intent(mContext, ActivityEducationSchoolReviewCreate.class));
                                            i.putExtra("athlete_id", user.getUserId());
                                            i.putExtra("athlete_image", user.getUserPicUrl());
                                            i.putExtra("athlete_name", user.getName());


                                            mContext.startActivity(i);
                                        }
                                    }
                                });
//
                                break;

                           case Request.REQUEST_STATUS_PENDING:
                               holder.txtRequestStatus.setVisibility(View.VISIBLE);
                               holder.img_send_request.setVisibility(View.GONE);
                              //  DialogHelper.showSimpleCustomDialog(mContext, "Request Is Pending", permissionPendingMesage);
                                break;

                    default:
                        Log.i(TAG,"request status = "+"send request , "+" code = "+user.getRequestStatus());
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
                        if (!checkPermissionStatus(user))
                            if (canSendPermissionRequest(user))
                                showPermissionDialog(holder, user);
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
       TextView txtName,txtRequestStatus;
        ImageView imgUserPic,img_send_request;
         ProgressBar progressBar;
        RelativeLayout relativeLayout;

        View layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imgUserPic = itemView.findViewById(R.id.img_user);
            progressBar = itemView.findViewById(R.id.recycler_contact_search_progress_bar);
           // layout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
            img_send_request = itemView.findViewById(R.id.recycler_already_get_review_img);
            relativeLayout = itemView.findViewById(R.id.recycler_athlete_name_search_layout);
            txtRequestStatus = itemView.findViewById(R.id.recycler_school_review_txt_status);
        }


    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }




    // sending connection request
    private void sendRequest(final AdapterSchoolReviewChooseAthleteName.MyViewHolder holder, final String userId)
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
                holder.txtRequestStatus.setText("Pending");
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
            presenter.sendRequest(userId, Request.REQUEST_FOR_TEACHER_GIVE_REVIEW);
        }
        else
            DialogHelper.showSimpleCustomDialog(mContext,"No Connection...");
  }



  private void showPermissionDialog(final AdapterSchoolReviewChooseAthleteName.MyViewHolder holder, final GetAthletes user)
  {
      String messagePermission = "Without permission you can not give school review";
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

    // if permission request has not been sent then returns true
    private boolean checkPermissionStatus(GetAthletes user)
    {
        return user.getRequestStatus().equals(Request.REQUEST_STATUS_ACCEPT);
    }

  // if permission request has not been sent then returns true
  private boolean canSendPermissionRequest(GetAthletes user)
  {
      return user.getRequestStatus().equals(Request.REQUEST_STATUS_REJECT);
  }

}
