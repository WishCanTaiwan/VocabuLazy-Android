package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/9/4.
 */
public class XXView extends View {

    private static int DEFAULT_PAINT_THICK_RES_ID = R.dimen.search_xx_thick;

    private static final int DEFAULT_XX_CIRCLE_SIZE_RES_ID = R.dimen.search_xx_circle_size;

    private static final int DEFAULT_XX_LENGTH_RES_ID = R.dimen.search_xx_length_size;

    private static final int DEFAULT_XX_COLOR_RES_ID = R.color.search_xx_color;

    private static final int DEFAULT_XX_BACKGROUND_COLOR_RES_ID = R.color.search_xx_background_color;

    private int mViewSize;

    private int mCircleSize;

    private int mXXLength;

    private Paint mPaint;

    private int mXXColor;

    private int mXXBackgroundColor;

    private int mPaintThick;

    private boolean mCircleOutline;

    public XXView(Context context) {
        this(context, null);
    }

    public XXView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.XXView, 0, 0);
        try {
            mCircleOutline = ta.getBoolean(R.styleable.XXView_setXXCircleOutline, true);
        } finally {
            ta.recycle();
        }

        mPaintThick = (int) getResources().getDimension(DEFAULT_PAINT_THICK_RES_ID);
        mCircleSize = (int) getResources().getDimension(DEFAULT_XX_CIRCLE_SIZE_RES_ID);
        mXXLength = (int) getResources().getDimension(DEFAULT_XX_LENGTH_RES_ID);
        mViewSize = mCircleSize + mPaintThick;
        mXXColor = getResources().getColor(DEFAULT_XX_COLOR_RES_ID);
        mXXBackgroundColor = getResources().getColor(DEFAULT_XX_BACKGROUND_COLOR_RES_ID);

        mPaint = new Paint();
        mPaint.setStrokeWidth(mPaintThick);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        outline.setOval(0, 0, mCircleSize, mCircleSize);
                }
            });
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center, radius;


        center = (float) 0.5 * mViewSize;
        radius = (float) 0.5 * mCircleSize;

        if(mCircleOutline) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mXXBackgroundColor);
            canvas.drawCircle(center, center, radius, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mXXColor);
            canvas.drawCircle(center, center, radius, mPaint);
        }

        float startX, stopX, startY, stopY;
        startX = startY = (float) 0.35 * mViewSize;
        stopX = stopY = (float) 0.65 * mViewSize;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        startX = (float) 0.65 * mViewSize;
        stopX = (float) 0.35 * mViewSize;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
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
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mCircleSize + mPaintThick, MeasureSpec.EXACTLY);

        if (heightMode == MeasureSpec.EXACTLY)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        else
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCircleSize + mPaintThick, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
