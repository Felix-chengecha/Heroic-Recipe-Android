package com.example.heroicrecipe.Models;
public class food_model {

    private String foodid;
    private String foodname;
    private String foodcategory;
    private String foodimg;
    private String foodlevel;

    public food_model(  String foodid, String foodname, String foodcategory, String foodimg, String foodlevel) {
        this.foodid =foodid;
        this.foodlevel=foodlevel;
        this.foodname = foodname;
        this.foodcategory = foodcategory;
        this.foodimg = foodimg;
    }

    public String getFoodlevel() {
        return foodlevel;
    }

    public void setFoodlevel(String foodlevel) {
        this.foodlevel = foodlevel;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodcategory() {
        return foodcategory;
    }

    public void setFoodcategory(String foodcategory) {
        this.foodcategory = foodcategory;
    }

    public String getFoodimg() {
        return foodimg;
    }

    public void setFoodimg(String foodimg) {
        this.foodimg = foodimg;
    }
}