package com.example.clprofile;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

public class SecondActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        CleverTapAPI.getDefaultInstance(getApplicationContext()).pushEvent("Native Display Media Event");
    }

    @Override
    protected void prepareDisplayView(CleverTapDisplayUnit unit) {
        super.prepareDisplayView(unit);
        showNativeDisplay(unit,R.id.native_display_frag);
    }
}
