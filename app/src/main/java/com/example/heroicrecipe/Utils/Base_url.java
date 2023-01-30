package com.example.heroicrecipe.Utils;

public class Base_url {

    private static String mainUrl = "https://1156-105-61-238-65.in.ngrok.io/";

    public static String getallfoods(){
        return mainUrl + "api/Recipe/allrecipes";
    }

    public static String getingridients(){
        return mainUrl + "api/Recipe/getingridients";
    }

    public static String getcookingprocess() {
        return mainUrl + "api/Recipe/cookingprocess";
    }

    public static String addbookmarks(){
        return mainUrl + "api/Recipe/add_bookmark";
    }

    public static String getbookmarks(){ return mainUrl + "api/Recipe/boookmarks";}

    public static  String searchfood(){ return mainUrl + "api/Recipe/searchfood";}

    public static String loginurl() {
        return mainUrl + "api/Recipe/login";
    }

    public static String registerurl(){
        return mainUrl + "api/Recipe/register";
    }

    public static String userdetails(){return mainUrl + "api/Recipe/userdetails";}



}
