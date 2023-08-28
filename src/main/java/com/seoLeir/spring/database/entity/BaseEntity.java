package com.seoLeir.spring.database.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {
    T getId();

    void setId(T id);
}
