package com.example.leonardo.bakingapp.presenter.interfaces;

import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.Nullable;

public interface RecipeStepMVP {

    interface RecipeStepFragmentInterface {

        void setPresenter(RecipeStepPresenterInterface presenter);
    }

    interface RecipeStepPresenterInterface extends BasePresenterInterface {

        long getCurrentPlayerPosition();

        void setCurrentPlayerPosition(long currentPlayerPosition);

        void setFragment(RecipeStepFragmentInterface fragment);

        @Nullable
        Uri getVideoUri();

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

        boolean getCurrentPlayingState();

        void setCurrentPlayingState(boolean playWhenReady);
    }

    interface StepNavigationClickListener{
        void onNextClick();
        void onBackClick();
    }
}
