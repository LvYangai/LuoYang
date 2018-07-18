package com.example.lyg_pro.listendangke;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class PhoneActivity extends BaseActivity implements View.OnClickListener {

    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private EditText phonePass;
    private EditText phonePhone;
    private Button regist;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        setStatusBar();
        initView();
    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_icon.setOnClickListener(this);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText("修改手机号");
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.GONE);
        phonePass = (EditText) findViewById(R.id.phonePass);
        phonePhone = (EditText) findViewById(R.id.phonePhone);
        regist = (Button) findViewById(R.id.regist);
        top_bar_search_btn.setOnClickListener(this);
        regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_icon:
                finish();
                break;
            case R.id.regist:
                if(submit()){

                    String user = App.USERNAME;
                    String pass = phonePass.getText().toString().trim();
                    String phone = phonePhone.getText().toString();
                    changePhone(user,pass,phone);
                }
                break;
        }
    }

    private void changePhone(String user, String pass, String phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.setChagePhone(user,pass,phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhoneActivity.this,"修改手机号成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhoneActivity.this,"服务器繁忙",Toast.LENGTH_SHORT).show();
                        }else if ("None".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhoneActivity.this,"原密码有误",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PhoneActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean submit() {
        // validate
        String phonePassString = phonePass.getText().toString().trim();
        if (TextUtils.isEmpty(phonePassString)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        String regPass3String = phonePhone.getText().toString().trim();
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
}
