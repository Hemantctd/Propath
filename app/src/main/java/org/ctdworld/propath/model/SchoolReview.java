package org.ctdworld.propath.model;

import java.io.Serializable;

public class SchoolReview implements Serializable
{
    public static final String ACTION_ACCEPT_PROGRESS_REPORT = "1";
    public static final String ACTION_REJECT_PROGRESS_REPORT = "2";

    public static final String ACTION_ACCEPT_SCHOOL_REVIEW = "1";
    public static final String ACTION_REJECT_SCHOOL_REVIEW = "2";

    private String subject;
    private String grade;
    private String comments;
    private String date;
    private String athleteName;
    private String teacherID;
    private String athleteID;
    private String teacherName;
    private String userPicUrl;
    private String requestFromId;
    private String requestToId;
    private String requestStatus;
    private String reviewID;
    private String improvements;
    private String suggestions;
    private String assistance;
    private String strengths;
    private String qualification;

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getAssistance() {
        return assistance;
    }

    public void setAssistance(String assistance) {
        this.assistance = assistance;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    private String subjectID;

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getSchool_recview_counter() {
        return school_recview_counter;
    }

    public void setSchool_recview_counter(String school_recview_counter) {
        this.school_recview_counter = school_recview_counter;
    }

    private String school_recview_counter;

    public String getRequestFromId() {
        return requestFromId;
    }

    public void setRequestFromId(String requestFromId) {
        this.requestFromId = requestFromId;
    }

    public String getRequestToId() {
        return requestToId;
    }

    public void setRequestToId(String requestToId) {
        this.requestToId = requestToId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(String athleteID) {
        this.athleteID = athleteID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public SchoolReview() {
    }

    public SchoolReview(String subject, String grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}
