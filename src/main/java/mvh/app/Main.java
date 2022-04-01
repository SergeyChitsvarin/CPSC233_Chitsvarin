package mvh.app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


public class Main extends Application {

    public static final String version = "1.0";

    /**
     * A program-wide random number generator
     */
    public static Random random = new Random(12345);

    public static void main(String[] args) {
        launch(args);
    }

    public static void setWindowTitle(Stage stage){
        stage.setTitle("Monsters VS Heroes V1.0");
    }

//    public static void EntityInfoHero(char symbol, int health, int weaponStrength, int armourStrength){
//        String symbolString = Character.toString(symbol);
//        TextField heroEntityDetails = new TextField("type: "+ symbolString+"\n");
//    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //Students edit here to set up the scene

        MainController appController = fxmlLoader.getController();
        appController.setControllerStage(stage);
        stage.setScene(scene);
        setWindowTitle(stage);

        stage.show();

        Pane root = fxmlLoader.getRoot();
        ComboBox weaponDropDownMenu = (ComboBox) root.lookup("#weaponDropDownMenu");
        weaponDropDownMenu.getItems().add("Axe");
        weaponDropDownMenu.getItems().add("Sword");
        weaponDropDownMenu.getItems().add("Club");

    }
}
