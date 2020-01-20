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

public class AdapterSurveyPreviewQuestions extends RecyclerView.Adapter<AdapterSurveyPreviewQuestions.MyViewHolder>
{
    //private static final String TAG = AdapterSurveyPreviewQuestions.class.getSimpleName();
    Context context;
    private List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList;

    public AdapterSurveyPreviewQuestions(Context context, List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList) {
        this.context = context;
        this.surveyQuestionsList = surveyQuestionsList;
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText survey_type_questions,type_answer;
        private TextView mTxtQuestionNO;
        private ImageView mQuesImage;
        public MyViewHolder(View view)
        {
            super(view);
            survey_type_questions = view.findViewById(R.id.survey_type_questions);
            type_answer = view.findViewById(R.id.answerBox);
            survey_type_questions.setTextColor(context.getResources().getColor(R.color.colorTheme));
            survey_type_questions.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            mTxtQuestionNO =  view.findViewById(R.id.txt_question_no);
            type_answer.setVisibility(View.VISIBLE);
            mQuesImage = view.findViewById(R.id.question_img);
            survey_type_questions.setFocusable(false);
            type_answer.setFocusable(false);


        }
    }

    public List<Survey.SurveyData.FreeResponse.Questions> getSurveyQusetions()
    {
        return surveyQuestionsList;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Survey.SurveyData.FreeResponse.Questions surveyQusetions = surveyQuestionsList.get(position);
          holder.survey_type_questions.setText(surveyQusetions.getQuestion());

        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNO.setText(positionData);

        int croppedImageHeight = UtilHelper.convertDpToPixel(context, 170);
        int croppedImageWidth = UtilHelper.getScreenWidth(context);

            Glide.with(context)
                    .load(surveyQusetions.getQuestionImg())
                    .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mQuesImage);

    }

    @Override
    public int getItemCount() {
        return surveyQuestionsList.size();
    }


}
