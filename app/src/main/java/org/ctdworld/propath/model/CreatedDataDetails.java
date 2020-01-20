package org.ctdworld.propath.model;


import java.io.Serializable;

// this modal class  is being used set user details and created or updated date for anything which has been created and saved on server
public class CreatedDataDetails extends User implements Serializable
{
    private String createdDate;
    private String updatedDate;


    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }




}
