package org.ctdworld.propath.model;

import java.io.Serializable;

public class SelfReview implements Serializable
{

    private String subject;
    private String grade;
    private String comments;

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getAssistance() {
        return assistance;
    }

    public void setAssistance(String assistance) {
        this.assistance = assistance;
    }

    private String date;
    private String athleteName;
    private String teacherID;
    private String athleteID;
    private String teacherName;
    private String qualification;
    private String userPicUrl;
    private String strengths;
    private String suggestions;
    private String improvements;
    private String assistance;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    private String subjectID;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    private String reviewId;

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
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

    public String getTeachername() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public SelfReview() {
    }

    public SelfReview(String subject, String grade) {
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
