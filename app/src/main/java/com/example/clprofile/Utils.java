package com.example.clprofile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {

    public static final String VIDEO_THUMBNAIL = "ct_video_1";
    public static final String AUDIO_THUMBNAIL = "ct_audio";
    public static final String IMAGE_PLACEHOLDER = "ct_image";

    public static int getThumbnailImage(Context context, String image) {
        if (context != null) {
            return context.getResources().getIdentifier(image, "drawable", context.getPackageName());
        } else {
            return -1;
        }
    }

    public static void fireUrlThroughIntent(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("\n", "").replace("\r", "")));
            context.startActivity(intent);
        } catch (Throwable t) {
            // Ignore
        }
    }
}
