package com.example.leonardo.bakingapp.presenter.impl;

import android.databinding.BaseObservable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepMVP;


public class RecipeStepPresenter extends BaseObservable implements RecipeStepMVP.RecipeStepPresenterInterface {

    @NonNull
    private Step step;
    @NonNull
    private RecipeStepMVP.StepNavigationClickListener navigationClickListener;
    private int nextVisible;
    private int backVisible;
    private RecipeStepMVP.RecipeStepFragmentInterface fragment;

    public RecipeStepPresenter(
        @NonNull Step step
        , @NonNull RecipeStepMVP.StepNavigationClickListener navigationClickListener
        , boolean showBackButton
        , boolean showNextButton) {

        this.step = step;
        this.navigationClickListener = navigationClickListener;
        nextVisible = showNextButton ? View.VISIBLE : View.GONE;
        backVisible = showBackButton ? View.VISIBLE : View.GONE;
    }

    @Override
    public void setFragment(RecipeStepMVP.RecipeStepFragmentInterface fragment) {
        this.fragment = fragment;
    }

    @Override
    @Nullable
    public Uri getMediaUri(){
        Uri uri = null;
        String url = step.getMediaURL();
        if(!TextUtils.isEmpty(url)){
            uri = Uri.parse(url);
        }
        return uri;
    }

    @Override
    public String getStepShortDescription() {
        return step.getShortDescription();
    }

    @Override
    public String getStepDescription() {
        return step.getDescription();
    }

    @Override
    public int getNextVisible() {
        return nextVisible;
    }

    @Override
    public int getBackVisible() {
        return backVisible;
    }

    @Override
    public RecipeStepMVP.StepNavigationClickListener getListener() {
        return navigationClickListener;
    }
}
