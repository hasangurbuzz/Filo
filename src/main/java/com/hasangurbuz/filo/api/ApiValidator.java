package com.hasangurbuz.filo.api;

import org.codehaus.plexus.util.StringUtils;
import org.openapitools.model.PageRequestDTO;
import org.openapitools.model.SortDTO;

import static com.hasangurbuz.filo.api.ApiConstant.*;
import static org.openapitools.model.SortDTO.DirectionEnum.ASC;

public class ApiValidator {

    public static boolean isChassisValid(String chassisNum) {
        int CHASSIS_NUM_LENGTH = 17;
        if (chassisNum.length() != CHASSIS_NUM_LENGTH) {
            return false;
        }
        return StringUtils.isAlphanumeric(chassisNum);
    }

    public static boolean isPlateNumberValid(String plateNum) {
        int MIN_CITY_CODE = 01;
        int MAX_CITY_CODE = 81;

        if (StringUtils.isBlank(plateNum)) {
            return false;
        }
        plateNum = StringUtils.deleteWhitespace(plateNum);

        if (plateNum.length() < 7) {
            return false;
        }

        if (plateNum.length() > 8) {
            return false;
        }

        if (!StringUtils.isAlphanumeric(plateNum)) {
            return false;
        }

        if (!Character.isDigit(plateNum.charAt(0))) {
            return false;
        }

        if (!Character.isDigit(plateNum.charAt(1))) {
            return false;
        }

        int cityCode = Integer.parseInt(plateNum.substring(0, 2));

        if (cityCode > MAX_CITY_CODE) {
            return false;
        }

        if (cityCode < MIN_CITY_CODE) {
            return false;
        }

        if (Character.isDigit(plateNum.charAt(2))) {
            return false;
        }

        if (Character.isDigit(plateNum.charAt(3))) {
            for (int i = 4; i < plateNum.length() - 1; i++) {
                if (Character.isLetter(plateNum.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        if (Character.isDigit(plateNum.charAt(4))) {
            for (int i = 5; i < plateNum.length() - 1; i++) {
                if (Character.isLetter(plateNum.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        if (Character.isDigit(plateNum.charAt(5))) {
            for (int i = 5; i < plateNum.length() - 1; i++) {
                if (Character.isLetter(plateNum.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static PageRequestDTO validatePageRequest(PageRequestDTO pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequestDTO();
        }
        if (pageRequest.getFrom() == null) {
            pageRequest.setFrom(PAGE_OFFSET);
        }
        if (pageRequest.getFrom() < 0) {
            pageRequest.setFrom(PAGE_OFFSET);
        }
        if (pageRequest.getSize() == null) {
            pageRequest.setSize(PAGE_LIMIT);
        }
        if (pageRequest.getSize() < 1) {
            pageRequest.setFrom(PAGE_LIMIT);
        }
        if (pageRequest.getSort() == null) {
            pageRequest.setSort(new SortDTO());
        }
        if (pageRequest.getSort().getProperty() == null) {
            pageRequest.getSort().setProperty(SORT_PROPERTY);
        }
        if (pageRequest.getSort().getDirection() == null) {
            pageRequest.getSort().setDirection(ASC);
        }
        return pageRequest;
    }

}
