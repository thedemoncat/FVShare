package okhttp3;

import javax.annotation.Nullable;

public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String scheme2, String realm2) {
        if (scheme2 == null) {
            throw new NullPointerException("scheme == null");
        } else if (realm2 == null) {
            throw new NullPointerException("realm == null");
        } else {
            this.scheme = scheme2;
            this.realm = realm2;
        }
    }

    public String scheme() {
        return this.scheme;
    }

    public String realm() {
        return this.realm;
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof Challenge) && ((Challenge) other).scheme.equals(this.scheme) && ((Challenge) other).realm.equals(this.realm);
    }

    public int hashCode() {
        return ((this.realm.hashCode() + 899) * 31) + this.scheme.hashCode();
    }

    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\"";
    }
}
