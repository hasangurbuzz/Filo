package com.hasangurbuz.vehiclemanager.repository;

import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleAuthorityRepository extends JpaRepository<VehicleAuthority, Long> {
}
