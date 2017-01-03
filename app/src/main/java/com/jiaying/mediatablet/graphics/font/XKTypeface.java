package com.jiaying.mediatablet.graphics.font;

import android.content.Context;
import android.graphics.Typeface;



/**
 * Created by hipilee on 2014/11/21.
 */
public class XKTypeface extends AbstractTypeface {
    private static XKTypeface ourInstance = new XKTypeface();

    private static Context context;

    public static XKTypeface getInstance(Context context) {
        XKTypeface.context = context;
        return ourInstance;
    }


    private XKTypeface() {

    }

    @Override
    public Typeface getTypeface() {

        return Typeface.createFromAsset(context.getAssets(),"fonts/STXINGKA.TTF");

    }


}
