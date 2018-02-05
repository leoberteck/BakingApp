package com.example.leonardo.bakingapp.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.leonardo.bakingapp.BR;
import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.presenter.impl.RecipeListPresenter;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeListMVP;

public class RecipeListActivity extends AppCompatActivity implements RecipeListMVP.RecipeListActivity {

    private static RecipeListPresenter presenter = new RecipeListPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding
            = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        viewDataBinding.setVariable(BR.presenter, presenter);
        presenter.setActivity(this);
        presenter.loadRecipes();

        RecyclerView movieGrid = findViewById(R.id.recyclerView_recipes);
        movieGrid.setLayoutManager(new GridLayoutManager(this, getOptimalNumberOfColumns(this)));
    }

    private int getOptimalNumberOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return Math.round(dpWidth / 300);
    }
}
