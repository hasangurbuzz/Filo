package com.hasangurbuz.vehiclemanager.api;

import org.openapitools.model.SortDTO;

public interface ApiConstant {
    int PAGE_OFFSET = 0;
    int PAGE_LIMIT = 20;
    SortDTO.PropertyEnum SORT_PROPERTY = SortDTO.PropertyEnum.CREATION_DATE;
    SortDTO.DirectionEnum SORT_DIRECTION = SortDTO.DirectionEnum.ASC;
}
