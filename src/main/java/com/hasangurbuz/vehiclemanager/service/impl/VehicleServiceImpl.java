package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.repository.VehicleRepository;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle create(Vehicle vehicle) {
        vehicle.setIsDeleted(false);
        vehicle = vehicleRepository.save(vehicle);
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

            return vehicleRepository.save(storedVehicle);
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = vehicleRepository
                .findVehicleByCompanyIdAndIsDeletedFalse(ApiContext.get().getCompanyId());
        return vehicles;
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository
                .findVehicleByIdAndCompanyIdAndIsDeletedFalse(id, ApiContext.get().getCompanyId());
        return vehicle;
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        if (vehicle != null) {
            vehicle.setIsDeleted(true);
            vehicleRepository.save(vehicle);
        }
    }

    @Override
    public boolean existsPlateNumber(Long companyId, String plateNumber) {
        Specification spec = Specification
                .where(
                        companyId(companyId)
                                .and(isDeleted(false)
                                        .and(plateNumber(plateNumber)))
                );
        return vehicleRepository.count(spec) > 0;
    }

    @Override
    public boolean existsChassisNumber(Long companyId, String chassisNumber) {
        Specification spec = Specification.where(
                companyId(companyId)
                        .and(isDeleted(false))
                        .and(chassisNumber(chassisNumber))
        );
        return vehicleRepository.count(spec) > 0;
    }

    public static Specification<Vehicle> isDeleted(boolean isDeleted) {
        return new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
            }
        };
    }

    public static Specification<Vehicle> plateNumber(String plateNumber) {
        return new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("plateNumber"), plateNumber);
            }
        };
    }

    public static Specification<Vehicle> companyId(Long companyId) {
        return new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("companyId"), companyId);
            }
        };
    }

    public static Specification<Vehicle> chassisNumber(String chassisNumber) {
        return new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("chassisNumber"), chassisNumber);
            }
        };
    }
}

