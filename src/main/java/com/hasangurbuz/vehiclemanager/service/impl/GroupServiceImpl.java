package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.Group;
import com.hasangurbuz.vehiclemanager.repository.GroupRepository;
import com.hasangurbuz.vehiclemanager.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }
}
