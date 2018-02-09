package com.example.leonardo.bakingapp.presenter.interfaces;


import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.bakingapp.api.entity.Recipe;

public interface RecipeListMVP {

    interface RecipeListActivity {
        void showRecipeDetails(Recipe recipe);
    }

    interface RecipeListPresenterInterface extends BasePresenterInterface {
        @Bindable
        RecyclerView.Adapter<?> getAdapter();

        void setActivity(RecipeListActivity activity);

        void loadRecipes();
    }
}
