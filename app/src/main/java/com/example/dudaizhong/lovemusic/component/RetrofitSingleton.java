package com.example.dudaizhong.lovemusic.component;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dudaizhong.lovemusic.modules.HotMusicApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Dudaizhong on 2016/5/27.
 * <p/>
 * 此类主要用于返回ApiService和失败时的错误信息
 */
public class RetrofitSingleton {

    private static ApiService apiService = null;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static final String TAG = RetrofitSingleton.class.getSimpleName();

    private RetrofitSingleton() {
        initOkHttp();//初始化Okhttp
        initRetrofit();//初始化Retrofit
        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
    }

    private static void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static void initOkHttp() {
        // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();//拦截器，用于输出网络请求和结果的Log
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)//设置出现错误重新连接
                .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
                .build();
    }


    /**
     * 显示请求失败时的错误信息
     *
     * @param t
     * @param context
     * @param view
     */
    public static void disposeFailureInfo(Throwable t, Context context, View view) {
        if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                t.toString().contains("UnknownHostException")) {
            //Snackbar.make(view, "网络不好,~( ´•︵•` )~", Snackbar.LENGTH_LONG).show();
            Toast.makeText(context, "网络不好", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, t.toString());
    }

    public Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> hotMusicApiObservable(String showapi_appid, String showapi_sign, String topid){
        return apiService.hotMusicApi(showapi_appid,showapi_sign,topid)
                .subscribeOn(Schedulers.io())//订阅发生在IO线程
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HotMusicApi, Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean>>() {
                    @Override
                    public Observable<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> call(HotMusicApi hotMusicApi) {
                        return Observable.from(hotMusicApi.getShowapi_res_body().getPagebean().getSonglist());
                    }
                });
    }
}
