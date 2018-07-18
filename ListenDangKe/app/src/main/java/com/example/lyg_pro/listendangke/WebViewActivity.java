package com.example.lyg_pro.listendangke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.fragment_2.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {


    private WebView webView;
    private String url;
    private String title;
    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private Button webSend;
    private SharedPreferences preferences;
    private Api api = null;
    private EditText webInput;
    private String channelid,classid,infoid ,content,username;
    private int show = -440;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setStatusBar();
        initView();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        top_bar_title.setText(title);
        url = intent.getStringExtra("url");
        channelid = intent.getStringExtra("channelid");
        classid = intent.getStringExtra("classid");
        infoid = intent.getStringExtra("infoid");
        content = intent.getStringExtra("content");
        show = intent.getExtras().getInt("photoweb",-450);
        if (998 == show){
            findViewById(R.id.webBottom).setVisibility(View.GONE);
        }else {
            findViewById(R.id.webBottom).setVisibility(View.VISIBLE);
        }
        username = App.USERNAME;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(url);
    }

    private void initView() {

        webView = (WebView) findViewById(R.id.webView);

        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_icon.setOnClickListener(this);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setOnClickListener(this);
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.GONE);
        webSend = (Button) findViewById(R.id.webSend);
        webSend.setOnClickListener(this);
        webInput = (EditText) findViewById(R.id.webInput);
        webInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_icon:
                finish();
                break;
            case R.id.webSend:
                if (submit()){
                    String content = webInput.getText().toString();
                    startIssue(content);
                }
                break;
        }
    }

    private void startIssue(String content) {
//        Log.e("EEE",""+channelid+"  "+classid+"  "+infoid+"  "+content+"  "+username);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .build();
        api = retrofit.create(Api.class);
        api.upInput(channelid,classid,infoid,content,username).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            if ("".equals(App.USERNAME)){
                                preferences = PreferenceManager.getDefaultSharedPreferences(WebViewActivity.this);
                                username = preferences.getString("user","");
                                App.USERNAME = username;
                                Toast.makeText(WebViewActivity.this,"评论成功!",Toast.LENGTH_SHORT).show();
                                webInput.setText("");
                                webView.reload();
                            }else {
                                Toast.makeText(WebViewActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                                webInput.setText("");
                                webView.reload();
                            }
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(WebViewActivity.this,"服务器繁忙",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(WebViewActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean submit() {
        // validate
        String webInputString = webInput.getText().toString().trim();
        if (TextUtils.isEmpty(webInputString)) {
            Toast.makeText(this, "请填写评论", Toast.LENGTH_SHORT).show();
            return false;
        }

        // TODO validate success, do something


        return true;
    }
    //点击其他区域 软键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
