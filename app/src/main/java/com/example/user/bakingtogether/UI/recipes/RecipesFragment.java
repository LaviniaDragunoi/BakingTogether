package com.example.user.bakingtogether.UI.recipes;

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

import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.data.ApiClient;
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
    private Unbinder unbinder;
    private RecipesAdapter recipesAdapter;
    private Retrofit retrofit;

    public RecipesFragment(){

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_recipes_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager layoutManager;
        if((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
             layoutManager = new GridLayoutManager(recipesRW.getContext(),2);
        }else {

            layoutManager = new GridLayoutManager(recipesRW.getContext(),1);
        }
        recipesRW.setLayoutManager(layoutManager);

        RecipeApiInterface apiService = ApiClient.getClient()
                .create(RecipeApiInterface.class);

        Call<List<RecipeResponse>> call = apiService.getRecipe();
        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeResponse>> call, @NonNull Response<List<RecipeResponse>> response) {
                List<RecipeResponse> responses = response.body();
                recipesRW.setAdapter(new RecipesAdapter(getContext(), new ArrayList<RecipeResponse>()));
            }


            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                recipesPB.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }
}
