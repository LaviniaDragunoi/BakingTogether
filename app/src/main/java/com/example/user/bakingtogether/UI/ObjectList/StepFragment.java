package com.example.user.bakingtogether.UI.ObjectList;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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


public class StepFragment extends Fragment {


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
    private ArrayList<StepEntity> stepListCurrent;;

    private StepEntity currentStep;
    public SimpleExoPlayer player;
    public boolean playWhenReady;
    public long playBackPosition;
    public int currentWindow;
    private AppRoomDatabase roomDB;
    private TheRepository repository;
    private StepActivityViewModel mViewModel;
    private StepViewModelFactory mStepFactory;


       private int first,last;



    public StepFragment(){}

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null){
//
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_details_fragment, container, false);
        ButterKnife.bind(this, rootView);

//        if(savedInstanceState != null){
//        currentStep = getArguments().getParcelable("CurrentStepDetails");
//        first = getArguments().getInt("First");
//        last = getArguments().getInt("Last");
//        }
        mViewModel = ViewModelProviders.of(getActivity()).get(StepActivityViewModel.class);
mViewModel.mStepId.observe(getActivity(), new Observer<Integer>() {
    @Override
    public void onChanged(@Nullable Integer integer) {
        if(integer != null){
          //  populateUI(mViewModel.getStep().getValue(),first,last);
        }
    }
});

        return rootView;
    }

    public void populateUI(StepEntity step, int firstStep, int lastStep){

            stepDescription.setText(step.getDescription());
            ((StepActivity) Objects.requireNonNull(getActivity()))
                    .setActionBarTitle(step.getShortDescription());
       if(!TextUtils.isEmpty(step.getVideoURL()) || !(step.getVideoURL().equals(""))){
            imageViewLogo.setVisibility(View.GONE);
            initializePlayer(Uri.parse(step.getVideoURL()));
        }else if(!TextUtils.isEmpty(step.getThumbnailURL())|| !(step.getThumbnailURL().equals(""))){
            imageViewLogo.setVisibility(View.GONE);
            initializePlayer(Uri.parse(step.getThumbnailURL()));
        }else {
            playerView.setVisibility(View.GONE);
            imageViewLogo.setVisibility(View.VISIBLE);
           Picasso.get().load(R.drawable.ic_logo).into(imageViewLogo);
        }
        nextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.mStepId.setValue(step.getId() + 1);
                mViewModel.setStepId(step.getId()+1);
                Intent intent  = new Intent(getContext(), StepActivity.class);
                intent.putExtra("NewStepId", mViewModel.getStepId().getValue());
                intent.putExtra("FirstStepId", firstStep);
                intent.putExtra("LastStepId", lastStep);
                intent.putExtra("RecipeId", step.getRecipeId());
                player.stop();
                startActivity(intent);
            }
        });
        previousFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.mStepId.setValue(step.getId() - 1);
                mViewModel.setStepId(step.getId()-1);
                Intent intent  = new Intent(getContext(), StepActivity.class);
                intent.putExtra("NewStepId", mViewModel.getStepId().getValue());
                intent.putExtra("FirstStepId", firstStep);
                intent.putExtra("LastStepId", lastStep);
                intent.putExtra("RecipeId", step.getRecipeId());
                player.stop();
                startActivity(intent);
            }
        });


        if (step.getId() != firstStep &&
                step.getId() != lastStep) {
            previousFAB.setVisibility(View.VISIBLE);
            nextFAB.setVisibility(View.VISIBLE);
        } else {
            if (step.getId() == firstStep &&
                    step.getId() != lastStep) {
                previousFAB.setVisibility(View.GONE);
                nextFAB.setVisibility(View.VISIBLE);
            } else if(step.getId() != firstStep &&
                    step.getId() == lastStep) {
                previousFAB.setVisibility(View.VISIBLE);
                nextFAB.setVisibility(View.GONE);
            }
        }


    }

    public void initializePlayer(Uri mediaUri){
        if(player == null){
            //Create and instance for ExoPLayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            //   LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);
            //prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingTogether");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new
                    DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            player .prepare(mediaSource);
            player.setPlayWhenReady(true);

        }
    }
   @Override
    public void onStart() {
        super.onStart();
//       if(is){
//           if(!TextUtils.isEmpty(currentStep.getVideoURL()) || !(currentStep.getVideoURL().equals(""))) {
//               initializePlayer(Uri.parse(currentStep.getVideoURL()));
//           } else if(!TextUtils.isEmpty(currentStep.getThumbnailURL())|| !(currentStep.getThumbnailURL().equals(""))){
//               imageViewLogo.setVisibility(View.GONE);
//               initializePlayer(Uri.parse(currentStep.getThumbnailURL()));
//           }else {
//               playerView.setVisibility(View.GONE);
//               imageViewLogo.setVisibility(View.VISIBLE);
//               Picasso.get().load(R.drawable.ic_logo).into(imageViewLogo);
//           }

    }
    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23){
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
        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }
        }

