package cn.microanswer.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Date;

/**
 * 显示成绩排行的Activity
 */
public class ScoreActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        webView = findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        String userAgentString = settings.getUserAgentString();
        settings.setUserAgentString(userAgentString + "; MicroanswerFlappyBirdGameWeb");

        webView.setWebChromeClient(new MyWebChrome());
        webView.setWebViewClient(new MyWebView());

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.score_activity_in2, R.anim.score_activity_out2);
    }

    private class MyWebChrome extends WebChromeClient {

    }

    private class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.loadUrl(request.getUrl().toString());
            } else {
                webView.loadUrl(request.toString());
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (!webView.canGoBack()) {
            super.onBackPressed();
        } else {
            webView.goBack();
        }
    }
}
