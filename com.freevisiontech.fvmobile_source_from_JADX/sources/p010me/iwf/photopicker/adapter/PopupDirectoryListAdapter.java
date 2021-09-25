package p010me.iwf.photopicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.entity.PhotoDirectory;

/* renamed from: me.iwf.photopicker.adapter.PopupDirectoryListAdapter */
public class PopupDirectoryListAdapter extends BaseAdapter {
    private List<PhotoDirectory> directories = new ArrayList();
    /* access modifiers changed from: private */
    public RequestManager glide;

    public PopupDirectoryListAdapter(RequestManager glide2, List<PhotoDirectory> directories2) {
        this.directories = directories2;
        this.glide = glide2;
    }

    public int getCount() {
        return this.directories.size();
    }

    public PhotoDirectory getItem(int position) {
        return this.directories.get(position);
    }

    public long getItemId(int position) {
        return (long) this.directories.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(C1661R.layout.__picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindData(this.directories.get(position));
        return convertView;
    }

    /* renamed from: me.iwf.photopicker.adapter.PopupDirectoryListAdapter$ViewHolder */
    private class ViewHolder {
        public ImageView ivCover;
        public TextView tvCount;
        public TextView tvName;

        public ViewHolder(View rootView) {
            this.ivCover = (ImageView) rootView.findViewById(C1661R.C1663id.iv_dir_cover);
            this.tvName = (TextView) rootView.findViewById(C1661R.C1663id.tv_dir_name);
            this.tvCount = (TextView) rootView.findViewById(C1661R.C1663id.tv_dir_count);
        }

        public void bindData(PhotoDirectory directory) {
            PopupDirectoryListAdapter.this.glide.load(directory.getCoverPath()).dontAnimate().thumbnail(0.1f).into(this.ivCover);
            this.tvName.setText(directory.getName());
            this.tvCount.setText(this.tvCount.getContext().getString(C1661R.string.__picker_image_count, new Object[]{Integer.valueOf(directory.getPhotos().size())}));
        }
    }
}
