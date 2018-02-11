package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.adapter.RecipeListAdapter;
import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Ingredient;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.util.ResourceUtils;
import com.example.leonardo.bakingapp.util.SimpleIdlingResource;

import static com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP.RecipeListActivity;
import static com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP.RecipeListPresenterInterface;

public class RecipeListPresenter extends BaseObservable implements RecipeListPresenterInterface, RecipeListAdapter.OnRecipeClickListener {

    private RecipeListAdapter adapter;
    private RecipeListActivity activity;
    private ResourceUtils resourceUtils;

    public RecipeListPresenter(ResourceUtils resourceUtils) {
        this.resourceUtils = resourceUtils;
    }

    @Override
    public void setActivity(RecipeListActivity activity) {
        this.activity = activity;
    }

    @Override
    public void loadRecipes(final SimpleIdlingResource idlingResource){
        if(idlingResource != null){
            idlingResource.setIdleState(false);
        }
        if(adapter == null){
            new LoadMoviesTask(this, idlingResource).execute();
        } else if(activity != null) {
            activity.setAdapter(adapter);
            if(idlingResource != null){
                idlingResource.setIdleState(true);
            }
        }
    }

    @Override
    public void onViewRecipeClick(Recipe recipe) {
        if(activity != null){
            activity.showRecipeDetails(recipe);
        }
    }

    @Override
    public void onShareClick(Recipe recipe) {
        StringBuilder recipeText = new StringBuilder();
        recipeText.append(recipe.getName()).append("\n")
            .append("\n")
            .append(resourceUtils.getString(R.string.ingredients)).append("\n")
            .append("\n");
        for (Ingredient ingredient : recipe.getIngredients()) {
            recipeText.append(ingredient.getIngredient())
                .append(" - ")
                .append(ingredient.getQuantity())
                .append(" ")
                .append(ingredient.getMeasure())
                .append("\n");
        }
        recipeText.append("\n")
            .append(resourceUtils.getString(R.string.steps)).append("\n")
            .append("\n");

        for (Step step : recipe.getSteps()) {
            recipeText.append(step.getDescription()).append("\n\n");
        }

        activity.shareRecipe(recipeText.toString());
    }

    private static class LoadMoviesTask extends AsyncTask<Void, Void, RecipeListAdapter> {

        @NonNull
        RecipeListPresenter caller;
        @Nullable
        private SimpleIdlingResource idlingResource;

        public LoadMoviesTask(@NonNull RecipeListPresenter caller, @Nullable SimpleIdlingResource idlingResource) {
            this.caller = caller;
            this.idlingResource = idlingResource;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(idlingResource != null){
                idlingResource.setIdleState(false);
            }
        }

        @Override
        protected RecipeListAdapter doInBackground(Void... voids) {
            return new RecipeListAdapter(RecipeAPI.getAllRecipies(), caller);
        }

        @Override
        protected void onPostExecute(RecipeListAdapter recipeListAdapter) {
            super.onPostExecute(recipeListAdapter);
            caller.adapter = recipeListAdapter;
            caller.activity.setAdapter(recipeListAdapter);
            if(idlingResource != null){
                idlingResource.setIdleState(true);
            }
        }
    }
}
