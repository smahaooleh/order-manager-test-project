package com.otsmaha.ordermanager.repository;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByItemOrderByPriceAsc(Item item);

    @Query("select o from Order o where o.creationDateTime <= :startValidPeriod")
    List<Order> findAllWithCreationDateTimeBefore(@Param("startValidPeriod")LocalDateTime startValidPeriod);
}
