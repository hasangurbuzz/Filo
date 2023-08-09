package com.hasangurbuz.vehiclemanager.controller;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.openapitools.api.VehicleApi;
import org.openapitools.model.VehicleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleController implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public ResponseEntity<VehicleDto> vehicleIdPost(String id) {
        VehicleDto vehicleDto = vehicleMapper.toDto(vehicleService.getVehicleById(Long.valueOf(id)));
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<List<VehicleDto>> vehiclePost() {
        List<Vehicle> vehicles = vehicleService.getVehicles();
        List<VehicleDto> vehicleDtos = vehicleMapper.toDtoList(vehicles);
        return ResponseEntity.ok(vehicleDtos);
    }

    @Override
    public ResponseEntity<VehicleDto> vehicleAddPost(VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle = vehicleService.addVehicle(vehicle);
        vehicleDto = vehicleMapper.toDto(vehicle);
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<VehicleDto> vehicleUpdateIdPost(String id, VehicleDto vehicleDto) {
        Vehicle oldVehicle = vehicleMapper.toEntity(vehicleDto);
        Vehicle updatedVehicle = vehicleService.updateVehicle(Long.valueOf(id), oldVehicle);
        vehicleDto = vehicleMapper.toDto(updatedVehicle);
        return ResponseEntity.ok(vehicleDto);
    }

    @Override
    public ResponseEntity<Void> vehicleDeleteIdPost(String id) {
        vehicleService.deleteVehicle(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }

}
