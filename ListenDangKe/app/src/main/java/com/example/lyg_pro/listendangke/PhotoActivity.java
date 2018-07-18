package com.example.lyg_pro.listendangke;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyg_pro.listendangke.PhotoData.ImageBean;
import com.example.lyg_pro.listendangke.PhotoData.Loader;
import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.fragment_2.BaseActivity;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yzs.imageshowpickerview.ImageShowPickerBean;
import com.yzs.imageshowpickerview.ImageShowPickerListener;
import com.yzs.imageshowpickerview.ImageShowPickerView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 233;
    List<ImageBean> list;
    ImageShowPickerView pickerView;
    private ImageView top_bar_icon;
    private TextView top_bar_title;
    private Button top_bar_search_btn;
    private ImageShowPickerView it_picker_view;
    private Api api;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private EditText upTitle;
    private EditText upcontent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        setStatusBar();
        pickerView = (ImageShowPickerView) findViewById(R.id.it_picker_view);
        list = new ArrayList<>();

        Log.e("list", "======" + list.size());
        pickerView.setImageLoaderInterface(new Loader());
        pickerView.setNewData(list);
        //展示有动画和无动画

        pickerView.setShowAnim(true);

        pickerView.setPickerListener(new ImageShowPickerListener() {
            @Override
            public void addOnClickListener(int remainNum) {
                Matisse.from(PhotoActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .theme(R.style.Matisse_Zhihu)
                        .maxSelectable(remainNum + 1)
                        .gridExpectedSize(300)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
//                Toast.makeText(PhotoActivity.this, "remainNum" + remainNum, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void picOnClickListener(List<ImageShowPickerBean> list, int position, int remainNum) {
            //    Toast.makeText(PhotoActivity.this, list.size() + "========" + position + "remainNum" + remainNum, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void delOnClickListener(int position, int remainNum) {
//                Toast.makeText(PhotoActivity.this, "delOnClickListenerremainNum" + remainNum, Toast.LENGTH_SHORT).show();
            }
        });
        pickerView.show();

        // Activity:
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale(rationaleListener)
                .callback(this)
                .start();
    }


    @PermissionYes(300)
    private void getPermissionYes(List<String> grantedPermissions) {
        // Successfully.

    }

    @PermissionNo(300)
    private void getPermissionNo(List<String> deniedPermissions) {
        // Failure.
    }

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int i, final Rationale rationale) {
            // 自定义对话框。
            AlertDialog.newBuilder(PhotoActivity.this)
                    .setTitle("请求权限")
                    .setMessage("请求权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel();
                        }
                    }).show();
        }
    };

    List<Uri> mSelected;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            mSelected = Matisse.obtainResult(data);
            List<Uri> uriList = Matisse.obtainResult(data);
            if (uriList.size() == 1) {
                pickerView.addData(new ImageBean(getRealFilePath(PhotoActivity.this, uriList.get(0))));
            } else {
                List<ImageBean> list = new ArrayList<>();
                for (Uri uri : uriList) {
                    list.add(new ImageBean(getRealFilePath(PhotoActivity.this, uri)));
                }
                pickerView.addData(list);
            }
        }
    }


    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void initView() {
        top_bar_icon = (ImageView) findViewById(R.id.top_bar_icon);
        top_bar_icon.setImageResource(R.mipmap.back);
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_title.setText("拍拍秀");
        top_bar_search_btn = (Button) findViewById(R.id.top_bar_search_btn);
        top_bar_search_btn.setBackgroundResource(R.mipmap.updown);
        it_picker_view = (ImageShowPickerView) findViewById(R.id.it_picker_view);
        top_bar_search_btn.setOnClickListener(this);
        top_bar_icon.setOnClickListener(this);
        upTitle = (EditText) findViewById(R.id.upTitle);
        upcontent = (EditText) findViewById(R.id.upcontent);
        progressDialog = new ProgressDialog(this);//1.创建一个ProgressDialog的实例
        progressDialog.setTitle("拍拍秀");//2.设置标题
        progressDialog.setMessage("正在上传中，请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_search_btn:
//                Log.e("EEE", "" + list.size() + "   " + list.get(0).getImageShowPickerUrl());
                if (submit()){
                    progressDialog.show();
                    String title = upTitle.getText().toString();
                    String content = upcontent.getText().toString();
                    String user = App.USERNAME;
                    startUpData(title,content,user);
                }
                break;
            case R.id.top_bar_icon:
                finish();
                break;
        }
    }

    private void startUpData(String utitle, String ucontent,String user) {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.2:8088")
                .baseUrl(App.IP)
                .build();
        api = retrofit.create(Api.class);
        MultipartBody.Part[] parts = new MultipartBody.Part[list.size()];
        for(int i=0;i<list.size();i++){
            File file = new File(list.get(i).getImageShowPickerUrl());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
            parts[i] = filePart;
    }
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"),utitle);
        RequestBody details = RequestBody.create(MediaType.parse("multipart/form-data"), ucontent);
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"),user);
        api.upPhotos(title,details,username,parts).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if ("Success".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhotoActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else if ("Failed".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhotoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        }else if ("Existed".equals(jsonObject.getString("data"))){
                            Toast.makeText(PhotoActivity.this,"文件已经存在",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        progressDialog.cancel();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PhotoActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
    }

    private boolean submit() {
        // validate
        String upTitleString = upTitle.getText().toString().trim();
        if (TextUtils.isEmpty(upTitleString)) {
            Toast.makeText(this, "标题还没有写", Toast.LENGTH_SHORT).show();
            return false;
        }

        String upcontentString = upcontent.getText().toString().trim();
        if (TextUtils.isEmpty(upcontentString)) {
            Toast.makeText(this, "内容还没有写", Toast.LENGTH_SHORT).show();
            return false;
        }
        list = pickerView.getDataList();
        if (list == null || list.size() == 0){
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        // TODO validate success, do something


        return true;
    }
}
