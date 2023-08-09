package com.hasangurbuz.vehiclemanager.mapper;

import java.util.List;

public interface Mapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDtoList(List<E> entityList);


}
