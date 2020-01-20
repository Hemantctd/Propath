package org.ctdworld.propath.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Survey implements Serializable
{
    // to check if questionsList is enable or not.
    public static final int SURVEY_ENABLE = 0;
    public static final int SURVEY_DISABLE = 1;

    // to show or hide creator name.
    public static final int SHOW_CREATOR_NAME = 1;
    public static final int HIDE_CREATOR_NAME = 0;
    
    

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private SurveyData surveyData;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public SurveyData getSurveyData() {
        return surveyData;
    }

    public void setSurveyData(SurveyData surveyData) {
        this.surveyData = surveyData;
    }



    
    // message class contains all types of questionsList ( free response, multiple choice, yes no type ) data.
    public static class SurveyData implements Serializable
    {

        // temporary class
        public static class CommonData implements Serializable
        {
            @SerializedName("id")
            @Expose
            private String id; // this is questionsList id.
            @SerializedName("user_id")
            @Expose
            private String userId; // id of the user who is creted the questionsList.
            @SerializedName("title")
            @Expose
            private String title; // questionsList title.
            @SerializedName("description")
            @Expose
            private String description; // questionsList description.
            @SerializedName("question_type")
            @Expose
            private String questionType; // questionsList questionType ( free response ).
            @SerializedName("question_no")
            @Expose
            private String questionNo; // total no of questions
            @SerializedName("hit_count")
            @Expose
            private String hitCount; // it show total no. of people who filled the questionsList.
            @SerializedName("status")
            @Expose
            private String status; // it shows questionsList is enabled or not ( 0 = enable, 1 = disable ).
            @SerializedName("show_anonymous")
            @Expose
            private String showAnonymous; // it show or hide creator name  ( 0 = hide, 1 = show )
            @SerializedName("date_time")
            private String dateTime;
            int surveyType ;

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getQuestionType() {
                return questionType;
            }

            public void setQuestionType(String questionType) {
                this.questionType = questionType;
            }

            public String getQuestionNo() {
                return questionNo;
            }

            public void setQuestionNo(String questionNo) {
                this.questionNo = questionNo;
            }

            public String getHitCount() {
                return hitCount;
            }

            public void setHitCount(String hitCount) {
                this.hitCount = hitCount;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getShowAnonymous() {
                return showAnonymous;
            }

            public void setShowAnonymous(String showAnonymous) {
                this.showAnonymous = showAnonymous;
            }

            public int getSurveyType() {
                return surveyType;
            }

            public void setSurveyType(int surveyType) {
                this.surveyType = surveyType;
            }
        }






      
        @SerializedName("Survey_free_responce_data")
        @Expose
        private List<FreeResponse> freeResponseList = null;

        public List<RatingScale> getRatingScaleList() {
            return ratingScaleList;
        }

        public void setRatingScaleList(List<RatingScale> ratingScaleList) {
            this.ratingScaleList = ratingScaleList;
        }

        @SerializedName("Survey_yes_no_data")
        @Expose
        private List<YesNo> yesNoList = null;

        private List<RatingScale> ratingScaleList = null;
        @SerializedName("Survey_multiple_data")
        @Expose
        private List<MultipleChoice> multipleChoiceList = null;

        public List<FreeResponse> getFreeResponseList() {
            return freeResponseList;
        }

        public void setFreeResponseList(List<FreeResponse> freeResponseList) {
            this.freeResponseList = freeResponseList;
        }

        public List<YesNo> getYesNoList() {
            return yesNoList;
        }

        public void setYesNoList(List<YesNo> yesNoList) {
            this.yesNoList = yesNoList;
        }

        public List<MultipleChoice> getMultipleChoiceList() {
            return multipleChoiceList;
        }

        public void setMultipleChoiceList(List<MultipleChoice> multipleChoiceList) {
            this.multipleChoiceList = multipleChoiceList;
        }






        // FreeResponse class contains free response type questionsList data.
        public static class FreeResponse extends CommonData implements Serializable
        {

            @SerializedName("Survey")
            @Expose
            private List<Questions> questionsList = null; //it contains all  free response type questions.

            public List<Questions> getQuestionsList() {
                return questionsList;
            }

            public void setQuestionsList(List<Questions> questionsList) {
                this.questionsList = questionsList;
            }

            // Questions class contains free response questions.

            public static class Questions implements Serializable
            {

                @SerializedName("question_id")
                @Expose
                private String questionId;
                @SerializedName("survey_question")
                @Expose
                private String question;

                @SerializedName("survey_image")
                private String questionImg;

                public String getQuestionImg() {
                    return questionImg;
                }

                public void setQuestionImg(String questionImg) {
                    this.questionImg = questionImg;
                }

                public String getQuestionId() {
                    return questionId;
                }

                public void setQuestionId(String questionId) {
                    this.questionId = questionId;
                }

                public String getQuestion() {
                    return question;
                }

                public void setQuestion(String question) {
                    this.question = question;
                }

            }

        }






        // MultipleChoice class contains multiple choice type questionsList data.

        public static class MultipleChoice extends CommonData implements Serializable
        {


            @SerializedName("Survey_multiple")
            @Expose
            private List<Questions> surveyMultipleQuestionList = null; //it contains all  multiple choice type questions.

            public List<Questions> getSurveyMultipleQuestionList() {
                return surveyMultipleQuestionList;
            }

            public void setSurveyMultipleQuestionList(List<Questions> surveyMultipleQuestionList) {
                this.surveyMultipleQuestionList = surveyMultipleQuestionList;
            }


            // Questions class contains multiple choice questions.

            public static class Questions implements Serializable
            {

                @SerializedName("question_id")
                @Expose
                private String questionId;
                @SerializedName("survey_question")
                @Expose
                private String question;

              /*  public List<String> getQuestionOption() {
                    return questionOption;
                }

                public void setQuestionOption(List<String> questionOption) {
                    this.questionOption = questionOption;
                }*/

               /* @SerializedName("question_option")
                @Expose
                private List<String> questionOption;*/
                @SerializedName("filename")
                @Expose
                private String questionImage;
                @SerializedName("link")
                @Expose
                private String questionImageLink; // not confirmed

                @SerializedName("question_option")
                @Expose
                private List<String> optionList = new ArrayList<>();

                public List<String> getOptionList() {
                    return optionList;
                }


                public Questions() {
                    optionList.add("");
                    optionList.add("");
                    optionList.add("");
                    optionList.add("");
                    optionList.add("");
                }

                public void setOptionList(List<String> optionList) {
                    this.optionList = optionList;
                }

                public String getQuestionId() {
                    return questionId;
                }

                public void setQuestionId(String questionId) {
                    this.questionId = questionId;
                }

                public String getQuestion() {
                    return question;
                }

                public void setQuestion(String question) {
                    this.question = question;
                }

               /* public String getQuestionOption() {
                    return questionOption;
                }

                public void setQuestionOption(String questionOption) {
                    this.questionOption = questionOption;
                }*/

                public String getQuestionImage() {
                    return questionImage;
                }

                public void setQuestionImage(String questionImage) {
                    this.questionImage = questionImage;
                }

                public String getQuestionImageLink() {
                    return questionImageLink;
                }

                public void setQuestionImageLink(String questionImageLink) {
                    this.questionImageLink = questionImageLink;
                }

            }

        }













        // YesNo class contains yes no type questionsList data.

        public static class YesNo extends CommonData implements Serializable
        {

            @SerializedName("Survey_yes_no")
            @Expose
            private List<Questions> surveyYesNoQuestionList = null;  //it contains all yes/no type questions.

            public List<Questions> getSurveyYesNoQuestionList() {
                return surveyYesNoQuestionList;
            }

            public void setSurveyYesNoQuestionList(List<Questions> surveyYesNoQuestionList) {
                this.surveyYesNoQuestionList = surveyYesNoQuestionList;
            }

            // Questions class contains yes no questions.

            public static class Questions implements Serializable
            {

                @SerializedName("question_id")
                @Expose
                private String questionId;
                @SerializedName("survey_question")
                @Expose
                private String question;
                @SerializedName("questionImage")
                @Expose
                private String filename;
                @SerializedName("link")
                @Expose
                private String questionImageLink;

                public String getQuestionId() {
                    return questionId;
                }

                public void setQuestionId(String questionId) {
                    this.questionId = questionId;
                }

                public String getQuestion() {
                    return question;
                }

                public void setQuestion(String question) {
                    this.question = question;
                }

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public String getQuestionImageLink() {
                    return questionImageLink;
                }

                public void setQuestionImageLink(String questionImageLink) {
                    this.questionImageLink = questionImageLink;
                }

            }

        }





        // YesNo class contains yes no type questionsList data.

        public static class RatingScale extends CommonData implements Serializable
        {

            private String ratingScaleRange;
            private String ratingScaleFormat;
            private String ratingScaleGraphicsItem;

            public String getRatingScaleGraphicsItem() {
                return ratingScaleGraphicsItem;
            }

            public void setRatingScaleGraphicsItem(String ratingScaleGraphicsItem) {
                this.ratingScaleGraphicsItem = ratingScaleGraphicsItem;
            }



            public String getRatingScaleRange() {
                return ratingScaleRange;
            }

            public void setRatingScaleRange(String ratingScaleRange) {
                this.ratingScaleRange = ratingScaleRange;
            }

            public String getRatingScaleFormat() {
                return ratingScaleFormat;
            }

            public void setRatingScaleFormat(String ratingScaleFormat) {
                this.ratingScaleFormat = ratingScaleFormat;
            }

            @SerializedName("Survey_rating_scale")
            @Expose
            private List<Questions> surveyRatingScaleQuestionList = null;  //it contains all yes/no type questions.

            public List<Questions> getSurveRatingScaleQuestionList() {
                return surveyRatingScaleQuestionList;
            }

            public void setSurveyRatingScaleQuestionList(List<Questions> surveyRatingScaleQuestionList) {
                this.surveyRatingScaleQuestionList = surveyRatingScaleQuestionList;
            }

            // Questions class contains yes no questions.

            public static class Questions implements Serializable
            {

                @SerializedName("question_id")
                @Expose
                private String questionId;
                @SerializedName("survey_question")
                @Expose
                private String question;
                @SerializedName("questionImage")
                @Expose
                private String filename;
                @SerializedName("link")
                @Expose
                private String questionImageLink;


                public String getQuestionId() {
                    return questionId;
                }

                public void setQuestionId(String questionId) {
                    this.questionId = questionId;
                }

                public String getQuestion() {
                    return question;
                }

                public void setQuestion(String question) {
                    this.question = question;
                }

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public String getQuestionImageLink() {
                    return questionImageLink;
                }

                public void setQuestionImageLink(String questionImageLink) {
                    this.questionImageLink = questionImageLink;
                }

            }

        }



    }



















}