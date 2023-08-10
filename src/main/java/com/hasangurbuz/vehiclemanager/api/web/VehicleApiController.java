package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiConstant;
import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
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
import org.openapitools.model.SortDTO;
import org.openapitools.model.VehicleCreateRequestDTO;
import org.openapitools.model.VehicleDTO;
import org.openapitools.model.VehicleListRequestDTO;
import org.openapitools.model.VehicleListResponseDTO;
import org.openapitools.model.VehicleUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

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
        vehicle.setCreationDate(OffsetDateTime.now());

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
    public ResponseEntity<VehicleListResponseDTO> search(VehicleListRequestDTO vehicleListRequestDTO) {
        if (vehicleListRequestDTO == null) {
            vehicleListRequestDTO = new VehicleListRequestDTO();
        }
        if (vehicleListRequestDTO.getFrom() == null) {
            vehicleListRequestDTO.setFrom(ApiConstant.PAGE_OFFSET);
        }
        if (vehicleListRequestDTO.getSize() == null) {
            vehicleListRequestDTO.setSize(ApiConstant.PAGE_LIMIT);
        }
        if (vehicleListRequestDTO.getSort() == null) {
            vehicleListRequestDTO.setSort(new SortDTO());
        }
        if (vehicleListRequestDTO.getSort().getProperty() == null) {
            vehicleListRequestDTO.getSort().setProperty(SortDTO.PropertyEnum.CREATION_DATE);
        }
        if (vehicleListRequestDTO.getSort().getDirection() == null) {
            vehicleListRequestDTO.getSort().setDirection(SortDTO.DirectionEnum.ASC);
        }

        PagedResults<VehicleAuthority> vehiclePagedResults = vAuthService.search(vehicleListRequestDTO);
        List<VehicleAuthority> vAuthorityList = vehiclePagedResults.getItems();

        VehicleListResponseDTO response = new VehicleListResponseDTO();
        response.setItems(vAuthMapper.toDtoList(vAuthorityList));
        response.setTotal(vehiclePagedResults.getTotal());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehicleDTO> getById(Long id) {
        VehicleAuthority vAuthority = vAuthService.getByVehicleId(id);

        if (vAuthority == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        VehicleDTO vehicle = vAuthMapper.toDto(vAuthority);
        return ResponseEntity.ok(vehicle);
    }

    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> update(Long id, VehicleUpdateRequestDTO vehicleUpdateRequestDTO) {
        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
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

        if (vehicleService.existsPlateNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getNumberPlate())) {
            throw ApiException.invalidInput("Plate number exists");
        }

        if (vehicleService.existsChassisNumber(ApiContext.get().getCompanyId(), vehicleUpdateRequestDTO.getChassisNumber())) {
            throw ApiException.invalidInput("Chassis number exists");
        }


        VehicleAuthority vAuthority = vAuthService.getByVehicleId(id);

        if (vAuthority == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        Vehicle vehicle = vAuthority.getVehicle();
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
        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        VehicleAuthority vAuthority = vAuthService.getByVehicleId(id);

        if (vAuthority == null) {
            throw ApiException.notFound("Not found id : " + id);
        }

        vehicleService.delete(vAuthority.getVehicle());
        return ResponseEntity.ok().build();
    }

}
