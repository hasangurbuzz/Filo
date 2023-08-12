package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.Group;
import com.hasangurbuz.vehiclemanager.domain.QGroup;
import com.hasangurbuz.vehiclemanager.repository.GroupRepository;
import com.hasangurbuz.vehiclemanager.service.GroupService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private EntityManager entityManager;


    @Override
    public Group create(Group group) {
        group.setDeleted(false);
        return groupRepo.save(group);
    }

    @Override
    public boolean existsSameName(Long companyId, String name) {
        QGroup group = QGroup.group;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        long count = queryFactory
                .selectFrom(group)
                .where(
                        group.name.eq(name)
                                .and(group.companyId.eq(companyId))
                )
                .fetchCount();

        return count > 0;
    }
}
