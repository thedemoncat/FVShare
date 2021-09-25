package android.support.p001v4.view.accessibility;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

@RequiresApi(19)
/* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat */
class AccessibilityNodeProviderCompatKitKat {

    /* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat$AccessibilityNodeInfoBridge */
    interface AccessibilityNodeInfoBridge {
        Object createAccessibilityNodeInfo(int i);

        List<Object> findAccessibilityNodeInfosByText(String str, int i);

        Object findFocus(int i);

        boolean performAction(int i, int i2, Bundle bundle);
    }

    AccessibilityNodeProviderCompatKitKat() {
    }

    public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeInfoBridge bridge) {
        return new AccessibilityNodeProvider() {
            public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
                return (AccessibilityNodeInfo) bridge.createAccessibilityNodeInfo(virtualViewId);
            }

            public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
                return bridge.findAccessibilityNodeInfosByText(text, virtualViewId);
            }

            public boolean performAction(int virtualViewId, int action, Bundle arguments) {
                return bridge.performAction(virtualViewId, action, arguments);
            }

            public AccessibilityNodeInfo findFocus(int focus) {
                return (AccessibilityNodeInfo) bridge.findFocus(focus);
            }
        };
    }
}
