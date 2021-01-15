package com.otsmaha.ordermanager.service;

import com.otsmaha.ordermanager.domain.DeliveryUnit;
import com.otsmaha.ordermanager.domain.Item;
import com.otsmaha.ordermanager.domain.Order;
import com.otsmaha.ordermanager.payload.response.ItemDeliveryResponse;
import com.otsmaha.ordermanager.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemDeliveryServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private ItemDeliveryService itemDeliveryService;

    @Test
    public void processItemDelivery_EmptyOrderListGiven_ShouldReturnResponseWithEmptyDeliveryUnitList() throws Exception {

        List<Order> orders = new ArrayList<>();

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        int quantity = 4;

        Item item = new Item("Apple");

        List<DeliveryUnit> expectedDelivery = new ArrayList<>();

        ItemDeliveryResponse expectedResponse = new ItemDeliveryResponse("For now there are no orders with item", "Apple", expectedDelivery);

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertEquals(expectedResponse.getMessage(), receivedResponse.getMessage());

        System.out.println("Expected message: " + expectedResponse.getMessage());

        assertEquals(expectedResponse.getItemName(), receivedResponse.getItemName());
        assertEquals(expectedResponse.getUnits().size(), receivedResponse.getUnits().size());

        System.out.println("Expected DU list size: " + expectedResponse.getUnits().size());
    }

    @Test
    public void processItemDelivery_QuantityGreaterThenItemNumberGiven_ShouldReturnResponseWithNotEnoughMessageAndAllAvailableOrders() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(3, 4, new Item("Apple")),
                new Order(5, 5, new Item("Apple")),
                new Order(6, 3, new Item("Apple")),
                new Order(8, 8, new Item("Apple"))
        ));

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        int quantity = 25; //Item number in order list is 20

        Item item = new Item("Apple");

        List<DeliveryUnit> expectedDelivery = new ArrayList<>(Arrays.asList(
                new DeliveryUnit(3, 4),
                new DeliveryUnit(5, 5),
                new DeliveryUnit(6, 3),
                new DeliveryUnit(8, 8)
        ));

        ItemDeliveryResponse expectedResponse = new ItemDeliveryResponse("For now requested item number was not found", "Apple", expectedDelivery);

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertEquals(expectedResponse.getMessage(), receivedResponse.getMessage());

        System.out.println("Expected message: " + expectedResponse.getMessage());

        assertEquals(expectedResponse.getItemName(), receivedResponse.getItemName());
        assertEquals(expectedResponse.getUnits().size(), receivedResponse.getUnits().size());

        System.out.println("Expected DU list size: " + expectedResponse.getUnits().size());

        for (int i = 0; i < expectedResponse.getUnits().size(); i++) {
            assertEquals(expectedResponse.getUnits().get(i).getPrice(), receivedResponse.getUnits().get(i).getPrice());
            assertEquals(expectedResponse.getUnits().get(i).getQuantity(), receivedResponse.getUnits().get(i).getQuantity());
        }

    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldReturnResponseWithOneDeliveryUnit() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(3, 4, new Item("Apple")),
                new Order(5, 5, new Item("Apple")),
                new Order(6, 3, new Item("Apple")),
                new Order(8, 8, new Item("Apple"))
        ));

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        doNothing().when(orderRepository).delete(any(Order.class));

        int quantity = 4;
        Item item = new Item("Apple");

        List<DeliveryUnit> expectedDelivery = new ArrayList<>(Arrays.asList(
                new DeliveryUnit(3, 4)
        ));

        ItemDeliveryResponse expectedResponse = new ItemDeliveryResponse("Requested item number was successfully processed", "Apple", expectedDelivery);

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertEquals(expectedResponse.getMessage(), receivedResponse.getMessage());

        System.out.println("Expected message: " + expectedResponse.getMessage());

        assertEquals(expectedResponse.getItemName(), receivedResponse.getItemName());
        assertEquals(expectedResponse.getUnits().size(), receivedResponse.getUnits().size());

        System.out.println("Expected DU list size: " + expectedResponse.getUnits().size());

        assertEquals(expectedResponse.getUnits().get(0).getPrice(), receivedResponse.getUnits().get(0).getPrice());
        assertEquals(expectedResponse.getUnits().get(0).getQuantity(), receivedResponse.getUnits().get(0).getQuantity());
    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldReturnResponseWithTwoDeliveryUnit() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(3, 4, new Item("Apple")),
                new Order(5, 5, new Item("Apple")),
                new Order(6, 3, new Item("Apple")),
                new Order(8, 8, new Item("Apple"))
        ));

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        doNothing().when(orderRepository).delete(any(Order.class));

        int quantity = 6;
        Item item = new Item("Apple");

        List<DeliveryUnit> expectedDelivery = new ArrayList<>(Arrays.asList(
                new DeliveryUnit(3, 4),
                new DeliveryUnit(5, 2)
        ));

        ItemDeliveryResponse expectedResponse = new ItemDeliveryResponse("Requested item number was successfully processed", "Apple", expectedDelivery);

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertEquals(expectedResponse.getMessage(), receivedResponse.getMessage());

        System.out.println("Expected message: " + expectedResponse.getMessage());

        assertEquals(expectedResponse.getItemName(), receivedResponse.getItemName());
        assertEquals(expectedResponse.getUnits().size(), receivedResponse.getUnits().size());

        System.out.println("Expected DU list size: " + expectedResponse.getUnits().size());

        assertEquals(expectedResponse.getUnits().get(0).getPrice(), receivedResponse.getUnits().get(0).getPrice());
        assertEquals(expectedResponse.getUnits().get(0).getQuantity(), receivedResponse.getUnits().get(0).getQuantity());
        assertEquals(expectedResponse.getUnits().get(1).getPrice(), receivedResponse.getUnits().get(1).getPrice());
        assertEquals(expectedResponse.getUnits().get(1).getQuantity(), receivedResponse.getUnits().get(1).getQuantity());
    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldReturnResponseWithSeveralDeliveryUnit() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(3, 4, new Item("Apple")),
                new Order(5, 5, new Item("Apple")),
                new Order(6, 3, new Item("Apple")),
                new Order(8, 8, new Item("Apple"))
        ));

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        doNothing().when(orderRepository).delete(any(Order.class));

        int quantity = 20;
        Item item = new Item("Apple");

        List<DeliveryUnit> expectedDelivery = new ArrayList<>(Arrays.asList(
                new DeliveryUnit(3, 4),
                new DeliveryUnit(5, 5),
                new DeliveryUnit(6, 3),
                new DeliveryUnit(8, 8)
        ));

        ItemDeliveryResponse expectedResponse = new ItemDeliveryResponse("Requested item number was successfully processed", "Apple", expectedDelivery);

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertEquals(expectedResponse.getMessage(), receivedResponse.getMessage());

        System.out.println("Expected message: " + expectedResponse.getMessage());

        assertEquals(expectedResponse.getItemName(), receivedResponse.getItemName());
        assertEquals(expectedResponse.getUnits().size(), receivedResponse.getUnits().size());

        System.out.println("Expected DU list size: " + expectedResponse.getUnits().size());

        for (int i = 0; i < expectedResponse.getUnits().size(); i++) {
            assertEquals(expectedResponse.getUnits().get(i).getPrice(), receivedResponse.getUnits().get(i).getPrice());
            assertEquals(expectedResponse.getUnits().get(i).getQuantity(), receivedResponse.getUnits().get(i).getQuantity());
        }
    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldDeleteOneOrderWithQuantityZero() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(1L, 3, 4, new Item("Apple")),
                new Order(2L, 5, 5, new Item("Apple")),
                new Order(3L, 6, 3, new Item("Apple")),
                new Order(4L, 8, 8, new Item("Apple"))
        ));

        List<Long> expectedDeletedOrdersId = new ArrayList<>(Arrays.asList(1L));

//        List<Long> expectedDeletedOrdersId = orders.stream().mapToLong(o -> o.getId()).boxed().collect(Collectors.toList());

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        doAnswer(invocationOnMock -> {
            Order arg = invocationOnMock.getArgument(0);

            assertTrue(expectedDeletedOrdersId.contains(arg.getId()));
            return null;
        }).when(orderRepository).delete(any(Order.class));

        int quantity = 4;

        Item item = new Item("Apple");

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertNotNull(receivedResponse);
    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldDeleteTwoOrderWithQuantityZero() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(1L, 3, 4, new Item("Apple")),
                new Order(2L, 5, 5, new Item("Apple")),
                new Order(3L, 6, 3, new Item("Apple")),
                new Order(4L, 8, 8, new Item("Apple"))
        ));

        List<Long> expectedDeletedOrdersId = new ArrayList<>(Arrays.asList(1L, 2L));

//        List<Long> expectedDeletedOrdersId = orders.stream().mapToLong(o -> o.getId()).boxed().collect(Collectors.toList());

        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        doAnswer(invocationOnMock -> {
            Order arg = invocationOnMock.getArgument(0);

            assertTrue(expectedDeletedOrdersId.contains(arg.getId()));
            return null;
        }).when(orderRepository).delete(any(Order.class));

        int quantity = 10;

        Item item = new Item("Apple");

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        assertNotNull(receivedResponse);
    }

    @Test
    public void processItemDelivery_CorrectQuantityGiven_ShouldNotDeleteAnyOrder() throws Exception {

        List<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(1L, 3, 4, new Item("Apple")),
                new Order(2L, 5, 5, new Item("Apple")),
                new Order(3L, 6, 3, new Item("Apple")),
                new Order(4L, 8, 8, new Item("Apple"))
        ));


        given(orderRepository.findAllByItemOrderByPriceAsc(any())).willReturn(orders);

        int quantity = 21; //

        Item item = new Item("Apple");

        ItemDeliveryResponse receivedResponse = itemDeliveryService.processItemDelivery(item, quantity);

        verify(orderRepository, times(0)).delete(any());

        assertNotNull(receivedResponse);
    }
}
