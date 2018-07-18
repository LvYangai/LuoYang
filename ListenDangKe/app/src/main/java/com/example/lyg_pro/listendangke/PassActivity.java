package com.example.lyg_pro.listendangke;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PassActivity extends BaseActivity implements View.OnClickListener {

    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private EditText changeRawPass;
    private EditText changePass1;
    private EditText changePass2;
    private Button regist;
    private Api api;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        setStatusBar();
        initView();
    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_icon.setOnClickListener(this);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText("修改密码");
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setVisibility(View.GONE);
        changeRawPass = (EditText) findViewById(R.id.changeRawPass);
        changePass1 = (EditText) findViewById(R.id.changePass1);
        changePass2 = (EditText) findViewById(R.id.changePass2);
        regist = (Button) findViewById(R.id.regist);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                    if (submit()){
                        String user = App.USERNAME;
                        String rawPass = changeRawPass.getText().toString();
                        String changePass = changePass1.getText().toString();
                        changePass(user,rawPass,changePass);
                    }
                break;
        }
    }

    private void changePass(String user, String rawPass, String changePass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.setPassword(user,rawPass,changePass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            editor = preferences.edit();
                            editor.putBoolean("auto",false);
                            editor.apply();
                            Toast.makeText(PassActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(PassActivity.this,"服务器繁忙",Toast.LENGTH_SHORT).show();
                        }else if ("None".equals(jsonObject.getString("data"))){
                            Toast.makeText(PassActivity.this,"用户名或手机号有误",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PassActivity.this,"请求错误",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean submit() {
        // validate
        String changeRawPassString = changeRawPass.getText().toString().trim();
        if (TextUtils.isEmpty(changeRawPassString)) {
            Toast.makeText(this, "原密码未输入", Toast.LENGTH_SHORT).show();
            return false;
        }

        String changePass1String = changePass1.getText().toString().trim();
        if (TextUtils.isEmpty(changePass1String)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        String changePass2String = changePass2.getText().toString().trim();
        if (TextUtils.isEmpty(changePass2String)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!changePass1String.equals(changePass2String)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }else if(changePass1String.length()<6 || changePass1String.length()>20){
            Toast.makeText(this, "密码在6~20位之间", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
