package com.example.lyg_pro.listendangke.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5.adapter.helper.BaseAdapterHelper;
import com.cniao5.adapter.helper.QuickAdapter;
import com.cniao5.cwidgetutils.PullToRefreshListView;
import com.example.lyg_pro.listendangke.PhotoData.GlideImageLoader;
import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.WebViewActivity;
import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.data.ImageLoader;
import com.example.lyg_pro.listendangke.data.LoadImagesTask;
import com.example.lyg_pro.listendangke.fragment_2.Fragment_Page;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_News extends Fragment {

    private static String TAG= Fragment_News.class.getSimpleName();
    private View mView;
    private Banner banner;
    private Intent intent;
    private LayoutInflater mInflater;
    private QuickAdapter<ListNews> quickAdapter;
    private Api api = null;
    private View headView;
    private ArrayList image = new ArrayList();
    private ArrayList title = new ArrayList();
    private PullToRefreshListView fragnews_listview;
    private List<ListNews> newsList;
    public Fragment_News() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mView==null){
            mView = inflater.inflate(R.layout.fragment_fragment__news, container, false);
            mInflater=LayoutInflater.from(getActivity());
            headView=mInflater.inflate(R.layout.news_headerview,null);
            initView(mView);
            image.clear();
            title.clear();
        }
        return mView;
    }

    private void startRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getNewsInfo().enqueue(new Callback<com.example.lyg_pro.listendangke.data.News>() {
            @Override
            public void onResponse(Call<com.example.lyg_pro.listendangke.data.News> call, Response<com.example.lyg_pro.listendangke.data.News> response) {
                for(int i=0;i<response.body().getData().size();i++){
                    newsList.add(new ListNews(App.IP+response.body().getData().get(i).getDefaultPic(),response.body().getData().get(i).getTitle(),App.IP+"/3g/view.aspx?m_id="+response.body().getData().get(i).getChannelID()+"&id="+response.body().getData().get(i).getInfoID(),response.body().getData().get(i).getInfoID(),response.body().getData().get(i).getChannelID(),response.body().getData().get(i).getClassID(),response.body().getData().get(i).getAddDate(),response.body().getData().get(i).getIntro()));

                    if (i<3){
                        image.add(newsList.get(i).getImageUrl());
                        title.add(newsList.get(i).getTitle());
                    }
                }
                initData();
                quickAdapter=new QuickAdapter<ListNews>(getActivity(),R.layout.new_listview,newsList) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, ListNews item) {
                        helper.setText(R.id.frag_list_title,item.getTitle())
                                .setText(R.id.frag_list_time,item.getTime())
                                .setImageUrl(R.id.news_list_img,item.getImageUrl());
                    }
                };
                fragnews_listview.setAdapter(quickAdapter);
                quickAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<com.example.lyg_pro.listendangke.data.News> call, Throwable t) {
//                newsList.clear();
                image.clear();
                title.clear();
                image.add("http://mmbiz.qpic.cn/mmbiz_png/ENDandl7DNagM1AMDmJ5BBh13SicxqvUePicu1icprwz9lia0wyKjVB61kBYIrLQibPuZVvibibV4libOeosV7HxtaPt9g/0?wx_fmt=png");
//        image.add("        image.add(\"http://mmbiz.qpic.cn/mmbiz_png/ENDandl7DNagM1AMDmJ5BBh13SicxqvUePicu1icprwz9lia0wyKjVB61kBYIrLQibPuZVvibibV4libOeosV7HxtaPt9g/0?wx_fmt=png")
                image.add("http://mmbiz.qpic.cn/mmbiz_png/ENDandl7DNagM1AMDmJ5BBh13SicxqvUeyVlvtV3GFsKzHOuJ5NgDg7D9e7fKbv4lGBUchSraApgiaIT166kZP0A/0?wx_fmt=png");
                image.add("http://mmbiz.qpic.cn/mmbiz_png/ENDandl7DNagM1AMDmJ5BBh13SicxqvUePicu1icprwz9lia0wyKjVB61kBYIrLQibPuZVvibibV4libOeosV7HxtaPt9g/0?wx_fmt=png");
                title.add("ssadsa");
                title.add("dsadsadsaff");
                title.add("ffffsssd");
                initData();
            }
        });
    }

    private void initData() {
        banner.setImages(image)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setBannerTitles(title)
                .setImageLoader(new GlideImageLoader())
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int l) {
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("channelid",newsList.get(l).getChannelId());
                intent.putExtra("classid",newsList.get(l).getClassId());
                intent.putExtra("infoid",newsList.get(l).getInfoId());
                intent.putExtra("title",newsList.get(l).getTitle());
                intent.putExtra("url",newsList.get(l).getUrl());
                startActivity(intent);
            }
        });
        fragnews_listview.onRefreshComplete();
    }


    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        Log.e("EEE","news1 start");
        banner.startAutoPlay();

    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsList = new ArrayList<>();
        startRetrofit();
        fragnews_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("channelid",newsList.get((int) l).getChannelId());
                intent.putExtra("classid",newsList.get((int) l).getClassId());
                intent.putExtra("infoid",newsList.get((int) l).getInfoId());
                intent.putExtra("title",newsList.get((int) l).getTitle());
                intent.putExtra("url",newsList.get((int) l).getUrl());
                startActivity(intent);
            }
        });

        fragnews_listview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsList.clear();
                title.clear();
                image.clear();
                startRetrofit();
            }
        });


    }


    private void initView(View view) {
        banner = headView.findViewById(R.id.bannerNews1);
        fragnews_listview = (PullToRefreshListView) view.findViewById(R.id.fragnews_listview);
        fragnews_listview.addHeaderView(headView);
    }

    private class ListNews{
        private String imageUrl;
        private String title;
        private String url;
        private String infoId;
        private String channelId;
        private String classId;
        private String content;
        private String time;

        public ListNews(String imageUrl, String title,String url,String infoId,String channelId,String classId, String time,String content) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.url = url;
            this.infoId = infoId;
            this.channelId = channelId;
            this.classId = classId;
            this.time = time;
            this.content = content;

        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }


        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getInfoId() {
            return infoId;
        }

        public void setInfoId(String infoId) {
            this.infoId = infoId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            banner.stopAutoPlay();
        }else {
            banner.startAutoPlay();
        }
    }
}
