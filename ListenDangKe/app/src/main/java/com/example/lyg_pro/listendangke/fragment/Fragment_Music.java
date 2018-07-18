package com.example.lyg_pro.listendangke.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cniao5.cwidgetutils.PullToRefreshListView;
import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.data.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tcking.github.com.giraffeplayer.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Music extends Fragment {

    private static String TAG = Fragment_Music.class.getSimpleName();
    private List<ListMusic> musicList;
    private MusicListviewAdapter musicListviewAdapter;
    private ListView frag_music_listview;
    private int select = -1;
    private MusicListviewAdapter.ViewHolder viewHolder = null, viewHolder1 = null;
    private Api api;
    private View mView;
    private RelativeLayout app_video_box;
    private PullToRefreshListView pullToRefreshListView;
    private IjkVideoView mPlayer;

    public Fragment_Music() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer.release(true);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_fragment__music, container, false);
            initView(mView);
        }
        initView(mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        musicList = new ArrayList<>();
//        musicList.add(new ListMusic("dada", "dsada", "http://file.kuyinyun.com/group1/M00/52/54/rBBGdFO10gGAV2YSABeMNNnn7Sc042.mp3"));
//        musicList.add(new ListMusic("dada", "dsada", "http://file.kuyinyun.com/group1/M00/52/54/rBBGdFO10gGAV2YSABeMNNnn7Sc042.mp3"));
//        musicList.add(new ListMusic("dada", "dsada", "http://file.kuyinyun.com/group1/M00/52/54/rBBGdFO10gGAV2YSABeMNNnn7Sc042.mp3"));
        startRetrofit();
        musicListviewAdapter = new MusicListviewAdapter();
        frag_music_listview.setAdapter(musicListviewAdapter);
        frag_music_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //                Log.e("EEE","sdsad");
//                Log.e("EEE", "i = " + i + "  select = " + select+"   "+player.isPlaying());
//                int i = (int) l1;
                View view1 = frag_music_listview.getChildAt(i);
                viewHolder = (MusicListviewAdapter.ViewHolder) view1.getTag();
//                viewHolder.musicImage.setBackgroundResource(R.mipmap.start);
                Glide.with(getActivity())
                        .load(R.mipmap.musicstop)
                        .into(viewHolder.musicImage);
                if (select == i) {
                    if (mPlayer.isPlaying()) {
//                        Log.e("EEE", "no");
//                        viewHolder.musicImage.setBackgroundResource(R.mipmap.stop);
//                        player.start();
                        Glide.with(getActivity())
                                .load(R.mipmap.musicstop)
                                .into(viewHolder.musicImage);
                        mPlayer.pause();
                    } else {
//                        mediaPlayer.reset();
//                        Log.e("EEE", "isif"+player.isPlaying());
//                        viewHolder.musicImage.setBackgroundResource(R.mipmap.start);
                        Glide.with(getActivity())
                                .load(R.mipmap.musicstart).asGif()
                                .signature(new StringSignature(UUID.randomUUID().toString()))
                                .into(viewHolder.musicImage);
                        mPlayer.start();
                        if (!mPlayer.isPlaying()) {
                            Glide.with(getActivity())
                                    .load(R.mipmap.musicstart).asGif()
                                    .signature(new StringSignature(UUID.randomUUID().toString()))
                                    .into(viewHolder.musicImage);
                        }
                    }
                } else {
//            v.setBackgroundResource(R.mipmap.stop);
                    if (mPlayer.isPlaying() && mPlayer != null) {
//                        Log.e("EEE", "else is player");
                        mPlayer.stopPlayback();
                        mPlayer.release(true);
//                        mediaPlayer.reset();
                    }
                    if (select >= 0) {
//                        Log.e("EEE", "else >0");
                        View view2 = frag_music_listview.getChildAt(select);
                        viewHolder1 = (MusicListviewAdapter.ViewHolder) view2.getTag();
//                        viewHolder1.musicImage.setBackgroundResource(R.mipmap.start);
                        Glide.with(getActivity())
                                .load(R.mipmap.musicstop)
                                .into(viewHolder1.musicImage);

                    }
                    Glide.with(getActivity())
                            .load(R.mipmap.musicstart).asGif()
                            .signature(new StringSignature(UUID.randomUUID().toString()))
                            .into(viewHolder.musicImage);
//                    mediaPlayer.play(musicList.get(i).getMusicUrl());
                    initMediaPlayer(i);
                }
                select = i;
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                musicList.clear();
                startRetrofit();
            }
        });



    }

    private void initMediaPlayer(int i) {

        try {
            mPlayer.setVideoURI(Uri.parse(musicList.get(i).getMusicUrl())); // 设置数据源
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void startRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getMusicInfo().enqueue(new Callback<Music>() {
            @Override
            public void onResponse(Call<Music> call, Response<Music> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
//                    Log.e("EEE",""+response.body().getData().get(i).getUrl());
                    musicList.add(new ListMusic(response.body().getData().get(i).getPeriodName(), response.body().getData().get(i).getAdddate(), App.IP + response.body().getData().get(i).getUrl()));
                }

                musicListviewAdapter.notifyDataSetChanged();
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {

            }
        });

    }

    private void initView(View view) {
        frag_music_listview = (ListView) view.findViewById(R.id.frag_music_listview);
        mPlayer = (IjkVideoView) view.findViewById(R.id.ijkVideo);
        pullToRefreshListView = view.findViewById(R.id.frag_music_listview);
        mPlayer.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayer != null) {
            mPlayer.release(true);
            mPlayer = null;
        }
    }

    public class ListMusic {
        private String title;
        private String context;
        private String musicUrl;

        public ListMusic(String title, String context, String musicUrl) {
            this.title = title;
            this.context = context;
            this.musicUrl = musicUrl;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }

    public class MusicListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return musicList.size();
        }

        @Override
        public Object getItem(int position) {
            return musicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.music_listview, null);
                holder.musicImage = (ImageView) view.findViewById(R.id.music_image);
                holder.musicTitle = (TextView) view.findViewById(R.id.music_title);
                holder.musicContext = (TextView) view.findViewById(R.id.music_context);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
//            holder.musicImage.setBackgroundResource(R.mipmap.start);
            Glide.with(getActivity())
                    .load(R.mipmap.musicstop)
                    .into(holder.musicImage);
            holder.musicTitle.setText("" + musicList.get(position).getTitle());
            holder.musicContext.setText("" + musicList.get(position).getContext());
            return view;
        }


        public final class ViewHolder {
            private ImageView musicImage;
            private TextView musicTitle;
            private TextView musicContext;
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (mPlayer != null || mPlayer.isPlaying()) {
                if (viewHolder != null) {
                    Glide.with(getActivity())
                            .load(R.mipmap.musicstop)
                            .into(viewHolder.musicImage);
                }
                mPlayer.stopPlayback();
                mPlayer.release(true);
                mPlayer = null;
            }

        } else {
            mPlayer = mView.findViewById(R.id.ijkVideo);
            mPlayer.release(true);

        }
        super.onHiddenChanged(hidden);
    }
}
