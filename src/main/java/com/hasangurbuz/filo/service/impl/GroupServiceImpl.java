package com.hasangurbuz.filo.service.impl;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.QGroup;
import com.hasangurbuz.filo.repository.GroupRepository;
import com.hasangurbuz.filo.service.GroupService;
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
    public Group get(Long companyId, Long groupId) {
        QGroup group = QGroup.group;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        Group result = queryFactory.selectFrom(group)
                .where(
                        group.companyId.eq(companyId)
                                .and(group.id.eq(groupId))
                                .and(group.isDeleted.isFalse())
                ).fetchOne();

        return result;
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
