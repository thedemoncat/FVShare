package p010me.iwf.photopicker.entity;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.utils.FileUtils;

/* renamed from: me.iwf.photopicker.entity.PhotoDirectory */
public class PhotoDirectory {
    private String coverPath;
    private long dateAdded;

    /* renamed from: id */
    private String f1208id;
    private String name;
    private List<Photo> photos = new ArrayList();

    public boolean equals(Object o) {
        boolean hasId;
        boolean otherHasId;
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoDirectory)) {
            return false;
        }
        PhotoDirectory directory = (PhotoDirectory) o;
        if (!TextUtils.isEmpty(this.f1208id)) {
            hasId = true;
        } else {
            hasId = false;
        }
        if (!TextUtils.isEmpty(directory.f1208id)) {
            otherHasId = true;
        } else {
            otherHasId = false;
        }
        if (!hasId || !otherHasId || !TextUtils.equals(this.f1208id, directory.f1208id)) {
            return false;
        }
        return TextUtils.equals(this.name, directory.name);
    }

    public int hashCode() {
        if (!TextUtils.isEmpty(this.f1208id)) {
            int result = this.f1208id.hashCode();
            if (!TextUtils.isEmpty(this.name)) {
                return (result * 31) + this.name.hashCode();
            }
            return result;
        } else if (TextUtils.isEmpty(this.name)) {
            return 0;
        } else {
            return this.name.hashCode();
        }
    }

    public String getId() {
        return this.f1208id;
    }

    public void setId(String id) {
        this.f1208id = id;
    }

    public String getCoverPath() {
        return this.coverPath;
    }

    public void setCoverPath(String coverPath2) {
        this.coverPath = coverPath2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public long getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(long dateAdded2) {
        this.dateAdded = dateAdded2;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(List<Photo> photos2) {
        if (photos2 != null) {
            int j = 0;
            int num = photos2.size();
            for (int i = 0; i < num; i++) {
                Photo p = photos2.get(j);
                if (p == null || !FileUtils.fileIsExists(p.getPath())) {
                    photos2.remove(j);
                } else {
                    j++;
                }
            }
            this.photos = photos2;
        }
    }

    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(this.photos.size());
        for (Photo photo : this.photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    public void addPhoto(int id, String path) {
        if (FileUtils.fileIsExists(path)) {
            this.photos.add(new Photo(id, path));
        }
    }
}
