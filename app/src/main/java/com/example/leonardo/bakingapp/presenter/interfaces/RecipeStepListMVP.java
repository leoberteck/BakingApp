package com.example.leonardo.bakingapp.presenter.interfaces;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

public interface RecipeStepListMVP {

    interface RecipeStepListFragmentInterface {

    }

    interface RecipeStepListPresenterInterface extends BasePresenterInterface{
        @Bindable
        RecyclerView.Adapter<?> getAdapter();
        void setFragment(RecipeStepListFragmentInterface fragment);
        void loadAdapter();
    }
}