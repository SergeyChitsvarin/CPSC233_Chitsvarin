package mvh.app;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvh.util.Reader;
import mvh.world.*;

import java.io.File;

public class MainController {

    //Store the data of editor
    private World world;

    @FXML
    private MenuItem aboutAlert;

    @FXML
    private Button createNewWorld;

    @FXML
    private MenuItem loadButton;

    @FXML
    private MenuItem quitButton;

    @FXML
    private TextField rowsTextField;

    @FXML
    private TextField columnsTextField;

    /**
     * Setup the window state
     */

    private Stage controllerStage;
    public void setControllerStage(Stage stage) {
        controllerStage = stage;
    }

    @FXML
    public void initialize() {

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser myFileChooser = new FileChooser();
                File selectedFile = myFileChooser.showOpenDialog(controllerStage);

                World worldFromFile = Reader.loadWorld(selectedFile);

                int rows = worldFromFile.getRows();
            }
        });

        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        aboutAlert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
                aboutAlert.setTitle("About");
                aboutAlert.setContentText("Author: Sergey Chitsvarin \nEmail: Sergey.chitsvarin@ucalgary.ca \nVersion: 1.0 \nThis is a world editor for Monsters VS Heroes");
                aboutAlert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        });


        createNewWorld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String askingRows = rowsTextField.getText();
                String askingColumns = columnsTextField.getText();

                System.out.println(askingRows + askingColumns);
                int rowsInt = Integer.parseInt(askingRows);
                int columnsInt = Integer.parseInt(askingColumns);

                World myWorld = new World(rowsInt, columnsInt);
                System.out.println(myWorld);
            }
        });
    }
}
