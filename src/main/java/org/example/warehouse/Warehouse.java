package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private static final ArrayList<Warehouse> warehouses = new ArrayList<Warehouse>();

    private static final List<ProductRecord> addedProducts = new ArrayList<>();

    private static final List<ProductRecord> changedProducts = new ArrayList<>();

    private final String warehouseName;

    private Warehouse () {
        this.warehouseName = null;
    }

    private Warehouse(String storeName) {
        this.warehouseName = storeName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public static Warehouse getInstance() {
       return new Warehouse();
    }

    public static Warehouse getInstance(String storeName) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getWarehouseName().equals(storeName)) {
                return warehouse;
            }
        }
        Warehouse warehouse = new Warehouse(storeName);
        warehouses.add(warehouse);
        return warehouse;
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        for (ProductRecord productRecord : addedProducts) {
            if (productRecord.uuid().equals(uuid)){
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates");
            }
        }
        ProductRecord productRecord = new ProductRecord(uuid, name, category, price);
        addedProducts.add(productRecord);
        return productRecord;
    }

    public List<ProductRecord> getProducts() {
        return addedProducts;
    }

    public ProductRecord getProductById(UUID uuid) {
        if (!addedProducts.isEmpty()) {
            for (ProductRecord productRecord : addedProducts) {
                if (productRecord.uuid().equals(uuid)) {
                    return productRecord;
                }
            }
        }
        return null;
    }

    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        if (isNotEmpty()) {
            if (checkID(uuid)) {
                getProductById(uuid).setNewPrice(newPrice);
                changedProducts.add(getProductById(uuid));
            }
        }
    }

    public void updateProduct(UUID uuid, String newName){
        if (checkID(uuid)) {
            getProductById(uuid).setNewName(newName);
        }
    }

    public List<ProductRecord> getChangedProducts() {
        if (changedProducts.isEmpty()) {
            return null;
        }
        return changedProducts;
    }
    public boolean isNotEmpty(){
        return !addedProducts.isEmpty() || !changedProducts.isEmpty();
    }

    public boolean checkID(UUID uuid) {
        for (ProductRecord productRecord : addedProducts) {
            if (productRecord.uuid().equals(uuid)) {
                return true;
            }
        }
        throw new IllegalArgumentException("Product with that id doesn't exist");
    }

    public List <ProductRecord> getProductsBy(Category category){
        List<ProductRecord> productsByCategory = getProducts();
        for (ProductRecord productRecord : addedProducts) {
            if (productRecord.category().equals(Category.of(String.valueOf(category)))) {
                productsByCategory.add(productRecord);
            }
        }
        return productsByCategory;
    }





}
