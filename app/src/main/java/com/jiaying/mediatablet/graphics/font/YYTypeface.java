package com.jiaying.mediatablet.graphics.font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by hipil on 2016/4/21.
 */
public class YYTypeface extends AbstractTypeface {
    private static YYTypeface ourInstance = new YYTypeface();
    private static Context context;

    public static YYTypeface getInstance(Context context) {
        YYTypeface.context = context;
        return ourInstance;
    }

    private YYTypeface() {
    }

    @Override
    public Typeface getTypeface() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/SIMYOU.TTF");

    }
}
