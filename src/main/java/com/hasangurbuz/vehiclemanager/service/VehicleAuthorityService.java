package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.openapitools.model.PageRequestDTO;

public interface VehicleAuthorityService {
    VehicleAuthority create(VehicleAuthority vehicleAuthority);

    VehicleAuthority find(Long companyId, Long userId, Long vehicleId);

    VehicleAuthority update(VehicleAuthority vehicleAuthority);

    PagedResults<VehicleAuthority> searchByUserId(Long companyId, Long userId, PageRequestDTO pageRequest);

    PagedResults<VehicleAuthority> searchByVehicleId(Long companyId, Long vehicleId, PageRequestDTO pageRequest);

    void delete(VehicleAuthority vehicleAuthority);

}
