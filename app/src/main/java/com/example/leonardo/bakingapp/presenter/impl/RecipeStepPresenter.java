package com.example.leonardo.bakingapp.presenter.impl;

import android.content.Context;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;

import com.example.leonardo.bakingapp.api.entity.Step;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepMVP;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;


public class RecipeStepPresenter extends BaseObservable implements RecipeStepMVP.RecipeStepPresenterInterface {

    @NonNull
    private Step step;
    @NonNull
    private RecipeStepMVP.StepNavigationClickListener navigationClickListener;
    private int nextVisible;
    private int backVisible;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
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

    @Override
    public void initMediaSession(Context context, String tag, MediaSessionCompat.Callback callback){
        long position = 0;
        if(mMediaSession != null){
            position = getCurrentPlaybackPosition();
            releaseSession();
        }
        mMediaSession = new MediaSessionCompat(context, tag);
        mMediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
            | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PAUSE
                | PlaybackStateCompat.ACTION_PLAY
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                | PlaybackStateCompat.ACTION_PLAY_PAUSE
                | PlaybackStateCompat.ACTION_SEEK_TO
            );
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(callback);
        mMediaSession.setActive(true);
        mMediaSession.setPlaybackState(
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, position, 1f)
            .build()
        );
    }

    @Override
    public void releaseSession() {
        if(mMediaSession != null){
            mMediaSession.setCallback(null);
            mMediaSession.setActive(false);
            mMediaSession.release();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(mMediaSession != null){
            if((playbackState == Player.STATE_READY) && playWhenReady){
                mStateBuilder.setState(
                        PlaybackStateCompat.STATE_PLAYING
                        , getCurrentPlaybackPosition()
                        , 1f);
            } else if((playbackState == Player.STATE_READY)){
                mStateBuilder.setState(
                        PlaybackStateCompat.STATE_PAUSED
                        , getCurrentPlaybackPosition()
                        , 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }

    private long getCurrentPlaybackPosition(){
        return mMediaSession.getController().getPlaybackState().getPosition();
    }

    //region not implemented methods
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onRepeatModeChanged(int repeatMode) {}

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}

    @Override
    public void onPlayerError(ExoPlaybackException error) {}

    @Override
    public void onPositionDiscontinuity(int reason) {}

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

    @Override
    public void onSeekProcessed(){}
    //endregion
}
