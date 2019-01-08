package com.chandan.android.bakingapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.chandan.android.bakingapp.R;
import com.chandan.android.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.chandan.android.bakingapp.model.RecipeStepsData;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

public class RecipeDetailFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String RECIPE_LIST_KEY = "recipe_list_key";

    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";

    private RecipeStepsData recipeStepsData = null;
    MediaPlayerStateListener mCallback;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;

    public interface MediaPlayerStateListener {
        void onMediaPlayerError(String error);
        void onMediaPlayerNoUrlAvailable();
    }

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public void setRecipeStepsData(RecipeStepsData recipeStepsData) {
        this.recipeStepsData = recipeStepsData;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            recipeStepsData = savedInstanceState.getParcelable(RECIPE_LIST_KEY);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }

        FragmentRecipeDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);
        View rootView = binding.getRoot();

        binding.setRecipestepsdata(recipeStepsData);

        mPlayerView = binding.playerView;
        binding.playerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.play_button_image));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        initFullscreenDialog();
        initFullscreenButton();

        String mediaUrlStr = "";
        if (recipeStepsData.getStepVideoUrl() != null && !recipeStepsData.getStepVideoUrl().equals("")) {
            mediaUrlStr = recipeStepsData.getStepVideoUrl();
        } else if (recipeStepsData.getStepThumbnailUrl() != null && !recipeStepsData.getStepThumbnailUrl().equals("")) {
            mediaUrlStr = recipeStepsData.getStepThumbnailUrl();
        } else {
            if (mCallback != null) {
                mCallback.onMediaPlayerNoUrlAvailable();
            }
        }

        initializePlayer(Uri.parse(mediaUrlStr));

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelable(RECIPE_LIST_KEY, recipeStepsData);
        currentState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        currentState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        currentState.putLong(STATE_RESUME_POSITION, mResumePosition);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mResumeWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayerView.getPlayer().getCurrentPosition());

            mPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (MediaPlayerStateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

            if (haveResumePosition) {
                mPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
            }

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void initFullscreenButton() {

        PlaybackControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        //((FrameLayout) getView().findViewById(R.id.main_media_frame).
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    // ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady) { //Playing

        } else if((playbackState == ExoPlayer.STATE_READY)) { //Paused

        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mCallback != null) {
            mCallback.onMediaPlayerError(error.getLocalizedMessage());
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
