package com.example.jon.bestforkforward.DataHandling;


import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("id")
    private int step_id;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("description")
    private String description;
    @SerializedName("videoURL")
    private String videoUrl;
    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    public Step(int step_id, String shortDescription, String description, String videoUrl, String thumbnailUrl){
        this.step_id = step_id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getStep_id() {
        return this.step_id;
    }
    public String getShortDescription(){
        return this.shortDescription;
    }
    public String getDescription() {
        return this.description;
    }
    public String getVideoUrl() {
        return this.videoUrl;
    }
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setStep_id(int step_id){
        this.step_id = step_id;
    }
    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setVideoUrl(String videoUrl){
        this.videoUrl = videoUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }
}
