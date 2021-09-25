package p010me.iwf.photopicker.widget.control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* renamed from: me.iwf.photopicker.widget.control.DataChangeNotification */
public final class DataChangeNotification {
    private static DataChangeNotification mNotification = new DataChangeNotification();
    private Map<IssueKey, Set<DataChangeObserver>> mIssueDataChangeObserverMap = new HashMap();
    private Map<String, Integer> mObserverDefMaxCount = new HashMap();
    private Map<String, Set<OnDataChangeObserver>> mObservers = new HashMap();

    /* renamed from: me.iwf.photopicker.widget.control.DataChangeNotification$DataChangeObserver */
    private final class DataChangeObserver {
        /* access modifiers changed from: private */
        public ObserverGroup mGroup;
        /* access modifiers changed from: private */
        public OnDataChangeObserver mObserver;

        private DataChangeObserver(OnDataChangeObserver observer, ObserverGroup group) {
            this.mObserver = observer;
            this.mGroup = group;
        }
    }

    public static DataChangeNotification getInstance() {
        return mNotification;
    }

    private DataChangeNotification() {
    }

    public void addObserver(IssueKey issue, OnDataChangeObserver observer) {
        addObserver(issue, observer, (ObserverGroup) null);
    }

    private void checkObserver(OnDataChangeObserver observer) {
        String type = observer.getClass().getName();
        int maxCount = 2;
        if (this.mObserverDefMaxCount.get(type) != null) {
            maxCount = this.mObserverDefMaxCount.get(type).intValue();
        }
        Set<OnDataChangeObserver> observers = this.mObservers.get(type);
        if (observers == null) {
            observers = new HashSet<>();
            this.mObservers.put(type, observers);
        }
        if (observers.size() > 1) {
            removeObserver(observer);
        }
        observers.add(observer);
        if (observers.size() > maxCount) {
            throw new IllegalArgumentException("repeat register, observer = " + type);
        }
    }

    public void addObserver(IssueKey issue, OnDataChangeObserver observer, ObserverGroup group) {
        Set<DataChangeObserver> dataChangeObserverSet = this.mIssueDataChangeObserverMap.get(issue);
        if (dataChangeObserverSet == null) {
            dataChangeObserverSet = new HashSet<>();
            this.mIssueDataChangeObserverMap.put(issue, dataChangeObserverSet);
        }
        boolean bFound = false;
        Iterator<DataChangeObserver> it = dataChangeObserverSet.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().mObserver == observer) {
                    bFound = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!bFound) {
            dataChangeObserverSet.add(new DataChangeObserver(observer, group));
        }
    }

    public void removeObserver(OnDataChangeObserver observer) {
        for (Set<DataChangeObserver> dataChangeObserverSet : this.mIssueDataChangeObserverMap.values()) {
            Iterator<DataChangeObserver> iterator = dataChangeObserverSet.iterator();
            while (iterator.hasNext()) {
                if (observer == iterator.next().mObserver) {
                    iterator.remove();
                }
            }
        }
        Set<OnDataChangeObserver> observers = this.mObservers.get(observer.getClass().getName());
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void removeObserver(ObserverGroup group) {
        for (Set<DataChangeObserver> dataChangeObserverSet : this.mIssueDataChangeObserverMap.values()) {
            Iterator<DataChangeObserver> iterator = dataChangeObserverSet.iterator();
            while (iterator.hasNext()) {
                if (group == iterator.next().mGroup) {
                    iterator.remove();
                }
            }
        }
        ObserverGroup.removeGroup(group);
    }

    public void notifyDataChanged(IssueKey issue) {
        notifyDataChanged(issue, (Object) null);
    }

    public void notifyDataChanged(IssueKey issue, Object o) {
        Set<DataChangeObserver> dataChangeObserverSet = this.mIssueDataChangeObserverMap.get(issue);
        if (dataChangeObserverSet != null) {
            for (DataChangeObserver dataChangeObserver : dataChangeObserverSet) {
                dataChangeObserver.mObserver.onDataChanged(issue, o);
            }
        }
    }
}
