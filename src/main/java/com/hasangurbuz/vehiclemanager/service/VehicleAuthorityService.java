package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import org.openapitools.model.VehicleListRequestDTO;

public interface VehicleAuthorityService {
    PagedResults<Vehicle> search(VehicleListRequestDTO request);
}
