package com.example.lyg_pro.listendangke.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.data.DefineView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements DefineView {

    private static String TAG = Fragment_News.class.getSimpleName();
    private Fragment mCurFragment = new Fragment();
    private View mView;
    private String table1 = "国内",table2 = "镇上";
    private TabLayout tab_layout;
    private RelativeLayout newsContent;
    private Fragment_News fragmentNews = new Fragment_News();
    private Fragment_News2 fragmentNews2 = new Fragment_News2();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            initView();
            initValidata();
            initListener();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (table1.equals(tab.getText())){
                    switchNews1();
                }else if (table2.equals(tab.getText())){
                    switchNews2();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {//如果要显示的targetFragment没有添加过
            transaction
                    .hide(mCurFragment)//隐藏当前Fragment
                    .add(R.id.newsContent, targetFragment, targetFragment.getClass().getName())//添加targetFragment
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

    @Override
    public void initView() {
        tab_layout = (TabLayout) mView.findViewById(R.id.tab_layout);
        newsContent = (RelativeLayout) mView.findViewById(R.id.newsContent);

    }

    @Override
    public void initValidata() {
        switchNews1();
    }

    @Override
    public void initListener() {
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.addTab(tab_layout.newTab().setText(R.string.home_news1));
        tab_layout.addTab(tab_layout.newTab().setText(R.string.home_news2));
    }

    @Override
    public void bindData() {

    }

    private void switchNews1() {
        switchFragment(fragmentNews);
    }

    private void switchNews2() {
        switchFragment(fragmentNews2);
    }
}
