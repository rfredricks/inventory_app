package model;

import helper.IdGenerator;

/** Class Outsourced creates outsourced parts.
    @author Rebecca Fredricks */
public class Outsourced extends Part {

    // the company name for an outsourced part
    private String companyName;

    /** Constructor for Outsourced parts.

        @param id the id
        @param name the name
        @param price the price
        @param stock the stock
        @param min the min
        @param max the max
        @param companyName the company name */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock ,min, max);
        this.companyName = companyName;
    }


    /** Constructor for Outsourced parts. Uses next available id.

     @param name the name
     @param price the price
     @param stock the stock
     @param min the min
     @param max the max
     @param companyName the company name */
    public Outsourced(String name, double price, int stock, int min, int max, String companyName) {
        super(IdGenerator.getNewPartID(), name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Gets the company name.

        @return companyName the company name */
    public String getCompanyName() {
        return companyName;
    }

    /** Sets the company name.

        @param companyName the company name to set. */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
