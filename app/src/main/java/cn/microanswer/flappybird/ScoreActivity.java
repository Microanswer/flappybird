package cn.microanswer.flappybird;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 显示成绩排行的Activity
 */
public class ScoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        WebView webView = findViewById(R.id.webview);

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

        webView.setWebChromeClient(new MyWebChrome());
        webView.setWebViewClient(new MyWebView());

        webView.loadUrl("http://microanswer.cn/flappybird/scorebord.html?time=" + Math.random());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.score_activity_in2, R.anim.score_activity_out2);
    }

    private class MyWebChrome extends WebChromeClient {

    }

    private class MyWebView extends WebViewClient {

    }
}
