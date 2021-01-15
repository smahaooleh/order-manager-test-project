package com.otsmaha.ordermanager.service.impl;

import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.repository.OrderRepository;
import com.otsmaha.ordermanager.service.OrderRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderRegistrationServiceImpl implements OrderRegistrationService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public void registerOrder(Order order) {
        orderRepository.save(order);
    }
}
