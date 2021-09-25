package com.freevisiontech.fvmobile.bean;

import java.util.List;

public class FVUpgradeBean {
    private List<RowsBean> rows;
    private int total;

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public List<RowsBean> getRows() {
        return this.rows;
    }

    public void setRows(List<RowsBean> rows2) {
        this.rows = rows2;
    }

    public static class RowsBean {
        private String apppath;
        private String apptype;
        private String appver;
        private String encodetype;
        private String isforce;
        private String releasedate;
        private String releasenotes;
        private String verifycode;

        public String getApppath() {
            return this.apppath;
        }

        public void setApppath(String apppath2) {
            this.apppath = apppath2;
        }

        public String getApptype() {
            return this.apptype;
        }

        public void setApptype(String apptype2) {
            this.apptype = apptype2;
        }

        public String getAppver() {
            return this.appver;
        }

        public void setAppver(String appver2) {
            this.appver = appver2;
        }

        public String getEncodetype() {
            return this.encodetype;
        }

        public void setEncodetype(String encodetype2) {
            this.encodetype = encodetype2;
        }

        public String getIsforce() {
            return this.isforce;
        }

        public void setIsforce(String isforce2) {
            this.isforce = isforce2;
        }

        public String getReleasedate() {
            return this.releasedate;
        }

        public void setReleasedate(String releasedate2) {
            this.releasedate = releasedate2;
        }

        public String getReleasenotes() {
            return this.releasenotes;
        }

        public void setReleasenotes(String releasenotes2) {
            this.releasenotes = releasenotes2;
        }

        public String getVerifycode() {
            return this.verifycode;
        }

        public void setVerifycode(String verifycode2) {
            this.verifycode = verifycode2;
        }

        public String toString() {
            return "RowsBean{apppath='" + this.apppath + '\'' + ", apptype='" + this.apptype + '\'' + ", appver='" + this.appver + '\'' + ", encodetype='" + this.encodetype + '\'' + ", isforce='" + this.isforce + '\'' + ", releasedate='" + this.releasedate + '\'' + ", releasenotes='" + this.releasenotes + '\'' + ", verifycode='" + this.verifycode + '\'' + '}';
        }
    }

    public String toString() {
        return "FVUpgradeBean{total=" + this.total + ", rows=" + this.rows + '}';
    }
}
