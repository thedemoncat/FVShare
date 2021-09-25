package com.freevisiontech.fvmobile.bean;

import java.util.List;

public class UpgradeFromAssetsBean {
    private List<UpdateinfoBean> updateinfo;

    public List<UpdateinfoBean> getUpdateinfo() {
        return this.updateinfo;
    }

    public void setUpdateinfo(List<UpdateinfoBean> updateinfo2) {
        this.updateinfo = updateinfo2;
    }

    public static class UpdateinfoBean {
        private String fwname;
        private String fwtype;
        private String isforce;
        private String releasenotes;
        private String verifycode;
        private String version;

        public String getFwtype() {
            return this.fwtype;
        }

        public void setFwtype(String fwtype2) {
            this.fwtype = fwtype2;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version2) {
            this.version = version2;
        }

        public String getVerifycode() {
            return this.verifycode;
        }

        public void setVerifycode(String verifycode2) {
            this.verifycode = verifycode2;
        }

        public String getIsforce() {
            return this.isforce;
        }

        public void setIsforce(String isforce2) {
            this.isforce = isforce2;
        }

        public String getReleasenotes() {
            return this.releasenotes;
        }

        public void setReleasenotes(String releasenotes2) {
            this.releasenotes = releasenotes2;
        }

        public String getFwname() {
            return this.fwname;
        }

        public void setFwname(String fwname2) {
            this.fwname = fwname2;
        }

        public String toString() {
            return "UpdateinfoBean{fwtype='" + this.fwtype + '\'' + ", version='" + this.version + '\'' + ", verifycode='" + this.verifycode + '\'' + ", isforce='" + this.isforce + '\'' + ", releasenotes='" + this.releasenotes + '\'' + ", fwname='" + this.fwname + '\'' + '}';
        }
    }
}
