package com.hasangurbuz.vehiclemanager.service;

import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import org.openapitools.model.VehicleListRequestDTO;
import org.openapitools.model.VehicleUserListRequestDTO;

public interface VehicleAuthorityService {
    PagedResults<VehicleAuthority> search(Long companyId, Long userId, UserRole userRole, VehicleListRequestDTO request);

    VehicleAuthority create(VehicleAuthority vehicleAuthority);

    VehicleAuthority getByVehicleId(Long vehicleId, Long companyId, Long userId, UserRole userRole);

    void delete(VehicleAuthority vehicleAuthority);

    PagedResults<VehicleAuthority> searchUser(Long vehicleId, Long companyId, Long userId, UserRole userRole, VehicleUserListRequestDTO request);

    VehicleAuthority getByVehicleAndUserId(Long vehicleId, Long companyId, Long userId, UserRole userRole, Long requestedUserId);

    VehicleAuthority update(VehicleAuthority vehicleAuthority);

    void deleteUser(VehicleAuthority vehicleAuthority);

    VehicleAuthority exists(Long vehicleId, Long companyId, Long userId);

}
