package org.ctdworld.propath.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.RoleHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.CreatedDataDetails;



// this fragment is being used to show user details and created or updated date for anything which has been created and saved on server
public class FragCreatingDetails extends Fragment {


    // # views
    private ImageView mImgCreatorPic;
    private TextView mTxtCreatorName;
    private TextView mTxtDataTime;
    private TextView mTxtCreatorRole;
    private TextView mTxtCreatedBy;


    // # key to set data
    private static final String ARG = "data";

    // # other variables
    private Context mContext;
    private CreatedDataDetails mCreatedDataDetails;


    public static FragCreatingDetails newInstance(CreatedDataDetails createdDataDetails) {
        FragCreatingDetails fragment = new FragCreatingDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG, createdDataDetails);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCreatedDataDetails = (CreatedDataDetails) getArguments().getSerializable(ARG);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_creating_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            init(view);  // initializing variables
            setViews(mCreatedDataDetails);  // setting data on views
        }
        catch (Exception ignored){}

    }



    // initializing variables
    private void init(View view) {
        mContext = getContext();
        mImgCreatorPic = view.findViewById(R.id.img_profile_pic);
        mTxtCreatorName = view.findViewById(R.id.txt_creator_name);
        mTxtCreatorRole = view.findViewById(R.id.txt_creator_role);
        mTxtDataTime = view.findViewById(R.id.txt_date_time);
        mTxtCreatedBy = view.findViewById(R.id.txt_created_by);
    }



    public void setViews(CreatedDataDetails createdDataDetails) {
        if (createdDataDetails == null)
            return;

        String creatorName = /*"Created By : "+*/createdDataDetails.getName();
        String creatorRole ;


        if (createdDataDetails.getRoleId() != null) {
            creatorRole = "Role : " + RoleHelper.getInstance(mContext).getRoleNameById(createdDataDetails.getRoleId());
        }
        else
        {
            creatorRole = "Role : " + createdDataDetails.getRole();
        }

        if (createdDataDetails.getCreated_by() != null)
        {
            String createdBy = "By : " + createdDataDetails.getCreated_by();

            mTxtCreatedBy.setVisibility(View.VISIBLE);
            mTxtCreatedBy.setText(createdBy);
        }
     /*   Date createdDate = DateTimeHelper.getDateObject(createdDataDetails.getCreatedDate(), DateTimeHelper.FORMAT_DATE_TIME);
        Date updatedDate = DateTimeHelper.getDateObject(createdDataDetails.getUpdatedDate(), DateTimeHelper.FORMAT_DATE_TIME);

        if (createdDate != null && updatedDate != null && updatedDate.getTime() > createdDate.getTime())
            dateTime = createdDataDetails.getCreatedDate();
        else
            dateTime = createdDataDetails.getUpdatedDate();

        if (dateTime == null)
            dateTime = createdDataDetails.getCreatedDate();*/

    //    String createDate = DateTimeHelper.getDateTime(createdDataDetails.getCreatedDate(), DateTimeHelper.FORMAT_DATE_TIME);
    //    String updateDate = DateTimeHelper.getDateTime(createdDataDetails.getUpdatedDate(), DateTimeHelper.FORMAT_DATE_TIME);

        try {
            if (UtilHelper.isFirstDateGreater(createdDataDetails.getUpdatedDate(), createdDataDetails.getCreatedDate()))
                mTxtDataTime.setText(createdDataDetails.getUpdatedDate());
            else
                mTxtDataTime.setText(createdDataDetails.getCreatedDate());

        } catch (Exception e)
        {
            mTxtDataTime.setText(createdDataDetails.getCreatedDate());
            e.printStackTrace();
        }

        mTxtCreatorName.setText(creatorName);
        mTxtCreatorRole.setText(creatorRole);
        UtilHelper.loadImageWithGlide(mContext, createdDataDetails.getUserPicUrl(), mImgCreatorPic, R.drawable.ic_profile);

    }
}
