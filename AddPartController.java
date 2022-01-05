package controller;

import helper.IdGenerator;
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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for addPartView.
    @author Rebecca Fredricks */
public class AddPartController implements Initializable {

    @FXML
    private TextField apartidTextField;

    @FXML
    private TextField apartnameTextField;

    @FXML
    private TextField apartinvTextField;

    @FXML
    private TextField apartpriceTextField;

    @FXML
    private TextField apartmaxTextField;

    @FXML
    private TextField apartminTextField;

    @FXML
    private TextField apartfieldTextField;
    @FXML
    private Label fieldlbl;
    @FXML
    private RadioButton inhouseRB;

    @FXML
    private ToggleGroup partSource;

    @FXML
    private RadioButton outsourcedRB;
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
    private Label machidlbl;

    // change scene variables
    private Stage stage;
    private Parent scene;
    // input variables

    /** Method to return to the main menu. This method returns to the main menu when the cancel button
         is pressed from the add part menu.
        @param actionEvent the cancel button click.
        @throws IOException filename error */
    @FXML
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
     @param save save successful
     @throws IOException filename error */
    @FXML
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

    /** Method to save a part. Used when the save button is clicked from the add part
        menu. Calls the appropriate method to save either an in house part or an outsourced
        part.
        @param actionEvent the save button click
        @throws IOException filename error */
    @FXML
    public void savepart(ActionEvent actionEvent) throws IOException {
        if(outsourcedRB.isSelected()) {
            saveOSpart(actionEvent);
        }
        else if (inhouseRB.isSelected()) {
            saveIHpart(actionEvent);
        }
    }

    /** Save an outsourced part. This method takes user input from the form, creates an outsourced part,
        and adds it to the inventory.
        @param actionEvent the save button click.
        @throws IOException filename error */
    public void saveOSpart(ActionEvent actionEvent) throws IOException {
        String name, companyName;
        double price;
        int inv, min, max;
        boolean bprice = false, binv = false, bmin = false, bmax = false, bminmax = false, binvminmax = false;
        name = apartnameTextField.getText();
        try { price = Math.abs(Double.parseDouble(apartpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; price = -1; }
        try { inv = Math.abs(Integer.parseInt(apartinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true; inv = -1; }
        try {
            min = Math.abs(Integer.parseInt(apartminTextField.getText()));
            if (inv < min) binvminmax = true;
        } catch(NumberFormatException e) { bmin = true; min = -1; }
        try {
            max = Math.abs(Integer.parseInt(apartmaxTextField.getText()));
            if (max < min) bminmax = true;
            if (max < inv) binvminmax = true;
        } catch(NumberFormatException e) { bmax = true; max = -1; }
        companyName = apartfieldTextField.getText();
        setErrorLabels(bmin, bmax, binv, bminmax, binvminmax, bprice);
        if (bmin || bmax || binv || bminmax || binvminmax || bprice) {
            mainmenuview(actionEvent, false);
            }
        else {
            Inventory.addPart(new Outsourced(name, price, inv, min, max, companyName));
            mainmenuview(actionEvent, true);
        }
    }

    /** Save an inhouse part. This method takes user input from the form, creates an inhouse part,
        and adds it to the inventory.
        @param actionEvent the save button click.
        @throws IOException filename error */
    public void saveIHpart(ActionEvent actionEvent) throws IOException {
        String name;
        double price;
        int inv, min, max, machID;
        boolean bprice = false, binv = false, bmin = false, bmax = false, bminmax = false, binvminmax = false, bmachid = false;
        name = apartnameTextField.getText();
        try { price = Math.abs(Double.parseDouble(apartpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; price = -1; }
        try {
            inv = Math.abs(Integer.parseInt(apartinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true; inv = -1; }
        try {
            min = Math.abs(Integer.parseInt(apartminTextField.getText()));
            if (min > inv) binvminmax = true;
        } catch(NumberFormatException e) { bmin = true; min = -1; }
        try {
            max = Math.abs(Integer.parseInt(apartmaxTextField.getText()));
            if (max < min) bminmax = true;
            if (max < inv) binvminmax = true;
        } catch(NumberFormatException e) { bmax = true; max = -1; }
        try {
            machID = Math.abs(Integer.parseInt(apartfieldTextField.getText()));
        } catch(NumberFormatException e) { bmachid = true; machID = -1; }
        setErrorLabels(bmin, bmax, binv, bmachid, bminmax, binvminmax, bprice);
        if (bmin || bmax || binv || bminmax || bmachid || binvminmax || bprice) {
            mainmenuview(actionEvent, false);
            }
        else {
            Inventory.addPart(new InHouse(name, price, inv, min, max, machID));
            mainmenuview(actionEvent, true);
        }
    }

    /** Initialize method for the add part controller class.
     @param resourceBundle the resource bundle
     @param url the URL */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inhouseRB.setSelected(true);
        apartidTextField.setText(String.valueOf(IdGenerator.getCurrentPartID()));
        fieldlbl.setText("Machine ID");

    }

    /** Method to generate detailed error messages for inhouse part creation. Generates messages based
        on user input if input errors are detected.
        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param machidint machine id must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be a numeric (decimal) value */
    private void setErrorLabels(boolean minint, boolean maxint, boolean invint, boolean machidint,
                               boolean minmax, boolean invminmax, boolean pricenum){
        setErrorLabels(minint, maxint, invint, minmax, invminmax, pricenum);
        if(machidint) machidlbl.setText("Machine ID must be an integer"); else machidlbl.setText("");
    }

    /** Method to generate detailed error messages for outsource part creation. Generates messages
        based on user input if input errors are detected.
        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be a numeric (decimal) value */
    private void setErrorLabels(boolean minint, boolean maxint, boolean invint, boolean minmax,
                               boolean invminmax, boolean pricenum){
        if(minint) minintlbl.setText("Min must be an integer"); else minintlbl.setText("");
        if(maxint) maxintlbl.setText("Max must be an integer"); else maxintlbl.setText("");
        if(invint) invintlbl.setText("Inv must be an integer"); else invintlbl.setText("");
        if(minmax) minmaxlbl.setText("Min must be less than max"); else minmaxlbl.setText("");
        if(invminmax) invminmaxlbl.setText("Inv must be between min and max"); else invminmaxlbl.setText("");
        if(pricenum) pricenumlbl.setText("Price must be a numeric (decimal) value"); else pricenumlbl.setText("");
    }

    /** Method to set appropriate fields in the form when the user clicks a radio button.
     Changing between an in house and an outsourced part will change certain areas of the
     form to require different input.
     @param actionEvent the radio button */
     @FXML
    public void changeview(ActionEvent actionEvent) {
        if (inhouseRB.isSelected()) {
            fieldlbl.setText("Machine ID");
        }
        else if (outsourcedRB.isSelected()) {
            fieldlbl.setText("Company name");
        }
    }

}
