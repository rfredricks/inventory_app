package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/** Inventory the inventory of parts and products.
    @author Rebecca Fredricks */
public class Inventory {

    // all parts and all products.
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /** Adds a part to the inventory.

        @param part the part to add. */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /** Adds a product to the inventory.

        @param product the product to add. */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /** Looks up a part in the inventory. This lookup method uses the part ID.

        @param partID the id to look up.
        @return the part. */
    public static Part lookupPart(int partID){
        for (Part part : allParts) {
            if (part.getId() == partID) return part;
        }
        return null;
    }

    /** Looks up a product in the inventory. This lookup method uses the product ID.

        @param productID the id to look up.
        @return the product. */
    public static Product lookupProduct(int productID){
        for (Product product : allProducts) {
            if (product.getId() == productID) return product;
        }
        return null;
    }

    /** Looks up parts in the inventory. This method uses the part name and returns a list
        of matching parts.

        @param partName the part name.
        @return the list of matching parts. */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> partList = FXCollections.observableArrayList();
        for (Part part : allParts){
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                partList.add(part);
            }
        }
        return partList;
    }

    /** Looks up products in the inventory. This method uses the product name and returns
        a list of matching products.

        @param productName the product name.
        @return the list of matching products. */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> productList = FXCollections.observableArrayList();
        for (Product product : allProducts){
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                productList.add(product);
            }
        }
        return productList;
    }

    /** Updates a part in the inventory.

        @param index the index to update.
        @param part the part to update. */
    public static void updatePart(int index, Part part){
        allParts.remove(index);
        allParts.add(index, part);
    }

    /** Updates a product in the inventory.

        @param index the index to update.
        @param product the product to update. */
    public static void updateProduct(int index, Product product){
        allProducts.remove(index);
        allProducts.add(index, product);
    }

    /** Deletes a part from the inventory.

        @param part the part to delete
        @return successful */
    public static boolean deletePart(Part part){
        return allParts.remove(part);
    }

    /** Deletes a product from the inventory.

        @param product the product to delete
        @return successful */
    public static boolean deleteProduct(Product product){
        return allProducts.remove(product);
    }

    /** Returns all parts in the inventory.

        @return the list of parts */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** Returns all products in the inventory.

        @return the list of products */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
