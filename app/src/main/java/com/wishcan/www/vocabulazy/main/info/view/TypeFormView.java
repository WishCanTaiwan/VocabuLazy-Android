package com.wishcan.www.vocabulazy.main.info.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;

import com.wishcan.www.vocabulazy.R;

/**
 * A view containing a {@link WebView} and a {@link ProgressBar}.
 * The {@link WebView} is used to display the TypeFrom page.
 * The {@link ProgressBar} is used to show the progress of loading {@link WebView}.
 *
 * @author Allen Cheng Yu-Lun
 * @version 1.0
 * @since 1.0
 */
public class TypeFormView extends RelativeLayout {

    /**
     * String for debugging.
     */
    public static final String TAG = TypeFormView.class.getSimpleName();

    /**
     * Url string of the link to the form.
     */
    private static final String TYPEFORM_URL = "https://sojier.typeform.com/to/VSt4sV";

    /**
     * Layout resource id
     */
    private static final int LAYOUT_TYPEFORM = R.layout.view_typeform;

    /**
     * WebView resource id.
     */
    private static final int VIEW_WEBVIEW = R.id.webview_typeform;

    /**
     * WebView object to display TypeForm.
     */
    private WebView mWebView;

    /**
     * Constructor. In the constructor, the layout is inflated and the web view is setup properly by
     * enabling JavaScript and assigning a {@link WebViewClient}. Also the TypeForm will be loaded
     * after the client is set up.
     *
     * @param context Given the context of the activity, we are able to access system service.
     */
    public TypeFormView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(LAYOUT_TYPEFORM, null);
        mWebView = (WebView) viewGroup.findViewById(VIEW_WEBVIEW);
        mWebView.setVisibility(INVISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new TypeFormWebViewClient(viewGroup));
        mWebView.loadUrl(TYPEFORM_URL);

        viewGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(viewGroup);
    }

    /**
     * Inner class for customizing the {@link WebViewClient} for the {@link WebView} in TypeForm.
     */
    private class TypeFormWebViewClient extends WebViewClient {

        /**
         * ProgressBar resource id.
         */
        private static final int VIEW_PROGRESSBAR = R.id.progressbar_typeform;

        /**
         * ProgressBar to indicate the web page is being loaded.
         */
        private ProgressBar progressBar;

        /**
         * Constructor.
         * Retrieve the {@link ProgressBar} and set it to {@link #VISIBLE}.
         *
         * @param viewGroup Given the {@link ViewGroup}, we can call {@link #findViewById(int)}.
         */
        public TypeFormWebViewClient(ViewGroup viewGroup) {
            progressBar = (ProgressBar) viewGroup.findViewById(VIEW_PROGRESSBAR);
            progressBar.setVisibility(VISIBLE);
        }

        /**
         * Called after finishing loading page.
         * Set the visibility of {@link ProgressBar} to {@link #GONE},
         * and set the visibility of {@link WebView} to {@link #VISIBLE}.
         *
         * @param webview The WebView which displays the web page.
         * @param url The link of the loaded web page.
         */
        @Override
        public void onPageFinished(WebView webview, String url) {
            super.onPageFinished(webview, url);
            progressBar.setVisibility(GONE);
            webview.setVisibility(VISIBLE);
        }
    }
}
