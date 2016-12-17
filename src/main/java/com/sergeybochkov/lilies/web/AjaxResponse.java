package com.sergeybochkov.lilies.web;

import java.io.Serializable;

public final class AjaxResponse implements Serializable {

    private final String result;

    public AjaxResponse() {
        this("success");
    }

    public AjaxResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
