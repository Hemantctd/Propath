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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;

import java.util.List;

public class AdapterYesOrNoSurveyQuestionsWithOptions extends RecyclerView.Adapter<AdapterYesOrNoSurveyQuestionsWithOptions.MyViewHolder> {
    private static final String TAG = AdapterYesOrNoSurveyQuestionsWithOptions.class.getSimpleName();
    Context context;
    private List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList;
    private OnAdapterSurveyYesNoListener mListener;
    private int mUpdatingItemPosition = ConstHelper.NOT_FOUND;


    public AdapterYesOrNoSurveyQuestionsWithOptions(Context context, List<Survey.SurveyData.YesNo.Questions> surveyQuestionsList, OnAdapterSurveyYesNoListener listener) {
        this.context = context;
        this.surveyQuestionsList = surveyQuestionsList;
        this.mListener = listener;
    }

    public interface OnAdapterSurveyYesNoListener{void onChangeImageClickedInAdapter();}


    public List<Survey.SurveyData.YesNo.Questions> getSurveyQusetions()
    {
        return surveyQuestionsList;
    }

    public int getUpdatingItemPosition()
    {
        return mUpdatingItemPosition;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private EditText mQuestions;
        private ImageView mQuestionsImage;
        private TextView mTxtQuestionNo;

        public MyViewHolder(View view)
        {
            super(view);
            mQuestions = view.findViewById(R.id.question_text);
            mQuestionsImage = view.findViewById(R.id.question_img);
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_yes_or_not_layout,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtQuestionNo.setText(positionData);

        final Survey.SurveyData.YesNo.Questions surveyQuestions = surveyQuestionsList.get(position);
        holder.mQuestions.setText(surveyQuestions.getQuestion());
        surveyQuestionsList.set(holder.getAdapterPosition(), surveyQuestions);
        holder.mQuestionsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                {
                    Log.i(TAG,"add image clicked");
                    mListener.onChangeImageClickedInAdapter();
                    mUpdatingItemPosition = holder.getAdapterPosition();
                }

            }
        });


        String link ;
        link = surveyQuestions.getQuestionImageLink();
        if (link != null && !link.isEmpty()) {
            int croppedImageHeight = UtilHelper.convertDpToPixel(context,170);
            int croppedImageWidth = UtilHelper.getScreenWidth(context);
            Glide.with(context)
                    .load(surveyQuestions.getQuestionImageLink())
                    .apply(new RequestOptions().override(croppedImageWidth,croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.mQuestionsImage);

        }
    }

    // updating image
    public void updateItem(int position, Survey.SurveyData.YesNo.Questions surveyQuestions)
    {

        if (surveyQuestions == null)
            return;

        surveyQuestionsList.set(position, surveyQuestions);
        notifyItemChanged(position, surveyQuestions);
    }

    @Override
    public int getItemCount()
    {
            return surveyQuestionsList.size();
    }

}
