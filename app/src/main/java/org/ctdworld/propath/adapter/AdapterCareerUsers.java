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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityProfileView;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CareerPlan;
import org.ctdworld.propath.model.Request;

import java.util.List;

public class AdapterCareerUsers extends RecyclerView.Adapter<AdapterCareerUsers.MyViewHolder>
{
    private final String TAG = AdapterCareerUsers.class.getSimpleName();
    private Context mContext;
    private List<CareerPlan.CareerUser> mCareerUserList;
    private int mUserPicDimen;
    private Listener mListener;
    private MyViewHolder mHolder;



    public interface Listener{
        void onAdapterItemClicked(CareerPlan.CareerUser careerUser);
        void onSendPermissionRequestClicked(CareerPlan.CareerUser careerUser);
    }


    public AdapterCareerUsers(Context context, List<CareerPlan.CareerUser> careerUserList, Listener listener) {
        Log.i(TAG,"adapter constructor");
        mCareerUserList = careerUserList;
        mContext = context;
        mUserPicDimen = UtilHelper.convertDpToPixel(context, (int) context.getResources().getDimension(R.dimen.adapterUserPicSize));
        mListener = listener;
    }


    public void updateUserList(List<CareerPlan.CareerUser> careerUserList)
    {
        this.mCareerUserList = careerUserList;
        notifyDataSetChanged();
    }



    // updating career data
    public void onDataUpdated(CareerPlan.CareerUser careerUser)
    {
        if (careerUser == null)
            return;

        int position = getAdapterPosition(careerUser.getUserId());

        if (position == ConstHelper.NOT_FOUND)
            return;

        mCareerUserList.set(position, careerUser);
        notifyItemChanged(position, careerUser);
    }


    // filtering contact list according to search in FragmentContact
    public void filterList(List<CareerPlan.CareerUser> filterUsers) {
        this.mCareerUserList = filterUsers;
        notifyDataSetChanged();
    }


    public void clearOlList()
    {
        if (mCareerUserList != null)
        {
            mCareerUserList.clear();
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_choose_athlete_name, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);


        if (validateNull(holder.getAdapterPosition()))  // returns if any object is null
            return;
        mHolder = holder;
        CareerPlan.CareerUser careerUser = mCareerUserList.get(position);
       // CareerPlan.CareerData careerData  = careerUser.getCareerData();


        holder.txtUserName.setText(careerUser.getUserName());
        UtilHelper.loadImageWithGlide(mContext,careerUser.getUserPicUrl(), R.drawable.ic_profile, mUserPicDimen, mUserPicDimen, holder.imgUserPic);
        setOnUserPicClicked(careerUser, holder.imgUserPic);
        setOnRowClicked(holder.txtUserName, careerUser);  // when user(row) is clicked
        showPermissionStatus(careerUser);   // setting permission status,
    }



    // returns true if any object is null
    private boolean validateNull(int position)
    {
        return mCareerUserList == null || mCareerUserList.get(position) == null || mCareerUserList.get(position).getCareerData() == null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mCareerUserList.size();
    }






    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtUserName;
        private ImageView imgUserPic;
        private ImageView imgPermission;
        private TextView txtPermission;
        View layout;
        View layoutPermission;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            txtUserName =  itemView.findViewById(R.id.txt_name);
            imgUserPic = itemView.findViewById(R.id.img_user);
            imgPermission = itemView.findViewById(R.id.recycler_already_get_review_img);
            txtPermission = itemView.findViewById(R.id.recycler_school_review_txt_status);
            layoutPermission = itemView.findViewById(R.id.recycler_contact_search_status_layout);
        }

    }

    
    
    private void setOnUserPicClicked(final CareerPlan.CareerUser careerUser, ImageView imageView)
    {
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityProfileView.class);
                intent.putExtra(ConstHelper.Key.ID, careerUser.getUserId());
                mContext.startActivity(intent);
            }
        });
    }



    // called when row(user) is clicked,
    private void setOnRowClicked(View view, final CareerPlan.CareerUser careerUser)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (mListener != null)
                    mListener.onAdapterItemClicked(careerUser);
            }
        });
    }




    // setting permission status if permission is given or not, also setting listener to send request if permission is not given
    private void showPermissionStatus(final CareerPlan.CareerUser careerUser)
    {

        if (careerUser.getPermissionRequestStatus() == null)
            return;

        mHolder.imgPermission.setVisibility(View.VISIBLE);


        switch (careerUser.getPermissionRequestStatus())
        {
            case Request.REQUEST_STATUS_ACCEPT:
                showPermissionImageView(mHolder, R.drawable.ic_done);
                mHolder.layoutPermission.setOnClickListener(null);   // removing click listener
                break;

            case Request.REQUEST_STATUS_PENDING:
                mHolder.layoutPermission.setOnClickListener(null);   // removing click listener
                showPermissionTextView(mContext.getString(R.string.pending));
                break;

            case Request.REQUEST_STATUS_REJECT:
                showPermissionImageView(mHolder, R.drawable.ic_add_contact);
                mHolder.layoutPermission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onSendPermissionRequestClicked(careerUser);
                    }
                });
                break;

                default:
                    mHolder.layoutPermission.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogHelper.showSimpleCustomDialog(mContext, mContext.getString(R.string.failed_title), mContext.getString(R.string.error_occurred));
                        }
                    });
        }
    }


    



    // showing permission TextView and hiding permission icon
    public void showPermissionTextView(String text)
    {
        mHolder.txtPermission.setVisibility(View.VISIBLE);
        mHolder.txtPermission.setText(text);
        mHolder.imgPermission.setVisibility(View.GONE);
    }



    // showing permission ImageView and hiding permission text
    private void showPermissionImageView(MyViewHolder holder, int imageResource)
    {
        holder.txtPermission.setVisibility(View.GONE);
        holder.imgPermission.setVisibility(View.VISIBLE);
        holder.imgPermission.setImageResource(imageResource);
    }



    // returns adapter position depending on CareerUser id
    private int getAdapterPosition(String userId)
    {
        if (userId == null)
            return ConstHelper.NOT_FOUND;

        for (int i=0; i<mCareerUserList.size(); i++)
        {
            CareerPlan.CareerUser careerUser = mCareerUserList.get(i);
            if (careerUser != null && careerUser.getUserId().equals(userId))
                return i;
        }
        return ConstHelper.NOT_FOUND;
    }



}
