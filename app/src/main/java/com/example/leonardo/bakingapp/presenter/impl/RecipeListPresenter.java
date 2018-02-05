package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.example.leonardo.bakingapp.BR;
import com.example.leonardo.bakingapp.adapter.RecipeListAdapter;
import com.example.leonardo.bakingapp.api.RecipeAPI;
import com.example.leonardo.bakingapp.api.entity.Recipe;

import static com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP.RecipeListActivity;
import static com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP.RecipeListPresenterInterface;

public class RecipeListPresenter extends BaseObservable implements RecipeListPresenterInterface, RecipeListAdapter.OnRecipeClickListener {

    private RecipeListAdapter adapter;
    private RecipeListActivity activity;

    @Override
    public RecyclerView.Adapter<?> getAdapter() {
        return adapter;
    }

    void setAdapter(RecipeListAdapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void setActivity(RecipeListActivity activity) {
        this.activity = activity;
    }

    @Override
    public void loadRecipes(){
        if(adapter == null){
            new LoadMoviesTask(this).execute();
        }
    }

    @Override
    public void onViewRecipeClick(Recipe recipe) {
        //TODO: implement view recipe logic
    }

    @Override
    public void onShareClick(Recipe recipe) {
        //TODO: implement share recipe logic
    }

    private static class LoadMoviesTask extends AsyncTask<Void, Void, RecipeListAdapter> {

        @NonNull
        RecipeListPresenter caller;

        public LoadMoviesTask(@NonNull RecipeListPresenter caller) {
            this.caller = caller;
        }

        @Override
        protected RecipeListAdapter doInBackground(Void... voids) {
            return new RecipeListAdapter(RecipeAPI.getAllRecipies(), caller);
        }

        @Override
        protected void onPostExecute(RecipeListAdapter recipeListAdapter) {
            super.onPostExecute(recipeListAdapter);
            caller.setAdapter(recipeListAdapter);
        }
    }
}
