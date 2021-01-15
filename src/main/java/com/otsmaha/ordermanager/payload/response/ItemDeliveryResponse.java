package com.otsmaha.ordermanager.payload.response;

import com.otsmaha.ordermanager.domain.DeliveryUnit;

import java.util.List;

public class ItemDeliveryResponse {

    private String message;

    private String itemName;

    private List<DeliveryUnit> units;

    public ItemDeliveryResponse(String message, String itemName, List<DeliveryUnit> units) {
        this.message = message;
        this.itemName = itemName;
        this.units = units;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<DeliveryUnit> getUnits() {
        return units;
    }

    public void setUnits(List<DeliveryUnit> units) {
        this.units = units;
    }
}
