package com.example.clprofile;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;

class NativeCarouselPageChangeListener implements ViewPager.OnPageChangeListener {
    private ImageView[] dots;
    private Context context;
    private View view;
    private CleverTapDisplayUnit cleverTapDisplayUnit;
    private TextView title, message;
    private int currentPage = 0;

    NativeCarouselPageChangeListener(Context context, ImageView[] dots, View view, CleverTapDisplayUnit cleverTapDisplayUnit) {
        this.context = context;
        this.dots = dots;
        this.dots[0].setImageDrawable(context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_selected_dot));
        this.view = view;
        this.cleverTapDisplayUnit = cleverTapDisplayUnit;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        for (ImageView dot : this.dots) {
            dot.setImageDrawable(context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_unselected_dot));
        }
        dots[position].setImageDrawable(context.getResources().getDrawable(com.clevertap.android.sdk.R.drawable.ct_selected_dot));
        setTitleAndMessage(view, cleverTapDisplayUnit, position);
        currentPage = position;
    }

    private void setTitleAndMessage(View view, CleverTapDisplayUnit cleverTapDisplayUnit, int position) {
        title = view.findViewById(R.id.messageTitle);
        message = view.findViewById(R.id.messageText);
        CleverTapDisplayUnitContent cleverTapDisplayUnitContent = cleverTapDisplayUnit.getContents().get(position);
        if(cleverTapDisplayUnitContent.getTitle().isEmpty()
                || cleverTapDisplayUnit.getType() == CTDisplayUnitType.CAROUSEL_WITH_IMAGE) {
            this.title.setVisibility(View.GONE);
        } else {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(cleverTapDisplayUnitContent.getTitle());
            this.title.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getTitleColor()));
        }
        if(cleverTapDisplayUnitContent.getMessage().isEmpty()
                || cleverTapDisplayUnit.getType() == CTDisplayUnitType.CAROUSEL_WITH_IMAGE) {
            this.message.setVisibility(View.GONE);
        } else {
            this.message.setVisibility(View.VISIBLE);
            this.message.setText(cleverTapDisplayUnitContent.getMessage());
            this.message.setTextColor(Color.parseColor(cleverTapDisplayUnitContent.getMessageColor()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
