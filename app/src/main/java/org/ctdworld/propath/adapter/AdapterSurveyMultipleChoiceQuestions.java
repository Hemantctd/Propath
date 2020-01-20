package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;


import java.util.ArrayList;
import java.util.List;

public class AdapterSurveyMultipleChoiceQuestions extends RecyclerView.Adapter<AdapterSurveyMultipleChoiceQuestions.MyViewHolder> {
    private static final String TAG = AdapterSurveyMultipleChoiceQuestions.class.getSimpleName();
    Context context;
    private ArrayList<EditText> questionList = new ArrayList<>();
    private List<Survey.SurveyData.MultipleChoice.Questions> mListQuestions;


    public AdapterSurveyMultipleChoiceQuestions(Context context) {
        this.context = context;
        mListQuestions = new ArrayList<>();
    }

    public void addQuestionList(List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsArrayList)
    {
        mListQuestions.addAll(surveyQuestionsArrayList);
        notifyDataSetChanged();
        /*for (int i=0; i<surveyQuestionsArrayList.size();i++)
        {
            addQuestion(surveyQuestionsArrayList.get(i));
        }*/
    }


    public void addQuestion(Survey.SurveyData.MultipleChoice.Questions surveyQuestions)
    {
        ArrayList<String> optionsList =new ArrayList<>();
        optionsList.add("");
        optionsList.add("");
        optionsList.add("");
        optionsList.add("");
        optionsList.add("");

        surveyQuestions.setOptionList(optionsList);
        mListQuestions.add(surveyQuestions);
        notifyItemInserted(getItemCount());
    }




    public List<Survey.SurveyData.MultipleChoice.Questions> getSurveyQusetions()
    {
        ArrayList<Survey.SurveyData.MultipleChoice.Questions> list  = new ArrayList<>();
        for (int i=0 ; i<mListQuestions.size() ; i++)
        {
            Survey.SurveyData.MultipleChoice.Questions surveyQuestions = mListQuestions.get(i);

            String question = questionList.get(i).getText().toString().trim();
            if (question.isEmpty())
                return  null;

            Log.d(TAG,"questions list : " + questionList.size());
            surveyQuestions.setQuestion(question);
            //surveyQuestions.setQuestionOptionsList();
            list.add(surveyQuestions);
        }

        return list;
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText survey_type_questions;
        private TextView mTxtQuestionNo;
        private ImageView mQuestionImg;

        public MyViewHolder(View view)
        {
            super(view);
            survey_type_questions = view.findViewById(R.id.survey_type_questions);
            mTxtQuestionNo =  view.findViewById(R.id.txt_question_no);
            mQuestionImg = view.findViewById(R.id.question_img);
            mQuestionImg.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_survey_item_questions,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNo.setText(positionData);
        questionList.add(holder.survey_type_questions);

        if (mListQuestions != null)
        {
            Survey.SurveyData.MultipleChoice.Questions surveyQuestions = mListQuestions.get(position);

            holder.survey_type_questions.setText(surveyQuestions.getQuestion());
        }
    }

    @Override
    public int getItemCount()
    {
        //return  1;
        if (mListQuestions!= null)
            return mListQuestions.size();
        else
            return 0;
    }

}
