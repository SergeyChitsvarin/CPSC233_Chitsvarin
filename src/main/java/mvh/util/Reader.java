package mvh.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvh.enums.WeaponType;
import mvh.world.Hero;
import mvh.world.Monster;
import mvh.world.World;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class to assist reading in world file
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Reader {

    /**
     * Load the world from the given file
     * (Do not expect students to create anything near as robust as this file reading method!)
     * (A better design would also use sub-functions.)
     *
     * @param fileWorld The world file to load
     * @return A World created from the world file
     */
    public static World loadWorld(File fileWorld) {
        try {
            // create a file reader
            Scanner fileReader = new Scanner(fileWorld);

            // read through and assign number of rows found in first line
            String firstLine = fileReader.nextLine();
            int rowCount = Integer.parseInt(firstLine);

            // read through and assign number of columns found in second line
            String secondLine = fileReader.nextLine();
            int columnCount = Integer.parseInt(secondLine);

            // create new world object
            World world = new World(rowCount, columnCount);

            // calculates number of lines
            int lineNum = rowCount * columnCount;

            // loops through while line number is greater than zero
            while (lineNum > 0){

                // reads onto the next line from the file
                String line = fileReader.nextLine();

                // splits the line using "," delimiter
                String[] lineObjects = line.split(",");

                // find length of line to determine the number of elements in the line
                int numOfElementsInLine = lineObjects.length;

                // checks if Hero or Monster is on tile
                if (numOfElementsInLine > 2) {

                    // assigns variables which are similar between Monster and Hero
                    int row = Integer.parseInt(lineObjects[0]);
                    int column = Integer.parseInt(lineObjects[1]);
                    String type = lineObjects[2];
                    char symbol = lineObjects[3].charAt(0);
                    int health = Integer.parseInt(lineObjects[4]);

                    // determine if Monster is on the tile
                    if (type.equals("MONSTER")) {
                        // assigns weaponType for the Monster
                        char weaponTypeChar = lineObjects[5].charAt(0);
                        WeaponType weaponType = WeaponType.getWeaponType(weaponTypeChar);
                        // creates new object Monster
                        Monster monster = new Monster(health, symbol, weaponType);
                        // adds entity to the world object at specific row and column
                        world.addEntity(row, column, monster);

                    }

                    // determines if Hero is on the tile
                    if (type.equals("HERO")) {
                        // assigns weapon and armor strength for the Hero
                        int weaponStrength = Integer.parseInt(lineObjects[5]);
                        int armorStrength = Integer.parseInt(lineObjects[6]);
                        // creates new object Hero
                        Hero hero = new Hero(health, symbol, weaponStrength, armorStrength);
                        // adds entity to the world object at specific row and column
                        world.addEntity(row, column, hero);
                    }
                }
                // subtract one from the lineNum
                lineNum--;

            }

            // returns world object with Hero and Monster entities (if applicable)
            return world;

            // bring up error if file was not found
        } catch (FileNotFoundException error) {
            System.out.println("File was not found.");
            error.printStackTrace();
            System.exit(1);
            // catch general error
        } catch (Exception error){
            System.out.println("Unexpected error happened while loading world.");
            error.printStackTrace();
            System.exit(1);
        }
        // default value for world
        return null;
    }
}


