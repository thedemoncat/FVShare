package com.freevisiontech.fvmobile.model;

import java.util.List;

public class FilePhotoModel {
    private List<DateBean> date;

    public List<DateBean> getDate() {
        return this.date;
    }

    public void setDate(List<DateBean> date2) {
        this.date = date2;
    }

    public static class DateBean {
        private List<ContentBean> content;
        private String time;

        public String getTime() {
            return this.time;
        }

        public void setTime(String time2) {
            this.time = time2;
        }

        public List<ContentBean> getContent() {
            return this.content;
        }

        public void setContent(List<ContentBean> content2) {
            this.content = content2;
        }

        public static class ContentBean {
            private String path;
            private boolean selected;
            private String type;

            public boolean isSelected() {
                return this.selected;
            }

            public void setSelected(boolean focus) {
                this.selected = focus;
            }

            public String getPath() {
                return this.path;
            }

            public void setPath(String path2) {
                this.path = path2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }

            public String toString() {
                return "ContentBean{path='" + this.path + '\'' + ", type='" + this.type + '\'' + '}';
            }
        }

        public String toString() {
            return "DateBean{time='" + this.time + '\'' + ", content=" + this.content + '}';
        }
    }

    public String toString() {
        return "FilePhotoModel{date=" + this.date + '}';
    }
}
