package com.hasangurbuz.filo.service;

import com.hasangurbuz.filo.domain.Group;

public interface GroupService {
    Group create(Group group);

    Group get(Long companyId, Long groupId);

    boolean existsSameName(Long companyId, String name);

}
