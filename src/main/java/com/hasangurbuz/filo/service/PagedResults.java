package com.hasangurbuz.filo.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResults<T> {

    private long total;

    private List<T> items;

}
