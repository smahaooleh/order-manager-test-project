package com.otsmaha.ordermanager.service;

import com.otsmaha.ordermanager.domain.Item;

import java.util.Optional;

public interface ItemService {

    Item getOrCreateItemByName(String itemName) throws IllegalArgumentException;

    Optional<Item> findByNameIgnoreCase(String itemName) throws IllegalArgumentException;
}
