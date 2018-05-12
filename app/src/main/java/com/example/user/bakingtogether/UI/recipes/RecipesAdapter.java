package com.example.user.bakingtogether.UI.recipes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.StepDetailsActivity;
import com.example.user.bakingtogether.models.RecipeResponse;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private List<RecipeResponse> recipesList;
    private Context context;

    public RecipesAdapter (Context context, List<RecipeResponse> recipesList){
        this.context = context;
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_list_item,parent, false);
        return new RecipesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        final RecipeResponse recipeResponse = recipesList.get(position);

        TextView recipeNameTextView = holder.recipeName;
        String name = recipeResponse.getName();
        recipeNameTextView.setText(name);
        ImageView recipeImageView = holder.recipeImage;
        String imageString = recipeResponse.getImage();
        if( imageString != null && !imageString.isEmpty()) {
            Picasso.get().load(Uri.parse(imageString)).into(recipeImageView);
        }else Picasso.get().load(R.drawable.ic_logo).into(recipeImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StepDetailsActivity.class);
                intent.putExtra("Recipe", recipeResponse);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_image)
        ImageView recipeImage;
        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }



}
