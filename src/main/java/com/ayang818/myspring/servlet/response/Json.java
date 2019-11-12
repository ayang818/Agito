package com.ayang818.myspring.servlet.response;

public class Json implements Renderable {

    private Object model;

    public Json(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
