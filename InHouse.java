package model;

import helper.IdGenerator;

/** Class InHouse creates in house parts.
    @author Rebecca Fredricks */
public class InHouse extends Part {

    //the machine id for an inhouse part
    private int machineID;

    /** Constructor for InHouse part.

        @param id the id
        @param name the name
        @param price the price
        @param stock the stock
        @param min the min
        @param max the max
        @param machineID the machine ID */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /** Constructor for InHouse part. Uses next available id.

        @param name the name
        @param price the price
        @param stock the stock
        @param min the min
        @param max the max
        @param machineID the machine ID */
    public InHouse(String name, double price, int stock, int min, int max, int machineID) {
        super(IdGenerator.getNewPartID(), name, price, stock, min, max);
        this.machineID = machineID;
    }

    /** Gets the machine ID.

     @return the machine ID. */
    public int getMachineID() {
        return machineID;
    }

    /** Sets the machine ID.

        @param machineID the ID to set. */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

}
