package model;

import helper.IdGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class for product creates products.
    @author Rebecca Fredricks */
public class Product {

    //instance variables
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Constructor for Product.

        @param id the id
        @param name the name
        @param price the price
        @param stock the stock
        @param min the min
        @param max the max */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** Constructor for Product. Uses the next available id.

     @param name the name
     @param price the price
     @param stock the stock
     @param min the min
     @param max the max */
    public Product(String name, double price, int stock, int min, int max) {
        this.id = IdGenerator.getNewProductID();
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /** Gets the id.

        @return the id*/
    public int getId() {
        return id;
    }

    /** Sets the id. Uses next available id. */
    public void setId(){
        this.id = IdGenerator.getNewProductID();
    }

    /** Sets the id.

        @param id the id */
    public void setId(int id) {
        this.id = id;
    }

    /** Gets the name.

        @return the name */
    public String getName() {
        return name;
    }

    /** Sets the name.

        @param name the name */
    public void setName(String name) {
        this.name = name;
    }

    /** Gets the price.

        @return the price */
    public double getPrice() {
        return price;
    }

    /** Sets the price.

        @param price the price */
    public void setPrice(double price) {
        this.price = price;
    }

    /** Gets the stock.

        @return the stock */
    public int getStock() {
        return stock;
    }

    /** Sets the stock.

        @param stock the stock */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** Gets the min.

        @return the min */
    public int getMin() {
        return min;
    }

    /** Sets the min.

        @param min the min */
    public void setMin(int min) {
        this.min = min;
    }

    /** Gets the max.

        @return the max */
    public int getMax() {
        return max;
    }

    /** Sets the max.

        @param max the max */
    public void setMax(int max) {
        this.max = max;
    }

    /** Adds an associated part to this product.

        @param part the part to add */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /** Deletes an associated part from this product.

        @param selectedAssociatedPart the associated part to delete
        @return successful */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(associatedParts.contains(selectedAssociatedPart)){
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else return false;
    }

    /** Gets all associated parts for this product.

        @return the list of associated parts */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
