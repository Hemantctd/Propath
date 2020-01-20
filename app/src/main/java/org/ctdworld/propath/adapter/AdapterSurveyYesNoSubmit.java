package org.ctdworld.propath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class AdapterSurveyYesNoSubmit extends RecyclerView.Adapter<AdapterSurveyYesNoSubmit.MyViewHolder> {
    private static final String TAG = AdapterSurveyYesNoSubmit.class.getSimpleName();
    Context context;
    List<Survey.SurveyData.YesNo.Questions> mSurveyQuestionList;


    public List<Survey.SurveyData.YesNo.Questions> getSurveyQusetions()
    {
        return mSurveyQuestionList;
    }


    public AdapterSurveyYesNoSubmit(Context context) {
        this.context = context;

    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText mSurveyQuestions;
        private ImageView mQuestionImg;
        private TextView mTxtQuestionNo;
        RadioButton radioButton1,radioButton2;

        public MyViewHolder(View view)
        {
            super(view);
            mSurveyQuestions = view.findViewById(R.id.question_text);
            mQuestionImg = view.findViewById(R.id.question_img);
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
            radioButton1 = view.findViewById(R.id.yes_btn);
            radioButton2 = view.findViewById(R.id.no_btn);
            radioButton1.setEnabled(true);
            radioButton2.setEnabled(true);
        /*    profile_img_plus = view.findViewById(R.id.profile_img_plus);
            profile_img_plus.setVisibility(View.GONE);*/
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_yes_or_not_layout,null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {


    }

    @Override
    public int getItemCount()
    {
            return 4;
    }

}
