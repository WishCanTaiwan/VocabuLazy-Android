package wishcantw.vocabulazy.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wishcantw.vocabulazy.R;

/**
 * Created by SwallowChen on 11/30/16.
 */

public class PlayerOptionBalloonView extends RelativeLayout {

    private static final int VIEW_BALLOON_IMG_RES_ID = R.id.player_option_balloon_img;
    private static final int VIEW_BALLOON_TXT_RES_ID = R.id.player_option_balloon_txt;

    private ImageView mBalloonImageView;
    private TextView mBalloonTextView;

    public PlayerOptionBalloonView(Context context) {
        this(context, null);
    }

    public PlayerOptionBalloonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBalloonImageView = (ImageView) findViewById(VIEW_BALLOON_IMG_RES_ID);
        mBalloonTextView  = (TextView)  findViewById(VIEW_BALLOON_TXT_RES_ID);
    }

    /**------------------------------------ public methods -------------------------------------**/
    public void setBalloonText(String str) {
        mBalloonTextView.setText(str);
    }
}
