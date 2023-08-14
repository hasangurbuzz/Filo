package com.hasangurbuz.filo.service.impl;

import com.hasangurbuz.filo.domain.Group;
import com.hasangurbuz.filo.domain.GroupAuthority;
import com.hasangurbuz.filo.domain.QGroupAuthority;
import com.hasangurbuz.filo.repository.GroupAuthorityRepository;
import com.hasangurbuz.filo.service.GroupAuthorityService;
import com.hasangurbuz.filo.service.PagedResults;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.openapitools.model.PageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class GroupAuthorityServiceImpl implements GroupAuthorityService {

    @Autowired
    private GroupAuthorityRepository groupAuthorityRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public GroupAuthority create(GroupAuthority groupAuthority) {
        return groupAuthorityRepository.save(groupAuthority);
    }

    @Override
    public GroupAuthority get(Long userId, Group group) {
        QGroupAuthority groupAuthority = QGroupAuthority.groupAuthority;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        GroupAuthority groupAuth = query.selectFrom(groupAuthority)
                .where(
                        groupAuthority.group.companyId.eq(group.getCompanyId())
                                .and(groupAuthority.userId.eq(userId))
                                .and(groupAuthority.group.id.eq(group.getId()))
                ).fetchOne();

        JPAQueryFactory v = new JPAQueryFactory(entityManager);


        return groupAuth;
    }

    @Override
    public List<GroupAuthority> searchByUserId(Long companyId, Long userId) {
        QGroupAuthority groupAuthority = QGroupAuthority.groupAuthority;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        List<GroupAuthority> results = query.selectFrom(groupAuthority)
                .where(
                        groupAuthority.group.companyId.eq(companyId)
                                .and(groupAuthority.userId.eq(userId))
                                .and(groupAuthority.group.isDeleted.isFalse())
                )
                .fetch();


        return results;
    }
}
