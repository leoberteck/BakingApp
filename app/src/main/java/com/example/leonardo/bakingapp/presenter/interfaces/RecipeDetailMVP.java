package com.example.leonardo.bakingapp.presenter.interfaces;

import android.support.annotation.NonNull;

import com.example.leonardo.bakingapp.adapter.RecipeStepListAdapter;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;

public interface RecipeDetailMVP {

    interface RecipeDetailActivityInterface {
        void navigateToRecipeList(@NonNull Recipe recipe, @NonNull RecipeStepListAdapter.OnStepClickListener stepClickListener);
        void navigateToRecipeStepDetail(
            @NonNull Step step
            , @NonNull RecipeStepMVP.StepNavigationClickListener navigationClickListener
            , boolean showBackButton
            , boolean showNextButton
        );
    }

    interface RecipeDetailPresenterInterface
            extends BasePresenterInterface
            , RecipeStepListAdapter.OnStepClickListener
            , RecipeStepMVP.StepNavigationClickListener {
        void setActivity(RecipeDetailActivityInterface activity);

        void setCurrentStepIndex(int currentStepIndex);

        Recipe getRecipe();
    }
}
