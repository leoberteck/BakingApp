package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.bakingapp.BR;
import com.example.leonardo.bakingapp.adapter.RecipeStepListAdapter;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepListMVP;

public class RecipeStepListPresenter extends BaseObservable implements RecipeStepListMVP.RecipeStepListPresenterInterface {

    private RecipeStepListAdapter adapter;
    private RecipeStepListMVP.RecipeStepListFragmentInterface fragment;

    @NonNull
    private Recipe recipe;
    @NonNull
    private RecipeStepListAdapter.OnStepClickListener stepClickListener;

    public RecipeStepListPresenter(@NonNull Recipe recipe, @NonNull RecipeStepListAdapter.OnStepClickListener stepClickListener) {
        this.recipe = recipe;
        this.stepClickListener = stepClickListener;
    }

    @Override
    public RecyclerView.Adapter<?> getAdapter() {
        return adapter;
    }

    @Override
    public void setFragment(RecipeStepListMVP.RecipeStepListFragmentInterface fragment) {
        this.fragment = fragment;
    }

    @Override
    public void loadAdapter() {
        if(adapter == null){
            adapter = new RecipeStepListAdapter(recipe, stepClickListener);
            notifyPropertyChanged(BR.adapter);
        }
    }
}
