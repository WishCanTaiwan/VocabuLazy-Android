package com.wishcan.www.vocabulazy.main.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public PlayerPanelView(Context context) {
        this(context, null);
    }

    public PlayerPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewGroup childView;
        childView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
        childView.findViewById(VIEW_ICON_FAVORITE).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionFavoriteClick();
            }
        });
        childView.findViewById(VIEW_ICON_PLAY).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionPlayClick();
            }
        });
        childView.findViewById(VIEW_ICON_OPTION).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
                if(mOnPanelItemClickListener != null)
                    mOnPanelItemClickListener.onOptionOptionClick();
            }
        });
        addView(childView);
    }

    public void setOnPanelItemClickListener(OnPanelItemClickListener listener){
    	mOnPanelItemClickListener = listener;
    }
}
