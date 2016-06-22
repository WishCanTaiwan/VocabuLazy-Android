package com.wishcan.www.vocabulazy.main.info.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.wishcan.www.vocabulazy.log.Logger;

/**
 * Created by allencheng07 on 2016/6/22.
 */
public class TypeFormView extends LinearLayout {

    public static final String TAG = TypeFormView.class.getSimpleName();

    private static final String TYPEFORM_URL = "https://tomliou.typeform.com/to/LlbCCb";

    private WebView mWebView;

    public TypeFormView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Logger.d(TAG, "loading url " + TYPEFORM_URL);
        mWebView.loadUrl(TYPEFORM_URL);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mWebView);
    }
}
