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

public class AdapterSurveyQuestions extends RecyclerView.Adapter<AdapterSurveyQuestions.MyViewHolder> {
    private static final String TAG = AdapterSurveyQuestions.class.getSimpleName();
    Context context;
    private ArrayList<EditText> questionList = new ArrayList<>();
    private List<Survey.SurveyData.FreeResponse.Questions> mListQuestions;

    public AdapterSurveyQuestions(Context context) {
        this.context = context;
        mListQuestions = new ArrayList<>();

    }

    public void addQuestion(Survey.SurveyData.FreeResponse.Questions surveyQuestions)
    {
        mListQuestions.add(surveyQuestions);
        notifyItemInserted(getItemCount());
    }

    public void addQuestionList(List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsArrayList)
    {
        for (int i=0; i<surveyQuestionsArrayList.size();i++)
        {
            addQuestion(surveyQuestionsArrayList.get(i));
        }
    }

    public List<Survey.SurveyData.FreeResponse.Questions> getSurveyQusetions()
    {
        ArrayList<Survey.SurveyData.FreeResponse.Questions> list  = new ArrayList<>();
        for (int i=0 ; i<mListQuestions.size() ; i++)
        {
            Survey.SurveyData.FreeResponse.Questions surveyQusetions = mListQuestions.get(i);

            String questions = questionList.get(i).getText().toString().trim();
            if (questions.isEmpty())
                return  null;

                Log.d(TAG,"questions list : " + questionList.size());
                surveyQusetions.setQuestion(questions);

                list.add(surveyQusetions);
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
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
            mQuestionImg = view.findViewById(R.id.question_img);
            mQuestionImg.setVisibility(View.GONE);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_survey_item_questions,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        questionList.add(holder.survey_type_questions);
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNo.setText(positionData);

        if (mListQuestions != null)
        {
            Survey.SurveyData.FreeResponse.Questions surveyQusetions = mListQuestions.get(position);
            holder.survey_type_questions.setText(surveyQusetions.getQuestion());
        }
    }

    @Override
    public int getItemCount()
    {

        if (mListQuestions!= null)
            return mListQuestions.size();
        else
            return 0;
    }

}
