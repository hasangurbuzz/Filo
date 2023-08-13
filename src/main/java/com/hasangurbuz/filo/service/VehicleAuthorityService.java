package com.hasangurbuz.filo.service;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.Vehicle;
import com.hasangurbuz.filo.domain.VehicleAuthority;
import org.openapitools.model.PageRequestDTO;

import java.util.List;

public interface VehicleAuthorityService {
    VehicleAuthority create(VehicleAuthority vehicleAuthority);

    VehicleAuthority find(Long userId, Vehicle vehicle);

    VehicleAuthority update(VehicleAuthority vehicleAuthority);

    PagedResults<VehicleAuthority> searchByUserId(Long companyId, Long userId, PageRequestDTO pageRequest);

    PagedResults<VehicleAuthority> searchByVehicle(Vehicle vehicle, PageRequestDTO pageRequest);

    List<VehicleAuthority> searchByGroup(Long userId, Group group);

    void delete(VehicleAuthority vehicleAuthority);

}
