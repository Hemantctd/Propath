package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class CareerPlan extends Response
{

   /* @SerializedName("res")
    private String responseStatus;   // server response status*/
    @SerializedName("data")
    private List<CareerUser> careerUserList;   // user list who have created career plan, each user contains multiple career plan



    /*public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }*/

    public List<CareerUser> getCareerUserList() {
        return careerUserList;
    }

    public void setCareerUserList(List<CareerUser> careerUserList) {
        this.careerUserList = careerUserList;
    }






    /*################################################################## CareerUser class ################################################################################*/

    // user who has created career plan, it contains user details and career plan data
    public static class CareerUser implements Serializable
    {
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_role")
        private String userRoleId;
        @SerializedName("profile_image")
        private String userPicUrl;
        @SerializedName("request_status")
        private String permissionRequestStatus = Request.REQUEST_STATUS_REJECT;  // default value is reject which means user can send request
        @SerializedName("career_plan")
        private List<CareerData> careerDataList;  // to set and get career plan list
        private CareerData careerData = new CareerData();  // to set and get single career plan data




        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserRoleId() {
            return userRoleId;
        }

        public void setUserRoleId(String userRoleId) {
            this.userRoleId = userRoleId;
        }

        public String getUserPicUrl() {
            return userPicUrl;
        }

        public void setUserPicUrl(String userPicUrl) {
            this.userPicUrl = userPicUrl;
        }

        public String getPermissionRequestStatus() {
            return permissionRequestStatus;
        }

        public void setPermissionRequestStatus(String permissionRequestStatus) {
            this.permissionRequestStatus = permissionRequestStatus;
        }

        public List<CareerData> getCareerDataList() {
            return careerDataList;
        }

        public void setCareerDataList(List<CareerData> careerDataList) {
            this.careerDataList = careerDataList;
        }

        public CareerData getCareerData() {
            return careerData;
        }

        public void setCareerData(CareerData careerData) {
            this.careerData = careerData;
        }
    }





    /*################################################################## CareerData class ################################################################################*/

    // # career plan data
    public static class CareerData implements Serializable
    {
        @SerializedName("carrer_id")
        private String careerId;
        @SerializedName("athlete_id")
        private String athleteId;
        @SerializedName("future_career")
        private String futureCareer;
        @SerializedName("location")
        private String locationOrganization;
        @SerializedName("scholarship")
        private String scholarShip;
        @SerializedName("internship")
        private String internShip;
        @SerializedName("apprenticeship")
        private String apprenticeship;
        @SerializedName("school_subject")
        private String subjectsNeeded;
        @SerializedName("email")
        private String email;
        @SerializedName("phone_number")
        private String phoneNumber;
        @SerializedName("create_date")
        private String createdDate;
        /*private String athleteName;
        private String athletePicUrl;*/


        public String getAthleteId() {
            return athleteId;
        }

        public void setAthleteId(String athleteId) {
            this.athleteId = athleteId;
        }


        public String getCareerId() {
            return careerId;
        }

        public void setCareerId(String careerId) {
            this.careerId = careerId;
        }

        public String getFutureCareer() {
            return futureCareer;
        }

        public void setFutureCareer(String futureCareer) {
            this.futureCareer = futureCareer;
        }

        public String getLocationOrganization() {
            return locationOrganization;
        }

        public void setLocationOrganization(String locationOrganization) {
            this.locationOrganization = locationOrganization;
        }

        public String getScholarShip() {
            return scholarShip;
        }

        public void setScholarShip(String scholarShip) {
            this.scholarShip = scholarShip;
        }

        public String getSubjectsNeeded() {
            return subjectsNeeded;
        }

        public String getInternShip() {
            return internShip;
        }

        public void setInternShip(String internShip) {
            this.internShip = internShip;
        }

        public String getApprenticeship() {
            return apprenticeship;
        }

        public void setApprenticeship(String apprenticeship) {
            this.apprenticeship = apprenticeship;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setSubjectsNeeded(String subjectsNeeded) {
            this.subjectsNeeded = subjectsNeeded;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

}