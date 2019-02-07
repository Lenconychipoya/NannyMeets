package zw.co.appsareus.nannymeets.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;

public class SquareViewContainer extends LinearLayoutCompat {
    public SquareViewContainer(Context context) {
        super(context);
    }

    public SquareViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
