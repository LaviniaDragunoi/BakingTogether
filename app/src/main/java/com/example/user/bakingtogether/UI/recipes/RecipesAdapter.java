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

import com.example.user.bakingtogether.DB.RecipeEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.DetailsActivity;
import com.example.user.bakingtogether.data.RecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private List<RecipeEntity> recipesList;
    private Context context;

    public RecipesAdapter (Context context, List<RecipeEntity> recipesList){
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
    public void onBindViewHolder(@NonNull final RecipesViewHolder holder, int position) {
        final RecipeEntity recipeEntity = recipesList.get(position);

        TextView recipeNameTextView = holder.recipeName;
        String name = recipeEntity.getName();
        recipeNameTextView.setText(name);
        ImageView recipeImageView = holder.recipeImage;
        String imageString = recipeEntity.getImage();
        if( imageString != null && !imageString.isEmpty()) {
            Picasso.get().load(Uri.parse(imageString)).into(recipeImageView);

        }else Picasso.get().load(R.drawable.ic_logo).into(recipeImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                intent.putExtra("Recipe", recipeEntity);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_name) TextView recipeName;
        @BindView(R.id.recipe_image) ImageView recipeImage;
        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }



}
