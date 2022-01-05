package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Inventory;
import model.Part;

/** Class that searches for parts in the inventory.
    @author Rebecca Fredricks  */
public class PartSearch {

    /** Method that searches for parts in the inventory. This method compares
     a search string to any matching part names or partt id in the inventory.
     @param searchString the string to compare
     @return the list of matching parts */
    public static ObservableList<Part> partSearch(String searchString) {

        ObservableList<Part> result = Inventory.lookupPart(searchString);

        try {
            Part part = (Inventory.lookupPart(Integer.parseInt(searchString)));
            if (part != null) { result.add(part); }

        } catch (NumberFormatException e) {
            return result;
        }
        return result;
    }
}

