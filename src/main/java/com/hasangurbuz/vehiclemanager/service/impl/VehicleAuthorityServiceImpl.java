package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.QVehicleAuthority;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;

import com.querydsl.core.QueryResults;
import com.querydsl.core.support.QueryBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.VehicleListRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class VehicleAuthorityServiceImpl implements VehicleAuthorityService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public PagedResults<Vehicle> search(VehicleListRequestDTO request) {
        PagedResults<Vehicle> pagedResults = new PagedResults<Vehicle>();
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<Vehicle> query = queryFactory
                .select(vehicle)
                .from(vehicleAuth)
                .where(
                        vehicleAuth.vehicle.companyId.eq(ApiContext.get().getCompanyId())
                                .and(vehicleAuth.userId.eq(ApiContext.get().getUserId()))
                );


        if (StringUtils.isNotBlank(request.getTerm())){
            query = query.where(vehicleAuth.vehicle.tag.containsIgnoreCase(request.getTerm()));
        }

        QueryResults<Vehicle> results = query
                .offset(request.getFrom())
                .limit(request.getSize())
                .fetchResults();

        pagedResults.setItems(results.getResults());
        pagedResults.setTotal(results.getTotal());

        return pagedResults;
    }
}
