package com.example.user.bakingtogether.data;

public class ApiUtils {
    public static final String BASE = "http://go.udacity.com/";
    public static RecipeApiInterface getRecipeInterfaceResponse(){
        return ApiClient.getClient(BASE).create(RecipeApiInterface.class);
    }
}
