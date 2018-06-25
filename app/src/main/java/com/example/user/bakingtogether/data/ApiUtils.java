package com.example.user.bakingtogether.data;

/**
 * ApiUtils helps to concatenate the BASE url with the rest of the url from the Interface
 */
public class ApiUtils {
    public static final String BASE = "http://go.udacity.com/";

    public static RecipeApiInterface getRecipeInterfaceResponse() {
        return ApiClient.getClient(BASE).create(RecipeApiInterface.class);
    }
}
