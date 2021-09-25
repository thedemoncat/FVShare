package p010me.iwf.photopicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.entity.Photo;
import p010me.iwf.photopicker.event.OnItemCheckListener;
import p010me.iwf.photopicker.fragment.ImagePagerFragment;
import p010me.iwf.photopicker.fragment.PhotoPickerFragment;

/* renamed from: me.iwf.photopicker.PhotoPickerActivity */
public class PhotoPickerActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = (!PhotoPickerActivity.class.desiredAssertionStatus());
    private int columnNumber = 3;
    private ImagePagerFragment imagePagerFragment;
    /* access modifiers changed from: private */
    public int maxCount = 9;
    /* access modifiers changed from: private */
    public MenuItem menuDoneItem;
    private boolean menuIsInflated = false;
    private ArrayList<String> originalPhotos = null;
    /* access modifiers changed from: private */
    public PhotoPickerFragment pickerFragment;
    private boolean showGif = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean showCamera = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, true);
        boolean showGif2 = getIntent().getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
        boolean previewEnabled = getIntent().getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);
        setShowGif(showGif2);
        setContentView(C1661R.layout.__picker_activity_photo_picker);
        setSupportActionBar((Toolbar) findViewById(C1661R.C1663id.toolbar));
        setTitle(C1661R.string.__picker_title);
        ActionBar actionBar = getSupportActionBar();
        if ($assertionsDisabled || actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                actionBar.setElevation(25.0f);
            }
            this.maxCount = getIntent().getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, 9);
            this.columnNumber = getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, 3);
            this.originalPhotos = getIntent().getStringArrayListExtra(PhotoPicker.EXTRA_ORIGINAL_PHOTOS);
            this.pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag(Progress.TAG);
            if (this.pickerFragment == null) {
                this.pickerFragment = PhotoPickerFragment.newInstance(showCamera, showGif2, previewEnabled, this.columnNumber, this.maxCount, this.originalPhotos);
                getSupportFragmentManager().beginTransaction().replace(C1661R.C1663id.container, this.pickerFragment, Progress.TAG).commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            this.pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
                public boolean onItemCheck(int position, Photo photo, int selectedItemCount) {
                    PhotoPickerActivity.this.menuDoneItem.setEnabled(selectedItemCount > 0);
                    if (PhotoPickerActivity.this.maxCount <= 1) {
                        List<String> photos = PhotoPickerActivity.this.pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                        if (photos.contains(photo.getPath())) {
                            return true;
                        }
                        photos.clear();
                        PhotoPickerActivity.this.pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                        return true;
                    } else if (selectedItemCount > PhotoPickerActivity.this.maxCount) {
                        Toast.makeText(PhotoPickerActivity.this.getActivity(), PhotoPickerActivity.this.getString(C1661R.string.__picker_over_max_count_tips, new Object[]{Integer.valueOf(PhotoPickerActivity.this.maxCount)}), 1).show();
                        return false;
                    } else {
                        PhotoPickerActivity.this.menuDoneItem.setTitle(PhotoPickerActivity.this.getString(C1661R.string.__picker_done_with_count, new Object[]{Integer.valueOf(selectedItemCount), Integer.valueOf(PhotoPickerActivity.this.maxCount)}));
                        return true;
                    }
                }
            });
            return;
        }
        throw new AssertionError();
    }

    public void onBackPressed() {
        if (this.imagePagerFragment == null || !this.imagePagerFragment.isVisible()) {
            super.onBackPressed();
        } else {
            this.imagePagerFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (PhotoPickerActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        PhotoPickerActivity.this.getSupportFragmentManager().popBackStack();
                    }
                }
            });
        }
    }

    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment2) {
        this.imagePagerFragment = imagePagerFragment2;
        getSupportFragmentManager().beginTransaction().replace(C1661R.C1663id.container, this.imagePagerFragment).addToBackStack((String) null).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.menuIsInflated) {
            return false;
        }
        getMenuInflater().inflate(C1661R.C1664menu.__picker_menu_picker, menu);
        this.menuDoneItem = menu.findItem(C1661R.C1663id.done);
        if (this.originalPhotos == null || this.originalPhotos.size() <= 0) {
            this.menuDoneItem.setEnabled(false);
        } else {
            this.menuDoneItem.setEnabled(true);
            this.menuDoneItem.setTitle(getString(C1661R.string.__picker_done_with_count, new Object[]{Integer.valueOf(this.originalPhotos.size()), Integer.valueOf(this.maxCount)}));
        }
        this.menuIsInflated = true;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            super.onBackPressed();
            return true;
        } else if (item.getItemId() != C1661R.C1663id.done) {
            return super.onOptionsItemSelected(item);
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths());
            setResult(-1, intent);
            finish();
            return true;
        }
    }

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return this.showGif;
    }

    public void setShowGif(boolean showGif2) {
        this.showGif = showGif2;
    }
}
