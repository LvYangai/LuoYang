package com.example.lyg_pro.listendangke.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cniao5.adapter.helper.BaseAdapterHelper;
import com.cniao5.adapter.helper.QuickAdapter;
import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.WebViewActivity;
import com.example.lyg_pro.listendangke.data.Api;
import com.example.lyg_pro.listendangke.data.App;
import com.example.lyg_pro.listendangke.data.Dangke;
import com.example.lyg_pro.listendangke.data.ImageLoader;
import com.example.lyg_pro.listendangke.data.LoadImagesTask;
import com.example.lyg_pro.listendangke.data.TimeMach;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TimeMachine extends Fragment {
    private static String TAG= Fragment_TimeMachine.class.getSimpleName();
    private List<ListTimeMach> listTime;
    private PullToRefreshListView frag_time_gridview;
    private Api api;
    private QuickAdapter<ListTimeMach> quickAdapter;
    private Intent intent;
    private LayoutInflater mInflater;
    private View mView = null;
    public Fragment_TimeMachine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_fragment__time_machine,container,false);
            mInflater=LayoutInflater.from(getActivity());
            initView(mView);
        }
        return mView;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listTime = new ArrayList<>();
        startRetrofit();
        frag_time_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title",listTime.get((int) l).getTitle());
                intent.putExtra("url",listTime.get((int) l).getInfoID());
                intent.putExtra("photoweb",998);//如果998隐藏评论
                startActivity(intent);
            }
        });
       frag_time_gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
           @Override
           public void onRefresh(PullToRefreshBase<ListView> refreshView) {
               listTime.clear();
               startRetrofit();
           }
       });


    }

    private void initView(View view) {
        frag_time_gridview = (PullToRefreshListView) view.findViewById(R.id.frag_time_gridview);
        frag_time_gridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

    }

    class ListTimeMach {
        private String infoID;
        private String channelID;
        private String title;
        private String defaultPic;
        private String intro;
        private String addDate;

        public ListTimeMach(String infoID, String channelID, String title, String defaultPic, String intro, String addDate) {
            this.infoID = infoID;
            this.channelID = channelID;
            this.title = title;
            this.defaultPic = defaultPic;
            this.intro = intro;
            this.addDate = addDate;
        }

        public String getInfoID() {
            return infoID;
        }

        public void setInfoID(String infoID) {
            this.infoID = infoID;
        }

        public String getChannelID() {
            return channelID;
        }

        public void setChannelID(String channelID) {
            this.channelID = channelID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDefaultPic() {
            return defaultPic;
        }

        public void setDefaultPic(String defaultPic) {
            this.defaultPic = defaultPic;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getAddDate() {
            return addDate;
        }

        public void setAddDate(String addDate) {
            this.addDate = addDate;
        }
    }

    private void startRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        api.getTimerInfo().enqueue(new Callback<TimeMach>() {
            @Override
            public void onResponse(Call<TimeMach> call, Response<TimeMach> response) {
                for(int i=0;i<response.body().getData().size();i++){
                    listTime.add(new ListTimeMach(App.IP+"/3g/view.aspx?m_id=2&id="+response.body().getData().get(i).getInfoID(),response.body().getData().get(i).getChannelID(),response.body().getData().get(i).getTitle(),App.IP+response.body().getData().get(i).getDefaultPic(),response.body().getData().get(i).getIntro(),response.body().getData().get(i).getAddDate()));
//                    Log.e("TIME",""+listTime.get(i).getDefaultPic());
                }
               quickAdapter = new QuickAdapter<ListTimeMach>(getActivity(),R.layout.time_gridview,listTime) {
                   @Override
                   protected void convert(BaseAdapterHelper helper, ListTimeMach item) {
                       helper.setText(R.id.time_gridview_context,""+item.getTitle())
                               .setImageUrl(R.id.time_gridview_image,item.getDefaultPic(),App.width,App.height)
                               .setText(R.id.timeData,""+item.getAddDate());
                   }
               };
                frag_time_gridview.setAdapter(quickAdapter);
                quickAdapter.notifyDataSetChanged();
                frag_time_gridview.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<TimeMach> call, Throwable t) {

            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            if (listTime!=null){
                listTime.clear();
                startRetrofit();
            }

        }
        super.onHiddenChanged(hidden);
    }
}
