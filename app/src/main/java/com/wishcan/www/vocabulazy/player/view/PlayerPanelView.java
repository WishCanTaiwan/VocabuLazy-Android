package com.wishcan.www.vocabulazy.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * @author Swallow Chen
 * @since 2016/1/17
 */
public class PlayerPanelView extends LinearLayout {
    /**
     * OnPanelItemListener is the callback function when any of setting item is clicked
     * */
	public interface OnPanelItemClickListener{
		void onOptionFavoriteClick();
		void onOptionPlayClick();
		void onOptionOptionClick();
	}

	private static final int VIEW_ICON_FAVORITE = R.id.action_player_favorite;
	private static final int VIEW_ICON_PLAY = R.id.action_player_play;
	private static final int VIEW_ICON_OPTION = R.id.action_player_option;

    private static final int ICON_PLAY_RES_ID[] = {
            R.drawable.player_stop,
            R.drawable.player_play
    };
    
    private static final int ICON_PLAY_STATE_STOP = 0x0;
    private static final int ICON_PLAY_STATE_PLAY = 0x1;
    
    private ImageView mActionFavoriteIcon;
    private ImageView mActionPlayIcon;
    private ImageView mActionOptionIcon;

	private OnPanelItemClickListener mOnPanelItemClickListener;
    private int mCurrentIconState;

    public PlayerPanelView(Context context) {
        this(context, null);
    }

    public PlayerPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCurrentIconState = -1;
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mActionFavoriteIcon = (ImageView) findViewById(VIEW_ICON_FAVORITE);
        mActionPlayIcon = (ImageView) findViewById(VIEW_ICON_PLAY);
        mActionOptionIcon = (ImageView) findViewById(VIEW_ICON_OPTION);
        
        registerEventListener();
    }

    /**
     * Hook the callback function
     * @param listener the callback function
     * */
    public void setOnPanelItemClickListener(OnPanelItemClickListener listener){
        mOnPanelItemClickListener = listener;
    }

    /**
     * The api for controller to customized the icon's state
     * @param favorite the icon "favorite" state
     * @param play the icon "play" state
     * @param option the icon "option" state
     * */
    public void setIconState(boolean favorite, boolean play, boolean option){
        if(play) {
            mCurrentIconState = ICON_PLAY_STATE_STOP;
            mActionPlayIcon.setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
        } else {
            mCurrentIconState = ICON_PLAY_STATE_PLAY;
            mActionPlayIcon.setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
        }
    }

    /**
     * Register all the child's event
     * */
    private void registerEventListener() {
        mActionFavoriteIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null) {
                    mOnPanelItemClickListener.onOptionFavoriteClick();
                }
            }
        });
        
        mActionPlayIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mCurrentIconState == ICON_PLAY_STATE_STOP) {
                    mCurrentIconState = ICON_PLAY_STATE_PLAY;
                }
                else {
                    mCurrentIconState = ICON_PLAY_STATE_STOP;
                }
                ((ImageView) v).setImageResource(ICON_PLAY_RES_ID[mCurrentIconState]);
                if(mOnPanelItemClickListener != null) {
                    mOnPanelItemClickListener.onOptionPlayClick();
                }
            }
        });
        
        mActionOptionIcon.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionOptionClick();
            }
        });
    }
}
