package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.dto.RequestContext;
import com.hasangurbuz.vehiclemanager.dto.VehicleDto;
import com.hasangurbuz.vehiclemanager.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.repository.VehicleRepository;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private RequestContext requestContext;

    @Override
    public Vehicle addVehicle(VehicleDto vehicleRequest) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequest);
        vehicle.setCompanyId(requestContext.getUser().getCompanyId());
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Long vehicleId) {

    }
}
