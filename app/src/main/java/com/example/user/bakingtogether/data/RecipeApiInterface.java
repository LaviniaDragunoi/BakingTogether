package com.example.user.bakingtogether.data;

import com.example.user.bakingtogether.models.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Lavinia Dragunoi on 7-05-2018
 */
public interface RecipeApiInterface {
    @GET("android-baking-app-json")
    Call<List<RecipeResponse>> getRecipe();
}
