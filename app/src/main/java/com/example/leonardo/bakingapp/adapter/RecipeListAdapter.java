package com.example.leonardo.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.databinding.RecipeItemBinding;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    @NonNull
    private List<Recipe> recipeList;
    @Nullable
    private OnRecipeClickListener listener;

    public RecipeListAdapter(@NonNull List<Recipe> recipeList, @Nullable OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecipeItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext())
                , R.layout.recipe_item
                , parent
                , false
        );
        ViewHolder viewHolder = new ViewHolder(binding.getRoot(), binding, listener);
        binding.setViewHolder(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface OnRecipeClickListener {
        void onViewRecipeClick(Recipe recipe);
        void onShareClick(Recipe recipe);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @NonNull
        private RecipeItemBinding recipeItemBinding;
        @NonNull
        private View itemView;
        @Nullable
        private OnRecipeClickListener listener;

        private ImageView imageViewThumbNail;
        public final ObservableField<String> recipeTitle = new ObservableField<>();
        public final ObservableField<String> numberOfSteps = new ObservableField<>();
        public final ObservableField<String> numberOfIngredients = new ObservableField<>();

        private Recipe recipe;

        public ViewHolder(@NonNull View itemView, @NonNull RecipeItemBinding recipeItemBinding, @Nullable OnRecipeClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            this.recipeItemBinding = recipeItemBinding;
            this.listener = listener;

            imageViewThumbNail = itemView.findViewById(R.id.imageView_recipe_thumb);
        }

        public void onViewRecipeClick(){
            if(listener != null){
                listener.onViewRecipeClick(recipe);
            }
        }

        public void onShareClick(){
            if(listener != null){
                listener.onShareClick(recipe);
            }
        }


        public void bind(Recipe recipe){
            String thumb = recipe.getDefaultThumbnailUri();
            if(thumb != null){
                Glide.with(itemView).load(
                        Uri.parse(recipe.getDefaultThumbnailUri())
                ).into(imageViewThumbNail);
            }
            recipeTitle.set(recipe.getName());
            numberOfIngredients.set(String.valueOf(recipe.getIngredients() != null ? recipe.getIngredients().size() : 0));
            numberOfSteps.set(String.valueOf(recipe.getSteps() != null ? recipe.getSteps().size() : 0));
        }
    }
}