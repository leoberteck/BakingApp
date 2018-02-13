package com.example.leonardo.bakingapp.presenter.interfaces;


import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.util.SimpleIdlingResource;

public interface RecipeListMVP {

    interface RecipeListActivity {
        void showRecipeDetails(Recipe recipe);

        void shareRecipe(String s);

        void setAdapter(RecyclerView.Adapter recipeListAdapter);

        void showToast(@StringRes int message);
    }

    interface RecipeListPresenterInterface extends BasePresenterInterface {
        void setActivity(RecipeListActivity activity);

        void loadRecipes(SimpleIdlingResource idlingResource);
    }
}
