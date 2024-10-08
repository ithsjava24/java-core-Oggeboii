package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final List<Warehouse> warehouses = new ArrayList<>();

    private final List<ProductRecord> addedProducts = new ArrayList<>();

    private final List<ProductRecord> changedProducts = new ArrayList<>();

    String name;

    private Warehouse() {}

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
        Warehouse warehouse = warehouses.stream()
                        .filter(w -> w.getName()
                        .equals(storeName))
                        .findFirst()
                        .orElse(null);
        if (warehouse == null) {
            warehouse = new Warehouse(storeName);
            warehouses.add(warehouse);
        }
        return warehouse;
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
       getProducts().forEach(p -> {
           if (p.uuid().equals(uuid)) {
               throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
           }
       });
        ProductRecord product = new ProductRecord(uuid, name, category, price);
        addedProducts.add(product);
        return product;
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(addedProducts);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return (addedProducts.stream().filter(p -> p.uuid().equals(uuid)).findFirst());
    }

    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        if (checkID(uuid)) {
            getProductById(uuid).orElseThrow().setNewPrice(newPrice);
            changedProducts.add(getProductById(uuid).orElseThrow());
        }
    }

    public List<ProductRecord> getChangedProducts() {
        return Collections.unmodifiableList(changedProducts);
    }

    public boolean checkID(UUID uuid) {
        if (addedProducts.stream().anyMatch(p -> p.uuid().equals(uuid))){
            return true;
        }
        else
            throw new IllegalArgumentException("Product with that id doesn't exist.");
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return  getProducts().stream().filter(p -> p.category()
                .getName()
                .equals(category.getName()))
                .toList();
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
