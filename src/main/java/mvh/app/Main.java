package mvh.app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;


/**
 * Name: Sergey Chitsvarin
 * Date: 04/01/2022
 * tutorial: T04
 */

public class Main extends Application {

    public static final String version = "1.0";

    /**
     * A program-wide random number generator
     */
    public static Random random = new Random(12345);

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * method establishes tittle of the GUI
     * @param stage instance of javafx stage class
     */
    public static void setWindowTitle(Stage stage){
        stage.setTitle("Monsters VS Heroes V1.0");
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //Students edit here to set up the scene

        // get controller and assign variable
        MainController appController = fxmlLoader.getController();
        // set up stage for controller
        appController.setControllerStage(stage);

        // set stage scene and window title
        stage.setScene(scene);
        setWindowTitle(stage);

        // show the stage in GUI
        stage.show();

        // get root from fxml loader and assign variable
        Pane root = fxmlLoader.getRoot();

        // find comboBox with the ID weaponDropDownMenu and add weapon type selections
        ComboBox weaponDropDownMenu = (ComboBox) root.lookup("#weaponDropDownMenu");
        weaponDropDownMenu.getItems().add("Axe");
        weaponDropDownMenu.getItems().add("Sword");
        weaponDropDownMenu.getItems().add("Club");

        // find button with ID deleteEntityButton and disable it
        Button deleteEntityButton = (Button) root.lookup("#deleteEntityButton");
        deleteEntityButton.setDisable(true);

    }
}
