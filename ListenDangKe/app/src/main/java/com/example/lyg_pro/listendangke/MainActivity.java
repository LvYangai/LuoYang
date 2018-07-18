package com.example.lyg_pro.listendangke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyg_pro.listendangke.adapter.LeftItemAdapter;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.data.DefineView;
import com.example.lyg_pro.listendangke.fragment.Fragment_DangKe;
import com.example.lyg_pro.listendangke.fragment.Fragment_Music;
import com.example.lyg_pro.listendangke.fragment.Fragment_News;
import com.example.lyg_pro.listendangke.fragment.Fragment_TimeMachine;
import com.example.lyg_pro.listendangke.fragment.HomeFragment;
import com.example.lyg_pro.listendangke.fragment_2.BaseActivity;
import com.example.lyg_pro.listendangke.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.GiraffePlayer;

public class MainActivity extends BaseActivity implements DefineView {

    private int page = 0;
    private ImageView top_bar_icon;
    private DragLayout drag_layout;
    private TextView titleMess;
    private TextView leftUser;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Fragment mCurFragment = new Fragment();
    private HomeFragment homeFragment = new HomeFragment();
    private Fragment_DangKe fragmentDangKe = new Fragment_DangKe();
    private Fragment_Music fragmentMusic = new Fragment_Music();
    private Fragment_TimeMachine fragmentTimeMachine = new Fragment_TimeMachine();
    private BottomNavigationView navigation;
    private ListView lv_left_main;
    private Button top_bar_search_btn;
    private long firstTime = 0;

    public DragLayout getDrag_layout() {
        return drag_layout;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    switchNews();
                    titleMess.setText(R.string.title_home);
                    page = 0;
                    return true;
                case R.id.navigation_dangke:
                    switchDangke();
                    titleMess.setText(R.string.title_dashboard);
                    page = 1;
                    return true;
                case R.id.navigation_music:
                    switchMusic();
                    titleMess.setText(R.string.title_notifications);
                    page = 2;
                    return true;
                case R.id.navigation_timemachine:
                    switchTimeChine();
                    titleMess.setText(R.string.title_notifications2);
                    page = 3;
                    return true;
            }
            return false;
        }
    };
    public void switchFragment(Fragment targetFragment){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {//如果要显示的targetFragment没有添加过
            transaction
                    .hide(mCurFragment)//隐藏当前Fragment
                    .add(R.id.maincontent, targetFragment,targetFragment.getClass().getName())//添加targetFragment
                    .commit();
        } else {//如果要显示的targetFragment已经添加过
            transaction//隐藏当前Fragment
                    .hide(mCurFragment)
                    .show(targetFragment)//显示targetFragment
                    .commit();
        }
        //更新当前Fragment为targetFragment
        mCurFragment = targetFragment;

    }

    private void switchTimeChine() {
        switchFragment(fragmentTimeMachine);
    }

    private void switchMusic() {
        switchFragment(fragmentMusic);
    }

    private void switchDangke() {
        switchFragment(fragmentDangKe);
    }

    private void switchNews() {
        switchFragment(homeFragment);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        initView();
        initValidata();
        initListener();
        bindData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        leftUser.setText("用户名:"+preferences.getString("user","null"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationViewHelper.disableShiftMode(navigation);
        switch (page) {
            case 0:
                switchNews();
                titleMess.setText(R.string.title_home);
                break;
            case 1:
                switchDangke();
                titleMess.setText(R.string.title_dashboard);
                break;
            case 2:
                switchMusic();
                titleMess.setText(R.string.title_notifications);
                break;
            case 3:
                switchTimeChine();
                titleMess.setText(R.string.title_notifications2);
                break;
        }
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        App.width = (int) ((wm.getDefaultDisplay().getWidth())*0.84);
        App.height = (int) ((wm.getDefaultDisplay().getHeight())*0.23);

    }

    @Override
    public void initView() {
        top_bar_icon = findViewById(R.id.top_bar_icon);
        titleMess = findViewById(R.id.top_bar_title);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        drag_layout = findViewById(R.id.drag_layout);
        lv_left_main=(ListView)findViewById(R.id.lv_left_main);
        leftUser = findViewById(R.id.leftUser);
        top_bar_search_btn = findViewById(R.id.top_bar_search_btn);
    }

    @Override
    public void initValidata() {
        lv_left_main.setAdapter(new LeftItemAdapter(MainActivity.this));
    }
    @Override
    public void initListener() {
        lv_left_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        startActivity(new Intent(MainActivity.this,PassActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,PhoneActivity.class));
                        break;
                    case 2:
                        editor = preferences.edit();
                        editor.putBoolean("auto",false);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        break;
                }
            }
        });
        drag_layout.setDragListener(new CustomDragListener());
        top_bar_icon.setOnClickListener(new CustomOnClickListener());
        top_bar_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"photo",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,PhotoActivity.class));
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("EEE","main restart");
        if (!preferences.getBoolean("auto",false)){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }

    @Override
    public void bindData() {

    }

    class CustomDragListener implements DragLayout.DragListener{

        /**
         * 界面打开
         */
        @Override
        public void onOpen() {

        }

        /**
         * 界面关闭
         */
        @Override
        public void onClose() {

        }

        /**
         * 界面进行滑动
         * @param percent
         */
        @Override
        public void onDrag(float percent) {
            ViewHelper.setAlpha(top_bar_icon,1-percent);
        }
    }
    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {
            drag_layout.open();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
