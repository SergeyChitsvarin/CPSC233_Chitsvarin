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
                // get entity to be deleted from the world
                String entityToBeDeleted = world.getEntity(rowOfItemToDelete, columnOfItemToDelete).getClass().getSimpleName();
                // delete entity from world
                world.addEntity(rowOfItemToDelete, columnOfItemToDelete, null);

                // redraw world
                redrawWorld();
                // disable button and clear details of deleted entity as well as add status message
                disableDeleteAndClearDetails();
                setSuccessStatus(entityToBeDeleted + " deleted successfully");
            }
        });

        /**
         * when hero button is pressed gather all the information from text fields and create new entity hero
         */
        heroButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                // get text from hero related text fields and check to see if the input is valid
                String askingHeroSymbol = symbolTextField.getText();
                boolean heroSymbolIsValid = symbolIsValid(askingHeroSymbol, Hero.class.getSimpleName());
                if (heroSymbolIsValid == false) {
                    // make button un selected
                    heroButton.setSelected(false);
                    return;
                }

                String askingHealth = healthTextField.getText();
                boolean heroHealthIsValid = integerIsValid(askingHealth, Hero.class.getSimpleName(), "Health");
                if (heroHealthIsValid == false){
                    // make button un selected
                    heroButton.setSelected(false);
                    return;
                }

                String askingWeaponStrength = weaponTextField.getText();
                boolean heroStrengthIsValid = integerIsValid(askingWeaponStrength, Hero.class.getSimpleName(), "Weapon Strength");
                if (heroStrengthIsValid == false){
                    // make button un selected
                    heroButton.setSelected(false);
                    return;
                }

                String askingArmourStrength = armourTextField.getText();
                boolean heroArmourIsValid = integerIsValid(askingArmourStrength, Hero.class.getSimpleName(), "Armour Strength");
                if (heroArmourIsValid == false){
                    // make button un selected
                    heroButton.setSelected(false);
                    return;
                }

                // set info status for clues on choosing a position to place hero
                setInfoStatus("Select position on grid");
                // clear error status
                setErrorStatus("");

                // convert input from String to correct type and use info to create hero
                char symbolChar = askingHeroSymbol.charAt(0);
                int healthInt = Integer.parseInt(askingHealth);
                int weaponStrength = Integer.parseInt(askingWeaponStrength);
                int armourStrength = Integer.parseInt(askingArmourStrength);

                Entity hero = new Hero(healthInt, symbolChar, weaponStrength, armourStrength);
                // change entityToAdd value
                entityToAdd = hero;

                // clear Monster related text fields
                clearMonsterInputs();
            }
        });

        /**
         * when hero button is pressed gather all the information from text fields and create new entity hero
         */
        monsterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // get inputs from monster related text fields and check if those inputs are valid
                String askingMonsterSymbol = monsterSymbolTextField.getText();
                boolean monsterSymbolIsValid = symbolIsValid(askingMonsterSymbol, Monster.class.getSimpleName());
                if (monsterSymbolIsValid == false) {
                    // make button un selected
                    monsterButton.setSelected(false);
                    return;
                }

                String askingHealth = monsterHealthTextField.getText();
                boolean monsterHealthIsValid = integerIsValid(askingHealth, Monster.class.getSimpleName(), "health");
                if (monsterHealthIsValid == false){
                    // make button un selected
                    monsterButton.setSelected(false);
                    return;
                }

                Object selected = weaponDropDownMenu.getValue();
                if (selected == null){
                    // set error status as "select weapon type"
                    setErrorStatus("select weapon type");
                    // make button un selected
                    monsterButton.setSelected(false);
                    return;
                }

                // set info status for clues on placing monster on grid
                setInfoStatus("Select position on grid");
                // clear error status
                setErrorStatus("");

                // changing type of input from strings into desired type and creating new Monster
                char symbolChar = askingMonsterSymbol.charAt(0);
                int healthInt = Integer.parseInt(askingHealth);
                char selectedChar = selected.toString().charAt(0);
                WeaponType selectedWeapon = WeaponType.getWeaponType(selectedChar);

                Entity monster = new Monster(healthInt, symbolChar, selectedWeapon);
                // changing Entity to add value to Monster
                entityToAdd = monster;

                // clear text fields related to hero
                clearHeroInputs();
            }
        });
    }

    /**
     * this method gets information on entity and shows its details also prepares the delete button
     * @param myButton
     * @param myEntity entity for which the details will be written
     * @param rowInGrid row of the entity to be deleted
     * @param columnInGrid column of the entity to be deleted
     */
    private void prepareDeleteAndDetails(Button myButton, Entity myEntity, int rowInGrid, int columnInGrid){
        // enable deleteEntity button
        deleteEntityButton.setDisable(false);

        // common information of the Hero and Monster added to sharedDetails String
        String sharedDetails = "ID: " + myEntity.shortString() + "\n" + "SYMBOL: "+ myEntity.getSymbol() + "\n" + "HEALTH: " + myEntity.getHealth() + "\n"+ "ALIVE: " + myEntity.isAlive();
        String entityDetails = "";

        // checks if entity is an instance of Hero
        if(myEntity instanceof Hero) {
            Hero myHero = (Hero) myEntity;
            // assigns myHero variable and adds information to entityDetails string
            entityDetails = "\n" + "WEAPON STRENGTH: " + myHero.weaponStrength() + "\n" + "ARMOUR STRENGTH: " + myHero.armorStrength();
        }

        // checks if entity is an instance of Monster
        if(myEntity instanceof Monster) {
            Monster myMonster = (Monster) myEntity;
            // assigns myMonster variable and adds information to entityDetails string
            entityDetails = "\n" + "WEAPON TYPE: " + myMonster.getWeaponType();
        }
        // combines shared details and entity details in details Text area
        entityDetailsTextArea.setText(sharedDetails + entityDetails);

        // assign rowOfItemToDelete and columnOfItemToDelete
        rowOfItemToDelete = rowInGrid;
        columnOfItemToDelete = columnInGrid;
        // reset info status
        setInfoStatus("");

    }

    /**
     * disable deleteEntity button and clear details text area
     * references:
     * disable button: https://stackoverflow.com/questions/36337732/scene-builder-javafx-button-disable-condition
     */
    private void disableDeleteAndClearDetails(){

        // disable deleteEntity button and clear details text area
        deleteEntityButton.setDisable(true);
        entityDetailsTextArea.setText("");

        // clear row and column of itemToDelete
        rowOfItemToDelete = 0;
        columnOfItemToDelete = 0;
    }

    /**
     * clear the monster inputs needed to create Monster
     */
    private void clearMonsterInputs(){
        // unselect monsterButton and set other inputs to default
        monsterButton.setSelected(false);
        monsterSymbolTextField.setText("");
        monsterHealthTextField.setText("");
        weaponDropDownMenu.setValue("");
    }

    /**
     * clear the hero inputs needed to create Hero
     */
    private void clearHeroInputs(){
        // unselect heroButton and set other inputs to default
        heroButton.setSelected(false);
        symbolTextField.setText("");
        healthTextField.setText("");
        weaponTextField.setText("");
        armourTextField.setText("");
    }

    /**
     * sets color of status green and prints message
     * @param message message to be printed as success message
     *
     * reference: changing color of text https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
     */
    private void setSuccessStatus(String message){
        // changes to green for success message
        leftStatus.setTextFill(Paint.valueOf("green"));
        leftStatus.setText(message);
    }

    /**
     * sets color of status red and prints message
     * @param message message to be printed as error message
     */
    private void setErrorStatus(String message){
        leftStatus.setTextFill(Paint.valueOf("red"));
        leftStatus.setText(message);
    }

    /**
     * sets the rightStatus text as message with info
     * @param message message to be printed as info
     */
    private void setInfoStatus(String message){
        rightStatus.setText(message);
    }

    /**
     * clear previous world and draw new one
     */
    private void redrawWorld(){
        // gets rows and columns from world
       int rowsInt = world.getRows();
       int columnsInt = world.getColumns();

       // clears worldGrid and details as well as disables deleteEntityButton
        disableDeleteAndClearDetails();
        worldGrid.getChildren().clear();

        // loop through the world by rows and columns
        for(int row = 0; row < rowsInt; row++) {
            for(int column = 0; column < columnsInt; column++) {
                // assigns button as null
                Button button = null;

                // if there is no entity at position use floor symbol for button text
                if (world.getEntity(row, column) == null) {
                    button = new Button(String.valueOf(Symbol.FLOOR.getSymbol()));
                }
                // if the entity at position is a hero or monster use their symbol for button text
                if (world.isHero(row, column) || world.isMonster(row, column)){
                    button = new Button(String.valueOf(world.getEntity(row, column).getSymbol()));
                }

                // assign row/column/button in grid
                final int rowInGrid = row;
                final int columnInGrid = column;
                final Button buttonInGrid = button;
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // assign the entity in button by getting the entity at row and column in grid
                        Entity entityInButton = world.getEntity(rowInGrid, columnInGrid);

                        // if the selected button has a monster or hero in it prepare the delete button and details on the entity
                        if (world.isHero(rowInGrid, columnInGrid) || world.isMonster(rowInGrid, columnInGrid)){
                            prepareDeleteAndDetails(buttonInGrid, entityInButton, rowInGrid, columnInGrid);

                            return;
                        }

                        // check if the button is null and the entity to add is not null
                        if ((entityInButton == null) && (entityToAdd != null)){
                            // add the entityToAdd to the world
                            world.addEntity(rowInGrid, columnInGrid, entityToAdd);

                            // get entity class and display that class has been added
                            String entityClass = entityToAdd.getClass().getSimpleName();
                            setSuccessStatus("Added New " + entityClass);

                            // get the entity symbol and assign it to the button text
                            String entitySymbol = Character.toString(entityToAdd.getSymbol());
                            buttonInGrid.setText(entitySymbol);

                            // prepare delete button and show details
                            prepareDeleteAndDetails(buttonInGrid, entityToAdd, rowInGrid, columnInGrid);

                            // clear both hero and monster inputs
                            clearHeroInputs();
                            clearMonsterInputs();

                            //reset entity to add
                            entityToAdd = null;
                            return;
                        }

                        // if there is no entity in button but there is also no entity to add disable deleteButton and
                        // clear the details
                        if ((entityInButton == null) && (entityToAdd == null)){
                            disableDeleteAndClearDetails();
                            return;
                        }
                    }
                });
                // setting the button sizing and positioning
                // reference: button sizing https://stackoverflow.com/questions/25754524/javafx-buttons-with-same-size
                button.setPadding(new Insets(3));
                button.setMinHeight(30.0);
                button.setMinWidth(30.0);
                // adding the button to worldGrid
                worldGrid.add(button, row, column);
            }
        }

        // positioning the buttons
        worldGrid.setHgap(3);
        worldGrid.setVgap(3);
        // reference: creating walls https://www.demo2s.com/java/javafx-gridpane-setborder-border-value.html
        worldGrid.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
        setSuccessStatus("New World Created");
    }

    /**
     * getting the file that will be used for reading info from
     * @return file that was selected by user
     */
    private File getFileForReading(){
        // creating new file chooser
        FileChooser myFileChooser = new FileChooser();
        // assigning selected file and returning it
        File selectedFile = myFileChooser.showOpenDialog(controllerStage);
        return selectedFile;
    }

    /**
     * getting the file that will be used for writing info into
     * @return file that was selected by user
     */
    private File getFileForWriting(){
        // creating new file chooser
        FileChooser myFileChooser = new FileChooser();
        // assigning selected file and returning it
        File selectedFile = myFileChooser.showSaveDialog(controllerStage);
        return selectedFile;
    }

    /**
     * checking if the Symbol input is valid
     * @param askingSymbol value for simple provided by user
     * @param entityType either Monster or Hero
     * @return
     */
    private boolean symbolIsValid(String askingSymbol, String entityType){

        // if the input is empty the method will provide an error message and return false
        if (askingSymbol.equals("")){
            setErrorStatus("Provide a " + entityType + " Symbol");
            return false;
        }

        // if the symbol input is more than one character the method will provide an error message and return false
        if (askingSymbol.length() > 1){
            setErrorStatus("Provide a " + entityType + " symbol with one character");
            return false;
        }

        // if the symbol input is equal to any of the symbols that are already needed for the game
        // the method will provide an error message and return false
        if ((askingSymbol.equals(String.valueOf(Symbol.FLOOR.getSymbol()))) ||
                (askingSymbol.equals(String.valueOf(Symbol.WALL.getSymbol()))) ||
                (askingSymbol.equals(String.valueOf(Symbol.DEAD.getSymbol())))){
            setErrorStatus("Do not use special characters for " + entityType + " symbol");
            return false;
        }

        // other than specified cases returns true
        return true;
    }

    /**
     * check if the String value is an integer
     * @param value String value to be checked
     * @return true if the value is an integer and false if it is not and integer
     */
    private boolean checkIfInt(String value){
        try {
            // try turning string into integer return true if possible
            Integer.parseInt(value);
            return true;
            // return false if not possible to turn value into integer
        } catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * check if provided String is a valid Integer value
     * @param askingInteger integer provided by user to be checked
     * @param entityType type of entity the integer was provided for
     * @param valueName what the integer was provided for
     * @return true if valid and false if not valid input
     */
    private boolean integerIsValid(String askingInteger, String entityType, String valueName){

        // if the provided value is empty the method returns false and provides an error status
        if (askingInteger.equals("")){
            setErrorStatus("Provide a " + entityType + " " + valueName + " value");
            return false;
        }

        // if the provided value is not an integer the method returns false and provides an error status
        if (checkIfInt(askingInteger) == false){
            setErrorStatus("Provide an integer for " + entityType + " " +  valueName + " value");
            return false;
        }

        // if the provided value is longer than on character the method returns false and provides an error status
        if (Integer.parseInt(askingInteger) < 0){
            setErrorStatus("Provide a positive value for " + entityType + " " +valueName + " value");
            return false;
        }

        // otherwise returns true
       return true;
    }

}
