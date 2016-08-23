package com.wishcan.www.vocabulazy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by Swallow on 8/23/16.
 */
public class LevelImageView extends ImageView {

    private static final int LEVEL_IMAGE_VIEW_ATTRIBUTE_s[] = {
        R.styleable.LevelImageView_setMaxImageLevel
    };

    private int mImageLevel;
    private int mMaxImageLevel;

    public LevelImageView(Context context) {
        super(context);
    }

    public LevelImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LevelImageView, 0, 0);
        try {
            final int N = LEVEL_IMAGE_VIEW_ATTRIBUTE_s.length;
            for (int i = 0; i < N; i++) {
                int attribute = LEVEL_IMAGE_VIEW_ATTRIBUTE_s[i];
                switch (attribute) {
                    case R.styleable.LevelImageView_setMaxImageLevel:
                        mMaxImageLevel = ta.getInteger(attribute, 0);
                        break;
                }
            }
        } finally {
            Log.d("LevelImageView", "mMaxImageLevel " + mMaxImageLevel);
            setMaxImageLevel(mMaxImageLevel);
        }
    }

    @Override
    public void setImageLevel(int level) {
        if (level > getMaxImageLevel()) {
            level = 0;
        }
        super.setImageLevel(level);
        mImageLevel = level;
    }

    public int getImageLevel() {
        return mImageLevel;
    }

    public void setMaxImageLevel(int maxLevel) {
        mMaxImageLevel = maxLevel;
    }

    public int getMaxImageLevel() {
        return mMaxImageLevel;
    }
}
