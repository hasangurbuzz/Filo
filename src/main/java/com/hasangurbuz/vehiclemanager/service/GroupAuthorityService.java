package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.GroupAuthority;

public interface GroupAuthorityService {

    GroupAuthority create(GroupAuthority groupAuthority);

    GroupAuthority find(Long companyId, Long userId, Long groupId);

}
