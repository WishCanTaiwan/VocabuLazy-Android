package com.wishcan.www.vocabulazy.main.player.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.R;

/**
 * Created by swallow on 2016/1/17.
 */
public class PlayerView extends LinearLayout {
	public interface OnGrayBackClickListener {
		void onGrayBackClick();
	}

	private static final int VIEW_RES_ID = R.layout.view_player;
	private static final int VIEW_PLAYER_MAIN_RES_ID = R.id.player_main_view;
	private static final int VIEW_PLAYER_PANEL_RES_ID = R.id.player_panel;
	private static final int VIEW_PLAYER_OPTION_RES_ID = R.id.player_option_view;
	private static final int VIEW_PLAYER_OPTION_PARENT_RES_ID = R.id.player_option_parent;
	private static final int ANIMATE_TRANSLATE_BOTTOM_UP = R.anim.translate_bottom_up;
	private static final int ANIMATE_TRANSLATE_UP_BOTTOM = R.anim.translate_up_bottom;

	private PlayerMainView mPlayerMainView;
	private PlayerPanelView mPlayerPanelView;
	private PlayerOptionView mPlayerOptionView;
	private ViewGroup mPlayerOptionGrayBack;

	private OnGrayBackClickListener mOnGrayBackClickListener;

    public PlayerView(Context context){
		this(context, null);
	}

	public PlayerView(Context context, AttributeSet attr){
		super(context, attr);
		ViewGroup childView;
        childView = (ViewGroup) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(VIEW_RES_ID, null);
		childView.setOnClickListener(new OnClickListener() {@Override public void onClick(View v) {}});
        mPlayerMainView = (PlayerMainView) childView.findViewById(VIEW_PLAYER_MAIN_RES_ID);
		mPlayerPanelView = (PlayerPanelView) childView.findViewById(VIEW_PLAYER_PANEL_RES_ID);
        mPlayerOptionView = (PlayerOptionView) childView.findViewById(VIEW_PLAYER_OPTION_RES_ID);
        mPlayerOptionGrayBack = (ViewGroup) childView.findViewById(VIEW_PLAYER_OPTION_PARENT_RES_ID);
		mPlayerOptionGrayBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnGrayBackClickListener != null) {
					mOnGrayBackClickListener.onGrayBackClick();
				}
				exitOptionView();
			}

		});
		addView(childView);
	}

	public PlayerMainView getPlayerMainView() {
		return mPlayerMainView;
	}

	public PlayerPanelView getPlayerPanelView() {
		return mPlayerPanelView;
	}

	public PlayerOptionView getPlayerOptionView() {
		return mPlayerOptionView;
	}

	public ViewGroup getPlayerOptionGrayBack() {
		return mPlayerOptionGrayBack;
	}

	public void showPlayerOptionView() {

		// this line is to set the default setting, which is stored in the database, to playerOptionView.
		//mPlayerOptionView.setOptionsInTabContent(mDatabase.getOptions());

		if (mPlayerOptionGrayBack.getVisibility() == View.INVISIBLE)
			mPlayerOptionGrayBack.setVisibility(View.VISIBLE);

		Animation comeInAnimation = AnimationUtils.loadAnimation(getContext(), ANIMATE_TRANSLATE_BOTTOM_UP);
		mPlayerOptionView.startAnimation(comeInAnimation);

		mPlayerOptionGrayBack.invalidate();
	}

	public void exitOptionView() {
		if (mPlayerOptionGrayBack.getVisibility() != View.VISIBLE)
			return;
		Animation goOutAnimation = AnimationUtils.loadAnimation(getContext(), ANIMATE_TRANSLATE_UP_BOTTOM);
		mPlayerOptionView.startAnimation(goOutAnimation);
		goOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				mPlayerOptionGrayBack.setVisibility(View.INVISIBLE);
				mPlayerOptionGrayBack.invalidate();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
		});
	}

	public void setOnGrayBackClickListener(OnGrayBackClickListener listener) {
		mOnGrayBackClickListener = listener;
	}
}
