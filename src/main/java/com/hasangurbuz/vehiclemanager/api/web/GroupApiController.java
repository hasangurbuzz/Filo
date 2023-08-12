package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.mapper.GroupMapper;
import com.hasangurbuz.vehiclemanager.domain.Group;
import com.hasangurbuz.vehiclemanager.domain.GroupVehicleAuthority;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.service.GroupService;
import com.hasangurbuz.vehiclemanager.service.GroupVAuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.GroupApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupApiController implements GroupApi {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupVAuthorityService groupVAuthService;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    @Transactional
    public ResponseEntity<GroupDTO> createGroup(GroupCreateRequestDTO groupCreateRequestDTO) {
        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        String groupName = groupCreateRequestDTO.getName();

        if (StringUtils.isBlank(groupName)) {
            throw ApiException.invalidInput("Invalid name");
        }

        groupName = StringUtils.normalizeSpace(groupName);

        if (groupService.existsSameName(groupName)) {
            throw ApiException.invalidInput("Name is taken");
        }

        Group group = new Group();
        group.setName(groupName);

        group = groupService.create(group);
        GroupVehicleAuthority groupVAuth = new GroupVehicleAuthority();
        groupVAuth.setUserRole(ApiContext.get().getUserRole());
        groupVAuth.setGroup(group);

        groupVAuth = groupVAuthService.create(groupVAuth);

        group = groupVAuth.getGroup();

        GroupDTO response = groupMapper.toDto(group);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GroupDTO> createGroupInGroup(Long id) {
        return GroupApi.super.createGroupInGroup(id);
    }

    @Override
    public ResponseEntity<Void> deleteGroup(Long id) {
        return GroupApi.super.deleteGroup(id);
    }

    @Override
    public ResponseEntity<GroupDTO> getGroup(Long id) {
        return GroupApi.super.getGroup(id);
    }

    @Override
    public ResponseEntity<GroupListResponseDTO> searchGroup(GroupListRequestDTO groupListRequestDTO) {
        
        return GroupApi.super.searchGroup(groupListRequestDTO);
    }

    @Override
    public ResponseEntity<GroupDTO> updateGroup(Long id, GroupUpdateRequestDTO groupUpdateRequestDTO) {
        return GroupApi.super.updateGroup(id, groupUpdateRequestDTO);
    }
}
