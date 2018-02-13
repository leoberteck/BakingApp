package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.adapter.RecipeListAdapter;
import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.util.ResourceUtils;
import com.example.leonardo.bakingapp.util.SimpleIdlingResource;

import java.util.List;

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
        if(adapter == null){
            RecipeAPI.getAllRecipiesAsync(new RecipeAPI.OnTaskFinishListener() {
                @Override
                public void onTaskFinish(List<Recipe> recipeList) {
                    if(recipeList == null || recipeList.size() == 0){
                        activity.showToast(R.string.err_load_recipe_no_internet);
                    } else {
                        RecipeListPresenter.this.adapter = new RecipeListAdapter(recipeList, RecipeListPresenter.this);
                        RecipeListPresenter.this.activity.setAdapter(RecipeListPresenter.this.adapter);
                    }
                }
            }, idlingResource);
        } else if(activity != null) {
            activity.setAdapter(adapter);
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
        activity.shareRecipe(recipe.getRecipeAsText(resourceUtils));
    }
}
