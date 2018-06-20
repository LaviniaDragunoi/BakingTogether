package com.example.user.bakingtogether.UI.ObjectList;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.example.user.bakingtogether.adapter.ListsAdapter;
import com.example.user.bakingtogether.data.ApiUtils;
import com.example.user.bakingtogether.data.RecipeApiInterface;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment implements View.OnClickListener {


    private static final int DEFAULT_VALUE = -1;
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



    private StepEntity currentStep;
    public SimpleExoPlayer player;
    public boolean playWhenReady;
    public long playBackPosition;
    public int currentWindow;
    private StepActivityViewModel mViewModel;
    private StepViewModelFactory mStepFactory;

    private int recipeId, stepId;
    private View rootView;
    private PlaybackStateCompat.Builder mPositionBuilder;

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
            recipeId = savedInstanceState.getInt("CurrentRecipeId", DEFAULT_VALUE);
            stepListCurrent = savedInstanceState.getParcelableArrayList("ListOfSteps");
            stepId = savedInstanceState.getInt("CurrentStepId", DEFAULT_VALUE);
            playBackPosition = savedInstanceState.getLong("PlayerPosition");
            playWhenReady = savedInstanceState.getBoolean("PlayWhenReady");

        } else{
            Bundle bundle = getArguments();
            if (bundle != null) {
                recipeId = bundle.getInt("RecipeId");
                stepId = bundle.getInt("StepId");
                stepListCurrent = bundle.getParcelableArrayList("List");
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

    private void populateUI(StepEntity stepEntity) {

        ((StepActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(stepEntity.getShortDescription());

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


        if(getResources().getBoolean(R.bool.isLandscape)){
            nextFAB.setVisibility(View.GONE);
            previousFAB.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            buttonsView.setVisibility(View.GONE);
        }else if(!getResources().getBoolean(R.bool.isLandscape) || getResources().getBoolean(R.bool.isTablet)) {
            scrollView.setVisibility(View.VISIBLE);
            buttonsView.setVisibility(View.VISIBLE);
            stepDescription.setText(stepEntity.getDescription());
            if (stepEntity.getId() != stepListCurrent.get(stepListCurrent.size() - 1).getId()) {
                nextFAB.setVisibility(View.VISIBLE);
            } else {
                nextFAB.setVisibility(View.INVISIBLE);
            }

            if (stepEntity.getId() != stepListCurrent.get(0).getId()) {
                previousFAB.setVisibility(View.VISIBLE);
            } else {
                previousFAB.setVisibility(View.INVISIBLE);
            }

        }
}
    private void showLogoImage(Boolean isShow){
        if(isShow){
            imageViewLogo.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.INVISIBLE);
        }else { imageViewLogo.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);}
    }


    public void initializePlayer(Uri mediaUri) {
        if (player == null) {
            //Create and instance for ExoPLayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);
            //prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingTogether");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new
                    DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            if(playBackPosition != -1) {
                player.seekTo(playBackPosition);
                playBackPosition = -1;
                player.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            releasePlayer();
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
            releasePlayer();
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.next_fab){
            if(player != null) releasePlayer();
            stepId++;
            mViewModel.setStepId(stepId);
            mViewModel.setStep(stepId);
            StepEntity newStep = null;
            for(int i=0;i<stepListCurrent.size();i++){
                if(stepListCurrent.get(i).getId() == stepId){
                    newStep = stepListCurrent.get(i);
                }

            }
            populateUI(newStep);
    }else if(v.getId() == R.id.previous_fab){
           if(player != null) releasePlayer();
            stepId--;
            StepEntity newStep = null;
            mViewModel.setStepId(stepId);
            mViewModel.setStep(stepId);
            for(int i=0;i<stepListCurrent.size();i++){
                if(stepListCurrent.get(i).getId() == stepId){
                    newStep = stepListCurrent.get(i);
                }

            }
            populateUI(newStep);
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentStepId", mViewModel.getStepIdInt());
        outState.putInt("CurrentRecipeId", recipeId);
        outState.putParcelableArrayList("ListOfSteps", stepListCurrent);

        if(player != null) {
            outState.putLong("PlayerPosition", player.getCurrentPosition());
            outState.putBoolean("PlayWhenReady", playWhenReady);
        }
    }



}