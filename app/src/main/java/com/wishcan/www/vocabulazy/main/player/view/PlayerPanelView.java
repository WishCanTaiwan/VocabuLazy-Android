package com.wishcan.www.vocabulazy.main.player.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2016/1/17.
 */
public class PlayerPanelView extends LinearLayout {

	public interface OnPanelItemClickListener{
		void onOptionFavoriteClick();
		void onOptionPlayClick();
		void onOptionOptionClick();
	}

	private static final int VIEW_RES_ID = R.layout.view_player_panel;
	private static final int VIEW_ICON_FAVORITE = R.id.action_player_favorite;
	private static final int VIEW_ICON_PLAY = R.id.action_player_play;
	private static final int VIEW_ICON_OPTION = R.id.action_player_option;

	private OnPanelItemClickListener mOnPanelItemClickListener;
    private ViewGroup mChildView;
    LevelListDrawable mDrawable = new LevelListDrawable();
    public PlayerPanelView(Context context) {
        this(context, null);
    }

    public PlayerPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawable.addLevel(0, 0, ContextCompat.getDrawable(context, R.drawable.player_stop));
        mDrawable.addLevel(0, 1, ContextCompat.getDrawable(context, R.drawable.player_play));

        mChildView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        mChildView.findViewById(VIEW_ICON_FAVORITE).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionFavoriteClick();
            }
        });
        mChildView.findViewById(VIEW_ICON_PLAY).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mDrawable.getLevel() == 0)
                    mDrawable.setLevel(1);
                else
                    mDrawable.setLevel(0);
                ((ImageView) v).setImageDrawable(mDrawable);
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionPlayClick();
            }
        });
        mChildView.findViewById(VIEW_ICON_OPTION).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionOptionClick();
            }
        });
        addView(mChildView);
    }

    public void setOnPanelItemClickListener(OnPanelItemClickListener listener){
    	mOnPanelItemClickListener = listener;
    }

    public void setIconInitState(boolean favorite, boolean play, boolean option){
        if(play) {
            mDrawable.setLevel(0);
            ((ImageView) mChildView.findViewById(VIEW_ICON_PLAY)).setImageDrawable(mDrawable);
        } else {
            mDrawable.setLevel(1);
            ((ImageView) mChildView.findViewById(VIEW_ICON_PLAY)).setImageDrawable(mDrawable);
        }
    }
}
