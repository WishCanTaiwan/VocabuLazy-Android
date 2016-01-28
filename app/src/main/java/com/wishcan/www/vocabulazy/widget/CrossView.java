package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/9/3.
 */
public class CrossView extends View {

    private static int DEFAULT_PAINT_THICK_RES_ID = R.dimen.note_cross_thick;
    private static int DEFAULT_CROSS_SIZE_RES_ID = R.dimen.note_cross_size;
    private static int DEFAULT_PAINT_COLOR_RES_ID = R.color.note_cross_color;

    private Paint mPaint;
    private int mPaintColor;
    private int mPaintThick;
    private int mCrossSize;

    public CrossView(Context context) {
        this(context, null);
    }

    public CrossView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CrossView, 0, 0);
        try {
            mPaintColor = ta.getColor(R.styleable.CrossView_setCrossColor, getResources().getColor(DEFAULT_PAINT_COLOR_RES_ID));
            mCrossSize = (int) ta.getDimension(R.styleable.CrossView_setCrossSize, getResources().getDimension(DEFAULT_CROSS_SIZE_RES_ID));
        } finally {
            ta.recycle();
        }

        mPaint = new Paint();
        mPaintThick = (int) getResources().getDimension(DEFAULT_PAINT_THICK_RES_ID);
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeWidth(mPaintThick);
    }

    public void setCrossSize(int newCrossSize) {
        mCrossSize = newCrossSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int middlePoint = mCrossSize / 2;
        canvas.drawLine(0, middlePoint, mCrossSize, middlePoint, mPaint);
        canvas.drawLine(middlePoint, 0, middlePoint, mCrossSize, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        // EXACTLY mode may be MATCH_PARENT or given a specific size
        // AT_MOST and UNSPECIFIED is appeared when wrap_content
        if (widthMode == MeasureSpec.EXACTLY)
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
        else
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mCrossSize, MeasureSpec.EXACTLY);

        if (heightMode == MeasureSpec.EXACTLY)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        else
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCrossSize, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}