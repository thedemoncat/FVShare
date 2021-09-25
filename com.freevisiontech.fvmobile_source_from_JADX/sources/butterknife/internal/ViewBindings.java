package butterknife.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ViewBindings {
    private final Set<FieldViewBinding> fieldBindings = new LinkedHashSet();

    /* renamed from: id */
    private final int f1217id;
    private final LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> methodBindings = new LinkedHashMap<>();

    ViewBindings(int id) {
        this.f1217id = id;
    }

    public int getId() {
        return this.f1217id;
    }

    public Collection<FieldViewBinding> getFieldBindings() {
        return this.fieldBindings;
    }

    public Map<ListenerClass, Map<ListenerMethod, Set<MethodViewBinding>>> getMethodBindings() {
        return this.methodBindings;
    }

    public boolean hasMethodBinding(ListenerClass listener, ListenerMethod method) {
        Map<ListenerMethod, Set<MethodViewBinding>> methods = this.methodBindings.get(listener);
        return methods != null && methods.containsKey(method);
    }

    public void addMethodBinding(ListenerClass listener, ListenerMethod method, MethodViewBinding binding) {
        Map<ListenerMethod, Set<MethodViewBinding>> methods = this.methodBindings.get(listener);
        Set<MethodViewBinding> set = null;
        if (methods == null) {
            methods = new LinkedHashMap<>();
            this.methodBindings.put(listener, methods);
        } else {
            set = methods.get(method);
        }
        if (set == null) {
            set = new LinkedHashSet<>();
            methods.put(method, set);
        }
        set.add(binding);
    }

    public void addFieldBinding(FieldViewBinding fieldBinding) {
        this.fieldBindings.add(fieldBinding);
    }

    public List<ViewBinding> getRequiredBindings() {
        List<ViewBinding> requiredViewBindings = new ArrayList<>();
        for (FieldViewBinding fieldBinding : this.fieldBindings) {
            if (fieldBinding.isRequired()) {
                requiredViewBindings.add(fieldBinding);
            }
        }
        for (Map<ListenerMethod, Set<MethodViewBinding>> methodBinding : this.methodBindings.values()) {
            for (Set<MethodViewBinding> set : methodBinding.values()) {
                for (MethodViewBinding binding : set) {
                    if (binding.isRequired()) {
                        requiredViewBindings.add(binding);
                    }
                }
            }
        }
        return requiredViewBindings;
    }
}
