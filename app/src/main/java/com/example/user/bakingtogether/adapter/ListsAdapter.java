package com.example.user.bakingtogether.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.IngredientEntity;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.StepActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;

/**
 * The ListsAdapter will set up the recyclerView and
 * will help to populate the fragment that contains stepsList and ingredientsList. Is a multifunctional
 * custom for the same recyclerView that will host in different situations different lists
 */
public class ListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int STEP = 0, INGREDIENT = 1, INVALID_OBJECT = -1;
    public static final String CURRENT_STEP = "CurrentStep";
    public static final String CURRENT_RECIPE = "CurrentRecipe";
    public static final String STEPS_LIST = "StepsList";
    private List<Object> objectList;
    private Context context;

    /**
     * ListsAdapter constructor that will help to create the adapter in order to be used for
     * displaying ingredients and steps lists
     * @param context    the context within will be displayed our object list
     * @param objectList the general object lst that will be changed with a ingredients or step list
     *                   for each special case
     */
    public ListsAdapter(Context context, List<Object> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    /**
     * This method will tell the RecyclerView about the type of view to inflate based on position
     *
     * @param position
     * @return if the object from the mentioned position is a IngredientEntity the returned value
     * is the int constant INGREDIENT( value 1), if is a StepEntity the returned value is the int constant
     * STEP(value 0), otherwhise the returned value is INVALID_OBJECT(value -1)
     */
    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof IngredientEntity) {
            return INGREDIENT;
        } else if (objectList.get(position) instanceof StepEntity) {
            return STEP;
        }
        return INVALID_OBJECT;
    }

    /**
     * This method will create different RecyclerView.ViewHolder objects based on the item view type.
     *
     * @param parent   viewGroup container for the item
     * @param viewType type of view that will be inflated
     * @return the viewHolder created in order to be inflated
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case INGREDIENT:
                View v1 = inflater.inflate(R.layout.ingredient_item, parent, false);
                viewHolder = new IngredientViewHolder(v1);
                break;
            default:
                View v2 = inflater.inflate(R.layout.steps_item_list, parent, false);
                viewHolder = new StepViewHolder(v2);
                break;

        }
        return viewHolder;
    }

    /**
     * This method is to update the RecyclerView.ViewHolder contents with the item at the given position
     *
     * @param holder   the type of the RecyclerView.ViewHolder to populate
     * @param position item position in the viewGroup
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREDIENT:
                IngredientViewHolder vh1 = (IngredientViewHolder) holder;
                configureIngredient(vh1, position);
                break;
            default:
                StepViewHolder vh2 = (StepViewHolder) holder;
                configureStep(vh2, position);
                break;
        }

    }

    /**
     * Method that will configure the step objects
     *
     * @param vh2      The holder for the Steps
     * @param position item position that will be configure
     */
    private void configureStep(final StepViewHolder vh2, int position) {
        final StepEntity step = (StepEntity) objectList.get(position);
        if (step != null) {
            //set the text for the Step Name
            vh2.getStepNameRB().setText(step.getShortDescription());
            //set the clickListener on each step item and send Intent with: id of the clicked step,
            //id of the current Recipe and the stepsList of the current recipe. at the final the
            // StepActivity.class will start
            vh2.getStepNameRB().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stepIntent = new Intent(vh2.itemView.getContext(), StepActivity.class);
                    stepIntent.putExtra(CURRENT_STEP, step.getId())
                            .putExtra(CURRENT_RECIPE, step.getRecipeId())
                            .putExtra(STEPS_LIST, new ArrayList<>(objectList));
                    vh2.itemView.getContext().startActivity(stepIntent);
                }
            });
        }
    }

    /**
     * Method that will configure the ingredient objects
     *
     * @param vh1      The holder for the ingredients
     * @param position item position that will be configure
     */
    private void configureIngredient(IngredientViewHolder vh1, int position) {
        IngredientEntity ingredient = (IngredientEntity) objectList.get(position);
        if (ingredient != null) {
            vh1.getQuantityTV().setText(valueOf(ingredient.getQuantity()));
            vh1.getMeasurementTV().setText(ingredient.getMeasure());
            vh1.getIngredientTV().setText(ingredient.getIngredient());
        }
    }

    /**
     * this method is counting the number of items in the list displayed
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        if (objectList == null) return 0;
        return this.objectList.size();
    }

    /**
     * This class will create the holder object for the Ingredient object
     */
    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.quantity)
        TextView quantityTV;
        @BindView(R.id.measurement)
        TextView measurementTV;
        @BindView(R.id.ingredient)
        TextView ingredientTV;
        private Double quantity;
        private String measure;
        private String ingredient;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public TextView getQuantityTV() {
            return quantityTV;
        }

        public void setQuantityTV(TextView quantityTV) {
            this.quantityTV = quantityTV;
        }

        public TextView getMeasurementTV() {
            return measurementTV;
        }

        public void setMeasurementTV(TextView measurementTV) {
            this.measurementTV = measurementTV;
        }

        public TextView getIngredientTV() {
            return ingredientTV;
        }

        public void setIngredientTV(TextView ingredientTV) {
            this.ingredientTV = ingredientTV;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

    }

    /**
     * This class will create the holder object for the Ingredient object
     */
    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_name_radio_button)
        RadioButton stepNameRB;
        private Integer id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public RadioButton getStepNameRB() {
            return stepNameRB;
        }

        public void setStepNameRB(RadioButton stepNameRB) {
            this.stepNameRB = stepNameRB;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }
    }
}

