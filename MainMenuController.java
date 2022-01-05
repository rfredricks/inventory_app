package controller;

import helper.PartSearch;
import helper.ProdSearch;
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

/** Main menu controller class.
    @author Rebecca Fredricks */
public class MainMenuController implements Initializable {

    @FXML
    public TextField mainpartsearchTextField;
    @FXML
    public TableView<Part> mainpartTableView;
    @FXML
    public TableColumn<Part, Integer> mainpartidTableColumn;
    @FXML
    public TableColumn<Part, String> mainpartnameTableColumn;
    @FXML
    public TableColumn<Part, Integer> mainpartinvTableColumn;
    @FXML
    public TableColumn<Part, Double> mainpartpriceTableColumn;
    @FXML
    public TextField mainprodsearchTextField;
    @FXML
    public TableView<Product> mainprodTableView;
    @FXML
    public TableColumn<Product, Integer> mainprodidTableColumn;
    @FXML
    public TableColumn<Product, String> mainprodnameTableColumn;
    @FXML
    public TableColumn<Product, Integer> mainprodinvTableColumn;
    @FXML
    public TableColumn<Product, Double> mainprodpriceTableColumn;

    //private instance variables
    private Stage stage;
    private Parent scene;

    /** Method to search for a part based on a user-entered search string. User enters
     an id or part name (or substring) and sees a list of parts matching the search term.
     @param actionEvent the user input event */
    public void partsearch(ActionEvent actionEvent) {
        ObservableList<Part> result = PartSearch.partSearch(mainpartsearchTextField.getText());
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Part not found");
            alert.showAndWait();
        } else {
            mainpartTableView.setItems(result);
            mainpartidTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            mainpartnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            mainpartinvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            mainpartpriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /** Method to navigate to the add part menu. Opens an empty menu for
        the user to enter new part data.
        @param actionEvent the add part button
        @throws IOException filename error */
    public void addpartview(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/views/addPartView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method to navigate to the modify part menu. Opens the user's selected
        part in the modify part menu.
        @param actionEvent the modify part button
        @throws IOException filename error */
    public void modpartview(ActionEvent actionEvent) throws IOException {
        if (mainpartTableView.getSelectionModel().getSelectedItem() != null ) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/modifyPartView.fxml"));
            loader.load();
            ModifyPartController mprtcontroller = loader.getController();
            mprtcontroller.sendData(mainpartTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            noSelection();
        }
    }

    /** Method to delete a selected part from inventory. This method deletes the part selected by the user.
        @param event the part delete button */
    public void maindeletepart(ActionEvent event) {
        if (mainpartTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.getAllParts().remove(mainpartTableView.getSelectionModel().getSelectedItem());
            }
        } else {
            noSelection();
        }
    }

    /** Method to prompt the user to make a selection. If a modify or delete button is
        clicked without an item selected to modify or delete, the user will be prompted
        to make a selection. */
    public void noSelection(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please make a selection");
        alert.showAndWait();
    }

    /** Method to delete a selected product from inventory. This method deletes the product selected by the user.
        @param event the product delete button */
    public void maindeleteproduct(ActionEvent event) {
        if (mainprodTableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                if(mainprodTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {
                    Inventory.getAllProducts().remove(mainprodTableView.getSelectionModel().getSelectedItem());
                }
                else {
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.setContentText("Product has associated parts. Delete not allowed.");
                    alert1.showAndWait();
                }
            }
        } else {
            noSelection();
        }
    }

    /** Method to search for a product based on a user-entered search string. User enters
        an id or product name (or substring) and sees a list of products filtered by search term.
        @param actionEvent the user input event */
    public void prodsearch(ActionEvent actionEvent) {
        ObservableList<Product> result = ProdSearch.prodSearch(mainprodsearchTextField.getText());
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Product not found");
            alert.showAndWait();
        } else {
            mainprodTableView.setItems(result);
            mainprodidTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            mainprodnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            mainprodinvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            mainprodpriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
    }

    /** Method to navigate to the add product menu. Opens an empty menu for the user
        to enter new product data.
        @param actionEvent the add product button
        @throws IOException filename error */
    public void addprodview(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/views/addProductView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method to navigate to the modify product menu. Opens the user's selected
        product in the modify product menu.
        @param actionEvent the modify product button
        @throws IOException filename error */
    public void modprodview(ActionEvent actionEvent) throws IOException {
        if (mainprodTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/modifyProductView.fxml"));
            loader.load();

            ModifyProductController mprdcontroller = loader.getController();

            mprdcontroller.sendData(mainprodTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            noSelection();
        }

    }

    /** Method to terminate the program. Terminates the program after user clicks the
        exit button.
        @param actionEvent the exit button */
    public void mainexit(ActionEvent actionEvent) {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage.close();
        }
    }

    /** Initialize method for the main menu controller class.
     @param resourceBundle the resource bundle
     @param url the URL */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        mainpartTableView.setItems(Inventory.getAllParts());
        mainpartidTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainpartinvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainpartnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainpartpriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainprodTableView.setItems(Inventory.getAllProducts());
        mainprodidTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainprodinvTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainprodnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainprodpriceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
