package com.enjoywater.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TvSegoeuiSb extends AppCompatTextView {
    private static Typeface mTypeface;

    public TvSegoeuiSb(final Context context) {
        this(context, null);
    }

    public TvSegoeuiSb(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvSegoeuiSb(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "seguisb.ttf");
        }
        setTypeface(mTypeface);
    }

}
