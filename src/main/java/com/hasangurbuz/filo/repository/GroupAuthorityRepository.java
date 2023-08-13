package com.hasangurbuz.filo.repository;

import com.hasangurbuz.filo.domain.GroupAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupAuthorityRepository extends JpaRepository<GroupAuthority, Long> {
}
