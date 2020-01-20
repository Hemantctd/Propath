package org.ctdworld.propath.model;

public class FAQ
{
    private String title, description;

    public FAQ(String title, String description) {
        this.title = title;        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
