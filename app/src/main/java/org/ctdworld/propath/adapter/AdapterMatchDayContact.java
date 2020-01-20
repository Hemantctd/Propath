package org.ctdworld.propath.adapter;

import android.content.Context;
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

import org.ctdworld.propath.R;
import org.ctdworld.propath.contract.ContractRequest;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Request;
import org.ctdworld.propath.model.User;
import org.ctdworld.propath.presenter.PresenterRequest;

import java.util.List;

public class AdapterMatchDayContact extends RecyclerView.Adapter<AdapterMatchDayContact.MyViewHolder>
{
    // # constants
    private final String TAG = AdapterMatchDayContact.class.getSimpleName();

    // # variables
    private Context mContext;
    private OnUserClickedListener mUserClickedListener;


    // # custom listener
    public interface OnUserClickedListener{void onUserClicked();}


    public AdapterMatchDayContact(Context context, OnUserClickedListener listener) {
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
        holder.imgSendRequest.setImageResource(R.drawable.ic_settings);
        holder.imgSendRequest.setColorFilter(R.color.colorTheme);
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









}
