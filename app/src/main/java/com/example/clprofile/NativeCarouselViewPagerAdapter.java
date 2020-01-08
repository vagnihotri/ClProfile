package com.example.clprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clevertap.android.sdk.Constants;
import com.clevertap.android.sdk.Logger;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NativeCarouselViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> carouselImages;
    private View view;
    private LinearLayout.LayoutParams layoutParams;
    private CleverTapDisplayUnit cleverTapDisplayUnit;
    private WeakReference<NativeBaseFragment> parentWeakReference;

    NativeCarouselViewPagerAdapter(Context context, NativeBaseFragment parent, CleverTapDisplayUnit cleverTapDisplayUnit, LinearLayout.LayoutParams layoutParams) {
        this.context = context;
        this.parentWeakReference = new WeakReference<>(parent);
        this.carouselImages = getCarouselImages(cleverTapDisplayUnit);
        this.layoutParams = layoutParams;
        this.cleverTapDisplayUnit = cleverTapDisplayUnit;
    }

    private ArrayList<String> getCarouselImages(CleverTapDisplayUnit cleverTapDisplayUnit) {
        ArrayList<String> carouselImages = new ArrayList<>();
        ArrayList<CleverTapDisplayUnitContent> contents = cleverTapDisplayUnit.getContents();
        for(int index = 0 ; index < contents.size(); index ++) {
            carouselImages.add(contents.get(index).getMedia());
        }
        return carouselImages;
    }

    NativeBaseFragment getParent() {
        return parentWeakReference.get();
    }

    @Override
    public int getCount() {
        return carouselImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //noinspection ConstantConditions
        view = layoutInflater.inflate(R.layout.inbox_carousel_image_layout,container,false);
        try {
            ImageView imageView = view.findViewById(R.id.squareImageView);
            imageView.setVisibility(View.VISIBLE);
            try {
                Glide.with(imageView.getContext())
                        .load(carouselImages.get(position))
                        .apply(new RequestOptions()
                                .placeholder(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER))
                                .error(Utils.getThumbnailImage(context,Utils.IMAGE_PLACEHOLDER)))
                        .into(imageView);
            }catch (NoSuchMethodError error){
                Logger.d("CleverTap SDK requires Glide v4.9.0 or above. Please refer CleverTap Documentation for more info");
                Glide.with(imageView.getContext())
                        .load(carouselImages.get(position))
                        .into(imageView);
            }

            container.addView(view, layoutParams);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NativeBaseFragment parent = getParent();
                    if (parent != null) {
                        parent.handleViewPagerClick(position);
                    }
                }
            });
        }catch (NoClassDefFoundError error) {
            Logger.d("CleverTap SDK requires Glide dependency. Please refer CleverTap Documentation for more info");
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
