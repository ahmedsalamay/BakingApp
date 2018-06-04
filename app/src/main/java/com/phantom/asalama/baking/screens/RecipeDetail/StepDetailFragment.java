package com.phantom.asalama.baking.screens.RecipeDetail;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.phantom.asalama.baking.BakingApplication;
import com.phantom.asalama.baking.R;
import com.phantom.asalama.baking.models.Step;
import com.phantom.asalama.baking.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mEmptyState;
    private ImageView mThumbnailImgView;
    private Step mStep;
    private Button nextBtn;
    private Button prevBtn;
    private ArrayList<Step> mSteps;
    private Picasso mPicasso;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStep = new Step();
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStep = getArguments().getParcelable(ARG_ITEM_ID);
            mSteps = getArguments().getParcelableArrayList(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        mPicasso = ((BakingApplication) getActivity().getApplication()).getmPicasso();
        mPlayerView = rootView.findViewById(R.id.player_view);
        mEmptyState = rootView.findViewById(R.id.empty_state_video);
        mThumbnailImgView = rootView.findViewById(R.id.step_image);

        TextView stepDescription = rootView.findViewById(R.id.step_description);
        stepDescription.setText(mStep.getDescription());

        nextBtn = rootView.findViewById(R.id.next_step_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_ITEM_ID, mSteps.get(mStep.getId() + 1));
                intent.putParcelableArrayListExtra
                        (Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, mSteps);
                getActivity().startActivity(intent);

                getActivity().finish();
            }
        });
        if (mStep.getId() == mSteps.size() - 1)
            nextBtn.setVisibility(View.INVISIBLE);
        else nextBtn.setVisibility(View.VISIBLE);

        prevBtn = rootView.findViewById(R.id.prev_step_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_ITEM_ID, mSteps.get(mStep.getId() - 1));
                intent.putParcelableArrayListExtra
                        (Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, mSteps);
                getActivity().startActivity(intent);

                getActivity().finish();
            }
        });
        if (mStep.getId() == 0)
            prevBtn.setVisibility(View.INVISIBLE);
        else prevBtn.setVisibility(View.VISIBLE);
        if (Utility.isTablet(getActivity()) && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
        }


        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE
                && !Utility.isTablet(getActivity())) {
            //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
            // getActivity().getWindow()
            //    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            //     WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mPlayerView.getLayoutParams().height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            nextBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
            stepDescription.setVisibility(View.GONE);
        }

        return rootView;
    }

    private String handleVideoCases() {
        String url = "";
        if (!TextUtils.isEmpty(mStep.getVideoURL())) {
            url = mStep.getVideoURL();
            mEmptyState.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            mThumbnailImgView.setVisibility(View.INVISIBLE);
            initPlayer();
            return url;
        } else if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
            url = mStep.getThumbnailURL();
            mEmptyState.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
            mThumbnailImgView.setVisibility(View.VISIBLE);
            mPicasso.load(mStep.getThumbnailURL())
                    .placeholder(R.drawable.recipe_default_image)
                    .error(R.drawable.recipe_default_image)
                    .into(mThumbnailImgView);
            return url;
        } else {
            mEmptyState.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.INVISIBLE);
            mThumbnailImgView.setVisibility(View.INVISIBLE);
        }
        return url;
    }

    private void initPlayer() {
        String url = mStep.getVideoURL();

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource
                (Uri.parse(url)
                        , new DefaultDataSourceFactory(getActivity(), userAgent),
                        new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
        //Stick with the course content even it is deprecated!  hmmm
        // Measures bandwidth during playback. Can be null if not required.
       /* BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer =
                ExoPlayerFactory.newSimpleInstance(getContext() , trackSelector);
        mPlayerView.setPlayer(mPlayer);
        String userAgent= Util.getUserAgent(getContext(), "yourApplicationName");
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext()
                ,userAgent
                , defaultBandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4"));
        // Prepare the player with the source.
        mPlayer.prepare(videoSource);*/
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        handleVideoCases();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

}
