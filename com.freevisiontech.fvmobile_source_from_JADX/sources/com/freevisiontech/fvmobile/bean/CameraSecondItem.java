package com.freevisiontech.fvmobile.bean;

public class CameraSecondItem {
    public int ItemType;
    public int iconID;
    public boolean isItemSelect;
    public int itemID;
    public String textID;
    public int type;

    public CameraSecondItem(int itemID2, int itemType, boolean isItemSelect2) {
        this(0, itemID2, itemType, isItemSelect2);
    }

    public CameraSecondItem(int type2, int itemID2, int itemType, boolean isItemSelect2) {
        this.type = type2;
        this.itemID = itemID2;
        this.ItemType = itemType;
        this.isItemSelect = isItemSelect2;
    }

    public CameraSecondItem(int type2, String textID2, int iconID2, boolean isItemSelect2) {
        this.type = type2;
        this.textID = textID2;
        this.iconID = iconID2;
        this.isItemSelect = isItemSelect2;
    }
}
