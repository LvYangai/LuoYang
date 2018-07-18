package com.example.lyg_pro.listendangke.fragment_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lyg_pro.listendangke.R;
import com.example.lyg_pro.listendangke.data.ImageLoader;
import com.example.lyg_pro.listendangke.data.LoadImagesTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Page extends Fragment {


    private ImageView frag_viewpager_title_img;
    private Bundle bundle;
//    private ImageLoader loader;
    public Fragment_Page() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        loader = new ImageLoader(getActivity());
        View view = inflater.inflate(R.layout.fragment_fragment__page, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        frag_viewpager_title_img = (ImageView) view.findViewById(R.id.frag_viewpager_title_img);
        bundle = getArguments();
        String image = bundle.getString("image");
        new LoadImagesTask(frag_viewpager_title_img).execute(image);
//        loader.DisplayImage(image,frag_viewpager_title_img);
    }
}
