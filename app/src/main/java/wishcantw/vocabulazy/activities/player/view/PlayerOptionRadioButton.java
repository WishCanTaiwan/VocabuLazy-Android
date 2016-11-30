package wishcantw.vocabulazy.activities.player.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RadioButton;

import wishcantw.vocabulazy.R;

/**
 * Created by SwallowChen on 11/20/16.
 */

public class PlayerOptionRadioButton extends RadioButton {
    private static final int STYLE_TXT_PLAYER_OPTION_CHECKED_RES_ID = R.style.AppTextStyle_PlayerOption_Checked;
    private static final int STYLE_TXT_PLAYER_OPTION_UNCHECKED_RES_ID = R.style.AppTextStyle_PlayerOption;

    public PlayerOptionRadioButton(Context context) {
        this(context, null);
    }

    public PlayerOptionRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (Build.VERSION.SDK_INT < 23) {
            if (checked) {
                setTextAppearance(getContext(), STYLE_TXT_PLAYER_OPTION_CHECKED_RES_ID);
            } else {
                setTextAppearance(getContext(), STYLE_TXT_PLAYER_OPTION_UNCHECKED_RES_ID);
            }
        } else {
            if (checked) {
                setTextAppearance(STYLE_TXT_PLAYER_OPTION_CHECKED_RES_ID);
            } else {
                setTextAppearance(STYLE_TXT_PLAYER_OPTION_UNCHECKED_RES_ID);
            }
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (Build.VERSION.SDK_INT < 23) {
            if (pressed) {
                setTextAppearance(getContext(), STYLE_TXT_PLAYER_OPTION_CHECKED_RES_ID);
            } else {
                if (!isChecked()) {
                    setTextAppearance(getContext(), STYLE_TXT_PLAYER_OPTION_UNCHECKED_RES_ID);
                }
            }
        } else {
            if (pressed) {
                setTextAppearance(STYLE_TXT_PLAYER_OPTION_CHECKED_RES_ID);
            } else {
                if (!isChecked()) {
                    setTextAppearance(STYLE_TXT_PLAYER_OPTION_UNCHECKED_RES_ID);
                }
            }

        }
    }
}
