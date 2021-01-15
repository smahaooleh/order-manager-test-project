package com.otsmaha.ordermanager.controller;

import com.jayway.jsonpath.JsonPath;
import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.service.ItemService;
import com.otsmaha.ordermanager.service.OrderRegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IncomingOrderController.class)
public class IncomingOrderControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private OrderRegistrationService orderRegistrationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerOrder_OrderWithBadPriceGiven_ShouldResponseWithStatus400() throws Exception {

        String orderWithBadPrice = "{\"price\": 0,\"quantity\": 7,\"item\": \"Apple\"}";

        mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
                .content(orderWithBadPrice))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerOrder_OrderWithBadQuantityGiven_ShouldResponseWithStatus400() throws Exception {

        String orderWithBadQuantity = "{\"price\": 14.55,\"quantity\": 0,\"item\": \"Apple\"}";

        mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
                .content(orderWithBadQuantity))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerOrder_OrderWithBadItemNameGiven_ShouldResponseWithStatus400() throws Exception {

        String orderWithBadItemName = "{\"price\": 14.55,\"quantity\": 7,\"item\": \"\"}";

        mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
                .content(orderWithBadItemName))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerOrder_CorrectOrderGiven_ShouldResponseWithStatus200() throws Exception {

        String order = "{\"price\": 14.55,\"quantity\": 7,\"item\": \"Apple\"}";

        given(itemService.getOrCreateItemByName(any())).willReturn(new Item(3L, "Apple"));

        doAnswer(invocationOnMock -> {
            Order arg = invocationOnMock.getArgument(0);

            assertEquals(14.55, arg.getPrice());
            assertEquals(7, arg.getQuantity());
            assertEquals("Apple", arg.getItem().getName());

            arg.setId(3L);
            return null;
        }).when(orderRegistrationService).registerOrder(any(Order.class));

        MvcResult response = mockMvc.perform(post("/api/order").contentType(MediaType.APPLICATION_JSON)
                .content(order))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.message").value("Order was successfully registered"))
                .andExpect(jsonPath("$.id").value(3))
                .andReturn();

        String creationDateStr = JsonPath.read(response.getResponse().getContentAsString(), "$.creationDate");
        String validUntilStr = JsonPath.read(response.getResponse().getContentAsString(), "$.validUntil");

        LocalDateTime creationDate = LocalDateTime.parse(creationDateStr);
        LocalDateTime validUntil = LocalDateTime.parse(validUntilStr);

        long validPeriod = ChronoUnit.MINUTES.between(creationDate, validUntil);

        assertEquals(10L, validPeriod);
    }

}
