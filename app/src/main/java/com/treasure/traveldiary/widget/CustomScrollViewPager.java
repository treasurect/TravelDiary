package com.treasure.traveldiary.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by treasure on 2017/8/30.
 */

public class CustomScrollViewPager extends ViewPager{
    public CustomScrollViewPager(Context context) {
        super(context);
    }

    public CustomScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            int measuredHeight = childAt.getMeasuredHeight();
            if (measuredHeight > height)
                height = measuredHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
