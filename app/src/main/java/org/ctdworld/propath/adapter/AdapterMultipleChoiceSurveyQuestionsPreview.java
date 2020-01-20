package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class AdapterMultipleChoiceSurveyQuestionsPreview extends RecyclerView.Adapter<AdapterMultipleChoiceSurveyQuestionsPreview.MyViewHolder> {
    //private static final String TAG = AdapterMultipleChoiceSurveyQuestionsPreview.class.getSimpleName();
    private Context mContext;
    private List<Survey.SurveyData.MultipleChoice.Questions> mSurveyQuestionList;
    private LayoutInflater inflater;

    public AdapterMultipleChoiceSurveyQuestionsPreview(Context context, List<Survey.SurveyData.MultipleChoice.Questions> questionsArrayList) {
        this.mContext = context;
        this.mSurveyQuestionList = questionsArrayList;
        inflater = LayoutInflater.from(mContext);

    }

    public List<Survey.SurveyData.MultipleChoice.Questions> getSurveyQusetions()
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
        View view = inflater.inflate(R.layout.recycler_multiple_choice_layout,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final Survey.SurveyData.MultipleChoice.Questions surveyQuestions = mSurveyQuestionList.get(position);
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtMultipleChoiceQuestionNo.setText(positionData);

        holder.mSurveyQuestionOptions1.setFocusable(false);
        holder.mSurveyQuestionOptions2.setFocusable(false);
        holder.mSurveyQuestionOptions3.setFocusable(false);
        holder.mSurveyQuestionOptions4.setFocusable(false);
        holder.mSurveyQuestionOptions5.setFocusable(false);

        holder.mSurveyQuestions.setText(surveyQuestions.getQuestion());

        List<String> optionsList = surveyQuestions.getOptionList();

        if (optionsList != null) {
            List<String> surveyOptionsList = surveyQuestions.getOptionList();

            holder.mSurveyQuestionOptions1.setText(surveyOptionsList.get(0));
            holder.mSurveyQuestionOptions2.setText(surveyOptionsList.get(1));
            holder.mSurveyQuestionOptions3.setText(surveyOptionsList.get(2));
            holder.mSurveyQuestionOptions4.setText(surveyOptionsList.get(3));
            holder.mSurveyQuestionOptions5.setText(surveyOptionsList.get(4));
        }
        else
            return;


       /* if (surveyQuestions.getLink().equals("")) {
            holder.mQuestionImage.setVisibility(View.GONE);
        }
        else {*/
            int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, 170);
            int croppedImageWidth = UtilHelper.getScreenWidth(mContext);

            Glide.with(mContext)
                    .load(surveyQuestions.getQuestionImageLink())
                    .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mQuestionImage);
        /*}*/

    }

    @Override
    public int getItemCount()
    {
            return mSurveyQuestionList.size();
    }





}
