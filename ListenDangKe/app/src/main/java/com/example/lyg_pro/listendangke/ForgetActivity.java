package com.example.lyg_pro.listendangke;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.yanzhenjie.alertdialog.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private EditText forgetUser;
    private Api api = null;
    private EditText forgetPass;
    private Button forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
        setStatusBar();

    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText("密码找回");
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.INVISIBLE);
        forgetUser = (EditText) findViewById(R.id.forgetUser);
        forgetPass = (EditText) findViewById(R.id.forgetPass);
        forget = (Button) findViewById(R.id.forget);
        forget.setOnClickListener(this);
        top_bar_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget:
                if (submit()){
                    String user = forgetUser.getText().toString();
                    String phone = forgetPass.getText().toString();
                    startForget(user,phone);
                }
                break;
            case R.id.top_bar_icon:
                finish();
                break;
        }
    }

    private void startForget(String user, String phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getForget(user,phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            showDialog();
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(ForgetActivity.this,"服务器繁忙",Toast.LENGTH_SHORT).show();
                        }else if ("None".equals(jsonObject.getString("data"))){
                            Toast.makeText(ForgetActivity.this,"用户名或手机号有误",Toast.LENGTH_SHORT).show();
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

            }
        });
    }

    private void showDialog() {
        AlertDialog.newBuilder(ForgetActivity.this)
                .setTitle("找回密码")
                .setMessage("您的密码已修改为：123456")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                }).show();
    }

    private boolean submit() {
        // validate
        String forgetUserString = forgetUser.getText().toString().trim();
        if (TextUtils.isEmpty(forgetUserString)) {
            Toast.makeText(this, " 用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String regPass3String = forgetPass.getText().toString().trim();
        if (TextUtils.isEmpty(regPass3String)) {
            Toast.makeText(this, " 手机号不能为空 ", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!isCellphone(regPass3String)){
            Toast.makeText(this, " 手机号不正确 ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // TODO validate success, do something


        return true;
    }
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    //其他区域 软键盘消失
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
