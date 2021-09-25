package p002cn.jzvd;

/* renamed from: cn.jzvd.JZUserAction */
public interface JZUserAction {
    public static final int ON_AUTO_COMPLETE = 6;
    public static final int ON_CLICK_PAUSE = 3;
    public static final int ON_CLICK_RESUME = 4;
    public static final int ON_CLICK_START_AUTO_COMPLETE = 2;
    public static final int ON_CLICK_START_ERROR = 1;
    public static final int ON_CLICK_START_ICON = 0;
    public static final int ON_ENTER_FULLSCREEN = 7;
    public static final int ON_ENTER_TINYSCREEN = 9;
    public static final int ON_QUIT_FULLSCREEN = 8;
    public static final int ON_QUIT_TINYSCREEN = 10;
    public static final int ON_SEEK_POSITION = 5;
    public static final int ON_TOUCH_SCREEN_SEEK_POSITION = 12;
    public static final int ON_TOUCH_SCREEN_SEEK_VOLUME = 11;

    void onEvent(int i, Object obj, int i2, Object... objArr);
}
