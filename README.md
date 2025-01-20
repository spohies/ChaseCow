# CHASE COW: 
Claire Hung & Sophie Jiang | Jan 2025 - ICS4U1

## BEFORE PLAYING:

### How to play? - Use a keyboard and mouse!
    • use WASD to move around
    • LEFT or RIGHT click to attack!
    • press E to toggle inventory
        • LEFT click to select an item to equip 
    • press C to walk through doors
    • press SPACE to interact with NPCs
    • press F for special actions

### About:
The goal of the game is to escape Markville! There are three levels (the floors of the school, from top to bottom) where you will encounter many enemies (🐮🐄) that you have to beat. You will be able to pick up weapons and items to help you defeat the enemies and reach the end. In each room, you will be able to speak to NPCs (teachers) for hints as to where to find keys to help you unlock rooms. Find your way out of the school!

### Cheats for Ms. Wong: 
**(!) PLEASE READ AND FOLLOW GAMEPROGRESSION.txt (!)**
	- Walking by any water fountain will heal you.
	- Since the game restarts once you die, this will help you reach the end!

### Changes from original plan:
	- Time permitting features that were not added because time was NOT permitting :(
		○ Mini Map
		○ Difficulty Option
	- Regular features that were omitted:
		○ Breakable objects
		○ Character Selection
	- Additional Changes:
		○ No Level Selection --> MUST PLAY FROM START TO FINISH
		○ (NEW) Final Boss Fight
			- Removed PaceCow object
			- WasteCow is now a boss fight
		○ (NEW) NPC Tasks
			- replacing breakable objects
		○ (NEW) Interactive Inventory
			- replacing character selection

### Known bugs / errors in your game
	- (Known strange feature) Cows do not detect collisions with walls
	- RARE OCCURENCE -> When cow dies, game freezes.
		- REASON: iterator.remove() and hashSet.remove() implicitly call hashCode and equals function(?) (this is from our research not 100% sure)
		- hashCode returns a DIFFERENT int sometimes for the same cow because the cow's variables change a lot. 
			-> we could not write an efficient hashCode method because it would be the same for all cows, or it would be based on HP and coords which would not return the same hashCode each time... UNLESS we added an ID to all cows which would take forever
			--> it is 1:30 and we want to sleep
				(this error only occurs once every like 100 cows so we just decided to omit)

### Who did what?

Claire:
Graphics, Custom Collision Detection, Handling Triangular Collisions, Interior Wall Layering, Most Text File Streaming, Stage 4, Boss Battle

Sophie:
Menus/Leaderboard, Sound, Player Movement, Coordinate System, Enemy Path Finding, Some Text File Streaming, Stages 1, 2, 3, 5

Together:
Level and Game Design, Testing and Debugging

### Any other important info for me to play/mark your game
moo