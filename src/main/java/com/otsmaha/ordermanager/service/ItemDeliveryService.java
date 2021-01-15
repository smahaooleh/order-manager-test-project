package com.otsmaha.ordermanager.service;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.payload.response.ItemDeliveryResponse;

public interface ItemDeliveryService {
    ItemDeliveryResponse processItemDelivery(Item item, int quantity);
}
