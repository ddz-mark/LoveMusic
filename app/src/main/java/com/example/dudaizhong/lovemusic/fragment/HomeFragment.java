package com.example.dudaizhong.lovemusic.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dudaizhong.lovemusic.R;
import com.example.dudaizhong.lovemusic.activity.MusicPlayActivity;
import com.example.dudaizhong.lovemusic.adapter.HomeRecyclerViewAdapter;
import com.example.dudaizhong.lovemusic.common.ACache;
import com.example.dudaizhong.lovemusic.component.RetrofitSingleton;
import com.example.dudaizhong.lovemusic.modules.HotMusicApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Dudaizhong on 2016/5/25.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.fragment_home_recyclerview)
    RecyclerView fragmentHomeRecyclerview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;
    private HomeRecyclerViewAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;

    private List<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> dataList = new ArrayList<>();
    private Observer<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> observer;
    private View rootView;
    private ACache aCache;

    private final static String showapi_appid = "19405";
    private final static String showapi_sign = "8466dea6edcf4769a1dcfe6667fbf1b8";
    private final static String topid = "5";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, rootView);
            initView();//初始化基础控件
            initDataListObserve();
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private void initView() {
        aCache = new ACache();
        homeAdapter = new HomeRecyclerViewAdapter(getContext(), dataList);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        fragmentHomeRecyclerview.setLayoutManager(linearLayoutManager);
        fragmentHomeRecyclerview.setHasFixedSize(true);
        fragmentHomeRecyclerview.setAdapter(homeAdapter);
        homeAdapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Toast.makeText(getActivity(), "测试", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.tab_color_1, R.color.tab_color_2, R.color.tab_color_3, R.color.tab_color_4);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            netWork();
                        }
                    }, 1000);
                }
            });
        }

    }


    public void netWork() {
        RetrofitSingleton.getApiService(getContext())//返回一个ApiService
                .hotMusicApi(showapi_appid, showapi_sign, topid)
                .subscribeOn(Schedulers.io())//订阅发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HotMusicApi, Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>>() {
                    @Override
                    public Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> call(HotMusicApi hotMusicApi) {
                        return Observable.from(hotMusicApi.getShowapi_res_body().getPagebean().getSonglist());
                    }
                })
                .subscribe(observer);
    }

    private void initDataListObserve() {

        observer = new Observer<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>() {
            @Override
            public void onCompleted() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "网络出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean) {
                aCache.put("songlistBean", songlistBean);//将hotMusicApi进行缓存
                dataList.add(songlistBean);
                homeAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
