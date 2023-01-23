package com.example.heroicrecipe.Utils;

public class Base_url {

    private static String mainUrl = "http://35.232.36.71:8081/";
    private static String auth = "http://35.232.36.71:8081/";

    public static String getallfoods(String category){
        return mainUrl + "api/Recipe/allfoods?category" +category;
    }

    public static String getbookmarks(String user_id){
        return mainUrl + "api/Recipe/bookmarks?user_id" +user_id;
    }

    public static String addbookmarks(){
        return mainUrl + "api/Recipe/addbookmark";
    }

    public static String getingridients(String food_id){
        return mainUrl + "api/Recipe/getingridients?meals_id" +food_id;
    }

    public static String getcookingprocess(String food_id) {
        return mainUrl + "api/Recipe/cookingprocess?meals_id" +food_id;
    }

    public static  String searchfood(String food){
        return mainUrl + "api/Recipe/searchfood?food" +food;
    }

    public static String loginurl() {
        return auth + "Recipe/login";
    }

    public static String registerurl(){
        return auth + "Recipe/register";
    }


}
