package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.Group;

public interface GroupService {
    Group create(Group group);

    boolean existsSameName(String name);
}
