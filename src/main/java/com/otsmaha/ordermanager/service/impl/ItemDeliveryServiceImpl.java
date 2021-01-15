package com.otsmaha.ordermanager.service.impl;

import com.otsmaha.ordermanager.domain.DeliveryUnit;
import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.payload.response.ItemDeliveryResponse;
import com.otsmaha.ordermanager.repository.OrderRepository;
import com.otsmaha.ordermanager.service.ItemDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemDeliveryServiceImpl implements ItemDeliveryService {

    @Autowired
    private OrderRepository orderRepository;

    @Lock(LockModeType.PESSIMISTIC_READ) //There is need to protect from deleting data during this method.
    @Transactional
    @Override
    public ItemDeliveryResponse processItemDelivery(Item item, int quantity) {

        List<Order> sortedOrders = orderRepository.findAllByItemOrderByPriceAsc(item);

        if (sortedOrders.size() == 0) {
            return new ItemDeliveryResponse("For now there are no orders with item", item.getName(), new ArrayList<>());
        }

        if (isEnoughItemNumber(sortedOrders, quantity)) {
            return new ItemDeliveryResponse("Requested item number was successfully processed", item.getName(), determineDeliveryUnits(sortedOrders, quantity));

        } else {
            return new ItemDeliveryResponse("For now requested item number was not found", item.getName(), fromOrderToDeliveryUnits(sortedOrders));
        }
    }

    private boolean isEnoughItemNumber(List<Order> orders, int quantity) {
        int itemNumber = orders.stream().mapToInt(order -> order.getQuantity()).sum();

        return itemNumber >= quantity;
    }

    private List<DeliveryUnit> fromOrderToDeliveryUnits(List<Order> orders) {
        return orders.stream().map(order -> new DeliveryUnit(order.getPrice(), order.getQuantity())).collect(Collectors.toList());
    }

    private List<DeliveryUnit> determineDeliveryUnits(List<Order> orders, int quantity) {

        int remainingQuantity = quantity;

        List<DeliveryUnit> deliveryUnits = new ArrayList<>();

        for (Order order : orders) {
            if (order.getQuantity() > remainingQuantity) {
                deliveryUnits.add(new DeliveryUnit(order.getPrice(), remainingQuantity));
                int currentOrderQuantity = order.getQuantity();
                order.setQuantity(currentOrderQuantity - remainingQuantity);
                break;
            } else {
                deliveryUnits.add(new DeliveryUnit(order.getPrice(), order.getQuantity()));
                remainingQuantity -= order.getQuantity();
                orderRepository.delete(order);
                if(remainingQuantity == 0) break;
            }
        }
        return deliveryUnits;
    }
}
