package android.support.p001v4.p014os;

import android.support.annotation.RestrictTo;
import com.google.android.vending.expansion.downloader.Constants;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.os.LocaleHelper */
final class LocaleHelper {
    LocaleHelper() {
    }

    static Locale forLanguageTag(String str) {
        if (str.contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
            String[] args = str.split(Constants.FILENAME_SEQUENCE_SEPARATOR);
            if (args.length > 2) {
                return new Locale(args[0], args[1], args[2]);
            }
            if (args.length > 1) {
                return new Locale(args[0], args[1]);
            }
            if (args.length == 1) {
                return new Locale(args[0]);
            }
        } else if (!str.contains("_")) {
            return new Locale(str);
        } else {
            String[] args2 = str.split("_");
            if (args2.length > 2) {
                return new Locale(args2[0], args2[1], args2[2]);
            }
            if (args2.length > 1) {
                return new Locale(args2[0], args2[1]);
            }
            if (args2.length == 1) {
                return new Locale(args2[0]);
            }
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + str + "]");
    }

    static String toLanguageTag(Locale locale) {
        StringBuilder buf = new StringBuilder();
        buf.append(locale.getLanguage());
        String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            buf.append(Constants.FILENAME_SEQUENCE_SEPARATOR);
            buf.append(locale.getCountry());
        }
        return buf.toString();
    }
}
