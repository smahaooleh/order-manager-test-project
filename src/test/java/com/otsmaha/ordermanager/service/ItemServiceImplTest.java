package com.otsmaha.ordermanager.service;

import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceImplTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


    @Test
    public void getOrCreateItemByName_IncorrectItemNameGiven_ShouldThrowIllegalArgumentException() {

        String incorrectItemName = "";

        given(itemRepository.findByNameIgnoreCase(any())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            Item item = itemService.getOrCreateItemByName(incorrectItemName);
        });
    }

    @Test
    public void getOrCreateItemByName_ExistingItemNameGiven_ShouldNotSaveNewItem() {

        String existingItemName = "Apple";

        Item existingItem = new Item(1L, "Apple");

        given(itemRepository.findByNameIgnoreCase(any())).willReturn(Optional.of(existingItem));

        verify(itemRepository, times(0)).save(any());


        Item item = itemService.getOrCreateItemByName(existingItemName);

        assertEquals(existingItem, item);
    }

    @Test
    public void getOrCreateItemByName_NewItemNameGiven_ShouldSaveNewItem() {

        String newItemName = "Apple";

        given(itemRepository.findByNameIgnoreCase(any())).willReturn(Optional.empty());

        Item item = itemService.getOrCreateItemByName(newItemName);

        verify(itemRepository, times(1)).save(any());

        assertNotNull(item);
    }

    @Test
    public void findByNameIgnoreCase_IncorrectItemNameGiven_ShouldThrowIllegalArgumentException() {

        String incorrectItemName = "";

        given(itemRepository.findByNameIgnoreCase(any())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Item> item = itemService.findByNameIgnoreCase(incorrectItemName);
        });
    }

    @Test
    public void getOrCreateItemByName_CorrectItemNameGiven_ShouldCallItemRepositoryFindByNameIgnoreCaseMethod() {

        String itemName = "Apple";

        given(itemRepository.findByNameIgnoreCase(any())).willReturn(Optional.empty());

        Optional<Item> item = itemService.findByNameIgnoreCase(itemName);

        verify(itemRepository, times(1)).findByNameIgnoreCase(any());
    }


}
