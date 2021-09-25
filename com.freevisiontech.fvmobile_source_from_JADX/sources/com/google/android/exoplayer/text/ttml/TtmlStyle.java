package com.google.android.exoplayer.text.ttml;

import android.text.Layout;
import com.google.android.exoplayer.util.Assertions;

final class TtmlStyle {
    public static final int FONT_SIZE_UNIT_EM = 2;
    public static final int FONT_SIZE_UNIT_PERCENT = 3;
    public static final int FONT_SIZE_UNIT_PIXEL = 1;
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f1204ON = 1;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_BOLD_ITALIC = 3;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_NORMAL = 0;
    public static final int UNSPECIFIED = -1;
    private int backgroundColor;
    private int bold = -1;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit = -1;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;

    /* renamed from: id */
    private String f1205id;
    private TtmlStyle inheritableStyle;
    private int italic = -1;
    private int linethrough = -1;
    private Layout.Alignment textAlign;
    private int underline = -1;

    public int getStyle() {
        int i = 0;
        if (this.bold == -1 && this.italic == -1) {
            return -1;
        }
        int i2 = this.bold != -1 ? this.bold : 0;
        if (this.italic != -1) {
            i = this.italic;
        }
        return i2 | i;
    }

    public boolean isLinethrough() {
        return this.linethrough == 1;
    }

    public TtmlStyle setLinethrough(boolean linethrough2) {
        boolean z;
        int i = 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!linethrough2) {
            i = 0;
        }
        this.linethrough = i;
        return this;
    }

    public boolean isUnderline() {
        return this.underline == 1;
    }

    public TtmlStyle setUnderline(boolean underline2) {
        boolean z;
        int i = 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!underline2) {
            i = 0;
        }
        this.underline = i;
        return this;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public TtmlStyle setFontFamily(String fontFamily2) {
        Assertions.checkState(this.inheritableStyle == null);
        this.fontFamily = fontFamily2;
        return this;
    }

    public int getFontColor() {
        if (this.hasFontColor) {
            return this.fontColor;
        }
        throw new IllegalStateException("Font color has not been defined.");
    }

    public TtmlStyle setFontColor(int fontColor2) {
        Assertions.checkState(this.inheritableStyle == null);
        this.fontColor = fontColor2;
        this.hasFontColor = true;
        return this;
    }

    public boolean hasFontColor() {
        return this.hasFontColor;
    }

    public int getBackgroundColor() {
        if (this.hasBackgroundColor) {
            return this.backgroundColor;
        }
        throw new IllegalStateException("Background color has not been defined.");
    }

    public TtmlStyle setBackgroundColor(int backgroundColor2) {
        this.backgroundColor = backgroundColor2;
        this.hasBackgroundColor = true;
        return this;
    }

    public boolean hasBackgroundColor() {
        return this.hasBackgroundColor;
    }

    public TtmlStyle setBold(boolean isBold) {
        boolean z;
        int i = 1;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (!isBold) {
            i = 0;
        }
        this.bold = i;
        return this;
    }

    public TtmlStyle setItalic(boolean isItalic) {
        boolean z;
        int i = 0;
        if (this.inheritableStyle == null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        if (isItalic) {
            i = 2;
        }
        this.italic = i;
        return this;
    }

    public TtmlStyle inherit(TtmlStyle ancestor) {
        return inherit(ancestor, false);
    }

    public TtmlStyle chain(TtmlStyle ancestor) {
        return inherit(ancestor, true);
    }

    private TtmlStyle inherit(TtmlStyle ancestor, boolean chaining) {
        if (ancestor != null) {
            if (!this.hasFontColor && ancestor.hasFontColor) {
                setFontColor(ancestor.fontColor);
            }
            if (this.bold == -1) {
                this.bold = ancestor.bold;
            }
            if (this.italic == -1) {
                this.italic = ancestor.italic;
            }
            if (this.fontFamily == null) {
                this.fontFamily = ancestor.fontFamily;
            }
            if (this.linethrough == -1) {
                this.linethrough = ancestor.linethrough;
            }
            if (this.underline == -1) {
                this.underline = ancestor.underline;
            }
            if (this.textAlign == null) {
                this.textAlign = ancestor.textAlign;
            }
            if (this.fontSizeUnit == -1) {
                this.fontSizeUnit = ancestor.fontSizeUnit;
                this.fontSize = ancestor.fontSize;
            }
            if (chaining && !this.hasBackgroundColor && ancestor.hasBackgroundColor) {
                setBackgroundColor(ancestor.backgroundColor);
            }
        }
        return this;
    }

    public TtmlStyle setId(String id) {
        this.f1205id = id;
        return this;
    }

    public String getId() {
        return this.f1205id;
    }

    public Layout.Alignment getTextAlign() {
        return this.textAlign;
    }

    public TtmlStyle setTextAlign(Layout.Alignment textAlign2) {
        this.textAlign = textAlign2;
        return this;
    }

    public TtmlStyle setFontSize(float fontSize2) {
        this.fontSize = fontSize2;
        return this;
    }

    public TtmlStyle setFontSizeUnit(int fontSizeUnit2) {
        this.fontSizeUnit = fontSizeUnit2;
        return this;
    }

    public int getFontSizeUnit() {
        return this.fontSizeUnit;
    }

    public float getFontSize() {
        return this.fontSize;
    }
}
