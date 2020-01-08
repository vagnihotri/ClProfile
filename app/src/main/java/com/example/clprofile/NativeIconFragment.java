package com.example.clprofile;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Constants;
import com.clevertap.android.sdk.Logger;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

public class NativeIconFragment extends NativeBaseFragment {

    private ImageView iconImage;
    private TextView title,message;
    private RelativeLayout clickLayout;

    public static NativeIconFragment newInstance(CleverTapDisplayUnit cleverTapDisplayUnit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_KEY, cleverTapDisplayUnit);

        NativeIconFragment fragment = new NativeIconFragment();
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
        View itemView = inflater.inflate(R.layout.native_carousel_icon, container, false);
        title = itemView.findViewById(R.id.messageTitle);
        message = itemView.findViewById(R.id.messageText);
        mediaImage = itemView.findViewById(R.id.media_image);
        iconImage = itemView.findViewById(R.id.image_icon);
        frameLayout = itemView.findViewById(R.id.icon_message_frame_layout);
        squareImage = itemView.findViewById(R.id.square_media_image);
        clickLayout = itemView.findViewById(R.id.click_relative_layout);
        ctaLinearLayout = itemView.findViewById(R.id.cta_linear_layout);
        progressBarFrameLayout = itemView.findViewById(R.id.icon_progress_frame_layout);
        mediaLayout = itemView.findViewById(R.id.media_layout);
        populate(itemView);
        if(cleverTapDisplayUnitContent.mediaIsVideo() || cleverTapDisplayUnitContent.mediaIsAudio()) {
            initializeVideo(getContext());
        }
        return itemView;
    }

    @Override
    protected void populate(View view) {
        title.setText(cleverTapDisplayUnitContent.getTitle());
        title.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getTitleColor()));
        message.setText(cleverTapDisplayUnitContent.getMessage());
        message.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getMessageColor()));
        clickLayout.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));
        frameLayout.setVisibility(View.GONE);
        mediaImage.setVisibility(View.GONE);
        mediaImage.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));
        squareImage.setVisibility(View.GONE);
        squareImage.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));
        mediaLayout.setVisibility(View.GONE);
        progressBarFrameLayout.setVisibility(View.GONE);

        try {
            if (cleverTapDisplayUnitContent.mediaIsImage()) {
                this.mediaLayout.setVisibility(View.VISIBLE);
                this.squareImage.setVisibility(View.VISIBLE);
                this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
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
                this.mediaLayout.setVisibility(View.VISIBLE);
                if(!cleverTapDisplayUnitContent.getPosterUrl().isEmpty()) {
                    this.squareImage.setVisibility(View.VISIBLE);
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }else {
                        this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                    try {
                        Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                        Glide.with(this.squareImage.getContext())
                                .load(cleverTapDisplayUnitContent.getPosterUrl())
                                .apply(new RequestOptions()
                                        .placeholder(Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL))
                                        .error(Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL)))
                                .into(this.squareImage);
                    }catch (NoSuchMethodError error){
                        Glide.with(this.squareImage.getContext())
                                .load(cleverTapDisplayUnitContent.getPosterUrl())
                                .into(this.squareImage);
                    }

                }else{
                    this.mediaLayout.setVisibility(View.VISIBLE);
                    this.squareImage.setVisibility(View.VISIBLE);
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        this.squareImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }else {
                        this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                    this.squareImage.setBackgroundColor(getImageBackgroundColor());
                    int drawableId = Utils.getThumbnailImage(context,Utils.VIDEO_THUMBNAIL);
                    if(drawableId != -1) {
                        Glide.with(this.squareImage.getContext())
                                .load(drawableId)
                                .into(this.squareImage);
                    }
                }
            } else if (cleverTapDisplayUnitContent.mediaIsAudio()){
                this.mediaLayout.setVisibility(View.VISIBLE);
                this.squareImage.setVisibility(View.VISIBLE);
                this.squareImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                this.squareImage.setBackgroundColor(getImageBackgroundColor() );
                int drawableId = Utils.getThumbnailImage(context,Utils.AUDIO_THUMBNAIL);
                if(drawableId != -1) {
                    Glide.with(this.squareImage.getContext())
                            .load(drawableId)
                            .into(this.squareImage);
                }
            }
            if (!cleverTapDisplayUnitContent.getIcon().isEmpty()) {
                iconImage.setVisibility(View.VISIBLE);
                try {
                    Glide.with(iconImage.getContext())
                            .load(cleverTapDisplayUnitContent.getIcon())
                            .apply(new RequestOptions()
                                    .placeholder(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER))
                                    .error(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER)))
                            .into(iconImage);
                }catch (NoSuchMethodError error){
                    Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                    Glide.with(iconImage.getContext())
                            .load(cleverTapDisplayUnitContent.getIcon())
                            .into(iconImage);
                }

            } else {
                iconImage.setVisibility(View.GONE);
            }
        } catch (NoClassDefFoundError error) {
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
