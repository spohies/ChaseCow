import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
// import javax.sound.sampled.LineUnavailableException;
// import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.Timer;

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
public class ChaseCow extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	// big boss variable
	int screen = 0;
	static JPanel myPanel;
	static JFrame frame;

	// self explanatory variables
	int FPS = 60;
	Thread thread;
	int screenWidth = 1080;
	int screenHeight = 720;
	boolean up, down, left, right;
	boolean interactable = false;

	// main screen + start screen
	Rectangle recAbout, recPlay, recOptions;
	boolean hoverAbout, hoverOptions, hoverPlay;
	BufferedImage titleScreenBG, title, play, play2, about, about2, settings, settings2, options, options2, back, back2;
	Rectangle recLB, recNewGame, recBack;
	boolean hoverNewGame, hoverLB, hoverBack;
	BufferedImage menuScreenBackground, gameTitle, newGame, leaderboard, newGame2, leaderboard2;

	// settings
	Rectangle recVolume;
	// im thinking we have like 5 buttons and its just 5 levels of volume next to
	Rectangle recQuitGame;
	// each other so its like a fake slider
	BufferedImage volumeImg, quitGame;
	// int difficulty = 1; // maybe just 1=easy 2=hard?
	int volume = 3; // 1-5??

	boolean showInventory = false;
	// game
	ArrayList<BufferedImage> cowImages = new ArrayList<BufferedImage>();

	// Rectangle player = new Rectangle(517, 328, 46, 64);
	// HashSet <Rectangle> walls = new HashSet <Rectangle>();
	BufferedImage playerImage, cowImage;
	Player suki;
	// HashSet<Cow> cows = new HashSet<Cow>();
	FloorMap currentMap;
	// ArrayList<FloorMap> maps = new ArrayList<>();
	HashMap<Integer, FloorMap> maps = new HashMap<>();
	BufferedImage[] innerWallImages;
	boolean healing = false;
	boolean fullHP = true;
	String[] cowNames = {
			"baseCow",
			"spaceCow",
			"wasteCow"
	};

	// CUTSCENE IMAGES
	BufferedImage floor3SceneImg;
	BufferedImage floor2SceneImg;
	BufferedImage floor1SceneImg;
	BufferedImage ventsSceneImg;
	BufferedImage stairsSceneImg;
	BufferedImage healthBar;

	int currentCowType;
	// NPC DIALOGUE
	// Dialogue variables
	String currentDialogue; // Current line of dialogue to display
	long dialogueEndTime; // Timestamp when the dialogue box should disappear
	boolean dialogueActive; // Flag to track if dialogue is currently active
	// STAGE 1
	boolean beakerCleaned;
	int beakerWalkCount;
	boolean showBeakerMessage;
	BufferedImage spillImage;
	boolean spillCleaned;
	int spillWalkCount;
	boolean showSpillMessage;

	// STAGE 2
	boolean essayWritten;
	int essayWalkCount;
	boolean showEssayMessage;
	boolean essaySubmitted;

	// STAGE 3
	int chipCount;
	boolean chipsStolen;
	boolean showLockedMessage;
	BufferedImage keyImage;
	boolean showVentMessage;

	// STAGE 4
	int tissueCount;
	BufferedImage boxPileImage;
	BufferedImage tissueImage;
	int balconyKeyCount;
	boolean avTalkedTo;
	boolean showBoxMessage;
	boolean boxesMoved;
	boolean showLockedBalconyMessage;
	boolean showLockedLoungeMessage;
	boolean showStairwellLockedMessage;
	int chowCount;

	// STAGE 5
	BufferedImage mopImage;
	boolean mopped;
	boolean showWaterMessage;
	boolean showMopMessage;
	BufferedImage weightImage;
	BufferedImage floodImage;
	boolean moppable;
	int weightCount;

	public ChaseCow() throws IOException {
		importImages();
		// resetVariables();
		initialize();
		currentMap = maps.get(224);
		setPreferredSize(new Dimension(1080, 720));

		// sets up JPanel
		frame.getContentPane().add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true); // Ensure the panel is focusable
		requestFocusInWindow(); // Request focus for key events
		addMouseListener(this);
		addMouseMotionListener(this);
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
			try {
				update();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	public void initialize() throws IOException {
		currentCowType = 0;
		// NPC DIALOGUE
		// Dialogue variables
		currentDialogue = null; // Current line of dialogue to display
		dialogueEndTime = 0; // Timestamp when the dialogue box should disappear
		dialogueActive = false; // Flag to track if dialogue is currently active
		// STAGE 1
		beakerCleaned = false;
		beakerWalkCount = 0;
		showBeakerMessage = false;
		spillImage = null;
		spillCleaned = true;
		spillWalkCount = 0;
		showSpillMessage = false;

		// STAGE 2
		essayWritten = true;
		essayWalkCount = 0;
		showEssayMessage = false;
		essaySubmitted = true;

		// STAGE 3
		chipCount = 0;
		chipsStolen = false;
		showLockedMessage = false;
		keyImage = null;
		showVentMessage = false;

		// STAGE 4
		tissueCount = 0;
		boxPileImage = null;
		tissueImage = null;
		balconyKeyCount = 0;
		avTalkedTo = false;
		showBoxMessage = false;
		boxesMoved = false;
		showLockedBalconyMessage = false;
		showLockedLoungeMessage = false;
		showStairwellLockedMessage = false;
		chowCount = 0;

		// STAGE 5
		mopImage = null;
		mopped = false;
		showWaterMessage = false;
		showMopMessage = false;
		moppable = false;
		weightCount = 0;

		try {
			titleScreenBG = ImageIO.read(getClass().getResource("/menu/titleBG.png"));
			// System.out.println("Loaded titleScreenBG image");
			int numCows = cowNames.length;
			for (int i = 0; i < numCows; i++) {
				String cowPath = "/sprites/" + cowNames[i] + ".png";
				cowImage = ImageIO.read(getClass().getResource(cowPath));
				cowImages.add(cowImage);
				// System.out.println("Loaded cow image: " + cowPath);
			}

			// screen 0 (main menu)
			title = ImageIO.read(getClass().getResource("/menu/title.png"));
			play = ImageIO.read(getClass().getResource("/menu/playbutton.png"));
			about = ImageIO.read(getClass().getResource("/menu/aboutbutton.png"));
			play2 = ImageIO.read(getClass().getResource("/menu/playbutton2.png"));
			about2 = ImageIO.read(getClass().getResource("/menu/aboutbutton2.png"));
			options = ImageIO.read(getClass().getResource("/menu/optionsbutton.png"));
			options2 = ImageIO.read(getClass().getResource("/menu/optionsbutton2.png"));
			back = ImageIO.read(getClass().getResource("/menu/backbutton.png"));
			back2 = ImageIO.read(getClass().getResource("/menu/backbutton2.png"));
			newGame = ImageIO.read(getClass().getResource("/menu/newgamebutton.png"));
			newGame2 = ImageIO.read(getClass().getResource("/menu/newgamebutton2.png"));
			leaderboard = ImageIO.read(getClass().getResource("/menu/lbbutton.png"));
			leaderboard2 = ImageIO.read(getClass().getResource("/menu/lbbutton2.png"));
			quitGame = ImageIO.read(getClass().getResource("/menu/quitgamebutton.png"));
			volumeImg = ImageIO.read(getClass().getResource("/menu/volume.png"));

			// DIMENSION NEED TO BE CHANGED!!!!!!
			recPlay = new Rectangle(100, 200, play.getWidth(), play.getHeight());
			recAbout = new Rectangle(100, 300, about.getWidth(), about.getHeight());
			recOptions = new Rectangle(990, 30, options.getWidth(), options.getHeight());
			recBack = new Rectangle(30, 30, back.getWidth(), back.getHeight());
			recNewGame = new Rectangle(100, 200, newGame.getWidth(), newGame.getHeight());
			recLB = new Rectangle(100, 300, leaderboard.getWidth(), leaderboard.getHeight());
			recQuitGame = new Rectangle(400, 400, quitGame.getWidth(), quitGame.getHeight());
			recVolume = new Rectangle(100, 200, volumeImg.getWidth(), volumeImg.getHeight());
			// for (int i = 0; i < 5; i++) {
			// recVolume.add(new Rectangle(100 + i * 50, 200, volumeImg.getWidth(),
			// volumeImg.getHeight()));
			// }

			BufferedImage playerImageDown = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			BufferedImage playerImageUp = ImageIO.read(getClass().getResource("/sprites/sukiUp.png"));
			BufferedImage playerImageRight = ImageIO.read(getClass().getResource("/sprites/sukiRight.png"));
			BufferedImage playerImageLeft = ImageIO.read(getClass().getResource("/sprites/sukiLeft.png"));

			floor3SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR3SCENE.png"));
			floor2SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR2SCENE.png"));
			floor1SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR1SCENE.png"));
			ventsSceneImg = ImageIO.read(getClass().getResource("/menu/VENTSCENE.png"));
			stairsSceneImg = ImageIO.read(getClass().getResource("/menu/STAIRSCENE.png"));

			healthBar = ImageIO.read(getClass().getResource("/menu/HPBAR.png"));

			spillImage = ImageIO.read(getClass().getResource("/map files/spill.png"));
			keyImage = ImageIO.read(getClass().getResource("/sprites/key.png"));
			tissueImage = ImageIO.read(getClass().getResource("/sprites/tissueBox.png"));
			boxPileImage = ImageIO.read(getClass().getResource("/map files/boxPile.png"));
			mopImage = ImageIO.read(getClass().getResource("/sprites/mop.png"));
			weightImage = ImageIO.read(getClass().getResource("/sprites/weight.png"));
			floodImage = ImageIO.read(getClass().getResource("/map files/flood.png"));

			System.out.println("spill loaded");
			suki = new Player(100, 4, new Rectangle(517, 382, 46, 10), new Rectangle(517, 328, 46, 64), 225, 275,
					playerImageDown, playerImageUp, playerImageRight, playerImageLeft);
			playerImage = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			System.out.println("Loaded player image");
			cowImage = ImageIO.read(getClass().getResource("/sprites/baseCow.png"));
			System.out.println("Loaded cow image");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetVariables() throws IOException {
		initialize();
		initializeMaps();
	}

	public void update() throws IOException {
		// update stuff

		if (screen == 5) {
			if (suki.getHP() <= 0) {
				showGameOverPanel();
			}
			move();
			if (currentMap != null) {
				currentMap.updateCows(suki);
			} else {
				System.out.println("No current map is set.");
			}

			Iterator<Wall> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {
				checkCollision(it.next());
			}

			Iterator<Wall> iter = currentMap.getInnerWalls().iterator();
			while (iter.hasNext()) {
				checkCollision(iter.next());
			}

			Iterator<Triangle> itr = currentMap.getTriWalls().iterator();
			while (itr.hasNext()) {
				checkCollision(itr.next());
			}

			if (!currentMap.getCows().isEmpty()) {
				Iterator<Cow> cowItr = currentMap.getCows().iterator();
				while (cowItr.hasNext()) {
					Cow cow = cowItr.next();
					checkCollision(cow);
				}
			}

			checkPlayerCollisions(suki);

			if (currentMap.getNPCs() != null) {
				for (NPC npc : currentMap.getNPCs()) {
					double distance = getNPCDistance(suki, npc);
					npc.setWithinRange(distance < 75);
				}

			}

			for (Door door : currentMap.getDoors()) {
				checkCollision(door);
			}

			if (currentMap == maps.get(3)) {
				Rectangle waterFountain = new Rectangle(6500, 950, 100, 25);
				Rectangle waterFountain2 = new Rectangle(1075, 3450, 50, 25);
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (waterFountain.intersects(playerRect) || waterFountain2.intersects(playerRect)) {
					healing = true;
				} else {
					healing = false;
				}

				Rectangle spillRect = new Rectangle(4400, 1850, 100, 300);
				if (spillRect.intersects(playerRect) && !spillCleaned) {
					double left1 = playerRect.getX();
					double right1 = playerRect.getX() + playerRect.getWidth();
					double right2 = spillRect.getX() + spillRect.getWidth();
					if (left1 < right2 && right1 > right2) {
						// Collision from right
						suki.setGameX(suki.getGamePos().x + suki.getSpeed());
					}
					Collectible spill = new Collectible("Goop", "goopy goop", spillImage, new Point(4400, 1850), 0);
					int index1 = suki.searchInventory("Beaker");
					int index2 = suki.searchInventory("Broom");
					if (index1 >= 0 && index2 >= 0) {
						spillCleaned = true;
						spill.setImage(ImageIO.read(getClass().getResource("/sprites/beakergoop.png")));
						spill.setDescription("Beaker of mysterious purple liquid.");
						spill.setName("Beaker with Goop");
						updateEquippedItemIndexBeforeChange();
						suki.getInventory().remove(suki.searchInventory("Beaker"));
						updateEquippedItemIndexAfterChange();
						suki.getInventory().add(spill);
						System.out.println("Spill cleaned.");
						currentMap.getNPCs().get(0).setState(1);
						currentMap.getNPCs().get(0).setCurrentDialogueIndex(0);
						spillWalkCount++;
					} else {
						showSpillMessage = true;
					}
				} else {
					showSpillMessage = false;
				}

				Rectangle msKimThreshold = new Rectangle(3200, 2625, 100, 200);
				if (msKimThreshold.intersects(playerRect) && !essaySubmitted) {
					// stop the player from moving
					double top1 = playerRect.getY();
					double bottom1 = playerRect.getY() + playerRect.getHeight();
					double top2 = msKimThreshold.y;
					if (bottom1 > top2 && top1 < top2) {
						// player collides from top side of the wall
						suki.setGameY((int) (suki.getGamePos().y - suki.getSpeed()));
					}
				}
			}

			if (currentMap == maps.get(2)) {
				Rectangle waterFountain = new Rectangle(475, 3400, 50, 25);
				Rectangle waterFountain2 = new Rectangle(6850, 950, 50, 25);
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (waterFountain.intersects(playerRect) || waterFountain2.intersects(playerRect)) {
					healing = true;
				} else {
					healing = false;
				}

				Rectangle boxesRect = new Rectangle(4600, 1800, 200, 500);
				if (boxesRect.intersects(playerRect) && !boxesMoved) {
					double left1 = playerRect.getX();
					double right1 = playerRect.getX() + playerRect.getWidth();
					double right2 = boxesRect.getX() + boxesRect.getWidth();
					if (left1 < right2 && right1 > right2) {
						// Collision from right
						suki.setGameX(suki.getGamePos().x + suki.getSpeed());
					}
					if (avTalkedTo) {
						boxesMoved = true;
						System.out.println("Boxes moved.");
					} else {
						showBoxMessage = true;
					}
				} else {
					showBoxMessage = false;
				}

				Rectangle floodRect = new Rectangle(1100, 3450, 150, 50);
				if (floodRect.intersects(playerRect) && !mopped) {
					showWaterMessage = true;
				} else {
					showWaterMessage = false;
				}
			}

			Rectangle floodRect1 = new Rectangle(50, 425, 100, 150);
			if (currentMap == maps.get(23)) {
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				System.out.println(suki.getGamePos());
				if (floodRect1.intersects(playerRect) && !mopped) {
					showMopMessage = true;
					int index = suki.searchInventory("Mop");
					if (index >= 0 && suki.getEquippedItem() == index) {
						moppable = true;
					} else {
						moppable = false;
					}
				} else {
					showMopMessage = false;
				}
			}

			Rectangle floodRect2 = new Rectangle(150, 400, 100, 150);
			if (currentMap == maps.get(22)) {
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (floodRect2.intersects(playerRect) && !mopped) {
					int index = suki.searchInventory("Mop");
					if (index >= 0 && suki.getEquippedItem() == index) {
						moppable = true;
					} else {
						moppable = false;
					}
					showMopMessage = true;
				} else {
					showMopMessage = false;
				}
			}

			long lastHealTime = 0;
			int healCooldown = 500; // 0.5 second cooldown for healing
			if (healing && !fullHP) {
				System.out.println("HEALING");
				if (System.currentTimeMillis() - lastHealTime >= healCooldown) {
					suki.setHP(suki.getHP() + 1);
					lastHealTime = System.currentTimeMillis();
				} else {
					fullHP = true;
				}
			}

			if (suki.getHP() >= suki.getMaxHP()) {
				fullHP = true;
			} else {
				fullHP = false;
			}
		}
	}

	public void initializeMaps() throws IOException {
		// Create and add FloorMap objects to the maps list
		BufferedReader b = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("/map files/mapInfo.txt")));
		try {
			int numMaps = Integer.parseInt(b.readLine().trim());
			for (int i = 0; i < numMaps; i++) {
				int mapID = Integer.parseInt(b.readLine().trim());
				System.out.println("Loading map " + mapID);
				String bgPath = b.readLine().trim();
				BufferedImage bg = ImageIO.read(getClass().getResource(bgPath));
				HashSet<Wall> rectWalls = new HashSet<>();
				TreeSet<Wall> innerWalls = new TreeSet<>();
				HashSet<Cow> cows = new HashSet<>();
				ArrayList<Weapon> weapons = new ArrayList<>();
				ArrayList<Collectible> collectibles = new ArrayList<>();
				ArrayList<NPC> npcs = new ArrayList<>();
				ArrayList<Door> doors = new ArrayList<>();
				int numRectWalls = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numRectWalls; j++) {
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					rectWalls.add(
							new Wall(null, null, new Rectangle(tlPoint.x, tlPoint.y, Integer.parseInt(st.nextToken()),
									Integer.parseInt(st.nextToken()))));
					System.out.println("loaded walls");
				}
				int numTriWalls = Integer.parseInt(b.readLine().trim());
				HashSet<Triangle> triWalls = new HashSet<>();
				for (int j = 0; j < numTriWalls; j++) {
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point[] vertices = new Point[3];
					for (int k = 0; k < 3; k++) {
						vertices[k] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					}
					triWalls.add(new Triangle(vertices[0], vertices[1], vertices[2]));
					System.out.println("loaded triangles");
				}
				int numInnerWalls = Integer.parseInt(b.readLine().trim());
				System.out.println("number of inner walls:" + numInnerWalls);
				for (int j = 0; j < numInnerWalls; j++) {
					StringTokenizer str = new StringTokenizer(b.readLine().trim());
					Rectangle hitbox = new Rectangle(Integer.parseInt(str.nextToken()),
							Integer.parseInt(str.nextToken()), Integer.parseInt(str.nextToken()),
							Integer.parseInt(str.nextToken()));
					System.out.println(hitbox);
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					System.out.println(tlPoint);
					int imageIdx = Integer.parseInt(b.readLine().trim());
					BufferedImage img = innerWallImages[imageIdx];
					innerWalls.add(new Wall(tlPoint, img, hitbox));
					System.out.println("loaded inside walls");
					System.out.println("total inner walls:" + innerWalls.size());
				}
				int numWeapons = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numWeapons; j++) {
					String name = b.readLine().trim();
					String desc = b.readLine().trim();
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					int reach = Integer.parseInt(st.nextToken());
					int damage = Integer.parseInt(st.nextToken());
					String imgPath = b.readLine().trim();
					BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
					StringTokenizer stk = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(stk.nextToken()), Integer.parseInt(stk.nextToken()));
					weapons.add(new Weapon(name, desc, reach, damage, img, tlPoint));
					System.out.println("loaded weapons");
				}
				int numCollectibles = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numCollectibles; j++) {
					String name = b.readLine().trim();
					String desc = b.readLine().trim();
					int reach = Integer.parseInt(b.readLine().trim());
					String imgPath = b.readLine().trim();
					BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
					StringTokenizer stk = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(stk.nextToken()), Integer.parseInt(stk.nextToken()));
					collectibles.add(new Collectible(name, desc, img, tlPoint, reach));
					System.out.println("loaded collectibles");
				}
				int numNPCs = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numNPCs; j++) {
					String name = b.readLine().trim();
					String imgPath = b.readLine().trim();
					BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					int numStates = Integer.parseInt(b.readLine().trim());
					String[][] dialogue = new String[numStates][];
					for (int k = 0; k < numStates; k++) {
						int numDialogues = Integer.parseInt(b.readLine().trim());
						dialogue[k] = new String[numDialogues]; // Initialize the inner array for this state
						for (int l = 0; l < numDialogues; l++) {
							dialogue[k][l] = b.readLine().trim();
						}
					}
					npcs.add(new NPC(name, tlPoint, dialogue, img));
					System.out.println("loaded npcs");
				}
				int numDoors = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numDoors; j++) {
					System.out.println("loading doors");
					Integer mapDest = Integer.parseInt(b.readLine().trim());
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					StringTokenizer stk = new StringTokenizer(b.readLine().trim());
					Point exitPos = new Point(Integer.parseInt(stk.nextToken()), Integer.parseInt(stk.nextToken()));
					doors.add(new Door(new Rectangle(tlPoint.x, tlPoint.y, 50, 50), mapDest, exitPos));
					System.out.println("loaded doors");
				}
				// maps.add(new FloorMap(mapID, new Point(0, 0), bg, rectWalls, triWalls));
				int numCows = Integer.parseInt(b.readLine().trim());
				System.out.println("cows in " + mapID + ": " + numCows);
				for (int j = 0; j < numCows; j++) {
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					int cowType = Integer.parseInt(st.nextToken());
					System.out.println(tlPoint + " " + cowType);
					if (cowType == 0) {
						cows.add(new BaseCow(tlPoint.x, tlPoint.y, cowImages.get(cowType), currentMap, suki));
					}
					if (cowType == 1) {
						cows.add(new SpaceCow(tlPoint.x, tlPoint.y, cowImages.get(cowType), currentMap, suki));
					}
					System.out.println("loaded cows");
				}
				System.out.println("Loaded map " + mapID);
				currentMap = maps.get(30);
				maps.put(mapID, new FloorMap(new Point(0, 0), rectWalls, triWalls, innerWalls, bg, doors, cows, npcs,
						weapons, collectibles));
				repaint();
			}
		} catch (NumberFormatException e) {
			System.out.println("Error reading map info file");
		}
		b.close();
	}

	public void importImages() throws IOException {
		// IMPORTS ALL IMAGES
		BufferedReader b = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("/inner walls/wallFiles.txt")));
		System.out.println("Found wall file");
		try {
			int numImages = Integer.parseInt(b.readLine().trim());
			innerWallImages = new BufferedImage[numImages];
			for (int i = 0; i < numImages; i++) {
				String imgPath = b.readLine().trim();
				innerWallImages[i] = ImageIO.read(getClass().getResource(imgPath));
				System.out.println("Loaded inner wall image: " + imgPath);
			}
		} catch (NumberFormatException e) {
			System.out.println("Error reading wall files");
		}
	}

	public double getNPCDistance(Player suki, NPC npc) {
		// Get NPC center coordinates
		int npcCenterX = npc.getGamePos().x + (npc.getImage().getWidth() / 2);
		int npcCenterY = npc.getGamePos().y + (npc.getImage().getHeight() / 2);

		// Get Player center coordinates
		int playerCenterX = suki.getGamePos().x + (suki.getHitboxM().width / 2);
		int playerCenterY = suki.getGamePos().y + (suki.getHitboxM().height / 2);

		return Math.sqrt(Math.pow(playerCenterX - npcCenterX, 2) + Math.pow(playerCenterY - npcCenterY, 2));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;
		// main screen
		if (screen == 0) {
			// g.drawImage(titleScreenBG, 0, 0, titleScreenBG.getWidth(),
			// titleScreenBG.getHeight(), this);
			g.drawImage(title, 100, 100, title.getWidth(), title.getHeight(), this);
			g.drawImage(hoverPlay ? play2 : play, 100, 200, play.getWidth(), play.getHeight(), this);
			g.drawImage(hoverAbout ? about2 : about, 100, 300, about.getWidth(), about.getHeight(), this);
		}

		// start game screen
		if (screen == 1) {
			g.drawImage(hoverNewGame ? newGame2 : newGame, 100, 200, newGame.getWidth(), newGame.getHeight(),
					this);
			g.drawImage(hoverLB ? leaderboard2 : leaderboard, 100, 300, leaderboard.getWidth(),
					leaderboard.getHeight(), this);
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// about screen
		if (screen == 2) {
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		if (screen == 3) {
			g.drawImage(hoverBack ? back2 : back, 30, 30, back.getWidth(), back.getHeight(), this);
			g.drawImage(volumeImg, 100, 200, volumeImg.getWidth(), volumeImg.getHeight(), this);
			g.drawImage(quitGame, 400, 400, quitGame.getWidth(), quitGame.getHeight(), this);
		}

		if (screen == 4) {
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// game screen
		if (screen == 5) { // Game screen
			int playerX = suki.getGamePos().x + (int) suki.getHitboxC().getWidth() / 2;
			int playerY = suki.getGamePos().y - (int) suki.getHitboxC().getHeight() / 2;
			g2.setColor(Color.GREEN);

			g2.setFont(new Font("Dialog", Font.PLAIN, 16));
			g.setFont(new Font("Dialog", Font.PLAIN, 16));
			if (currentMap.getBG() != null) {

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

				// Draw the background
				setBackground(Color.BLACK);
				g.drawImage(currentMap.getBG(),
						0, 0, destWidth, destHeight, // Destination rectangle
						srcX, srcY, srcX2, srcY2, // Source rectangle
						this);

			} else {
				System.out.println("Background image is null");
			}

			if (currentMap.getNPCs() != null) {
				Iterator<NPC> npcIt = currentMap.getNPCs().iterator();
				while (npcIt.hasNext()) {
					NPC npc = npcIt.next();
					// Translate NPC's in-game position to screen coordinates
					int npcScreenX = (npc.getGamePos().x - playerX) + ((screenWidth / 2));
					int npcScreenY = (npc.getGamePos().y - playerY) + ((screenHeight / 2));

					// Draw NPC
					if (npc.getImage() != null) {
						g.drawImage(npc.getImage(), npcScreenX, npcScreenY, npc.getImage().getWidth(),
								npc.getImage().getHeight(),
								this);
					} else {
						g.setColor(Color.BLUE);
						g.fillRect(npcScreenX, npcScreenY, 50, 50); // Placeholder
					}
				}
			}
			g.setFont(new Font("Dialog", Font.PLAIN, 16));

			if (!mopped) {
				if (showMopMessage && (currentMap == maps.get(22) || currentMap == maps.get(23))) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Looks like I need the mop", 60, 580);

				}
				if (currentMap == maps.get(23)) {
					int floodScreenX = (25 - suki.getGamePos().x) + (screenWidth / 2);
					int floodScreenY = (425 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(floodImage, floodScreenX, floodScreenY, floodImage.getWidth(), floodImage.getHeight(),
							this);
					if (moppable && showMopMessage) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press F to mop", 60, 580);
					}
				}
				if (currentMap == maps.get(22)) {
					int floodScreenX = (125 - suki.getGamePos().x) + (screenWidth / 2);
					int floodScreenY = (400 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(floodImage, floodScreenX, floodScreenY, floodImage.getWidth(), floodImage.getHeight(),
							this);
					if (moppable && showMopMessage) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press F to mop", 60, 580);
					}

				}
				if (showWaterMessage && currentMap == maps.get(2)) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Ew! Ew! Ew! I hate water.", 60, 580);
				}
			}

			drawInnerWalls(g2, playerX, playerY);

			for (Triangle tri : currentMap.getTriWalls()) {
				Point[] vertices = tri.getVertices();
				int[] xPoints = { (vertices[0].x - playerX) + (screenWidth / 2),
						(vertices[1].x - playerX) + (screenWidth / 2), (vertices[2].x - playerX) + (screenWidth / 2) };
				int[] yPoints = { (vertices[0].y - playerY) + (screenHeight / 2),
						(vertices[1].y - playerY) + (screenHeight / 2),
						(vertices[2].y - playerY) + (screenHeight / 2) };
				g2.fillPolygon(xPoints, yPoints, 3);
			}
			// System.out.println("walls in front:" + wallsInFront.size());

			if (!currentMap.getCows().isEmpty()) {
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

			double distance;
			if (currentMap.getNPCs() != null) {
				for (NPC npc : currentMap.getNPCs()) {
					g2.setFont(new Font("Dialog", Font.PLAIN, 16));
					distance = getNPCDistance(suki, npc);
					if (distance < 75) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press SPACE to interact", 60, 580);
					}
					// Render dialogue box if active
					if (dialogueActive) {
						// Draw dialogue box with same dimensions and style as the interaction box
						g2.setColor(Color.WHITE);
						g2.fillRect(50, 550, 600, 100); // Same size and position as the interaction box
						g2.setColor(Color.BLACK);
						g2.drawRect(50, 550, 600, 100);

						// Draw dialogue text inside the box
						if (currentDialogue != null) {
							String[] lines = currentDialogue.split("\n");
							int yPosition = 580; // Start text slightly below the top of the box
							for (String line : lines) {
								g2.drawString(line, 60, yPosition); // Indent slightly from the left
								yPosition += 20; // Spacing between lines
							}
						}

						// Hide dialogue box after the timer expires
						if (System.currentTimeMillis() > dialogueEndTime) {
							dialogueActive = false;
							currentDialogue = null;
						}
					}
				}
			}

			if (showInventory) {
				int boxSize = 50; // Size of each inventory box
				int padding = 10; // Padding between boxes
				int startX = 60; // Starting X position for the grid
				int startY = 100; // Starting Y position for the grid
				int cols = 6; // Number of columns in the grid

				g.setColor(Color.WHITE);
				g.fillRect(50, 50, 400, 400);
				g.setColor(Color.BLACK);
				g.drawString("Inventory", 60, 70);
				g.drawString("Press E to close", 60, 90);

				for (int i = 0; i < suki.getInventory().size(); i++) {
					Item item = suki.getInventory().get(i);
					int row = i / cols;
					int col = i % cols;
					int x = startX + col * (boxSize + padding);
					int y = startY + row * (boxSize + padding);

					// Draw item box
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(x, y, boxSize, boxSize);
					g.setColor(Color.BLACK);
					g.drawRect(x, y, boxSize, boxSize);

					// Draw item icon
					BufferedImage image = item.getImage();
					if (image != null) {
						g.drawImage(image, x + (boxSize - image.getWidth()) / 2, y + (boxSize - image.getHeight()) / 2,
								null);
					}

				}

				Point mousePos = getMousePosition();
				if (mousePos != null) {
					for (int i = 0; i < suki.getInventory().size(); i++) {
						Item item = suki.getInventory().get(i);
						int row = i / cols;
						int col = i % cols;
						int x = startX + col * (boxSize + padding);
						int y = startY + row * (boxSize + padding);

						Rectangle itemRect = new Rectangle(x, y, boxSize, boxSize);
						if (itemRect.contains(mousePos)) {
							if (itemRect.contains(mousePos)) {
								String itemName = item.getName();
								String itemDescription = item.getDescription();
								FontMetrics metrics = g.getFontMetrics();
								int textWidth = Math.max(metrics.stringWidth(itemName),
										metrics.stringWidth(itemDescription));
								int textHeight = metrics.getHeight() * 2;

								// Draw background box
								g.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
								g.fillRect(mousePos.x, mousePos.y - textHeight, textWidth + 10, textHeight + 10);

								// Draw item name and description
								g.setColor(Color.BLACK);
								Font originalFont = g.getFont();
								Font boldFont = originalFont.deriveFont(Font.BOLD);
								g.setFont(boldFont);
								g.drawString(itemName, mousePos.x + 5, mousePos.y - textHeight + metrics.getHeight());
								g.setFont(originalFont);
								g.drawString(itemDescription, mousePos.x + 5,
										mousePos.y - textHeight + metrics.getHeight() * 2);

								if (item instanceof Weapon) {
									Weapon weapon = (Weapon) item; // Cast the item to Weapon
									String damage = "Damage: " + weapon.getDamage();
									String reach = "Reach: " + weapon.getReach();

									// Display weapon stats
									g.drawString(damage, mousePos.x + 5,
											mousePos.y - textHeight + metrics.getHeight() * 3);
									g.drawString(reach, mousePos.x + 5,
											mousePos.y - textHeight + metrics.getHeight() * 4);
								}
							}
						}
					}
				}

			}

			for (Door door : currentMap.getDoors()) {
				if (door.interactable()) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100); // Example box size
					g.setColor(Color.BLACK);
					g.drawString("Press C to interact", 60, 580);
				}
			}

			if (beakerCleaned && showBeakerMessage) {
				if (beakerWalkCount == 1) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					if (beakerWalkCount == 1) {
						g.drawString("Sweeping. . .", 60, 580);
						javax.swing.Timer timer = new javax.swing.Timer(3000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								showBeakerMessage = false;
								beakerWalkCount++;
								repaint();
							}
						});
						timer.setRepeats(false);
						timer.start();
					} else {
						g.drawString("Shattered Glass has been sweeped.", 60, 580);
					}
					g.drawString("Shattered Glass has been sweeped.", 60, 580);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Shattered Glass has been sweeped.", 60, 580);
				}
			} else if (!beakerCleaned && showBeakerMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("\"If only I had a broom...\"", 60, 580);
			}

			if (!spillCleaned && showSpillMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("\"Looks like I need a broom and a container...\"", 60, 580);
			}

			if (!essayWritten && showEssayMessage && !essaySubmitted) {
				int index = suki.searchInventory("Sharp Pencil");
				if (index < 0) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"Dang... I need to find a pencil.\"", 60, 580);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"I should write my essay.\"", 60, 580);
				}
			} else if (essayWritten && !essaySubmitted) {

				if (currentMap == maps.get(310)) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"Wow! Mrs. Kim is going to love this one!\"", 60, 580);
				}
			}

			if (chipsStolen) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Mr. Lee: Hey kid! That's not for you! >:(", 60, 580);
			}

			if (showLockedMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("This door is locked...?", 60, 580);
			}

			if (showLockedBalconyMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("???: Help! I'm locked in here!", 60, 580);
			}

			if (showLockedLoungeMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Only teachers have the key to this room...", 60, 580);
			}

			if (!boxesMoved && showBoxMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("\"There are too many boxes, I can't move all of them myself.\"", 60, 580);
			} else if (!boxesMoved) {
				g.drawImage(boxPileImage, 4520, 1975, boxPileImage.getWidth(), boxPileImage.getHeight(), this);
			}

			if (showStairwellLockedMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("This door is locked, but it looks a little beat up...", 60, 580);
			}

			if (showVentMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Looks like I need to unscrew this to get through.", 60, 580);
			}

			if (healing && !fullHP) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Healing...", 60, 580);
				System.out.println(suki.getHP());
			} else if (healing && fullHP) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Suki is hydrated.", 60, 580);
			}

			// Draw border walls of each map
			if (currentMap != null) {
				g.setColor(Color.RED); // Set color for border walls
				for (Wall wall : currentMap.getRectWalls()) {
					int wallScreenX = (wall.getRect().x - suki.getGamePos().x - 24) + (screenWidth / 2);
					int wallScreenY = (wall.getRect().y - suki.getGamePos().y + 32) + (screenHeight / 2);
					g.drawRect(wallScreenX, wallScreenY, wall.getRect().width, wall.getRect().height);
				}
			}

			int barWidth = suki.getHP() * 4; // Each HP point is 4 pixels
			int barHeight = 20;
			int barX = screenWidth - 445;
			int barY = screenHeight - barHeight - 45;
			g3.setColor(Color.RED);
			g3.fillRect(barX, barY, barWidth, barHeight);
			g.drawImage(healthBar, screenWidth - 460, screenHeight - 130, healthBar.getWidth(), healthBar.getHeight(),
					this);
			// Draw equipped item icon if available
			int equippedIndex = suki.getEquippedItem();
			if (equippedIndex >= 0 && equippedIndex < suki.getInventory().size()) {
				Item equippedItem = suki.getInventory().get(equippedIndex);
				BufferedImage equippedItemImage = equippedItem.getImage();

				if (equippedItemImage != null) {
					int itemX = screenWidth - 460 - equippedItemImage.getWidth() - 40; // Position to the left of health
																						// bar
					int itemY = screenHeight - 130 + 20; // Align vertically with health bar
					g.drawImage(equippedItemImage, itemX, itemY, equippedItemImage.getWidth() + 20,
							equippedItemImage.getHeight() + 20, this);
				}
			}

			g.drawImage(options, 990, 30, options.getWidth(), options.getHeight(), this);
		}

		// floor 3 start screen
		if (screen == 6) {
			g.drawImage(floor3SceneImg, 0, 0, floor3SceneImg.getWidth(), floor3SceneImg.getHeight(), this);
		}

		// vent cutscene
		if (screen == 7) {
			g.drawImage(ventsSceneImg, 0, 0, ventsSceneImg.getWidth(), ventsSceneImg.getHeight(), this);
		}

		// floor 2 start screen
		if (screen == 8) {
			g.drawImage(floor2SceneImg, 0, 0, floor2SceneImg.getWidth(), floor2SceneImg.getHeight(), this);
		}

		// stairwell cutscene
		if (screen == 9) {
			g.drawImage(stairsSceneImg, 0, 0, stairsSceneImg.getWidth(), stairsSceneImg.getHeight(), this);
		}

		// floor 1 start screen
		if (screen == 10) {
			g.drawImage(floor1SceneImg, 0, 0, floor1SceneImg.getWidth(), floor1SceneImg.getHeight(), this);
		}
	}

	public void drawInnerWalls(Graphics2D g, int playerX, int playerY) {
		Set<Wall> wallsBehind = currentMap.getInnerWalls()
				.headSet(new Wall(new Point(0, 0), null, new Rectangle(0, playerY, 0, 0)));
		for (Wall wall : wallsBehind) {
			g.drawImage(wall.getImage(), (wall.getTLLocation().x - playerX) + (screenWidth / 2),
					(wall.getTLLocation().y - playerY) + (screenHeight / 2), wall.getImage().getWidth(),
					wall.getImage().getHeight(), this);
			g.drawRect((wall.getRect().x - playerX) + (screenWidth / 2),
					(wall.getRect().y - playerY) + (screenHeight / 2), wall.getRect().width, wall.getRect().height);
		}

		if (currentMap == maps.get(3) && !spillCleaned) {
			int spillScreenX = (4400 - suki.getGamePos().x) + (screenWidth / 2);
			int spillScreenY = (1900 - suki.getGamePos().y) + (screenHeight / 2);
			g.drawImage(spillImage, spillScreenX, spillScreenY, 100, 150, this);
		}

		// System.out.println("walls behind:" + wallsBehind.size());
		if (suki.getCurrentSprite() != null) {
			g.drawImage(suki.getCurrentSprite(),
					(screenWidth - suki.getCurrentSprite().getWidth()) / 2,
					(screenHeight - suki.getCurrentSprite().getHeight()) / 2,
					this);
			// g2.fill(suki.getHitboxM());
			// g.setColor(Color.GREEN); // Draw the hitbox in green for visibility
			// g.drawRect(suki.getHitboxC().x, suki.getHitboxC().y, suki.getHitboxC().width,
			// suki.getHitboxC().height);

		} else {
			System.out.println("Player image is null");
		}
		Set<Wall> wallsInFront = currentMap.getInnerWalls()
				.tailSet(new Wall(new Point(0, 0), null, new Rectangle(0, playerY, 0, 0)));
		for (Wall wall : wallsInFront) {
			g.drawImage(wall.getImage(), (wall.getTLLocation().x - playerX) + (screenWidth / 2),
					(wall.getTLLocation().y - playerY) + (screenHeight / 2), wall.getImage().getWidth(),
					wall.getImage().getHeight(), this);
			g.drawRect((wall.getRect().x - playerX) + (screenWidth / 2),
					(wall.getRect().y - playerY) + (screenHeight / 2), wall.getRect().width, wall.getRect().height);
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
			if (key == KeyEvent.VK_SPACE) {
				if (currentMap.getNPCs() != null) {
					for (NPC npc : currentMap.getNPCs()) {
						if (npc.interactable()) {
							if (currentMap == maps.get(3) && npc == currentMap.getNPCs().get(1)) {
								if (npc.getState() == 1 && essayWritten && !essaySubmitted) {
									int index = suki.searchInventory("Foolscap");
									if (index >= 0) {
										System.out.println(index);
										if (index >= 0) {
											updateEquippedItemIndexBeforeChange();
											suki.removeFromInventory(index);
											updateEquippedItemIndexAfterChange();
											essaySubmitted = true;
										} else {
											System.out.println("Invalid index: " + index);
										}
									}
								}
							}

							// Get the current dialogue set based on NPC's state
							if (npc.getState() < npc.getDialogues().length) {
								if (currentMap == maps.get(341) && npc == currentMap.getNPCs().get(0)) {
									int index = suki.searchInventory("Meter Stick");
									if (index < 0) {
										npc.setState(0);
										npc.setCurrentDialogueIndex(0);
									} else {
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}

								if (currentMap == maps.get(345) && npc == currentMap.getNPCs().get(1)) {
									int index = suki.searchInventory("Lays Classic Chips");
									if (index >= 0 && chipCount == 0) {
										updateEquippedItemIndexBeforeChange();
										suki.removeFromInventory(index);
										chipCount++;
										suki.addToInventory(new Collectible("Math Storage Key",
												"Key to the Math Department Printing Room", keyImage,
												new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								if (currentMap == maps.get(224) && npc == currentMap.getNPCs().get(0)) {
									int index = suki.searchInventory("Tissue Box");
									if (index < 0 && tissueCount == 0 && npc.getCurrentDialogueIndex() == 2) {
										tissueCount++;
										updateEquippedItemIndexBeforeChange();
										suki.addToInventory(new Collectible("Tissue Box",
												"A box of tissues Sophie & Claire delivered to Ms. Wong", tissueImage,
												new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								if (currentMap == maps.get(225) && npc == currentMap.getNPCs().get(0)) {
									int index = suki.searchInventory("Tissue Box");
									if (index >= 0 && chowCount == 0) {
										updateEquippedItemIndexBeforeChange();
										suki.removeFromInventory(index);
										chowCount++;
										suki.addToInventory(
												new Collectible("Staff Lounge Key", "Key to the Staff Lounge", keyImage,
														new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								if (currentMap == maps.get(21) && npc == currentMap.getNPCs().get(0)) {
									if (!avTalkedTo) {
										npc.setState(0);
										npc.setCurrentDialogueIndex(0);
										avTalkedTo = true;
									} else if (avTalkedTo && boxesMoved) {
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}

								if (currentMap == maps.get(24)) {
									if (!mopped && npc.getState() == 0 && npc.getCurrentDialogueIndex() == 2) {
										updateEquippedItemIndexBeforeChange();
										suki.addToInventory(new Weapon("Mop", "A Mop for Mopping.", 60, 100, mopImage,
												new Point(0, 0)));
										updateEquippedItemIndexAfterChange();
									} else if (mopped) {
										npc.setState(1);
										if (weightCount == 0) {
											weightCount++;
											updateEquippedItemIndexBeforeChange();
											suki.addToInventory(
													new Weapon("10lb Weight", "For gettin' those gainz!", 40,
															120, weightImage,
															new Point(0, 0)));
											updateEquippedItemIndexAfterChange();
										}
									}
								}
								// Get the current line of dialogue
								String currentDialogueLine = npc.getDialogues()[npc.getState()][npc
										.getCurrentDialogueIndex()];

								// Add the NPC's name and the current line of dialogue
								currentDialogue = npc.getName() + ": " + currentDialogueLine;

								// Check if there are more lines in the current dialogue set
								if (npc.getCurrentDialogueIndex() < npc.getDialogues()[npc.getState()].length - 1) {
									currentDialogue += "\n\nPress SPACE to interact."; // Prompt for next line
								}
								// Interact with NPC and get the current dialogue
								npc.interact();
							} else {
								// If all dialogues are finished, display "We've already spoken."
								currentDialogue = "We've already spoken.";
							}

							// Activate dialogue and set the timer
							dialogueActive = true;
							dialogueEndTime = System.currentTimeMillis() + 4000; // Reset 4-second timer

							break; // Interact with only one NPC at a time
						}
					}
				}
			}

			if (key == KeyEvent.VK_C) {
				for (Door door : currentMap.getDoors()) {
					if (door.interactable() && door.getMapDest() == 224 && currentMap == maps.get(34)) {
						screen = 7;
						Timer timer1 = new Timer(3000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								screen = 8;
								repaint();
								Timer timer2 = new Timer(2000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										screen = 5;
										repaint();
									}
								});
								timer2.setRepeats(false);
								timer2.start();
							}
						});
						timer1.setRepeats(false);
						timer1.start();
						repaint();
					} else if (door.interactable() && door.getMapDest() == 1 && currentMap == maps.get(2)) {
						screen = 9;
						Timer timer1 = new Timer(3000, new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								screen = 10;
								repaint();
								Timer timer2 = new Timer(2000, new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										screen = 5;
										repaint();
									}
								});
								timer2.setRepeats(false);
								timer2.start();
							}

						});
						timer1.setRepeats(false);
						timer1.start();
						repaint();
					}

					if (door.interactable()) {
						System.out.println(door.getMapDest());
						System.out.println(maps.get(door.getMapDest()));
						currentMap = maps.get(door.getMapDest());
						suki.setGameX(door.getExitPos().x);
						suki.setGameY(door.getExitPos().y);
						currentMap.setTLLocation(new Point(0, 0));
						up = down = left = right = false;
						repaint();
					}
				}

			}
			if (currentMap == maps.get(23) && key == KeyEvent.VK_F) {
				if (moppable) {
					showMopMessage = false;
					mopped = true;
				}
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
			// Diagonal movement
			int diagSpeed = (suki.getSpeed() / 2);
			if (up && left) {
				moveY = -diagSpeed;
				moveX = -diagSpeed;
			} else if (up && right) {
				moveY = -diagSpeed;
				moveX = diagSpeed;
			} else if (down && left) {
				moveY = diagSpeed;
				moveX = -diagSpeed;
			} else if (down && right) {
				moveY = diagSpeed;
				moveX = diagSpeed;
			} else {
				if (up) {
					moveY = -suki.getSpeed();
				} else if (down) {
					moveY = suki.getSpeed();
				} else if (left) {
					moveX = -suki.getSpeed();
				} else if (right) {
					moveX = suki.getSpeed();
				}
			}

			// Create a future hitbox for the player
			Rectangle futureHitbox = new Rectangle(suki.getHitboxM());
			futureHitbox.translate(moveX, moveY);

			// Check for collisions with rectangular walls
			boolean collision = false;
			// for (Wall wall : currentMap.getRectWalls()) {
			// if (futureHitbox.intersects(wall.getRect())) {
			// System.out.println(wall.getRect());
			// collision = true;
			// break;
			// }
			// }

			// Check for collisions with triangular walls
			// if (!collision) {
			// for (Triangle triangle : currentMap.getTriWalls()) {
			// if (triangle.intersects(futureHitbox) > 0) {
			// collision = true;
			// break;
			// }
			// }
			// }

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

			}
		}
	}

	// CAN DEFINITELY TURN THE DAMAGING INTO ITS OWN METHOD I JUST HAVENT GOTTEN TO
	// IT YET
	public void attack() {
		int index = suki.getEquippedItem();
		if (index < 0 || index >= suki.getInventory().size()) {
			System.out.println("one punch man");
			int damage = 10; // Default unarmed damage
			int reach = 30; // Default unarmed reach

			Iterator<Cow> cowIterator = currentMap.getCows().iterator();
			while (cowIterator.hasNext()) {
				Cow cow = cowIterator.next();

				double distance = suki.getGamePos().distance(cow.getGamePos());
				if (distance <= reach) {
					System.out.println("Hit cow at distance: " + distance);

					// Apply damage to the cow
					cow.hurt(damage);

					// Apply knockback
					knockback(cow, suki, 50); // smaller knockback for punch

					if (!cow.isAlive()) {
						System.out.println("Cow DIE!");
						currentMap.removeCow(cow); // Remove dead cow from the map
					}
				}
			}
			return;
		}

		Item equippedItem = suki.getInventory().get(suki.getEquippedItem());
		int damage = 0;
		int reach = 0;
		if (equippedItem instanceof Weapon) {
			Weapon weapon = (Weapon) equippedItem; // Cast to Weapon
			System.out.println("Attacking with weapon: " + weapon.getName());
			damage = weapon.getDamage();
			reach = weapon.getReach();
		} else if (equippedItem == null) {
			System.out.println("No item equipped! punching... ");
			damage = 10; // Default unarmed damage
			reach = 30;
		} else {
			System.out.println("equipped item is not a weapon! cannot attack.");
			return; // Abort attack
		}

		// Check for cows within range
		Iterator<Cow> cowIterator = currentMap.getCows().iterator();
		while (cowIterator.hasNext() && currentMap.getCows().size() > 0) {
			Cow cow = cowIterator.next();

			double distance = suki.getGamePos().distance(cow.getGamePos());
			if (distance <= reach) {
				System.out.println("hit cow at distance: " + distance);

				// Apply damage to the cow
				cow.hurt(damage);

				// Apply knockback
				knockback(cow, suki, 50); // Knockback distance is 50 units

				if (!cow.isAlive()) {
					System.out.println("Cow DIE!");
					cowIterator.remove(); // Remove dead cow from the map
				}
			}
		}
	}

	private void knockback(Cow cow, Player player, int knockbackDistance) {
		Point cowPos = cow.getGamePos();
		Point playerPos = player.getGamePos();

		// Calculate knockback direction
		double dx = cowPos.x - playerPos.x;
		double dy = cowPos.y - playerPos.y;
		double length = Math.sqrt(dx * dx + dy * dy);

		// Normalize the direction and apply knockback
		dx = (dx / length) * knockbackDistance;
		dy = (dy / length) * knockbackDistance;

		cow.setGamePos(cowPos.x + (int) dx, cowPos.y + (int) dy);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (screen == 5) {
			if (!showInventory) {
				attack();
			}
			if (showInventory) {
				Point mousePos = e.getPoint();
				int boxSize = 50; // Size of each inventory box
				int padding = 10; // Padding between boxes
				int startX = 60; // Starting X position for the grid
				int startY = 100; // Starting Y position for the grid
				int cols = 6; // Number of columns in the grid

				for (int i = 0; i < suki.getInventory().size(); i++) {
					int row = i / cols;
					int col = i % cols;
					int x = startX + col * (boxSize + padding);
					int y = startY + row * (boxSize + padding);

					Rectangle itemRect = new Rectangle(x, y, boxSize, boxSize);
					if (itemRect.contains(mousePos)) {
						suki.setEquippedItem(i);
						showInventory = false;
						System.out.println(i + " equipped.");
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point mousePoint = e.getPoint();

		// Update hover states for buttons on the current screen
		hoverPlay = recPlay.contains(mousePoint);
		hoverAbout = recAbout.contains(mousePoint);
		hoverNewGame = recNewGame.contains(mousePoint);
		hoverLB = recLB.contains(mousePoint);
		hoverBack = recBack.contains(mousePoint);

		repaint(); // Repaint to reflect changes
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

			} else if (recAbout.contains(selectedPoint)) {
				screen = 2;
				myPanel.repaint();

			}
		}

		// start menu
		// starts a new game, shows leaderboard, or returns to main menu

		else if (screen == 1) {
			if (recNewGame.contains(selectedPoint)) {
				try {
					resetVariables();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				screen = 5;

			}
			if (recLB.contains(selectedPoint)) {
				screen = 4;
				myPanel.repaint();

			} else if (recBack.contains(selectedPoint)) {
				screen = 0;
				myPanel.repaint();
			}
		}
		// about menu
		else if (screen == 2) {
			if (recBack.contains(selectedPoint)) {
				screen = 0;
			}

		}
		// settings menu
		else if (screen == 3) {
			if (recBack.contains(selectedPoint)) {
				screen = 5;
			}
			if (recQuitGame.contains(selectedPoint)) {
				screen = 0;
			}

		}
		// leaderboard
		else if (screen == 4) {
			if (recBack.contains(selectedPoint)) {
				screen = 1;
			}
		}
		// main game
		else if (screen == 5) {
			if (recOptions.contains(selectedPoint)) {
				screen = 3;
			}
		}
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

	public void updateEquippedItemIndexBeforeChange() {
		// Get the name of the currently equipped item before any inventory changes
		int equippedIndex = suki.getEquippedItem();
		Item equippedItem = (equippedIndex >= 0 && equippedIndex < suki.getInventory().size())
				? suki.getInventory().get(equippedIndex)
				: null;

		if (equippedItem != null) {
			suki.setEquippedItemName(equippedItem.getName()); // Store the item's name
		} else {
			suki.setEquippedItemName(null); // Clear the name if no item is equipped
		}
	}

	public void updateEquippedItemIndexAfterChange() {
		// Get the stored name of the previously equipped item
		String equippedItemName = suki.getEquippedItemName();

		if (equippedItemName != null) {
			// Try to find the item's new index in the inventory
			int newIndex = suki.searchInventory(equippedItemName);
			if (newIndex >= 0) {
				suki.setEquippedItem(newIndex); // Update the equipped index to the new position
				return;
			}
		}

		// If the item is no longer in the inventory, equip the first item or unequip
		if (!suki.getInventory().isEmpty()) {
			suki.setEquippedItem(0);
		} else {
			suki.setEquippedItem(-1); // No items equipped
		}
	}

	public void checkCollision(Door door) {
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		Rectangle doorRect = door.getDoorRect();

		if (playerRect.intersects(doorRect)) {
			// Check conditions for each door
			if (door.getMapDest() == 33) {
				int index = suki.searchInventory("Math Storage Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedMessage = false;
				} else {
					door.setInteractable(false);
					showLockedMessage = true;
				}
			} else if (door.getMapDest() == 224 && currentMap == maps.get(34)) {
				int equippedIndex = suki.getEquippedItem();
				if (equippedIndex < 0 || !suki.getInventory().get(equippedIndex).getName().equals("Screwdriver")) {
					door.setInteractable(false);
					showVentMessage = true;
				} else {
					door.setInteractable(true);
					showVentMessage = false;
				}
			} else if (door.getMapDest() == 1 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("10lb Weight");
				if (suki.getEquippedItem() == index) {
					door.setInteractable(true);
					showStairwellLockedMessage = false;
				} else {
					door.setInteractable(false);
					showStairwellLockedMessage = true;
				}
			} else if (door.getMapDest() == 20 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("Staff Lounge Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedLoungeMessage = false;
				} else {
					door.setInteractable(false);
					showLockedLoungeMessage = true;
				}
			} else if (door.getMapDest() == 21 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("Balcony Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedBalconyMessage = false;
				} else {
					door.setInteractable(false);
					showLockedBalconyMessage = true;
				}
			} else if (door.getMapDest() == 22 || door.getMapDest() == 23) {
				int index = suki.searchInventory("Mop");
				if (index >= 0) {
					door.setInteractable(true);
					showWaterMessage = false;
				} else {
					door.setInteractable(false);
					showWaterMessage = true;
				}
			} else if (door.getMapDest() == 1 && currentMap == maps.get(2)) {
				int equippedIndex = suki.getEquippedItem();
				if (equippedIndex < 0 || !suki.getInventory().get(equippedIndex).getName().equals("Weight")) {
					door.setInteractable(false);
					showVentMessage = true;
				} else {
					door.setInteractable(true);
					showVentMessage = false;
				}
			} else {
				// Default behavior for unlocked doors
				door.setInteractable(true);
				showLockedLoungeMessage = false;
				showBoxMessage = false;
				showStairwellLockedMessage = false;
			}
		} else {
			// Reset only the message for the current door if the player walks away
			if (door.getMapDest() == 33) {
				showLockedMessage = false;
			} else if (door.getMapDest() == 224 && currentMap == maps.get(34)) {
				showVentMessage = false;
			} else if (door.getMapDest() == 1 && currentMap == maps.get(2)) {
				showStairwellLockedMessage = false;
			} else if (door.getMapDest() == 20 && currentMap == maps.get(2)) {
				showLockedLoungeMessage = false;
			} else if (door.getMapDest() == 21 && currentMap == maps.get(2)) {
				showLockedBalconyMessage = false;
			} else if (door.getMapDest() == 22 || door.getMapDest() == 23) {
				showWaterMessage = false;
			}
			// Disable door interaction when not colliding
			door.setInteractable(false);
		}
	}

	public void checkPlayerCollisions(Player player) throws IOException {
		// Check collectibles
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		Iterator<Collectible> collectibleIterator = currentMap.getItems().iterator();
		while (collectibleIterator.hasNext()) {
			Collectible collectible = collectibleIterator.next();
			Rectangle itemRect = new Rectangle(collectible.getGamePos().x, collectible.getGamePos().y,
					collectible.getImage().getWidth(), collectible.getImage().getHeight());
			if (playerRect.intersects(itemRect)) {
				if (currentMap == maps.get(328)) {
					int index = suki.searchInventory("Broom");
					if (index >= 0) {
						beakerCleaned = true;
						currentMap.getNPCs().get(0).setState(1);
						currentMap.getNPCs().get(0).setCurrentDialogueIndex(0);
						collectible.setImage(ImageIO.read(getClass().getResource("/sprites/beaker.png")));
						Collectible beaker = collectible;
						System.out.println("Picked up collectible: " + collectible.getName());
						updateEquippedItemIndexBeforeChange();
						player.addToInventory(beaker);
						updateEquippedItemIndexAfterChange();
						collectibleIterator.remove();
						beakerWalkCount++;
					} else {
						showBeakerMessage = true;
					}
				} else if (currentMap == maps.get(310)) {
					int index = suki.searchInventory("Sharp Pencil");
					if (index >= 0) {
						JTextArea textArea = new JTextArea(collectible.getDescription());
						textArea.setEditable(true);
						JScrollPane scrollPane = new JScrollPane(textArea);
						JOptionPane.showMessageDialog(null, scrollPane, "My Synthesis Essay",
								JOptionPane.INFORMATION_MESSAGE);
						String input = textArea.getText();
						if (input != null && !input.trim().isEmpty()) {
							maps.get(3).getNPCs().get(1).setState(1);
							maps.get(3).getNPCs().get(1).setCurrentDialogueIndex(0);
							left = right = up = down = false;
							Collectible essay = collectible;
							collectible.setDescription(input);
							collectible.setImage(ImageIO.read(getClass().getResource("/sprites/writtenEssay.png")));
							essayWritten = true;
							essayWalkCount++;
							collectibleIterator.remove();
							updateEquippedItemIndexBeforeChange();
							player.addToInventory(essay);
							updateEquippedItemIndexAfterChange();
							System.out.println("Essay written.");
						} else {
							showEssayMessage = true;
						}
					} else {
						if (currentMap == maps.get(328)) {
							showBeakerMessage = true;
						} else if (currentMap == maps.get(310)) {
							showEssayMessage = true;
						}
					}
				} else if (currentMap == maps.get(341)) {
					chipsStolen = true;
					repaint();
					Timer timer = new Timer(4000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							chipsStolen = false;
							repaint();
						}
					});
					timer.setRepeats(false);
					timer.start();
					repaint();
					collectibleIterator.remove(); // Remove from map
					updateEquippedItemIndexBeforeChange();
					player.addToInventory(collectible); // Add to player's inventory
					updateEquippedItemIndexAfterChange();
					System.out.println("Picked up collectible: " + collectible.getName());
				} else {
					collectibleIterator.remove(); // Remove from map
					updateEquippedItemIndexBeforeChange();
					player.addToInventory(collectible); // Add to player's inventory
					updateEquippedItemIndexAfterChange();
					System.out.println("Picked up collectible: " + collectible.getName());
				}
			} else {
				showBeakerMessage = false; // Disable the message when not near the beaker
				showEssayMessage = false; // disable essay message when not near the paper
			}
		}

		// Check weapons
		Iterator<Weapon> weaponIterator = currentMap.getWeapons().iterator();
		while (weaponIterator.hasNext()) {
			Weapon weapon = weaponIterator.next();
			Rectangle itemRect = new Rectangle(weapon.getGamePos().x, weapon.getGamePos().y,
					weapon.getImage().getWidth(), weapon.getImage().getHeight());
			if (playerRect.intersects(itemRect)) {
				weaponIterator.remove(); // Remove from map
				updateEquippedItemIndexBeforeChange();
				player.addToInventory(weapon); // Add to player's inventory
				updateEquippedItemIndexAfterChange();
				System.out.println("Picked up weapon: " + weapon.getName());
			}
		}
	}

	public void checkCollision(Wall wall) {
		// game screen
		if (screen == 5) {

			Rectangle wallRect = new Rectangle(wall.getRect().x, wall.getRect().y, wall.getRect().width,
					wall.getRect().height);
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
		final double EPSILON = 1e-4; // small buffer to push the player out of the triangle more
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
		final int BUFFER = 1; // Small buffer to prevent re-collision
		for (Cow cow2 : currentMap.getCows()) {
			if (cow != cow2 && cow.getHitbox().intersects(cow2.getHitbox())) {
				Rectangle intersection = cow.getHitbox().intersection(cow2.getHitbox());
				int dx = intersection.width / 2 + BUFFER;
				int dy = intersection.height / 2 + BUFFER;

				// Resolve collision along x-axis
				if (intersection.width > intersection.height) {
					if (cow.getX() < cow2.getX()) {
						cow.setGamePos(cow.getGamePos().x - dx, cow.getGamePos().y);
						cow2.setGamePos(cow2.getGamePos().x + dx, cow2.getGamePos().y);
					} else {
						cow.setGamePos(cow.getGamePos().x + dx, cow.getGamePos().y);
						cow2.setGamePos(cow2.getGamePos().x - dx, cow2.getGamePos().y);
					}
				}

				// Resolve collision along y-axis
				if (intersection.height > intersection.width) {
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

		Rectangle futureHitbox = new Rectangle(cow.getHitbox());
		futureHitbox.translate(cow.getSpeed(), cow.getSpeed());

		for (Wall wall : currentMap.getRectWalls()) {
			Rectangle rectWall = wall.getRect();
			if (futureHitbox.intersects(rectWall)) {
				cow.setCollision(true);
				return;
			}
		}
		cow.setCollision(false);
	}

	public void moveCows() {
		for (Cow cow : currentMap.getCows()) {
			checkCollision(cow);
			if (!cow.isColliding()) {
				cow.move(cow.getSpeed(), cow.getSpeed(), suki);
			}
		}
	}

	public void showGameOverPanel() {
		// Create the Game Over panel
		JPanel gameOverPanel = new JPanel();
		gameOverPanel.setLayout(new BorderLayout());
		gameOverPanel.setBackground(Color.BLACK);

		// Add "Game Over" label
		JLabel gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
		gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
		gameOverLabel.setForeground(Color.RED);
		gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

		// Add "Return to Home Screen" button
		JButton returnButton = new JButton("Return to Home Screen");
		returnButton.setFont(new Font("Arial", Font.PLAIN, 18));
		returnButton.addActionListener(e -> {
			screen = 0; // Set the screen to the home screen
			frame.getContentPane().remove(gameOverPanel); // Remove the Game Over panel
			frame.getContentPane().add(this); // Add the main game panel
			frame.revalidate();
			frame.repaint();
		});
		gameOverPanel.add(returnButton, BorderLayout.SOUTH);

		// Display the Game Over panel
		frame.getContentPane().remove(this); // Remove the game panel
		frame.getContentPane().add(gameOverPanel); // Add the Game Over panel
		frame.revalidate();
		frame.repaint();
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

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'mouseDragged'");
	}
}
