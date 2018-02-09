package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeDetailMVP;


public class RecipeDetailPresenter extends BaseObservable implements RecipeDetailMVP.RecipeDetailPresenterInterface {

    @NonNull
    private Recipe recipe;
    private RecipeDetailMVP.RecipeDetailActivityInterface activity;
    private int currentStepIndex = 0;

    public RecipeDetailPresenter(@NonNull Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setActivity(RecipeDetailMVP.RecipeDetailActivityInterface activity) {
        this.activity = activity;
    }

    @Override
    public void setCurrentStepIndex(int currentStepIndex) {
        this.currentStepIndex = currentStepIndex;
    }

    @Override
    @NonNull
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public void onNextClick() {
        if(activity != null){
            currentStepIndex++;
            activity.navigateToRecipeStepDetail(
                recipe.getSteps().get(currentStepIndex)
                , this
                , currentStepIndex != 0
                , currentStepIndex != (recipe.getSteps().size() - 1)
            );
        }
    }

    @Override
    public void onBackClick() {
        if(activity != null){
            currentStepIndex--;
            activity.navigateToRecipeStepDetail(
                recipe.getSteps().get(currentStepIndex)
                , this
                , currentStepIndex != 0
                , currentStepIndex != (recipe.getSteps().size() - 1)
            );
        }
    }

    @Override
    public void onStepClick(Step step) {
        if(activity != null){
            currentStepIndex = recipe.getSteps().indexOf(step);
            activity.navigateToRecipeStepDetail(
                step
                , this
                , currentStepIndex != 0
                , currentStepIndex != (recipe.getSteps().size() - 1)
            );
        }
    }
}
