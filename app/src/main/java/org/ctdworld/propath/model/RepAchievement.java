package org.ctdworld.propath.model;

import java.io.Serializable;

public class RepAchievement implements Serializable
{
    private String  repTeam;
    private String repLocation;
    private String repFromMonth;
    private String repFromYear;
    private String repToMonth;
    private String repToYear;
    private String repRole;
    private String repLink;

    public String getRepID() {
        return repID;
    }

    public void setRepID(String repID) {
        this.repID = repID;
    }

    private String repID;
    private String  repMediaBase64;
    String repMediaUrl;


    public String getRepTeam() {
        return repTeam;
    }

    public void setRepTeam(String repTeam) {
        this.repTeam = repTeam;
    }

    public String getRepLocation() {
        return repLocation;
    }

    public void setRepLocation(String repLocation) {
        this.repLocation = repLocation;
    }

    public String getRepFromMonth() {
        return repFromMonth;
    }

    public void setRepFromMonth(String repFromMonth) {
        this.repFromMonth = repFromMonth;
    }

    public String getRepFromYear() {
        return repFromYear;
    }

    public void setRepFromYear(String repFromYear) {
        this.repFromYear = repFromYear;
    }

    public String getRepToMonth() {
        return repToMonth;
    }

    public void setRepToMonth(String repToMonth) {
        this.repToMonth = repToMonth;
    }

    public String getRepToYear() {
        return repToYear;
    }

    public void setRepToYear(String repToYear) {
        this.repToYear = repToYear;
    }

    public String getRepRole() {
        return repRole;
    }

    public void setRepRole(String repRole) {
        this.repRole = repRole;
    }

    public String getRepLink() {
        return repLink;
    }

    public void setRepLink(String repLink) {
        this.repLink = repLink;
    }

    public String getRepMediaBase64() {
        return repMediaBase64;
    }

    public void setRepMediaBase64(String repMediaBase64) {
        this.repMediaBase64 = repMediaBase64;
    }

    public String getRepMediaUrl() {
        return repMediaUrl;
    }

    public void setRepMediaUrl(String repMediaUrl) {
        this.repMediaUrl = repMediaUrl;
    }
}
