package com.example.user.bakingtogether.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.recipes.RecipesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.valueOf;

public class ListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> objectList;
    private static final int STEP = 0, INGREDIENT = 1;


    public ListsAdapter(List<Object> objectList){
        this.objectList = objectList;
    }

    @Override
    public int getItemViewType(int position) {
        if(objectList.get(position) instanceof IngredientEntity){
            return INGREDIENT;
        }else if(objectList.get(position) instanceof StepEntity){
            return STEP;
        }
        return -1;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case INGREDIENT:
                View v1 = inflater.inflate(R.layout.ingredient_item, parent, false);
                viewHolder = new IngredientEntity(v1);
                break;
            case STEP:
                View v2 = inflater.inflate(R.layout.steps_item_list, parent, false);
                viewHolder = new StepEntity(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new RecipesAdapter.RecipesViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case INGREDIENT:
                IngredientEntity vh1 = (IngredientEntity) holder;
                configureIngredient(vh1,position);
                break;
            case STEP:
                StepEntity vh2 = (StepEntity) holder;
                configureStep(vh2, position);
                break;
            default:
                break;
        }

    }

    private void configureStep(StepEntity vh2, int position) {
        StepEntity step = (StepEntity) objectList.get(position);
        if(step != null){
            vh2.getStepNameTV().setText(step.getShortDescription());
        }
    }

    private void configureIngredient(IngredientEntity vh1, int position) {
        IngredientEntity ingredient = (IngredientEntity) objectList.get(position);
        if(ingredient != null){
            vh1.getQuantityTV().setText(valueOf(ingredient.getQuantity()));
            vh1.getMeasurementTV().setText(ingredient.getMeasure());
            vh1.getIngredientTV().setText(ingredient.getIngredient());
        }
    }

    @Override
    public int getItemCount() {
        return this.objectList.size();
    }
    class IngredientEntity extends RecyclerView.ViewHolder{

        private Double quantity;

        private String measure;

        private String ingredient;

        @BindView(R.id.quantity)
        TextView quantityTV;
        @BindView(R.id.measurement)
        TextView measurementTV;
        @BindView(R.id.ingredient)
        TextView ingredientTV;


        public IngredientEntity(View view){
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

    class StepEntity extends RecyclerView.ViewHolder{

        private Integer id;

        private String shortDescription;

        private String description;

        private String videoURL;

        private String thumbnailURL;
        @BindView(R.id.step_name_radio_button)
        RadioButton stepNameTV;
        public StepEntity(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public RadioButton getStepNameTV() {
            return stepNameTV;
        }

        public void setStepNameTV(RadioButton stepNameTV) {
            this.stepNameTV = stepNameTV;
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
