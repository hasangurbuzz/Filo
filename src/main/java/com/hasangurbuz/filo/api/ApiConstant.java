package com.hasangurbuz.filo.api;

import org.openapitools.model.SortDTO;

import java.time.LocalDate;

public interface ApiConstant {
    final int PAGE_OFFSET = 0;
    final int PAGE_LIMIT = 20;
    final SortDTO.PropertyEnum SORT_PROPERTY = SortDTO.PropertyEnum.CREATION_DATE;
    final SortDTO.DirectionEnum SORT_DIRECTION = SortDTO.DirectionEnum.ASC;
    final LocalDate DATE_MIN = LocalDate.of(1950, 01, 01);
    final LocalDate DATE_MAX = LocalDate.now();
    final int BRAND_LENGTH_MAX = 25;
    final int MODEL_LENGTH_MAX = 25;
    final int TAG_LENGTH_MAX = 30;
}
