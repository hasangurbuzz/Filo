package com.hasangurbuz.vehiclemanager.api.web;

import com.hasangurbuz.vehiclemanager.api.ApiContext;
import com.hasangurbuz.vehiclemanager.api.ApiException;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleAuthorityMapper;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleMapper;
import com.hasangurbuz.vehiclemanager.api.mapper.VehicleUserMapper;
import com.hasangurbuz.vehiclemanager.domain.UserRole;
import com.hasangurbuz.vehiclemanager.domain.VehicleAuthority;
import com.hasangurbuz.vehiclemanager.service.PagedResults;
import com.hasangurbuz.vehiclemanager.service.VehicleAuthorityService;
import org.openapitools.api.VehicleUsersApi;
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

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserDTO.getId() == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (vehicleUserDTO.getRole() == null) {
            throw ApiException.invalidInput("User role required");
        }

        VehicleAuthority vAuthority = vAuthService.exists(id,
                ApiContext.get().getCompanyId(),
                vehicleUserDTO.getId()
        );

        if (vAuthority != null) {
            throw ApiException.invalidInput("User exists");
        }

        vAuthority = vAuthService
                .getByVehicleId(id,
                        ApiContext.get().getCompanyId(),
                        ApiContext.get().getUserId(),
                        ApiContext.get().getUserRole()
                );

        if (vAuthority == null) {
            throw ApiException.notFound("Not found id : " + id);
        }

        // inputlardaki değerlere sahip ama isDeleted = true olduğu için
        // serviceden gelmeyen vehicle-user'ların kontrolü yapılacak mı

        VehicleAuthority newVAuthority = new VehicleAuthority();

        newVAuthority.setUserId(vehicleUserDTO.getId());
        newVAuthority.setVehicle(vAuthority.getVehicle());
        newVAuthority.setRole(vehicleMapper.toUserRole(vehicleUserDTO.getRole()));

        newVAuthority = vAuthService.create(newVAuthority);

        VehicleUserDTO vehicleUser = new VehicleUserDTO();
        vehicleUser.setId(newVAuthority.getUserId());
        vehicleUser.setRole(vehicleMapper.toUserRoleDto(newVAuthority.getRole()));

        return ResponseEntity.ok(vehicleUser);

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserDTO> getUser(Long id, Long userId) {

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (id == null) {
            throw ApiException.invalidInput("Id required");
        }

        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        VehicleAuthority vAuthority = vAuthService
                .getByVehicleAndUserId(
                        id,
                        ApiContext.get().getCompanyId(),
                        ApiContext.get().getUserId(),
                        ApiContext.get().getUserRole(),
                        userId);

        if (vAuthority == null) {
            throw ApiException
                    .invalidInput("User : "
                            + userId
                            + " not found on vehicle : "
                            + id
                    );
        }

        return ResponseEntity.ok(vUserMapper.toDto(vAuthority));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VehicleUserListResponseDTO> searchUser(Long id, VehicleUserListRequestDTO vehicleUserListRequestDTO) {
        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserListRequestDTO == null) {
            vehicleUserListRequestDTO = new VehicleUserListRequestDTO();
        }
        if (vehicleUserListRequestDTO.getFrom() == null) {
            vehicleUserListRequestDTO.setFrom(PAGE_OFFSET);
        }
        if (vehicleUserListRequestDTO.getSize() == null) {
            vehicleUserListRequestDTO.setSize(PAGE_LIMIT);
        }
        if (vehicleUserListRequestDTO.getSort() == null) {
            vehicleUserListRequestDTO.setSort(new SortDTO());
        }
        if (vehicleUserListRequestDTO.getSort().getProperty() == null) {
            vehicleUserListRequestDTO.getSort().setProperty(SORT_PROPERTY);
        }
        if (vehicleUserListRequestDTO.getSort().getDirection() == null) {
            vehicleUserListRequestDTO.getSort().setDirection(ASC);
        }

        PagedResults<VehicleAuthority> vehiclePagedResults = vAuthService
                .searchUser(
                        id,
                        ApiContext.get().getCompanyId(),
                        ApiContext.get().getUserId(),
                        ApiContext.get().getUserRole(),
                        vehicleUserListRequestDTO
                );

        List<VehicleUserDTO> users = vUserMapper.toDtoList(vehiclePagedResults.getItems());

        VehicleUserListResponseDTO response = new VehicleUserListResponseDTO();
        response.setTotal(vehiclePagedResults.getTotal());
        response.setItems(users);

        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public ResponseEntity<VehicleUserDTO> updateUser(Long id, Long userId, VehicleUserUpdateRequestDTO vehicleUserUpdateRequestDTO) {
        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (vehicleUserUpdateRequestDTO.getUserRole() == null) {
            throw ApiException.invalidInput("User role required");
        }

        VehicleAuthority vAuthority = vAuthService
                .getByVehicleAndUserId(id,
                        ApiContext.get().getCompanyId(),
                        ApiContext.get().getUserId(),
                        ApiContext.get().getUserRole(),
                        userId);

        if (vAuthority == null) {
            throw ApiException.notFound("Not found user : " + userId + " on : " + id);
        }

        UserRole role = vehicleMapper.toUserRole(vehicleUserUpdateRequestDTO.getUserRole());
        vAuthority.setRole(role);

        vAuthority = vAuthService.update(vAuthority);

        VehicleUserDTO user = vUserMapper.toDto(vAuthority);

        return ResponseEntity.ok(user);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteUser(Long id, Long userId) {

        if (ApiContext.get().getUserRole() != UserRole.COMPANY_ADMIN) {
            throw ApiException.accessDenied();
        }

        if (id == null) {
            throw ApiException.invalidInput("Id required");
        }
        if (userId == null) {
            throw ApiException.invalidInput("User id required");
        }

        VehicleAuthority vAuthority = vAuthService
                .getByVehicleAndUserId(id,
                        ApiContext.get().getCompanyId(),
                        ApiContext.get().getUserId(),
                        ApiContext.get().getUserRole(),
                        userId);

        if (vAuthority == null) {
            throw ApiException.notFound("Not found user : " + userId + " on : " + id);
        }

        vAuthService.deleteUser(vAuthority);

        return ResponseEntity.ok().build();

    }
}
