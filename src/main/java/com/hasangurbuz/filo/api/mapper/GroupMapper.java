package com.hasangurbuz.filo.api.mapper;

import com.hasangurbuz.filo.domain.Group;
import org.openapitools.model.GroupDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupMapper implements Mapper<Group, GroupDTO> {

    @Override
    public Group toEntity(GroupDTO dto) {
        Group group = new Group();
        group.setName(dto.getName());
        return group;
    }

    @Override
    public GroupDTO toDto(Group entity) {
        GroupDTO dto = new GroupDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public List<GroupDTO> toDtoList(List<Group> entityList) {
        if (entityList == null) {
            return null;
        }
        List<GroupDTO> dtoList = new ArrayList<>(entityList.size());
        for (Group group : entityList) {
            dtoList.add(toDto(group));
        }
        return dtoList;
    }
}
