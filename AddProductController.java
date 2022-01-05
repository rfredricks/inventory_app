package controller;

import helper.IdGenerator;
import helper.PartSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for add product menu.
    @author Rebecca Fredricks */
public class AddProductController implements Initializable {

    @FXML
    private TextField prodidTextField;
    @FXML
    private TextField addprodnameTextField;
    @FXML
    private TextField addprodinvTextField;
    @FXML
    private TextField addprodpriceTextField;
    @FXML
    private TextField addprodmaxTextField;
    @FXML
    private TextField addprodminTextField;
    @FXML
    private Label invintlbl;
    @FXML
    private Label invminmaxlbl;
    @FXML
    private Label pricenumlbl;
    @FXML
    private Label maxintlbl;
    @FXML
    private Label minintlbl;
    @FXML
    private Label minmaxlbl;
    @FXML
    private TextField addprodsearchTextField;
    @FXML
    private TableView<Part> allpartTV;
    @FXML
    private TableColumn<Part, Integer> allpartidTC;
    @FXML
    private TableColumn<Part, String> allpartnameTC;
    @FXML
    private TableColumn<Part, Integer> allpartinvTC;
    @FXML
    private TableColumn<Part, Double> allpartpriceTC;
    @FXML
    private TableView<Part> aspartTV;
    @FXML
    private TableColumn<Part, Integer> aspartidTC;
    @FXML
    private TableColumn<Part, String> aspartnameTC;
    @FXML
    private TableColumn<Part, Integer> aspartinvTC;
    @FXML
    private TableColumn<Part, Double> aspartpriceTC;

    //private instance variables
    private Stage stage;
    private Parent scene;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    ObservableList<Part> result = FXCollections.observableArrayList();

    /** Method to add an associated part to the product. The user selects a part to add
        and clicks the add button.

        @param event the add button */
    @FXML
    void addpart(ActionEvent event) {
        if (allpartTV.getSelectionModel().getSelectedItem() != null) {
            associatedParts.add(allpartTV.getSelectionModel().getSelectedItem());
        } else noSelection();
    }

    /** Method to search for parts based on user-entered search string. User enters an id or
     part name (or name substring) and sees a list of parts filtered by search term.

     @param event the user input event. */
    @FXML
    void addprodsearchparts(ActionEvent event) {

        ObservableList<Part> result = PartSearch.partSearch(addprodsearchTextField.getText());
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Part not found");
            alert.showAndWait();
        } else {
            allpartTV.setItems(result);
            allpartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
            allpartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
            allpartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock"));
            allpartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /** Display an error message when called. */
    public void noSelection(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please make a selection");
        alert.showAndWait();
    }

    /** Method to return to the main menu. This method returns to the main menu after cancel.

     @param event the cancel button click
     @throws IOException filename error */
    @FXML
    void mainmenuview(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/views/mainMenuView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** Method to return to the main menu. This method returns to the main menu after save.

     @param actionEvent the save button click
     @param save save successful
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

    /** Method to remove an associated part from the product. The user selects a part to
        remove and clicks the remove associated part button.
     @param event the remove associated part button */
    @FXML
    void removepart(ActionEvent event) {
        if (aspartTV.getSelectionModel().getSelectedItem() != null ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove selected associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(aspartTV.getSelectionModel().getSelectedItem());
            }
        } else { noSelection();
        }
    }

    /** Method to save a new product into the inventory. This method takes user input from
     the form and adds the product to the inventory, then returns to the main menu if successful.

     @param event the save button
     @throws IOException filename error */
    @FXML
    void saveprod(ActionEvent event) throws IOException {
        String name;
        double price;
        int min, max, inv;
        boolean bprice = false, binv = false, bmin = false, bmax = false, bminmax = false, binvminmax = false;

        name = addprodnameTextField.getText();
        try {
            price = Math.abs(Double.parseDouble(addprodpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; price = -1; }
        try{
            inv = Math.abs(Integer.parseInt(addprodinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true;  inv = -1; }
        try {
            min = Math.abs(Integer.parseInt(addprodminTextField.getText()));
            if (inv < min) binvminmax = true;
        } catch(NumberFormatException e) { bmin = true; min = -1; }
        try {
            max = Math.abs(Integer.parseInt(addprodmaxTextField.getText()));
            if (inv > max) binvminmax = true;
            if (max < min) bminmax = true;
        } catch(NumberFormatException e) { bmax = true; max = -1; }
        errorLabels(bmin, bmax, binv, bminmax, binvminmax, bprice);
        if (bprice || binv || bmin || bmax || bminmax || binvminmax) {
            mainmenuview(event, false);
            return; }
        //if any of these errors exist do not allow save

        Product product = new Product(name, price, inv, min, max);
        for (Part part : associatedParts){
            product.addAssociatedPart(part);
        }
        Inventory.addProduct(product);

        mainmenuview(event, true);
    }

    /** Initialize method for the add product controller class.

     @param resourceBundle the resource bundle
     @param url the URL */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        associatedParts.clear();
        errorLabels(false, false, false, false, false, false);
        prodidTextField.setText(String.valueOf(IdGenerator.getCurrentProductID()));

        allpartTV.setItems(Inventory.getAllParts());
        allpartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        allpartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        allpartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allpartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));

        aspartTV.setItems(associatedParts);
        aspartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        aspartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        aspartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aspartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /** Method to generate descriptive error messages for product creation. Messages are
        generated based on user input if any input errors are detected.

        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be a numeric value */
    private void errorLabels(boolean minint, boolean maxint, boolean invint,
                             boolean minmax, boolean invminmax, boolean pricenum){
        if(minint) minintlbl.setText("Min must be an integer"); else minintlbl.setText("");
        if(maxint) maxintlbl.setText("Max must be an integer"); else maxintlbl.setText("");
        if(invint) invintlbl.setText("Inv must be an integer"); else invintlbl.setText("");
        if(minmax) minmaxlbl.setText("Min must be less than max"); else minmaxlbl.setText("");
        if(invminmax) invminmaxlbl.setText("Inv must be between min and max"); else invminmaxlbl.setText("");
        if(pricenum) pricenumlbl.setText("Price must be a numeric (decimal) value"); else pricenumlbl.setText("");
    }
}
