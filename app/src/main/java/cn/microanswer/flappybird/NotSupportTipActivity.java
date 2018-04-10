package cn.microanswer.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class NotSupportTipActivity extends Activity implements View.OnClickListener {
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去除标题栏
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_notsupport);

        textView = findViewById(R.id.textview);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        Intent intent = getIntent();
        String hint = intent.getStringExtra("hint");
        textView.setText(hint);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse("https://shop.m.taobao.com/shop/shop_index.htm?shop_id=150920153");
        intent.setData(content_url);
        startActivity(intent);
        finish();
    }
}
