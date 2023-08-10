package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.QVehicleAuthority;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.repository.VehicleAuthorityRepository;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.SortDTO;
import org.openapitools.model.VehicleListRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class VehicleAuthorityServiceImpl implements VehicleAuthorityService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleAuthorityRepository authRepo;

    @Override
    public PagedResults<VehicleAuthority> search(VehicleListRequestDTO request) {
        PagedResults<VehicleAuthority> pagedResults = new PagedResults<VehicleAuthority>();
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<VehicleAuthority> query = queryFactory
                .selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.vehicle.companyId.eq(ApiContext.get().getCompanyId())
                                .and(vehicleAuth.userId.eq(ApiContext.get().getUserId()))
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.role.in(ApiContext.get().getUserRole(), UserRole.STANDARD))

                );


        if (StringUtils.isNotBlank(request.getTerm())) {
            query = query.where(vehicleAuth.vehicle.tag.containsIgnoreCase(request.getTerm()));
        }

        QueryResults<VehicleAuthority> results = query
                .orderBy(getOrder(request.getSort()))
                .offset(request.getFrom())
                .limit(request.getSize())
                .fetchResults();

        pagedResults.setItems(results.getResults());
        pagedResults.setTotal(results.getTotal());

        return pagedResults;
    }


    private OrderSpecifier getOrder(SortDTO sort) {
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

    @Override
    public VehicleAuthority create(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(false);
        vehicleAuthority = authRepo.save(vehicleAuthority);
        return vehicleAuthority;
    }

    @Override
    public VehicleAuthority getByVehicleId(Long id) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        VehicleAuthority authority = queryFactory
                .selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.isDeleted.isFalse()
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.role.in(ApiContext.get().getUserRole(), UserRole.STANDARD))
                                .and(vehicleAuth.vehicle.id.eq(id))
                                .and(vehicleAuth.userId.eq(ApiContext.get().getUserId()))
                                .and(vehicleAuth.vehicle.companyId.eq(ApiContext.get().getCompanyId()))
                ).fetchOne();

        return authority;
    }


}
