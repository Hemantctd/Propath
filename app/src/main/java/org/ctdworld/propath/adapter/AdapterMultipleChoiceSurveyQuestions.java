package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AdapterMultipleChoiceSurveyQuestions extends RecyclerView.Adapter<AdapterMultipleChoiceSurveyQuestions.MyViewHolder> {
    private static final String TAG = AdapterMultipleChoiceSurveyQuestions.class.getSimpleName();
    Context context;
    private List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList;

    private OnAdapterSurveyMultipleChoiceListener mListener;
    private int mUpdatingItemPosition = ConstHelper.NOT_FOUND;
    LayoutInflater inflater;

    public AdapterMultipleChoiceSurveyQuestions(Context context, List<Survey.SurveyData.MultipleChoice.Questions> surveyQuestionsList, OnAdapterSurveyMultipleChoiceListener listener) {
        this.context = context;
        this.surveyQuestionsList = surveyQuestionsList;
        this.mListener = listener;
        inflater = LayoutInflater.from(context);
    }


    public interface OnAdapterSurveyMultipleChoiceListener{void onChangeImageClickedInAdapter();}


    public List<Survey.SurveyData.MultipleChoice.Questions> getSurveyQusetions()
    {
        return surveyQuestionsList;
    }

    public int getUpdatingItemPosition()
    {
        return mUpdatingItemPosition;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private EditText mQuestions,mMultipleChoiceOption1,mMultipleChoiceOption2,mMultipleChoiceOption3,mMultipleChoiceOption4
                ,mMultipleChoiceOption5;
        private TextView mTxtMultipleChoiceQuestionNo;

        public MyViewHolder(View view)
        {
            super(view);
            mQuestions = view.findViewById(R.id.multiple_choice_questions);
            mMultipleChoiceOption1 = view.findViewById(R.id.multiple_choice_options_1);
            mMultipleChoiceOption2 = view.findViewById(R.id.multiple_choice_options_2);
            mMultipleChoiceOption3 = view.findViewById(R.id.multiple_choice_options_3);
            mMultipleChoiceOption4 = view.findViewById(R.id.multiple_choice_options_4);
            mMultipleChoiceOption5 = view.findViewById(R.id.multiple_choice_options_5);
            imageView = view.findViewById(R.id.question_img);
            mTxtMultipleChoiceQuestionNo = view.findViewById(R.id.multilple_no_of_question);

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {

        final Survey.SurveyData.MultipleChoice.Questions surveyQuestions = surveyQuestionsList.get(position);
        holder.mQuestions.setText(surveyQuestions.getQuestion());
        String holderPosition = String.valueOf((holder.getAdapterPosition()+1));
        String positionData = holderPosition +" . ";
        holder.mTxtMultipleChoiceQuestionNo.setText(positionData);


        List<String> optionsList = surveyQuestions.getOptionList();

        if (optionsList == null)
        return;



        holder.mMultipleChoiceOption1.setText(optionsList.get(0));
        holder.mMultipleChoiceOption2.setText(optionsList.get(1));
        holder.mMultipleChoiceOption3.setText(optionsList.get(2));
        holder.mMultipleChoiceOption4.setText(optionsList.get(3));
        holder.mMultipleChoiceOption5.setText(optionsList.get(4));



        editListener(optionsList,0,holder.mMultipleChoiceOption1);
        editListener(optionsList,1,holder.mMultipleChoiceOption2);
        editListener(optionsList,2,holder.mMultipleChoiceOption3);
        editListener(optionsList,3,holder.mMultipleChoiceOption4);
        editListener(optionsList,4,holder.mMultipleChoiceOption5);

        surveyQuestionsList.set(holder.getAdapterPosition(), surveyQuestions);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                {
                    Log.i(TAG,"add image or video clicked");
                    mListener.onChangeImageClickedInAdapter();
                    mUpdatingItemPosition = holder.getAdapterPosition();
                }


            }
        });

        String link;
        link = surveyQuestions.getQuestionImageLink();
        if (link != null && !link.isEmpty()) {
            //int imageSize = UtilHelper.convertDpToPixel(context, (int) context.getResources().getDimension(R.dimen.trainingPlanItemImageSize));
            int croppedImageHeight = UtilHelper.convertDpToPixel(context,170);
            int croppedImageWidth = UtilHelper.getScreenWidth(context);

            Glide.with(context)
                    .load(surveyQuestions.getQuestionImageLink())
                    .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.imageView);

        }
    }

    // updating image
    public void updateItem(int position, Survey.SurveyData.MultipleChoice.Questions surveyQuestions)
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


    private void editListener(final List<String> optionsList, final int optionListIndex, EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                optionsList.set(optionListIndex, String.valueOf(s)) ;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}