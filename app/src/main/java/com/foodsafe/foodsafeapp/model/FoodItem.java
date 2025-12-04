package com.foodsafe.foodsafeapp.model;

public class FoodItem {
    private String title;
    private String description;
    private String ratingInfo;
    private int imageResId;
    private int dietIconResId;

    public FoodItem(String title, String description, String ratingInfo, int imageResId, int dietIconResId) {
        this.title = title;
        this.description = description;
        this.ratingInfo = ratingInfo;
        this.imageResId = imageResId;
        this.dietIconResId = dietIconResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRatingInfo() { return ratingInfo; }
    public int getImageResId() { return imageResId; }
    public int getDietIconResId() { return dietIconResId; }
}