package p010me.iwf.photopicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.p001v4.view.ViewPager;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.List;
import p010me.iwf.photopicker.fragment.ImagePagerFragment;

/* renamed from: me.iwf.photopicker.PhotoPagerActivity */
public class PhotoPagerActivity extends AppCompatActivity {
    private ActionBar actionBar;
    /* access modifiers changed from: private */
    public ImagePagerFragment pagerFragment;
    private boolean showDelete;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1661R.layout.__picker_activity_photo_pager);
        int currentItem = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);
        List<String> paths = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
        this.showDelete = getIntent().getBooleanExtra(PhotoPreview.EXTRA_SHOW_DELETE, true);
        if (this.pagerFragment == null) {
            this.pagerFragment = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(C1661R.C1663id.photoPagerFragment);
        }
        this.pagerFragment.setPhotos(paths, currentItem);
        setSupportActionBar((Toolbar) findViewById(C1661R.C1663id.toolbar));
        this.actionBar = getSupportActionBar();
        if (this.actionBar != null) {
            this.actionBar.setDisplayHomeAsUpEnabled(true);
            updateActionBarTitle();
            if (Build.VERSION.SDK_INT >= 21) {
                this.actionBar.setElevation(25.0f);
            }
        }
        this.pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PhotoPagerActivity.this.updateActionBarTitle();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (!this.showDelete) {
            return true;
        }
        getMenuInflater().inflate(C1661R.C1664menu.__picker_menu_preview, menu);
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.pagerFragment.getPaths());
        setResult(-1, intent);
        finish();
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
            return true;
        } else if (item.getItemId() != C1661R.C1663id.delete) {
            return super.onOptionsItemSelected(item);
        } else {
            final int index = this.pagerFragment.getCurrentItem();
            final String deletedPath = this.pagerFragment.getPaths().get(index);
            Snackbar snackbar = Snackbar.make(this.pagerFragment.getView(), C1661R.string.__picker_deleted_a_photo, 0);
            if (this.pagerFragment.getPaths().size() <= 1) {
                new AlertDialog.Builder(this).setTitle(C1661R.string.__picker_confirm_to_delete).setPositiveButton(C1661R.string.__picker_yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        PhotoPagerActivity.this.pagerFragment.getPaths().remove(index);
                        PhotoPagerActivity.this.pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                        PhotoPagerActivity.this.onBackPressed();
                    }
                }).setNegativeButton(C1661R.string.__picker_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            } else {
                snackbar.show();
                this.pagerFragment.getPaths().remove(index);
                this.pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
            }
            snackbar.setAction(C1661R.string.__picker_undo, (View.OnClickListener) new View.OnClickListener() {
                public void onClick(View view) {
                    if (PhotoPagerActivity.this.pagerFragment.getPaths().size() > 0) {
                        PhotoPagerActivity.this.pagerFragment.getPaths().add(index, deletedPath);
                    } else {
                        PhotoPagerActivity.this.pagerFragment.getPaths().add(deletedPath);
                    }
                    PhotoPagerActivity.this.pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                    PhotoPagerActivity.this.pagerFragment.getViewPager().setCurrentItem(index, true);
                }
            });
            return true;
        }
    }

    public void updateActionBarTitle() {
        if (this.actionBar != null) {
            this.actionBar.setTitle((CharSequence) getString(C1661R.string.__picker_image_index, new Object[]{Integer.valueOf(this.pagerFragment.getViewPager().getCurrentItem() + 1), Integer.valueOf(this.pagerFragment.getPaths().size())}));
        }
    }
}
