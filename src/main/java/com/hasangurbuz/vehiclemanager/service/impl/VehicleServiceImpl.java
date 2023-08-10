package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
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

    @Override
    public Vehicle create(Vehicle vehicle) {
        vehicle.setIsDeleted(false);
        vehicle = vehicleRepo.save(vehicle);
        return vehicle;
    }


    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle storedVehicle = getVehicleById(id);
        if (storedVehicle != null) {
            storedVehicle.setBrand(vehicle.getBrand());
            storedVehicle.setChassisNumber(vehicle.getChassisNumber());
            storedVehicle.setModel(vehicle.getModel());
            storedVehicle.setModelYear(vehicle.getModelYear());
            storedVehicle.setNumberPlate(vehicle.getNumberPlate());
            storedVehicle.setTag(vehicle.getTag());

            return vehicleRepo.save(storedVehicle);
        }
        return null;
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepo
                .findVehicleByIdAndCompanyIdAndIsDeletedFalse(id, ApiContext.get().getCompanyId());
        return vehicle;
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        if (vehicle != null) {
            vehicle.setIsDeleted(true);
            vehicleRepo.save(vehicle);
        }

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
