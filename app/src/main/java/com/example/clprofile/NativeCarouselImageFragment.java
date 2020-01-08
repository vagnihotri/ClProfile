package com.example.clprofile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.clevertap.android.sdk.CTCarouselViewPager;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

public class NativeCarouselImageFragment extends NativeBaseFragment {

    private CTCarouselViewPager imageViewPager;
    private LinearLayout sliderDots;
    private TextView title,message;
    private RelativeLayout clickLayout;
    private Context appContext;
    private NativeCarouselPageChangeListener carouselPageChangeListener;

    public static NativeCarouselImageFragment newInstance(CleverTapDisplayUnit cleverTapDisplayUnit) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_KEY, cleverTapDisplayUnit);

        NativeCarouselImageFragment fragment = new NativeCarouselImageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CleverTapDisplayUnit cleverTapDisplayUnit = getArguments().getParcelable(ARG_KEY);
            configure(cleverTapDisplayUnit);
        }
        appContext = getContext().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.native_carousel_image, container, false);
        title = view.findViewById(R.id.messageTitle);
        message = view.findViewById(R.id.messageText);
        imageViewPager = view.findViewById(R.id.image_carousel_viewpager);
        sliderDots = view.findViewById(R.id.sliderDots);
        clickLayout = view.findViewById(R.id.body_linear_layout);
        populate(view);
        return view;
    }

    @Override
    protected void populate(View view) {
        if(cleverTapDisplayUnitContent.getTitle().isEmpty() || cleverTapDisplayUnit.getType() == CTDisplayUnitType.CAROUSEL_WITH_IMAGE) {
            this.title.setVisibility(View.GONE);
        } else {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(cleverTapDisplayUnitContent.getTitle());
            this.title.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getTitleColor()));
        }
        if(cleverTapDisplayUnitContent.getMessage().isEmpty() || cleverTapDisplayUnit.getType() == CTDisplayUnitType.CAROUSEL_WITH_IMAGE) {
            this.message.setVisibility(View.GONE);
        } else {
            this.message.setVisibility(View.VISIBLE);
            this.message.setText(cleverTapDisplayUnitContent.getMessage());
            this.message.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getMessageColor()));
        }


        this.clickLayout.setBackgroundColor(Color.parseColor(cleverTapDisplayUnit.getBgColor()));

        //Loads the viewpager
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.imageViewPager.getLayoutParams();
        NativeCarouselViewPagerAdapter carouselViewPagerAdapter = new NativeCarouselViewPagerAdapter(appContext, this, cleverTapDisplayUnit,layoutParams);
        this.imageViewPager.setAdapter(carouselViewPagerAdapter);
        //Adds the dots for the carousel
        int dotsCount = cleverTapDisplayUnit.getContents().size();
        if(this.sliderDots.getChildCount()>0){
            this.sliderDots.removeAllViews();
        }
        ImageView[] dots = new ImageView[dotsCount];
        for(int k=0;k<dotsCount;k++){
            dots[k] = new ImageView(getActivity());
            dots[k].setVisibility(View.VISIBLE);
            dots[k].setImageDrawable(appContext.getResources().getDrawable(R.drawable.ct_unselected_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 6, 4, 6);
            params.gravity = Gravity.CENTER;
            if(this.sliderDots.getChildCount() < dotsCount)
                this.sliderDots.addView(dots[k],params);
        }
        dots[0].setImageDrawable(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.ct_selected_dot));
        carouselPageChangeListener = new NativeCarouselPageChangeListener(getContext(), dots, view, cleverTapDisplayUnit);
        this.imageViewPager.addOnPageChangeListener(carouselPageChangeListener);

        this.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CleverTapAPI.getDefaultInstance(getContext().getApplicationContext())
                        .pushDisplayUnitClickedEventForID(cleverTapDisplayUnit.getUnitID());
                int currentItem = carouselPageChangeListener.getCurrentPage();
                String actionUrl = cleverTapDisplayUnit.getContents().get(currentItem).getActionUrl();
                fireUrlThroughIntent(actionUrl);
            }
        });
    }
}
