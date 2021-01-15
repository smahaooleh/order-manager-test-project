package com.otsmaha.ordermanager;

import com.otsmaha.ordermanager.service.ItemDeliveryService;
import com.otsmaha.ordermanager.service.ItemService;
import com.otsmaha.ordermanager.service.OrderRegistrationService;
import com.otsmaha.ordermanager.service.OrderValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderManagerApplicationTests {

    @Autowired
    private ItemDeliveryService itemDeliveryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderRegistrationService orderRegistrationService;

    @Autowired
    private OrderValidationService orderValidationService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(itemDeliveryService);
        Assertions.assertNotNull(itemService);
        Assertions.assertNotNull(orderRegistrationService);
        Assertions.assertNotNull(orderValidationService);
    }

}
