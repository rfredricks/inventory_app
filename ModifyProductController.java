package controller;

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

/** Controller class for modify product menu.
    @author Rebecca Fredricks */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField modprodidTextField;
    @FXML
    private TextField modprodnameTextField;
    @FXML
    private TextField modprodinvTextField;
    @FXML
    private TextField modprodpriceTextField;
    @FXML
    private TextField modprodmaxTextField;
    @FXML
    private TextField modprodminTextField;
    @FXML
    private TextField modprodsearchTextField;
    @FXML
    private TableView<Part> mprodallpartTV;
    @FXML
    private TableColumn<Part, Integer> mprodallpartidTC;
    @FXML
    private TableColumn<Part, String> mprodallpartnameTC;
    @FXML
    private TableColumn<Part, Integer> mprodallpartinvTC;
    @FXML
    private TableColumn<Part, Double> mprodallpartpriceTC;
    @FXML
    private TableView<Part> mprodaspartTV;
    @FXML
    private TableColumn<Part, Integer> mprodaspartidTC;
    @FXML
    private TableColumn<Part, String> mprodaspartnameTC;
    @FXML
    private TableColumn<Part, Integer> mprodaspartinvTC;
    @FXML
    private TableColumn<Part, Double> mprodaspartpriceTC;
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

    //index of selected item
    private int index;
    private Stage stage;
    private Parent scene;
    private ObservableList<Part> asparts = FXCollections.observableArrayList();

    /** Method to add an associated part to the product being modified. Adds the user-selected
        part to the product's associated parts list.

        @param event the user input event */
    public void addpart(ActionEvent event) {
        if (mprodallpartTV.getSelectionModel().getSelectedItem() != null) {
            asparts.add(mprodallpartTV.getSelectionModel().getSelectedItem());
        } else { noSelection();
        }
    }

    /** Display an error message when called. */
    public void noSelection(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please make a selection");
        alert.showAndWait();
    }

    /** Method to return to the main menu. This method returns to the main menu after cancel.

        @param event the cancel button
        @throws IOException filename error */
    public void mainmenuview(ActionEvent event) throws IOException {
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

     @param actionEvent button click
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

    /** Method to save a modified product into the inventory. This method takes user input from
        the form and updates the product in the inventory, then returns to the main menu if successful.

        @param event the save button
        @throws IOException filename error */
    public void onactionsave(ActionEvent event) throws IOException {
        String name;
        double price;
        int id, min, max, inv;
        boolean bprice = false, binv = false, bmin = false, bmax = false, bminmax = false, binvminmax = false;
        try {
            id = Integer.parseInt(modprodidTextField.getText());
        } catch(NumberFormatException e) { return; }
        name = modprodnameTextField.getText();
        try {
            price = Math.abs(Double.parseDouble(modprodpriceTextField.getText()));
        } catch(NumberFormatException e) { bprice = true; price = -1; }
        try{
            inv = Math.abs(Integer.parseInt(modprodinvTextField.getText()));
        } catch(NumberFormatException e) { binv = true;  inv = -1; }
        try {
            min = Math.abs(Integer.parseInt(modprodminTextField.getText()));
            if (inv < min) binvminmax = true;
        } catch(NumberFormatException e) { bmin = true; min = -1; }
        try {
            max = Math.abs(Integer.parseInt(modprodmaxTextField.getText()));
            if (inv > max) binvminmax = true;
            if (max < min) bminmax = true;
        } catch(NumberFormatException e) { bmax = true; max = -1; }
        errorLabels(bmin, bmax, binv, bminmax, binvminmax, bprice);
        if (bprice || binv || bmin || bmax || bminmax || binvminmax) {
            mainmenuview(event, false);
            return; }
        //if any of these errors exist do not allow save

        Product updateProduct = new Product(id, name, price, inv, min, max);
        for (Part part : asparts){
            updateProduct.addAssociatedPart(part);
        }
        Inventory.updateProduct(index, updateProduct);

        mainmenuview(event, true);

    }

    /** Method to remove an associated part from the product being modified. Removes the user-selected
        associated part from the associated parts list.

        @param event the user input event */
    public void removepart(ActionEvent event) {
        if (mprodaspartTV.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove selected associated part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                asparts.remove(mprodaspartTV.getSelectionModel().getSelectedItem());
            }
        } else { noSelection();
        }
    }

    /** Method to search for parts based on user-entered search string. User enters an id or
        part name (or name substring) and sees a list of parts filtered by search term.

        @param event the user input event. */
    public void searchparts(ActionEvent event) {

        ObservableList<Part> result = PartSearch.partSearch(modprodsearchTextField.getText());
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Part not found");
            alert.showAndWait();
        } else {
            mprodallpartTV.setItems(result);
            mprodallpartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
            mprodallpartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
            mprodallpartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock"));
            mprodallpartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /** Initialize method for the modify product controller class.

     @param resourceBundle the resource bundle
     @param url the URL */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        mprodallpartTV.setItems(Inventory.getAllParts());
        mprodallpartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        mprodallpartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        mprodallpartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock")); //?
        mprodallpartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));

        errorLabels(false, false, false, false, false, false);

    }

    /** Method to generate descriptive error messages for product creation. Messages are generated based
        on user input if input errors are detected.

        @param minint min must be an integer
        @param maxint max must be an integer
        @param invint inv must be an integer
        @param minmax min must be less than max
        @param invminmax inv must be between min and max
        @param pricenum price must be a numeric value */
    private void errorLabels(boolean minint, boolean maxint, boolean invint, boolean minmax, boolean invminmax, boolean pricenum) {
        if(minint) minintlbl.setText("Min must be an integer"); else minintlbl.setText("");
        if(maxint) maxintlbl.setText("Max must be an integer"); else maxintlbl.setText("");
        if(invint) invintlbl.setText("Inv must be an integer"); else invintlbl.setText("");
        if(minmax) minmaxlbl.setText("Max must be greater than min"); else minmaxlbl.setText("");
        if(invminmax) invminmaxlbl.setText("Inv must be between max and min"); else invminmaxlbl.setText("");
        if(pricenum) pricenumlbl.setText("Price must be a numeric (decimal) value"); else pricenumlbl.setText("");
    }

    /** Method to retrieve data from the selected product and populate fields which
        the user may edit. Populates text fields and the associated parts TableView with
        current data from the selected product, including the product ID which will be
        retained with the modified product.

        @param selectedItem the selected product */
    public void sendData(Product selectedItem) {

        index = Inventory.getAllProducts().indexOf(selectedItem);
        asparts.clear();
        asparts = selectedItem.getAllAssociatedParts();
        mprodaspartTV.getItems().clear();
        mprodaspartTV.refresh();

        modprodidTextField.setText(String.valueOf(selectedItem.getId()));
        modprodnameTextField.setText(selectedItem.getName());
        modprodinvTextField.setText(String.valueOf(selectedItem.getStock()));
        modprodpriceTextField.setText(String.valueOf(selectedItem.getPrice()));
        modprodmaxTextField.setText(String.valueOf(selectedItem.getMax()));
        modprodminTextField.setText(String.valueOf(selectedItem.getMin()));

        mprodaspartTV.setItems(asparts);
        mprodaspartidTC.setCellValueFactory(new PropertyValueFactory<>("id"));
        mprodaspartnameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        mprodaspartinvTC.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mprodaspartpriceTC.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
