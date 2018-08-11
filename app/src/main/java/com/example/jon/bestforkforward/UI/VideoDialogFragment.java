package com.example.jon.bestforkforward.UI;

import android.graphics.BitmapFactory;
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
import android.widget.EditText;
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

import java.util.List;

public class VideoDialogFragment extends DialogFragment {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static List<Step> mSteps;
    private int mPosition;

    private static final String POSITION_KEY = "position";
    private static final String VIDEO_FRAGMENT_NAME = "video_frag";

    public VideoDialogFragment(){

    }

    public static VideoDialogFragment newInstance(List<Step> steps, int pos){
        mSteps = steps;
        VideoDialogFragment frag = new VideoDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_KEY, pos);
        frag.setArguments(args);
        return frag;

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

        String videoURL = mSteps.get(mPosition).getVideoURL();
        mPlayerView = view.findViewById(R.id.exoplayer_view);
        ImageView defaultArtwork = view.findViewById(R.id.video_dialog_default_artwork);
        TextView defaultText = view.findViewById(R.id.no_video_textview);

        if (!(videoURL == null || videoURL.isEmpty())){
            defaultArtwork.setVisibility(View.INVISIBLE);
            defaultText.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoURL));
        } else {
            defaultArtwork.setVisibility(View.VISIBLE);
            defaultText.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    private void initializeButtons(View view){
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
                if (mPosition < mSteps.size()-1){
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
                if (mPosition > 0){
                    mPosition--;
                } else {
                    mPosition = mSteps.size()-1;
                }
                dismissCurrent();
                startNewVideoFragment();
            }
        });
    }

    private void startNewVideoFragment(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        VideoDialogFragment frag = VideoDialogFragment.
                newInstance(mSteps, mPosition);
        frag.show(fm, VIDEO_FRAGMENT_NAME);
    }

    private void setFragText(View view){
        TextView title = view.findViewById(R.id.dialog_title);
        String titleText = StepsFragment.dessertName;
        if (mPosition > 0){
            titleText = titleText + getResources().getString(R.string.video_title_step) + mPosition;
        }
        title.setText(titleText);

        TextView instructionView = view.findViewById(R.id.step_long_instruction);
        String instruction = mSteps.get(mPosition).getDescription();
        if (mPosition != 0){
            instruction = instruction.substring(3);
        }
        instructionView.setText(instruction);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BestForkForward");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    private void dismissCurrent(){
        Fragment current = getActivity().getSupportFragmentManager().findFragmentByTag(VIDEO_FRAGMENT_NAME);
        if (current != null) {
            VideoDialogFragment df = (VideoDialogFragment) current;
            df.dismiss();
        }
    }
}
