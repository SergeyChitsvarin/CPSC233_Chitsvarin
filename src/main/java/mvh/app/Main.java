package mvh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    }
}
