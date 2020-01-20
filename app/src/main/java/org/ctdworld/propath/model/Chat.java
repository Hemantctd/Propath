package org.ctdworld.propath.model;

import java.io.Serializable;

public class Chat /*extends GroupChatInjury*/ implements  Serializable
{
    // #message types
    public static final String MESSAGE_TYPE_MESSAGE = "Message";
    public static final String MESSAGE_TYPE_IMAGE = "Image";
    public static final String MESSAGE_TYPE_VIDEO = "Video";

    // #chat types
    public static final String CHAT_TYPE_ONE_TO_ONE = "OtoO";
    public static final String CHAT_TYPE_GROUP_CHAT = "MtoM";
    public static final String CHAT_TYPE_INJURY_MANAGEMENT = "MtoM";

    // #grouop types
    public static final String GROUP_TYPE_GROUP_CHAT = "group_chat";
    public static final String GROUP_TYPE_INJURY_MANAGEMENT = "injury_management";

    // #member role in group
    public static final String MEMBER_ROLE_MEMBER = "member";
    public static final String MEMBER_ROLE_ADMIN = "admin";



    // Strings to put received message details and sending chat details
    private int positionInAdapter;
    private String id, messageFromUserId, messageToUserId, messageFromUserName, messageFromUserPicUrl, messageDateTime, messageDate, messageTime, messageType, message/*(image or video)*/;
    private String messageChatType, messageCount;
    private String userId; // #logged in user id
    // Strings to user details to whom logged in user is chatting
    private String chattingToId, chattingToName, chattingToPicUrl;
    // # group details
    private String groupType, groupAdminId, groupMemberRole, groupMemberId, groupMemberName, groupMemberPicUrl;
    private String[] fileArray;
    private boolean sendingMessage = false;  // file uploading status in chat, default value = false
    private int fileUploadingFirstPositionInAdapter; // first position in files which are being sent
    private long messageDateLong;  // date in long to compare


    public int getPositionInAdapter() {
        return positionInAdapter;
    }

    public void setPositionInAdapter(int positionInAdapter) {
        this.positionInAdapter = positionInAdapter;
    }

    public String getGroupMemberPicUrl() {
        return groupMemberPicUrl;
    }

    public void setGroupMemberPicUrl(String groupMemberPicUrl) {
        this.groupMemberPicUrl = groupMemberPicUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupMemberName() {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        this.groupMemberName = groupMemberName;
    }

    public String getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(String groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public String getGroupAdminId() {
        return groupAdminId;
    }

    public void setGroupAdminId(String groupAdminId) {
        this.groupAdminId = groupAdminId;
    }

    public String getGroupMemberRole() {
        return groupMemberRole;
    }

    public void setGroupMemberRole(String groupMemberRole) {
        this.groupMemberRole = groupMemberRole;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getMessageChatType() {
        return messageChatType;
    }

    public void setMessageChatType(String messageChatType) {
        this.messageChatType = messageChatType;
    }

    public long getMessageDateLong() {
        return messageDateLong;
    }

    public void setMessageDateLong(long messageDateLong) {
        this.messageDateLong = messageDateLong;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public int getFileUploadingFirstPositionInAdapter() {
        return fileUploadingFirstPositionInAdapter;
    }

    public void setFileUploadingFirstPositionInAdapter(int fileUploadingFirstPositionInAdapter) {
        this.fileUploadingFirstPositionInAdapter = fileUploadingFirstPositionInAdapter;
    }

    public boolean isSendingMessage() {
        return sendingMessage;
    }

    public void setSendingMessage(boolean sendingMessage) {
        this.sendingMessage = sendingMessage;
    }

    public String[] getFileArray() {
        return fileArray;
    }

    public void setFileArray(String[] fileArray) {
        this.fileArray = fileArray;
    }

    public String getMessageToUserId() {
        return messageToUserId;
    }

    public void setMessageToUserId(String messageToUserId) {
        this.messageToUserId = messageToUserId;
    }

    public String getChattingToPicUrl() {
        return chattingToPicUrl;
    }

    public void setChattingToPicUrl(String chattingToUserPicUrl) {
        this.chattingToPicUrl = chattingToUserPicUrl;
    }

    public String getMessageFromUserName() {
        return messageFromUserName;
    }

    public void setMessageFromUserName(String messageFromUserName) {
        this.messageFromUserName = messageFromUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageFromUserId() {
        return messageFromUserId;
    }

    public void setMessageFromUserId(String messageFromUserId) {
        this.messageFromUserId = messageFromUserId;
    }

    public String getChattingToId() {
        return chattingToId;
    }

    public void setChattingToId(String chattingToId) {
        this.chattingToId = chattingToId;
    }

    public String getMessageFromUserPicUrl() {
        return messageFromUserPicUrl;
    }

    public void setMessageFromUserPicUrl(String messageFromUserPicUrl) {
        this.messageFromUserPicUrl = messageFromUserPicUrl;
    }

    public String getChattingToName() {
        return chattingToName;
    }

    public void setChattingToName(String chattingToName) {
        this.chattingToName = chattingToName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }
}