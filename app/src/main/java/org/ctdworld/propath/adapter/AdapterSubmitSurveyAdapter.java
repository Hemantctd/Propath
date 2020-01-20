package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.List;

public class AdapterSubmitSurveyAdapter extends RecyclerView.Adapter<AdapterSubmitSurveyAdapter.MyViewHolder>
{
    private final String TAG = AdapterSubmitSurveyAdapter.class.getSimpleName();
    Context context;
    List<SurveyQusetions> mSurveyQuestionsList;

    public AdapterSubmitSurveyAdapter(Context context, List<SurveyQusetions> surveyQusetionsList)
    {
        this.context=context;
        this.mSurveyQuestionsList = surveyQusetionsList;
    }
    public  void updateList(List<SurveyQusetions> surveyQusetionsList)
    {
        this.mSurveyQuestionsList = surveyQusetionsList;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_survey_item_questions, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        SurveyQusetions surveyQusetions = mSurveyQuestionsList.get(position);
        Log.d(TAG,"survey questionss" + surveyQusetions.getQuestions());
        Log.d(TAG,"survey answers" + surveyQusetions.getAnswers());


        holder.survey_type_questions.setText(surveyQusetions.getQuestions());
        holder.type_answer.setText(surveyQusetions.getAnswers());
    }

    @Override
    public int getItemCount()
    {
        return mSurveyQuestionsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public EditText survey_type_questions,type_answer;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            survey_type_questions = itemView.findViewById(R.id.survey_type_questions);
            type_answer = itemView.findViewById(R.id.answerBox);
            survey_type_questions.setTextColor(context.getResources().getColor(R.color.colorTheme));
            survey_type_questions.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            type_answer.setVisibility(View.VISIBLE);
            survey_type_questions.setFocusable(false);
            type_answer.setFocusable(false);

        }
        // public void bind(String text) {textView.setText(text); }
    }
}
