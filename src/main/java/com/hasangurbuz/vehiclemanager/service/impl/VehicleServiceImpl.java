package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.repository.VehicleRepository;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.OffsetDateTime;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VehicleAuthorityService vAuthService;

    @Override
    public Vehicle create(Vehicle vehicle) {
        vehicle.setIsDeleted(false);
        vehicle.setCreationDate(OffsetDateTime.now());
        vehicle = vehicleRepo.save(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public void delete(Vehicle vehicle) {
        vehicle.setIsDeleted(true);
        vehicleRepo.save(vehicle);
    }

    @Override
    public boolean existsPlateNumber(Long companyId, String plateNumber) {
        QVehicle vehicle = QVehicle.vehicle;
        JPAQuery<Vehicle> query = new JPAQuery(entityManager);
        long vehicleCount = query.select(vehicle)
                .from(vehicle)
                .where(
                        vehicle.companyId.eq(companyId)
                                .and(vehicle.numberPlate.eq(plateNumber))
                                .and(vehicle.isDeleted.isFalse())
                )
                .fetchCount();

        return vehicleCount > 0;
    }

    @Override
    public boolean existsChassisNumber(Long companyId, String chassisNumber) {
        QVehicle vehicle = QVehicle.vehicle;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        long vehicleCount = query
                .selectFrom(vehicle)
                .where(
                        vehicle.companyId.eq(companyId)
                                .and(vehicle.chassisNumber.eq(chassisNumber))
                                .and(vehicle.isDeleted.isFalse())
                )
                .fetchCount();

        return vehicleCount > 0;
    }
}
