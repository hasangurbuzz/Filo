package com.hasangurbuz.vehiclemanager.repository;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findVehicleByCompanyIdAndIsDeletedFalse(Long companyId);

    Vehicle findVehicleByIdAndCompanyIdAndIsDeletedFalse(Long id, Long companyId);

}
