package com.epam.esm.repository.order;

import com.epam.esm.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Transactional
    @Override
    public Order save(Order order);

    public Page<Order> findByUserId(int userId, Pageable pageable);
}
