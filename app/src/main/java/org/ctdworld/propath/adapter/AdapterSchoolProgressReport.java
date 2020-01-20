package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.SchoolReview;

import java.util.List;

public class AdapterSchoolProgressReport extends RecyclerView.Adapter<AdapterSchoolProgressReport.MyViewHolder>
{
    private final String TAG = AdapterSchoolProgressReport.class.getSimpleName();
    Context context;
    private List<SchoolReview> progressList;
    public AdapterSchoolProgressReport(Context context,List<SchoolReview> mProgressList)
    {
        this.context=context;
        this.progressList = mProgressList;
    }
    public  void updateList(List<SchoolReview> progressView)
    {
        this.progressList = progressView;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_school_review_items, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        SchoolReview schoolReview = progressList.get(position);
        Log.d(TAG,"subjects = "+schoolReview.getSubject());
        holder.subjectName.setText(schoolReview.getSubject());

        String grade = schoolReview.getGrade();
        if(grade.equalsIgnoreCase("n"))
        {
            holder.nButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }
        if (grade.equalsIgnoreCase("a"))
        {
            holder.aButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
        }
        if (grade.equalsIgnoreCase("m"))
        {
            holder.mButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        }
        if (grade.equalsIgnoreCase("e"))
        {
            holder.eButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
        }
    }

    @Override
    public int getItemCount()
    {
        return progressList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText subjectName;
        private Button eButtonSelect,nButtonSelect,aButtonSelect,mButtonSelect;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            subjectName =   itemView.findViewById(R.id.subjectName);
            subjectName.setFocusable(false);
            nButtonSelect = itemView.findViewById(R.id.nButtonSelect);
            aButtonSelect = itemView.findViewById(R.id.aButtonSelect);
            mButtonSelect = itemView.findViewById(R.id.mButtonSelect);
            eButtonSelect = itemView.findViewById(R.id.eButtonSelect);
        }
        // public void bind(String text) {textView.setText(text); }
    }
}
