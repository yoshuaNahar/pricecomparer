package nl.yoshuan.pricecomparer.utils;

import nl.yoshuan.pricecomparer.entities.Category;

public class CategoryUtil {

    public static final String GROENTE_FRUIT_AARDAPPELEN = "groente, fruit en aardappelen";
    public static final String GROENTE = "groente";

    private CategoryUtil() {}

    public static Category createParentCategory() {
        return new Category(GROENTE_FRUIT_AARDAPPELEN, null);
    }

    public static Category createFirstChildCategory(Category parent) {
        return new Category(GROENTE, parent);
    }

}
