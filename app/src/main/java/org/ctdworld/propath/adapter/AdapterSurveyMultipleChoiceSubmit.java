package org.ctdworld.propath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.Survey;

import java.util.List;


public class AdapterSurveyMultipleChoiceSubmit extends RecyclerView.Adapter<AdapterSurveyMultipleChoiceSubmit.MyViewHolder> {
    //private static final String TAG = AdapterMultipleChoiceSurveyQuestionsPreview.class.getSimpleName();
    private Context mContext;
    private List<Survey.SurveyData.MultipleChoice.Questions> mSurveyQuestionList;
    private LayoutInflater inflater;

    public AdapterSurveyMultipleChoiceSubmit(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);

    }

    public List<Survey.SurveyData.MultipleChoice.Questions> getSurveyQuestions()
    {
        return mSurveyQuestionList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView mTxtMultipleChoiceQuestionNo;
        private ImageView mQuestionImage;
        private EditText mSurveyQuestions,mSurveyQuestionOptions1,mSurveyQuestionOptions2,mSurveyQuestionOptions3,mSurveyQuestionOptions4,mSurveyQuestionOptions5;
        public MyViewHolder(View view) {
            super(view);
            mSurveyQuestions = view.findViewById(R.id.multiple_choice_questions);
            mSurveyQuestionOptions1 = view.findViewById(R.id.multiple_choice_options_1);
            mSurveyQuestionOptions2 = view.findViewById(R.id.multiple_choice_options_2);
            mSurveyQuestionOptions3 = view.findViewById(R.id.multiple_choice_options_3);
            mSurveyQuestionOptions4 = view.findViewById(R.id.multiple_choice_options_4);
            mSurveyQuestionOptions5 = view.findViewById(R.id.multiple_choice_options_5);
            mQuestionImage = view.findViewById(R.id.question_img);
            mTxtMultipleChoiceQuestionNo = view.findViewById(R.id.multilple_no_of_question);
         /*   profile_img_plus = view.findViewById(R.id.profile_img_plus);
            profile_img_plus.setVisibility(View.GONE);
        }*/
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_multiple_choice_layout_for_user,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
            return 2;
    }





}
