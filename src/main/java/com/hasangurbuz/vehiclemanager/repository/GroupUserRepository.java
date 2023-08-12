package com.hasangurbuz.vehiclemanager.repository;

import com.hasangurbuz.vehiclemanager.domain.GroupAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupAuthority, Long> {
}
