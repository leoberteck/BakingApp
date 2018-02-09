package com.example.leonardo.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.entity.Ingredient;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.databinding.RecipeDetailIngredientsItemBinding;
import com.example.leonardo.bakingapp.databinding.RecipeDetailStepItemBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.ViewHolder> {

    private static final int INGREDIENTS_VIEW = 1;
    private static final int STEP_VIEW = 2;

    @NonNull
    private Recipe recipe;
    @NonNull
    private OnStepClickListener listener;

    public RecipeStepListAdapter(@NonNull Recipe recipe, @NonNull OnStepClickListener listener) {
        this.recipe = recipe;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return INGREDIENTS_VIEW;
        } else {
            return STEP_VIEW;
        }
    }

    @Override
    public RecipeStepListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(viewType == INGREDIENTS_VIEW){
            RecipeDetailIngredientsItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext())
                    , R.layout.recipe_detail_ingredients_item
                    , parent
                    , false
            );
            viewHolder = new IngredientsViewHolder(binding.getRoot());
        } else if(viewType == STEP_VIEW) {
            RecipeDetailStepItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext())
                    , R.layout.recipe_detail_step_item
                    , parent
                    , false
            );
            viewHolder = new StepViewHolder(binding.getRoot(), listener);
            binding.setViewHolder((StepViewHolder)viewHolder);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeStepListAdapter.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == INGREDIENTS_VIEW){
            holder.bind(recipe.getIngredients());
        } else if(viewType == STEP_VIEW) {
            holder.bind(recipe.getSteps().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return (recipe.getSteps() != null ? recipe.getSteps().size() : 0) + 1;
    }

    public interface OnStepClickListener {
        void onStepClick(Step step);
    }

    public abstract class ViewHolder<T> extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(T model);
    }

    public class IngredientsViewHolder extends ViewHolder<List<Ingredient>>{

        private final int QUANTITY_COLUMN_SIZE = 6;
        private final int MEASURE_COULUMN_SIZE = 6;
        private final String TEMPLATE = "%s %s - %s";

        private TextView textViewIngredients;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            textViewIngredients = itemView.findViewById(R.id.textView_ingredients);
        }

        @Override
        public void bind(List<Ingredient> model) {
            List<String> ingredientsList = new ArrayList<>();
            for (Ingredient ingredient : model) {
                String quantity = String.valueOf(ingredient.getQuantity());
                String measure = ingredient.getMeasure();
                ingredientsList.add(String.format(TEMPLATE, quantity, measure, ingredient.getIngredient()));
            }
            StringBuilder builder = new StringBuilder();
            for (String item : ingredientsList){
                builder.append(String.format("%s\n", item));
            }
            textViewIngredients.setText(builder.toString());
        }
    }

    public class StepViewHolder extends ViewHolder<Step> implements View.OnClickListener {

        @NonNull
        private View itemView;
        @NonNull
        private OnStepClickListener listener;
        private final ImageView imageViewThumbnail;

        public final ObservableField<String> description = new ObservableField<>();
        private Step step;

        public StepViewHolder(@NonNull View itemView, @NonNull OnStepClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.listener = listener;
            this.itemView.setOnClickListener(this);
            imageViewThumbnail = itemView.findViewById(R.id.imageView_step_thumb);
        }

        @Override
        public void bind(Step model) {
            this.step = model;
            String url = model.getMediaURL();
            if(!TextUtils.isEmpty(url)){
                Glide.with(imageViewThumbnail)
                    .load(Uri.parse(url))
                    .into(imageViewThumbnail);
            }
            description.set(model.getDescription());
        }

        @Override
        public void onClick(View v) {
            listener.onStepClick(step);
        }
    }
}
