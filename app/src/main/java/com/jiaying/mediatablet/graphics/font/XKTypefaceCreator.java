package com.jiaying.mediatablet.graphics.font;


import android.content.Context;

/**
 * Created by hipilee on 2014/11/21.
 */
public class XKTypefaceCreator extends AbstractTypefaceCreator {
    @Override
         public AbstractTypeface createTypeface(Context context) {
        return XKTypeface.getInstance(context);
    }
}
