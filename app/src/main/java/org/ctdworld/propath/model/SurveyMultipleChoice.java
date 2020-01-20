package org.ctdworld.propath.model;

import java.io.Serializable;
import java.util.List;

public class SurveyMultipleChoice implements Serializable {

    private String surveyTitle;
    private String surveyDesc;
    private String surveyNoOfQuestion;
    private String surveyQusetionType;
    private String surveyId;

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

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }


    public static class SurveyQuestions implements Serializable
    {
        private String surveyQuestion;
        private String questionImageUrl;
        private List<String> questionOptionsList;


        public String getSurveyQuestion() {
            return surveyQuestion;
        }

        public void setSurveyQuestion(String surveyQuestion) {
            this.surveyQuestion = surveyQuestion;
        }


        public String getQuestionImageUrl() {
            return questionImageUrl;
        }

        public void setQuestionImageUrl(String questionImageUrl) {
            this.questionImageUrl = questionImageUrl;
        }

        public List<String> getQuestionOptionsList() {
            return questionOptionsList;
        }

        public void setQuestionOptionsList(List<String> questionOptionsList) {
            this.questionOptionsList = questionOptionsList;
        }
    }

}
