package com.example.leonardo.bakingapp.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.leonardo.bakingapp.BR;
import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.presenter.impl.RecipeListPresenter;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP;
import com.example.leonardo.bakingapp.util.ResourceUtils;
import com.example.leonardo.bakingapp.util.SimpleIdlingResource;

public class RecipeListActivity extends AppCompatActivity implements RecipeListMVP.RecipeListActivity {

    private static RecipeListPresenter presenter;
    @Nullable
    private SimpleIdlingResource idlingResource;
    private RecyclerView movieGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding
            = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);

        movieGrid = findViewById(R.id.recyclerView_recipes);
        movieGrid.setLayoutManager(new GridLayoutManager(this, getOptimalNumberOfColumns(this)));

        if(presenter == null){
            presenter = new RecipeListPresenter(new ResourceUtils(getApplicationContext()));
        }

        viewDataBinding.setVariable(BR.presenter, presenter);
        presenter.setActivity(this);
        presenter.loadRecipes(idlingResource);
    }

    private int getOptimalNumberOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return Math.round(dpWidth / 300);
    }

    @Override
    public void showRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, recipe);
        startActivity(intent);
    }

    @Override
    public void shareRecipe(String s) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
        if(sharingIntent.resolveActivity(getPackageManager()) != null){
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_recipe)));
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter recipeListAdapter) {
        movieGrid.setAdapter(recipeListAdapter);
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @NonNull
    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if(idlingResource == null){
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @VisibleForTesting
    public RecipeListPresenter getPresenter() {
        return presenter;
    }
}
