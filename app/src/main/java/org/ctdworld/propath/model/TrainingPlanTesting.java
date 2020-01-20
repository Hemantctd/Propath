package org.ctdworld.propath.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrainingPlanTesting
{


    /*public static final String KEY_TRAINING_PLAN = "training plan";
    public static final String KEY_TYPE_RECEIVED_OR_CREATED = "received or created";*/

   /* public static final int EXTRA_TYPE_RECEIVED = 1;
    public static final int EXTRA_TYPE_CREATED = 2;*/
   /* public static final int REQUEST_CODE_VIEW_TRAINING_PLAN = 3;
    public static final int REQUEST_CODE_CREATE_TRAINING_PLAN = 4;
    public static final int REQUEST_CODE_EDIT_TRAINING_PLAN = 5;
    private static final int REQUEST_CODE_SHARE_TRAINING_PLAN = 6;  // to share training plan*/


    public TrainingPlanTesting() {}


    @SerializedName("res")
    private String status;

    @SerializedName("response")
    private List<TrainingPlanTesting.Plan> planList;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TrainingPlanTesting.Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<TrainingPlanTesting.Plan> planList) {
        this.planList = planList;
    }




    // # contains plan data
    public static class Plan  implements Parcelable
    {
        @SerializedName("training_plan_id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("items_list")
        private ArrayList<TrainingPlanTesting.Plan.PlanItem> planItemList;

        private boolean isSelfCreated;
        private int positionInAdapter = 0;
        private TrainingPlanTesting.Plan.PlanItem planItem;


        protected Plan(Parcel in) {
            id = in.readString();
            title = in.readString();
            description = in.readString();
            createdBy = in.readString();
            planItemList = in.createTypedArrayList(TrainingPlanTesting.Plan.PlanItem.CREATOR);
            isSelfCreated = in.readByte() != 0;
            positionInAdapter = in.readInt();
            planItem = in.readParcelable(TrainingPlanTesting.Plan.PlanItem.class.getClassLoader());
        }

        public static final Creator<TrainingPlanTesting.Plan> CREATOR = new Creator<TrainingPlanTesting.Plan>() {
            @Override
            public TrainingPlanTesting.Plan createFromParcel(Parcel in) {
                return new TrainingPlanTesting.Plan(in);
            }

            @Override
            public TrainingPlanTesting.Plan[] newArray(int size) {
                return new TrainingPlanTesting.Plan[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(title);
            parcel.writeString(description);
            parcel.writeString(createdBy);
            parcel.writeTypedList(planItemList);
            parcel.writeByte((byte) (isSelfCreated ? 1 : 0));
            parcel.writeInt(positionInAdapter);
            parcel.writeParcelable(planItem, i);
        }


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

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public ArrayList<TrainingPlanTesting.Plan.PlanItem> getPlanItemArrayList() {
            return planItemList;
        }

        public void setPlanItemArrayList(ArrayList<TrainingPlanTesting.Plan.PlanItem> planItemArrayList) {
            this.planItemList = planItemArrayList;
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

        public TrainingPlanTesting.Plan.PlanItem getPlanItem() {
            return planItem;
        }

        public void setPlanItem(TrainingPlanTesting.Plan.PlanItem planItem) {
            this.planItem = planItem;
        }

        public static Creator<TrainingPlanTesting.Plan> getCREATOR() {
            return CREATOR;
        }



        // # modal class to for training plan item
        public static class PlanItem implements  Parcelable
        {
            public static final String MEDIA_TYPE_IMAGE = "image";
            public static final String MEDIA_TYPE_VIDEO = "video";

            @SerializedName("item_id")
            String id;
            @SerializedName("title")
            String title;
            @SerializedName("media_url")
            String mediaUrl;
            @SerializedName("media_type")
            String mediaType;
            int positionInAdapter;
            boolean itemFromServer;

            public PlanItem(){};


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

            protected PlanItem(Parcel in) {
                id = in.readString();
                title = in.readString();
                mediaUrl = in.readString();
                mediaType = in.readString();
                positionInAdapter = in.readInt();
                itemFromServer = in.readByte() != 0;
            }

            public static final Creator<TrainingPlanTesting.Plan.PlanItem> CREATOR = new Creator<TrainingPlanTesting.Plan.PlanItem>() {
                @Override
                public TrainingPlanTesting.Plan.PlanItem createFromParcel(Parcel in) {
                    return new TrainingPlanTesting.Plan.PlanItem(in);
                }

                @Override
                public TrainingPlanTesting.Plan.PlanItem[] newArray(int size) {
                    return new TrainingPlanTesting.Plan.PlanItem[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(id);
                parcel.writeString(title);
                parcel.writeString(mediaUrl);
                parcel.writeString(mediaType);
                parcel.writeInt(positionInAdapter);
                parcel.writeByte((byte) (itemFromServer ? 1 : 0));
            }


        }

    }



}
