package com.example.leonardo.bakingapp.presenter.interfaces;

import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.Nullable;

public interface RecipeStepMVP {

    interface RecipeStepFragmentInterface {

        void setPresenter(RecipeStepPresenterInterface presenter);
    }

    interface RecipeStepPresenterInterface extends BasePresenterInterface {

        void setFragment(RecipeStepFragmentInterface fragment);

        @Nullable
        Uri getMediaUri();

        @Bindable
        String getStepShortDescription();
        @Bindable
        String getStepDescription();

        @Bindable
        int getNextVisible();
        @Bindable
        int getBackVisible();

        @Bindable
        StepNavigationClickListener getListener();
    }

    interface StepNavigationClickListener{
        void onNextClick();
        void onBackClick();
    }
}
