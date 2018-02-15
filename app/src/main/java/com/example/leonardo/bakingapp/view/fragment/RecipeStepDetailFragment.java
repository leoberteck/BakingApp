package com.example.leonardo.bakingapp.view.fragment;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leonardo.bakingapp.R;
import com.example.leonardo.bakingapp.databinding.RecipeDetailStepDetailFragmentBinding;
import com.example.leonardo.bakingapp.presenter.interfaces.RecipeStepMVP;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailFragment extends Fragment implements RecipeStepMVP.RecipeStepFragmentInterface {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static RecipeStepMVP.RecipeStepPresenterInterface presenter;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private RecipeDetailStepDetailFragmentBinding binding;

    public static RecipeStepDetailFragment newInstance(RecipeStepMVP.RecipeStepPresenterInterface presenter){
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        RecipeStepDetailFragment.presenter = presenter;
        return fragment;
    }

    public RecipeStepDetailFragment() {}

    @Override
    public void setPresenter(RecipeStepMVP.RecipeStepPresenterInterface presenter) {
        if(binding != null){
            binding.setPresenter(presenter);
            binding.notifyChange();
        }
    }

    public RecipeStepMVP.RecipeStepPresenterInterface getPresenter(){
        return binding.getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(container.getContext())
            , R.layout.recipe_detail_step_detail_fragment
            , container
            , false
        );
        View view = binding.getRoot();
        mPlayerView = view.findViewById(R.id.step_media_player_view);
        presenter.setFragment(this);
        this.setPresenter(presenter);
        return view;
    }

    private void initPlayer(Uri mediaUri) {
        if (mExoPlayer == null && mediaUri != null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getActivity()
                , trackSelector
                , loadControl
            );
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            //mExoPlayer.addListener(presenter);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                mediaUri
                , new DefaultDataSourceFactory(getActivity(), userAgent)
                , new DefaultExtractorsFactory()
                , null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(getPresenter().getCurrentPlayerPosition());
            mExoPlayer.setPlayWhenReady(getPresenter().getCurrentPlayingState());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer(getPresenter().getVideoUri());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initPlayer(getPresenter().getVideoUri());
        }
    }

    private void releasePlayer(){
        if(mExoPlayer != null){
            getPresenter().setCurrentPlayerPosition(mExoPlayer.getCurrentPosition());
            getPresenter().setCurrentPlayingState(mExoPlayer.getPlayWhenReady());
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
