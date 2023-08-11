package com.hasangurbuz.vehiclemanager.service.impl;


import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.QVehicleAuthority;
import com.querydsl.core.types.OrderSpecifier;
import org.openapitools.model.SortDTO;
import org.springframework.stereotype.Component;

@Component
public class SortHelper {

    public OrderSpecifier getVehicleOrder(SortDTO sort) {
        SortDTO.DirectionEnum direction = sort.getDirection();
        SortDTO.PropertyEnum property = sort.getProperty();

        boolean isAscending = direction == SortDTO.DirectionEnum.ASC;
        QVehicle vehicle = QVehicle.vehicle;
        OrderSpecifier orderSpecifier;

        switch (property) {
            case CREATION_DATE:
                if (isAscending) {
                    orderSpecifier = vehicle.creationDate.asc();
                    break;
                }
                orderSpecifier = vehicle.creationDate.desc();
                break;
            default:
                orderSpecifier = vehicle.creationDate.asc();
                break;
        }

        return orderSpecifier;
    }

    public OrderSpecifier getVehicleAuthorityOrder(SortDTO sort) {
        SortDTO.DirectionEnum direction = sort.getDirection();
        SortDTO.PropertyEnum property = sort.getProperty();
        OrderSpecifier orderSpecifier;
        QVehicleAuthority qVAuthority = QVehicleAuthority.vehicleAuthority;

        boolean isAscending = direction == SortDTO.DirectionEnum.ASC;

        switch (property) {
            case CREATION_DATE:
                if (isAscending) {
                    orderSpecifier = qVAuthority.creationDate.asc();
                    break;
                }
                orderSpecifier = qVAuthority.creationDate.desc();
                break;

            default:
                orderSpecifier = qVAuthority.creationDate.asc();
                break;
        }

        return orderSpecifier;
    }

}
