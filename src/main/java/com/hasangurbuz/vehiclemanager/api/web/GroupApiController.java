package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.mapper.GroupMapper;
import com.hasangurbuz.vehiclemanager.domain.Group;
import com.hasangurbuz.vehiclemanager.domain.GroupAuthority;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.service.GroupAuthorityService;
import com.hasangurbuz.vehiclemanager.service.GroupService;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
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
    private GroupAuthorityService groupAuthService;

    @Autowired
    private VehicleAuthorityService vAuthService;

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

        if (groupService.existsSameName(ApiContext.get().getCompanyId(), groupName)) {
            throw ApiException.invalidInput("Name is taken");
        }

        Group group = new Group();

        if (groupCreateRequestDTO.getParentId() != null) {
            GroupAuthority parentGroupAuth = groupAuthService
                    .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), groupCreateRequestDTO.getParentId());

            if (parentGroupAuth == null) {
                throw ApiException.notFound("Not found parent : " + groupCreateRequestDTO.getParentId());
            }

            Group parentGroup = parentGroupAuth.getGroup();

            if (parentGroup == null) {
                throw ApiException.notFound("Not found parent : " + groupCreateRequestDTO.getParentId());
            }

            group.setParentGroup(parentGroup);
        }

        group.setName(groupName);
        group.setCompanyId(ApiContext.get().getCompanyId());
        group = groupService.create(group);

        GroupAuthority groupAuthority = new GroupAuthority();
        groupAuthority.setUserId(ApiContext.get().getUserId());
        groupAuthority.setRole(ApiContext.get().getUserRole());
        groupAuthority.setGroup(group);

        groupAuthService.create(groupAuthority);

        GroupDTO response = groupMapper.toDto(group);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GroupDTO> getGroup(Long id) {
        GroupAuthority currentUserAuth = groupAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null){
            throw ApiException.notFound("Not found id : " + id);
        }

        Group group = currentUserAuth.getGroup();


        return GroupApi.super.getGroup(id);
    }

    @Override
    public ResponseEntity<GroupListResponseDTO> searchGroup(GroupListRequestDTO groupListRequestDTO) {
        PagedResults<VehicleAuthority> vAuth = vAuthService.searchByUserId(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), groupListRequestDTO.getPageRequest());



        return GroupApi.super.searchGroup(groupListRequestDTO);
    }
}
