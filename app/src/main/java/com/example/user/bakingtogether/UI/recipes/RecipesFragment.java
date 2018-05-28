package com.example.user.bakingtogether.UI.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.RecipeDao;
import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.ViewModel.MainActivityViewModel;
import com.example.user.bakingtogether.ViewModel.MainViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.Ingredients;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.example.user.bakingtogether.data.RecipeResponse;
import com.example.user.bakingtogether.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recipesRW;
    @BindView(R.id.recipes_progress_bar)
    ProgressBar recipesPB;
    @BindView(R.id.error_loading_recipes)
    TextView errorMessage;
    private RecipesAdapter recipesAdapter;
    private Retrofit retrofit;
    private Context mContext;
    private AppRoomDatabase roomDB;
    private LiveData<List<RecipeEntity>> recipesList;
    private TheRepository repository;
    private MainActivityViewModel mainViewModel;
    private MainViewModelFactory mMainViewModelFactory;

    private RecipeDao recipeDao;
    private RecipeApiInterface recipeApiInterface;

    public RecipesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle saveInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipes_main, container, false);
        ButterKnife.bind(this, rootView);

        isLoadingData();

        roomDB = AppRoomDatabase.getsInstance(getContext());
        AppExecutors executors = AppExecutors.getInstance();
        RecipeApiInterface recipeApiInterface =ApiUtils.getRecipeInterfaceResponse();
        repository = TheRepository.getsInstance(executors,
                roomDB,roomDB.recipeDao(),recipeApiInterface);
       mMainViewModelFactory = new MainViewModelFactory(repository);
       mainViewModel = ViewModelProviders.of(this, mMainViewModelFactory).get(MainActivityViewModel.class);
       mainViewModel.getRecipeEntity().observe(this,recipeEntities ->{
           if(recipeEntities != null && recipeEntities.size() != 0){
               RecyclerView.LayoutManager layoutManager = new GridLayoutManager(recipesRW.getContext(), 1);
               recipesRW.setLayoutManager(layoutManager);
               recipesAdapter = new RecipesAdapter(getContext(),recipeEntities);
               showData();
               recipesRW.setAdapter(recipesAdapter);
           }else if(recipeEntities != null){
               errorMessage.setVisibility(View.VISIBLE);

           }
       });
        return rootView;
    }
    private void isLoadingData(){
        recipesPB.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        recipesRW.setVisibility(View.GONE);
    }
    private void showData(){
        recipesPB.setVisibility(View.GONE);
        recipesRW.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
    }
}
