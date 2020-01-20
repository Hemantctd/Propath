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

public class AdapterYesOrNoSurveyQuestionsPreview extends RecyclerView.Adapter<AdapterYesOrNoSurveyQuestionsPreview.MyViewHolder> {
    private static final String TAG = AdapterYesOrNoSurveyQuestionsPreview.class.getSimpleName();
    Context context;
    List<Survey.SurveyData.YesNo.Questions> mSurveyQuestionList;


    public List<Survey.SurveyData.YesNo.Questions> getSurveyQusetions()
    {
        return mSurveyQuestionList;
    }


    public AdapterYesOrNoSurveyQuestionsPreview(Context context, List<Survey.SurveyData.YesNo.Questions> questionsArrayList) {
        this.context = context;
        this.mSurveyQuestionList = questionsArrayList;

    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText mSurveyQuestions;
        private ImageView mQuestionImg;
        private TextView mTxtQuestionNo;

        public MyViewHolder(View view)
        {
            super(view);
            mSurveyQuestions = view.findViewById(R.id.question_text);
            mQuestionImg = view.findViewById(R.id.question_img);
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
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
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNo.setText(positionData);

        final Survey.SurveyData.YesNo.Questions surveyQuestions = mSurveyQuestionList.get(position);
        holder.mSurveyQuestions.setText(surveyQuestions.getQuestion());

        int croppedImageHeight = UtilHelper.convertDpToPixel(context,170);
        int croppedImageWidth = UtilHelper.getScreenWidth(context);
        Glide.with(context)
                .load(surveyQuestions.getQuestionImageLink())
                .apply(new RequestOptions().override(croppedImageWidth,croppedImageHeight))
                .apply(new RequestOptions().centerCrop())
                .into(holder.mQuestionImg);


    }

    @Override
    public int getItemCount()
    {
            return mSurveyQuestionList.size();
    }

}
