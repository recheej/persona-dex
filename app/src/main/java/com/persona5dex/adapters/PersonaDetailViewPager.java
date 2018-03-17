package com.persona5dex.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by reche on 3/17/2018.
 */

public class PersonaDetailViewPager extends ViewPager {

    public PersonaDetailViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //we have to intercept a touch event because of the PhotoView library I'm using.
            //see: https://github.com/chrisbanes/PhotoView
            return false;
        }
    }
}
