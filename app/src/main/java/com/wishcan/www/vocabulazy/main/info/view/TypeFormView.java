package com.wishcan.www.vocabulazy.main.info.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by allencheng07 on 2016/6/22.
 */
public class TypeFormView extends LinearLayout {

    public static final String TAG = TypeFormView.class.getSimpleName();

    private static final String TYPEFORM_HTML = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "<html>\n" +
            "<head>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
            "\n" +
            "  <!--Add the title of your typeform below-->\n" +
            "  <title>Customer Satisfaction Survey</title>\n" +
            "\n" +
            "  <!--CSS styles that ensure your typeform takes up all the available screen space (DO NOT EDIT!)-->\n" +
            "  <style type=\"text/css\">\n" +
            "    html{\n" +
            "      margin: 0;\n" +
            "      height: 100%;\n" +
            "      overflow: hidden;\n" +
            "    }\n" +
            "    iframe{\n" +
            "      position: absolute;\n" +
            "      left:0;\n" +
            "      right:0;\n" +
            "      bottom:0;\n" +
            "      top:0;\n" +
            "      border:0;\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <iframe id=\"typeform-full\" width=\"100%\" height=\"100%\" frameborder=\"0\" src=\"https://allencheng07.typeform.com/to/Aubmep\"></iframe>\n" +
            "  <script type=\"text/javascript\" src=\"https://s3-eu-west-1.amazonaws.com/share.typeform.com/embed.js\"></script>\n" +
            "</body>\n" +
            "</html>\n";

    private WebView mWebView;

    public TypeFormView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mWebView = new WebView(context);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(TYPEFORM_HTML, "text/html", null);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mWebView);
    }
}
