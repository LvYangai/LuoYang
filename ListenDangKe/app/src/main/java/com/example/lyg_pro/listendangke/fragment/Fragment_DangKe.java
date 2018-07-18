package com.example.lyg_pro.listendangke.fragment;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cniao5.adapter.helper.BaseAdapterHelper;
import com.cniao5.adapter.helper.QuickAdapter;
import com.cniao5.cwidgetutils.PullToRefreshListView;
import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.data.Dangke;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcking.github.com.giraffeplayer.GiraffePlayer;
import tcking.github.com.giraffeplayer.IjkVideoView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_DangKe extends Fragment {

    private static String TAG = Fragment_DangKe.class.getSimpleName();
    private PullToRefreshListView frag_dangke_gridview;
    private List<ListDangKe> dangKeList;
    private QuickAdapter<ListDangKe> quickAdapter;
    private GiraffePlayer mPlayer;
    private Api api;
    private View mView;
    private LayoutInflater mInflater;
    private ImageView app_music_play;
    private TextView app_music_title;
    private RelativeLayout playermain;
    private LinearLayout musicplayer;

    public Fragment_DangKe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_fragment__dang_ke, container, false);
            mInflater = LayoutInflater.from(getActivity());
            initView(mView);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dangKeList = new ArrayList<>();
        startRetrofit();
        mPlayer = new GiraffePlayer(getActivity());

        mPlayer.setShowNavIcon(false);

        frag_dangke_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.setSelected(true);
                adapterView.setPressed(true);
                String url = dangKeList.get((int) l).getUrl();
                url = url.substring(url.length() - 3, url.length()).toLowerCase();
                if ("mp3".equals(url)) {
                    playermain.setVisibility(View.GONE);
                    musicplayer.setVisibility(View.VISIBLE);
                    app_music_title.setText("正在播放："+dangKeList.get((int) l).getTitle());
                    Glide.with(getActivity())
                            .load(R.mipmap.musicstart).asGif()
                            .signature(new StringSignature(UUID.randomUUID().toString()))
                            .into(app_music_play);
                    mPlayer.play(dangKeList.get((int) l).getUrl());
                } else {
                    mPlayer.setTitle("" + dangKeList.get((int) l).getTitle());
                    mPlayer.setScaleType(GiraffePlayer.SCALETYPE_FILLPARENT);
                    playermain.setVisibility(View.VISIBLE);
                    musicplayer.setVisibility(View.GONE);
                    mPlayer.play(dangKeList.get((int) l).getUrl());
                }
//                Log.e("Dangke",""+url);
            }
        });
        frag_dangke_gridview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dangKeList.clear();
                startRetrofit();
            }
        });

    }

    private void initView(View view) {
        frag_dangke_gridview = (PullToRefreshListView) view.findViewById(R.id.frag_dangke_gridview);
        app_music_play = (ImageView) view.findViewById(R.id.app_music_play);
        app_music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer.isPlaying()){
                    Glide.with(getActivity())
                            .load(R.mipmap.musicstop).into(app_music_play);

                    mPlayer.pause();
                }else {
                    Glide.with(getActivity())
                            .load(R.mipmap.musicstart).asGif()
                            .signature(new StringSignature(UUID.randomUUID().toString()))
                            .into(app_music_play);
                    mPlayer.start();
                }
            }
        });
        app_music_title = (TextView) view.findViewById(R.id.app_music_title);
        musicplayer = (LinearLayout) view.findViewById(R.id.musicplayer);
        musicplayer.setVisibility(View.GONE);
        playermain = view.findViewById(R.id.app_video_box);

    }


    private class ListDangKe {
        private String title;
        private String context;
        private String url;

        public ListDangKe(String title, String context, String url) {
            this.title = title;
            this.context = context;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showBottomNavigationView();
        } else {
            hideBottomNavigationView();
        }
        if (mPlayer != null) {
            mPlayer.onConfigurationChanged(newConfig);
        }
    }

    private void hideBottomNavigationView() {
        getActivity().findViewById(R.id.bar_layout).setVisibility(View.GONE);
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
    }

    private void showBottomNavigationView() {
        getActivity().findViewById(R.id.bar_layout).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.onPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.onDestroy();
        }
    }

    private void startRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getDangkeInfo().enqueue(new Callback<Dangke>() {
            @Override
            public void onResponse(Call<Dangke> call, Response<Dangke> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    dangKeList.add(new ListDangKe(response.body().getData().get(i).getPeriodName(), response.body().getData().get(i).getAdddate(), App.IP + response.body().getData().get(i).getUrl()));
                }
                quickAdapter = new QuickAdapter<ListDangKe>(getActivity(), R.layout.dangke_gridview, dangKeList) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, ListDangKe item) {
                        helper.setText(R.id.gridview_title, item.getTitle())
                                .setText(R.id.gridview_content, item.getContext());

                    }
                };
                frag_dangke_gridview.setAdapter(quickAdapter);
                quickAdapter.notifyDataSetChanged();
                frag_dangke_gridview.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<Dangke> call, Throwable t) {

            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {

        if (hidden) {
            if (mPlayer != null) {
                mPlayer.stop();
                Glide.with(getActivity())
                        .load(R.mipmap.musicstop).into(app_music_play);
                mPlayer.onDestroy();
                mPlayer = null;

            }
        } else {
            if (mPlayer == null ){
                mPlayer = new GiraffePlayer(getActivity());
                mPlayer.stop();
            }
        }
        super.onHiddenChanged(hidden);
    }
}
