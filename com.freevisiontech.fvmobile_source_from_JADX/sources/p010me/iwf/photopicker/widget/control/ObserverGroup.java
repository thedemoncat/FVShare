package p010me.iwf.photopicker.widget.control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* renamed from: me.iwf.photopicker.widget.control.ObserverGroup */
public final class ObserverGroup {
    private static Map<ObserverGroupType, Integer> mGroupDefMaxCount = new HashMap();
    private static Map<ObserverGroupType, Set<ObserverGroup>> mGroupMap = new HashMap();
    private static ObserverGroup mMainGroup;
    private ObserverGroupType mGroupType;

    private ObserverGroup(ObserverGroupType type) {
        this.mGroupType = type;
    }

    private ObserverGroupType getType() {
        return this.mGroupType;
    }

    static void setGroupDefMaxCount(ObserverGroupType type, int count) {
        mGroupDefMaxCount.put(type, Integer.valueOf(count));
    }

    private static void addGroup(ObserverGroup group) {
        int maxCount = 2;
        if (mGroupDefMaxCount.get(group.getType()) != null) {
            maxCount = mGroupDefMaxCount.get(group.getType()).intValue();
        }
        Set<ObserverGroup> observerGroups = mGroupMap.get(group.getType());
        if (observerGroups == null) {
            observerGroups = new HashSet<>(2);
            mGroupMap.put(group.getType(), observerGroups);
        }
        if (observerGroups.size() < maxCount) {
            observerGroups.add(group);
            return;
        }
        throw new IllegalArgumentException("repeat register, group = " + group.getType());
    }

    static void removeGroup(ObserverGroup group) {
        Set<ObserverGroup> observerGroups = mGroupMap.get(group.getType());
        if (observerGroups != null) {
            observerGroups.remove(group);
        }
    }

    public static ObserverGroup createMainGroup() {
        mMainGroup = new ObserverGroup(ObserverGroupType.MAIN_ACTIVITY);
        addGroup(mMainGroup);
        return mMainGroup;
    }

    public static ObserverGroup getMainGroup() {
        return mMainGroup;
    }
}
