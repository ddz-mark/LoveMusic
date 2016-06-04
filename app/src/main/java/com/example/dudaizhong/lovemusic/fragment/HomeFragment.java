package com.example.dudaizhong.lovemusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dudaizhong.lovemusic.R;
import com.example.dudaizhong.lovemusic.activity.MusicPlayActivity;
import com.example.dudaizhong.lovemusic.adapter.HomeRecyclerViewAdapter;
import com.example.dudaizhong.lovemusic.common.ACache;
import com.example.dudaizhong.lovemusic.common.PLog;
import com.example.dudaizhong.lovemusic.component.RetrofitSingleton;
import com.example.dudaizhong.lovemusic.modules.HotMusicApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by Dudaizhong on 2016/5/25.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.fragment_home_recyclerview)
    RecyclerView fragmentHomeRecyclerview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.iv_erro)
    ImageView ivErro;

    private HomeRecyclerViewAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;

    private List<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> dataList = new ArrayList<>();
    private Observer<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> observer;
    HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean;
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
            load();
        }
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private void initView() {

        aCache = new ACache();
        homeAdapter = new HomeRecyclerViewAdapter(getContext(), dataList);

        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.tab_color_1);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            load();
                        }
                    }, 1000);
                }
            });
        }

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        fragmentHomeRecyclerview.setLayoutManager(linearLayoutManager);
        fragmentHomeRecyclerview.setHasFixedSize(true);
        fragmentHomeRecyclerview.setAdapter(homeAdapter);
        homeAdapter.setOnItemClickListener(new HomeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * 优化网络 + 缓存逻辑
     */
    private void load() {
        Observable.concat(fetchDataByNetWork(), fetchDataByCache())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        progressBar.setVisibility(View.INVISIBLE);
                        ivErro.setVisibility(View.VISIBLE);
                    }
                })
                .doOnNext(new Action1<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>() {
                    @Override
                    public void call(HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean) {
                        progressBar.setVisibility(View.GONE);
                        fragmentHomeRecyclerview.setVisibility(View.VISIBLE);
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .subscribe(observer);
    }

    /**
     * 从网络获取
     */
    private Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> fetchDataByNetWork() {

        return RetrofitSingleton.getInstance()
                .hotMusicApiObservable(showapi_appid, showapi_sign, topid)
                .onErrorReturn(new Func1<Throwable, HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>() {
                    @Override
                    public HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean call(Throwable throwable) {
                        PLog.e(throwable.getMessage());
                        return null;
                    }
                });
    }

    /**
     * 从缓存获取
     *
     * @return
     */
    private Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> fetchDataByCache() {

        return Observable.defer(new Func0<Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>>() {
            @Override
            public Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> call() {
                songlistBean = (HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean) aCache.getAsObject("songlistBean");
                return Observable.just(songlistBean);
            }
        });
    }

    private void initDataListObserve() {

        observer = new Observer<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>() {
            @Override
            public void onCompleted() {
//                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
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
