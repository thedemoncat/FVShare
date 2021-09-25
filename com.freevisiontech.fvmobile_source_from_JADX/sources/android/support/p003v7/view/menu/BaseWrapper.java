package android.support.p003v7.view.menu;

/* renamed from: android.support.v7.view.menu.BaseWrapper */
class BaseWrapper<T> {
    final T mWrappedObject;

    BaseWrapper(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Wrapped Object can not be null.");
        }
        this.mWrappedObject = object;
    }

    public T getWrappedObject() {
        return this.mWrappedObject;
    }
}
