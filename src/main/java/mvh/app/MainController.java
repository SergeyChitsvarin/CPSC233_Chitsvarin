package mvh.app;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mvh.enums.Symbol;
import mvh.enums.WeaponType;
import mvh.util.Reader;
import mvh.world.*;

import java.io.File;

public class MainController {

    //Store the data of editor
    private World world;

    @FXML
    private MenuItem club;

    @FXML
    private MenuItem axe;

    @FXML
    private MenuItem sword;

    @FXML
    private MenuItem aboutAlert;

    @FXML
    private Button createNewWorld;

    @FXML
    private RadioButton heroButton;

    @FXML
    private MenuItem loadButton;

    @FXML
    private MenuItem quitButton;

    @FXML
    private TextField rowsTextField;

    @FXML
    private TextField columnsTextField;

    @FXML
    private TextField symbolTextField;

    @FXML
    private TextField healthTextField;

    @FXML
    private TextField weaponTextField;

    @FXML
    private TextField armourTextField;

    @FXML
    private RadioButton monsterButton;

    @FXML
    private TextField monsterHealthTextField;

    @FXML
    private TextField monsterSymbolTextField;

    @FXML
    private ComboBox weaponDropDownMenu;

    @FXML
    private GridPane worldGrid;

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
                aboutAlert.showAndWait().filter(click -> click == ButtonType.OK);
            }
        });

        createNewWorld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String askingRows = rowsTextField.getText();
                String askingColumns = columnsTextField.getText();

                int rowsInt = Integer.parseInt(askingRows);
                int columnsInt = Integer.parseInt(askingColumns);
                world = new World(rowsInt, columnsInt);


                worldGrid.getChildren().clear();
                for(int row = 0; row < rowsInt; row++) {
                    for(int column = 0; column < columnsInt; column++) {
                        Button button = null;
                        if (world.getEntity(row, column) == null) {
                            button = new Button(String.valueOf(Symbol.FLOOR.getSymbol()));
                        }
                        if (world.isHero(row, column) || world.isMonster(row, column)){
                            button = new Button(String.valueOf(world.getEntity(row, column).getSymbol()));
                        }

                        button.setPadding(new Insets(3));
                        worldGrid.add(button, row, column);
                    }
                }


                worldGrid.setHgap(3);
                worldGrid.setVgap(3);

            }
        });


        heroButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String askingSymbol = symbolTextField.getText();
                char symbolChar = askingSymbol.charAt(0);

                String askingHealth = healthTextField.getText();
                int healthInt = Integer.parseInt(askingHealth);

                String askingWeaponStrength = weaponTextField.getText();
                int weaponStrength = Integer.parseInt(askingWeaponStrength);

                String askingArmourStrength = armourTextField.getText();
                int armourStrength = Integer.parseInt(askingArmourStrength);
                Entity hero = new Hero(healthInt, symbolChar, weaponStrength, armourStrength);
                world.addEntity(0, 0, hero);
                System.out.println("done");
            }
        });

        monsterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String askingSymbol = monsterSymbolTextField.getText();
                char symbolChar = askingSymbol.charAt(0);

                String askingHealth = monsterHealthTextField.getText();
                int healthInt = Integer.parseInt(askingHealth);

                Object selected = weaponDropDownMenu.getValue();
                char selectedChar = selected.toString().charAt(0);
                WeaponType selectedWeapon = WeaponType.getWeaponType(selectedChar);

                Entity monster = new Monster(healthInt, symbolChar, selectedWeapon);
                world.addEntity(0, 0, monster);
                System.out.println("done");
            }
        });
    }
}
