package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyFreeResponse implements Serializable
{
    @SerializedName("id")
    private String survey_id;
    @SerializedName("status")
    private String survey_enable_type;
    @SerializedName("show_anonymous")
    private String survey_anonymous_type;
    @SerializedName("title")
    private String surveyTitle;
    @SerializedName("description")
    private String surveyDesc;
    @SerializedName("question_no")
    private String surveyNoOfQuestion;
    @SerializedName("question_type")
    private String surveyQusetionType;
    @SerializedName("user_id")
    private String user_id;
    private String isCompleted;
    @SerializedName("hit_count")
    private String hit_count;
    private String user_name;
    private String group_id;

    @SerializedName("Survey")
    private ArrayList<SurveyFreeResponse.SurveyQuestions> listQuestions;



    public ArrayList<SurveyFreeResponse.SurveyQuestions> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(ArrayList<SurveyFreeResponse.SurveyQuestions> listQuestions) {
        this.listQuestions = listQuestions;
    }

    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
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

    public String getSurveyNoOfQuestion() {
        return surveyNoOfQuestion;
    }

    public void setSurveyNoOfQuestion(String surveyNoOfQuestion) {
        this.surveyNoOfQuestion = surveyNoOfQuestion;
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

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getHit_count() {
        return hit_count;
    }

    public void setHit_count(String hit_count) {
        this.hit_count = hit_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }









    public static class SurveyQuestions implements Serializable
    {

        @SerializedName("question_id")
        private String questionID;
        @SerializedName("survey_question")
        private String surveyQuestion;
        private String answer;

        public String getQuestionID() {
            return questionID;
        }

        public void setQuestionID(String questionID) {
            this.questionID = questionID;
        }




        public String getSurveyQuestion() {
            return surveyQuestion;
        }

        public void setSurveyQuestion(String surveyQuestion) {
            this.surveyQuestion = surveyQuestion;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

    }

}
