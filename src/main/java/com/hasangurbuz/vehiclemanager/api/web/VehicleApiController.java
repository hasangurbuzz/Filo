package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.UserRoleDTO;
import org.openapitools.model.VehicleCreateRequestDTO;
import org.openapitools.model.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class VehicleApiController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public ResponseEntity<VehicleDTO> create(VehicleCreateRequestDTO vehicleCreateRequestDTO) {
        if (ApiContext.get().getUserRole() != UserRoleDTO.COMPANYADMIN) {
            Vehicle vehicle = new Vehicle();
            vehicle.setBrand(vehicleCreateRequestDTO.getBrand());
            vehicle.setTag(vehicleCreateRequestDTO.getTag());
            vehicle.setModel(vehicleCreateRequestDTO.getModel());
            vehicle.setModelYear(vehicleCreateRequestDTO.getModelYear());
            vehicle.setChassisNumber(vehicleCreateRequestDTO.getChassisNumber());
            vehicle.setNumberPlate(vehicleCreateRequestDTO.getNumberPlate());
            vehicle.setCompanyId(ApiContext.get().getCompanyId());

            vehicle = vehicleService.create(vehicle);
            VehicleDTO dto = vehicleMapper.toDto(vehicle);
            return ResponseEntity.ok(dto);
        }

        throw ApiException.accessDenied();
    }

    @Override
    public ResponseEntity<VehicleDTO> vehicleIdPost(String id) {
        VehicleDTO vehicleDto = vehicleMapper.toDto(vehicleService.getVehicleById(Long.valueOf(id)));
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<List<VehicleDTO>> vehiclePost() {
        List<Vehicle> vehicles = vehicleService.getVehicles();
        List<VehicleDTO> vehicleDtos = vehicleMapper.toDtoList(vehicles);
        return ResponseEntity.ok(vehicleDtos);
    }


    @Override
    public ResponseEntity<VehicleDTO> vehicleUpdateIdPost(String id, VehicleDTO vehicleDto) {
        Vehicle oldVehicle = vehicleMapper.toEntity(vehicleDto);

        Vehicle updatedVehicle = vehicleService.updateVehicle(Long.valueOf(id), oldVehicle);
        vehicleDto = vehicleMapper.toDto(updatedVehicle);
        return ResponseEntity.ok(vehicleDto);
    }


}
