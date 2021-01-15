package com.otsmaha.ordermanager.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private double price;

    @NotBlank
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @NotBlank
    private LocalDateTime creationDateTime;

    public Order() {
    }

    public Order(Long id, @NotBlank double price, @NotBlank int quantity, Item item) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.item = item;
    }

    public Order(@NotBlank double price, @NotBlank int quantity, Item item) {
        this.price = price;
        this.quantity = quantity;
        this.item = item;
    }

    public Order(@NotBlank double price, @NotBlank int quantity, Item item, @NotBlank LocalDateTime creationDateTime) {
        this.price = price;
        this.quantity = quantity;
        this.item = item;
        this.creationDateTime = creationDateTime;
    }

    public Order(Long id, @NotBlank double price, @NotBlank int quantity, Item item, @NotBlank LocalDateTime creationDateTime) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.item = item;
        this.creationDateTime = creationDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
