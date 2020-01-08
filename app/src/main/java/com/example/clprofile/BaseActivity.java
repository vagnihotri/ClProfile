package com.example.clprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements DisplayUnitListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        cleverTapDefaultInstance.setDisplayUnitListener(this);
    }


    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        for (int i = 0; i <units.size() ; i++) {
            CleverTapDisplayUnit unit = units.get(i);
            prepareDisplayView(unit);
        }
    }

    protected void prepareDisplayView(CleverTapDisplayUnit unit) {
        //Pass container layout id here after overriding
        Log.d("CleverTap ND Test BASE","Activity: " + getLocalClassName());
        Log.d("CleverTap ND Test BASE","CTDU: " + unit.toString());
    }


    protected void showNativeDisplay(CleverTapDisplayUnit unit, int fragmentContainerLayoutId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(unit.getType() == CTDisplayUnitType.SIMPLE || unit.getType() == CTDisplayUnitType.SIMPLE_WITH_IMAGE) {
            NativeSimpleFragment simpleFragment = NativeSimpleFragment.newInstance(unit);
            fragmentTransaction.replace(fragmentContainerLayoutId, simpleFragment);
        } else if (unit.getType() == CTDisplayUnitType.CAROUSEL || unit.getType() == CTDisplayUnitType.CAROUSEL_WITH_IMAGE) {
            NativeCarouselImageFragment nativeCarouselImageFragment = NativeCarouselImageFragment.newInstance(unit);
            fragmentTransaction.replace(fragmentContainerLayoutId, nativeCarouselImageFragment);
        } else if (unit.getType() == CTDisplayUnitType.MESSAGE_WITH_ICON) {
            NativeIconFragment nativeIconFragment = NativeIconFragment.newInstance(unit);
            fragmentTransaction.replace(fragmentContainerLayoutId, nativeIconFragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d("CleverTap ND Test BASE","onPointerCaptureChanged: " + hasCapture);
    }
}
