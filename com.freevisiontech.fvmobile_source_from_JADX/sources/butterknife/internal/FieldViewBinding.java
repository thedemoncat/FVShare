package butterknife.internal;

final class FieldViewBinding implements ViewBinding {
    private final String name;
    private final boolean required;
    private final String type;

    FieldViewBinding(String name2, String type2, boolean required2) {
        this.name = name2;
        this.type = type2;
        this.required = required2;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return "field '" + this.name + "'";
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean requiresCast() {
        return !"android.view.View".equals(this.type);
    }
}
