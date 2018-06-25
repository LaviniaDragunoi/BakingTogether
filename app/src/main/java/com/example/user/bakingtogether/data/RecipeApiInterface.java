package com.example.user.bakingtogether.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Lavinia Dragunoi
 * ApiInterface that has the endpoint defined using Retrofit annotation
 */
public interface RecipeApiInterface {
    @GET("android-baking-app-json")
    Call<List<RecipeResponse>> getRecipeResponse();
}
