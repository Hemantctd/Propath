package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class SurveyQusetions implements Parcelable
{

    private String questions;
    private String answers;
    private String survey_id;
    private String survey_enable_type; private String survey_anonymous_type;
    private String surveyTitle;
    private String surveyDesc;
    private String survey_no_of_question;
    private String surveyQusetionType;
    private String user_id;
    private String isCompleted;
    private String hit_count;
    private String user_name;
    private String group_id;
    private List<List<SurveyQusetions>> oneObjectDataFromServer;



    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }




    public List<List<SurveyQusetions>> getOneObjectDataFromServer() {
        return oneObjectDataFromServer;
    }

    public void setOneObjectDataFromServer(List<List<SurveyQusetions>> oneObjectDataFromServer) {
        this.oneObjectDataFromServer = oneObjectDataFromServer;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getSurvey_enable_type() {
        return survey_enable_type;
    }

    public void setSurvey_enable_type(String survey_enable_type) {
        this.survey_enable_type = survey_enable_type;
    }

    public String getSurvey_anonymous_type() {
        return survey_anonymous_type;
    }

    public void setSurvey_anonymous_type(String survey_anonymous_type) {
        this.survey_anonymous_type = survey_anonymous_type;
    }



    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }


    public String getHit_count() {
        return hit_count;
    }

    public void setHit_count(String hit_count) {
        this.hit_count = hit_count;
    }


    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    public String getSurvey_no_of_question() {
        return survey_no_of_question;
    }

    public void setSurvey_no_of_question(String survey_no_of_question) {
        this.survey_no_of_question = survey_no_of_question;
    }

    public String getSurveyQusetionType() {
        return surveyQusetionType;
    }

    public void setSurveyQusetionType(String surveyQusetionType) {
        this.surveyQusetionType = surveyQusetionType;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }


    public SurveyQusetions()
    {
    }

    public SurveyQusetions(String questions)
    {
        this.questions = questions;
    }

    protected SurveyQusetions(Parcel in)
    {
        questions = in.readString();
    }

    public static final Creator<SurveyQusetions> CREATOR = new Creator<SurveyQusetions>() {
        @Override
        public SurveyQusetions createFromParcel(Parcel in) {
            return new SurveyQusetions(in);
        }

        @Override
        public SurveyQusetions[] newArray(int size) {
            return new SurveyQusetions[size];
        }
    };

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(questions);
    }
}
