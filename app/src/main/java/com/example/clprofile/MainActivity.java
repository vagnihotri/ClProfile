package com.example.clprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.CTDisplayUnitType;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class MainActivity extends BaseActivity implements CTInboxListener {

    private CleverTapAPI cleverTapDefaultInstance;
    //private static final String AF_DEV_KEY = "GxxdHKzAre8jEGypBfwEYg";
    private Button inboxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        inboxButton = findViewById(R.id.inboxButton);
        inboxButton.setText("Next Activity");
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(nextIntent);
            }
        });

        Button ndButton = findViewById(R.id.ndButton);
        ndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleverTapDefaultInstance.pushEvent("ND Launch");
            }
        });

        /*if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
        }*/


    }

    /*private void initAppsFlyer() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {


            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(getApplication());
        String attributionID = CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapAttributionIdentifier();
        Log.d("CleverTap attr ID", attributionID);
        AppsFlyerLib.getInstance().setCustomerUserId(attributionID);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        /*Branch branch = Branch.getInstance();
        branch.setRequestMetadata("$clevertap_attribution_id",
                CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapAttributionIdentifier());

        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /*super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.getInstance().reInitSession(this, branchReferralInitListener);*/
    }
    /*private Branch.BranchUniversalReferralInitListener branchReferralInitListener =
            new Branch.BranchUniversalReferralInitListener() {
                @Override public void onInitFinished(BranchUniversalObject branchUniversalObject,
                                                     LinkProperties linkProperties, BranchError branchError) {
                    // do something with branchUniversalObject/linkProperties..
                }
            };*/

    @Override
    public void inboxDidInitialize() {
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> tabs = new ArrayList<>();
                tabs.add("Promotions");
                tabs.add("Offers");

                CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
                //styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
                //styleConfig.setTabBackgroundColor("#FF0000");//provide Hex code in string ONLY
                //styleConfig.setSelectedTabIndicatorColor("#0000FF");
                //styleConfig.setSelectedTabColor("#000000");
                //styleConfig.setUnselectedTabColor("#FFFFFF");
                styleConfig.setBackButtonColor("#FF0000");
                styleConfig.setNavBarTitleColor("#FF0000");
                styleConfig.setNavBarTitle("Test INBOX");
                styleConfig.setNavBarColor("#FFFFFF");
                styleConfig.setInboxBackgroundColor("#00FF00");

                //cleverTapDefaultInstance.showAppInbox(styleConfig); //Opens activity tith Tabs
                //OR
                cleverTapDefaultInstance.showAppInbox();//Opens Activity with default style config
            }
        });
    }

    @Override
    public void inboxMessagesDidUpdate() {

    }

    @Override
    protected void prepareDisplayView(CleverTapDisplayUnit unit) {
        super.prepareDisplayView(unit);
        showNativeDisplay(unit, R.id.native_display_frag);
    }
}
