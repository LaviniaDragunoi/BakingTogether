package com.example.user.bakingtogether.adapter;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Will set up the recyclerView for the recipes list that will populate the RecipesFragment
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    public static final String EXTRA_RECIPE = "CurrentRecipe";
    public static final int DEFAULT_VALUE = -1;
    private List<RecipeEntity> recipesList;
    private Context context;

    public RecipesAdapter(Context context, List<RecipeEntity> recipeList) {
        this.context = context;
        this.recipesList = recipeList;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_list_item, parent, false);
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
        //if the image String from the API is empty, it will be used the app's logo
        if (imageString != null && !imageString.isEmpty()) {
            Picasso.get().load(Uri.parse(imageString)).into(recipeImageView);
        } else Picasso.get().load(R.drawable.ic_logo).into(recipeImageView);
        //on the click on an item of the adapter it will be send intent with the recipeEntity and
        // the DetailsActivity.class will start it's activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                intent.putExtra(EXTRA_RECIPE, recipeEntity);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipesList == null) return 0;
        return recipesList.size();
    }

    public static class RecipesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
