package p010me.iwf.photopicker.entity;

/* renamed from: me.iwf.photopicker.entity.Photo */
public class Photo {

    /* renamed from: id */
    private int f1207id;
    private String path;

    public Photo(int id, String path2) {
        this.f1207id = id;
        this.path = path2;
    }

    public Photo() {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        if (this.f1207id != ((Photo) o).f1207id) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.f1207id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public int getId() {
        return this.f1207id;
    }

    public void setId(int id) {
        this.f1207id = id;
    }
}
