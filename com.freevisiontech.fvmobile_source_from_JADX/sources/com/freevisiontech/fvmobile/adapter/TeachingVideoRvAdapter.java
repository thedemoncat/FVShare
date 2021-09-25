package com.freevisiontech.fvmobile.adapter;

import android.graphics.Color;
import android.support.p003v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.TeachingVideo;
import java.util.List;

public class TeachingVideoRvAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<TeachingVideo> mData;

    public TeachingVideoRvAdapter(List<TeachingVideo> mData2) {
        this.mData = mData2;
    }

    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0853R.layout.item_teaching_video_recycler_view, parent, false));
    }

    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.itemTitleTv.setText(this.mData.get(position).getVideoTitle());
        holder.itemContentTv.setText(this.mData.get(position).getVideoContent());
        holder.itemContentTv.setLinkTextColor(Color.parseColor("#3b44fe"));
        holder.itemContentTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public int getItemCount() {
        return this.mData.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView itemContentTv;
        TextView itemTitleTv;

        public VideoViewHolder(View itemView) {
            super(itemView);
            this.itemTitleTv = (TextView) itemView.findViewById(C0853R.C0855id.video_item_title);
            this.itemContentTv = (TextView) itemView.findViewById(C0853R.C0855id.video_item_content);
        }
    }
}
