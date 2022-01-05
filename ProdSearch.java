package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Inventory;
import model.Product;

/** Class that searches for products in the inventory.
    @author Rebecca Fredricks  */
public class ProdSearch {

    /** Method that searches for products in the inventory. This method compares
        a search string to any matching product names or product id in the
        inventory.
        @param searchString the string to compare
        @return the list of matching products */
    public static ObservableList<Product> prodSearch(String searchString) {

        ObservableList<Product> result = Inventory.lookupProduct(searchString);

        try {
            Product prod = Inventory.lookupProduct(Integer.parseInt(searchString));
            if (prod != null) { result.add(prod); }

        } catch (NumberFormatException e) {
            return result;
        }
        return result;
    }
}
