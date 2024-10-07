package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final List<Warehouse> warehouses = new ArrayList<>();

    private final List<ProductRecord> addedProducts = new ArrayList<>();

    private final List<ProductRecord> changedProducts = new ArrayList<>();

    private final String name;

    private Warehouse() {
        this.name = null;
    }

    private Warehouse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Warehouse getInstance() {
        return new Warehouse();

    }

    public static Warehouse getInstance(String storeName) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName().equals(storeName)) {
                return warehouse;
            }
        }
        Warehouse warehouse = new Warehouse(storeName);
        warehouses.add(warehouse);
        return warehouse;
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        for (ProductRecord productRecord : getProducts()) {
            if (productRecord.uuid().equals(uuid)) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }
        }

        ProductRecord productRecord = new ProductRecord(uuid, name, category, price);
        addedProducts.add(productRecord);
        return productRecord;
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(addedProducts);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {

        if (!addedProducts.isEmpty()) {
            for (ProductRecord productRecord : addedProducts) {
                if (productRecord.uuid().equals(uuid)) {
                    return Optional.of(productRecord);
                }
            }
        }
        return Optional.empty();
    }

    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        if (checkID(uuid)) {
            getProductById(uuid).orElseThrow().setNewPrice(newPrice);
            changedProducts.add(getProductById(uuid).orElseThrow());
        }
    }

//    public void updateProduct(UUID uuid, String newName) {
//        if (checkID(uuid)) {
//            getProductById(uuid).orElseThrow().setNewName(newName);
//            changedProducts.add(getProductById(uuid).orElseThrow());
//        }
//    }

    public List<ProductRecord> getChangedProducts() {
        if (changedProducts.isEmpty()) {
            return null;
        }
        return Collections.unmodifiableList(changedProducts);
    }

    public boolean checkID(UUID uuid) {
        for (ProductRecord productRecord : addedProducts) {
            if (productRecord.uuid().equals(uuid)) {
                return true;
            }
        }
        throw new IllegalArgumentException("Product with that id doesn't exist.");
    }

    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> productsByCategory = getProducts();
        for (ProductRecord productRecord : getProducts()) {
            if (productRecord.category().equals(Category.of(String.valueOf(category)))) {
                productsByCategory.add(productRecord);
            }
        }
        return productsByCategory;
    }

    public boolean isEmpty() {
        return addedProducts.isEmpty();
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {

        Map<Category, List<ProductRecord>> productsOfCategories = addedProducts.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
        Collectors.mapping(ProductRecord::name, Collectors.toList());

        return productsOfCategories;
    }
}
