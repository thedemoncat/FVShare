package p010me.iwf.photopicker.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment;
import android.support.p003v7.widget.DefaultItemAnimator;
import android.support.p003v7.widget.ListPopupWindow;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p010me.iwf.photopicker.C1661R;
import p010me.iwf.photopicker.PhotoPicker;
import p010me.iwf.photopicker.PhotoPickerActivity;
import p010me.iwf.photopicker.adapter.PhotoGridAdapter;
import p010me.iwf.photopicker.adapter.PopupDirectoryListAdapter;
import p010me.iwf.photopicker.entity.Photo;
import p010me.iwf.photopicker.entity.PhotoDirectory;
import p010me.iwf.photopicker.event.OnPhotoClickListener;
import p010me.iwf.photopicker.utils.AndroidLifecycleUtils;
import p010me.iwf.photopicker.utils.ImageCaptureManager;
import p010me.iwf.photopicker.utils.MediaStoreHelper;
import p010me.iwf.photopicker.utils.PermissionsUtils;

/* renamed from: me.iwf.photopicker.fragment.PhotoPickerFragment */
public class PhotoPickerFragment extends Fragment {
    public static int COUNT_MAX = 4;
    private static final String EXTRA_CAMERA = "camera";
    private static final String EXTRA_COLUMN = "column";
    private static final String EXTRA_COUNT = "count";
    private static final String EXTRA_GIF = "gif";
    private static final String EXTRA_ORIGIN = "origin";
    /* access modifiers changed from: private */
    public int SCROLL_THRESHOLD = 30;
    private ImageCaptureManager captureManager;
    int column;
    /* access modifiers changed from: private */
    public List<PhotoDirectory> directories;
    /* access modifiers changed from: private */
    public PopupDirectoryListAdapter listAdapter;
    /* access modifiers changed from: private */
    public ListPopupWindow listPopupWindow;
    /* access modifiers changed from: private */
    public RequestManager mGlideRequestManager;
    private ArrayList<String> originalPhotos;
    /* access modifiers changed from: private */
    public PhotoGridAdapter photoGridAdapter;

    public static PhotoPickerFragment newInstance(boolean showCamera, boolean showGif, boolean previewEnable, int column2, int maxCount, ArrayList<String> originalPhotos2) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_CAMERA, showCamera);
        args.putBoolean(EXTRA_GIF, showGif);
        args.putBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, previewEnable);
        args.putInt("column", column2);
        args.putInt("count", maxCount);
        args.putStringArrayList("origin", originalPhotos2);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        this.mGlideRequestManager = Glide.with((Fragment) this);
        this.directories = new ArrayList();
        this.originalPhotos = getArguments().getStringArrayList("origin");
        this.column = getArguments().getInt("column", 3);
        boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, true);
        boolean previewEnable = getArguments().getBoolean(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);
        this.photoGridAdapter = new PhotoGridAdapter(getActivity(), this.mGlideRequestManager, this.directories, this.originalPhotos, this.column);
        this.photoGridAdapter.setShowCamera(showCamera);
        this.photoGridAdapter.setPreviewEnable(previewEnable);
        this.listAdapter = new PopupDirectoryListAdapter(this.mGlideRequestManager, this.directories);
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, getArguments().getBoolean(EXTRA_GIF));
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs, new MediaStoreHelper.PhotosResultCallback() {
            public void onResultCallback(List<PhotoDirectory> dirs) {
                PhotoPickerFragment.this.directories.clear();
                PhotoPickerFragment.this.directories.addAll(dirs);
                PhotoPickerFragment.this.photoGridAdapter.notifyDataSetChanged();
                PhotoPickerFragment.this.listAdapter.notifyDataSetChanged();
                PhotoPickerFragment.this.adjustHeight();
            }
        });
        this.captureManager = new ImageCaptureManager(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(C1661R.layout.__picker_fragment_photo_picker, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(C1661R.C1663id.rv_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(this.column, 1);
        layoutManager.setGapStrategy(2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this.photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final Button btSwitchDirectory = (Button) rootView.findViewById(C1661R.C1663id.button);
        this.listPopupWindow = new ListPopupWindow(getActivity());
        this.listPopupWindow.setWidth(-1);
        this.listPopupWindow.setAnchorView(btSwitchDirectory);
        this.listPopupWindow.setAdapter(this.listAdapter);
        this.listPopupWindow.setModal(true);
        this.listPopupWindow.setDropDownGravity(80);
        this.listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PhotoPickerFragment.this.listPopupWindow.dismiss();
                btSwitchDirectory.setText(((PhotoDirectory) PhotoPickerFragment.this.directories.get(position)).getName());
                PhotoPickerFragment.this.photoGridAdapter.setCurrentDirectoryIndex(position);
                PhotoPickerFragment.this.photoGridAdapter.notifyDataSetChanged();
            }
        });
        this.photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            public void onClick(View v, int position, boolean showCamera) {
                int index;
                if (showCamera) {
                    index = position - 1;
                } else {
                    index = position;
                }
                List<String> photos = PhotoPickerFragment.this.photoGridAdapter.getCurrentPhotoPaths();
                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                ((PhotoPickerActivity) PhotoPickerFragment.this.getActivity()).addImagePagerFragment(ImagePagerFragment.newInstance(photos, index, screenLocation, v.getWidth(), v.getHeight()));
            }
        });
        this.photoGridAdapter.setOnCameraClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (PermissionsUtils.checkCameraPermission(PhotoPickerFragment.this) && PermissionsUtils.checkWriteStoragePermission(PhotoPickerFragment.this)) {
                    PhotoPickerFragment.this.openCamera();
                }
            }
        });
        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PhotoPickerFragment.this.listPopupWindow.isShowing()) {
                    PhotoPickerFragment.this.listPopupWindow.dismiss();
                } else if (!PhotoPickerFragment.this.getActivity().isFinishing()) {
                    PhotoPickerFragment.this.adjustHeight();
                    PhotoPickerFragment.this.listPopupWindow.show();
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > PhotoPickerFragment.this.SCROLL_THRESHOLD) {
                    PhotoPickerFragment.this.mGlideRequestManager.pauseRequests();
                } else {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 0) {
                    PhotoPickerFragment.this.resumeRequestsIfNotDestroyed();
                }
            }
        });
        return rootView;
    }

    /* access modifiers changed from: private */
    public void openCamera() {
        try {
            startActivityForResult(this.captureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            if (this.captureManager == null) {
                this.captureManager = new ImageCaptureManager(getActivity());
            }
            this.captureManager.galleryAddPic();
            if (this.directories.size() > 0) {
                String path = this.captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = this.directories.get(0);
                directory.getPhotos().add(0, new Photo(path.hashCode(), path));
                directory.setCoverPath(path);
                this.photoGridAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == 0) {
            switch (requestCode) {
                case 1:
                case 3:
                    if (PermissionsUtils.checkWriteStoragePermission(this) && PermissionsUtils.checkCameraPermission(this)) {
                        openCamera();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public PhotoGridAdapter getPhotoGridAdapter() {
        return this.photoGridAdapter;
    }

    public void onSaveInstanceState(Bundle outState) {
        this.captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        this.captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        return this.photoGridAdapter.getSelectedPhotoPaths();
    }

    public void adjustHeight() {
        if (this.listAdapter != null) {
            int count = this.listAdapter.getCount();
            if (count >= COUNT_MAX) {
                count = COUNT_MAX;
            }
            if (this.listPopupWindow != null) {
                this.listPopupWindow.setHeight(getResources().getDimensionPixelOffset(C1661R.dimen.__picker_item_directory_height) * count);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.directories != null) {
            for (PhotoDirectory directory : this.directories) {
                directory.getPhotoPaths().clear();
                directory.getPhotos().clear();
                directory.setPhotos((List<Photo>) null);
            }
            this.directories.clear();
            this.directories = null;
        }
    }

    /* access modifiers changed from: private */
    public void resumeRequestsIfNotDestroyed() {
        if (AndroidLifecycleUtils.canLoadImage((Fragment) this)) {
            this.mGlideRequestManager.resumeRequests();
        }
    }
}
