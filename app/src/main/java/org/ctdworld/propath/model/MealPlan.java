package org.ctdworld.propath.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MealPlan extends Response {

    public MealPlan() {
    }

    @SerializedName("response")
    private List<Plan> planList;


    public List<Plan> getPlanList() {
        return planList;
    }

    public void setMealList(List<Plan> planList) {
        this.planList = planList;
    }


    // # contains plan data
    public static class Plan implements Serializable {
        @SerializedName("training_plan_id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("created_by")
        private String createdByUserId;
        @SerializedName("created_by_name")
        private String createdByName;
        @SerializedName("created_by_role_id")
        private String createdByRoleId;
        @SerializedName("created_by_profile_pic")
        private String createdByProfilePic;
        @SerializedName("created_date_time")
        private String createdDateTime;
        @SerializedName("updated_date_time")
        private String updatedDate;
        @SerializedName("items_list")
        private ArrayList<MealPlanItem> planItemList;


        private boolean isSelfCreated;
        private int positionInAdapter = 0;
        private MealPlan.Plan.MealPlanItem planItem;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(String createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public String getCreatedByRoleId() {
            return createdByRoleId;
        }

        public void setCreatedByRoleId(String createdByRoleId) {
            this.createdByRoleId = createdByRoleId;
        }

        public String getCreatedByProfilePic() {
            return createdByProfilePic;
        }

        public void setCreatedByProfilePic(String createdByProfilePic) {
            this.createdByProfilePic = createdByProfilePic;
        }

        public String getCreatedDateTime() {
            return createdDateTime;
        }

        public void setCreatedDateTime(String createdDateTime) {
            this.createdDateTime = createdDateTime;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public ArrayList<MealPlanItem> getPlanItemList() {
            return planItemList;
        }

        public void setPlanItemList(ArrayList<MealPlanItem> planItemList) {
            this.planItemList = planItemList;
        }

        public boolean isSelfCreated() {
            return isSelfCreated;
        }

        public void setSelfCreated(boolean selfCreated) {
            isSelfCreated = selfCreated;
        }

        public int getPositionInAdapter() {
            return positionInAdapter;
        }

        public void setPositionInAdapter(int positionInAdapter) {
            this.positionInAdapter = positionInAdapter;
        }

        public MealPlanItem getPlanItem() {
            return planItem;
        }

        public void setPlanItem(MealPlanItem planItem) {
            this.planItem = planItem;
        }


        // # modal class to for meal plan item
        public static class MealPlanItem implements Serializable {
            public static final String MEDIA_TYPE_IMAGE = "image";
            public static final String MEDIA_TYPE_VIDEO = "video";

            @SerializedName("item_id")
            String id = null;
            @SerializedName("title")
            String title;
            @SerializedName("media_url")
            String mediaUrl;
            @SerializedName("media_type")
            String mediaType;
            @SerializedName("youtube_link")
            String link;
            int positionInAdapter;
            boolean itemFromServer;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMediaUrl() {
                return mediaUrl;
            }

            public void setMediaUrl(String mediaUrl) {
                this.mediaUrl = mediaUrl;
            }

            public String getMediaType() {
                return mediaType;
            }

            public void setMediaType(String mediaType) {
                this.mediaType = mediaType;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public int getPositionInAdapter() {
                return positionInAdapter;
            }

            public void setPositionInAdapter(int positionInAdapter) {
                this.positionInAdapter = positionInAdapter;
            }

            public boolean isItemFromServer() {
                return itemFromServer;
            }

            public void setItemFromServer(boolean itemFromServer) {
                this.itemFromServer = itemFromServer;
            }


            // this method returns plan item using plan item id
            public static int getPlanItemPosition(List<MealPlanItem> itemList, String itemId) {
                int position = -1;
                if (itemList != null) {
                    for (int i = 0; i < itemList.size(); i++) {
                        MealPlan.Plan.MealPlanItem planItem = itemList.get(i);
                        if (itemId.equals(planItem.getId())) {
                            position = i;
                            break;
                        }
                    }
                }

                return position;
            }
        }


    }


}
