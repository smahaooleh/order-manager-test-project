package com.otsmaha.ordermanager.service;


import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRegistrationServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderRegistrationService orderRegistrationService;

    @Test
    public void registerOrder_OrderGiven_ShouldCallOrderRepositorySaveMethod() {

        Item item = new Item("Apple");
        Order order = new Order(7, 6, item);

        given(orderRepository.save(any(Order.class))).willReturn(order);

        orderRegistrationService.registerOrder(order);

        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
