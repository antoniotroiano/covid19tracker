package com.statistics.corona.model.data;

public class JsonValueTransfer {

    Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataValue{" +
                "value=" + value +
                '}';
    }
}