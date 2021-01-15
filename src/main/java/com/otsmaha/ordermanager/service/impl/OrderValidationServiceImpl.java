package com.otsmaha.ordermanager.service.impl;

import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.repository.OrderRepository;
import com.otsmaha.ordermanager.service.OrderValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderValidationServiceImpl implements OrderValidationService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${order.valid.period}")
    private Integer orderValidPeriod;

    @Scheduled(cron = "*/10 * * * * *")
    @Lock(LockModeType.PESSIMISTIC_READ) //There is need to protect from reading invalid order.
    @Transactional
    @Override
    public void deleteInvalidOrder() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startValidPeriod = now.minusMinutes(orderValidPeriod);
        List<Order> allWithCreationDateTimeBefore = orderRepository.findAllWithCreationDateTimeBefore(startValidPeriod);

        long count = allWithCreationDateTimeBefore.stream().count();

        orderRepository.deleteAll(allWithCreationDateTimeBefore);

        System.out.println(count + " invalid orders was deleted");
    }
}
