package com.hasangurbuz.vehiclemanager.repository;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
