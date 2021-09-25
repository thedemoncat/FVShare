package android.support.p001v4.app;

import java.util.List;

/* renamed from: android.support.v4.app.FragmentManagerNonConfig */
public class FragmentManagerNonConfig {
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;

    FragmentManagerNonConfig(List<Fragment> fragments, List<FragmentManagerNonConfig> childNonConfigs) {
        this.mFragments = fragments;
        this.mChildNonConfigs = childNonConfigs;
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getFragments() {
        return this.mFragments;
    }

    /* access modifiers changed from: package-private */
    public List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }
}
