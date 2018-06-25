package com.example.user.bakingtogether.UI.ObjectList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.adapter.ListsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment that will br used to populate the ingredients and steps list on the DetailsActivity
 */
public class MyListsFragment extends Fragment {
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    private Context mContext;

    //Empty constructor
    public MyListsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(mContext);
        objectListRV.setLayoutManager(layoutManagerReviews);

        return rootView;
    }

    /**
     * Method that will convert the ingredients list into an object list that will be used to bind
     * data into ListsAdapter that is a multi objects(ingredients and steps) used adapter
     *
     * @param ingredientEntityList the list to be converted
     * @return the object list, the result of conversion
     */
    public List<Object> convertIngredientListToObjectList(List<IngredientEntity> ingredientEntityList) {
        return new ArrayList<>(ingredientEntityList);
    }

    /**
     * Method that will convert the steps list into an object list that will be used to bind
     * data into ListsAdapter that is a multi objects(ingredients and steps) used adapter
     *
     * @param stepsEntityList the list to be converted
     * @return the object list, the result of conversion
     */
    public List<Object> convertStepsListToObjectList(List<StepEntity> stepsEntityList) {
        return new ArrayList<>(stepsEntityList);
    }

    /**
     * Method that will bind the list of objects(ingredients or steps) to recycler view
     *
     * @param objectList the list that will be set on the RecyclerView
     */
    public void bindDataToAdapter(List<Object> objectList) {
        // Bind adapter to recycler view object
        objectListRV.setAdapter(new ListsAdapter(mContext, objectList));
    }
}
