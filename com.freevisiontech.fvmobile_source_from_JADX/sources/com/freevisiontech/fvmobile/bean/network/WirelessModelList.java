package com.freevisiontech.fvmobile.bean.network;

import java.util.List;

public class WirelessModelList {
    private String code;
    private List<DataBean> data;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public List<DataBean> getData() {
        return this.data;
    }

    public void setData(List<DataBean> data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String brand;
        private String model;

        public DataBean() {
        }

        public DataBean(String brand2, String model2) {
            this.brand = brand2;
            this.model = model2;
        }

        public String getModel() {
            return this.model;
        }

        public void setModel(String model2) {
            this.model = model2;
        }

        public String getBrand() {
            return this.brand;
        }

        public void setBrand(String brand2) {
            this.brand = brand2;
        }
    }
}
