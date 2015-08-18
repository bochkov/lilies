package com.sergeybochkov.lilies.web;

import java.io.Serializable;

public class AjaxResponse implements Serializable {

    private String result;

    public AjaxResponse() {
        this.result = "success";
    }

    public AjaxResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
