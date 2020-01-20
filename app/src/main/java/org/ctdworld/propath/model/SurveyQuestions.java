package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class SurveyQuestions implements Serializable
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

    public List<String> getQuestionOptionsList() {
        return questionOptionsList;
    }

    public void setQuestionOptionsList(List<String> questionOptionsList) {
        this.questionOptionsList = questionOptionsList;
    }

    private List<String> questionOptionsList;
    public SurveyQuestions() {
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    private String isCompleted;

    private List<List<SurveyQuestions>> oneObjectDataFromServer;


    public List<List<SurveyQuestions>> getOneObjectDataFromServer() {
        return oneObjectDataFromServer;
    }

    public void setOneObjectDataFromServer(List<List<SurveyQuestions>> oneObjectDataFromServer) {
        this.oneObjectDataFromServer = oneObjectDataFromServer;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private String user_name;

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

    private String group_id;

    public String getHit_count() {
        return hit_count;
    }

    public void setHit_count(String hit_count) {
        this.hit_count = hit_count;
    }

    private String hit_count;

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



    public SurveyQuestions(String questions) {
        this.questions = questions;
    }

    protected SurveyQuestions(Parcel in) {
        questions = in.readString();
    }


    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }


}
