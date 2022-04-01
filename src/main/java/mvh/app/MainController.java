package mvh.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvh.enums.Symbol;
import mvh.enums.WeaponType;
import mvh.util.Reader;
import mvh.world.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class MainController {

    //Store the data of editor
    private World world;

    private Entity entityToAdd = null;

    private int rowOfItemToDelete = 0;

    private int columnOfItemToDelete = 0;

    @FXML
    private Button deleteEntityButton;

    @FXML
    private TextArea entityDetailsTextArea;

    @FXML
    private Label leftStatus;

    @FXML
    private Label rightStatus;

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
    private MenuItem saveFile;

    @FXML
    private MenuItem saveAs;

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
                File selectedFile = getFileForReading();
                if (selectedFile == null){return;}

                world = Reader.loadWorld(selectedFile);
                redrawWorld();
            }
        });

        saveFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("saving file!!!");

            }
        });
        /**
         * references:
         * saving to file: https://www.tutorialspoint.com/Java-Program-to-Append-Text-to-an-Existing-File
         */
        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int rows = 0;
                int columns = 0;
                try {
                    rows = world.getRows();
                    columns = world.getColumns();

                }catch (NullPointerException error) {
                    setErrorStatus("Load or create world first");
                    return;
                }

                try {
                    File selectedFile = getFileForWriting();
                    if (selectedFile == null){return;}
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(selectedFile));

                    bufferedWriter.write("" + rows);
                    bufferedWriter.newLine();
                    bufferedWriter.write("" + columns);
                    bufferedWriter.newLine();

                    for(int row = 0; row < rows; row++) {
                        for (int column = 0; column < columns; column++) {
                            bufferedWriter.write(row + "," + column);

                            if (world.isHero(row, column)){
                                Hero myHero = (Hero) world.getEntity(row, column);

                                String heroSymbol = Character.toString(myHero.getSymbol());
                                String heroHealth = String.valueOf(myHero.getHealth());
                                String heroArmourStrength = String.valueOf(myHero.armorStrength());
                                String heroAttackStrength = String.valueOf(myHero.weaponStrength());

                                bufferedWriter.write(",HERO," + heroSymbol + "," + heroHealth + "," + heroAttackStrength + "," + heroArmourStrength);
                            }

                            if (world.isMonster(row, column)){
                                Monster myMonster = (Monster) world.getEntity(row, column);

                                String monsterSymbol = Character.toString(myMonster.getSymbol());
                                String monsterHealth = String.valueOf(myMonster.getHealth());
                                String monsterWeaponType = String.valueOf(myMonster.getWeaponType());

                                bufferedWriter.write(",MONSTER," + monsterSymbol + "," + monsterHealth + "," + monsterWeaponType.charAt(0));
                            }
                            bufferedWriter.newLine();
                        }
                    }
                    bufferedWriter.close();
                    System.out.println("done!!!!");

                } catch (Exception error) {
                    setErrorStatus("Failed to 'Save as' file");
                }

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
                redrawWorld();
            }
        });

        deleteEntityButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String entityToBeDeleted = world.getEntity(rowOfItemToDelete, columnOfItemToDelete).getClass().getSimpleName();
                world.addEntity(rowOfItemToDelete, columnOfItemToDelete, null);

                redrawWorld();
                disableDeleteAndClearDetails();
                setSuccessStatus(entityToBeDeleted + " deleted successfully");
            }
        });

        /**
         *
         */
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
                entityToAdd = hero;

                clearMonsterInputs();
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
                entityToAdd = monster;

                clearHeroInputs();
            }
        });
    }
    private void prepareDeleteAndDetails(Button myButton, Entity myEntity, int rowInGrid, int columnInGrid){
        deleteEntityButton.setDisable(false);

        String sharedDetails = "ID: " + myEntity.shortString() + "\n" + "SYMBOL: "+ myEntity.getSymbol() + "\n" + "HEALTH: " + myEntity.getHealth() + "\n"+ "ALIVE: " + myEntity.isAlive();
        String entityDetails = "";
        if(myEntity instanceof Hero) {
            Hero myHero = (Hero) myEntity;
            entityDetails = "\n" + "WEAPON STRENGTH: " + myHero.weaponStrength() + "\n" + "ARMOUR STRENGTH: " + myHero.armorStrength();
        }
        if(myEntity instanceof Monster) {
            Monster myMonster = (Monster) myEntity;
            entityDetails = "\n" + "WEAPON TYPE: " + myMonster.getWeaponType();
        }

        entityDetailsTextArea.setText(sharedDetails + entityDetails);

        rowOfItemToDelete = rowInGrid;
        columnOfItemToDelete = columnInGrid;

    }

    private void disableDeleteAndClearDetails(){
        deleteEntityButton.setDisable(true);
        entityDetailsTextArea.setText("");

        rowOfItemToDelete = 0;
        columnOfItemToDelete = 0;
    }

    private void clearMonsterInputs(){
        monsterButton.setSelected(false);
        monsterSymbolTextField.setText("");
        monsterHealthTextField.setText("");
        weaponDropDownMenu.setValue("");
    }

    private void clearHeroInputs(){
        heroButton.setSelected(false);
        symbolTextField.setText("");
        healthTextField.setText("");
        weaponTextField.setText("");
        armourTextField.setText("");
    }

    private void setSuccessStatus(String message){
        leftStatus.setTextFill(Paint.valueOf("green"));
        leftStatus.setText(message);
    }

    private void setErrorStatus(String message){
        leftStatus.setTextFill(Paint.valueOf("red"));
        leftStatus.setText(message);
    }

    private void setInfoStatus(String message){
        rightStatus.setText(message);
    }

    private void redrawWorld(){
       int rowsInt = world.getRows();
       int columnsInt = world.getColumns();

        disableDeleteAndClearDetails();
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
                final int rowInGrid = row;
                final int columnInGrid = column;
                final Button buttonInGrid = button;
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Entity entityInButton = world.getEntity(rowInGrid, columnInGrid);
                        if (world.isHero(rowInGrid, columnInGrid) || world.isMonster(rowInGrid, columnInGrid)){

                            prepareDeleteAndDetails(buttonInGrid, entityInButton, rowInGrid, columnInGrid);

                            return;
                        }

                        if ((entityInButton == null) && (entityToAdd != null)){
                            world.addEntity(rowInGrid, columnInGrid, entityToAdd);
                            String entityClass = entityToAdd.getClass().getSimpleName();
                            setSuccessStatus("Added New " + entityClass);

                            String entitySymbol = Character.toString(entityToAdd.getSymbol());
                            buttonInGrid.setText(entitySymbol);

                            prepareDeleteAndDetails(buttonInGrid, entityToAdd, rowInGrid, columnInGrid);
                            clearHeroInputs();
                            clearMonsterInputs();

                            entityToAdd = null;
                            return;
                        }

                        if ((entityInButton == null) && (entityToAdd == null)){
                            disableDeleteAndClearDetails();
                            return;
                        }
                    }
                });
                button.setPadding(new Insets(3));
                worldGrid.add(button, row, column);
            }
        }

        worldGrid.setHgap(3);
        worldGrid.setVgap(3);
        setSuccessStatus("New World Created");
    }

    private File getFileForReading(){
        FileChooser myFileChooser = new FileChooser();
        File selectedFile = myFileChooser.showOpenDialog(controllerStage);
        return selectedFile;
    }

    private File getFileForWriting(){
        FileChooser myFileChooser = new FileChooser();
        File selectedFile = myFileChooser.showSaveDialog(controllerStage);
        return selectedFile;
    }

}
