package com.hasangurbuz.filo.api.web;

import com.hasangurbuz.filo.api.ApiContext;
import com.hasangurbuz.filo.api.ApiException;
import com.hasangurbuz.filo.api.ApiValidator;
import com.hasangurbuz.filo.api.mapper.GroupMapper;
import com.hasangurbuz.filo.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.filo.api.mapper.VehicleMapper;
import com.hasangurbuz.filo.domain.*;
import com.hasangurbuz.filo.service.GroupAuthorityService;
import com.hasangurbuz.filo.service.GroupService;
import com.hasangurbuz.filo.service.VehicleAuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.GroupApi;
import org.openapitools.model.GroupCreateRequestDTO;
import org.openapitools.model.GroupDTO;
import org.openapitools.model.GroupNodeDTO;
import org.openapitools.model.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private VehicleAuthorityMapper vAuthMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

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
            Group parentGroup = groupService.get(ApiContext.get().getCompanyId(), groupCreateRequestDTO.getParentId());

            if (parentGroup == null) {
                throw ApiException.notFound("Not found parent : " + groupCreateRequestDTO.getParentId());
            }

            GroupAuthority parentGroupAuth = groupAuthService
                    .get(ApiContext.get().getUserId(), parentGroup);

            if (parentGroupAuth == null) {
                throw ApiException.notFound("Not found parent : " + groupCreateRequestDTO.getParentId());
            }

            group.setParentGroup(parentGroup);
        }

        group.setName(groupName);
        group.setCompanyId(ApiContext.get().getCompanyId());
        group = groupService.create(group);

        GroupAuthority groupAuthority = new GroupAuthority();
        groupAuthority.setUserId(ApiContext.get().getUserId());
        groupAuthority.setGroup(group);

        groupAuthService.create(groupAuthority);

        GroupDTO response = groupMapper.toDto(group);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<GroupNodeDTO>> getGroupTree() {
        PageRequestDTO request = new PageRequestDTO();
        request.setFrom(0);
        request.setSize(Integer.MAX_VALUE);
        request = ApiValidator.validatePageRequest(request);

        List<VehicleAuthority> assignedVAuthorities = vAuthService
                .searchByUserId(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), request)
                .getItems();

        List<Vehicle> assignedVehicles = vAuthMapper.toVehicleList(assignedVAuthorities);

        List<GroupAuthority> assignedGroupAuths = groupAuthService
                .searchByUserId(ApiContext.get().getCompanyId(), ApiContext.get().getUserId());

        List<Group> assignedGroups = new ArrayList<>();
        for (GroupAuthority g : assignedGroupAuths) {
            assignedGroups.add(g.getGroup());
        }

        for (Group g : assignedGroups) {
            List<VehicleAuthority> vehicleAuths = vAuthService.searchByGroup(ApiContext.get().getUserId(), g);
            List<Vehicle> vehicles = vAuthMapper.toVehicleList(vehicleAuths);
            for (Vehicle v : assignedVehicles) {
                if (!assignedVehicles.contains(v)) {
                    assignedVehicles.add(v);
                }
            }
        }

        Map<Long, List<Vehicle>> vehiclesByGroup = new HashMap<>();
        Map<Long, Group> visibleGroups = new HashMap<>();

        for (Vehicle vehicle : assignedVehicles) {

            Group group = vehicle.getGroup();
            Group temp = group;
            while (temp != null) {
                visibleGroups.put(temp.getId(), temp);
                temp = temp.getParentGroup();
            }

            List<Vehicle> vehicles = vehiclesByGroup.get(group.getId());
            if (vehicles == null) {
                vehicles = new ArrayList();
                vehiclesByGroup.put(group.getId(), vehicles);
            }

            vehicles.add(vehicle);
        }

        for (Group assignedGroup : assignedGroups) {

            Group temp = assignedGroup;
            while (temp != null) {
                visibleGroups.put(temp.getId(), temp);
                temp = temp.getParentGroup();
            }

            List<VehicleAuthority> vAuthorities = vAuthService.searchByGroup(ApiContext.get().getUserId(), assignedGroup);
            List<Vehicle> assignedGroupVehicles = vAuthMapper.toVehicleList(vAuthorities);

            if (assignedGroupVehicles == null) {
                List<Vehicle> vehicles = vehiclesByGroup.get(assignedGroup.getId());
                if (vehicles == null) {
                    vehicles = new ArrayList();
                    vehiclesByGroup.put(assignedGroup.getId(), vehicles);
                }
                for (Vehicle v : assignedGroupVehicles) {
                    if (!vehicles.contains(v)) {
                        vehicles.add(v);
                    }
                }
            }
        }

        Map<Long, GroupNodeDTO> nodeTree = new HashMap<>();
        for (Map.Entry<Long, Group> entry : visibleGroups.entrySet()) {
            Group visibleGroup = entry.getValue();

            GroupNodeDTO dto = new GroupNodeDTO();
            dto.setGroup(groupMapper.toDto(visibleGroup));


            List<Vehicle> myVehicles = vehiclesByGroup.get(visibleGroup.getId());
            if (myVehicles != null) {
                for (Vehicle v : myVehicles) {
                    dto.addVehiclesItem(vehicleMapper.toDto(v));
                }
            }
            nodeTree.put(visibleGroup.getId(), dto);
        }

        List<GroupNodeDTO> response = new ArrayList<>();


        for (Map.Entry<Long, Group> entry : visibleGroups.entrySet()) {
            Group visibleGroup = entry.getValue();

            if (visibleGroup.getParentGroup() != null) {
                Long parentId = visibleGroup.getParentGroup().getId();

                GroupNodeDTO parentNode = nodeTree.get(parentId);
                parentNode.addChildrenItem(nodeTree.get(visibleGroup.getId()));
            } else {
                response.add(nodeTree.get(visibleGroup.getId()));
            }
        }

        return ResponseEntity.ok(response);

    }

}

