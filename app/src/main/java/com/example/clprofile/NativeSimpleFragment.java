package com.example.clprofile;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Logger;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

public class NativeSimpleFragment extends NativeBaseFragment {


    private TextView title;
    private TextView message;

    public NativeSimpleFragment() {

    }

    public static NativeSimpleFragment newInstance(CleverTapDisplayUnit cleverTapDisplayUnit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_KEY, cleverTapDisplayUnit);

        NativeSimpleFragment fragment = new NativeSimpleFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CleverTapDisplayUnit cleverTapDisplayUnit = getArguments().getParcelable(ARG_KEY);
            configure(cleverTapDisplayUnit);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.native_simple_message, container, false);
        title = view.findViewById(R.id.messageTitle);
        message = view.findViewById(R.id.messageText);
        mediaImage = view.findViewById(R.id.media_image);
        relativeLayout = view.findViewById(R.id.simple_message_relative_layout);
        frameLayout = view.findViewById(R.id.simple_message_frame_layout);
        squareImage = view.findViewById(R.id.square_media_image);
        clickLayout = view.findViewById(R.id.click_relative_layout);
        bodyRelativeLayout = view.findViewById(R.id.body_linear_layout);
        progressBarFrameLayout = view.findViewById(R.id.simple_progress_frame_layout);
        mediaLayout = view.findViewById(R.id.media_layout);
        populate(view);
        if(cleverTapDisplayUnitContent.mediaIsVideo() || cleverTapDisplayUnitContent.mediaIsAudio()) {
            initializeVideo(getContext());
        }
        return view;
    }

    @Override
    protected void populate(View view) {
        this.title.setText(cleverTapDisplayUnitContent.getTitle());
        this.title.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getTitleColor()));
        this.message.setText(cleverTapDisplayUnitContent.getMessage());
        this.message.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getMessageColor()));
        this.bodyRelativeLayout.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));
        frameLayout.setVisibility(View.GONE);

        try {
            if (cleverTapDisplayUnitContent.mediaIsImage()) {
                this.mediaLayout.setVisibility(View.VISIBLE);
                this.squareImage.setVisibility(View.VISIBLE);
                this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try{
                    Glide.with(this.squareImage.getContext())
                            .load(cleverTapDisplayUnitContent.getMedia())
                            .apply(new RequestOptions()
                                    .placeholder(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER))
                                    .error(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER)))
                            .into(this.squareImage);
                }catch (NoSuchMethodError error){
                    Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                    Glide.with(this.squareImage.getContext())
                            .load(cleverTapDisplayUnitContent.getMedia())
                            .into(this.squareImage);
                }
            } else if (cleverTapDisplayUnitContent.mediaIsGIF()) {
                this.mediaLayout.setVisibility(View.VISIBLE);
                this.squareImage.setVisibility(View.VISIBLE);
                this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                try {
                    Glide.with(this.squareImage.getContext())
                            .asGif()
                            .load(cleverTapDisplayUnitContent.getMedia())
                            .apply(new RequestOptions()
                                    .placeholder(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER))
                                    .error(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER)))
                            .into(this.squareImage);
                }catch (NoSuchMethodError error){
                    Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                    Glide.with(this.squareImage.getContext())
                            .asGif()
                            .load(cleverTapDisplayUnitContent.getMedia())
                            .into(this.squareImage);
                }
            } else if (cleverTapDisplayUnitContent.mediaIsVideo()) {
                if(!cleverTapDisplayUnitContent.getPosterUrl().isEmpty()) {
                    this.mediaLayout.setVisibility(View.VISIBLE);
                    this.squareImage.setVisibility(View.VISIBLE);
                    if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }else {
                        this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                    try {
                        Glide.with(this.squareImage.getContext())
                                .load(cleverTapDisplayUnitContent.getPosterUrl())
                                .apply(new RequestOptions()
                                        .placeholder(Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL))
                                        .error(Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL)))
                                .into(this.squareImage);
                    }catch (NoSuchMethodError error){
                        Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                        Glide.with(this.squareImage.getContext())
                                .load(cleverTapDisplayUnitContent.getPosterUrl())
                                .into(this.squareImage);
                    }

                }else{
                    this.mediaLayout.setVisibility(View.VISIBLE);
                    this.squareImage.setVisibility(View.VISIBLE);
                    if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }else {
                        this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                    int drawableId = Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL);
                    if(drawableId != -1) {
                        Glide.with(this.squareImage.getContext())
                                .load(drawableId)
                                .into(this.squareImage);
                    }
                }
            }else if(cleverTapDisplayUnitContent.mediaIsAudio()){
                this.mediaLayout.setVisibility(View.VISIBLE);
                this.squareImage.setVisibility(View.VISIBLE);
                this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.squareImage.setBackgroundColor(getImageBackgroundColor());
                int drawableId = Utils.getThumbnailImage(context,Utils.AUDIO_THUMBNAIL);
                if(drawableId != -1) {
                    Glide.with(this.squareImage.getContext())
                            .load(drawableId)
                            .into(this.squareImage);
                }
            }
        }catch (NoClassDefFoundError error) {
            Logger.d("CleverTap SDK requires Glide dependency. Please refer CleverTap Documentation for more info");
        }
        this.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CleverTapAPI.getDefaultInstance(getContext().getApplicationContext())
                        .pushDisplayUnitClickedEventForID(cleverTapDisplayUnit.getUnitID());
                String actionUrl = cleverTapDisplayUnitContent.getActionUrl();
                fireUrlThroughIntent(actionUrl);
            }
        });
    }
}
