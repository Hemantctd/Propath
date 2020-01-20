package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Request extends Response implements Serializable
{
    // # notification types
    public static final int NOTIFICATION_TYPE_REQUEST = 1;
    public static final int NOTIFICATION_TYPE_SIMPLE_MESSAGE = 2;



    // these are response type to respond request, one of these response may be used to respond to request
    public static final String REQUEST_STATUS_PENDING = "1";
    public static final String REQUEST_STATUS_ACCEPT = "2";
    public static final String REQUEST_STATUS_REJECT = "0";



    // these are request types, to send and respond to request one type of these will have to be used.
    public static final String REQUEST_FOR_COACH_FEEDBACK = "Coach_feedback";
    public static final String REQUEST_FOR_TEACHER_GIVE_REVIEW = "Byteacher_athlete_review";
    public static final String REQUEST_FOR_EDUCATION = "Education";
    public static final String REQUEST_FOR_FRIEND = "Friend_request";
    public static final String REQUEST_FOR_SELF_REVIEW = "Access_self_review";
    public static final String REQUEST_FOR_CAREER_PLAN_VIEW = "view career plan";  // value is to be changed it's just dummy value
    public static final String REQUEST_FOR_COACH_SELF_REVIEW = "view_coach_self_review";



    @SerializedName("message")
    private List<Data> requestList;


    public List<Data> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Data> requestList) {
        this.requestList = requestList;
    }

    // contains all data of request
    public static class Data implements Serializable
    {
        @SerializedName("id")
        private String requestId;
        @SerializedName("user_id")
        private String requestFromUserId;
        @SerializedName("to_id")
        private String requestToUserId;
        @SerializedName("message")
        private String requestMessage;
        @SerializedName("request_for")
        private String requestFor;
        @SerializedName("status")
        private String requestStatus = REQUEST_STATUS_REJECT;   // request status, by default it's rejected (request can be sent)
        @SerializedName("read_status")
        private String requestReadStatus;
        @SerializedName("date_time")
        private String requestDateTime;
        @SerializedName("profile_image")
        private String requestFromProfileImage;
        @SerializedName("user_name")
        private String requestFromUserName;    // request from


        // # for notification
        private int notificationId;
        private int notificationType;






        public int getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(int notificationId) {
            this.notificationId = notificationId;
        }

        public int getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(int notificationType) {
            this.notificationType = notificationType;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getRequestFromUserId() {
            return requestFromUserId;
        }

        public void setRequestFromUserId(String requestFromUserId) {
            this.requestFromUserId = requestFromUserId;
        }

        public String getRequestToUserId() {
            return requestToUserId;
        }

        public void setRequestToUserId(String requestToUserId) {
            this.requestToUserId = requestToUserId;
        }

        public String getRequestMessage() {
            return requestMessage;
        }

        public void setRequestMessage(String requestMessage) {
            this.requestMessage = requestMessage;
        }

        public String getRequestFor() {
            return requestFor;
        }

        public void setRequestFor(String requestFor) {
            this.requestFor = requestFor;
        }

        public String getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(String requestStatus) {
            this.requestStatus = requestStatus;
        }

        public String getRequestReadStatus() {
            return requestReadStatus;
        }

        public void setRequestReadStatus(String requestReadStatus) {
            this.requestReadStatus = requestReadStatus;
        }

        public String getRequestDateTime() {
            return requestDateTime;
        }

        public void setRequestDateTime(String requestDateTime) {
            this.requestDateTime = requestDateTime;
        }

        public String getRequestFromProfileImage() {
            return requestFromProfileImage;
        }

        public void setRequestFromProfileImage(String requestFromProfileImage) {
            this.requestFromProfileImage = requestFromProfileImage;
        }

        public String getRequestFromUserName() {
            return requestFromUserName;
        }

        public void setRequestFromUserName(String requestFromUserName) {
            this.requestFromUserName = requestFromUserName;
        }
    }
}
