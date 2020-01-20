package org.ctdworld.propath.model;

import java.io.Serializable;

public class ContactAndGroup implements Serializable
{
    public static final String TYPE_FRIEND = "Friend";
    public static final String TYPE_GROUP = "Group";

    private String id, type, name, picUrl;


    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
