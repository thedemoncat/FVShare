package com.freevisiontech.fvmobile.model.adrecord;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.freevisiontech.fvmobile.utils.BleAdRecordUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AdRecordStore implements Parcelable {
    public static final Parcelable.Creator<AdRecordStore> CREATOR = new Parcelable.Creator<AdRecordStore>() {
        public AdRecordStore createFromParcel(Parcel in) {
            return new AdRecordStore(in);
        }

        public AdRecordStore[] newArray(int size) {
            return new AdRecordStore[size];
        }
    };
    private static final String LOCAL_NAME_COMPLETE = "local_name_complete";
    private static final String LOCAL_NAME_SHORT = "local_name_short";
    private static final String RECORDS_ARRAY = "records_array";
    private final SparseArray<AdRecord> mAdRecords;
    private final String mLocalNameComplete;
    private final String mLocalNameShort;

    public AdRecordStore(Parcel in) {
        Bundle b = in.readBundle(getClass().getClassLoader());
        this.mAdRecords = b.getSparseParcelableArray(RECORDS_ARRAY);
        this.mLocalNameComplete = b.getString(LOCAL_NAME_COMPLETE);
        this.mLocalNameShort = b.getString(LOCAL_NAME_SHORT);
    }

    public AdRecordStore(SparseArray<AdRecord> adRecords) {
        this.mAdRecords = adRecords;
        this.mLocalNameComplete = BleAdRecordUtil.getRecordDataAsString(this.mAdRecords.get(9));
        this.mLocalNameShort = BleAdRecordUtil.getRecordDataAsString(this.mAdRecords.get(8));
    }

    public int describeContents() {
        return 0;
    }

    public String getLocalNameComplete() {
        return this.mLocalNameComplete;
    }

    public String getLocalNameShort() {
        return this.mLocalNameShort;
    }

    public AdRecord getRecord(int record) {
        return this.mAdRecords.get(record);
    }

    public String getRecordDataAsString(int record) {
        return BleAdRecordUtil.getRecordDataAsString(this.mAdRecords.get(record));
    }

    public Collection<AdRecord> getRecordsAsCollection() {
        return Collections.unmodifiableCollection(asList(this.mAdRecords));
    }

    public boolean isRecordPresent(int record) {
        return this.mAdRecords.indexOfKey(record) >= 0;
    }

    public String toString() {
        return "AdRecordStore [mLocalNameComplete=" + this.mLocalNameComplete + ", mLocalNameShort=" + this.mLocalNameShort + "]";
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        Bundle b = new Bundle();
        b.putString(LOCAL_NAME_COMPLETE, this.mLocalNameComplete);
        b.putString(LOCAL_NAME_SHORT, this.mLocalNameShort);
        b.putSparseParcelableArray(RECORDS_ARRAY, this.mAdRecords);
        parcel.writeBundle(b);
    }

    public static <C> Collection<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) {
            return null;
        }
        Collection<C> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }
}
