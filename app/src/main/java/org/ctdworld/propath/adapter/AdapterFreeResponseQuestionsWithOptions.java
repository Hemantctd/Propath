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
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.ctdworld.propath.R;
import org.ctdworld.propath.helper.ConstHelper;
import org.ctdworld.propath.helper.UtilHelper;
import org.ctdworld.propath.model.Survey;

import java.io.File;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

public class AdapterFreeResponseQuestionsWithOptions extends RecyclerView.Adapter<AdapterFreeResponseQuestionsWithOptions.MyViewHolder> {
    private static final String TAG = AdapterFreeResponseQuestionsWithOptions.class.getSimpleName();
    Context context;
    private List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList;
    private OnAdapterSurveyFreeResponseListener mListener;
    private int mUpdatingItemPosition = ConstHelper.NOT_FOUND;


    public AdapterFreeResponseQuestionsWithOptions(Context context, List<Survey.SurveyData.FreeResponse.Questions> surveyQuestionsList, OnAdapterSurveyFreeResponseListener listener) {
        this.context = context;
        this.surveyQuestionsList = surveyQuestionsList;
        this.mListener = listener;
    }

    public interface OnAdapterSurveyFreeResponseListener{void onChangeImageClickedInAdapter();}


    public List<Survey.SurveyData.FreeResponse.Questions> getSurveyQusetions()
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
        private RadioButton mYesBtn,mNoBtn;

        public MyViewHolder(View view)
        {
            super(view);
            mQuestions = view.findViewById(R.id.question_text);
            mQuestionsImage = view.findViewById(R.id.question_img);
            mTxtQuestionNo = view.findViewById(R.id.txt_question_no);
            mYesBtn = view.findViewById(R.id.yes_btn);
            mNoBtn = view.findViewById(R.id.no_btn);
            mYesBtn.setVisibility(View.GONE);
            mNoBtn.setVisibility(View.GONE);


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

        final Survey.SurveyData.FreeResponse.Questions surveyQuestions = surveyQuestionsList.get(position);
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
        link = surveyQuestions.getQuestionImg();
        if (link != null && !link.isEmpty()) {

            File f = new File(link);
            String mimetype= new MimetypesFileTypeMap().getContentType(f);
            String type = mimetype.split("/")[0];
            if(type.equals("image")) {

                int croppedImageHeight = UtilHelper.convertDpToPixel(context, 170);
                int croppedImageWidth = UtilHelper.getScreenWidth(context);
                Glide.with(context)
                        .load(surveyQuestions.getQuestionImg())
                        .apply(new RequestOptions().override(croppedImageWidth, croppedImageHeight))
                        .apply(new RequestOptions().centerCrop())
                        .into(holder.mQuestionsImage);
            }

        }
    }

    // updating image
    public void updateItem(int position, Survey.SurveyData.FreeResponse.Questions surveyQuestions)
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
