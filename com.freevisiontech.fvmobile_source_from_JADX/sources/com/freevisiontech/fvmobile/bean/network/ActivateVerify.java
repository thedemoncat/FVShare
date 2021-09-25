package com.freevisiontech.fvmobile.bean.network;

public class ActivateVerify {
    private String activationId;
    private String code;
    private String random;
    private String result;

    public String getResult() {
        return this.result;
    }

    public void setResult(String result2) {
        this.result = result2;
    }

    public String getRandom() {
        return this.random;
    }

    public void setRandom(String random2) {
        this.random = random2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getActivationId() {
        return this.activationId;
    }

    public void setActivationId(String activationId2) {
        this.activationId = activationId2;
    }
}
