package com.example.user.bakingtogether.UI.ObjectList;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.bakingtogether.AppExecutors;
import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.TheRepository;
import com.example.user.bakingtogether.UI.StepActivity;
import com.example.user.bakingtogether.ViewModel.StepActivityViewModel;
import com.example.user.bakingtogether.ViewModel.StepViewModelFactory;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.bakingtogether.adapter.ListsAdapter.CURRENT_RECIPE;
import static com.example.user.bakingtogether.adapter.ListsAdapter.CURRENT_STEP;
import static com.example.user.bakingtogether.adapter.ListsAdapter.STEPS_LIST;

/**
 * Fragment that will be used to populate the step item on the StepActivity
 */
public class StepFragment extends Fragment implements View.OnClickListener {

    private static final int DEFAULT_VALUE = -1;
    private static final String PLAYER_POSITION = "PlayerPosition";
    private static final String APP_NAME = "BakingTogether";
    private static final String PLAY_WHEN_READY = "PlayWhenReady";
    private static final String CURRENT_WINDOW = "CurrentWindow";
    public SimpleExoPlayer player;
    public boolean playWhenReady = true;
    public long playBackPosition = -1;
    public int currentWindow;
    @BindView(R.id.description_step)
    TextView stepDescription;
    @BindView(R.id.previous_fab)
    FloatingActionButton previousFAB;
    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.image_logo)
    ImageView imageViewLogo;
    @BindView(R.id.scroling_view)
    NestedScrollView scrollView;
    @BindView(R.id.buttons_view)
    RelativeLayout buttonsView;
    private ArrayList<StepEntity> stepListCurrent;
    private StepActivityViewModel mViewModel;
    private StepViewModelFactory mStepFactory;
    private int recipeId, stepId;
    private View rootView;

    //Empty constructor
    public StepFragment() {
    }

    @SuppressLint("InlinedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.step_details_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(CURRENT_RECIPE, DEFAULT_VALUE);
            stepListCurrent = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            stepId = savedInstanceState.getInt(CURRENT_STEP, DEFAULT_VALUE);
            playBackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                recipeId = bundle.getInt(CURRENT_RECIPE);
                stepId = bundle.getInt(CURRENT_STEP);
                stepListCurrent = bundle.getParcelableArrayList(STEPS_LIST);
            }
        }
        if (recipeId != DEFAULT_VALUE && stepId != DEFAULT_VALUE) {
            AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(getContext());
            AppExecutors executors = AppExecutors.getInstance();
            RecipeApiInterface recipeApiInterface = ApiUtils.getRecipeInterfaceResponse();
            TheRepository repository = TheRepository.getsInstance(executors,
                    roomDB, roomDB.recipeDao(), recipeApiInterface);
            mStepFactory = new StepViewModelFactory(repository, recipeId, stepId);
        }
        mViewModel = ViewModelProviders.of(this, mStepFactory).get(StepActivityViewModel.class);

        nextFAB.setOnClickListener(this);
        previousFAB.setOnClickListener(this);
        mViewModel.getStep().observe(this, stepEntity -> {
            if (stepEntity != null) {
                populateUI(stepEntity);
            }
        });
        return rootView;
    }

    /**
     * Method that will populate the UI with the specified param
     *
     * @param stepEntity the step that will populate the UI
     */
    private void populateUI(StepEntity stepEntity) {

        //sets the ActionBar Title on the StepActivity
        ((StepActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(stepEntity.getShortDescription());
        //check if in the result from the API there are video, or thumbnail, if are information that
        // could be used it will load a video, or a thumbnail by each case, in other cases the
        // app's logo will be displayed
        String videoUrl = stepEntity.getVideoURL();
        String thumbnail = stepEntity.getThumbnailURL();
        if (videoUrl != null && !videoUrl.isEmpty()) {
            showLogoImage(false);
            initializePlayer(Uri.parse(videoUrl));
        } else if (thumbnail != null && !thumbnail.isEmpty()) {
            showLogoImage(true);
            Picasso.get().load(thumbnail).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo)
                    .into(imageViewLogo);
        } else {
            showLogoImage(true);
            Picasso.get().load(R.drawable.ic_logo).into(imageViewLogo);
        }
        //set's the UI for the landscape mode
        if (getResources().getBoolean(R.bool.isLandscape)) {
            nextFAB.setVisibility(View.GONE);
            previousFAB.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            buttonsView.setVisibility(View.GONE);
        } else if (!getResources().getBoolean(R.bool.isLandscape) || getResources().getBoolean(R.bool.isTablet)) {
            scrollView.setVisibility(View.VISIBLE);
            buttonsView.setVisibility(View.VISIBLE);
            stepDescription.setText(stepEntity.getDescription());
            //if the step is not the last step from the recipe NextButton will be Visible, else it
            // will be Invisible
            if (stepEntity.getId() != stepListCurrent.get(stepListCurrent.size() - 1).getId()) {
                nextFAB.setVisibility(View.VISIBLE);
            } else {
                nextFAB.setVisibility(View.INVISIBLE);
            }
            //if the step is not the last step from the recipe PreviousButton will be Visible, else it
            // will be Invisible
            if (stepEntity.getId() != stepListCurrent.get(0).getId()) {
                previousFAB.setVisibility(View.VISIBLE);
            } else {
                previousFAB.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showLogoImage(Boolean isShow) {
        if (isShow) {
            imageViewLogo.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.INVISIBLE);
        } else {
            imageViewLogo.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method that will initialize player for the video if the API has information for the video
     * @param mediaUri the Uri received from the API
     */
    public void initializePlayer(Uri mediaUri) {
        if (player == null) {
            //Create and instance for ExoPLayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);
            //prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), APP_NAME);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new
                    DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
        if (playBackPosition != -1) {
            player.seekTo(currentWindow, playBackPosition);
            player.setPlayWhenReady(playWhenReady);
            playBackPosition = -1;
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            playBackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            releasePlayer();
        }
    }

    @Override
    public void onClick(View v) {
        //for the nextBttn clicked it will be updated the UI with the next step, the ViewModel will
        // be set for the next step else if the PrevioustBttn was clicked it will be updated the
        // UI with the previous step, the ViewModel will be set for the previous step
        if (v.getId() == R.id.next_fab) {
            if (player != null) releasePlayer();
            stepId++;
            mViewModel.setStepId(stepId);
            mViewModel.setStep(stepId);
            StepEntity newStep = null;
            for (int i = 0; i < stepListCurrent.size(); i++) {
                if (stepListCurrent.get(i).getId() == stepId) {
                    newStep = stepListCurrent.get(i);
                }
            }
            populateUI(newStep);
        } else if (v.getId() == R.id.previous_fab) {
            if (player != null) releasePlayer();
            stepId--;
            StepEntity newStep = null;
            mViewModel.setStepId(stepId);
            mViewModel.setStep(stepId);
            for (int i = 0; i < stepListCurrent.size(); i++) {
                if (stepListCurrent.get(i).getId() == stepId) {
                    newStep = stepListCurrent.get(i);
                }
            }
            populateUI(newStep);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP, mViewModel.getStepIdInt());
        outState.putInt(CURRENT_RECIPE, recipeId);
        outState.putParcelableArrayList(STEPS_LIST, stepListCurrent);
        if (player != null) {
            outState.putLong(PLAYER_POSITION, player.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW, player.getCurrentWindowIndex());
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }
    }
}