package com.example.lyg_pro.listendangke;

import android.content.Context;
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

public class NewsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private EditText regUser;
    private EditText regPass1;
    private EditText regPass2;
    private Api api = null;
    private Button regist;
    private EditText regPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setStatusBar();
        initView();
    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText("用户注册");
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.INVISIBLE);
        regUser = (EditText) findViewById(R.id.regUser);
        regPass1 = (EditText) findViewById(R.id.regPass1);
        regPass2 = (EditText) findViewById(R.id.regPass2);
        regist = (Button) findViewById(R.id.regist);

        top_bar_icon.setOnClickListener(this);
        regist.setOnClickListener(this);
        regPhone = (EditText) findViewById(R.id.regPhone);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist:
                if (submit()) {
                    String user = regUser.getText().toString();
                    String pass = regPass1.getText().toString();
                    String phone = regPhone.getText().toString();
                    startReg(user,pass,phone);
                }
                break;
            case R.id.top_bar_icon:
                finish();
                break;
        }
    }

    private void startReg(final String user, final String pass, final String phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getRegedit(user,pass,phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            Toast.makeText(NewsActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(NewsActivity.this,"服务器繁忙",Toast.LENGTH_SHORT).show();
                        }else if ("Existed".equals(jsonObject.getString("data"))){
                            Toast.makeText(NewsActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                        }else if ("PhoneExisted".equals(jsonObject.getString("data"))){
                            Toast.makeText(NewsActivity.this,"手机号已被注册",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewsActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean submit() {
        String regUserString = regUser.getText().toString().trim();
        if (TextUtils.isEmpty(regUserString)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (regUserString.length()<4){
            Toast.makeText(this, " 用户名长度小于4位", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!checkAccountMark(regUserString)){
            Toast.makeText(this, " 用户名只能包含字母，数字", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!isChineseStr(regUserString)){
            Toast.makeText(this, " 用户名不能使用中文", Toast.LENGTH_SHORT).show();
            return false;
        }

        String regPass1String = regPass1.getText().toString().trim();
        if (TextUtils.isEmpty(regPass1String)) {
            Toast.makeText(this, " 密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (regPass1String.length()<6||regPass1String.length()>30){
            Toast.makeText(this, " 密码长度在6~20位之间", Toast.LENGTH_SHORT).show();
            return false;
        }

        String regPass2String = regPass2.getText().toString().trim();
        if (TextUtils.isEmpty(regPass2String)) {
            Toast.makeText(this, " 确认密码不能为空 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(regPass1String.equals(regPass2String))){
            Toast.makeText(this, " 两次密码不一致 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        String regPass3String = regPhone.getText().toString().trim();
        if (TextUtils.isEmpty(regPass3String)) {
            Toast.makeText(this, " 手机号不能为空 ", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!isCellphone(regPass3String)){
            Toast.makeText(this, " 手机号不正确 ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //判断注册是否有中文
    public boolean isChineseStr(String str){

        String regex = "[\\u4e00-\\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int count=0;
        while (matcher.find()) {
            count++;
        }
//        String regEx = "[\u4e00-\u9fa5]";
//        Pattern p = Pattern.compile(regEx);
//        int num = 0;//汉字长度
//        char c[] = str.toCharArray();
//        for(int i=0;i<str.length();i++){
//            if(p.matches(regEx, str.substring(i, i + 1))){
//                num++;
//            }
//            Matcher matcher = p.matcher(String.valueOf(c[i]));
//            if(!matcher.matches()){
//                return true;
//            }
//        }

//        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
//        char c[] = str.toCharArray();
//        for(int i=0;i<c.length;i++){
//            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
//            if(!matcher.matches()){
//                return false;
//            }
//        }
        if (count>0){
            return false;
        }else{
            return true;
        }

    }
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public static boolean checkAccountMark(String account){
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all,account);
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
