package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Notes implements Serializable
{
    // final string to know who has created file
  //  public static final String CREATED_BY_SELF = "created by self";
  //  public static final String CREATED_BY_OTHER = "created by other";

    public static final String DEFAULT_CATEGORY_UNCATEGORISED = "0";

    private int positionInAdapter;
    private boolean isNoteEdited;
    private boolean isNoteDeleted;
    private boolean isSelfCreated;

   // private String userId;
    private String createdByUserId;
    private String noteId;
    private String title;
    private String description;
    private String createdByUserName="";
    private String createdDateTime="";
    private String updatedDateTime="";
    @SerializedName("role_name")
    private String creatorRoleName;
    // private String createdBy;
    private String categoryId, categoryName;
    @SerializedName("user_pic")
    public String createdByUserPic;

    public String getCreatedByUserPic() {
        return createdByUserPic;
    }

    public void setCreatedByUserPic(String createdByUserPic) {
        this.createdByUserPic = createdByUserPic;
    }

    private Media media;
    private Document document;
    private Link link;

    private List<Media> listMedia;
    private List<Document> listDocument;
    private List<Link> listLinks;


    public int getPositionInAdapter() {
        return positionInAdapter;
    }

    public void setPositionInAdapter(int positionInAdapter) {
        this.positionInAdapter = positionInAdapter;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getCreatorRoleName() {
        return creatorRoleName;
    }

    public void setCreatorRoleName(String creatorRoleName) {
        this.creatorRoleName = creatorRoleName;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public boolean isSelfCreated() {
        return isSelfCreated;
    }

    public void setSelfCreated(boolean selfCreated) {
        isSelfCreated = selfCreated;
    }

    public boolean isNoteEdited() {
        return isNoteEdited;
    }

    public void setNoteEdited(boolean noteEdited) {
        isNoteEdited = noteEdited;
    }

    public boolean isNoteDeleted() {
        return isNoteDeleted;
    }

    public void setNoteDeleted(boolean noteDeleted) {
        isNoteDeleted = noteDeleted;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public List<Media> getListMedia() {
        return listMedia;
    }

    public void setListMedia(List<Media> listMedia) {
        this.listMedia = listMedia;
    }

    public List<Document> getListDocument() {
        return listDocument;
    }

    public void setListDocument(List<Document> listDocument) {
        this.listDocument = listDocument;
    }

    public List<Link> getListLinks() {
        return listLinks;
    }

    public void setListLinks(List<Link> listLinks) {
        this.listLinks = listLinks;
    }

  /*  public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }*/

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

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
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


    //# this class is being used to set media data
    public static class Media implements  Serializable
    {
        String mediaId, mediaUrl;
        // to identify whether media has come from server or has been added new in adapter. it will be used while saving new note
        // or editing note
        boolean isMediaFromServer;  // # it must be used otherwise there will be problem while saving edited note


        public Media()
        {

        }

        public Media(String mediaId, String mediaUrl, boolean isMediaFromServer) {
            this.mediaId = mediaId;
            this.mediaUrl = mediaUrl;
            this.isMediaFromServer = isMediaFromServer;
        }


        public boolean isMediaFromServer() {
            return isMediaFromServer;
        }

        public void setMediaFromServer(boolean mediaFromServer) {
            isMediaFromServer = mediaFromServer;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }
    }


    //# this class is being used to set document data
    public static class Document implements  Serializable
    {
        private String documentId, documentUrl, documentTitle;
        // to identify whether document has come from server or has been added new in adapter. it will be used while saving new note
        // or editing note
        boolean isDocumentFromServer;

        public Document() { }

        public Document(String docId, String docUrl, String docTitle, boolean isDocumentFromServer) {
            this.documentId = docId;
            this.documentUrl = docUrl;
            this.documentTitle = docTitle;
            this.isDocumentFromServer = isDocumentFromServer;  // # it must be used otherwise there will be problem while saving edited note
        }

        public boolean isDocumentFromServer() {
            return isDocumentFromServer;
        }

        public void setDocumentFromServer(boolean documentFromServer) {
            isDocumentFromServer = documentFromServer;
        }

        public String getDocumentTitle() {
            return documentTitle;
        }

        public void setDocumentTitle(String documenTitle) {
            this.documentTitle = documenTitle;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getDocumentUrl() {
            return documentUrl;
        }

        public void setDocumentUrl(String documentUrl) {
            this.documentUrl = documentUrl;
        }
    }

    //# this class is being used to set links
    public static class Link implements  Serializable
    {
        private String linkId, linkUrl, linkTitle;
        // to identify whether Link has come from server or has been added new in adapter. it will be used while saving new note
        // or editing note
        boolean isLinkFromServer;   // # it must be used otherwise there will be problem while saving edited note

        // empty constructor
        public Link() {
        }

        // constructor
        public Link(String linkId, String linkUrl, boolean isLinkFromServer) {
            this.linkId = linkId;
            this.linkUrl = linkUrl;
            this.isLinkFromServer = isLinkFromServer;
        }

        public boolean isLinkFromServer() {
            return isLinkFromServer;
        }

        public void setLinkFromServer(boolean linkFromServer) {
            isLinkFromServer = linkFromServer;
        }

        public String getLinkId() {
            return linkId;
        }

        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getLinkTitle() {
            return linkTitle;
        }

        public void setLinkTitle(String linkTitle) {
            this.linkTitle = linkTitle;
        }
    }
}
