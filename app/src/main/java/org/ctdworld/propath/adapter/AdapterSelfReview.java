package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityEducationSelfReviewCreate;
import org.ctdworld.propath.model.SelfReview;

import java.util.ArrayList;
import java.util.List;

public class AdapterSelfReview extends RecyclerView.Adapter<AdapterSelfReview.MyViewHolder> {

    private final String TAG = AdapterSelfReview.class.getSimpleName();
    Context mContext;
    ArrayList<SelfReview> listSelfReview;
 //   ArrayList<EditText> subjectList = new ArrayList<>();
 //   ArrayList<String> gradeList = new ArrayList<>();



    int MODE_CRETE_OR_EDIT;


    public AdapterSelfReview(Context context,ArrayList<SelfReview> selfReviewlist, int modeCreateOrEdit)
    {
        MODE_CRETE_OR_EDIT = modeCreateOrEdit;
        this.mContext = context;
        listSelfReview = new ArrayList<>();

        if (MODE_CRETE_OR_EDIT != ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT)
        {
            listSelfReview = selfReviewlist;

            for (int i = 0; i < listSelfReview.size(); i++)  // adding default value
                listSelfReview.get(i).setGrade("1");
        }

    }


    public void updateSelfReviewList(List<SelfReview> selfReviewList)
    {
        listSelfReview.addAll(selfReviewList);
        if (MODE_CRETE_OR_EDIT == ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT)
        {
            int otherSelfReviewSize = 7-listSelfReview.size();
            for (int i = 0; i < otherSelfReviewSize; i++)  // adding default value
            {
                SelfReview selfReview = new SelfReview();
                selfReview.setGrade("1");
               // Log.d(TAG,"Subject_id ... "+ selfReview.getSubjectID());

                listSelfReview.add(selfReview);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_self_review_report_layout,null);

        return new MyViewHolder(view);
    }

    public List<SelfReview> getReviewList()
    {
        ArrayList<SelfReview> listTemp  = new ArrayList<>();

        int totalSizeInLoop = 7;

//        if (subjectList.size() > gradeList.size())
//            totalSizeInLoop = gradeList.size();
//        else
//            totalSizeInLoop = subjectList.size();
//

        for (int i=0 ; i < listSelfReview.size() ; i++)
        {
            SelfReview selfReview = new SelfReview();
             String subject = listSelfReview.get(i).getSubject(); //subjectList.get(i).getText().toString().trim();
                String grade = listSelfReview.get(i).getGrade();///gradeList.get(i);
                String subjectId = listSelfReview.get(i).getSubjectID();

            if (subject != null && !subject.isEmpty())
                {
                    selfReview.setSubject(subject);
                    selfReview.setGrade(grade);
                    selfReview.setSubjectID(subjectId);
                    Log.d(TAG,"Subject_id ####... "+ selfReview.getSubjectID());

                    listTemp.add(selfReview);
                }

        }
        return listTemp;
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        SelfReview selfReview = listSelfReview.get(position);

        if (MODE_CRETE_OR_EDIT != ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT)
        {
            holder.editSubject.setHint("Subject " +(holder.getAdapterPosition()+1));
           // subjectList.add(holder.selfReviewSubjects);
           // gradeList.add(position, String.valueOf(1));

            //selfReview.setSubject(holder.selfReviewSubjects.getText().toString().trim());
        }


        if (MODE_CRETE_OR_EDIT == ActivityEducationSelfReviewCreate.VALUE_MODE_EDIT)
        {
            if (selfReview.getSubject() != null && !selfReview.getSubject().isEmpty()) {
                holder.editSubject.setText(selfReview.getSubject());
                setGrade(selfReview.getGrade(),holder);

            }
            else {
                holder.editSubject.setHint("Subject " + (holder.getAdapterPosition() + 1));
            }
        }

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               /* if(gradeList.get(position).contains("1"))
                {*/
                   /* holder.selfReviewlikeDislike.setImageResource(R.drawable.ic_dislike_icon);
                    holder.selfReviewlikeDislike.setColorFilter(mContext.getResources().getColor(R.color.colorDarkGrey));
*/
                DrawableCompat.setTint(holder.imgLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
                DrawableCompat.setTint(holder.imgDisLike.getDrawable(), mContext.getResources().getColor(R.color.colorGrey));

                SelfReview selfReview1 = listSelfReview.get(holder.getAdapterPosition());
                selfReview1.setGrade("1");
                listSelfReview.set(position, selfReview1);

                   /* Log.d("flag value dislike : " , String.valueOf(gradeList.get(position)));

                }
                else
                {

                    holder.selfReviewlikeDislike.setImageResource(R.drawable.ic_like);
                    holder.selfReviewlikeDislike.setColorFilter(mContext.getResources().getColor(R.color.colorTheme));*/

                    /*gradeList.set(position, "1");

                    Log.d("flag value like : " , String.valueOf(gradeList.get(position)));
                }*/
            }
        });


        holder.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DrawableCompat.setTint(holder.imgLike.getDrawable(), mContext.getResources().getColor(R.color.colorGrey));
                DrawableCompat.setTint(holder.imgDisLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
               // gradeList.set(position, "0");

                SelfReview selfReview1 = listSelfReview.get(holder.getAdapterPosition());
                selfReview1.setGrade("0");
                listSelfReview.set(position, selfReview1);
            }
        });


        holder.editSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SelfReview selfReview1 = listSelfReview.get(holder.getAdapterPosition());
                selfReview1.setSubject(s.toString());
                listSelfReview.set(position, selfReview1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public int getItemCount()
    {
        return listSelfReview.size();
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        EditText editSubject;
        ImageView imgDisLike, imgLike;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            editSubject = itemView.findViewById(R.id.self_review_subjects);
            imgDisLike = itemView.findViewById(R.id.self_review_like_dislike);
            imgLike = itemView.findViewById(R.id.self_review_like);
        }

    }

    // setting grade if review is being edited
    private void setGrade(String grade, AdapterSelfReview.MyViewHolder holder)
    {
        if (grade == null || grade.isEmpty())
            return;


        switch (grade)
        {
            case "0" :
                DrawableCompat.setTint(holder.imgLike.getDrawable(), mContext.getResources().getColor(R.color.colorGrey));
                DrawableCompat.setTint(holder.imgDisLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
                break;

            case "1" :
                DrawableCompat.setTint(holder.imgLike.getDrawable(), mContext.getResources().getColor(R.color.colorTheme));
                DrawableCompat.setTint(holder.imgDisLike.getDrawable(), mContext.getResources().getColor(R.color.colorGrey));
                break;

        }
    }
}
