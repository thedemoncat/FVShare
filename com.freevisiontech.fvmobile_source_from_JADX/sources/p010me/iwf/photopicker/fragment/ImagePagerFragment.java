package p010me.iwf.photopicker.fragment;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.view.PagerAdapter;
import android.support.p001v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.bumptech.glide.Glide;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.adapter.PhotoPagerAdapter;
import p010me.iwf.photopicker.utils.PpEvent;
import p010me.iwf.photopicker.utils.PpEventConstant;

/* renamed from: me.iwf.photopicker.fragment.ImagePagerFragment */
public class ImagePagerFragment extends Fragment {
    public static final long ANIM_DURATION = 200;
    public static final String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";
    public static final String ARG_HAS_ANIM = "HAS_ANIM";
    public static final String ARG_PATH = "PATHS";
    public static final String ARG_THUMBNAIL_HEIGHT = "THUMBNAIL_HEIGHT";
    public static final String ARG_THUMBNAIL_LEFT = "THUMBNAIL_LEFT";
    public static final String ARG_THUMBNAIL_TOP = "THUMBNAIL_TOP";
    public static final String ARG_THUMBNAIL_WIDTH = "THUMBNAIL_WIDTH";
    private final ColorMatrix colorizerMatrix = new ColorMatrix();
    /* access modifiers changed from: private */
    public int currentItem = 0;
    /* access modifiers changed from: private */
    public boolean hasAnim = false;
    private PhotoPagerAdapter mPagerAdapter;
    /* access modifiers changed from: private */
    public ViewPager mViewPager;
    private ArrayList<String> paths;
    private int thumbnailHeight = 0;
    /* access modifiers changed from: private */
    public int thumbnailLeft = 0;
    /* access modifiers changed from: private */
    public int thumbnailTop = 0;
    private int thumbnailWidth = 0;

    public static ImagePagerFragment newInstance(List<String> paths2, int currentItem2) {
        ImagePagerFragment f = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PATH, (String[]) paths2.toArray(new String[paths2.size()]));
        args.putInt(ARG_CURRENT_ITEM, currentItem2);
        args.putBoolean(ARG_HAS_ANIM, false);
        f.setArguments(args);
        return f;
    }

    public static ImagePagerFragment newInstance(List<String> paths2, int currentItem2, int[] screenLocation, int thumbnailWidth2, int thumbnailHeight2) {
        ImagePagerFragment f = newInstance(paths2, currentItem2);
        f.getArguments().putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        f.getArguments().putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        f.getArguments().putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth2);
        f.getArguments().putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight2);
        f.getArguments().putBoolean(ARG_HAS_ANIM, true);
        return f;
    }

    public void setPhotos(List<String> paths2, int currentItem2) {
        this.paths.clear();
        this.paths.addAll(paths2);
        this.currentItem = currentItem2;
        this.mViewPager.setCurrentItem(currentItem2);
        this.mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.paths = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String[] pathArr = bundle.getStringArray(ARG_PATH);
            this.paths.clear();
            if (pathArr != null) {
                this.paths = new ArrayList<>(Arrays.asList(pathArr));
            }
            this.hasAnim = bundle.getBoolean(ARG_HAS_ANIM);
            this.currentItem = bundle.getInt(ARG_CURRENT_ITEM);
            this.thumbnailTop = bundle.getInt(ARG_THUMBNAIL_TOP);
            this.thumbnailLeft = bundle.getInt(ARG_THUMBNAIL_LEFT);
            this.thumbnailWidth = bundle.getInt(ARG_THUMBNAIL_WIDTH);
            this.thumbnailHeight = bundle.getInt(ARG_THUMBNAIL_HEIGHT);
        }
        this.mPagerAdapter = new PhotoPagerAdapter(Glide.with((Fragment) this), this.paths, getActivity());
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(PpEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(PpEvent event) {
        switch (event.getCode()) {
            case PpEventConstant.FM210_VIDEO_START_TO_PLAY /*2184*/:
                this.mPagerAdapter.playByPtz();
                return;
            default:
                return;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C1661R.layout.__picker_picker_fragment_image_pager, container, false);
        this.mViewPager = (ViewPager) rootView.findViewById(C1661R.C1663id.vp_photos);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setCurrentItem(this.currentItem);
        this.mViewPager.setOffscreenPageLimit(5);
        if (savedInstanceState == null && this.hasAnim) {
            this.mViewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    ImagePagerFragment.this.mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                    int[] screenLocation = new int[2];
                    ImagePagerFragment.this.mViewPager.getLocationOnScreen(screenLocation);
                    int unused = ImagePagerFragment.this.thumbnailLeft = ImagePagerFragment.this.thumbnailLeft - screenLocation[0];
                    int unused2 = ImagePagerFragment.this.thumbnailTop = ImagePagerFragment.this.thumbnailTop - screenLocation[1];
                    ImagePagerFragment.this.runEnterAnimation();
                    return true;
                }
            });
        }
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EventBus.getDefault().post(new PpEvent(PpEventConstant.STATUS_PTZ_ZOOM_NORMAL));
            }

            public void onPageSelected(int position) {
                boolean unused = ImagePagerFragment.this.hasAnim = ImagePagerFragment.this.currentItem == position;
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        return rootView;
    }

    /* access modifiers changed from: private */
    public void runEnterAnimation() {
        ViewHelper.setPivotX(this.mViewPager, 0.0f);
        ViewHelper.setPivotY(this.mViewPager, 0.0f);
        ViewHelper.setScaleX(this.mViewPager, ((float) this.thumbnailWidth) / ((float) this.mViewPager.getWidth()));
        ViewHelper.setScaleY(this.mViewPager, ((float) this.thumbnailHeight) / ((float) this.mViewPager.getHeight()));
        ViewHelper.setTranslationX(this.mViewPager, (float) this.thumbnailLeft);
        ViewHelper.setTranslationY(this.mViewPager, (float) this.thumbnailTop);
        ViewPropertyAnimator.animate(this.mViewPager).setDuration(200).scaleX(1.0f).scaleY(1.0f).translationX(0.0f).translationY(0.0f).setInterpolator(new DecelerateInterpolator());
        ObjectAnimator bgAnim = ObjectAnimator.ofInt((Object) this.mViewPager.getBackground(), "alpha", 0, 255);
        bgAnim.setDuration(200);
        bgAnim.start();
        ObjectAnimator colorizer = ObjectAnimator.ofFloat((Object) this, "saturation", 0.0f, 1.0f);
        colorizer.setDuration(200);
        colorizer.start();
    }

    public void runExitAnimation(final Runnable endAction) {
        if (!getArguments().getBoolean(ARG_HAS_ANIM, false) || !this.hasAnim) {
            endAction.run();
            return;
        }
        ViewPropertyAnimator.animate(this.mViewPager).setDuration(200).setInterpolator(new AccelerateInterpolator()).scaleX(((float) this.thumbnailWidth) / ((float) this.mViewPager.getWidth())).scaleY(((float) this.thumbnailHeight) / ((float) this.mViewPager.getHeight())).translationX((float) this.thumbnailLeft).translationY((float) this.thumbnailTop).setListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                endAction.run();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        ObjectAnimator bgAnim = ObjectAnimator.ofInt((Object) this.mViewPager.getBackground(), "alpha", 0);
        bgAnim.setDuration(200);
        bgAnim.start();
        ObjectAnimator colorizer = ObjectAnimator.ofFloat((Object) this, "saturation", 1.0f, 0.0f);
        colorizer.setDuration(200);
        colorizer.start();
    }

    public void setSaturation(float value) {
        this.colorizerMatrix.setSaturation(value);
        this.mViewPager.getBackground().setColorFilter(new ColorMatrixColorFilter(this.colorizerMatrix));
    }

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    public ArrayList<String> getPaths() {
        return this.paths;
    }

    public int getCurrentItem() {
        return this.mViewPager.getCurrentItem();
    }

    public void onDestroy() {
        super.onDestroy();
        this.paths.clear();
        this.paths = null;
        if (this.mViewPager != null) {
            this.mViewPager.setAdapter((PagerAdapter) null);
        }
        EventBus.getDefault().unregister(this);
    }
}
