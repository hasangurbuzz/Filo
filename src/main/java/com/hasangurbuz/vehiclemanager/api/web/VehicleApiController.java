package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleApiController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMapper vehicleMapper;

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
    public ResponseEntity<VehicleDTO> vehicleAddPost(VehicleDTO vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle = vehicleService.addVehicle(vehicle);
        vehicleDto = vehicleMapper.toDto(vehicle);
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<Void> vehicleDeleteIdPost() {
        return VehicleApi.super.vehicleDeleteIdPost();
    }

    @Override
    public ResponseEntity<VehicleDTO> vehicleUpdateIdPost(String id, VehicleDTO vehicleDto) {
        Vehicle oldVehicle = vehicleMapper.toEntity(vehicleDto);

        Vehicle updatedVehicle = vehicleService.updateVehicle(Long.valueOf(id), oldVehicle);
        vehicleDto = vehicleMapper.toDto(updatedVehicle);
        return ResponseEntity.ok(vehicleDto);
    }



}
