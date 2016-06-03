package com.example.dudaizhong.lovemusic.component;

import com.example.dudaizhong.lovemusic.modules.HotMusicApi;
import com.example.dudaizhong.lovemusic.modules.Lyric;
import com.example.dudaizhong.lovemusic.modules.QueryMusic;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dudaizhong on 2016/5/27.
 */
public interface ApiService {

    String BASE_URL = "http://route.showapi.com/";

    //热门榜单
    @GET("213-4")
    rx.Observable<HotMusicApi> hotMusicApi(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign") String showapi_sign, @Query("topid") String topid);

    //根据歌曲id查询歌词
    @GET("213-2")
    rx.Observable<Lyric> lyric(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign") String showapi_sign, @Query("musicid") String musicid);

    //根据歌名，人命查询歌曲
    @GET("213-1")
    rx.Observable<QueryMusic> queryMusic(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign") String showapi_sign,
                                         @Query("keyword") String keyword, @Query("page") String page);
}
