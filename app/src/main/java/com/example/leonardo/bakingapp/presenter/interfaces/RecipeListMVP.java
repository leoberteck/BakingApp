package com.example.leonardo.bakingapp.presenter.interfaces;


import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

public interface RecipeListMVP {

    interface RecipeListActivity {

    }

    interface RecipeListPresenterInterface extends BasePresenterInterface {
        @Bindable
        RecyclerView.Adapter<?> getAdapter();

        void setActivity(RecipeListActivity activity);

        void loadRecipes();
    }
}
