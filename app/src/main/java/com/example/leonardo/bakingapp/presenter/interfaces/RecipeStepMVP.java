package com.example.leonardo.bakingapp.presenter.interfaces;

import android.content.Context;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.Player;

public interface RecipeStepMVP {

    interface RecipeStepFragmentInterface {

        void setPresenter(RecipeStepPresenterInterface presenter);
    }

    interface RecipeStepPresenterInterface extends BasePresenterInterface, Player.EventListener {

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

        void initMediaSession(Context context, String tag, MediaSessionCompat.Callback callback);

        void releaseSession();
    }

    interface StepNavigationClickListener{
        void onNextClick();
        void onBackClick();
    }
}
