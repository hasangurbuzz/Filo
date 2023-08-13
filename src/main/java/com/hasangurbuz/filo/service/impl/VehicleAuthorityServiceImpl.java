package com.hasangurbuz.filo.service.impl;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.QVehicleAuthority;
import com.hasangurbuz.filo.domain.Vehicle;
import com.hasangurbuz.filo.domain.VehicleAuthority;
import com.hasangurbuz.filo.repository.VehicleAuthorityRepository;
import com.hasangurbuz.filo.service.PagedResults;
import com.hasangurbuz.filo.service.VehicleAuthorityService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.openapitools.model.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class VehicleAuthorityServiceImpl implements VehicleAuthorityService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleAuthorityRepository vAuthRepo;

    @Autowired
    private SortHelper sortHelper;

    @Override
    public VehicleAuthority create(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(false);
        vehicleAuthority.setCreationDate(OffsetDateTime.now());
        vehicleAuthority = vAuthRepo.save(vehicleAuthority);
        return vehicleAuthority;
    }

    @Override
    public VehicleAuthority find(Long userId, Vehicle vehicle) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        VehicleAuthority vAuthority = queryFactory
                .selectFrom(vehicleAuth)
                .where(vehicleAuth.vehicle.companyId.eq(vehicle.getCompanyId())
                        .and(vehicleAuth.userId.eq(userId))
                        .and(vehicleAuth.vehicle.id.eq(vehicle.getId()))
                        .and(vehicleAuth.isDeleted.isFalse())
                        .and(vehicleAuth.vehicle.isDeleted.isFalse()))
                .fetchOne();

        return vAuthority;
    }

    @Override
    public VehicleAuthority update(VehicleAuthority vehicleAuthority) {
        return vAuthRepo.save(vehicleAuthority);
    }

    @Override
    public PagedResults<VehicleAuthority> searchByUserId(Long companyId, Long userId, PageRequestDTO pageRequest) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        PagedResults<VehicleAuthority> pagedResults = new PagedResults<>();
        OrderSpecifier order = sortHelper.getVehicleAuthorityOrder(pageRequest.getSort());

        QueryResults<VehicleAuthority> results = queryFactory
                .selectFrom(vehicleAuth)
                .where(vehicleAuth.userId.eq(userId)
                        .and(vehicleAuth.vehicle.companyId.eq(companyId))
                        .and(vehicleAuth.isDeleted.isFalse())
                        .and(vehicleAuth.vehicle.isDeleted.isFalse()))
                .offset(pageRequest.getFrom())
                .limit(pageRequest.getSize())
                .orderBy(order)
                .fetchResults();

        pagedResults.setItems(results.getResults());
        pagedResults.setTotal(results.getTotal());
        return pagedResults;
    }

    @Override
    public PagedResults<VehicleAuthority> searchByVehicle(
            Vehicle vehicle, PageRequestDTO pageRequest
    ) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        PagedResults<VehicleAuthority> pagedResults = new PagedResults<>();
        OrderSpecifier order = sortHelper.getVehicleAuthorityOrder(pageRequest.getSort());

        QueryResults<VehicleAuthority> results = queryFactory
                .selectFrom(vehicleAuth)
                .where(vehicleAuth.vehicle.companyId.eq(vehicle.getCompanyId())
                        .and(vehicleAuth.vehicle.id.eq(vehicle.getId()))
                        .and(vehicleAuth.vehicle.isDeleted.isFalse())
                        .and(vehicleAuth.isDeleted.isFalse()))
                .offset(pageRequest.getFrom())
                .limit(pageRequest.getSize())
                .orderBy(order)
                .fetchResults();

        pagedResults.setTotal(results.getTotal());
        pagedResults.setItems(results.getResults());
        return pagedResults;
    }

    @Override
    public List<VehicleAuthority> searchByGroup(Long userId, Group group) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<VehicleAuthority> results = queryFactory.selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.isDeleted.isFalse()
                                .and(vehicleAuth.userId.eq(userId))
                                .and(vehicleAuth.vehicle.group.id.eq(group.getId()))
                                .and(vehicleAuth.vehicle.companyId.eq(group.getCompanyId()))
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.vehicle.group.companyId.eq(group.getCompanyId()))

                ).fetch();

        return results;
    }


    @Override
    public void delete(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(true);
        vAuthRepo.save(vehicleAuthority);
    }

}
