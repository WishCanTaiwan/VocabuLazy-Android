package wishcantw.vocabulazy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import wishcantw.vocabulazy.utility.Logger;

/**
 * Created by SwallowChen on 9/4/16.
 */
public class DialogViewNew extends LinearLayout {
    public interface OnYesOrNoClickListener {
        void onYesClick();
        void onNoClick();
    }

    public interface OnBackgroundClickListener {
        void onBackgroundClick();
    }

    private View mContentView;
    private View mContentYesView, mContentNoView;

    private OnYesOrNoClickListener mOnYesOrNoClickListener;
    private OnBackgroundClickListener mOnBackgroundClickListener;
    private int mContentYesViewId, mContentNoViewId;

    public DialogViewNew(Context context) {
        this(context, null);
    }

    public DialogViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            Logger.d("DialogView", "DialogView can contains only one child as dialog content");
            return;
        }
        mContentView = getChildAt(0);
        if (mContentYesViewId != -1) {
            mContentYesView = mContentView.findViewById(mContentYesViewId);
        }
        if (mContentNoViewId != -1) {
            mContentNoView = mContentView.findViewById(mContentNoViewId);
        }

        registerEventListener();
    }

    public void setOnYesOrNoClickListener(OnYesOrNoClickListener listener) {
        mOnYesOrNoClickListener = listener;
    }

    public void setOnBackgroundClickListener(OnBackgroundClickListener listener) {
        mOnBackgroundClickListener = listener;
    }

    public void setYesOrNoId(int yesId, int noId) {
        if (yesId != -1) {
            mContentYesViewId = yesId;
        }
        if (noId != -1) {
            mContentNoViewId = noId;
        }
    }

    public void enableYesFunction() {
        registerYesEvent(true);
    }

    public void disableYesFunction() {
        registerYesEvent(false);
    }

    public void enableNoFunction() {
        registerNoEvent(true);
    }

    public void disableNoFunction() {
        registerNoEvent(false);
    }

    private void registerYesEvent(boolean enable) {
        if (mContentYesView == null) {
            Log.d("DialogView", "unable to find ContentYesView");
            return;
        }

        if (enable != true) {
            mContentYesView.setOnClickListener(null);
        }
        mContentYesView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnYesOrNoClickListener != null) {
                    mOnYesOrNoClickListener.onYesClick();
                }
            }
        });
    }

    private void registerNoEvent(boolean enable) {
        if (mContentNoView == null) {
            Log.d("DialogView", "unable to find ContentNoView");
            return;
        }

        if (enable != true) {
            mContentNoView.setOnClickListener(null);
        }
        mContentNoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnYesOrNoClickListener != null) {
                    mOnYesOrNoClickListener.onNoClick();
                }
            }
        });
    }

    private void registerEventListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnBackgroundClickListener != null) {
                    mOnBackgroundClickListener.onBackgroundClick();
                }
            }
        });
        registerYesEvent(true);
        registerNoEvent(true);
    }
}
