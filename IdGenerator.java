package helper;

/** IdGenerator generates unique part and product IDs.
    @author Rebecca Fredricks */
public class IdGenerator {

    //part ID variable
    private static int partID = 0;
    //product ID variable
    private static int productID = 0;

    /** Generates a new unique part ID. This method returns an integer part ID.
        @return a new part ID */
    public static int getNewPartID(){
        return partID++;
    }

    /** Generates a new unique product ID. This method returns an integer product ID.
        @return a new product ID */
    public static int getNewProductID(){
        return productID++;
    }

    /** Returns the current part ID in use.
        @return the part ID */
    public static int getCurrentPartID(){
        return partID;
    }

    /** Returns the current product ID in use.
        @return the product ID */
    public static int getCurrentProductID(){
        return productID;
    }
}
