package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.ApiValidator;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleUserMapper;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import org.openapitools.api.VehicleUsersApi;
import org.openapitools.model.PageRequestDTO;
import org.openapitools.model.SortDTO;
import org.openapitools.model.VehicleUserDTO;
import org.openapitools.model.VehicleUserListRequestDTO;
import org.openapitools.model.VehicleUserListResponseDTO;
import org.openapitools.model.VehicleUserUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hasangurbuz.vehiclemanager.api.ApiConstant.PAGE_LIMIT;
import static com.hasangurbuz.vehiclemanager.api.ApiConstant.PAGE_OFFSET;
import static com.hasangurbuz.vehiclemanager.api.ApiConstant.SORT_PROPERTY;
import static org.openapitools.model.SortDTO.DirectionEnum.ASC;

@RestController
public class VehicleUsersApiController implements VehicleUsersApi {

    @Autowired
    private VehicleAuthorityService vAuthService;

    @Autowired
    private VehicleAuthorityMapper vAuthMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleUserMapper vUserMapper;


    @Override
    @Transactional
    public ResponseEntity<VehicleUserDTO> createUser(Long id, VehicleUserDTO vehicleUserDTO) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserDTO.getId() == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (vehicleUserDTO.getRole() == null) {
            throw ApiException.invalidInput("User role required");
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(ApiContext.get().getCompanyId(), vehicleUserDTO.getId(), id);

        if (userVAuthority != null) {
            throw ApiException.invalidInput("User exists");
        }

        userVAuthority.setVehicle(currentUserAuth.getVehicle());
        userVAuthority.setUserId(vehicleUserDTO.getId());
        userVAuthority.setRole(vehicleMapper.toUserRole(vehicleUserDTO.getRole()));

        userVAuthority = vAuthService.create(userVAuthority);

        VehicleUserDTO response = vUserMapper.toDto(userVAuthority);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserDTO> getUser(Long id, Long userId) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (id == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(ApiContext.get().getCompanyId(), userId, id);

        if (userVAuthority == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        VehicleUserDTO response = vUserMapper.toDto(userVAuthority);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserListResponseDTO> searchUser(Long id, VehicleUserListRequestDTO vehicleUserListRequestDTO) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserListRequestDTO == null) {
            vehicleUserListRequestDTO = new VehicleUserListRequestDTO();
        }

        PageRequestDTO pageRequest = ApiValidator.validatePageRequest(vehicleUserListRequestDTO.getPageRequest());

        PagedResults<VehicleAuthority> results = vAuthService
                .searchByVehicleId(ApiContext.get().getCompanyId(), id, pageRequest);

        List<VehicleUserDTO> resultItems = vUserMapper.toDtoList(results.getItems());

        VehicleUserListResponseDTO response = new VehicleUserListResponseDTO();
        response.setItems(resultItems);
        response.setTotal(results.getTotal());

        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public ResponseEntity<VehicleUserDTO> updateUser(Long id, Long userId, VehicleUserUpdateRequestDTO vehicleUserUpdateRequestDTO) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserUpdateRequestDTO.getUserRole() == null) {
            throw ApiException.invalidInput("User role required");
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(ApiContext.get().getCompanyId(), userId, id);

        if (userVAuthority == null) {
            throw ApiException.notFound("Not found : " + userId);
        }

        userVAuthority.setRole(vehicleMapper.toUserRole(vehicleUserUpdateRequestDTO.getUserRole()));

        userVAuthority = vAuthService.update(userVAuthority);

        VehicleUserDTO response = vUserMapper.toDto(userVAuthority);

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(Long id, Long userId) {

        VehicleAuthority currentUserAuth = vAuthService
                .find(ApiContext.get().getCompanyId(), ApiContext.get().getUserId(), id);

        if (currentUserAuth == null) {
            throw ApiException.notFound("Not found : " + id);
        }

        if (currentUserAuth.getRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (currentUserAuth.getUserId() == userId) {
            throw ApiException.accessDenied();
        }

        if (id == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        VehicleAuthority userVAuthority = vAuthService
                .find(ApiContext.get().getCompanyId(), userId, id);

        if (userVAuthority == null) {
            throw ApiException.notFound("Not found : " + userId);
        }


        vAuthService.delete(userVAuthority);

        return ResponseEntity.ok().build();
    }
}
