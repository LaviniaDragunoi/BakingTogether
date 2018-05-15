package com.example.user.bakingtogether.UI.recipes;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.data.ApiClient;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.example.user.bakingtogether.models.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view) RecyclerView recipesRW;
    @BindView(R.id.recipes_progress_bar) ProgressBar recipesPB;
    @BindView(R.id.error_loading_recipes)
    TextView errorMessage;
    private RecipesAdapter recipesAdapter;
    private Retrofit retrofit;
    private Context mContext;

    public RecipesFragment(){

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_recipes_main, container, false);
        ButterKnife.bind(this, rootView);

        recipesPB.setVisibility(View.VISIBLE);
        RecipeApiInterface apiService = ApiUtils.getRecipeInterfaceResponse();
        Call<List<RecipeResponse>> call = apiService.getRecipeResponse();
        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                List<RecipeResponse> recipes = response.body();
                RecyclerView.LayoutManager layoutManager;
                layoutManager = new GridLayoutManager(recipesRW.getContext(),1);
                recipesPB.setVisibility(View.INVISIBLE);
                recipesRW.setLayoutManager(layoutManager);
                recipesAdapter = new RecipesAdapter(mContext, recipes);
                recipesRW.setAdapter(recipesAdapter);
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                recipesPB.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.VISIBLE);

            }
        });
        return rootView;
    }
}
