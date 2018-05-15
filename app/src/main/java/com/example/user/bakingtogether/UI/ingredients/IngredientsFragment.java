package com.example.user.bakingtogether.UI.ingredients;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.bakingtogether.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {
    @BindView(R.id.ingredients_recycler_view)
    RecyclerView ingredientsRV;
    @BindView(R.id.clear_ingredients_button)
    Button clearIngredientsButton;
    IngredientAdapter ingredientAdapter;

    public IngredientsFragment(){

    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
