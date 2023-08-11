package com.hasangurbuz.vehiclemanager.api;

import org.codehaus.plexus.util.StringUtils;

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


}
