package org.example.warehouse;

import java.util.*;

public class Category {
    private final String categoryName;

    private static final List<Category> categories = new ArrayList<>();


    private Category(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        this.categoryName = name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    public String getName() {
        return categoryName;
    }

    public static Category of(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        Category category = new Category(categoryName);
        categories.add(category);
        return category;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryName);
    }
}
