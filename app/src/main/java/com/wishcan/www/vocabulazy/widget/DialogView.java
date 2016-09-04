package com.wishcan.www.vocabulazy.widget;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2015/9/25.
 */
abstract public class DialogView<WishCan> extends LinearLayout {

    /**
     * Default call back function of dialog view, if necessary.
     * If there are yes and no button, implement the call back and register yes/no view
     * by setYesOnNoViewId function will automatically enable the callback
     * @see #setYesOrNoViewId(int, int)
     * @see #setOnYesOrNoClickedListener(OnYesOrNoClickListener)
     */
    public interface OnYesOrNoClickListener {
        void onYesClicked();
        void onNoClicked();
    }

    abstract public void setDialogOutput(WishCan output);
    abstract public WishCan getDialogOutput();
    abstract public LayoutTransition getDialogTransition();

    private static final int DEFAULT_DIALOG_BACKGROUND_COLOR = R.color.widget_dialog_background;
    private static final int DEFAULT_DIALOG_BUTTON_BACKGROUND_STOP_COLOR = R.color.widget_dialog_button_background_gray;
    private static final int DEFAULT_DIALOG_BUTTON_BACKGROUND_START_COLOR = R.color.widget_dialog_button_background_green;

    private LayoutParams mDefaultLayoutParams;
    private LayoutParams mDefaultContentLayoutParams;
    private View mContentView;
    private Context mContext;
    private OnYesOrNoClickListener mListener;
    private View mYesView;
    private View mNoView;

    public DialogView(Context context) {
        this(context, null);
    }

    public DialogView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mDefaultLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setGravity(Gravity.CENTER);
        setVisibility(INVISIBLE);
        setBackgroundColor(ContextCompat.getColor(context, DEFAULT_DIALOG_BACKGROUND_COLOR));
        setLayoutParams(mDefaultLayoutParams);

        mDefaultContentLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mContentView = new View(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onNoClicked();
                closeDialog();
            }
        });
    }

    public void setDialog(int resId) {
        View v = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resId, null);
        setDialog(v);
    }

    public void setDialog(View v) {
        setDialog(v, mDefaultLayoutParams);
    }

    public void setDialog(View v, ViewGroup.LayoutParams layoutParams) {
        mContentView = v;
        if (!(v instanceof AdapterView)) {
            mContentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestFocus();
                }
            });
        } else {
            ((AdapterView) mContentView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    requestFocus();
                }
            });
        }
        if (layoutParams != null) {
            mContentView.setLayoutParams(layoutParams);
        }

        mContentView.setVisibility(GONE);
        addView(mContentView);
        setDialogTransition(getDialogTransition());
    }

    public void showDialog() {
        setVisibility(VISIBLE);
        mContentView.setVisibility(VISIBLE);
    }

    public void closeDialog() {
        removeAllViews();
        setVisibility(INVISIBLE);
    }

    public View getDialog() {
        return mContentView;
    }

    public void setDialogTransition(LayoutTransition transition) {
        if (transition != null) {
            setLayoutTransition(transition);
            return;
        }

        ValueAnimator translateAnim = ObjectAnimator.ofFloat(this, "translationY", 1920f, 0f);
        translateAnim.setDuration(500);
        translateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.APPEARING, translateAnim);
        setLayoutTransition(layoutTransition);
    }

    public void setOnYesOrNoClickedListener(OnYesOrNoClickListener listener) {
        mListener = listener;
    }

    public void setYesOrNoViewId(int yesId, int noId) {
        mYesView = mContentView.findViewById(yesId);
        mNoView = mContentView.findViewById(noId);
        setYesOrNoView();
    }

    public void stopYesFunction() {
        if (mYesView != null) {
            mYesView.setOnClickListener(null);
            mYesView.setBackgroundColor(ContextCompat.getColor(getContext(), DEFAULT_DIALOG_BUTTON_BACKGROUND_STOP_COLOR));
        }
    }

    public void stopNoFunction() {
        if (mNoView != null) {
            mNoView.setOnClickListener(null);
        }
    }

    public void startYesFunction() {
        if (mYesView != null) {
            mYesView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onYesClicked();
                }
            });
            mYesView.setBackgroundColor(ContextCompat.getColor(getContext(), DEFAULT_DIALOG_BUTTON_BACKGROUND_START_COLOR));
        }
    }

    public void startNoFunction() {
        if (mNoView != null) {
            mNoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNoClicked();
                }
            });
        }
    }

    private void setYesOrNoView() {
        if(mYesView != null) {
            mYesView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onYesClicked();
                }
            });
        }
        if(mNoView != null) {
            mNoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNoClicked();
                }
            });
        }
    }

}
