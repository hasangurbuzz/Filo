package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.ApiValidator;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.codehaus.plexus.util.StringUtils;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hasangurbuz.vehiclemanager.api.ApiConstant.*;

@RestController
public class VehicleApiController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleAuthorityService vAuthService;

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

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(vehicleCreateRequestDTO.getBrand());
        vehicle.setTag(vehicleCreateRequestDTO.getTag());
        vehicle.setModel(vehicleCreateRequestDTO.getModel());
        vehicle.setModelYear(vehicleCreateRequestDTO.getModelYear());
        vehicle.setChassisNumber(vehicleCreateRequestDTO.getChassisNumber());
        vehicle.setNumberPlate(vehicleCreateRequestDTO.getNumberPlate());
        vehicle.setCompanyId(ApiContext.get().getCompanyId());

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
    public ResponseEntity<VehicleDTO> getById(Long id) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        VehicleDTO response = vAuthMapper.toDto(currentUserAuth);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> update(Long id, VehicleUpdateRequestDTO vehicleUpdateRequestDTO) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
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

        boolean plateNumberExists = vehicleService
                .existsPlateNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getNumberPlate());

        if (plateNumberExists) {
            throw ApiException.invalidInput("Plate number exists");
        }

        boolean chassisNumberExists = vehicleService
                .existsChassisNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getChassisNumber());

        if (chassisNumberExists) {
            throw ApiException.invalidInput("Chassis number exists");
        }

        Vehicle vehicle = currentUserAuth.getVehicle();
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
    public ResponseEntity<Void> delete(Long id) {
        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        vehicleService.delete(currentUserAuth.getVehicle());

        return ResponseEntity.ok().build();
    }

}
