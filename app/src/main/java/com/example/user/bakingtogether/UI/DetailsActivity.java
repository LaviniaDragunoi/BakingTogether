package com.example.user.bakingtogether.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.ingredients.IngredientsFragment;
import com.example.user.bakingtogether.models.RecipeResponse;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    private Intent intent;
    RecipeResponse currentRecipe;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        intent = getIntent();
        currentRecipe = intent.getParcelableExtra("Recipe");
        //set recipe title
        setTitle(currentRecipe.getName());



    }

}
