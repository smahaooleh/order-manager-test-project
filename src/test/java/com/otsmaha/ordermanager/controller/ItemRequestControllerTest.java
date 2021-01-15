package com.otsmaha.ordermanager.controller;

import com.otsmaha.ordermanager.domain.DeliveryUnit;
import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.payload.response.ItemDeliveryResponse;
import com.otsmaha.ordermanager.service.ItemDeliveryService;
import com.otsmaha.ordermanager.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemDeliveryService itemDeliveryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void requestItemDelivery_IncorrectQuantityGiven_ShouldResponseWithStatus400() throws Exception {
        mockMvc.perform(get("/api/item").param("itemName", "Apple").param("quantity", "0"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("Quantity can not be zero or negative"));

        mockMvc.perform(get("/api/item").param("itemName", "Apple").param("quantity", "-7"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("Quantity can not be zero or negative"));

        mockMvc.perform(get("/api/item").param("itemName", "Apple").param("quantity", "-7.25"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void requestItemDelivery_IncorrectItemNameGiven_ShouldResponseWithStatus400() throws Exception {
        mockMvc.perform(get("/api/item").param("itemName", "").param("quantity", "7"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("Item name can not be empty"));
    }

    @Test
    public void requestItemDelivery_CorrectRequestGiven_ShouldResponseWithStatus200() throws Exception {

        Optional<Item> emptyItem = Optional.of(new Item("Apple"));

        List<DeliveryUnit> expectedDelivery = new ArrayList<>(Arrays.asList(
                new DeliveryUnit(3, 4)
        ));

        ItemDeliveryResponse serviceResponse = new ItemDeliveryResponse("Requested item number was successfully processed", "Apple", expectedDelivery);


        given(itemService.findByNameIgnoreCase(any())).willReturn(emptyItem);

        doAnswer(invocationOnMock -> {

            Item argItem = invocationOnMock.getArgument(0);
            Integer argQuantity = invocationOnMock.getArgument(1);

            assertEquals("Apple", argItem.getName());
            assertEquals(7, argQuantity);

            return serviceResponse;
        }).when(itemDeliveryService).processItemDelivery(any(Item.class), any(Integer.class));

        mockMvc.perform(get("/api/item").param("itemName", "Apple").param("quantity", "7"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
