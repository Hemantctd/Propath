package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivitySurveyFreeResponseSubmit;
import org.ctdworld.propath.activity.ActivitySurveyMultipleChoiceSubmit;
import org.ctdworld.propath.activity.ActivitySurveyYesNoSubmit;
import org.ctdworld.propath.model.SurveyQusetions;

import java.util.ArrayList;
import java.util.List;

public class AdapterGetAllSharedSurvey extends RecyclerView.Adapter<AdapterGetAllSharedSurvey.MyViewHolder> {
    private static final String TAG = AdapterGetAllSharedSurvey.class.getSimpleName();
    private Context context;
    private List<List<List<SurveyQusetions>>> mSurveyList;
    private Intent intent;
    private SurveyQusetions mSurveyCommonData;


    // constructor
    public AdapterGetAllSharedSurvey(Context context) {
        this.context = context;
        this.mSurveyList = new ArrayList<>();

    }

    // update the survey
    public void updateSurvey(SurveyQusetions surveyQuestions, List<List<List<SurveyQusetions>>> surveyFinalList) {
        //  Log.d(TAG,"DESC : " + mSurveyCommonData.getSurveyTitle());
        //  this.mSurveyCommonData = surveyQuestions;
      // this.mSurveyList = surveyFinalList;

        // mSurveyList.add(mSurveyQuestions);

        //notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_get_shared_survey, parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_adapter_item);
        holder.layout.setAnimation(animation);



        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0 || position == 1) {
                    context.startActivity(new Intent(context, ActivitySurveyFreeResponseSubmit.class));
                }
                else if (position == 2 || position == 3) {
                    context.startActivity(new Intent(context, ActivitySurveyMultipleChoiceSubmit.class));
                }
                else {
                    context.startActivity(new Intent(context, ActivitySurveyYesNoSubmit.class));

                }
            }
        });
      /*  final List<List<SurveyQusetions>> listOneObject = mSurveyList.get(position);


        Log.i(TAG, "one object *************************************************************************************************************");
        Log.i(TAG, "listOneObject size = " + listOneObject.size());


        final List<SurveyQusetions> listCommonData = listOneObject.get(0);
        final SurveyQusetions surveyCommon = listCommonData.get(0); // get from 0 because there is only object


        final List<SurveyQusetions> listQuestions = listOneObject.get(1);
        Log.i(TAG, "listQuestions  " + listQuestions.size());


        if (surveyCommon != null) {
            if (surveyCommon.getSurvey_anonymous_type() != null) {
                Log.e(TAG, "printing anonymous");

                if (surveyCommon.getSurvey_anonymous_type().equals("0")) {
                    holder.survey_type.setText("name : anonymous");
                } else {
                    holder.survey_type.setText("name : " + surveyCommon.getUser_name());
                }
            } else
                Log.e(TAG, "getSurvey_anonymous_type is null");


        } else
            Log.e(TAG, "mSurveyQuestions is null");

        holder.survey_title.setText(surveyCommon.getSurveyTitle());
        holder.survey_list_date.setText("06-Jun-19 05:27 PM");

        holder.survey_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SurveyQusetions sendSurvey = new SurveyQusetions();
//                sendSurvey.setOneObjectDataFromServer(listOneObject);
                if (surveyCommon.getIsCompleted().equals("0")) {
                    Intent intent = new Intent(context, ActivitySubmitSurvey.class);
                    intent.putExtra(ActivitySubmitSurvey.SURVEY_TITLE, surveyCommon.getSurveyTitle());
                    intent.putExtra(ActivitySubmitSurvey.SURVEY_QUESTION_TYPE, surveyCommon.getSurveyQusetionType());
                    intent.putExtra(ActivitySubmitSurvey.SURVEY_NO_OF_QUESTIONS, surveyCommon.getSurvey_no_of_question());
                    intent.putExtra(ActivitySubmitSurvey.SURVEY_DESCRIPTION, surveyCommon.getSurveyDesc());
                    intent.putExtra(ActivitySubmitSurvey.SURVEY_ID, surveyCommon.getSurvey_id());
                    intent.putParcelableArrayListExtra(ActivitySubmitSurvey.SURVEY_QUESTION_LIST, (ArrayList<? extends Parcelable>) listQuestions);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ActivityViewSubmitSurvey.class);
                    intent.putExtra(ActivityViewSubmitSurvey.SURVEY_ID, surveyCommon.getSurvey_id());
                    context.startActivity(intent);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView survey_type, survey_title, survey_list_date;
        RelativeLayout survey_layout;
        View layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            survey_type = view.findViewById(R.id.survey_type);
            survey_title = view.findViewById(R.id.survey_title);
            survey_layout = view.findViewById(R.id.survey_layout);
            survey_list_date = view.findViewById(R.id.survey_list_date);
        }
    }
}