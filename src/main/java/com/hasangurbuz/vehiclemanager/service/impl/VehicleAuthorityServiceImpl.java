package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.QVehicleAuthority;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.repository.VehicleAuthorityRepository;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.model.VehicleListRequestDTO;
import org.openapitools.model.VehicleUserListRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.OffsetDateTime;

@Service
public class VehicleAuthorityServiceImpl implements VehicleAuthorityService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleAuthorityRepository vAuthRepo;

    @Autowired
    private SortHelper sortHelper;

    @Override
    public PagedResults<VehicleAuthority> search(Long companyId, Long userId, UserRole userRole, VehicleListRequestDTO request) {
        PagedResults<VehicleAuthority> pagedResults = new PagedResults<VehicleAuthority>();
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<VehicleAuthority> query = queryFactory
                .selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.vehicle.companyId.eq(companyId)
                                .and(vehicleAuth.userId.eq(userId))
                                .and(vehicleAuth.role.in(userRole, UserRole.STANDARD))
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.isDeleted.isFalse())

                );


        if (StringUtils.isNotBlank(request.getTerm())) {
            query = query.where(vehicleAuth.vehicle.tag.containsIgnoreCase(request.getTerm()));
        }

        QueryResults<VehicleAuthority> results = query
                .orderBy(sortHelper.getVehicleOrder(request.getSort()))
                .offset(request.getFrom())
                .limit(request.getSize())
                .fetchResults();

        pagedResults.setItems(results.getResults());
        pagedResults.setTotal(results.getTotal());

        return pagedResults;
    }


    @Override
    public VehicleAuthority create(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(false);
        vehicleAuthority.setCreationDate(OffsetDateTime.now());
        vehicleAuthority = vAuthRepo.save(vehicleAuthority);
        return vehicleAuthority;
    }

    @Override
    public VehicleAuthority getByVehicleId(Long vehicleId, Long companyId, Long userId, UserRole userRole) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        VehicleAuthority authority = queryFactory
                .selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.isDeleted.isFalse()
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.role.in(userRole, UserRole.STANDARD))
                                .and(vehicleAuth.vehicle.id.eq(vehicleId))
                                .and(vehicleAuth.userId.eq(userId))
                                .and(vehicleAuth.vehicle.companyId.eq(companyId))
                ).fetchOne();

        return authority;
    }

    @Override
    public void delete(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(true);
        vAuthRepo.save(vehicleAuthority);
    }

    @Override
    public PagedResults<VehicleAuthority> searchUser(Long vehicleId, Long companyId, Long userId, UserRole userRole, VehicleUserListRequestDTO request) {
        PagedResults<VehicleAuthority> vAuthorityPaged = new PagedResults<>();
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);


        // dto düzenlenip dto.userId eklenecek
        JPAQuery<VehicleAuthority> query = queryFactory.selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.vehicle.companyId.eq(companyId)
                                .and(vehicleAuth.userId.eq(userId))
                                .and(vehicleAuth.role.in(userRole, UserRole.STANDARD))
                                .and(vehicleAuth.isDeleted.isFalse())
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.vehicle.id.eq(vehicleId))

                );
        if (userRole != null) {
            query = query.where(vehicleAuth.role.eq(userRole));
        }
        QueryResults<VehicleAuthority> results = query
                .offset(request.getFrom())
                .limit(request.getSize())
                .fetchResults();

        vAuthorityPaged.setTotal(results.getTotal());
        vAuthorityPaged.setItems(results.getResults());

        return vAuthorityPaged;
    }

    @Override
    public VehicleAuthority getByVehicleAndUserId(Long vehicleId, Long companyId, Long userId, UserRole userRole, Long requestedUserId) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        VehicleAuthority vAuthority = queryFactory
                .selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.userId.eq(userId)
                                .and(vehicleAuth.userId.eq(requestedUserId))
                                .and(vehicleAuth.vehicle.id.eq(vehicleId))
                                .and(vehicleAuth.vehicle.companyId.eq(companyId))
                                .and(vehicleAuth.role.in(userRole, UserRole.STANDARD))
                                .and(vehicleAuth.isDeleted.isFalse())
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                ).fetchOne();

        return vAuthority;
    }

    @Override
    public VehicleAuthority update(VehicleAuthority vehicleAuthority) {
        return vAuthRepo.save(vehicleAuthority);
    }

    @Override
    public void deleteUser(VehicleAuthority vehicleAuthority) {
        vehicleAuthority.setDeleted(true);
        vAuthRepo.save(vehicleAuthority);
    }

    @Override
    public VehicleAuthority exists(Long vehicleId, Long companyId, Long userId) {
        QVehicleAuthority vehicleAuth = QVehicleAuthority.vehicleAuthority;
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        VehicleAuthority vAuthority = queryFactory.selectFrom(vehicleAuth)
                .where(
                        vehicleAuth.isDeleted.isFalse()
                                .and(vehicleAuth.vehicle.isDeleted.isFalse())
                                .and(vehicleAuth.vehicle.id.eq(vehicleId))
                                .and(vehicleAuth.userId.eq(userId))
                                .and(vehicle.companyId.eq(companyId))
                )
                .fetchOne();

        return vAuthority;
    }


}
