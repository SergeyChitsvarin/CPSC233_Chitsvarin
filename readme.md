#File Menu
##Load
When the user clicks "Load", a window pops up. In this window
the user has a choice between choosing a file to open which would
create a world from this file. Or pressing cancel in which case
the window closes and nothing happens.
##Save As
When the user clicks "Save As" a window will pop up. in this window
the user will choose a file and that file will have the world added
in specific notation. If the user wants to cancel out of this window
it will close and nothing will happen.
##Quit
When the user presses "Quit" the program will close.

_________________________________________
# Help Menu

##About
when the user presses the "About Monsters VS Heroes V1.0" button an Alert would pop up.
The alert contains information about the author/Email/version and general information about the
program.

____________________________________________________________
#World Setup

##Rows
in "Rows" text field the user is able to provide an integer greater than 0 that will be responsible for
the number of rows the world has.
##Columns
in "Columns" text field the user is able to provide an integer greater than 0 that will be responsible for
the number of columns the world has.
##Create New World Button
this Button is responsible for taking in the information from "Rows" and "Columns" and using 
them by creating a grid with the number of rows and columns.
_______________________________________________________
#Adding Hero
##Hero Button
the radio button will be available to use when all the hero related text fields
are filled in with valid inputs. When the user clicks on the "Hero" Button the text fields
associated with "monster" will be cleared and the User can click on the grid to find a place 
to put  this Hero.
##Hero Symbol text field
the User can input any one character into the symbol text field except for ".", "#" and "$"
as those characters are already used in the game. the value put into this text field
will later be displayed at the heroes position.
## Hero Health text field
the user is able to put in any integer that is greater or equal to 0. This value will be used
as the heroes health and will be displayed in the details tab when the hero is placed and/or clicked on.
## Hero Strength text field
the user is able to put in any integer that is greater or equal to 0. This value will be used
as the heroes attack strength and will be displayed in the details tab when the hero is placed and/or clicked on.
## Hero Armour text field
the user is able to put in any integer that is greater or equal to 0. This value will be used
as the heroes armour strength and will be displayed in the details tab when the hero is placed and/or clicked on.

_______________________________________________________________________________
#Adding Monster
##Monster Button
the radio button will be available to use when all the monster related text fields
are filled in with valid inputs. When the user clicks on the "Monster" Button the text fields
associated with "Hero" will be cleared and the User can click on the grid to find a place
to put this Monster.
##Monster Health text field
the User can input any one character into the symbol text field except for ".", "#" and "$"
as those characters are already used in the game. the value put into this text field
will later be displayed at the Monsters position.
##Monster Health text field
the user is able to put in any integer that is greater or equal to 0. This value will be used
as the Monsters health and will be displayed in the details tab when the monster is placed and/or clicked on.
##Monster Weapon Type dropdown
The user can click on the dropdown and is presented with 3 options for weapon(Axe, Sword, Club).
the choice will be the monster's weapon type and will be displayed in the details
when that monster is placed or clicked on.

_____________________________________________________________________________
#View

## World grid
the view tab shows the current world and will update with each change to the world.
Or if the user decided to create a new or load new world.
___________________________________________________________________
#Details

##Details text area
the details text area shows the entity details after placing or clicking on entity.
the details include: "ID", "Symbol", "Health", and health status for both heroes and monsters.
however, if the entity clicked is a hero the program also shows "Weapon Strength" and "Armour Strength".
And if the entity is a monster it will show the shared details and "Weapon Type."
##Delete Entity button
The "delete entity" button will be disabled until the user has an entity selected.
In this case if the button is clicked the current entity selected will be deleted and replaced by
a floor symbol in its previous place.


