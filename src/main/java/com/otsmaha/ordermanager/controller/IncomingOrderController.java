package com.otsmaha.ordermanager.controller;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.payload.request.OrderRegistrationRequest;
import com.otsmaha.ordermanager.payload.response.MessageResponse;
import com.otsmaha.ordermanager.payload.response.SuccessfulRegistrationResponse;
import com.otsmaha.ordermanager.service.ItemService;
import com.otsmaha.ordermanager.service.OrderRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/order")
public class IncomingOrderController {

    @Autowired
    private OrderRegistrationService orderRegistrationService;

    @Autowired
    private ItemService itemService;

    @Value("${order.valid.period}")
    private Integer orderValidPeriod;

    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderRegistrationRequest orderRegistrationRequest) {

        if (orderRegistrationRequest.getPrice() <= 0 || orderRegistrationRequest.getQuantity() <= 0 || orderRegistrationRequest.getItem().equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Order's field are incorrect"));

        Item item = itemService.getOrCreateItemByName(orderRegistrationRequest.getItem());

        LocalDateTime creationDate = LocalDateTime.now();

        Order newOrder = new Order( orderRegistrationRequest.getPrice(),
                                    orderRegistrationRequest.getQuantity(),
                                    item,
                                    creationDate);

        orderRegistrationService.registerOrder(newOrder);

        LocalDateTime validUntil = creationDate.plusMinutes(orderValidPeriod);

        return ResponseEntity.ok(new SuccessfulRegistrationResponse("Order was successfully registered", newOrder.getId(), creationDate, validUntil));
    }
}
