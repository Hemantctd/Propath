package org.ctdworld.propath.model;

import java.io.Serializable;

public class User extends Profile implements Serializable
{
    private String userId;
    private String name;
    private String created_by;
    private String role;
    private String email;
    private String password;
    private String confirm_password;
    private String otp;
    private String roleId;
    private String status;
    private String activity;
    private String authToken;
    private String oldPassword;
    private String firebaseToken;

    private String userPicUrl;
    private String connectionRequestStatus;
    private String sports;

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;



    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getConnectionRequestStatus() {
        return connectionRequestStatus;
    }

    public void setConnectionRequestStatus(String connectionRequestStatus) {
        this.connectionRequestStatus = connectionRequestStatus;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String role) {
        this.roleId = role;
    }
}
