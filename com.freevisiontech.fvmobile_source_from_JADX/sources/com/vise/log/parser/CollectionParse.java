package com.vise.log.parser;

import android.annotation.SuppressLint;
import com.vise.log.common.LogConvert;
import java.util.Collection;

public class CollectionParse implements Parser<Collection> {
    public Class<Collection> parseClassType() {
        return Collection.class;
    }

    @SuppressLint({"DefaultLocale"})
    public String parseString(Collection collection) {
        String msg = String.format("%s size = %d [" + LINE_SEPARATOR, new Object[]{collection.getClass().getName(), Integer.valueOf(collection.size())});
        if (!collection.isEmpty()) {
            int flag = 0;
            for (Object item : collection) {
                StringBuilder append = new StringBuilder().append(msg);
                Object[] objArr = new Object[3];
                objArr[0] = Integer.valueOf(flag);
                objArr[1] = LogConvert.objectToString(item);
                int flag2 = flag + 1;
                objArr[2] = flag < collection.size() + -1 ? "," + LINE_SEPARATOR : LINE_SEPARATOR;
                msg = append.append(String.format("[%d]:%s%s", objArr)).toString();
                flag = flag2;
            }
        }
        return msg + "]";
    }
}
