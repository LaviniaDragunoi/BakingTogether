package com.example.user.bakingtogether.UI.ObjectList;

import android.content.Context;
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

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.adapter.ListsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyListsFragment extends Fragment {
    private static final String LOG_TAG = MyListsFragment.class.getSimpleName();
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    @BindView(R.id.clear_button)
    Button clearButton;
    private Context mContext;
    ListsAdapter objectAdapter;
    private AppRoomDatabase roomDB;
    private int recipeId;
    private ArrayList<Parcelable> objectIngredientsList, objectStepsList;

    public MyListsFragment(){

    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();

        objectIngredientsList = bundle.getParcelableArrayList("IngredientsList");
        Log.d(LOG_TAG,"The value of bundle is: " + objectIngredientsList );

     //   objectStepsList = bundle.getParcelableArrayList("StepsList");

        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(mContext);
        objectListRV.setLayoutManager(layoutManagerReviews);

        bindDataToAdapter(objectIngredientsList);
        return rootView;
    }

  /* public List<Object> convertIngredientListToObjectList(List<IngredientEntity> ingredientEntityList){
        List<Object> newObjectList = new ArrayList<>();
        for(int i = 0; i<ingredientEntityList.size(); i++){
            newObjectList.add(ingredientEntityList.get(i));
        }
        return newObjectList;
    }

    public List<Object> convertStepsListToObjectList(List<StepEntity> stepsEntityList){
        List<Object> newObjectList = new ArrayList<>();
        for(int i = 0; i<stepsEntityList.size(); i++){
            newObjectList.add(stepsEntityList.get(i));
        }
        return newObjectList;
    }*/



    public void bindDataToAdapter(ArrayList<Parcelable> objectList) {
        // Bind adapter to recycler view object

        objectListRV.setAdapter(new ListsAdapter(objectList));
    }
}