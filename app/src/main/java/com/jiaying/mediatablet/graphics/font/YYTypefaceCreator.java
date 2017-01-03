package com.jiaying.mediatablet.graphics.font;

import android.content.Context;

/**
 * Created by hipil on 2016/4/21.
 */
public class YYTypefaceCreator extends AbstractTypefaceCreator {
    @Override
    public AbstractTypeface createTypeface(Context context) {
        return YYTypeface.getInstance(context);
    }
}
