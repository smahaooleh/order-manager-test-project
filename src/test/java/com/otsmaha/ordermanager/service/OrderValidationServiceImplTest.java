package com.otsmaha.ordermanager.service;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "order.valid.period=2",
})
@SpringBootTest
public class OrderValidationServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidationService orderValidationService;

    @Value("${order.valid.period}")
    private Integer orderValidPeriod;

    @Test
    public void validationValueSetUp() {
        assertEquals(2, orderValidPeriod);
    }

    @Test
    public void deleteInvalidOrder_OrderListGiven_ShouldDeleteAllOrders() {

        List<Order> expectedDeletedOrders = new ArrayList<>(Arrays.asList(
                new Order(3, 4, new Item("Apple")),
                new Order(5, 5, new Item("Apple")),
                new Order(6, 3, new Item("Apple")),
                new Order(8, 8, new Item("Apple"))
        ));

        given(orderRepository.findAllWithCreationDateTimeBefore(any(LocalDateTime.class))).willReturn(expectedDeletedOrders);

        orderValidationService.deleteInvalidOrder();

        verify(orderRepository, times(1)).deleteAll(any());

        doAnswer(invocationOnMock -> {
            List<Order> arg = invocationOnMock.getArgument(0);

            assertEquals(expectedDeletedOrders, arg);
            return null;
        }).when(orderRepository).deleteAll(any());
    }
}
