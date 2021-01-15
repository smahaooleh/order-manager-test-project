package com.otsmaha.ordermanager.service.impl;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.repository.ItemRepository;
import com.otsmaha.ordermanager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item getOrCreateItemByName(String itemName) throws IllegalArgumentException {

        if (itemName.equals("")) throw new IllegalArgumentException("itemName can not be empty");

        Optional<Item> item = itemRepository.findByNameIgnoreCase(itemName);

        if (!item.isPresent()) {
            Item createdItem = new Item(itemName);
            itemRepository.save(createdItem);
            return createdItem;
        }
        return item.get();
    }

    @Override
    public Optional<Item> findByNameIgnoreCase(String itemName) throws IllegalArgumentException {

        if (itemName.equals("")) throw new IllegalArgumentException("itemName can not be empty");

        return itemRepository.findByNameIgnoreCase(itemName);
    }
}
