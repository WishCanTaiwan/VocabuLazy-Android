package wishcantw.vocabulazy.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import wishcantw.vocabulazy.R;

/**
 * Created by SwallowChen on 12/6/16.
 */

public class FlagView extends RelativeLayout {
    private static final int COLOR_FLAG_RES_ID        = R.color.widget_flag_background;
    private static final int DIMEN_FLAG_WIDTH_RES_ID  = R.dimen.widget_flag_width;
    private static final int DIMEN_FLAG_HEIGHT_RES_ID = R.dimen.widget_flag_height;

    private int mWidth, mHeight;
    private ShapeDrawable mFlagDrawable;
    public FlagView(Context context) {
        this(context, null);
    }

    public FlagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Path path;

        mWidth  = (int) context.getResources().getDimension(DIMEN_FLAG_WIDTH_RES_ID);
        mHeight = (int) context.getResources().getDimension(DIMEN_FLAG_HEIGHT_RES_ID);
        path = getFlagDrawablePath(mWidth, mHeight);
        mFlagDrawable = new ShapeDrawable(new PathShape(path, mWidth, mHeight));
        setFlagColor(ContextCompat.getColor(context, COLOR_FLAG_RES_ID));

        setLayoutParams(new LayoutParams(mWidth, mHeight));
        setBackground(mFlagDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /** this is important, change outline make shadow appear */
            setOutlineProvider(new FlagOutlineProvider(context, mWidth, mHeight));
            setClipToOutline(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec  = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**------------------------------------- public method --------------------------------------**/

    /**
     * The api for setting the flag color
     * @param color should be rgb, instead of res id
     */
    public void setFlagColor(int color) {
        mFlagDrawable.getPaint().setColor(color);
        mFlagDrawable.invalidateSelf();
    }

    /**------------------------------------- private method -------------------------------------**/
    /*(0,0)        (w,0)
          |-------------|
          |             |
          |             |
          |             |
          |             |
          |    /---\    |
          |   /     \   |
          |  /       \  |
          |_/---------\_|
          (0,h)        (w,h)
    */
    /**
     * The path to draw the flag
     * @param width
     * @param height
     * @return
     */
    private Path getFlagDrawablePath(int width, int height){
        int w = width, h = height;
        Path path  = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, h);
        path.lineTo((int) (0.5 * w), (int) (0.8 * h));
        path.lineTo(w, h);
        path.lineTo(w, 0);
        return path;
    }

    @TargetApi(21)
    private static class FlagOutlineProvider extends ViewOutlineProvider {

        int mW, mH;

        public FlagOutlineProvider(Context context, int width, int height) {
            mW = width;
            mH = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            Path path  = new Path();
            path.moveTo(0, 0);
            path.lineTo(0, mH);
            path.lineTo((int) (0.5 * mW), (int) (0.8 * mH));
            path.lineTo(mW, mH);
            path.lineTo(mW, 0);
            outline.setConvexPath(path);
        }
    }
}
