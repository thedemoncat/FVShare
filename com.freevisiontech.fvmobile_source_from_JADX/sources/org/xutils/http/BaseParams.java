package org.xutils.http;

import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.http.body.BodyItemWrapper;
import org.xutils.http.body.FileBody;
import org.xutils.http.body.InputStreamBody;
import org.xutils.http.body.MultipartBody;
import org.xutils.http.body.RequestBody;
import org.xutils.http.body.StringBody;
import org.xutils.http.body.UrlEncodedParamsBody;

abstract class BaseParams {
    private boolean asJsonContent = false;
    private String bodyContent;
    private final List<KeyValue> bodyParams = new ArrayList();
    private String charset = "UTF-8";
    private final List<KeyValue> fileParams = new ArrayList();
    private final List<Header> headers = new ArrayList();
    private HttpMethod method;
    private boolean multipart = false;
    private final List<KeyValue> queryStringParams = new ArrayList();
    private RequestBody requestBody;

    BaseParams() {
    }

    public void setCharset(String charset2) {
        if (!TextUtils.isEmpty(charset2)) {
            this.charset = charset2;
        }
    }

    public String getCharset() {
        return this.charset;
    }

    public void setMethod(HttpMethod method2) {
        this.method = method2;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public boolean isMultipart() {
        return this.multipart;
    }

    public void setMultipart(boolean multipart2) {
        this.multipart = multipart2;
    }

    public boolean isAsJsonContent() {
        return this.asJsonContent;
    }

    public void setAsJsonContent(boolean asJsonContent2) {
        this.asJsonContent = asJsonContent2;
    }

    public void setHeader(String name, String value) {
        Header header = new Header(name, value, true);
        Iterator<Header> it = this.headers.iterator();
        while (it.hasNext()) {
            if (name.equals(it.next().key)) {
                it.remove();
            }
        }
        this.headers.add(header);
    }

    public void addHeader(String name, String value) {
        this.headers.add(new Header(name, value, false));
    }

    public void addParameter(String name, Object value) {
        if (value != null) {
            if (this.method == null || HttpMethod.permitsRequestBody(this.method)) {
                if (TextUtils.isEmpty(name)) {
                    this.bodyContent = value.toString();
                } else if ((value instanceof File) || (value instanceof InputStream) || (value instanceof byte[])) {
                    this.fileParams.add(new KeyValue(name, value));
                } else if (value instanceof List) {
                    for (Object item : (List) value) {
                        this.bodyParams.add(new ArrayItem(name, item));
                    }
                } else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    int len = array.length();
                    for (int i = 0; i < len; i++) {
                        this.bodyParams.add(new ArrayItem(name, array.opt(i)));
                    }
                } else if (value.getClass().isArray()) {
                    int len2 = Array.getLength(value);
                    for (int i2 = 0; i2 < len2; i2++) {
                        this.bodyParams.add(new ArrayItem(name, Array.get(value, i2)));
                    }
                } else {
                    this.bodyParams.add(new KeyValue(name, value));
                }
            } else if (TextUtils.isEmpty(name)) {
            } else {
                if (value instanceof List) {
                    for (Object item2 : (List) value) {
                        this.queryStringParams.add(new ArrayItem(name, item2));
                    }
                } else if (value.getClass().isArray()) {
                    int len3 = Array.getLength(value);
                    for (int i3 = 0; i3 < len3; i3++) {
                        this.queryStringParams.add(new ArrayItem(name, Array.get(value, i3)));
                    }
                } else {
                    this.queryStringParams.add(new KeyValue(name, value));
                }
            }
        }
    }

    public void addQueryStringParameter(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            this.queryStringParams.add(new KeyValue(name, value));
        }
    }

    public void addBodyParameter(String name, String value) {
        if (!TextUtils.isEmpty(name)) {
            this.bodyParams.add(new KeyValue(name, value));
        } else {
            this.bodyContent = value;
        }
    }

    public void addBodyParameter(String name, File value) {
        addBodyParameter(name, value, (String) null, (String) null);
    }

    public void addBodyParameter(String name, Object value, String contentType) {
        addBodyParameter(name, value, contentType, (String) null);
    }

    public void addBodyParameter(String name, Object value, String contentType, String fileName) {
        if (!TextUtils.isEmpty(contentType) || !TextUtils.isEmpty(fileName)) {
            this.fileParams.add(new KeyValue(name, new BodyItemWrapper(value, contentType, fileName)));
        } else {
            this.fileParams.add(new KeyValue(name, value));
        }
    }

    public void setBodyContent(String content) {
        this.bodyContent = content;
    }

    public String getBodyContent() {
        checkBodyParams();
        return this.bodyContent;
    }

    public List<Header> getHeaders() {
        return new ArrayList(this.headers);
    }

    public List<KeyValue> getQueryStringParams() {
        checkBodyParams();
        return new ArrayList(this.queryStringParams);
    }

    public List<KeyValue> getBodyParams() {
        checkBodyParams();
        return new ArrayList(this.bodyParams);
    }

    public List<KeyValue> getFileParams() {
        checkBodyParams();
        return new ArrayList(this.fileParams);
    }

    public List<KeyValue> getStringParams() {
        List<KeyValue> result = new ArrayList<>(this.queryStringParams.size() + this.bodyParams.size());
        result.addAll(this.queryStringParams);
        result.addAll(this.bodyParams);
        return result;
    }

    public String getStringParameter(String name) {
        for (KeyValue kv : this.queryStringParams) {
            if (name == null && kv.key == null) {
                return kv.getValueStr();
            }
            if (name != null && name.equals(kv.key)) {
                return kv.getValueStr();
            }
        }
        for (KeyValue kv2 : this.bodyParams) {
            if (name == null && kv2.key == null) {
                return kv2.getValueStr();
            }
            if (name != null && name.equals(kv2.key)) {
                return kv2.getValueStr();
            }
        }
        return null;
    }

    public List<KeyValue> getParams(String name) {
        List<KeyValue> result = new ArrayList<>();
        for (KeyValue kv : this.queryStringParams) {
            if (name == null && kv.key == null) {
                result.add(kv);
            } else if (name != null && name.equals(kv.key)) {
                result.add(kv);
            }
        }
        for (KeyValue kv2 : this.bodyParams) {
            if (name == null && kv2.key == null) {
                result.add(kv2);
            } else if (name != null && name.equals(kv2.key)) {
                result.add(kv2);
            }
        }
        for (KeyValue kv3 : this.fileParams) {
            if (name == null && kv3.key == null) {
                result.add(kv3);
            } else if (name != null && name.equals(kv3.key)) {
                result.add(kv3);
            }
        }
        return result;
    }

    public void clearParams() {
        this.queryStringParams.clear();
        this.bodyParams.clear();
        this.fileParams.clear();
        this.bodyContent = null;
        this.requestBody = null;
    }

    public void removeParameter(String name) {
        if (!TextUtils.isEmpty(name)) {
            Iterator<KeyValue> it = this.queryStringParams.iterator();
            while (it.hasNext()) {
                if (name.equals(it.next().key)) {
                    it.remove();
                }
            }
            Iterator<KeyValue> it2 = this.bodyParams.iterator();
            while (it2.hasNext()) {
                if (name.equals(it2.next().key)) {
                    it2.remove();
                }
            }
            Iterator<KeyValue> it3 = this.fileParams.iterator();
            while (it3.hasNext()) {
                if (name.equals(it3.next().key)) {
                    it3.remove();
                }
            }
            return;
        }
        this.bodyContent = null;
    }

    public void setRequestBody(RequestBody requestBody2) {
        this.requestBody = requestBody2;
    }

    public RequestBody getRequestBody() throws IOException {
        checkBodyParams();
        if (this.requestBody != null) {
            return this.requestBody;
        }
        if (!TextUtils.isEmpty(this.bodyContent)) {
            return new StringBody(this.bodyContent, this.charset);
        }
        if (this.multipart || this.fileParams.size() > 0) {
            if (this.multipart || this.fileParams.size() != 1) {
                this.multipart = true;
                return new MultipartBody(this.fileParams, this.charset);
            }
            Iterator<KeyValue> it = this.fileParams.iterator();
            if (!it.hasNext()) {
                return null;
            }
            String contentType = null;
            Object value = it.next().value;
            if (value instanceof BodyItemWrapper) {
                BodyItemWrapper wrapper = (BodyItemWrapper) value;
                value = wrapper.getValue();
                contentType = wrapper.getContentType();
            }
            if (value instanceof File) {
                return new FileBody((File) value, contentType);
            }
            if (value instanceof InputStream) {
                return new InputStreamBody((InputStream) value, contentType);
            }
            if (value instanceof byte[]) {
                return new InputStreamBody(new ByteArrayInputStream((byte[]) value), contentType);
            }
            if (value instanceof String) {
                RequestBody result = new StringBody((String) value, this.charset);
                result.setContentType(contentType);
                return result;
            }
            LogUtil.m1570w("Some params will be ignored for: " + toString());
            return null;
        } else if (this.bodyParams.size() > 0) {
            return new UrlEncodedParamsBody(this.bodyParams, this.charset);
        } else {
            return null;
        }
    }

    public String toJSONString() {
        JSONObject jsonObject;
        List<KeyValue> list = new ArrayList<>(this.queryStringParams.size() + this.bodyParams.size());
        list.addAll(this.queryStringParams);
        list.addAll(this.bodyParams);
        try {
            if (!TextUtils.isEmpty(this.bodyContent)) {
                jsonObject = new JSONObject(this.bodyContent);
            } else {
                jsonObject = new JSONObject();
            }
            params2Json(jsonObject, list);
            return jsonObject.toString();
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        checkBodyParams();
        StringBuilder sb = new StringBuilder();
        if (!this.queryStringParams.isEmpty()) {
            for (KeyValue kv : this.queryStringParams) {
                sb.append(kv.key).append("=").append(kv.value).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (HttpMethod.permitsRequestBody(this.method)) {
            sb.append("<");
            if (!TextUtils.isEmpty(this.bodyContent)) {
                sb.append(this.bodyContent);
            } else if (!this.bodyParams.isEmpty()) {
                for (KeyValue kv2 : this.bodyParams) {
                    sb.append(kv2.key).append("=").append(kv2.value).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(">");
        }
        return sb.toString();
    }

    private void checkBodyParams() {
        JSONObject jsonObject;
        if (!this.bodyParams.isEmpty()) {
            if (!HttpMethod.permitsRequestBody(this.method) || !TextUtils.isEmpty(this.bodyContent) || this.requestBody != null) {
                this.queryStringParams.addAll(this.bodyParams);
                this.bodyParams.clear();
            }
            if (!this.bodyParams.isEmpty() && (this.multipart || this.fileParams.size() > 0)) {
                this.fileParams.addAll(this.bodyParams);
                this.bodyParams.clear();
            }
            if (this.asJsonContent && !this.bodyParams.isEmpty()) {
                try {
                    if (!TextUtils.isEmpty(this.bodyContent)) {
                        jsonObject = new JSONObject(this.bodyContent);
                    } else {
                        jsonObject = new JSONObject();
                    }
                    params2Json(jsonObject, this.bodyParams);
                    this.bodyContent = jsonObject.toString();
                    this.bodyParams.clear();
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void params2Json(JSONObject jsonObject, List<KeyValue> paramList) throws JSONException {
        JSONArray ja;
        HashSet<String> arraySet = new HashSet<>(paramList.size());
        LinkedHashMap<String, JSONArray> tempData = new LinkedHashMap<>(paramList.size());
        for (int i = 0; i < paramList.size(); i++) {
            KeyValue kv = paramList.get(i);
            String key = kv.key;
            if (!TextUtils.isEmpty(key)) {
                if (tempData.containsKey(key)) {
                    ja = tempData.get(key);
                } else {
                    ja = new JSONArray();
                    tempData.put(key, ja);
                }
                ja.put(RequestParamsHelper.parseJSONObject(kv.value));
                if (kv instanceof ArrayItem) {
                    arraySet.add(key);
                }
            }
        }
        for (Map.Entry<String, JSONArray> entry : tempData.entrySet()) {
            String key2 = entry.getKey();
            JSONArray ja2 = entry.getValue();
            if (ja2.length() > 1 || arraySet.contains(key2)) {
                jsonObject.put(key2, ja2);
            } else {
                jsonObject.put(key2, ja2.get(0));
            }
        }
    }

    public static final class ArrayItem extends KeyValue {
        public ArrayItem(String key, Object value) {
            super(key, value);
        }
    }

    public static final class Header extends KeyValue {
        public final boolean setHeader;

        public Header(String key, String value, boolean setHeader2) {
            super(key, value);
            this.setHeader = setHeader2;
        }
    }
}
