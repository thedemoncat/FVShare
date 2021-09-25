package android.support.p003v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.p003v7.app.AlertController;
import android.support.p003v7.appcompat.C0777R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

/* renamed from: android.support.v7.app.AlertDialog */
public class AlertDialog extends AppCompatDialog implements DialogInterface {
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;
    final AlertController mAlert;

    protected AlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        this.mAlert = new AlertController(getContext(), this, getWindow());
    }

    protected AlertDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
        if (((resid >>> 24) & 255) >= 1) {
            return resid;
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(C0777R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }

    public Button getButton(int whichButton) {
        return this.mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return this.mAlert.getListView();
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        this.mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        this.mAlert.setMessage(message);
    }

    public void setView(View view) {
        this.mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        this.mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    /* access modifiers changed from: package-private */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void setButtonPanelLayoutHint(int layoutHint) {
        this.mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        this.mAlert.setButton(whichButton, text, (DialogInterface.OnClickListener) null, msg);
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        this.mAlert.setButton(whichButton, text, listener, (Message) null);
    }

    public void setIcon(int resId) {
        this.mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        this.mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        this.mAlert.setIcon(out.resourceId);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAlert.installContent();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /* renamed from: android.support.v7.app.AlertDialog$Builder */
    public static class Builder {

        /* renamed from: P */
        private final AlertController.AlertParams f1054P;
        private final int mTheme;

        public Builder(@NonNull Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            this.f1054P = new AlertController.AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, themeResId)));
            this.mTheme = themeResId;
        }

        @NonNull
        public Context getContext() {
            return this.f1054P.mContext;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.f1054P.mTitle = this.f1054P.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            this.f1054P.mTitle = title;
            return this;
        }

        public Builder setCustomTitle(@Nullable View customTitleView) {
            this.f1054P.mCustomTitleView = customTitleView;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            this.f1054P.mMessage = this.f1054P.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            this.f1054P.mMessage = message;
            return this;
        }

        public Builder setIcon(@DrawableRes int iconId) {
            this.f1054P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(@Nullable Drawable icon) {
            this.f1054P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(@AttrRes int attrId) {
            TypedValue out = new TypedValue();
            this.f1054P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.f1054P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            this.f1054P.mPositiveButtonText = this.f1054P.mContext.getText(textId);
            this.f1054P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f1054P.mPositiveButtonText = text;
            this.f1054P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            this.f1054P.mNegativeButtonText = this.f1054P.mContext.getText(textId);
            this.f1054P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f1054P.mNegativeButtonText = text;
            this.f1054P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(@StringRes int textId, DialogInterface.OnClickListener listener) {
            this.f1054P.mNeutralButtonText = this.f1054P.mContext.getText(textId);
            this.f1054P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.f1054P.mNeutralButtonText = text;
            this.f1054P.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.f1054P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.f1054P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.f1054P.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.f1054P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(@ArrayRes int itemsId, DialogInterface.OnClickListener listener) {
            this.f1054P.mItems = this.f1054P.mContext.getResources().getTextArray(itemsId);
            this.f1054P.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            this.f1054P.mItems = items;
            this.f1054P.mOnClickListener = listener;
            return this;
        }

        public Builder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            this.f1054P.mAdapter = adapter;
            this.f1054P.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            this.f1054P.mCursor = cursor;
            this.f1054P.mLabelColumn = labelColumn;
            this.f1054P.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.f1054P.mItems = this.f1054P.mContext.getResources().getTextArray(itemsId);
            this.f1054P.mOnCheckboxClickListener = listener;
            this.f1054P.mCheckedItems = checkedItems;
            this.f1054P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            this.f1054P.mItems = items;
            this.f1054P.mOnCheckboxClickListener = listener;
            this.f1054P.mCheckedItems = checkedItems;
            this.f1054P.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            this.f1054P.mCursor = cursor;
            this.f1054P.mOnCheckboxClickListener = listener;
            this.f1054P.mIsCheckedColumn = isCheckedColumn;
            this.f1054P.mLabelColumn = labelColumn;
            this.f1054P.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            this.f1054P.mItems = this.f1054P.mContext.getResources().getTextArray(itemsId);
            this.f1054P.mOnClickListener = listener;
            this.f1054P.mCheckedItem = checkedItem;
            this.f1054P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            this.f1054P.mCursor = cursor;
            this.f1054P.mOnClickListener = listener;
            this.f1054P.mCheckedItem = checkedItem;
            this.f1054P.mLabelColumn = labelColumn;
            this.f1054P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            this.f1054P.mItems = items;
            this.f1054P.mOnClickListener = listener;
            this.f1054P.mCheckedItem = checkedItem;
            this.f1054P.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            this.f1054P.mAdapter = adapter;
            this.f1054P.mOnClickListener = listener;
            this.f1054P.mCheckedItem = checkedItem;
            this.f1054P.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            this.f1054P.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(int layoutResId) {
            this.f1054P.mView = null;
            this.f1054P.mViewLayoutResId = layoutResId;
            this.f1054P.mViewSpacingSpecified = false;
            return this;
        }

        public Builder setView(View view) {
            this.f1054P.mView = view;
            this.f1054P.mViewLayoutResId = 0;
            this.f1054P.mViewSpacingSpecified = false;
            return this;
        }

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        @Deprecated
        public Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            this.f1054P.mView = view;
            this.f1054P.mViewLayoutResId = 0;
            this.f1054P.mViewSpacingSpecified = true;
            this.f1054P.mViewSpacingLeft = viewSpacingLeft;
            this.f1054P.mViewSpacingTop = viewSpacingTop;
            this.f1054P.mViewSpacingRight = viewSpacingRight;
            this.f1054P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        @Deprecated
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            this.f1054P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        public Builder setRecycleOnMeasureEnabled(boolean enabled) {
            this.f1054P.mRecycleOnMeasure = enabled;
            return this;
        }

        public AlertDialog create() {
            AlertDialog dialog = new AlertDialog(this.f1054P.mContext, this.mTheme);
            this.f1054P.apply(dialog.mAlert);
            dialog.setCancelable(this.f1054P.mCancelable);
            if (this.f1054P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(this.f1054P.mOnCancelListener);
            dialog.setOnDismissListener(this.f1054P.mOnDismissListener);
            if (this.f1054P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.f1054P.mOnKeyListener);
            }
            return dialog;
        }

        public AlertDialog show() {
            AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
