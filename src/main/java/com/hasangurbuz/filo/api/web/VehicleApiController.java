package com.hasangurbuz.filo.api.web;

import com.hasangurbuz.filo.api.ApiContext;
import com.hasangurbuz.filo.api.ApiException;
import com.hasangurbuz.filo.api.ApiValidator;
import com.hasangurbuz.filo.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.filo.api.mapper.VehicleMapper;
import com.hasangurbuz.filo.domain.*;
import com.hasangurbuz.filo.service.*;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hasangurbuz.filo.api.ApiConstant.*;

@RestController
public class VehicleApiController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleAuthorityService vAuthService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupAuthorityService groupAuthService;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleAuthorityMapper vAuthMapper;


    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> create(VehicleCreateRequestDTO vehicleCreateRequestDTO) {

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleCreateRequestDTO.getGroupId() == null) {
            throw ApiException.invalidInput("Group id required");
        }

        if (StringUtils.isBlank(vehicleCreateRequestDTO.getBrand())) {
            throw ApiException.invalidInput("Brand required");
        }

        if (StringUtils.isBlank(vehicleCreateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Number plate required");
        }

        if (StringUtils.isBlank(vehicleCreateRequestDTO.getModel())) {
            throw ApiException.invalidInput("Model required");
        }

        if (vehicleCreateRequestDTO.getModelYear() == null) {
            throw ApiException.invalidInput("Model year required");
        }

        if (vehicleCreateRequestDTO.getModelYear() < DATE_MIN.getYear()) {
            throw ApiException.invalidInput("Invalid model year");
        }

        if (vehicleCreateRequestDTO.getModelYear() > DATE_MAX.getYear()) {
            throw ApiException.invalidInput("Invalid model year");
        }

        if (vehicleCreateRequestDTO.getBrand().length() > BRAND_LENGTH_MAX) {
            throw ApiException.invalidInput("Brand max length : " + BRAND_LENGTH_MAX);
        }

        if (!ApiValidator.isPlateNumberValid(vehicleCreateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Invalid plate number");
        }

        if (!ApiValidator.isChassisValid(vehicleCreateRequestDTO.getChassisNumber())) {
            throw ApiException.invalidInput("Invalid chassis number");
        }

        if (vehicleService.existsPlateNumber(ApiContext.get().getCompanyId(), vehicleCreateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Plate number exists");
        }

        if (vehicleService.existsChassisNumber(ApiContext.get().getCompanyId(), vehicleCreateRequestDTO.getChassisNumber())) {
            throw ApiException.invalidInput("Chassis number exists");
        }

        Group group = groupService.get(ApiContext.get().getCompanyId(), vehicleCreateRequestDTO.getGroupId());

        if (group == null) {
            throw ApiException.notFound("Not found group : " + vehicleCreateRequestDTO.getGroupId());
        }

        GroupAuthority groupAuthority = groupAuthService.get(ApiContext.get().getUserId(), group);

        if (groupAuthority == null) {
            throw ApiException.notFound("Not found group : " + vehicleCreateRequestDTO.getGroupId());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleCreateRequestDTO.getBrand());
        vehicle.setTag(vehicleCreateRequestDTO.getTag());
        vehicle.setModel(vehicleCreateRequestDTO.getModel());
        vehicle.setModelYear(vehicleCreateRequestDTO.getModelYear());
        vehicle.setChassisNumber(StringUtils.upperCase(vehicleCreateRequestDTO.getChassisNumber()));
        vehicle.setNumberPlate(StringUtils.upperCase(vehicleCreateRequestDTO.getNumberPlate()));
        vehicle.setCompanyId(ApiContext.get().getCompanyId());
        vehicle.setGroup(group);


        vehicle = vehicleService.create(vehicle);

        VehicleAuthority authority = new VehicleAuthority();
        authority.setVehicle(vehicle);
        authority.setRole(ApiContext.get().getUserRole());
        authority.setUserId(ApiContext.get().getUserId());
        vAuthService.create(authority);

        VehicleDTO dto = vehicleMapper.toDto(vehicle);
        return ResponseEntity.ok(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleListResponseDTO> search(VehicleListRequestDTO vehicleListRequestDTO) {

        if (vehicleListRequestDTO == null) {
            vehicleListRequestDTO = new VehicleListRequestDTO();
        }

        PageRequestDTO pageRequest = ApiValidator.validatePageRequest(vehicleListRequestDTO.getPageRequest());

        PagedResults<VehicleAuthority> results = vAuthService
                .searchByUserId(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), pageRequest);

        VehicleListResponseDTO response = new VehicleListResponseDTO();
        List<VehicleDTO> items = vAuthMapper.toDtoList(results.getItems());
        response.setItems(items);
        response.setTotal(results.getTotal());

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleDTO> getById(Long vehicleId) {

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        GroupAuthority currentGroupAuth = groupAuthService.get(ApiContext.get().getUserId(), vehicle.getGroup());

        if (currentUserAuth == null && currentGroupAuth == null) {
            throw ApiException.invalidInput("Not found");
        }

        return ResponseEntity.ok(vehicleMapper.toDto(vehicle));
    }

    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> update(Long vehicleId, VehicleUpdateRequestDTO vehicleUpdateRequestDTO) {

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        GroupAuthority currentGroupAuth = groupAuthService.get(ApiContext.get().getUserId(), vehicle.getGroup());

        if (currentUserAuth == null && currentGroupAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (StringUtils.isBlank(vehicleUpdateRequestDTO.getBrand())) {
            throw ApiException.invalidInput("Brand required");
        }

        if (StringUtils.isBlank(vehicleUpdateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Number plate required");
        }

        if (StringUtils.isBlank(vehicleUpdateRequestDTO.getModel())) {
            throw ApiException.invalidInput("Model required");
        }

        if (vehicleUpdateRequestDTO.getModelYear() == null) {
            throw ApiException.invalidInput("Model year required");
        }

        if (!ApiValidator.isPlateNumberValid(vehicleUpdateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Invalid plate number");
        }

        if (!ApiValidator.isChassisValid(vehicleUpdateRequestDTO.getChassisNumber())) {
            throw ApiException.invalidInput("Invalid chassis number");
        }

        vehicleUpdateRequestDTO.setChassisNumber(StringUtils.upperCase(vehicleUpdateRequestDTO.getChassisNumber()));
        vehicleUpdateRequestDTO.setNumberPlate(StringUtils.upperCase(vehicleUpdateRequestDTO.getNumberPlate()));

        boolean plateNumberExists = false;
        boolean chassisNumberExists = false;

        if (vehicle.getNumberPlate() != vehicleUpdateRequestDTO.getNumberPlate()) {
            plateNumberExists = vehicleService
                    .existsPlateNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getNumberPlate());
        }

        if (plateNumberExists) {
            throw ApiException.invalidInput("Plate number exists");
        }

        if (vehicle.getChassisNumber() != vehicleUpdateRequestDTO.getChassisNumber())
            chassisNumberExists = vehicleService
                    .existsChassisNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getChassisNumber());

        if (chassisNumberExists) {
            throw ApiException.invalidInput("Chassis number exists");
        }

        vehicle.setBrand(vehicleUpdateRequestDTO.getBrand());
        vehicle.setTag(vehicleUpdateRequestDTO.getTag());
        vehicle.setNumberPlate(vehicleUpdateRequestDTO.getNumberPlate());
        vehicle.setChassisNumber(vehicleUpdateRequestDTO.getChassisNumber());
        vehicle.setModel(vehicleUpdateRequestDTO.getModel());
        vehicle.setModelYear(vehicleUpdateRequestDTO.getModelYear());

        vehicle = vehicleService.update(vehicle);

        return ResponseEntity.ok(vehicleMapper.toDto(vehicle));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> delete(Long vehicleId) {

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        GroupAuthority currentGroupAuth = groupAuthService.get(ApiContext.get().getUserId(), vehicle.getGroup());

        if (currentUserAuth == null && currentGroupAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        vehicleService.delete(vehicle);

        return ResponseEntity.ok().build();
    }

}
