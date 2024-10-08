package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final List<Warehouse> warehouses = new ArrayList<>();

    private final List<ProductRecord> addedProducts = new ArrayList<>();

    private final List<ProductRecord> changedProducts = new ArrayList<>();

    String name;

    private Warehouse() {

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

        ProductRecord product = new ProductRecord(uuid, name, category, price);
        addedProducts.add(product);
        return product;
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
        List<ProductRecord> productsByCategory = new ArrayList<>();
        for (ProductRecord productRecord : getProducts()) {
            if (productRecord.category().getName().equals(category.getName())) {
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
