package com.foodsafe.foodsafeapp.model;

public class FoodItem {
    private long id;
    private String title;
    private String description;
    private String ratingInfo;
    private int imageResId;
    private int dietIconResId;

    public FoodItem(long id, String title, String description, String ratingInfo, int imageResId, int dietIconResId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ratingInfo = ratingInfo;
        this.imageResId = imageResId;
        this.dietIconResId = dietIconResId;
    }

    public long getId() { return id; } // Método getId() adicionado
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRatingInfo() { return ratingInfo; }
    public int getImageResId() { return imageResId; }
    public int getDietIconResId() { return dietIconResId; }

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRatingInfo(String ratingInfo) { this.ratingInfo = ratingInfo; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setDietIconResId(int dietIconResId) { this.dietIconResId = dietIconResId; }
}