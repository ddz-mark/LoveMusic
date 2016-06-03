package com.example.dudaizhong.lovemusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dudaizhong.lovemusic.R;
import com.example.dudaizhong.lovemusic.modules.HotMusicApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Dudaizhong on 2016/5/25.
 */
public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> dataList = new ArrayList<>();
    private HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean = null;

    public HomeRecyclerViewAdapter(Context context, List<HotMusicApi.ShowapiResBodyBean.PagebeanBean.SonglistBean> data) {
        this.context = context;
        this.dataList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //视图建立联系
        songlistBean = dataList.get(position);
        holder.itemHomeSinger.setText(songlistBean.getSingername());
        holder.itemHomeSongname.setText(songlistBean.getSongname());
        Glide.with(context).load(songlistBean.getAlbumpic_small()).crossFade().into(holder.itemhomeposterimg);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_home)
        RelativeLayout home_layout;
        @Bind(R.id.item_home_poster_img)
        ImageView itemhomeposterimg;
        @Bind(R.id.item_home_songname)
        TextView itemHomeSongname;
        @Bind(R.id.item_home_singer)
        TextView itemHomeSinger;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            home_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
