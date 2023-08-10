package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.QVehicle;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.repository.VehicleRepository;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

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
        vehicle = vehicleRepo.save(vehicle);
        return vehicle;
    }


    @Override
    public Vehicle update(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        VehicleAuthority vAuthority = vAuthService.getByVehicleId(id);
        if (vAuthority == null) {
            return null;
        }
        return vAuthority.getVehicle();
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
                                .and(plateNumberIs(plateNumber))
                                .and(isDeleted(false))
                )
                .fetchCount();

        return vehicleCount > 0;
    }

    @Override
    public boolean existsChassisNumber(Long companyId, String chassisNumber) {
        QVehicle vehicle = QVehicle.vehicle;
        JPAQuery<Vehicle> query = new JPAQuery(entityManager);
        long vehicleCount = query
                .select(vehicle)
                .from(vehicle)
                .where(
                        vehicle.companyId.eq(companyId)
                                .and(vehicle.chassisNumber.eq(chassisNumber))
                                .and(vehicle.isDeleted.isFalse())
                )
                .fetchCount();

        return vehicleCount > 0;
    }

    private BooleanExpression plateNumberIs(String plateNumber) {
        return QVehicle
                .vehicle.numberPlate.eq(plateNumber);
    }

    private BooleanExpression chassisNumberIs(String chassisNumber) {
        return QVehicle
                .vehicle.chassisNumber.eq(chassisNumber);
    }

    private BooleanExpression isDeleted(boolean isDeleted) {
        return QVehicle
                .vehicle.isDeleted.eq(isDeleted);
    }


}
