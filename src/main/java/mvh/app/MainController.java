package mvh.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

/**
 * Name: Sergey Chitsvarin
 * Date: 04/01/2022
 * tutorial: T04
 */

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
        /**
         * when load button is selected load a file from computer
          */

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // storing selected file in variable
                File selectedFile = getFileForReading();
                // check if user clicked cancel button and does not want to load from file
                if (selectedFile == null){
                    return;
                }

                // create new world using reader class
                world = Reader.loadWorld(selectedFile);
                // draw world in GUI
                redrawWorld();
            }
        });

        /**
         * when user clicks 'save as' button and chooses a file write the world onto that file in correct format
         *
         * references:
         * saving to file: https://www.tutorialspoint.com/Java-Program-to-Append-Text-to-an-Existing-File
         */
        saveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int rows = 0;
                int columns = 0;
                try {
                    // get rows and columns from world
                    rows = world.getRows();
                    columns = world.getColumns();

                }catch (NullPointerException error) {
                    // if world was not created or loaded throw exception with message
                    setErrorStatus("Load or create world first");
                    return;
                }

                try {
                    // get file to write in
                    File selectedFile = getFileForWriting();

                    // if no file was selected stop trying to save file
                    if (selectedFile == null){
                        return;
                    }

                    // create buffered writer and file writer
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(selectedFile));

                    // the first two lines of file are rows and columns on separate lines
                    bufferedWriter.write("" + rows);
                    bufferedWriter.newLine();
                    bufferedWriter.write("" + columns);
                    bufferedWriter.newLine();

                    // loop through the rows and columns of world and type out their values with a comma in between
                    for(int row = 0; row < rows; row++) {
                        for (int column = 0; column < columns; column++) {
                            bufferedWriter.write(row + "," + column);

                            // check if the position has a hero on it
                            if (world.isHero(row, column)){
                                // assign hero to variable
                                Hero myHero = (Hero) world.getEntity(row, column);

                                // get information about the hero
                                String heroSymbol = Character.toString(myHero.getSymbol());
                                String heroHealth = String.valueOf(myHero.getHealth());
                                String heroArmourStrength = String.valueOf(myHero.armorStrength());
                                String heroAttackStrength = String.valueOf(myHero.weaponStrength());

                                // write out the hero's information after it's position
                                bufferedWriter.write(",HERO," + heroSymbol + "," + heroHealth + "," + heroAttackStrength + "," + heroArmourStrength);
                            }

                            // check if the position has a monster on it
                            if (world.isMonster(row, column)){
                                // assign Monster to variable
                                Monster myMonster = (Monster) world.getEntity(row, column);

                                // get information about the monster
                                String monsterSymbol = Character.toString(myMonster.getSymbol());
                                String monsterHealth = String.valueOf(myMonster.getHealth());
                                String monsterWeaponType = String.valueOf(myMonster.getWeaponType());


                                // write out the monster's information after it's position
                                bufferedWriter.write(",MONSTER," + monsterSymbol + "," + monsterHealth + "," + monsterWeaponType.charAt(0));
                            }
                            // go onto next line in file
                            bufferedWriter.newLine();
                        }
                    }
                    // close the file
                    bufferedWriter.close();

                } catch (Exception error) {
                    // general exception for failing to save the file
                    setErrorStatus("Failed to 'Save as' file");
                }

            }
        });
        /**
         * exit out of the GUI with status 0 when quit button is pressed
         */
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        /**
         * show user general information about the author and program when About menu button is pressed
         */
        aboutAlert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // create new alert
                Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
                // set tittle of alert "About"
                aboutAlert.setTitle("About");
                // context of Alert is general information about the program and author
                aboutAlert.setContentText("Author: Sergey Chitsvarin \nEmail: Sergey.chitsvarin@ucalgary.ca \nVersion: 1.0 \nThis is a world editor for Monsters VS Heroes");
                // show button to exit the alert called "ok"
                aboutAlert.showAndWait().filter(click -> click == ButtonType.OK);
            }
        });

        /**
         * create new world when createNewWorld button is pressed
          */
        createNewWorld.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // get rows and columns for the world and check if the input is valid Integers
                String askingRows = rowsTextField.getText();
                boolean askingRowsIsValid = integerIsValid(askingRows, "rows", "input");
                if (askingRowsIsValid == false){
                    return;
                }

                String askingColumns = columnsTextField.getText();
                boolean askingColumnsIsValid = integerIsValid(askingRows, "columns", "input");
                if (askingColumnsIsValid == false){
                    return;
                }

                // convert row and column values from Strings to integers and create new world
                int rowsInt = Integer.parseInt(askingRows);
                int columnsInt = Integer.parseInt(askingColumns);
                world = new World(rowsInt, columnsInt);
                // draw new world in GUI
                redrawWorld();
            }
        });
        /**
         *
         */
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

                String askingHeroSymbol = symbolTextField.getText();
                boolean heroSymbolIsValid = symbolIsValid(askingHeroSymbol, Hero.class.getSimpleName());
                if (heroSymbolIsValid == false) {
                    heroButton.setSelected(false);
                    return;
                }

                String askingHealth = healthTextField.getText();
                boolean heroHealthIsValid = integerIsValid(askingHealth, Hero.class.getSimpleName(), "Health");
                if (heroHealthIsValid == false){
                    heroButton.setSelected(false);
                    return;
                }

                String askingWeaponStrength = weaponTextField.getText();
                boolean heroStrengthIsValid = integerIsValid(askingWeaponStrength, Hero.class.getSimpleName(), "Weapon Strength");
                if (heroStrengthIsValid == false){
                    heroButton.setSelected(false);
                    return;
                }

                String askingArmourStrength = armourTextField.getText();
                boolean heroArmourIsValid = integerIsValid(askingArmourStrength, Hero.class.getSimpleName(), "Armour Strength");
                if (heroArmourIsValid == false){
                    heroButton.setSelected(false);
                    return;
                }

                setInfoStatus("Select position on grid");
                setErrorStatus("");

                char symbolChar = askingHeroSymbol.charAt(0);
                int healthInt = Integer.parseInt(askingHealth);
                int weaponStrength = Integer.parseInt(askingWeaponStrength);
                int armourStrength = Integer.parseInt(askingArmourStrength);

                Entity hero = new Hero(healthInt, symbolChar, weaponStrength, armourStrength);
                entityToAdd = hero;

                clearMonsterInputs();
            }
        });

        monsterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String askingMonsterSymbol = monsterSymbolTextField.getText();
                boolean monsterSymbolIsValid = symbolIsValid(askingMonsterSymbol, Monster.class.getSimpleName());
                if (monsterSymbolIsValid == false) {
                    monsterButton.setSelected(false);
                    return;
                }

                String askingHealth = monsterHealthTextField.getText();
                boolean monsterHealthIsValid = integerIsValid(askingHealth, Monster.class.getSimpleName(), "health");
                if (monsterHealthIsValid == false){
                    monsterButton.setSelected(false);
                    return;
                }

                Object selected = weaponDropDownMenu.getValue();
                if (selected == null){
                    setErrorStatus("select weapon type");
                    monsterButton.setSelected(false);
                    return;
                }

                setInfoStatus("Select position on grid");
                setErrorStatus("");

                char symbolChar = askingMonsterSymbol.charAt(0);
                int healthInt = Integer.parseInt(askingHealth);
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
        setInfoStatus("");

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
                button.setMinHeight(30.0);
                button.setMinWidth(30.0);
                worldGrid.add(button, row, column);
            }
        }

        worldGrid.setHgap(3);
        worldGrid.setVgap(3);
        // reference: creating walls https://www.demo2s.com/java/javafx-gridpane-setborder-border-value.html
        worldGrid.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
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

    private boolean symbolIsValid(String askingHeroSymbol, String entityType){

        if (askingHeroSymbol.equals("")){
            setErrorStatus("Provide a " + entityType + " Symbol");
            return false;
        }

        if (askingHeroSymbol.length() > 1){
            setErrorStatus("Provide a " + entityType + " symbol with one character");
            return false;
        }

        if ((askingHeroSymbol.equals(String.valueOf(Symbol.FLOOR.getSymbol()))) ||
                (askingHeroSymbol.equals(String.valueOf(Symbol.WALL.getSymbol()))) ||
                (askingHeroSymbol.equals(String.valueOf(Symbol.DEAD.getSymbol())))){
            setErrorStatus("Do not use special characters for " + entityType + " symbol");
            return false;
        }

        return true;
    }


    private boolean checkIfInt(String value){
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private boolean integerIsValid(String askingInteger, String entityType, String valueName){

        if (askingInteger.equals("")){
            setErrorStatus("Provide a " + entityType + " " + valueName + " value");
            return false;
        }

        if (checkIfInt(askingInteger) == false){
            setErrorStatus("Provide an integer for " + entityType + " " +  valueName + " value");
            return false;
        }

        if (Integer.parseInt(askingInteger) < 0){
            setErrorStatus("Provide a positive value for " + entityType + " " +valueName + " value");
            return false;
        }

       return true;
    }

}
