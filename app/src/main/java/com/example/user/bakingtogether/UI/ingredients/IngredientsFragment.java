package com.example.user.bakingtogether.UI.ingredients;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.user.bakingtogether.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IngredientsFragment extends Fragment {
    @BindView(R.id.ingredients_recycler_view) RecyclerView ingredients_recycler_view;
    private Unbinder unbinder;
    public IngredientsFragment(){

    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
