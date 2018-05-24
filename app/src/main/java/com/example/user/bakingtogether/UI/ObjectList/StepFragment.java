package com.example.user.bakingtogether.UI.ObjectList;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
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
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.bakingtogether.DB.AppRoomDatabase;
import com.example.user.bakingtogether.DB.StepEntity;
import com.example.user.bakingtogether.R;
import com.example.user.bakingtogether.UI.StepActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;


public class StepFragment extends Fragment {
    @BindView(R.id.description_step)
    TextView stepDescription;
    @BindView(R.id.previous_fab)
    FloatingActionButton previousFAB;
    @BindView(R.id.next_fab)
    FloatingActionButton nextFAB;
   /* @BindView(R.id.video_view)
    public PlayerView playerView;
    @BindView(R.id.image_logo)
    ImageView imageViewLogo;*/


    StepEntity currentStep;
    AppRoomDatabase roomDB;
    private List<StepEntity> stepsList;
    private int stepId;
    public SimpleExoPlayer player;
    public boolean playWhenReady;
    public long playBackPosition;
    public int currentWindow;


    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_details_fragment, container, false);
        ButterKnife.bind(this, rootView);
        roomDB = AppRoomDatabase.getsInstance(getContext());
        Bundle bundle = getArguments();
        currentStep = bundle.getParcelable("CurrentStep");
        stepsList = roomDB.recipeDao().getStepsByRecipeId(currentStep.getRecipeId());
        stepId = currentStep.getId();
        populateUI(stepId);

        nextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepId++;
                if(stepId == (stepsList.get(stepsList.size() -1).getId()));{
                populateUI(stepId);
                }
            }
        });
        previousFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepId--;
                populateUI(stepId);
            }
        });


        return rootView;
    }

    public void populateUI(int stepId){
        currentStep = roomDB.recipeDao().getStepByStepId(stepId);
        stepDescription.setText(currentStep.getDescription());

        ((StepActivity) getActivity())
                .setActionBarTitle(currentStep.getShortDescription());
        if (stepId == stepsList.get(0).getId()) {

            previousFAB.setVisibility(View.GONE);
            nextFAB.setVisibility(View.VISIBLE);


        } else{ if (stepId == (stepsList.get(stepsList.size() - 1).getId())) {

            previousFAB.setVisibility(View.VISIBLE);
            nextFAB.setVisibility(View.GONE);

        }else {
            previousFAB.setVisibility(View.VISIBLE);
            nextFAB.setVisibility(View.VISIBLE);
        }
        }
      /*  if(!TextUtils.isEmpty(currentStep.getVideoURL()) || !(currentStep.getVideoURL().equals(""))){
            imageViewLogo.setVisibility(View.GONE);

        initializePlayer(Uri.parse(currentStep.getVideoURL()));

        }else if(!TextUtils.isEmpty(currentStep.getThumbnailURL())|| !(currentStep.getThumbnailURL().equals(""))){
            imageViewLogo.setVisibility(View.GONE);
            initializePlayer(Uri.parse(currentStep.getThumbnailURL()));
        }else {
            playerView.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.ic_logo).into(imageViewLogo);
        }*/
    }
    
  /*  public void initializePlayer(Uri mediaUri){
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
        if(Util.SDK_INT > 23){
            initializePlayer(Uri.parse(currentStep.getVideoURL()));
        }
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
*/


}
