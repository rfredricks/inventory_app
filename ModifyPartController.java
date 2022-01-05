package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for modifyPartView.
    @author Rebecca Fredricks */
public class ModifyPartController implements Initializable {

    public ToggleGroup partSource;
    @FXML
    private TextField mpartidTextField;

    @FXML
    private TextField mpartnameTextField;

    @FXML
    private TextField mpartinvTextField;

    @FXML
    private TextField mpartpriceTextField;

    @FXML
    private TextField mpartmaxTextField;

    @FXML
    private TextField mpartminTextField;

    @FXML
    private TextField mpartfieldTextField;
    @FXML
    public RadioButton inhouseRB;
    @FXML
    public RadioButton outsourcedRB;

    @FXML
    private Label invintlbl;

    @FXML
    private Label invminmaxlbl;

    @FXML
    private Label maxintlbl;

    @FXML
    private Label minintlbl;

    @FXML
    private Label minmaxlbl;

    @FXML
    private Label pricenumlbl;

    @FXML
    private Label machidintlbl;

    @FXML
    private Label fieldlbl;

    //change scene variables
    private Stage stage;
    private Parent scene;
    //input variables

    private int index;

    private String name, companyName;
    private double price;
    private int id, min, max, inv, machid;

    private enum Sub { INHOUSE, OUTSOURCED };
    private Sub sub;

    /** Method to return to the main menu. This method returns to the main menu after cancel.

        @param actionEvent button click
        @throws IOException filename error */
    public void mainmenuview(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/views/mainMenuView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /** Method to return to the main menu. This method returns to the main menu after save.

        @param actionEvent button click
        @param save will return to the main menu if true or prompt user if false
        @throws IOException filename error */
    public void mainmenuview(ActionEvent actionEvent, boolean save) throws IOException {
        if(save){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Save successful");
            alert.showAndWait();
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/views/mainMenuView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("One or more errors detected. Not saved.");
            alert.showAndWait();
        }
    }

    /** Method to determine which subclass of part to save. This method calls the appropriate
        subclass method to save a part. Activated when user clicks the save button.

        @param event the save button
        @throws IOException filename error */
    @FXML
    void savemodpart(ActionEvent event) throws IOException {
        switch(sub) {
            case INHOUSE : modIHpart(event);
            break;
            case OUTSOURCED : modOSpart(event);
            break;
        }
    }

    /** Method to save a modified part as outsourced part. This method parses user input from the
     form and modifies the part as an outsourced part. Updates inventory and returns to the main menu.

     @param actionEvent the save button click.
     @throws IOException filename error */
    public void modOSpart(ActionEvent actionEvent) throws IOException {
        boolean bprice = false, binv = false, bmin = false, bmax = false,
                bminmax = false, binvminmax = false;

        id = Integer.parseInt(mpartidTextField.getText());
        name = mpartnameTextField.getText();
        companyName = mpartfieldTextField.getText();
        try {
            price = Math.abs(Double.parseDouble(mpartpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; }
        try{
            inv = Math.abs(Integer.parseInt(mpartinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true; }
        try {
            min = Math.abs(Integer.parseInt(mpartminTextField.getText()));
            if (inv < min) binvminmax = true;
         } catch(NumberFormatException e) { bmin = true; }
        try {
            max = Math.abs(Integer.parseInt(mpartmaxTextField.getText()));
            if (inv > max) binvminmax = true;
            if (max < min) bminmax = true;
        } catch(NumberFormatException e) { bmax = true; }

        osErrorLabels(bmin, bmax, binv, bminmax, binvminmax, bprice);
        if (bprice || binv || bmin || bmax || bminmax || binvminmax) {
            mainmenuview(actionEvent, false); }
        //if any of these errors exist do not allow save
        else {
            Part updatePart = new Outsourced(id, name, price, inv, min, max, companyName);
            Inventory.updatePart(index, updatePart);
            mainmenuview(actionEvent, true);
        }
    }

    /** Method to save a modified part as inhouse part. This method parses user input from the form and
        modifies the part as an inhouse part. Updates inventory and returns to the main menu.
     <p>
     This method produced an error at runtime when I first built the application. Despite the program's
     logic to determine which subclass of part to modify, this method was selected every time,
     generating null pointer exceptions and producing incorrect results. I was unable to save modified
     parts at all, then when I made changes to try to identify the error, was only able to save valid
     inhouse parts. First, I followed the stack trace to determine which line was generating the error.
     Then, I stepped through the program logic by hand. I set break points in the program and commented
     out segments as needed to troubleshoot the problem. Eventually I discovered the cause of the issue:
     a mismatched fx:id. After correcting the mismatch, the error was resolved.
    </p>

     @param actionEvent the save button click.
     @throws IOException filename error */
    @FXML
    public void modIHpart(ActionEvent actionEvent) throws IOException {
        boolean bprice = false, binv = false, bmin = false, bmax = false, bminmax = false, binvminmax = false, bmachid = false;
        id = Integer.parseInt(mpartidTextField.getText());
        name = mpartnameTextField.getText();
        try {
            price = Math.abs(Double.parseDouble(mpartpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; }
        try {
            inv = Math.abs(Integer.parseInt(mpartinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true; }
        try {
            min = Math.abs(Integer.parseInt(mpartminTextField.getText()));
            if(inv < min) binvminmax = true;
        } catch(NumberFormatException e) { bmin = true; }
        try {
            max = Math.abs(Integer.parseInt(mpartmaxTextField.getText()));
            if(max < min) bminmax = true;
            if(inv > max) binvminmax = true;
        } catch(NumberFormatException e) { bmax = true; }
        try {
            machid = Math.abs(Integer.parseInt(mpartfieldTextField.getText()));
        } catch(NumberFormatException e) { bmachid = true; }

        ihErrorLabels(bmin, bmax, binv, bmachid, bminmax, binvminmax, bprice);
        if (bmin || bmax || binv || bmachid || bminmax || binvminmax || bprice) {
            mainmenuview(actionEvent, false); }

        else {
            Part updatePart = new InHouse(id, name, price, inv, min, max, machid);
            Inventory.updatePart(index, updatePart);
            mainmenuview(actionEvent, true);
        }
    }

    /** Initialize method for the modify part controller class.

        @param resourceBundle the resource bundle
        @param url the URL */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    /** Method to retrieve data from the selected part and populate fields that the user may edit.

        @param selected the selected part. */
    public void sendData(Part selected) {

        index = Inventory.getAllParts().indexOf(selected);
        if (selected instanceof InHouse) {
            inhouseRB.setSelected(true);
            setTextFieldsIH(selected);
            fieldlbl.setText("Machine ID");
            sub = Sub.INHOUSE;
        }
        else if (selected instanceof Outsourced) {
            outsourcedRB.setSelected(true);
            setTextFieldsOS(selected);
            fieldlbl.setText("Company name");
            sub = Sub.OUTSOURCED;
        }

    }

    /** Method to generate descriptive input error messages for outsourced part creation.

        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be numeric */
    private void osErrorLabels(boolean minint, boolean maxint, boolean invint, boolean minmax,
                                boolean invminmax, boolean pricenum) {
        if(minint) {minintlbl.setText("Min must be an integer");} else {minintlbl.setText("");}
        if(maxint) {maxintlbl.setText("Max must be an integer");} else {maxintlbl.setText("");}
        if(invint) {invintlbl.setText("Inv must be an integer");} else {invintlbl.setText("");}
        if(minmax) {minmaxlbl.setText("Max must be greater than min");} else {minmaxlbl.setText("");}
        if(invminmax) {invminmaxlbl.setText("Inv must be between max and min values");} else {invminmaxlbl.setText("");}
        if(pricenum) {pricenumlbl.setText("Price must be a numeric (decimal) value");} else {pricenumlbl.setText("");}
    }

    /** Method to generate descriptive input error messages for in house part creation.

        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param machidint machine id must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be numeric */
    private void ihErrorLabels(boolean minint, boolean maxint, boolean invint, boolean machidint,
                                boolean minmax, boolean invminmax, boolean pricenum) {
        if(minint) {minintlbl.setText("Min must be an integer");} else {minintlbl.setText("");}
        if(maxint) {maxintlbl.setText("Max must be an integer");} else {maxintlbl.setText("");}
        if(invint) {invintlbl.setText("Inv must be an integer");} else {invintlbl.setText("");}
        if(minmax) {minmaxlbl.setText("Max must be greater than min");} else {minmaxlbl.setText("");}
        if(invminmax) {invminmaxlbl.setText("Inv must be between max and min values");} else {invminmaxlbl.setText("");}
        if(pricenum) {pricenumlbl.setText("Price must be a numeric (decimal) value");} else {pricenumlbl.setText("");}
        if (machidint) {machidintlbl.setText("Machine ID must be an integer");} else {machidintlbl.setText("");}
    }

    /** Method to populate fields in the table when an in house part is loaded. Populates fields
        with current data which user may modify. Populates ID field with the part ID which will
        be retained in the modified part.

        @param selected the selected part */
    private void setTextFieldsIH(Part selected)
    {
        mpartidTextField.setText(String.valueOf(selected.getId()));
        mpartnameTextField.setText(selected.getName());
        mpartinvTextField.setText(String.valueOf(selected.getStock()));
        mpartmaxTextField.setText(String.valueOf(selected.getMax()));
        mpartminTextField.setText(String.valueOf(selected.getMin()));
        mpartpriceTextField.setText(String.valueOf(selected.getPrice()));
        mpartfieldTextField.setText(String.valueOf(((InHouse)(selected)).getMachineID()));
    }

    /** Method to populate fields in the table when an outsourced part is loaded.
        Populates fields with current data which user may modify. Populates ID field
        with the part ID which will be retained in the modified part.

        @param selected the selected part */
    private void setTextFieldsOS(Part selected)
    {
        mpartidTextField.setText(String.valueOf(selected.getId()));
        mpartnameTextField.setText(selected.getName());
        mpartinvTextField.setText(String.valueOf(selected.getStock()));
        mpartmaxTextField.setText(String.valueOf(selected.getMax()));
        mpartminTextField.setText(String.valueOf(selected.getMin()));
        mpartpriceTextField.setText(String.valueOf(selected.getPrice()));
        mpartfieldTextField.setText(((Outsourced)(selected)).getCompanyName());
    }

    /** Method to set appropriate fields in the form when the user clicks a radio button.
        Changing between an in house and an outsourced part will set certain areas of the
        form to accept different input.

        @param actionEvent the radio button */
    @FXML
    public void changeview(ActionEvent actionEvent) {
        if(outsourcedRB.isSelected()) {
            fieldlbl.setText("Company name");
            sub = Sub.OUTSOURCED;

        }
        else if (inhouseRB.isSelected()) {
            fieldlbl.setText("Machine ID");
            sub = Sub.INHOUSE;
        }
    }
}
