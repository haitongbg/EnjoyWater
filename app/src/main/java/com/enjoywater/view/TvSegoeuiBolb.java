package com.enjoywater.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TvSegoeuiBolb extends AppCompatTextView {
    private static Typeface mTypeface;

    public TvSegoeuiBolb(final Context context) {
        this(context, null);
    }

    public TvSegoeuiBolb(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvSegoeuiBolb(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "segoeuib.ttf");
        }
        setTypeface(mTypeface);
    }

}
