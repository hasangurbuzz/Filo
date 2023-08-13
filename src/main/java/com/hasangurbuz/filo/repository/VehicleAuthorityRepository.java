package com.hasangurbuz.filo.repository;

import com.hasangurbuz.filo.domain.VehicleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleAuthorityRepository extends JpaRepository<VehicleAuthority, Long> {
}
