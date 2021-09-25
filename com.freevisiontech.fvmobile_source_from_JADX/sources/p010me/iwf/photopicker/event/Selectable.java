package p010me.iwf.photopicker.event;

import p010me.iwf.photopicker.entity.Photo;

/* renamed from: me.iwf.photopicker.event.Selectable */
public interface Selectable {
    void clearSelection();

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

    void toggleSelection(Photo photo);
}
