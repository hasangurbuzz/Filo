package com.hasangurbuz.filo.service;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.GroupAuthority;

import java.util.List;

public interface GroupAuthorityService {

    GroupAuthority create(GroupAuthority groupAuthority);

    GroupAuthority get(Long userId, Group group);

    List<GroupAuthority> searchByUserId(Long companyId, Long userId);

}
