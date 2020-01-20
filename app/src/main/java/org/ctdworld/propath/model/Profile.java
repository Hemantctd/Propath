package org.ctdworld.propath.model;

import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;

public class Profile implements Serializable
{
    private String id;
    private String name;
    private String address;
    private String athleteBio;
    private String sportName;
    private String profilePicBase64;
    private Bitmap picBitmap;
    private String highlightReel;
    private String roleName;


    public String getHighlightReel() {
        return highlightReel;
    }

    public void setHighlightReel(String highlightReel) {
        this.highlightReel = highlightReel;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    private String playlist;


    private String  repTeam, repLocation, repFromMonth, repFromYear, repToMonth, repToYear, repRole, repLink;
    private String  repMediaBase64;
    String profilePicUrl, repMediaUrl;

    private String picUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAthleteBio() {
        return athleteBio;
    }

    public void setAthleteBio(String athleteBio) {
        this.athleteBio = athleteBio;
    }

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

    public Bitmap getProfilePicBitmap() {
        return picBitmap;
    }

    public void setProfilePicBitmap(Bitmap picBitmap) {
        this.picBitmap = picBitmap;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }

    public void setProfilePicBase64(String picBase64) {
        this.profilePicBase64 = picBase64;
    }

    public String getRepMediaBase64() {
        return repMediaBase64;
    }

    public void setRepMediaBase64(String repMediaBase64) {
        this.repMediaBase64 = repMediaBase64;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
