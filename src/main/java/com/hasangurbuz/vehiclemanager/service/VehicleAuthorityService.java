package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.openapitools.model.VehicleListRequestDTO;

public interface VehicleAuthorityService {
    PagedResults<VehicleAuthority> search(VehicleListRequestDTO request);

    VehicleAuthority create(VehicleAuthority vehicleAuthority);
}
