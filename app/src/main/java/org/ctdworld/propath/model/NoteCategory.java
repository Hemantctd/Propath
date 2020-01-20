package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


// contains list of Notes, note contains category data
public class NoteCategory extends Response implements Serializable
{

    // private String userId;
    @SerializedName("id")
    private String categoryId;
    @SerializedName("category")
    private String categoryName;
    @SerializedName("datetime")   // contains category datetime
    private String categoryDateTime;


    @SerializedName("message")
    private List<NoteCategory> noteCategoryList;


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDateTime() {
        return categoryDateTime;
    }

    public void setCategoryDateTime(String categoryDateTime) {
        this.categoryDateTime = categoryDateTime;
    }

    public List<NoteCategory> getNoteCategoryList() {
        return noteCategoryList;
    }

    public void setNoteCategoryList(List<NoteCategory> noteCategoryList) {
        this.noteCategoryList = noteCategoryList;
    }
}
