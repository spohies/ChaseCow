import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
// import javax.sound.sampled.LineUnavailableException;
// import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.image.*;

/* 
 * screen 0 = main menu
 * screen 1 = start menu
 * screen 2 = about menu
 * screen 3 = settings menu
 * screen 4 = leaderboard
 * screen 5 = game ??????
 */

@SuppressWarnings("serial") // funky warning, just suppress it. It's not gonna do anything.
public class ChaseCow extends JPanel implements Runnable, KeyListener, MouseListener {
	// big boss variable
	int screen = 5;
	static JPanel myPanel;
	static JFrame frame;

	// self explanatory variables
	int FPS = 60;
	Thread thread;
	int screenWidth = 1080;
	int screenHeight = 720;
	boolean up, down, left, right;
	boolean interactable = false;

	// main screen
	Rectangle recAbout, recOptions, recPlay, recExit;
	boolean hoverAbout, hoverOptions, hoverPlay, hoverExit;
	BufferedImage titleScreenBG, title, play, play2, about, about2, settings, settings2, options, options2, exit, exit2;

	// start screen
	Rectangle recLB, recNewGame, recBack;
	boolean hoverNewGame, hoverLB, hoverBack;
	BufferedImage menuScreenBackground, gameTitle, newGame, leaderboard, newGame2, leaderboard2, back;

	// settings
	ArrayList<Rectangle> recVolume;
	// im thinking we have like 5 buttons and its just 5 levels of volume next to
	// each other so its like a fake slider
	ArrayList<Rectangle> recDifficulty;
	// maybe we just have hp regen vs no regen for difficulty
	BufferedImage volumeImg, difficultyImg;
	// int difficulty = 1; // maybe just 1=easy 2=hard?
	int volume = 3; // 1-5??

	boolean showInventory = false;
	// game
	ArrayList<BufferedImage> cowImages = new ArrayList<BufferedImage>();

	// Rectangle player = new Rectangle(517, 328, 46, 64);
	// HashSet <Rectangle> walls = new HashSet <Rectangle>();
	BufferedImage tempBG, playerImage, cowImage;
	Player suki;
	// HashSet<Cow> cows = new HashSet<Cow>();
	FloorMap currentMap;
	ArrayList<FloorMap> maps = new ArrayList<>();
	String[] cowNames = {
			"baseCow",
			"paceCow",
			"spaceCow",
			"wasteCow"
	};
	int currentCowType = 0;

	public ChaseCow() throws IOException {
		initialize();

		// sets up JPanel
		setPreferredSize(new Dimension(1080, 720));
		setVisible(true);
		setFocusable(true); // Ensure the panel is focusable
		requestFocusInWindow(); // Request focus for key events
		addMouseListener(this);
		addKeyListener(this); // Add KeyListener to the panel

		// addKeyListener(this);
		// starting the thread
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			// main game loop
			update();
			this.repaint();
			try {
				Thread.sleep(1000 / FPS);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// IF SCREEN IS ON A GAME SCREEN ie != screen 0 1 2 3 4 or smth
			// then do stuff that needs to happen at all times during game
		}
	}

	public void initialize() {

		try {
			titleScreenBG = ImageIO.read(getClass().getResource("/menu/titleBG.png"));
			System.out.println("Loaded titleScreenBG image");
			int numCows = cowNames.length;
			for (int i = 0; i < numCows; i++) {
				String cowPath = "/sprites/" + cowNames[i] + ".png";
				cowImage = ImageIO.read(getClass().getResource(cowPath));
				cowImages.add(cowImage);
				System.out.println("Loaded cow image: " + cowPath);
			}

			// screen 0 (main menu)
			title = ImageIO.read(getClass().getResource("/menu/title.png"));
			System.out.println("Loaded title image");
			play = ImageIO.read(getClass().getResource("/menu/playbutton.png"));
			System.out.println("Loaded play button image");
			about = ImageIO.read(getClass().getResource("/menu/aboutbutton.png"));
			System.out.println("Loaded about button image");
			play2 = ImageIO.read(getClass().getResource("/menu/playbutton2.png"));
			System.out.println("Loaded play button 2 image");
			about2 = ImageIO.read(getClass().getResource("/menu/aboutbutton2.png"));
			System.out.println("Loaded about button 2 image");
			// settings = ImageIO.read(getClass().getResource("/menu/settingsbutton.png"));
			// System.out.println("Loaded settings button image");
			// settings2 =
			// ImageIO.read(getClass().getResource("/menu/settingsbutton2.png"));
			// System.out.println("Loaded settings button 2 image");
			// options = ImageIO.read(getClass().getResource("/menu/optionsbutton.png"));
			// System.out.println("Loaded options button image");
			// options2 = ImageIO.read(getClass().getResource("/menu/optionsbutton2.png"));
			// System.out.println("Loaded options button 2 image");
			// exit = ImageIO.read(getClass().getResource("/menu/exitbutton.png"));
			// System.out.println("Loaded exit button image");
			// exit2 = ImageIO.read(getClass().getResource("/menu/exitbutton2.png"));
			// System.out.println("Loaded exit button 2 image");
			// newGame = ImageIO.read(getClass().getResource("/menu/newgamebutton.png"));
			// System.out.println("Loaded new game button image");
			// newGame2 = ImageIO.read(getClass().getResource("/menu/newgamebutton2.png"));
			// System.out.println("Loaded new game button 2 image");
			// leaderboard =
			// ImageIO.read(getClass().getResource("/menu/leaderboardbutton.png"));
			// System.out.println("Loaded leaderboard button image");
			// leaderboard2 =
			// ImageIO.read(getClass().getResource("/menu/leaderboardbutton2.png"));
			// System.out.println("Loaded leaderboard button 2 image");
			// back = ImageIO.read(getClass().getResource("/menu/backbutton.png"));
			// System.out.println("Loaded back button image");
			// volumeImg = ImageIO.read(getClass().getResource("/menu/volume.png"));
			// System.out.println("Loaded volume image");
			// DIMENSION NEED TO BE CHANGED!!!!!!
			recPlay = new Rectangle(100, 200, play.getWidth(), play.getHeight());
			recAbout = new Rectangle(100, 300, about.getWidth(), about.getHeight());

			// screen 1 (start menu)

			// screen 2 (wtvwtv )... so on

			// screen 5
			// starting room
			HashSet<Wall> electricalWalls = new HashSet<>();
			BufferedImage wallImage = ImageIO.read(getClass().getResource("/map files/tempWall.png"));
			System.out.println("Loaded wall image");
			electricalWalls
					.add(new Wall(wallImage, new Rectangle(7300, 200, wallImage.getWidth(), wallImage.getHeight())));
			electricalWalls
					.add(new Wall(wallImage, new Rectangle(7400, 40, wallImage.getWidth(), wallImage.getHeight())));
			electricalWalls
					.add(new Wall(wallImage, new Rectangle(7630, 100, wallImage.getWidth(), wallImage.getHeight())));
			electricalWalls
					.add(new Wall(wallImage, new Rectangle(7480, 150, wallImage.getWidth(), wallImage.getHeight())));
			electricalWalls
					.add(new Wall(wallImage, new Rectangle(7500, 350, wallImage.getWidth(), wallImage.getHeight())));
			tempBG = ImageIO.read(getClass().getResource("/map files/FLOOR3.png"));
			System.out.println("Loaded tempBG image");

			HashSet<Triangle> tempWalls = new HashSet<>();
			HashSet<Cow> electricalCows = new HashSet<>();

			// TEMPORARY (THERE WILL BE AN NPC ARRAYLIST)
			BufferedImage npcImage = ImageIO.read(getClass().getResource("/sprites/npc1.png"));
			BufferedImage stickImage = ImageIO.read(getClass().getResource("/map files/tempWeapon.png"));
			NPC npc = new NPC(new Point(8100, 200),
					new String[] { "ive been here for four hours help", "PLEASE WORK I AM BEGGINGG" }, npcImage);
			ArrayList<Weapon> weapons = new ArrayList<>();
			ArrayList<Collectible> items = new ArrayList<>();
			Weapon tempWeapon = new Weapon("stick", "use to attack", 50, 3, stickImage, new Point(7400, 370));
			weapons.add(tempWeapon);
			// TO BE CHANGE TO ACTUAL LOCATIONS
			tempWalls.add(new Triangle(new Point(7100, 100), new Point(7200, 200), new Point(7300, 100)));
			tempWalls.add(new Triangle(new Point(7300, 600), new Point(7360, 800), new Point(7510, 700)));
			currentMap = new FloorMap(new Point(0, 0), electricalWalls, tempWalls, tempBG, new Rectangle[0],
					electricalCows, npc, weapons, items);

			// playerImage = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));

			BufferedImage playerImageDown = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			BufferedImage playerImageUp = ImageIO.read(getClass().getResource("/sprites/sukiUp.png"));
			BufferedImage playerImageRight = ImageIO.read(getClass().getResource("/sprites/sukiRight.png"));
			BufferedImage playerImageLeft = ImageIO.read(getClass().getResource("/sprites/sukiLeft.png"));

			suki = new Player(100, 4, new Rectangle(517, 382, 46, 10), new Rectangle(517, 328, 46, 64), 8000, 300, playerImageDown, playerImageUp, playerImageRight, playerImageLeft);
			currentMap.getCows().add(new BaseCow(8300, 200, cowImages.get(currentCowType), currentMap, suki));
			currentMap.getCows().add(new BaseCow(7800, 300, cowImages.get(currentCowType), currentMap, suki));
			currentMap.getCows().add(new BaseCow(7700, 400, cowImages.get(currentCowType), currentMap, suki));
			currentMap.getCows().add(new BaseCow(8000, 350, cowImages.get(currentCowType), currentMap, suki));
			currentMap.getCows().add(new BaseCow(7600, 200, cowImages.get(currentCowType), currentMap, suki));
			// other images
			// tempBG = ImageIO.read(getClass().getResource("/menu/tempBG.png"));
			// System.out.println("Loaded tempBG image");
			playerImage = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			System.out.println("Loaded player image");
			cowImage = ImageIO.read(getClass().getResource("/sprites/baseCow.png"));
			System.out.println("Loaded cow image");

		} catch (IOException e) {
			e.printStackTrace();
		}

		initializeMaps();
		// spawnCows();
	}

	public static void resetVariables() {
		// RESETS ALL CHANGING VARIABLES WHEN NEW GAME IS STARTED
	}

	// public void spawnCows() {

	// }

	public void update() {
		// update stuff

		if (screen == 5) {
			// System.out.println("suki: " + suki.getGamePos());
			move();
			keepInBound();
			// System.out.println(suki.getGamePos());
			if (currentMap != null) {
				currentMap.updateCows(suki);
			} else {
				System.out.println("currentMap is null");
			}

			Iterator<Wall> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {
				checkCollision(it.next());
			}
			Iterator<Triangle> itr = currentMap.getTriWalls().iterator();
			while (itr.hasNext()) {
				checkCollision(itr.next());
			}

			Iterator<Cow> cowItr = currentMap.getCows().iterator();
			while (cowItr.hasNext()) {
				Cow cow = cowItr.next();
				checkCollision(cow);
			}

			checkPlayerCollisions(suki);

			NPC npc = currentMap.npc; // Get the map's NPC
			if (npc != null) {
				double distance = getNPCDistance(suki, currentMap.npc);
				if (distance < 75) {
					interactable = true;
				} else {
					interactable = false;
				}
			}
		}
	}

	private void initializeMaps() {
		// Create and add FloorMap objects to the maps list
	}

	public double getNPCDistance(Player suki, NPC npc) {
		// Get NPC center coordinates
		int npcCenterX = npc.getGamePos().x + (npc.image.getWidth() / 2);
		int npcCenterY = npc.getGamePos().y + (npc.image.getHeight() / 2);

		// Get Player center coordinates
		int playerCenterX = suki.getGamePos().x + (suki.getHitboxM().width / 2);
		int playerCenterY = suki.getGamePos().y + (suki.getHitboxM().height / 2);

		return Math.sqrt(Math.pow(playerCenterX - npcCenterX, 2) + Math.pow(playerCenterY - npcCenterY, 2));
	}

	public void changeMap(FloorMap currentMap, int n) {
		FloorMap m = maps.get(n);
		// change map according to what map is next... im not sure how to do this tbh we
		// may need to number everysingle map
		// and add an if statement for every single one
		// spawnCows();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;
		// white background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// main screen
		if (screen == 0) {
			// g.drawImage(titleScreenBG, 0, 0, titleScreenBG.getWidth(),
			// titleScreenBG.getHeight(), this);
			g.drawImage(title, 100, 100, title.getWidth(), title.getHeight(), this);
			g.drawImage(play, 100, 200, play.getWidth(), play.getHeight(), this);
			g.drawImage(about, 100, 300, about.getWidth(), about.getHeight(), this);
		}

		// start game screen
		if (screen == 1) {
			// g.drawImage(newGame, 100, 200, newGame.getWidth(), newGame.getHeight(),
			// this);
			// g.drawImage(leaderboard, 100, 300, leaderboard.getWidth(),
			// leaderboard.getHeight(), this);
			// g.drawImage(back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// draw stuff
		if (screen == 5) { // Game screen
			int playerX = suki.getGamePos().x + (int) suki.getHitboxC().getWidth() / 2;
			int playerY = suki.getGamePos().y - (int) suki.getHitboxC().getHeight() / 2;
			if (tempBG != null) {

				// Visible area dimensions
				int visibleWidth = screenWidth;
				int visibleHeight = screenHeight;

				// Calculate the source rectangle based on player position
				int srcX = playerX - visibleWidth / 2;
				int srcY = playerY - visibleHeight / 2;
				int srcX2 = srcX + visibleWidth;
				int srcY2 = srcY + visibleHeight;

				// Adjust the destination rectangle dimensions
				int destWidth = srcX2 - srcX;
				int destHeight = srcY2 - srcY;

				// Draw the background (scaled to the screen dimensions if necessary)
				g.drawImage(tempBG,
						0, 0, destWidth, destHeight, // Destination rectangle
						srcX, srcY, srcX2, srcY2, // Source rectangle
						this);

			} else {
				System.out.println("Background image is null");
			}

			NPC npc = currentMap.npc;
			if (npc != null) {
				// Translate NPC's in-game position to screen coordinates
				int npcScreenX = (npc.getGamePos().x - playerX) + ((screenWidth / 2));
				int npcScreenY = (npc.getGamePos().y - playerY) + ((screenHeight / 2));

				// Draw NPC
				if (npc.image != null) {
					g.drawImage(npc.image, npcScreenX, npcScreenY, npc.image.getWidth(), npc.image.getHeight(), this);
				} else {
					g.setColor(Color.BLUE);
					g.fillRect(npcScreenX, npcScreenY, 50, 50); // Placeholder
				}
			}

			g2.setColor(Color.GREEN);
			Iterator<Wall> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {

				Wall wall = it.next();
				int wallScreenX = (wall.getRect().x - playerX) + (screenWidth / 2);
				int wallScreenY = (wall.getRect().y - playerY) + (screenHeight / 2);
				// int wallScreenX = (wall.getRect().x - suki.getGamePos().x - (int)
				// suki.getHitboxC().getWidth()/2) + (screenWidth / 2);
				// int wallScreenY = (wall.getRect().y - suki.getGamePos().y + (int)
				// suki.getHitboxC().getHeight()/2) + (screenHeight / 2);
				int wallWidth = wall.getRect().width;
				int wallHeight = wall.getRect().height;

				// Ensure alignment between rectangle and image
				g.drawImage(wall.getImage(), wallScreenX, wallScreenY, wallWidth, wallHeight, this);
			}
			for (Triangle tri : currentMap.getTriWalls()) {
				Point[] vertices = tri.getVertices();
				int[] xPoints = { (vertices[0].x - playerX) + (screenWidth / 2),
						(vertices[1].x - playerX) + (screenWidth / 2), (vertices[2].x - playerX) + (screenWidth / 2) };
				int[] yPoints = { (vertices[0].y - playerY) + (screenHeight / 2),
						(vertices[1].y - playerY) + (screenHeight / 2),
						(vertices[2].y - playerY) + (screenHeight / 2) };
				g2.fillPolygon(xPoints, yPoints, 3);
			}

			if (suki.getCurrentSprite() != null) {
				g.drawImage(suki.getCurrentSprite(),
						(screenWidth - suki.getHitboxC().width) / 2,
						(screenHeight - suki.getHitboxC().height) / 2,
						suki.getHitboxC().width,
						suki.getHitboxC().height,
						this);

				// g2.fill(suki.getHitboxM());

			} else {
				System.out.println("Player image is null");
			}

			for (Cow cow : currentMap.getCows()) {
				// Translate the cow's in-game position to screen coordinates
				int cowScreenX = (cow.getGamePos().x - playerX) + ((screenWidth / 2));
				int cowScreenY = (cow.getGamePos().y - playerY) + ((screenHeight / 2));

				// Draw the cow image or placeholder
				BufferedImage cowImage = cow.getImage();
				if (cowImage != null) {
					g.drawImage(cowImage,
							cowScreenX, cowScreenY,
							cow.getHitbox().width, cow.getHitbox().height,
							this);
				} else {
					// Placeholder if the image is missing
					g.setColor(Color.RED);
					g.fillRect(cowScreenX, cowScreenY, cow.getHitbox().width, cow.getHitbox().height);
				}
			}

			if (npc != null) {
				double distance = getNPCDistance(suki, currentMap.npc);

				if (distance < 75) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 600, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Press SPACE to interact", 60, 620);
				}
			}

			if (showInventory) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 50, 400, 400);
				g.setColor(Color.BLACK);
				g.drawString("Inventory", 60, 70);
				g.drawString("Press E to close", 60, 90);

				int y = 40;
				for (Item item : suki.getInventory()) {
					// Draw item name
					g.drawString(item.getName(), 10, y);

					// Optionally draw item icon
					BufferedImage image = item.getImage();
					if (image != null) {
						g.drawImage(image, 60, y+70, image.getWidth(), image.getHeight(), null);
					}

					y += 40; // Spacing between items
				}
			}

			if (currentMap.getWeapons() != null) {
				for (Weapon w : currentMap.getWeapons()) {
					int itemScreenX = (w.getGamePos().x - suki.getGamePos().x) + (screenWidth / 2);
					int itemScreenY = (w.getGamePos().y - suki.getGamePos().y) + (screenHeight / 2);

					g.drawImage(w.getImage(), itemScreenX, itemScreenY, w.getImage().getWidth(),
							w.getImage().getHeight(), this);
				}
			}

			if (currentMap.getItems() != null) {
				for (Collectible c : currentMap.getItems()) {
					int itemScreenX = (c.getGamePos().x - suki.getGamePos().x) + (screenWidth / 2);
					int itemScreenY = (c.getGamePos().y - suki.getGamePos().y) + (screenHeight / 2);

					g.drawImage(c.getImage(), itemScreenX, itemScreenY, c.getImage().getWidth(),
							c.getImage().getHeight(), this);
				}
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		// game screen
		if (screen == 5) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				left = true;
				right = false;
				suki.setDirection("left");
			} else if (key == KeyEvent.VK_D) {
				right = true;
				left = false;
				suki.setDirection("right");
			} else if (key == KeyEvent.VK_W) {
				up = true;
				down = false;
				suki.setDirection("up");
			} else if (key == KeyEvent.VK_S) {
				down = true;
				up = false;
				suki.setDirection("down");
			} else if (key == KeyEvent.VK_E) {
				if (showInventory == true) {
					showInventory = false;
				} else {
					showInventory = true;
				}
			}

			if (interactable == true && key == KeyEvent.VK_SPACE) {
				currentMap.npc.interact();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// game screen
		if (screen == 5) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				left = false;
			} else if (key == KeyEvent.VK_D) {
				right = false;
			} else if (key == KeyEvent.VK_W) {
				up = false;
			} else if (key == KeyEvent.VK_S) {
				down = false;
			}

			if (key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
				suki.setDirection(suki.getCurrentDirection()); // Retain direction
			}
		}
	}

	public void move() {
		// game screen
		if (screen == 5) {
			int moveX = 0, moveY = 0;
			if (left) {
				moveX = -suki.getSpeed();
			} else if (right) {
				moveX = suki.getSpeed();
			}
			if (up) {
				moveY = -suki.getSpeed();
			} else if (down) {
				moveY = suki.getSpeed();
			}

			// Create a future hitbox for the player
			Rectangle futureHitbox = new Rectangle(suki.getHitboxM());
			futureHitbox.translate(moveX, moveY);

			// Check for collisions with rectangular walls
			boolean collision = false;
			for (Wall wall : currentMap.getRectWalls()) {
				if (futureHitbox.intersects(wall.getRect())) {
					collision = true;
					break;
				}
			}

			// Only update coordinates if no collision is detected
			if (!collision) {
				// System.out.printf("no collision");
				suki.move(moveX, moveY);
				// System.out.println("moving");
				// center player
				suki.getHitboxC().x = (screenWidth / 2) - (suki.getHitboxC().width / 2);
				suki.getHitboxC().y = (screenHeight / 2) - (suki.getHitboxC().height / 2);

				// Update in-game coordinates
				currentMap.setTLLocation(
						new Point(currentMap.getTLLocation().x - moveX, currentMap.getTLLocation().y - moveY));

				// for (Cow cow : currentMap.getCows()) {
				// cow.setMapPos(cow.getMapPos().x - moveX, cow.getMapPos().y - moveY);
				// }

				// for (Triangle tri : currentMap.getTriWalls()) {
				// for (Point vertex : tri.getVertices()) {
				// vertex.x -= moveX;
				// vertex.y -= moveY;
				// }
				// }
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * screen 0 = main menu
	 * screen 1 = start menu
	 * screen 2 = about menu
	 * screen 3 = settings menu
	 * screen 4 = leaderboard
	 * screen 5 = game
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point selectedPoint = new Point(e.getX(), e.getY());
		if (screen == 0) {
			System.out.println("clicked" + e.getX() + " " + e.getY());
			if (recPlay.contains(selectedPoint)) {
				screen = 1;
				myPanel.repaint();
				// try {
				// // button = true;
				// // soundPlayer();
				// } catch (UnsupportedAudioFileException | IOException |
				// LineUnavailableException e1) {
				// e1.printStackTrace();
				// }
			} else if (recAbout.contains(selectedPoint)) {
				screen = 2;
				myPanel.repaint();
				// try {
				// // button = true;
				// // soundPlayer();
				// } catch (UnsupportedAudioFileException | IOException |
				// LineUnavailableException e1) {
				// e1.printStackTrace();
				// }
			}
		}

		// start menu
		// starts a new game, shows leaderboard, or returns to main menu

		// else if (screen == 1) {
		// if (recNewGame.contains(selectedPoint)) {
		// screen = 5;
		// // try {
		// // // button = true;
		// // // soundPlayer();
		// // // music.stop();
		// // // musicPlayer();
		// // } catch (UnsupportedAudioFileException | IOException |
		// LineUnavailableException e1) {
		// // e1.printStackTrace();
		// // }

		// myPanel.repaint();
		// // timer = new Timer("Timer");
		// // timer.scheduleAtFixedRate(new ThreadTimer(), 10, 1000);
		// resetVariables();
		// } else if (recLB.contains(selectedPoint)) {
		// screen = 4;
		// myPanel.repaint();
		// // try {
		// // // button = true;
		// // // soundPlayer();
		// // } catch (UnsupportedAudioFileException | IOException |
		// LineUnavailableException e1) {
		// // e1.printStackTrace();
		// // }
		// } else if (recBack.contains(selectedPoint)) {
		// screen = 0;
		// myPanel.repaint();
		// // try {
		// // // button = true;
		// // // soundPlayer();
		// // } catch (UnsupportedAudioFileException | IOException |
		// LineUnavailableException e1) {
		// // e1.printStackTrace();
		// // }
		// }
		// }
		// // about menu
		// else if (screen == 2) {
		// if (recBack.contains(selectedPoint)) {
		// screen = 0;
		// }

		// }
		// // settings menu
		// else if (screen == 3) {
		// if (recBack.contains(selectedPoint)) {
		// screen = 5;
		// }

		// }
		// // leaderboard
		// else if (screen == 4) {
		// if (recBack.contains(selectedPoint)) {
		// screen = 1;
		// }
		// }
		// // main game
		// else if (screen == 5) {
		// if (showInventory) {
		// // add inventory buttons
		// }

		// if (recOptions.contains(selectedPoint)) {
		// screen = 3;
		// }
		// }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// use for hovering over buttons / changing button colour
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// use for hovering over buttons / changing button colour
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// TO BE FIXED
	public void keepInBound() {
		// game screen
		// MUST CHANGE 100 and mapWidth to changing variables -> instance variables for
		// currentMap
		if (screen == 5) {
			// if (suki.getHitboxM().x < 100)
			// suki.getHitboxM().x = 100;
			// else if (suki.getHitboxM().x > currentMap.getBG().getWidth() -
			// suki.getHitboxM().width)
			// suki.getHitboxM().x = currentMap.getBG().getWidth() -
			// suki.getHitboxM().width;
			// if (suki.getHitboxM().y < 100)
			// suki.getHitboxM().y = 100;
			// else if (suki.getHitboxM().y > currentMap.getBG().getHeight() -
			// suki.getHitboxM().height)
			// suki.getHitboxM().y = currentMap.getBG().getHeight() -
			// suki.getHitboxM().height;
		}

	}

	public void checkPlayerCollisions(Player player) {
        // Check collectibles
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
					(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
        Iterator<Collectible> collectibleIterator = currentMap.getItems().iterator();
        while (collectibleIterator.hasNext()) {
            Collectible collectible = collectibleIterator.next();
			Rectangle itemRect = new Rectangle(collectible.getGamePos().x, collectible.getGamePos().y, collectible.getImage().getWidth(), collectible.getImage().getHeight());
            if (playerRect.intersects(itemRect)) {
                System.out.println("Picked up");
                
                collectibleIterator.remove(); // Remove from map
                player.getInventory().add(collectible); // Add to player's inventory
                System.out.println("Picked up collectible: " + collectible.getName());
            }
        }
    
        // Check weapons
        Iterator<Weapon> weaponIterator = currentMap.getWeapons().iterator();
        while (weaponIterator.hasNext()) {
            Weapon weapon = weaponIterator.next();
            Rectangle itemRect = new Rectangle (weapon.getGamePos().x, weapon.getGamePos().y, weapon.getImage().getWidth(), weapon.getImage().getHeight());
            if (playerRect.intersects(itemRect)) {
                weaponIterator.remove(); // Remove from map
                player.getInventory().add(weapon); // Add to player's inventory
                System.out.println("Picked up weapon: " + weapon.getName());
            }
        }
    }

	public void checkCollision(Wall wall) {
		// game screen
		if (screen == 5) {

			Rectangle wallRect = new Rectangle(wall.getRect().x, wall.getRect().y, wall.getImage().getWidth(),
					wall.getImage().getHeight());
			Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
					(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
			// System.out.println();
			// check if player touches wall
			if (playerRect.intersects(wallRect)) {

				// stop the player from moving
				double left1 = playerRect.getX();
				double right1 = playerRect.getX() + playerRect.getWidth();
				double top1 = playerRect.getY();
				double bottom1 = playerRect.getY() + playerRect.getHeight();

				double left2 = wallRect.x;
				double right2 = wallRect.x + wallRect.getWidth();
				double top2 = wallRect.y;
				double bottom2 = wallRect.y + wallRect.getHeight();

				if (right1 > left2 &&
						left1 < left2 &&
						right1 - left2 < bottom1 - top2 &&
						right1 - left2 < bottom2 - top1) {
					// player collides from left side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x + suki.getSpeed(), currentMap.getTLLocation().y));
					// stop movement for cows when collision occurs
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x + suki.getSpeed(), cow.getMapPos().y);
					}
					suki.setGameX((int) (suki.getGamePos().x - suki.getSpeed()));
				} else if (left1 < right2 &&
						right1 > right2 &&
						right2 - left1 < bottom1 - top2 &&
						right2 - left1 < bottom2 - top1) {
					// player collides from right side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x - suki.getSpeed(), currentMap.getTLLocation().y));

					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x - suki.getSpeed(), cow.getMapPos().y);
					}

					suki.setGameX((int) (suki.getGamePos().x) + suki.getSpeed());
				} else if (bottom1 > top2 && top1 < top2) {
					// player collides from top side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y + suki.getSpeed()));
					// for (Wall w : currentMap.getRectWalls()) {
					// w.setGamePos(w.getRect().x, w.getRect().y + suki.getSpeed());
					// }
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y + suki.getSpeed());
					}

					suki.setGameY((int) (suki.getGamePos().y - suki.getSpeed()));
				} else if (top1 < bottom2 && bottom1 > bottom2) {
					// player collides from bottom side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y - suki.getSpeed()));

					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y - suki.getSpeed());
					}

					suki.setGameY((int) (suki.getGamePos().y + suki.getSpeed()));
				}
			}
		}
	}

	public void checkCollision(Triangle wall) {
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		int intersectionType = wall.intersects(playerRect);
		if (intersectionType > 0) {
			// get player hitbox vertices
			// point order = TL, TR, BR, BL
			Point[] hitboxVertices = { new Point(playerRect.x, playerRect.y),
					new Point(playerRect.x + playerRect.width, playerRect.y),
					new Point(playerRect.x + playerRect.width,
							playerRect.y + playerRect.height),
					new Point(playerRect.getLocation().x,
							playerRect.getLocation().y + playerRect.height) };
			Point[] wallVertices = wall.getVertices();
			// System.out.println("wall vertices: " + wallVertices[0] + " " +
			// wallVertices[1] + " " + wallVertices[2]);
			// get wall sides
			Line wallSide1 = new Line(wall.getVertices()[0], wall.getVertices()[1]);
			Line wallSide2 = new Line(wall.getVertices()[1], wall.getVertices()[2]);
			Line wallSide3 = new Line(wall.getVertices()[2], wall.getVertices()[0]);

			// get player sides
			Line playerSide1 = new Line(hitboxVertices[0], hitboxVertices[1]);
			Line playerSide2 = new Line(hitboxVertices[1], hitboxVertices[2]);
			Line playerSide3 = new Line(hitboxVertices[2], hitboxVertices[3]);
			Line playerSide4 = new Line(hitboxVertices[3], hitboxVertices[0]);

			// if player vertex is inside triangle wall
			if (intersectionType == 1) {
				handleTriCollision(wall, new Line[] { wallSide1, wallSide2, wallSide3 }, hitboxVertices);
			}
			// if triangle vertex is inside player
			else if (intersectionType == 2) {
				handleTriCollision(wall, new Line[] { playerSide1, playerSide2, playerSide3, playerSide4 },
						wallVertices);
			}
			// player side intersects wall side
			// only happens when a triangle side is parallel to a player side
			// handle like rectangular collision
			else if (intersectionType == 3) {
				// find the two sides that touch
			}
		}
	}

	public void handleTriCollision(Triangle wall, Line[] sides, Point[] vertices) {
		final double EPSILON = 1e-6; // small buffer to push the player out of the triangle more
		for (Point p : vertices) {
			if (wall.containsPoint(p)) {
				double minDist = Double.MAX_VALUE;
				int closestSideIndex = -1;

				for (int i = 0; i < sides.length; i++) {
					double distance = sides[i].getDistance(p);
					if (distance < minDist) {
						minDist = distance;
						closestSideIndex = i;
					}
				}

				if (closestSideIndex != -1) {
					Point closest = sides[closestSideIndex].closestPoint(p);
					double dx = p.x - closest.x;
					double dy = p.y - closest.y;

					// normalize the direction vector
					double length = Math.sqrt(dx * dx + dy * dy);
					dx /= length;
					dy /= length;

					// move slightly more than the minimum distance
					double pushDistance = minDist + EPSILON;

					// apply collision response
					triCollisionResponse(dx * pushDistance, dy * pushDistance);
				}
			}
		}
	}

	private void triCollisionResponse(double dx, double dy) {
		int roundedDx = (int) Math.round(dx);
		int roundedDy = (int) Math.round(dy);

		suki.setGameX(suki.getGamePos().x - roundedDx);
		suki.setGameY(suki.getGamePos().y - roundedDy);

		// Update the map's top-left location
		currentMap.setTLLocation(new Point(
				currentMap.getTLLocation().x + roundedDx,
				currentMap.getTLLocation().y + roundedDy));

		// Adjust cow positions
		for (Cow cow : currentMap.getCows()) {
			cow.inScreenMove(roundedDx, roundedDy);
		}

	}

	public void checkCollision(Cow cow) {

		final int BUFFER = 5; // Small buffer to prevent overlap
		for (Cow cow2 : currentMap.getCows()) {
			if (cow != cow2 && cow.getHitbox().intersects(cow2.getHitbox())) {
				Rectangle intersection = cow.getHitbox().intersection(cow2.getHitbox());
				int dx = intersection.width / 2 + BUFFER;
				int dy = intersection.height / 2 + BUFFER;

				if (cow.getX() < cow2.getX()) {
					cow.setGamePos(cow.getGamePos().x - dx, cow.getGamePos().y);
					cow2.setGamePos(cow2.getGamePos().x + dx, cow2.getGamePos().y);
				} else {
					cow.setGamePos(cow.getGamePos().x + dx, cow.getGamePos().y);
					cow2.setGamePos(cow2.getGamePos().x - dx, cow2.getGamePos().y);
				}

				if (cow.getY() < cow2.getY()) {
					cow.setGamePos(cow.getGamePos().x, cow.getGamePos().y - dy);
					cow2.setGamePos(cow2.getGamePos().x, cow2.getGamePos().y + dy);
				} else {
					cow.setGamePos(cow.getGamePos().x, cow.getGamePos().y + dy);
					cow2.setGamePos(cow2.getGamePos().x, cow2.getGamePos().y - dy);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		// The following lines creates your window

		// makes a brand new JFrame
		frame = new JFrame("ChaseCow");
		// makes a new copy of your "game" that is also a JPanel
		myPanel = new ChaseCow();
		// so your JPanel to the frame so you can actually see it

		frame.add(myPanel);
		// self explanatory. You don't want to resize your window because
		// it might mess up your graphics and collisions
		frame.setResizable(false);
		// self explanatory. You want to see your frame
		frame.setVisible(true);
		// some weird method that you must run
		frame.pack();
		// place your frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		// without this, your thread will keep running even when you windows is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
