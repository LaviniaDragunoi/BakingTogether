package com.example.user.bakingtogether.UI.ObjectList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.StepActivity;
import com.example.user.bakingtogether.ViewModel.DetailsActivityViewModel;
import com.example.user.bakingtogether.ViewModel.DetailsViewModelFactory;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyListsFragment extends Fragment {
    private static final String LOG_TAG = MyListsFragment.class.getSimpleName();
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    private Context mContext;
    ListsAdapter objectAdapter;
    private AppRoomDatabase roomDB;
    private int recipeIdIngredients, recipeIdSteps;
    private DetailsActivityViewModel mViewModel;
    private ArrayList<Parcelable> objectIngredientsList, objectStepsList;
    private TheRepository repository;
    private DetailsViewModelFactory mDetailsViewModelFactory;

    public MyListsFragment(){

    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();
        if(bundle != null){
        recipeIdIngredients = bundle.getInt("CurrentRecipeIdForIngredient");
        recipeIdSteps = bundle.getInt("CurrentRecipeIdForSteps");}

        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(mContext);
        objectListRV.setLayoutManager(layoutManagerReviews);

        //view model
        roomDB = AppRoomDatabase.getsInstance(getContext());
        repository = TheRepository.getsInstance(AppExecutors.getInstance(),
                roomDB,roomDB.recipeDao(), ApiUtils.getRecipeInterfaceResponse());
        if(recipeIdIngredients !=  0) {
            mDetailsViewModelFactory = new DetailsViewModelFactory(repository, recipeIdIngredients);
            mViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).get(DetailsActivityViewModel.class);

            mViewModel.getmIngredientsList().observe(this, ingredientEntityList -> {
                if (ingredientEntityList != null) {

                    bindDataToAdapter(convertIngredientListToObjectList(ingredientEntityList));
                }
            });
        }
        if(recipeIdSteps != 0) {
            mDetailsViewModelFactory = new DetailsViewModelFactory(repository, recipeIdSteps);
            mViewModel = ViewModelProviders.of(this, mDetailsViewModelFactory).get(DetailsActivityViewModel.class);
            mViewModel.getmStepsList().observe(this, stepEntityList -> {
                if (stepEntityList != null) {

                    bindDataToAdapter(convertStepsListToObjectList(stepEntityList));
                }
            });
        }

        return rootView;
    }

  public List<Object> convertIngredientListToObjectList(List<IngredientEntity> ingredientEntityList){
      return new ArrayList<>(ingredientEntityList);
    }

    public List<Object> convertStepsListToObjectList(List<StepEntity> stepsEntityList){
        return new ArrayList<>(stepsEntityList);
    }



    public void bindDataToAdapter(List<Object> objectList) {
        // Bind adapter to recycler view object

        objectListRV.setAdapter(new ListsAdapter(mContext,objectList));
    }
}