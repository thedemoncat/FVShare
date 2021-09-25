package p010me.iwf.photopicker.adapter;

import android.content.Context;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.entity.Photo;
import p010me.iwf.photopicker.entity.PhotoDirectory;
import p010me.iwf.photopicker.event.OnItemCheckListener;
import p010me.iwf.photopicker.event.OnPhotoClickListener;
import p010me.iwf.photopicker.utils.AndroidLifecycleUtils;

/* renamed from: me.iwf.photopicker.adapter.PhotoGridAdapter */
public class PhotoGridAdapter extends SelectableAdapter<PhotoViewHolder> {
    private static final int COL_NUMBER_DEFAULT = 3;
    public static final int ITEM_TYPE_CAMERA = 100;
    public static final int ITEM_TYPE_PHOTO = 101;
    private int columnNumber;
    private RequestManager glide;
    private boolean hasCamera;
    private int imageSize;
    /* access modifiers changed from: private */
    public View.OnClickListener onCameraClickListener;
    /* access modifiers changed from: private */
    public OnItemCheckListener onItemCheckListener;
    /* access modifiers changed from: private */
    public OnPhotoClickListener onPhotoClickListener;
    /* access modifiers changed from: private */
    public boolean previewEnable;

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories) {
        this.onItemCheckListener = null;
        this.onPhotoClickListener = null;
        this.onCameraClickListener = null;
        this.hasCamera = true;
        this.previewEnable = true;
        this.columnNumber = 3;
        this.photoDirectories = photoDirectories;
        this.glide = requestManager;
        setColumnNumber(context, this.columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<PhotoDirectory> photoDirectories, ArrayList<String> orginalPhotos, int colNum) {
        this(context, requestManager, photoDirectories);
        setColumnNumber(context, colNum);
        this.selectedPhotos = new ArrayList();
        if (orginalPhotos != null) {
            this.selectedPhotos.addAll(orginalPhotos);
        }
    }

    private void setColumnNumber(Context context, int columnNumber2) {
        this.columnNumber = columnNumber2;
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
        this.imageSize = metrics.widthPixels / columnNumber2;
    }

    public int getItemViewType(int position) {
        return (!showCamera() || position != 0) ? 101 : 100;
    }

    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(C1661R.layout.__picker_item_photo, parent, false));
        if (viewType == 100) {
            holder.vSelected.setVisibility(8);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PhotoGridAdapter.this.onCameraClickListener != null) {
                        PhotoGridAdapter.this.onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return holder;
    }

    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        final Photo photo;
        if (getItemViewType(position) == 101) {
            List<Photo> photos = getCurrentPhotos();
            if (showCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }
            if (AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext())) {
                this.glide.load(new File(photo.getPath())).centerCrop().dontAnimate().thumbnail(0.5f).override(this.imageSize, this.imageSize).placeholder(C1661R.C1662drawable.__picker_ic_photo_black_48dp).error(C1661R.C1662drawable.__picker_ic_broken_image_black_48dp).into(holder.ivPhoto);
            }
            boolean isChecked = isSelected(photo);
            holder.vSelected.setSelected(isChecked);
            holder.ivPhoto.setSelected(isChecked);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PhotoGridAdapter.this.onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (PhotoGridAdapter.this.previewEnable) {
                            PhotoGridAdapter.this.onPhotoClickListener.onClick(view, pos, PhotoGridAdapter.this.showCamera());
                        } else {
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;
                    if (PhotoGridAdapter.this.onItemCheckListener != null) {
                        isEnable = PhotoGridAdapter.this.onItemCheckListener.onItemCheck(pos, photo, (PhotoGridAdapter.this.isSelected(photo) ? -1 : 1) + PhotoGridAdapter.this.getSelectedPhotos().size());
                    }
                    if (isEnable) {
                        PhotoGridAdapter.this.toggleSelection(photo);
                        PhotoGridAdapter.this.notifyItemChanged(pos);
                    }
                }
            });
            return;
        }
        holder.ivPhoto.setImageResource(C1661R.C1662drawable.__picker_camera);
    }

    public int getItemCount() {
        int photosCount = this.photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }

    /* renamed from: me.iwf.photopicker.adapter.PhotoGridAdapter$PhotoViewHolder */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView ivPhoto;
        /* access modifiers changed from: private */
        public View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = (ImageView) itemView.findViewById(C1661R.C1663id.iv_photo);
            this.vSelected = itemView.findViewById(C1661R.C1663id.v_selected);
        }
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener2) {
        this.onItemCheckListener = onItemCheckListener2;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener2) {
        this.onPhotoClickListener = onPhotoClickListener2;
    }

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener2) {
        this.onCameraClickListener = onCameraClickListener2;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());
        for (String photo : this.selectedPhotos) {
            selectedPhotoPaths.add(photo);
        }
        return selectedPhotoPaths;
    }

    public void setShowCamera(boolean hasCamera2) {
        this.hasCamera = hasCamera2;
    }

    public void setPreviewEnable(boolean previewEnable2) {
        this.previewEnable = previewEnable2;
    }

    public boolean showCamera() {
        return this.hasCamera && this.currentDirectoryIndex == 0;
    }

    public void onViewRecycled(PhotoViewHolder holder) {
        Glide.clear((View) holder.ivPhoto);
        super.onViewRecycled(holder);
    }
}
