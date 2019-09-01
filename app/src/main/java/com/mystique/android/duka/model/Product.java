package com.mystique.android.duka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("original_id")
    @Expose
    private String originalId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private int value;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("abstract")
    @Expose
    private String _abstract;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("images")
    @Expose
    private Images images;

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

}
