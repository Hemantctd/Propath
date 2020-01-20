package org.ctdworld.propath.model;

import java.io.Serializable;

public class Nutrition implements Serializable {

    private String userId;
    private String commentStatus;
    private String title;
    private String file;
    private String post_id;
    private String media_url;
    private String like_count;
    private String share_with;
    private String post_share_by;
    private String post_share_text;
    private String status;
    private String date_time;
    private String user_name;
    private String user_profile_image;
    private String like;

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_by_user() {
        return comment_by_user;
    }

    public void setComment_by_user(String comment_by_user) {
        this.comment_by_user = comment_by_user;
    }

    public String getComment_by_image() {
        return comment_by_image;
    }

    public void setComment_by_image(String comment_by_image) {
        this.comment_by_image = comment_by_image;
    }

    public String getComments_id() {
        return comments_id;
    }

    public void setComments_id(String comments_id) {
        this.comments_id = comments_id;
    }

    public String getComment_by_user_id() {
        return comment_by_user_id;
    }

    public void setComment_by_user_id(String comment_by_user_id) {
        this.comment_by_user_id = comment_by_user_id;
    }

    public String getComment_by_user_name() {
        return comment_by_user_name;
    }

    public void setComment_by_user_name(String comment_by_user_name) {
        this.comment_by_user_name = comment_by_user_name;
    }

    private String comment_date;
    private String comment_by_user;
    private String comment_by_image;
    private String comments_id;
    private String comment_by_user_id;
    private String comment_by_user_name;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String comments;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getShare_with() {
        return share_with;
    }

    public void setShare_with(String share_with) {
        this.share_with = share_with;
    }

    public String getPost_share_by() {
        return post_share_by;
    }

    public void setPost_share_by(String post_share_by) {
        this.post_share_by = post_share_by;
    }

    public String getPost_share_text() {
        return post_share_text;
    }

    public void setPost_share_text(String post_share_text) {
        this.post_share_text = post_share_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    private String comment_count;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
