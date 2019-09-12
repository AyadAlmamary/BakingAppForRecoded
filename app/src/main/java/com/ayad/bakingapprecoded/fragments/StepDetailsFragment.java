package com.ayad.bakingapprecoded.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ayad.bakingapprecoded.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    private String mVideoURL;
    private String mThumbnailURL;
    private String mDescription;

    @BindView(R.id.tv_instructions_layout)
    LinearLayout instructionsLayout;
    @BindView(R.id.tv_instructions)
    TextView instructionsTextView;
    @BindView(R.id.thumbnailView)
    ImageView thumbnailImageView;
    @BindView(R.id.playerView)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.media_frame)
    FrameLayout mediaFrameLayout;

    private SimpleExoPlayer mExoPlayer;
    private long mPlaybackPosition;
    private boolean mPlayWhenReady;

    public static final String PLAYBACK_POSITION = "playPosition";
    public static final String PLAY_WHEN_READY = "playWhenReady";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            mVideoURL = args.getString("video_url");
            mThumbnailURL = args.getString("thumbnail_url");
            mDescription = args.getString("description");
        }

        // Retrieve the playback position if it's previously saved
        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        } else {
            mPlayWhenReady = true;
        }

        if (TextUtils.isEmpty(mVideoURL)) {
            simpleExoPlayerView.setVisibility(View.GONE);
        }

        // Set the step thumbnail image if available
        if (TextUtils.isEmpty(mThumbnailURL)) {
            if (TextUtils.isEmpty(mVideoURL)) {
                mediaFrameLayout.setVisibility(View.GONE);
            }
        } else {
            Picasso.get()
                    .load(mThumbnailURL)
                    .placeholder(R.drawable.placeholder)
                    .into(thumbnailImageView);
        }

        // Set the step instructions if available
        if (TextUtils.isEmpty(mDescription)) {
            instructionsTextView.setText(getString(R.string.msg_no_instructions));
        } else {
            instructionsTextView.setText(mDescription);
        }

        // Display the player in full view when orientation is landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            instructionsLayout.setVisibility(View.GONE);
        }

        return rootView;
    }

    /**
     * ExoPlayer starting.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            MediaSource mediaSource = new ExtractorMediaSource.Factory
                    (new DefaultHttpDataSourceFactory(getString(R.string.app_name))).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mPlaybackPosition);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!TextUtils.isEmpty(mVideoURL) && Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(mVideoURL));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mVideoURL) && (Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(mVideoURL));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!TextUtils.isEmpty(mVideoURL) && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < Build.VERSION_CODES.P && !TextUtils.isEmpty(mVideoURL)) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
        outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
    }
}
