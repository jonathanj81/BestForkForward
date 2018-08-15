package com.example.jon.bestforkforward.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.bestforkforward.DataHandling.Step;
import com.example.jon.bestforkforward.R;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoDialogFragment extends DialogFragment {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static List<Step> mSteps;
    private int mPosition;
    private String mVideoURL;
    private boolean mReady = true;
    private long mSeekPosition = 0;

    private static final String POSITION_KEY = "position";
    private static final String VIDEO_FRAGMENT_NAME = "video_frag";

    public VideoDialogFragment() {

    }

    public static VideoDialogFragment newInstance(List<Step> steps, int pos) {
        mSteps = steps;
        VideoDialogFragment frag = new VideoDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_KEY, pos);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            mSeekPosition = savedInstanceState.getLong("video_position");
            mReady = savedInstanceState.getBoolean("player_ready");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPosition = getArguments().getInt(POSITION_KEY);

        setFragText(view);
        initializeButtons(view);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mVideoURL = mSteps.get(mPosition).getVideoURL();
        mPlayerView = view.findViewById(R.id.exoplayer_view);
        ImageView defaultArtwork = view.findViewById(R.id.video_dialog_default_artwork);
        String thumbnail = mSteps.get(mPosition).getThumbnailURL();
        if (!(thumbnail.isEmpty() || thumbnail.contains(".mp4"))) {
            Picasso.with(getContext()).load(Uri.parse(thumbnail)).into(defaultArtwork);
        }
        TextView defaultText = view.findViewById(R.id.no_video_textview);

        if (!(mVideoURL == null || mVideoURL.isEmpty())) {
            defaultArtwork.setVisibility(View.INVISIBLE);
            defaultText.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
        } else {
            defaultArtwork.setVisibility(View.VISIBLE);
            defaultText.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private void initializeButtons(View view) {
        ImageButton close = view.findViewById(R.id.video_dialog_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissCurrent();
            }
        });

        ImageButton right = view.findViewById(R.id.next_image_button);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition < mSteps.size() - 1) {
                    mPosition++;
                } else {
                    mPosition = 0;
                }
                dismissCurrent();
                startNewVideoFragment();
            }
        });

        ImageButton left = view.findViewById(R.id.previous_image_button);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition > 0) {
                    mPosition--;
                } else {
                    mPosition = mSteps.size() - 1;
                }
                dismissCurrent();
                startNewVideoFragment();
            }
        });
    }

    private void startNewVideoFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        VideoDialogFragment frag = VideoDialogFragment.
                newInstance(mSteps, mPosition);
        frag.show(fm, VIDEO_FRAGMENT_NAME);
    }

    private void setFragText(View view) {
        TextView title = view.findViewById(R.id.dialog_title);
        String titleText = StepsFragment.dessertName;
        if (mPosition > 0) {
            titleText = titleText + getResources().getString(R.string.video_title_step) + mPosition;
        }
        title.setText(titleText);

        TextView instructionView = view.findViewById(R.id.step_long_instruction);
        String instruction = mSteps.get(mPosition).getDescription();
        if (mPosition != 0) {
            instruction = instruction.substring(3);
        }
        instructionView.setText(instruction);
    }

    private void initializePlayer() {
        if (mExoPlayer == null && !(mVideoURL == null || mVideoURL.isEmpty())) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BestForkForward");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoURL), new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mReady);
            mExoPlayer.seekTo(mSeekPosition);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
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
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    private void dismissCurrent() {
        Fragment current = getActivity().getSupportFragmentManager().findFragmentByTag(VIDEO_FRAGMENT_NAME);
        if (current != null) {
            VideoDialogFragment df = (VideoDialogFragment) current;
            df.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        long playerPosition = mExoPlayer.getCurrentPosition();
        outState.putLong("video_position", playerPosition);
        boolean playerReady = mExoPlayer.getPlayWhenReady();
        outState.putBoolean("player_ready", playerReady);

        super.onSaveInstanceState(outState);
    }
}
