package org.ctdworld.propath.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationForAthlete;
import org.ctdworld.propath.adapter.AdapterSchoolReview;
import org.ctdworld.propath.contract.ContractSchoolReview;
import org.ctdworld.propath.helper.DialogHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.SchoolReview;
import org.ctdworld.propath.prefrence.SessionHelper;
import org.ctdworld.propath.presenter.PresenterSchoolReview;

import java.util.List;

public class FragmentSelfReview extends Fragment implements ContractSchoolReview.View {

    private final String TAG = FragmentSelfReview.class.getSimpleName();
    TextView school_review_txt_user_name; //school_review_txt_profile_created_date;
    Context mContext;
    Button subjectAdd,submitSchoolReview;
    RecyclerView recyclerSchoolReview;
    ImageView school_review_img_profile;
    EditText comments_given_by_teacher;
    FrameLayout self_Review_Layout;
    //ArrayList<String> schoolReviewList = new ArrayList<>();
    AdapterSchoolReview adapter;
    String athlete_id, athlete_name,athlete_image;



    // mode value review or edit
    public static final int VALUE_MODE_CREATE = 1;
    public static final int VALUE_MODE_EDIT = 2;

    int MODE_CRETE_OR_EDIT;


    ContractSchoolReview.Presenter mPresenter;
    ProgressBar mProgressBar;



    public FragmentSelfReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self_review, container, false);

        ActivityEducationForAthlete activity = (ActivityEducationForAthlete) getActivity();
        String athlete_ids = activity.getAthleteId();
        Log.d(TAG,"FragmentSelfReviewActivity ////-----> " + athlete_ids);

        init(view);
        setSubjectsAdapter();
        setListeners();
        return view;
    }

    private void init(View view) {
        mContext = getContext();
        mProgressBar = view.findViewById(R.id.progress_bar);
        mPresenter = new PresenterSchoolReview(mContext,this);
        school_review_txt_user_name = view.findViewById(R.id.school_review_txt_user_name);
     //   school_review_txt_profile_created_date = view.findViewById(R.id.school_review_txt_profile_created_date);
        school_review_img_profile = view.findViewById(R.id.school_review_img_profile);
        recyclerSchoolReview=view.findViewById(R.id.recycler_school_review);
        subjectAdd = view.findViewById(R.id.subjectAdd);
        submitSchoolReview = view.findViewById(R.id.submitSchoolReview);
        self_Review_Layout = view.findViewById(R.id.self_Review_Layout);
    }


    private void setListeners()
    {
        int picDimen = (int) mContext.getResources().getDimension(R.dimen.contactSearchRecyclerUserPicSize);
        int picWidth = UtilHelper.convertDpToPixel(mContext, picDimen);
        Glide.with(mContext)
                .load(SessionHelper.getInstance(mContext).getUser().getUserPicUrl())
                .apply(new RequestOptions().error(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().placeholder(mContext.getResources().getDrawable(R.drawable.ic_profile)))
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(picWidth, picWidth))
                .into(school_review_img_profile);
        school_review_txt_user_name.setText(SessionHelper.getInstance(mContext).getUser().getName());


        subjectAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                adapter.addLayout();
                if (adapter.getItemCount() == 7) {
                    subjectAdd.setVisibility(View.GONE);
                }
            }
        });


        submitSchoolReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<SchoolReview> list = adapter.getReviewList();

                for (int i = 0; i < list.size(); i++) {
                    SchoolReview schoolReview = new SchoolReview();
                    schoolReview = list.get(i);
                    schoolReview.setAthleteID(athlete_id);
                    schoolReview.setSubject(schoolReview.getSubject());
                    schoolReview.setGrade(schoolReview.getGrade());
                    schoolReview.setComments(comments_given_by_teacher.getText().toString());


                    Log.d(TAG, "Subject : " + schoolReview.getSubject() + " , grade = " + schoolReview.getGrade());

                }

                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.saveSchoolReview(list);
            }
        });
    }

    private void setSubjectsAdapter()
    {
     /*   adapter = new AdapterSchoolReview(mContext,0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerSchoolReview.setLayoutManager(layoutManager);
        recyclerSchoolReview.setAdapter(adapter);*/

    }


    @Override
    public void onSuccess(String msg) {
        Log.d("messages ",msg);
        DialogHelper.showSimpleCustomDialog(mContext,msg);
        Log.d("onSuccess ","Success.......");
    }

    @Override
    public void onFailed(String msg) {
        DialogHelper.showSimpleCustomDialog(mContext,msg);
        Log.d("messages ",msg);

        Log.d("onFailed ","Failed.......");
    }

    @Override
    public void onProgressListDeleted(String id) {

    }

    @Override
    public void onReceivedProgressReportList(List<SchoolReview> schoolReviewList) {

    }

    @Override
    public void onShowProgress() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onHideProgress() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onSetViewsDisabledOnProgressBarVisible() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSetViewsEnabledOnProgressBarGone() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onShowMessage(String message) {
      DialogHelper.showSimpleCustomDialog(mContext,message);
    }
}
