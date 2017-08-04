package com.treasure.traveldiary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by treasure on 2017.04.08.
 */

public class CustomScrollListView extends ListView {
    public CustomScrollListView(Context context) {
        super(context);
    }

    public CustomScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureSpec);
    }
}
