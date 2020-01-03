package com.s1mple.test.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {
    private final Object message;

    Assertions(Object message) {
        this.message = message;
    }

    public <T> void isInstanceOf(Class<T> clazz) {
        assertThat(isInstanceOfa(clazz)).isTrue();
    }

    private <T> boolean isInstanceOfa(Class<T> clazz) {
        try {
            T t = JSON.toJavaObject((JSON) message, clazz);
            System.out.println(t);
            return true;
        } catch (ClassCastException | JSONException ex) {
            return false;
        }
    }

    public <T> void castTo(Class<T> clazz) {

    }
}
