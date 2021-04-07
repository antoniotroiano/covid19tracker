package com.covid19.model.data;

public class JsonValueTransfer {

    private Integer value;

    public JsonValueTransfer() {

    }

    public JsonValueTransfer(final JsonValueTransfer jsonValueTransfer) {
        this.value = jsonValueTransfer.getValue();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonValueTransfer{" +
                "value=" + value +
                '}';
    }
}