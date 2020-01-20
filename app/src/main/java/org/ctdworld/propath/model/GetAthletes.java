package org.ctdworld.propath.model;

import java.io.Serializable;

public class GetAthletes implements Serializable {
    String userId;
    String name;
    String email;
    String roleId;
    String status;
    String activity;
    String authToken;
    String schoolreviewed;
    String userPicUrl;
    String requestStatus;
    String school_recview_counter;

    public String getCoach_feedback_counter() {
        return coach_feedback_counter;
    }

    public void setCoach_feedback_counter(String coach_feedback_counter) {
        this.coach_feedback_counter = coach_feedback_counter;
    }

    String coach_feedback_counter;

    public String getSchool_recview_counter() {
        return school_recview_counter;
    }

    public void setSchool_recview_counter(String school_recview_counter) {
        this.school_recview_counter = school_recview_counter;
    }


    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getSchoolreviewed() {
        return schoolreviewed;
    }

    public void setSchoolreviewed(String schoolreviewed) {
        this.schoolreviewed = schoolreviewed;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }



}
