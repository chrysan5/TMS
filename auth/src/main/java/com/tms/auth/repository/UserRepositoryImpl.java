package com.tms.auth.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.auth.dto.UserRequestDto;
import com.tms.auth.dto.UserResponseDto;
import com.tms.auth.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tms.auth.model.QUser.user;


@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserResponseDto> searchUsers(UserRequestDto userRequestDto, Pageable pageable){
         Session session = entityManager.unwrap(Session.class);
         //session.enableFilter("deletedFilter").setParameter("isDeleted", false);
         session.disableFilter("deletedFilter"); // User 엔티티에 있는 필터 제거

        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);

        QueryResults<User> results = queryFactory
                .selectFrom(user)
                .where(
                        usernameContains(userRequestDto.getUsername()), //name이 없을 경우 null로 반환후 검색조건에서 제외
                        deliveryTypeContains(userRequestDto.getDeliveryType()),
                        hubIdContains(userRequestDto.getHubId()),
                        isDeletedCondition(userRequestDto.getIsDelete())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UserResponseDto> content = results.getResults().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort() != null) {
            for (Sort.Order sortOrder : pageable.getSort()) {
                com.querydsl.core.types.Order direction = sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;
                switch (sortOrder.getProperty()) {
                    case "username":
                        orders.add(new OrderSpecifier<>(direction, user.username));
                        break;
                    case "createdAt":
                        orders.add(new OrderSpecifier<>(direction, user.createdAt));
                        break;
                    default:
                        break;
                }
            }
        }

        return orders;
    }


    private BooleanExpression usernameContains(String username) {
        return username != null ? user.username.containsIgnoreCase(username) : null;
    }

    private BooleanExpression deliveryTypeContains(String deliveryType) {
        return deliveryType != null ? user.deliveryUser.deliveryType.stringValue().containsIgnoreCase(deliveryType) : null;
    }

    private BooleanExpression hubIdContains(Long hubId) {
        return hubId != null ? user.deliveryUser.hubId.eq(hubId) : null;
    }

    private BooleanExpression isDeletedCondition(Boolean isDelete){
        return isDelete != null ? user.isDelete.eq(isDelete) : null;
    }
}