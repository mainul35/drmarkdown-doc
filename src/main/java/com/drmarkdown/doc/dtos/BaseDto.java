package com.drmarkdown.doc.dtos;

public abstract class BaseDto<T> {

    public abstract void mapEntityToDto(T t);
}