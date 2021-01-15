package com.otsmaha.ordermanager.controller;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.payload.request.ItemRequest;
import com.otsmaha.ordermanager.payload.response.MessageResponse;
import com.otsmaha.ordermanager.service.ItemDeliveryService;
import com.otsmaha.ordermanager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/item")
public class ItemRequestController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDeliveryService itemDeliveryService;

    @GetMapping
    public ResponseEntity<?> requestItemDelivery(@RequestParam String itemName, @RequestParam int quantity) {

        if (quantity <= 0)
            return ResponseEntity.badRequest().body(new MessageResponse("Quantity can not be zero or negative"));
        if (itemName.equals(""))
            return ResponseEntity.badRequest().body(new MessageResponse("Item name can not be empty"));

        Optional<Item> item = itemService.findByNameIgnoreCase(itemName);
        if (!item.isPresent()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(itemDeliveryService.processItemDelivery(item.get(), quantity));
    }
}
