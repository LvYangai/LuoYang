package com.example.lyg_pro.listendangke;

import android.app.ProgressDialog;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private ImageView loginImage;
    private EditText loginUser;
    private EditText loginPass;
    private TextView loginForget;
    private TextView loginNews;
    private Button login;
    private Api api;
    private String user, pass;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Boolean auto;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBar();
        initView();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = preferences.getString("user", "");
        pass = preferences.getString("pass", "");
        auto = preferences.getBoolean("auto",false);
        loginUser.setText(user);
        loginPass.setText(pass);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!"".equals(this.user) && !"".equals(this.pass) && auto) {
            startLogin(user, pass);
        }
    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setVisibility(View.INVISIBLE);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.INVISIBLE);
        loginImage = (ImageView) findViewById(R.id.loginImage);
        loginUser = (EditText) findViewById(R.id.loginUser);
        loginPass = (EditText) findViewById(R.id.loginPass);
        loginForget = (TextView) findViewById(R.id.loginForget);
        loginNews = (TextView) findViewById(R.id.loginNews);
        top_bar_title.setText("用户登陆");
        loginNews.setOnClickListener(this);
        loginForget.setOnClickListener(this);
        top_bar_search_btn.setOnClickListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);//1.创建一个ProgressDialog的实例
        progressDialog.setTitle("用户登录");//2.设置标题
        progressDialog.setMessage("正在加载中，请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (submit()) {
                    String user = loginUser.getText().toString();
                    String pass = loginPass.getText().toString();
                    startLogin(user, pass);
                }
                break;
            case R.id.loginNews:
                startActivity(new Intent(LoginActivity.this, NewsActivity.class));
                break;
            case R.id.loginForget:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
        }
    }

    private void startLogin(final String user, final String pass) {
//        MyDialog.showDialog(LoginActivity.this).show();
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .build();
        api = retrofit.create(Api.class);
        api.getLoginInfo(user, pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.e("EEE", "" + jsonObject.getString("data"));
                        if ("Success".equals(jsonObject.getString("data"))) {
                            editor = preferences.edit();
                            editor.putString("user", user);
                            editor.putString("pass", pass);
                            editor.putBoolean("auto",true);
                            editor.apply();
                            App.USERNAME = user;
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if ("Failed".equals(jsonObject.getString("data"))) {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        progressDialog.cancel();
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
    }

    private boolean submit() {
        // validate
        String loginUserString = loginUser.getText().toString().trim();
        if (TextUtils.isEmpty(loginUserString)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        String loginPassString = loginPass.getText().toString().trim();
        if (TextUtils.isEmpty(loginPassString)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        // TODO validate success, do something

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
