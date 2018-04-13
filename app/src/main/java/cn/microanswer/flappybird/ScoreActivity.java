package cn.microanswer.flappybird;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONObject;

import cn.microanswer.flappybird.other.USER;

/**
 * 显示成绩排行的Activity
 */
public class ScoreActivity extends Activity {
    private LinearLayout loadingView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loadingView = findViewById(R.id.loadingView);
        webView = findViewById(R.id.webview);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setNetworkAvailable(true);
        webView.setWebChromeClient(new MyWebChrome());
        webView.setWebViewClient(new MyWebView());
        webView.addJavascriptInterface(new FlappyBird(), "flappyBird");

        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setGeolocationEnabled(true);
        String userAgentString = settings.getUserAgentString();
        settings.setUserAgentString(userAgentString + "; MicroanswerFlappyBirdGameWeb");

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.score_activity_in2, R.anim.score_activity_out2);
    }

    private class MyWebChrome extends com.tencent.smtt.sdk.WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String str = consoleMessage.message();
            if (!TextUtils.isEmpty(str) && str.toLowerCase().contains("is not defiend")) {
                webView.loadUrl(webView.getUrl());
                return true;
            }
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class MyWebView extends com.tencent.smtt.sdk.WebViewClient {
        @Override
        public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
            super.onPageFinished(webView, s);
            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            loadingView.setVisibility(View.VISIBLE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 223) {
            // 权限申请完成。
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 授权成功
                // 保存文件
                Util.saveUserInfo2File(App.user);
                Toast.makeText(ScoreActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    class FlappyBird {
        public String version = "1.0.0";

        @JavascriptInterface
        public String getVersion() {
            return version;
        }

        @JavascriptInterface
        public void onLogin(String loginedJsonInfo) {
            try {
                JSONObject jsonObject = new JSONObject(loginedJsonInfo);
                if (App.user == null) {
                    App.user = new USER();
                }
                App.user.setInfo(loginedJsonInfo);
                App.user.setAccount(jsonObject.getString("account"));
                App.user.setPassword(jsonObject.getString("password"));
                App.user.setToken(jsonObject.getString("token"));

                // 检测文件写入权限是否有
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int i = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 没有权限, 申请权限
                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 223);
                        return;
                    }
                }

                Util.saveUserInfo2File(App.user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScoreActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
