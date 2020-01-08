package com.example.clprofile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Constants;
import com.clevertap.android.sdk.Logger;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONObject;

import java.util.Iterator;

public class NativeBaseFragment extends Fragment {

    protected static final String ARG_KEY = "ctdu";

    RelativeLayout relativeLayout,clickLayout;
    LinearLayout ctaLinearLayout,bodyRelativeLayout;
    FrameLayout frameLayout;
    Context context;
    ImageView mediaImage,squareImage;
    FrameLayout progressBarFrameLayout;
    ImageView muteIcon;
    RelativeLayout mediaLayout;

    SimpleExoPlayer player;
    //surface view for playing video
    private PlayerView videoSurfaceView;
    private Context appContext;

    protected CleverTapDisplayUnitContent cleverTapDisplayUnitContent;

    protected CleverTapDisplayUnit cleverTapDisplayUnit;

    protected boolean requiresMediaPlayer;

    protected void configure (CleverTapDisplayUnit cleverTapDisplayUnit) {
        this.cleverTapDisplayUnit = cleverTapDisplayUnit;
        this.cleverTapDisplayUnitContent = cleverTapDisplayUnit.getContents().get(0);
        requiresMediaPlayer = cleverTapDisplayUnitContent.mediaIsAudio() || cleverTapDisplayUnitContent.mediaIsVideo();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

    }

    protected void populate(View view) {
        CleverTapAPI.getDefaultInstance(getContext().getApplicationContext()).pushDisplayUnitViewedEventForID(cleverTapDisplayUnit.getUnitID());
    }

    private FrameLayout getLayoutForMediaPlayer() {
        return frameLayout;
    }

    int getImageBackgroundColor() {
        return Color.TRANSPARENT;
    }

    boolean needsMediaPlayer () {
        return requiresMediaPlayer;
    }

    boolean shouldAutoPlay() {
        return cleverTapDisplayUnitContent.mediaIsVideo();
    }

    void playerReady() {
        FrameLayout frameLayout = getLayoutForMediaPlayer();
        frameLayout.setVisibility(View.VISIBLE);
        if (muteIcon != null) {
            muteIcon.setVisibility(View.VISIBLE);
        }
        if (progressBarFrameLayout != null) {
            progressBarFrameLayout.setVisibility(View.GONE);
        }
    }

    void playerRemoved() {
        if (progressBarFrameLayout != null) {
            progressBarFrameLayout.setVisibility(View.GONE);
        }
        if (muteIcon != null) {
            muteIcon.setVisibility(View.GONE);
        }
        FrameLayout frameLayout = getLayoutForMediaPlayer();
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
    }

    void playerBuffering(){
        if (progressBarFrameLayout != null) {
            progressBarFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    Bitmap drawableToBitmap(Drawable drawable)
            throws NullPointerException {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    boolean addMediaPlayer(PlayerView videoSurfaceView) {
        if (!requiresMediaPlayer) {
            return false;
        }
        FrameLayout frameLayout = getLayoutForMediaPlayer();
        if (frameLayout == null) {
            return false;
        }
        frameLayout.removeAllViews();
        frameLayout.setVisibility(View.GONE); // Gets set visible in playerReady

        final Resources resources = context.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        int width = resources.getDisplayMetrics().widthPixels;
        int height = width;

        videoSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(width,height));

        frameLayout.addView(videoSurfaceView);
        frameLayout.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));

        if (progressBarFrameLayout != null) {
            progressBarFrameLayout.setVisibility(View.VISIBLE);
        }

        final SimpleExoPlayer player = (SimpleExoPlayer) videoSurfaceView.getPlayer();
        float currentVolume = player.getVolume();
        if (cleverTapDisplayUnitContent.mediaIsVideo()) {
            muteIcon = new ImageView(context);
            muteIcon.setVisibility(View.GONE);
            if (currentVolume > 0) {
                muteIcon.setImageDrawable(context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_volume_on));
            } else {
                muteIcon.setImageDrawable(context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_volume_off));
            }

            int iconWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            int iconHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(iconWidth, iconHeight);
            int iconTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics);
            int iconRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);
            layoutParams.setMargins(0, iconTop, iconRight, 0);
            layoutParams.gravity = Gravity.END;
            muteIcon.setLayoutParams(layoutParams);
            muteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float currentVolume = player.getVolume();
                    if (currentVolume > 0) {
                        player.setVolume(0f);
                        muteIcon.setImageDrawable(resources.getDrawable(com.clevertap.android.sdk.R.drawable.ct_volume_off));
                    } else if (currentVolume == 0) {
                        player.setVolume(1);
                        muteIcon.setImageDrawable(resources.getDrawable(com.clevertap.android.sdk.R.drawable.ct_volume_on));
                    }
                }
            });
            frameLayout.addView(muteIcon);
        }

        videoSurfaceView.requestFocus();
        videoSurfaceView.setShowBuffering(false);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.getPackageName()), defaultBandwidthMeter);
        String uriString = cleverTapDisplayUnitContent.getMedia();
        if (uriString != null) {
            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(uriString));
            // Prepare the player with the source.
            player.prepare(hlsMediaSource);
            if(cleverTapDisplayUnitContent.mediaIsAudio()) {
                videoSurfaceView.showController();//show controller for audio as it is not autoplay
                player.setPlayWhenReady(false);
                player.setVolume(1f);
            }else if(cleverTapDisplayUnitContent.mediaIsVideo()){
                player.setPlayWhenReady(true);
                player.setVolume(currentVolume);
            }
        }
        return true;
    }

    protected void initializeVideo(Context context) {
        appContext = context.getApplicationContext();
        videoSurfaceView = new PlayerView(appContext);
        videoSurfaceView.setBackgroundColor(Color.TRANSPARENT);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }else{
            videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
        videoSurfaceView.setUseArtwork(true);
        Drawable artwork = context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_audio);
        videoSurfaceView.setDefaultArtwork(drawableToBitmap(artwork));

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(appContext, trackSelector);
        player.setVolume(0f); // start off muted
        videoSurfaceView.setUseController(true);
        videoSurfaceView.setControllerAutoShow(false);
        videoSurfaceView.setPlayer(player);


        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        playerBuffering();
                        break;
                    case Player.STATE_ENDED:
                        if (player != null) {
                            player.seekTo(0);
                            player.setPlayWhenReady(false);
                            if (videoSurfaceView != null) {
                                videoSurfaceView.showController();
                            }
                        }
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        playerReady();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {}
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
            @Override
            public void onLoadingChanged(boolean isLoading) {}
            @Override
            public void onRepeatModeChanged(int repeatMode) {}
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}
            @Override
            public void onPlayerError(ExoPlaybackException error) {}
            @Override
            public void onPositionDiscontinuity(int reason) {}
            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
            @Override
            public void onSeekProcessed() {}
        });
        addMediaPlayer(videoSurfaceView);
    }

    void handleViewPagerClick(int position) {
        try {
            //TO DO: didClick(data, position, null);
            String actionUrl = cleverTapDisplayUnit.getContents().get(position).getActionUrl();
            Utils.fireUrlThroughIntent(getContext(), actionUrl);
        } catch (Throwable t) {
            Logger.d("Error handling notification button click: " + t.getCause());
        }
    }

    protected void fireUrlThroughIntent(String actionUrl) {
        if(actionUrl == null || actionUrl.isEmpty()) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionUrl.replace("\n", "").replace("\r", "")));
            startActivity(intent);
        } catch (Throwable t) {
            // Ignore
        }
    }
}
