package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.GroupVehicleAuthority;
import com.hasangurbuz.vehiclemanager.repository.GroupVAuthorityRepository;
import com.hasangurbuz.vehiclemanager.service.GroupVAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupVAuthorityServiceImpl implements GroupVAuthorityService {

    @Autowired
    private GroupVAuthorityRepository groupVAuthRepo;

    @Override
    public GroupVehicleAuthority create(GroupVehicleAuthority groupVAuthority) {
        groupVAuthority.setDeleted(false);
        return groupVAuthRepo.save(groupVAuthority);
    }
}
