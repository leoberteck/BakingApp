package com.example.leonardo.bakingapp.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.adapter.RecipeStepListAdapter;
import com.example.leonardo.bakingapp.api.entity.Recipe;
import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.presenter.impl.RecipeDetailPresenter;
import com.example.leonardo.bakingapp.presenter.impl.RecipeStepListPresenter;
import com.example.leonardo.bakingapp.presenter.impl.RecipeStepPresenter;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeDetailMVP;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepMVP;
import com.example.leonardo.bakingapp.view.fragment.RecipeStepDetailFragment;
import com.example.leonardo.bakingapp.view.fragment.RecipeStepListFragment;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailMVP.RecipeDetailActivityInterface {

    public static String RECIPE_EXTRA = "RECIPE_EXTRA";
    public static String RECIPE_STEP_TAG = RecipeStepListFragment.class.getSimpleName();
    public static String RECIPE_STEP_DETAIL_TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static RecipeDetailMVP.RecipeDetailPresenterInterface presenter;

    @IdRes
    private int masterFragment;
    @IdRes
    private int detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if(findViewById(R.id.fragment_master) != null){
            masterFragment = R.id.fragment_master;
            detailFragment = R.id.fragment_detail;
        } else {
            masterFragment = detailFragment = R.id.fragment_detail;
        }

        if(getIntent().hasExtra(RECIPE_EXTRA)){
            Recipe recipe = getIntent().getExtras().getParcelable(RECIPE_EXTRA);
            if(presenter == null){
                presenter = new RecipeDetailPresenter(recipe);
            }
            presenter.setActivity(this);

            if(getSupportFragmentManager().findFragmentById(masterFragment) == null){
                navigateToRecipeList(recipe, presenter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(masterFragment);
        if(fragment == null || fragment instanceof RecipeStepListFragment){
            finish();
        } else {
            navigateToRecipeList(presenter.getRecipe(), presenter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setActivity(null);
        if(isFinishing()){
            presenter = null;
        }
    }

    @Override
    public void navigateToRecipeList(@NonNull Recipe recipe, @NonNull RecipeStepListAdapter.OnStepClickListener stepClickListener) {
        navigate(masterFragment, RECIPE_STEP_TAG, RecipeStepListFragment.newInstance(
            new RecipeStepListPresenter(recipe, stepClickListener)
        ));
    }

    @Override
    public void navigateToRecipeStepDetail(
        @NonNull Step step
        , @NonNull RecipeStepMVP.StepNavigationClickListener navigationClickListener
        , boolean showBackButton
        , boolean showNextButton)
    {
        navigate(detailFragment, RECIPE_STEP_DETAIL_TAG + step.getId(), RecipeStepDetailFragment.newInstance(
            new RecipeStepPresenter(step, navigationClickListener, showBackButton, showNextButton)
        ));
    }

    private void navigate(@IdRes int container, String tag, Fragment newInstance){
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragment == null){
            fragment = newInstance;
        } else {
            //If fragment already exists, but in another container : remove and replace
            if(
                fragment.getView() == null
                || fragment.getView().getParent() == null
                || ((View)fragment.getView().getParent()).getId() != container
                ){
                transaction.remove(fragment);
                fragment = newInstance;
            }

        }
        transaction
            .replace(container, fragment, tag)
            .commit();
    }
}
