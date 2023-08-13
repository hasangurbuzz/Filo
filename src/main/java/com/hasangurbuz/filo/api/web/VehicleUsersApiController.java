package com.hasangurbuz.filo.api.web;

import com.hasangurbuz.filo.api.ApiContext;
import com.hasangurbuz.filo.api.ApiException;
import com.hasangurbuz.filo.api.ApiValidator;
import com.hasangurbuz.filo.api.mapper.VAuthUserMapper;
import com.hasangurbuz.filo.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.filo.api.mapper.VehicleMapper;
import com.hasangurbuz.filo.domain.UserRole;
import com.hasangurbuz.filo.domain.Vehicle;
import com.hasangurbuz.filo.domain.VehicleAuthority;
import com.hasangurbuz.filo.service.PagedResults;
import com.hasangurbuz.filo.service.VehicleAuthorityService;
import com.hasangurbuz.filo.service.VehicleService;
import org.openapitools.api.VehicleUsersApi;
import org.openapitools.model.PageRequestDTO;
import org.openapitools.model.VehicleUserDTO;
import org.openapitools.model.VehicleUserListRequestDTO;
import org.openapitools.model.VehicleUserListResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleUsersApiController implements VehicleUsersApi {

    @Autowired
    private VehicleAuthorityService vAuthService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleAuthorityMapper vAuthMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VAuthUserMapper vAuthUserMapper;


    @Override
    @Transactional
    public ResponseEntity<VehicleUserDTO> createUser(Long vehicleId, VehicleUserDTO vehicleUserDTO) {

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (vehicleUserDTO.getId() == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (vehicleUserDTO.getRole() == null) {
            throw ApiException.invalidInput("User role required");
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(vehicleUserDTO.getId(), vehicle);

        if (userVAuthority != null) {
            throw ApiException.invalidInput("User exists");
        }

        userVAuthority.setVehicle(vehicle);
        userVAuthority.setUserId(vehicleUserDTO.getId());
        userVAuthority.setRole(vehicleMapper.toUserRole(vehicleUserDTO.getRole()));

        userVAuthority = vAuthService.create(userVAuthority);

        VehicleUserDTO response = vAuthUserMapper.toDto(userVAuthority);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserDTO> getUser(Long vehicleId, Long userId) {

        if (vehicleId == null) {
            throw ApiException.invalidInput("Vehicle id required");
        }

        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(userId, vehicle);

        if (userVAuthority == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleUserDTO response = vAuthUserMapper.toDto(userVAuthority);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserListResponseDTO> searchUser(Long vehicleId, VehicleUserListRequestDTO vehicleUserListRequestDTO) {

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserListRequestDTO == null) {
            vehicleUserListRequestDTO = new VehicleUserListRequestDTO();
        }

        PageRequestDTO pageRequest = ApiValidator.validatePageRequest(vehicleUserListRequestDTO.getPageRequest());

        PagedResults<VehicleAuthority> results = vAuthService
                .searchByVehicle(vehicle, pageRequest);

        List<VehicleUserDTO> resultItems = vAuthUserMapper.toDtoList(results.getItems());

        VehicleUserListResponseDTO response = new VehicleUserListResponseDTO();
        response.setItems(resultItems);
        response.setTotal(results.getTotal());

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(Long vehicleId, Long userId) {

        Vehicle vehicle = vehicleService.get(vehicleId, ApiContext.get().getCompanyId());

        if (vehicle == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getUserId(), vehicle);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + vehicleId);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (currentUserAuth.getUserId() == userId) {
            throw ApiException.accessDenied();
        }

        if (vehicleId == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(userId, vehicle);

        if (userVAuthority == null) {
            throw ApiException.notFound("Not found : " + userId);
        }

        vAuthService.delete(userVAuthority);

        return ResponseEntity.ok().build();
    }
}
