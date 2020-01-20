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

import java.util.ArrayList;
import java.util.List;

public class AdapterSubmitSurvey extends RecyclerView.Adapter<AdapterSubmitSurvey.MyViewHolder>
{
    private static final String TAG = AdapterSurveyPreviewQuestions.class.getSimpleName();
    Context context;
    String get_number_of_qusetions;
    ArrayList<SurveyQusetions> surveyQuestionsList;
    ArrayList<EditText> answerList = new ArrayList<>();
    ArrayList<SurveyQusetions> mSurveyAnswerList;

    public AdapterSubmitSurvey(Context context, String get_number_of_qusetions, ArrayList<SurveyQusetions> surveyQuestionsList, ArrayList<SurveyQusetions> surveyAnswerList) {
        this.context = context;
        this.get_number_of_qusetions = get_number_of_qusetions;
        this.surveyQuestionsList = surveyQuestionsList;
        this.mSurveyAnswerList = surveyAnswerList;
    }

    public List<SurveyQusetions> getSurveyAnswers()
    {
        ArrayList<SurveyQusetions> list  = new ArrayList<>();
        for (int i=0 ; i<answerList.size() ; i++)
        {
            SurveyQusetions surveyQusetions = new SurveyQusetions();

            String answers = answerList.get(i).getText().toString().trim();
            if (answers.isEmpty())
                return  null;

            Log.d(TAG,"answer list : " + answerList.size());
            surveyQusetions.setAnswers(answers);

            list.add(surveyQusetions);
        }

        return list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public EditText survey_type_questions,type_answer;
        public MyViewHolder(View view)
        {
            super(view);
            survey_type_questions = view.findViewById(R.id.survey_type_questions);
            type_answer = view.findViewById(R.id.answerBox);
            survey_type_questions.setTextColor(context.getResources().getColor(R.color.colorTheme));
            survey_type_questions.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            type_answer.setVisibility(View.VISIBLE);
            survey_type_questions.setFocusable(false);
            type_answer.setFocusable(true);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_survey_item_questions,null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SurveyQusetions surveyQusetions = surveyQuestionsList.get(position);
        holder.survey_type_questions.setText(surveyQusetions.getQuestions());

        answerList.add(holder.type_answer);

    }

    @Override
    public int getItemCount() {
        return Integer.valueOf(get_number_of_qusetions);
    }
}
