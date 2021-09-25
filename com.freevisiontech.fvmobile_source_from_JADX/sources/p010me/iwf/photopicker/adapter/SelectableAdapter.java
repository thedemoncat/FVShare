package p010me.iwf.photopicker.adapter;

import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.entity.Photo;
import p010me.iwf.photopicker.entity.PhotoDirectory;
import p010me.iwf.photopicker.event.Selectable;

/* renamed from: me.iwf.photopicker.adapter.SelectableAdapter */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Selectable {
    private static final String TAG = SelectableAdapter.class.getSimpleName();
    public int currentDirectoryIndex = 0;
    protected List<PhotoDirectory> photoDirectories = new ArrayList();
    protected List<String> selectedPhotos = new ArrayList();

    public boolean isSelected(Photo photo) {
        return getSelectedPhotos().contains(photo.getPath());
    }

    public void toggleSelection(Photo photo) {
        if (this.selectedPhotos.contains(photo.getPath())) {
            this.selectedPhotos.remove(photo.getPath());
        } else {
            this.selectedPhotos.add(photo.getPath());
        }
    }

    public void clearSelection() {
        this.selectedPhotos.clear();
    }

    public int getSelectedItemCount() {
        return this.selectedPhotos.size();
    }

    public void setCurrentDirectoryIndex(int currentDirectoryIndex2) {
        this.currentDirectoryIndex = currentDirectoryIndex2;
    }

    public List<Photo> getCurrentPhotos() {
        return this.photoDirectories.get(this.currentDirectoryIndex).getPhotos();
    }

    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }

    public List<String> getSelectedPhotos() {
        return this.selectedPhotos;
    }
}
