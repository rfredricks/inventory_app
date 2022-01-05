package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Driver class for Inventory Management System application.
    @author Rebecca Fredricks */
public class Main extends Application {

    /** Start method for main class.
        @param primaryStage the primary stage */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/mainMenuView.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 900  , 400));
        primaryStage.show();
    }

    /** Main method.

        In the next version of the Inventory Management System application, a compatible
        feature that would extend its functionality is the ability to create and export
        reports based on user-entered data. This feature would enhance the program's
        usability and create additional value for the user in the form of information,
        which can be used to make business decisions.

        @param args command line argument */
    public static void main(String[] args) {
        launch(args);
    }
}
