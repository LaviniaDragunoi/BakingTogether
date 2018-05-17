package com.example.user.bakingtogether.UI.ObjectList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.Ingredient;
import com.example.user.bakingtogether.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {
    @BindView(R.id.object_list_recycler_view)
    RecyclerView objectListRV;
    @BindView(R.id.clear_button)
    Button clearButton;
    private Context mContext;
    ListsAdapter objectAdapter;
    List<Object> objectList;
    private AppRoomDatabase roomDB;

    public ListFragment(){

    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.object_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public List<Object> convertIngredientListToOrbjectList(List<IngredientEntity> ingredientEntityList){
        List<Object> newObjectList = new ArrayList<>();
        for(int i = 0; i<ingredientEntityList.size(); i++){
            newObjectList.add(ingredientEntityList.get(i));
        }
        return newObjectList;
    }



    public void bindDataToAdapter(Context context, List<Object> objectList) {
        // Bind adapter to recycler view object
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(context);
        objectListRV.setLayoutManager(layoutManagerReviews);
        objectListRV.setAdapter(new ListsAdapter(objectList));
    }
}