package com.hasangurbuz.vehiclemanager.service.impl;

import com.hasangurbuz.vehiclemanager.domain.GroupAuthority;
import com.hasangurbuz.vehiclemanager.domain.QGroupAuthority;
import com.hasangurbuz.vehiclemanager.repository.GroupUserRepository;
import com.hasangurbuz.vehiclemanager.service.GroupAuthorityService;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class GroupAuthorityServiceImpl implements GroupAuthorityService {

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public GroupAuthority create(GroupAuthority groupAuthority) {
        return groupUserRepository.save(groupAuthority);
    }

    @Override
    public GroupAuthority find(Long companyId, Long userId, Long groupId) {
        QGroupAuthority groupAuthority = QGroupAuthority.groupAuthority;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        GroupAuthority groupAuth = query.selectFrom(groupAuthority)
                .where(
                        groupAuthority.group.companyId.eq(companyId)
                                .and(groupAuthority.userId.eq(userId))
                                .and(groupAuthority.group.id.eq(groupId))
                ).fetchOne();

        JPAQueryFactory v = new JPAQueryFactory(entityManager);


        return groupAuth;
    }
}
