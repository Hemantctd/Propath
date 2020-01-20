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


public class AdapterSurveyFreeResponseSubmit extends RecyclerView.Adapter<AdapterSurveyFreeResponseSubmit.MyViewHolder>{


    //private static final String TAG = AdapterSurveyPreviewQuestions.class.getSimpleName();
    Context mContext;
    private List<Survey.SurveyData.FreeResponse.Questions> mQuestionsList;

    public AdapterSurveyFreeResponseSubmit(Context context) {
        this.mContext = context;
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
            survey_type_questions.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
            survey_type_questions.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            mTxtQuestionNO =  view.findViewById(R.id.txt_question_no);
            type_answer.setVisibility(View.VISIBLE);
            mQuesImage = view.findViewById(R.id.question_img);
            survey_type_questions.setFocusable(false);
            type_answer.setFocusable(true);


        }
    }

    public List<Survey.SurveyData.FreeResponse.Questions> getSurveyQusetions()
    {
        return mQuestionsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recycler_survey_item_questions,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


       /* Survey.SurveyData.FreeResponse.Questions surveyQusetions = mQuestionsList.get(position);
          holder.survey_type_questions.setText(surveyQusetions.getQuestion());

        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNO.setText(positionData);

        int croppedImageHeight = UtilHelper.convertDpToPixel(mContext, 170);
        int croppedImageWidth = UtilHelper.getScreenWidth(mContext);

            Glide.with(mContext)
                    .load(surveyQusetions.getQuestionImg())
                    .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mQuesImage);
*/
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
