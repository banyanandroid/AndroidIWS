package com.banyan.androidiws.global;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

public class NestedListview extends ListView {

    public NestedListview(Context context) {
        super(context);
    }
    public NestedListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NestedListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);    
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}