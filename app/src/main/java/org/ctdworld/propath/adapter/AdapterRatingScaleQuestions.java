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


public class AdapterRatingScaleQuestions extends RecyclerView.Adapter<AdapterRatingScaleQuestions.MyViewHolder> {
    private static final String TAG = AdapterRatingScaleQuestions.class.getSimpleName();
    Context context;
    private ArrayList<EditText> questionList = new ArrayList<>();
    private List<Survey.SurveyData.RatingScale.Questions> mListQuestions;



    public AdapterRatingScaleQuestions(Context context) {
        this.context = context;
        mListQuestions = new ArrayList<>();
      //  this.get_number_of_qusetions = get_number_of_qusetions;

    }


    public void addQuestionList(List<Survey.SurveyData.RatingScale.Questions> surveyQuestionArrayList)
    {
        mListQuestions.addAll(surveyQuestionArrayList);
        notifyDataSetChanged();
    }
    public void addQuestion(Survey.SurveyData.RatingScale.Questions surveyQuestions)
    {
        mListQuestions.add(surveyQuestions);
        notifyItemInserted(getItemCount());
    }


    public List<Survey.SurveyData.RatingScale.Questions> getSurveyQuestions()
    {
        ArrayList<Survey.SurveyData.RatingScale.Questions> list  = new ArrayList<>();
        for (int i=0 ; i<mListQuestions.size() ; i++)
        {
            Survey.SurveyData.RatingScale.Questions surveyQuestions =mListQuestions.get(i);

            String question = questionList.get(i).getText().toString().trim();
            if (question.isEmpty())
                return  null;

            Log.d(TAG,"questions list : " + questionList.size());
            surveyQuestions.setQuestion(question);

            list.add(surveyQuestions);
        }

        return list;
    }


    @NonNull
    @Override
    public AdapterRatingScaleQuestions.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_survey_item_questions,viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new AdapterRatingScaleQuestions.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRatingScaleQuestions.MyViewHolder holder, int i) {


        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNo.setText(positionData);
        questionList.add(holder.survey_type_questions);

        if (mListQuestions != null)
        {
            Survey.SurveyData.RatingScale.Questions surveyQuestions = mListQuestions.get(i);

            holder.survey_type_questions.setText(surveyQuestions.getQuestion());
        }
    }

    @Override
    public int getItemCount() {

        if (mListQuestions!= null)
            return mListQuestions.size();
        else
            return 0;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public EditText survey_type_questions;
        private ImageView mQuesImg;
        private TextView mTxtQuestionNo;

        public MyViewHolder(View view)
        {
            super(view);
            mQuesImg = view.findViewById(R.id.question_img);
            mQuesImg.setVisibility(View.GONE);
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
            survey_type_questions = view.findViewById(R.id.survey_type_questions);


        }
    }
}
