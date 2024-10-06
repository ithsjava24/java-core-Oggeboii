package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductRecord {

    private final UUID uuid;
    private String name;
    private final Category category;
    private BigDecimal price;

    public ProductRecord(UUID uuid, String name, Category category, BigDecimal price) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Category category() {
        return category;
    }

    public BigDecimal price() {
        return price;
    }
    public void setNewPrice(BigDecimal newPrice) {
        price = newPrice;
    }
    public void setNewName(String newName) {
        name = newName;
    }

}
