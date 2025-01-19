// ICS4U1 Culminating Assignment
// ChaseCow: Markville Cow Invasion
// Claire Hung & Sophie Jiang
// January 2025
// Description: See readme.txt! 

// imports
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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

@SuppressWarnings("serial") // suppressing random warning
public class ChaseCow extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	// big boss variables
	int screen = 0;
	static JPanel myPanel;
	static JFrame frame;

	// self explanatory variables
	int FPS = 60;
	Thread thread;
	int screenWidth = 1080;
	int screenHeight = 720;

	// movement variables
	static boolean up = false;
	static boolean down = false;
	static boolean left = false;
	static boolean right = false;

	// main screen + start screen buttons
	Rectangle recAbout, recPlay, recOptions;
	boolean hoverAbout, hoverOptions, hoverPlay;
	BufferedImage titleScreenBG, title, play, play2, about, about2, aboutInfo, settings, settings2, options, options2, back, back2;
	Rectangle recLB, recNewGame, recBack;
	boolean hoverNewGame, hoverLB, hoverBack;
	BufferedImage menuScreenBackground, gameTitle, newGame, leaderboard, newGame2, leaderboard2;

	// settings
	Rectangle recVolume;
	Rectangle recQuitGame;
	BufferedImage volumeImg, quitGame;
	int volume = 3;

	// games global variables
	boolean showInventory = false;
	boolean interactable = false;
	ArrayList<BufferedImage> cowImages = new ArrayList<BufferedImage>();
	BufferedImage playerImage, cowImage;
	Player suki;
	FloorMap currentMap;
	HashMap<Integer, FloorMap> maps = new HashMap<>();
	BufferedImage[] innerWallImages;
	boolean healing = false;
	boolean fullHP = true;
	String[] cowNames = {
			"baseCow",
			"spaceCow",
			"wasteCow"
	};
	int currentCowType;

	// CUTSCENE IMAGES
	BufferedImage floor3SceneImg;
	BufferedImage floor2SceneImg;
	BufferedImage floor1SceneImg;
	BufferedImage ventsSceneImg;
	BufferedImage stairsSceneImg;

	// HEALTH BARS
	BufferedImage healthBar;
	BufferedImage bossHealthBar;

	// NPC DIALOGUE
	// dialogue variables
	String currentDialogue; // current line of dialogue to display
	long dialogueEndTime; // timestamp when the dialogue box should disappear
	boolean dialogueActive; // track if dialogue is happening

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
	int mopCount;
	BufferedImage floodImage1;
	BufferedImage floodImage2;

	// STAGE 6
	HashSet<Projectile> projectiles = new HashSet<>();
	final int FIRE_RATE = 3000;
	long lastProjectileTime;
	boolean bossDefeated;
	BufferedImage IDcardImage;
	int mooCount;

	// MUSIC AND SOUND

	static Clip music, sound;
	static AudioInputStream audioInputStream, audioInputClick, audioInputCow, audioInputDoor, audioInputItem,
			audioInputSwing, audioInputTalking, audioInputVents, audioInputWin, audioInputLose;
	static boolean clickSound, cowSound, doorSound, itemSound, swingSound, talkSound, ventSound, winSound,
			loseSound;

	// leaderboard
	static int[] arrTopTime = { 10000000, 10000000, 10000000, 10000000, 10000000, 10000000 };
	static String[] arrTopName = { "", "", "", "", "", "" };

	static int seconds = 0;
	static int minutes = 0;
	Timer gameTimer;

	// constructor
	public ChaseCow() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		// initialize variables
		importImages();
		initialize();
		setPreferredSize(new Dimension(1080, 720));

		// set up JPanel
		frame.getContentPane().add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();

		// checks for user input in mouse and keyboard
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		musicPlayer();
		// makes sure program can run
		thread = new Thread(this);
		thread.start();
	}

	public void musicPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		if (screen != 5) {
			audioInputStream = AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream("/sound/menumusic.wav"));
		} else {
			audioInputStream = AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream("/sound/gamemusic.wav"));
		}
		music = AudioSystem.getClip();
		music.open(audioInputStream);
		music.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void soundPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioInputItem = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/plop.wav"));
		audioInputClick = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/click.wav"));
		audioInputCow = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/cowhurt.wav"));
		audioInputDoor = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/door.wav"));
		audioInputLose = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/gameover.wav"));
		audioInputSwing = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/swing.wav"));
		audioInputTalking = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/talking.wav"));
		audioInputVents = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/vents.wav"));
		audioInputWin = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sound/winsound.wav"));

		sound = AudioSystem.getClip();
		if (doorSound) {
			doorSound = false;
			sound.open(audioInputDoor);
			sound.start();
		} else if (itemSound) {
			itemSound = false;
			sound.open(audioInputItem);
			sound.start();
		} else if (clickSound) {
			clickSound = false;
			sound.open(audioInputClick);
			sound.start();
		} else if (winSound) {
			winSound = false;
			sound.open(audioInputWin);
			sound.start();
		} else if (cowSound) {
			cowSound = false;
			sound.open(audioInputCow);
			sound.start();
		} else if (loseSound) {
			loseSound = false;
			sound.open(audioInputLose);
			sound.start();
		} else if (swingSound) {
			swingSound = false;
			sound.open(audioInputSwing);
			sound.start();
		} else if (talkSound) {
			talkSound = false;
			sound.close();
			sound.open(audioInputTalking);
			sound.start();
			// play audio for a random amount of time between 0.5 to 2 seconds
			int randomDuration = 800 + new Random().nextInt(2000);
			Timer timer = new Timer(randomDuration, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (sound.isRunning()) {
						sound.stop();
					}
				}
			});
			timer.setRepeats(false);
			timer.start();
		} else if (ventSound) {
			ventSound = false;
			sound.open(audioInputVents);
			sound.start();
		}
	}

	// Description: always running
	// Parameters: n/a
	// Return: void
	@Override
	public void run() {
		// main game loop
		while (true) {
			// update ingame variables
			try {
				update();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// update visuals
			this.repaint();
			// frames
			try {
				Thread.sleep(1000 / FPS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Description: initialize variables
	// Parameters: n/a
	// Return: void
	public void initialize() throws IOException {
		currentCowType = 0;
		// NPC DIALOGUE
		// Dialogue variables
		currentDialogue = null; // current line of dialogue to display
		dialogueEndTime = 0; // timestamp when the dialogue box should disappear
		dialogueActive = false; // flag to track if dialogue is currently happening
		// STAGE 1
		beakerCleaned = false;
		beakerWalkCount = 0;
		showBeakerMessage = false;
		spillImage = null;
		spillCleaned = false;
		spillWalkCount = 0;
		showSpillMessage = false;

		// STAGE 2
		essayWritten = false;
		essayWalkCount = 0;
		showEssayMessage = false;
		essaySubmitted = false;

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
		mopCount = 0;

		// STAGE 6
		lastProjectileTime = 0;
		mooCount = 0;

		// read in non-map images, initialize variables
		try {
			titleScreenBG = ImageIO.read(getClass().getResource("/menu/titleBG.png"));
			int numCows = cowNames.length;
			for (int i = 0; i < numCows; i++) {
				String cowPath = "/sprites/" + cowNames[i] + ".png";
				cowImage = ImageIO.read(getClass().getResource(cowPath));
				cowImages.add(cowImage);
			}

			// screen 0, 1, 2, 3, 4 (menu)
			title = ImageIO.read(getClass().getResource("/menu/title.png"));
			play = ImageIO.read(getClass().getResource("/menu/playbutton.png"));
			about = ImageIO.read(getClass().getResource("/menu/aboutbutton.png"));
			play2 = ImageIO.read(getClass().getResource("/menu/playbutton2.png"));
			about2 = ImageIO.read(getClass().getResource("/menu/aboutbutton2.png"));
			aboutInfo = ImageIO.read(getClass().getResource("/menu/aboutScreen.png"));
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

			// rectangles to detect if button is pressed
			recPlay = new Rectangle(340, 400, play.getWidth(), play.getHeight());
			recAbout = new Rectangle(340, 520, about.getWidth(), about.getHeight());
			recOptions = new Rectangle(1010, 20, 50, 50);
			recBack = new Rectangle(20, 20, back.getWidth(), back.getHeight());
			recNewGame = new Rectangle(340, 400, newGame.getWidth(), newGame.getHeight());
			recLB = new Rectangle(340, 520, leaderboard.getWidth(), leaderboard.getHeight());
			recQuitGame = new Rectangle(340, 520, quitGame.getWidth(), quitGame.getHeight());
			recVolume = new Rectangle(340, 400, volumeImg.getWidth(), volumeImg.getHeight());

			// player sprites
			BufferedImage playerImageDown = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			BufferedImage playerImageUp = ImageIO.read(getClass().getResource("/sprites/sukiUp.png"));
			BufferedImage playerImageRight = ImageIO.read(getClass().getResource("/sprites/sukiRight.png"));
			BufferedImage playerImageLeft = ImageIO.read(getClass().getResource("/sprites/sukiLeft.png"));

			// cutscenes for when a level is beat
			floor3SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR3SCENE.png"));
			floor2SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR2SCENE.png"));
			floor1SceneImg = ImageIO.read(getClass().getResource("/menu/FLOOR1SCENE.png"));
			ventsSceneImg = ImageIO.read(getClass().getResource("/menu/VENTSCENE.png"));
			stairsSceneImg = ImageIO.read(getClass().getResource("/menu/STAIRSCENE.png"));

			// health bar images
			healthBar = ImageIO.read(getClass().getResource("/menu/HPBAR.png"));
			bossHealthBar = ImageIO.read(getClass().getResource("/menu/WASTECOWHP.png"));

			// images for specific stages
			spillImage = ImageIO.read(getClass().getResource("/map files/spill.png"));
			keyImage = ImageIO.read(getClass().getResource("/sprites/key.png"));
			tissueImage = ImageIO.read(getClass().getResource("/sprites/tissueBox.png"));
			boxPileImage = ImageIO.read(getClass().getResource("/map files/boxPile.png"));
			mopImage = ImageIO.read(getClass().getResource("/sprites/mop.png"));
			weightImage = ImageIO.read(getClass().getResource("/sprites/weight.png"));
			floodImage1 = ImageIO.read(getClass().getResource("/map files/washroom flood1.png"));
			floodImage2 = ImageIO.read(getClass().getResource("/map files/washroom flood2.png"));
			floodImage = ImageIO.read(getClass().getResource("/map files/flood.png"));
			IDcardImage = ImageIO.read(getClass().getResource("/sprites/IDCard.png"));

			// initialize player
			suki = new Player(100, 4, new Rectangle(517, 382, 46, 10), new Rectangle(517, 328, 46, 64), 250, 300,
					playerImageDown, playerImageUp, playerImageRight, playerImageLeft);
			// set playerImage to facing down
			playerImage = ImageIO.read(getClass().getResource("/sprites/sukiDown.png"));
			// load first cow as basecow
			cowImage = ImageIO.read(getClass().getResource("/sprites/baseCow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Description: resets all variables for when a new game starts
	// Parameters: n/a
	// Return: void
	public void resetVariables() throws IOException {
		seconds = 0;
		minutes = 0;
		initialize();
		initializeMaps();
	}

	// Description: updates the game at every frame
	// Parameters: n/a
	// Return: void
	public void update() throws IOException {

		// on game screen:
		if (screen == 5) {
			// if suki dies, show game over screen
			if (suki.getHP() <= 0) {
				showGameOverPanel();
			}
			// allow player to move
			move();

			// makes sure map isnt null, and updates cows to follow suki
			if (currentMap != null) {
				currentMap.updateCows(suki);
			} else {
				System.out.println("No current map is set.");
			}

			// check collision for all outer walls
			Iterator<Wall> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {
				checkCollision(it.next());
			}

			// check collision for all inner walls
			Iterator<Wall> iter = currentMap.getInnerWalls().iterator();
			while (iter.hasNext()) {
				checkCollision(iter.next());
			}

			// check collision for all triangular walls
			Iterator<Triangle> itr = currentMap.getTriWalls().iterator();
			while (itr.hasNext()) {
				checkCollision(itr.next());
			}

			// check collision for all cows
			if (!currentMap.getCows().isEmpty()) {
				Iterator<Cow> cowItr = currentMap.getCows().iterator();
				while (cowItr.hasNext()) {
					Cow cow = cowItr.next();
					checkCollision(cow);
				}
			}

			// check if player walks through any items
			checkPlayerCollisions(suki);

			// check if player is within range of an npc
			if (currentMap.getNPCs() != null) {
				for (NPC npc : currentMap.getNPCs()) {
					double distance = getNPCDistance(suki, npc);
					npc.setWithinRange(distance < 75);
				}

			}

			// check if player is within range of a door
			for (Door door : currentMap.getDoors()) {
				checkCollision(door);
			}

			// FLOOR 3
			if (currentMap == maps.get(3)) {
				// MS WONG CHEAT: HEALING AT WATER FOUNTAINS
				Rectangle waterFountain = new Rectangle(6500, 950, 100, 25);
				Rectangle waterFountain2 = new Rectangle(1075, 3450, 50, 25);
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (waterFountain.intersects(playerRect) || waterFountain2.intersects(playerRect)) {
					healing = true;
				} else {
					healing = false;
				}

				// STAGE 1
				Rectangle spillRect = new Rectangle(4400, 1850, 100, 300);
				// if at goop spill:
				if (spillRect.intersects(playerRect) && !spillCleaned) {
					// find player coords
					double left1 = playerRect.getX();
					double right1 = playerRect.getX() + playerRect.getWidth();
					double right2 = spillRect.getX() + spillRect.getWidth();
					if (left1 < right2 && right1 > right2) {
						// collision from right
						suki.setGameX(suki.getGamePos().x + suki.getSpeed());
					}
					Collectible spill = new Collectible("Goop", "goopy goop", spillImage, new Point(4400, 1850), 0);
					// ensure that necessary items are in inventory
					int index1 = suki.searchInventory("Beaker");
					int index2 = suki.searchInventory("Broom");
					if (index1 >= 0 && index2 >= 0) {
						// get beaker with goop!
						spillCleaned = true;
						spill.setImage(ImageIO.read(getClass().getResource("/sprites/beakerPurple.png")));
						spill.setDescription("Beaker of mysterious purple liquid.");
						spill.setName("Beaker with Goop");
						updateEquippedItemIndexBeforeChange();
						suki.getInventory().remove(suki.searchInventory("Beaker"));
						updateEquippedItemIndexAfterChange();
						suki.getInventory().add(spill);
						System.out.println("Spill cleaned.");
						// update dialogue for av guy
						currentMap.getNPCs().get(0).setState(1);
						currentMap.getNPCs().get(0).setCurrentDialogueIndex(0);
						// ensure it can only occur once
						spillWalkCount++;
					} else {
						showSpillMessage = true;
					}
				} else {
					// only show spill message if suki is in range
					showSpillMessage = false;
				}

				// STAGE 2
				Rectangle msKimThreshold = new Rectangle(3200, 2625, 100, 200);
				if (msKimThreshold.intersects(playerRect) && !essaySubmitted) {
					// stop the player from moving at wall if essay NOT submitted
					double top1 = playerRect.getY();
					double bottom1 = playerRect.getY() + playerRect.getHeight();
					double top2 = msKimThreshold.y;
					if (bottom1 > top2 && top1 < top2) {
						// player collides from top of the wall
						suki.setGameY((int) (suki.getGamePos().y - suki.getSpeed()));
					}
				}
			}

			// FLOOR 2
			if (currentMap == maps.get(2)) {
				// MS WONG CHEAT: WATERFOUNTAIN HEALING!
				Rectangle waterFountain = new Rectangle(475, 3400, 50, 25);
				Rectangle waterFountain2 = new Rectangle(6850, 950, 50, 25);
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (waterFountain.intersects(playerRect) || waterFountain2.intersects(playerRect)) {
					healing = true;
				} else {
					healing = false;
				}

				// STAGE 4
				// CAN'T WALK PAST BOXES UNLESS STAGE COMPLETE.
				Rectangle boxesRect = new Rectangle(4600, 1800, 200, 500);
				if (boxesRect.intersects(playerRect) && !boxesMoved) {
					double left1 = playerRect.getX();
					double right1 = playerRect.getX() + playerRect.getWidth();
					double right2 = boxesRect.getX() + boxesRect.getWidth();
					if (left1 < right2 && right1 > right2) {
						// collision from right
						suki.setGameX(suki.getGamePos().x + suki.getSpeed());
					}
					if (avTalkedTo) {
						// move boxes if spoken to AV Guy.
						boxesMoved = true;
						System.out.println("Boxes moved.");
					} else {
						showBoxMessage = true;
					}
				} else {
					// only show box message if suki is in range.
					showBoxMessage = false;
				}

				// STAGE 5
				// if player touches water, show message about water
				Rectangle floodRect = new Rectangle(1100, 3450, 150, 50);
				if (floodRect.intersects(playerRect) && !mopped) {
					showWaterMessage = true;
				} else {
					showWaterMessage = false;
				}
			}

			// FLOOD IN WASHROOMS LOGIC (LEFT)
			Rectangle floodRect1 = new Rectangle(50, 425, 100, 150);
			if (currentMap == maps.get(23)) {
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (floodRect1.intersects(playerRect) && !mopped) {
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

			// FLOOD IN WASHROOMS LOGIC (RIGHT)
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

			// FLOOR 1
			if (currentMap == maps.get(1)) {
				// MS WONG CHEAT: WATERFOUNTAIN HEALING!
				Rectangle waterFountain = new Rectangle(2600, 900, 100, 25);
				Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
						(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
				if (waterFountain.intersects(playerRect)) {
					healing = true;
				} else {
					healing = false;
				}
			}
			if (currentMap == maps.get(11)) {
				if (!bossDefeated) {
					for (Cow cow : currentMap.getCows()) {
						spawnProjectiles(cow);
						updateProjectiles();
						checkProjectileCollisions();
					}
					repaint();
				}
			}

			// healing timer so it is not instant healing. player must wait to heal.
			long lastHealTime = 0;
			int healCooldown = 800; // 0.5 second cooldown for healing
			if (healing && !fullHP) {
				System.out.println("HEALING");
				if (System.currentTimeMillis() - lastHealTime >= healCooldown) {
					suki.setHP(suki.getHP() + 1);
					lastHealTime = System.currentTimeMillis();
				} else {
					fullHP = true;
				}
			}

			// if suki is at full hp, stop healing.
			if (suki.getHP() >= suki.getMaxHP()) {
				fullHP = true;
			} else {
				fullHP = false;
			}
		}
	}

	// Description: initialize all maps from SUPER LONG text file
	// Paramters: n/a
	// Return: void
	public void initializeMaps() throws IOException {
		// create and add floormap objects to the maps list
		BufferedReader b = new BufferedReader(
				new InputStreamReader(getClass().getResourceAsStream("/map files/mapInfo.txt")));
		try {

			int numMaps = Integer.parseInt(b.readLine().trim());
			// loop for each map
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
				// load all exterior walls
				int numRectWalls = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numRectWalls; j++) {
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					rectWalls.add(
							new Wall(null, null, new Rectangle(tlPoint.x, tlPoint.y, Integer.parseInt(st.nextToken()),
									Integer.parseInt(st.nextToken()))));
					System.out.println("loaded walls");
				}
				// load all triangular walls
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
				// load all interior walls ie. chairs tables etc.
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
				// load all weapons
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
				// load all collectibles
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
				// load all npcs
				int numNPCs = Integer.parseInt(b.readLine().trim());
				for (int j = 0; j < numNPCs; j++) {
					String name = b.readLine().trim();
					String imgPath = b.readLine().trim();
					BufferedImage img = ImageIO.read(getClass().getResource(imgPath));
					StringTokenizer st = new StringTokenizer(b.readLine().trim());
					Point tlPoint = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					int numStates = Integer.parseInt(b.readLine().trim());
					String[][] dialogue = new String[numStates][];
					// load npc dialogue
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
				// load all door rectangles
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
				// load all cows
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
					if (cowType == 2) {
						cows.add(new WasteCow(tlPoint.x, tlPoint.y, cowImages.get(cowType), currentMap, suki));
					}
					System.out.println("loaded cows");
				}
				System.out.println("Loaded map " + mapID);
				// add map to map list
				maps.put(mapID, new FloorMap(new Point(0, 0), rectWalls, triWalls, innerWalls, bg, doors, cows, npcs,
						weapons, collectibles));
				// set first map to spawn point
				currentMap = maps.get(30);
			}
		} catch (NumberFormatException e) {
			// incase there is a reading error
			System.out.println("Error reading map info file");
		}
		b.close();
	}

	// Description: imports all interior wall images
	// Paramters: void
	// Return: n/a
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
			// incase an error occurs...
			System.out.println("Error reading wall files");
		}
	}

	// Description: find distance from npc to player
	// Parameters: npc and suki (finding distance between them)
	// Return: distance between the two
	public double getNPCDistance(Player suki, NPC npc) {
		// Get NPC center coordinates
		int npcCenterX = npc.getGamePos().x + (npc.getImage().getWidth() / 2);
		int npcCenterY = npc.getGamePos().y + (npc.getImage().getHeight() / 2);

		// Get Player center coordinates
		int playerCenterX = suki.getGamePos().x + (suki.getHitboxM().width / 2);
		int playerCenterY = suki.getGamePos().y + (suki.getHitboxM().height / 2);

		return Math.sqrt(Math.pow(playerCenterX - npcCenterX, 2) + Math.pow(playerCenterY - npcCenterY, 2));
	}

	// Description: paintComponent... paints everything ???
	// Parameters: Graphics... this thing lets it paint stuff
	// Return: void
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// extra stuff for different styles
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;

		// all buttons have 2 options. will change depending on if you hover over the
		// button or not
		// main screen
		if (screen == 0) {
			// g.drawImage(titleScreenBG, 0, 0, titleScreenBG.getWidth(),
			// titleScreenBG.getHeight(), this);
			g.drawImage(title, screenWidth / 2 - title.getWidth() / 2, 150, title.getWidth(), title.getHeight(), this);
			g.drawImage(hoverPlay ? play2 : play, 340, 400, play.getWidth(), play.getHeight(), this);
			g.drawImage(hoverAbout ? about2 : about, 340, 520, about.getWidth(), about.getHeight(), this);
		}

		// start game screen
		if (screen == 1) {
			g.drawImage(hoverNewGame ? newGame2 : newGame, 340, 400, newGame.getWidth(), newGame.getHeight(),
					this);
			g.drawImage(hoverLB ? leaderboard2 : leaderboard, 340, 520, leaderboard.getWidth(),
					leaderboard.getHeight(), this);
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// about screen
		if (screen == 2) {
			g.drawImage(aboutInfo, screenWidth / 2 - aboutInfo.getWidth() / 2, screenHeight / 2 - aboutInfo.getHeight() / 2, 
				aboutInfo.getWidth(), aboutInfo.getHeight(), this);
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// settings
		if (screen == 3) {
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
			g.drawImage(volumeImg, 340, 400, volumeImg.getWidth(), volumeImg.getHeight(), this);
			g.drawImage(quitGame, 340, 520, quitGame.getWidth(), quitGame.getHeight(), this);
		}

		// leaderboard
		if (screen == 4) {
			g.drawImage(titleScreenBG, 0, 0, null);

			int[] yCoord = { 220, 285, 345, 410, 475 };
			for (int i = 0; i < 5; i++) {
				// if time is not saved, do not display
				if (arrTopTime[i] != 10000000) {
					int seconds = arrTopTime[i] % 60;
					int minutes = arrTopTime[i] / 60;
					g.setFont(new Font("Monospaced", Font.PLAIN, 40));
					g.drawString(String.format("%d. %-24s%dm %ds", i+1, arrTopName[i], minutes, seconds), 140, yCoord[i]);
				}

			}
			g.drawImage(hoverBack ? back2 : back, 20, 20, back.getWidth(), back.getHeight(), this);
		}

		// game screen
		if (screen == 5) {
			// get player game coordinates
			int playerX = suki.getGamePos().x + (int) suki.getHitboxC().getWidth() / 2;
			int playerY = suki.getGamePos().y - (int) suki.getHitboxC().getHeight() / 2;
			g2.setColor(Color.GREEN);

			// change styles
			g2.setFont(new Font("Dialog", Font.PLAIN, 16));
			g.setFont(new Font("Dialog", Font.PLAIN, 16));

			// paint map if it isnt null
			if (currentMap.getBG() != null) {
				// this was so confusing

				// visible area dimensions
				int visibleWidth = screenWidth;
				int visibleHeight = screenHeight;

				// calculate the source rectangle based on player position
				int srcX = playerX - visibleWidth / 2;
				int srcY = playerY - visibleHeight / 2;
				int srcX2 = srcX + visibleWidth;
				int srcY2 = srcY + visibleHeight;

				// adjust the rectangle dimensions
				int destWidth = srcX2 - srcX;
				int destHeight = srcY2 - srcY;

				// background
				setBackground(Color.BLACK);
				g.drawImage(currentMap.getBG(),
						0, 0, destWidth, destHeight, // destination rectangle
						srcX, srcY, srcX2, srcY2, // source rectangle
						this);

			} else {
				// incase background is null
				System.out.println("Background image is null");
			}

			// print all NPCs on the current map
			if (currentMap.getNPCs() != null) {
				Iterator<NPC> npcIt = currentMap.getNPCs().iterator();
				while (npcIt.hasNext()) {
					NPC npc = npcIt.next();
					// translate NPCs in-game position to screen coordinates
					int npcScreenX = (npc.getGamePos().x - playerX) + ((screenWidth / 2));
					int npcScreenY = (npc.getGamePos().y - playerY) + ((screenHeight / 2));

					// draw NPC
					if (npc.getImage() != null) {
						g.drawImage(npc.getImage(), npcScreenX, npcScreenY, npc.getImage().getWidth(),
								npc.getImage().getHeight(),
								this);
					} else {
						g.setColor(Color.RED);
						g.fillRect(npcScreenX, npcScreenY, 50, 50); // placeholder if npc image is null for some reason
					}
				}
			}
			// change font again
			g.setFont(new Font("Dialog", Font.PLAIN, 16));

			// STAGE 4 (BUSINESS WASHROOM)
			// if not mopped...
			if (!mopped) {
				// prompt user to hold mop
				if (showMopMessage && (currentMap == maps.get(22) || currentMap == maps.get(23))) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Looks like I need the mop", 60, 580);

				}
				// prompt user to mop
				if (currentMap == maps.get(23)) {
					// find flood screen coords based on game coords
					int floodScreenX = (50 - suki.getGamePos().x) + (screenWidth / 2);
					int floodScreenY = (200 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(floodImage2, floodScreenX, floodScreenY, floodImage2.getWidth(),
							floodImage2.getHeight(),
							this);
					if (moppable && showMopMessage) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press F to mop", 60, 580);
					}
				}
				// prompt user to mop
				if (currentMap == maps.get(22)) {
					// find flood in screen coords based on gamecoords
					int floodScreenX = (50 - suki.getGamePos().x) + (screenWidth / 2);
					int floodScreenY = (200 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(floodImage1, floodScreenX, floodScreenY, floodImage1.getWidth(),
							floodImage1.getHeight(),
							this);
					if (moppable && showMopMessage) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press F to mop", 60, 580);
					}

				}
				// if suki steps in water, display that suki hates water (CAUSE SHES A CAT)
				if (showWaterMessage && currentMap == maps.get(2)) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Ew! Ew! Ew! I hate water.", 60, 580);
				}

				if (currentMap == maps.get(2)) {
					int floodScreenX = (1100 - suki.getGamePos().x) + (screenWidth / 2);
					int floodScreenY = (3450 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(floodImage, floodScreenX, floodScreenY, floodImage.getWidth(), floodImage.getHeight(),
							this);
				}
			}

			// layer player and inner walls based on location
			drawInnerWalls(g2, playerX, playerY);

			// FOR TESTING PURPOSES (COMMENT OUT INCASE MS Wong WANTs TO SEE THE TRIANGLES)
			for (Triangle tri : currentMap.getTriWalls()) {
				Point[] vertices = tri.getVertices();
				int[] xPoints = { (vertices[0].x - playerX) + (screenWidth / 2),
						(vertices[1].x - playerX) + (screenWidth / 2), (vertices[2].x - playerX) + (screenWidth / 2) };
				int[] yPoints = { (vertices[0].y - playerY) + (screenHeight / 2),
						(vertices[1].y - playerY) + (screenHeight / 2),
						(vertices[2].y - playerY) + (screenHeight / 2) };
				g2.fillPolygon(xPoints, yPoints, 3);
			}

			// print all cows
			if (!currentMap.getCows().isEmpty()) {
				for (Cow cow : currentMap.getCows()) {
					// find the cow's in-game position to screen coordinates
					int cowScreenX = (cow.getGamePos().x - playerX) + ((screenWidth / 2));
					int cowScreenY = (cow.getGamePos().y - playerY) + ((screenHeight / 2));

					// draw the cow image or placeholder
					BufferedImage cowImage = cow.getImage();
					if (cowImage != null) {
						g.drawImage(cowImage,
								cowScreenX, cowScreenY,
								cow.getHitbox().width, cow.getHitbox().height,
								this);
					} else {
						// placeholder if it breaks
						g.setColor(Color.RED);
						g.fillRect(cowScreenX, cowScreenY, cow.getHitbox().width, cow.getHitbox().height);
					}
				}
			}

			// print all weapons on the ground
			if (currentMap.getWeapons() != null) {
				for (Weapon w : currentMap.getWeapons()) {
					// find inscreen location based on game coords
					int itemScreenX = (w.getGamePos().x - suki.getGamePos().x) + (screenWidth / 2);
					int itemScreenY = (w.getGamePos().y - suki.getGamePos().y) + (screenHeight / 2);

					g.drawImage(w.getImage(), itemScreenX, itemScreenY, w.getImage().getWidth(),
							w.getImage().getHeight(), this);
				}
			}

			// print all collectible items
			if (currentMap.getItems() != null) {
				for (Collectible c : currentMap.getItems()) {
					// find screen coords based on game coords
					int itemScreenX = (c.getGamePos().x - suki.getGamePos().x) + (screenWidth / 2);
					int itemScreenY = (c.getGamePos().y - suki.getGamePos().y) + (screenHeight / 2);

					g.drawImage(c.getImage(), itemScreenX, itemScreenY, c.getImage().getWidth(),
							c.getImage().getHeight(), this);
				}
			}

			double distance;
			// print all npcs
			if (currentMap.getNPCs() != null) {
				for (NPC npc : currentMap.getNPCs()) {
					g2.setFont(new Font("Dialog", Font.PLAIN, 16));
					distance = getNPCDistance(suki, npc);
					// if close to npc, prompt user to speak to them
					if (distance < 75) {
						g.setColor(Color.WHITE);
						g.fillRect(50, 550, 400, 100);
						g.setColor(Color.BLACK);
						g.drawString("Press SPACE to interact", 60, 580);
					}
					// render dialogue box if dialogue is active
					if (dialogueActive) {
						// draw dialogue box
						g2.setColor(Color.WHITE);
						g2.fillRect(50, 550, 600, 100);
						g2.setColor(Color.BLACK);
						g2.drawRect(50, 550, 600, 100);

						// draw text inside the box
						if (currentDialogue != null) {
							String[] lines = currentDialogue.split("\n");
							int yPosition = 580;
							for (String line : lines) {
								g2.drawString(line, 60, yPosition);
								yPosition += 20; // spacing between lines
							}
						}

						// hide dialogue box after some time passes
						if (System.currentTimeMillis() > dialogueEndTime) {
							dialogueActive = false;
							currentDialogue = null;
						}
					}
				}
			}

			// display inventory if user opens inventory
			if (showInventory) {
				int boxSize = 50; // size of each inventory box
				int padding = 10; // padding between boxes
				int startX = 60; // starting X position for the grid
				int startY = 100; // starting Y position for the grid
				int cols = 6; // number of columns in the grid

				// displaying the box
				g.setColor(Color.WHITE);
				g.fillRect(50, 50, 400, 400);
				g.setColor(Color.BLACK);
				g.drawString("Inventory", 60, 70);
				g.drawString("Press E to close", 60, 90);

				// draw every item
				for (int i = 0; i < suki.getInventory().size(); i++) {
					Item item = suki.getInventory().get(i);
					int row = i / cols;
					int col = i % cols;
					int x = startX + col * (boxSize + padding);
					int y = startY + row * (boxSize + padding);

					// draw item box
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(x, y, boxSize, boxSize);
					g.setColor(Color.BLACK);
					g.drawRect(x, y, boxSize, boxSize);

					// draw item image
					BufferedImage image = item.getImage();
					if (image != null) {
						g.drawImage(image, x + (boxSize - image.getWidth()) / 2, y + (boxSize - image.getHeight()) / 2,
								null);
					}

				}

				// move boxes (dialogue for av guy in balcony)
				if (!boxesMoved && showBoxMessage) {

					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"There are too many boxes, I can't move all of them myself.\"", 60, 580);
				} else if (!boxesMoved && currentMap == maps.get(2)) {
					int boxScreenX = (4520 - suki.getGamePos().x) + (screenWidth / 2);
					int boxScreenY = (1975 - suki.getGamePos().y) + (screenHeight / 2);
					g.drawImage(boxPileImage, boxScreenX, boxScreenY, boxPileImage.getWidth(), boxPileImage.getHeight(),
							this);
				}

				// display details of item if hovering over it
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
								// change box based on text width and height
								int textWidth = Math.max(metrics.stringWidth(itemName),
										metrics.stringWidth(itemDescription));
								int textHeight = metrics.getHeight() * 2;

								// draw background box
								g.setColor(new Color(255, 255, 255, 200));
								g.fillRect(mousePos.x, mousePos.y - textHeight, textWidth + 10, textHeight + 10);

								// draw item name and description
								g.setColor(Color.BLACK);
								Font originalFont = g.getFont();
								Font boldFont = originalFont.deriveFont(Font.BOLD);
								g.setFont(boldFont);
								g.drawString(itemName, mousePos.x + 5, mousePos.y - textHeight + metrics.getHeight());
								g.setFont(originalFont);
								g.drawString(itemDescription, mousePos.x + 5,
										mousePos.y - textHeight + metrics.getHeight() * 2);

								// if weapon... add damage and reach stats
								if (item instanceof Weapon) {
									Weapon weapon = (Weapon) item; // cast the item to Weapon
									String damage = "Damage: " + weapon.getDamage();
									String reach = "Reach: " + weapon.getReach();

									// display weapon stats
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

			// prompt suki to interact with door if its in range
			for (Door door : currentMap.getDoors()) {
				if (door.interactable()) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("Press C to interact", 60, 580);
				}
			}

			// STAGE 1
			// BEAKER
			if (beakerCleaned && showBeakerMessage) {
				if (beakerWalkCount == 1) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					if (beakerWalkCount == 1) {
						g.drawString("Sweeping. . .", 60, 580);
						// this doesn't work lol
						javax.swing.Timer timer = new javax.swing.Timer(3000, new ActionListener() {
							// if action happens... beaker is cleaned!
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
				// if no broom, tell user you need a broom
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("\"If only I had a broom...\"", 60, 580);
			}

			// if no broom or beaker, spill will tell you to go get them
			if (!spillCleaned && showSpillMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("\"Looks like I need a broom and a container...\"", 60, 580);
			}

			// STAGE 2
			// WRITE MRS. KIM'S ESSAY
			if (!essayWritten && showEssayMessage && !essaySubmitted) {
				int index = suki.searchInventory("Sharp Pencil");
				// check if pencil exists in inventory
				if (index < 0) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"Dang... I need to find a pencil.\"", 60, 580);
				} else {
					// doesn't show because suki will instantly start writing if she walks over the
					// paper
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"I should write my essay.\"", 60, 580);
				}
			} else if (essayWritten && !essaySubmitted) {
				// when essay is done, prompt player to give ms kim the essay
				if (currentMap == maps.get(310)) {
					g.setColor(Color.WHITE);
					g.fillRect(50, 550, 400, 100);
					g.setColor(Color.BLACK);
					g.drawString("\"Wow! Mrs. Kim is going to love this one!\"", 60, 580);
				}
			}

			// IF cat steals the chips, mr lee gets MAD LOL
			if (chipsStolen) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Mr. Lee: Hey kid! That's not for you! >:(", 60, 580);
			}

			// (later accessible) locked doors will show a message
			if (showLockedMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("This door is locked...?", 60, 580);
			}

			// balcony is locked, show message (person in the balcony talking to you)
			if (showLockedBalconyMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("???: Help! I'm locked in here!", 60, 580);
			}

			// staff lounge locked, show message
			if (showLockedLoungeMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Only teachers have the key to this room...", 60, 580);
			}

			// FLOOR 2 stair well is locked!
			if (showStairwellLockedMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("This door is locked, but it looks a little beat up...", 60, 580);
			}

			// vent message because vent needs screwdriver
			if (showVentMessage) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Looks like I need to unscrew this to get through.", 60, 580);
			}

			// BOSS FIGHT
			if (currentMap == maps.get(11)) {
				// show projectiles
				HashSet<Projectile> currentProjectiles = new HashSet<>(projectiles);
				for (Projectile p : currentProjectiles) {
					g.setColor(Color.YELLOW);
					g.fillOval((int) (p.getX() - suki.getGamePos().x) + (screenWidth / 2),
							(int) (p.getY() - suki.getGamePos().y) + (screenHeight / 2), Projectile.getSize(),
							Projectile.getSize());
				}
				// if boss is alive, show boss health bar
				Iterator<Cow> cowIterator = currentMap.getCows().iterator();
				while (cowIterator.hasNext()) {
					Cow cow = cowIterator.next();
					if (cow.getHP() > 0) {
						// draw health bar
						int barWidth = (int) (cow.getHP() / 2.5); // hp point is 1/3 pixels
						int barHeight = 30;
						int barX = screenWidth / 2 - barWidth / 2;
						int barY = 80;
						g3.setColor(Color.RED);
						g3.fillRect(barX, barY, barWidth, barHeight);
						g.drawImage(bossHealthBar, screenWidth / 2 - bossHealthBar.getWidth() / 2, 15,
								bossHealthBar.getWidth(), bossHealthBar.getHeight(), this);
					}
				}
			}
			// CHEATS FOR MS WONG:
			// tells user when they are healing
			if (healing && !fullHP) {
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Healing...", 60, 580);
				System.out.println(suki.getHP());
			} else if (healing && fullHP) {
				// tell user if suki is at full health
				g.setColor(Color.WHITE);
				g.fillRect(50, 550, 400, 100);
				g.setColor(Color.BLACK);
				g.drawString("Suki is hydrated.", 60, 580);
			}

			g.setColor(Color.WHITE);
			g.setFont(new Font("Dialog", Font.PLAIN, 24));
			g.drawString(minutes + "m" + seconds + "s", 910, 50);

			// draw border walls of each map
			// COMMENT OUT WHEN DONE DEBUGGING (keep for ms wong to see)
			if (currentMap != null) {
				g.setColor(Color.RED);
				for (Wall wall : currentMap.getRectWalls()) {
					int wallScreenX = (wall.getRect().x - suki.getGamePos().x - 24) + (screenWidth / 2);
					int wallScreenY = (wall.getRect().y - suki.getGamePos().y + 32) + (screenHeight / 2);
					g.drawRect(wallScreenX, wallScreenY, wall.getRect().width, wall.getRect().height);
				}
			}

			// HEALTH BAR ON ALL MAPS:
			int barWidth = suki.getHP() * 4; // hp point is 4 pixels
			int barHeight = 20;
			int barX = screenWidth - 445;
			int barY = screenHeight - barHeight - 45;
			g3.setColor(Color.RED);
			g3.fillRect(barX, barY, barWidth, barHeight);
			g.drawImage(healthBar, screenWidth - 460, screenHeight - 130, healthBar.getWidth(), healthBar.getHeight(),
					this);
			// draw equipped item icon if it exists
			int equippedIndex = suki.getEquippedItem();
			if (equippedIndex >= 0 && equippedIndex < suki.getInventory().size()) {
				Item equippedItem = suki.getInventory().get(equippedIndex);
				BufferedImage equippedItemImage = equippedItem.getImage();

				if (equippedItemImage != null) {
					int itemX = screenWidth - 460 - equippedItemImage.getWidth() - 40; // left of health bar
					int itemY = screenHeight - 130 + 20;
					g.drawImage(equippedItemImage, itemX, itemY, equippedItemImage.getWidth() + 20,
							equippedItemImage.getHeight() + 20, this);
				}
			}

			g.drawImage(options, 1010, 20, options.getWidth() / 2, options.getHeight() / 2, this);
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

	// Description: draw all walls based on where it is on the map
	// WE USED HEADSET MS WONG!!
	// Parameters: graphics (for painting), player's game coordinates
	// Return: void
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

		if (suki.getCurrentSprite() != null) {
			g.drawImage(suki.getCurrentSprite(),
					(screenWidth - suki.getCurrentSprite().getWidth()) / 2,
					(screenHeight - suki.getCurrentSprite().getHeight()) / 2,
					this);
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

	// must have because we are implemeting an interface
	@Override
	public void keyTyped(KeyEvent e) {

	}

	// Description: detects when key is pressed
	// Parameters: KeyEvent (the key you pressed)
	// Return: void
	@Override
	public void keyPressed(KeyEvent e) {

		// game screen
		if (screen == 5) {
			int key = e.getKeyCode();
			// detect which direction player wants suki to walk and sets direction
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
			}
			// toggle inventory button is E
			else if (key == KeyEvent.VK_E) {
				if (showInventory == true) {
					showInventory = false;
				} else {
					showInventory = true;
				}
			}
			// interaction with NPCS
			if (key == KeyEvent.VK_SPACE) {
				if (currentMap.getNPCs() != null) {
					// check each npc
					for (NPC npc : currentMap.getNPCs()) {
						if (npc.interactable()) {
							try {
								if (sound.isRunning()) {
									sound.stop();
								}
								if (!sound.isRunning()) {
									talkSound = true;
									soundPlayer();
								}
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
								e1.printStackTrace();
							}
							// STAGE 2 --> mrs. kim foolscap
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

							if (npc.getState() < npc.getDialogues().length) {
								// mr lee
								if (currentMap == maps.get(341) && npc == currentMap.getNPCs().get(0)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									int index = suki.searchInventory("Meter Stick");
									if (index < 0) {
										npc.setState(0);
										npc.setCurrentDialogueIndex(0);
									} else {
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}

								// mr lien needs chips (stage3)
								if (currentMap == maps.get(345) && npc == currentMap.getNPCs().get(1)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									int index = suki.searchInventory("Lays Classic Chips");
									if (index >= 0 && chipCount == 0) {
										updateEquippedItemIndexBeforeChange();
										suki.removeFromInventory(index);
										chipCount++;
										suki.addToInventory(new Collectible("Math Storage Key",
												"Key to the Math Department Printing Room", keyImage,
												new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										try {
											itemSound = true;
											soundPlayer();
										} catch (UnsupportedAudioFileException | IOException
												| LineUnavailableException e1) {
											e1.printStackTrace();
										}
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								// ms wong gives you tissues
								if (currentMap == maps.get(224) && npc == currentMap.getNPCs().get(0)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									int index = suki.searchInventory("Tissue Box");
									if (index < 0 && tissueCount == 0 && npc.getCurrentDialogueIndex() == 2) {
										tissueCount++;
										updateEquippedItemIndexBeforeChange();
										suki.addToInventory(new Collectible("Tissue Box",
												"A box of tissues Sophie & Claire delivered to Ms. Wong", tissueImage,
												new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										try {
											itemSound = true;
											soundPlayer();
										} catch (UnsupportedAudioFileException | IOException
												| LineUnavailableException e1) {
											e1.printStackTrace();
										}
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								// mr chow gives you keys
								if (currentMap == maps.get(225) && npc == currentMap.getNPCs().get(0)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									int index = suki.searchInventory("Tissue Box");
									if (index >= 0 && chowCount == 0) {
										updateEquippedItemIndexBeforeChange();
										suki.removeFromInventory(index);
										chowCount++;
										suki.addToInventory(
												new Collectible("Staff Lounge Key", "Key to the Staff Lounge", keyImage,
														new Point(0, 0), 0));
										updateEquippedItemIndexAfterChange();
										try {
											itemSound = true;
											soundPlayer();
										} catch (UnsupportedAudioFileException | IOException
												| LineUnavailableException e1) {
											e1.printStackTrace();
										}
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								// av guy moves boxes
								if (currentMap == maps.get(21) && npc == currentMap.getNPCs().get(0)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									if (!avTalkedTo) {
										npc.setState(0);
										npc.setCurrentDialogueIndex(0);
										avTalkedTo = true;
									} else if (avTalkedTo && boxesMoved) {
										npc.setState(1);
										npc.setCurrentDialogueIndex(0);
									}
								}
								// mr j cheung asks you to mop the washrooms
								if (currentMap == maps.get(24)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									if (!mopped && npc.getState() == 0 && mopCount == 0) {
										mopCount++;
										updateEquippedItemIndexBeforeChange();
										suki.addToInventory(new Weapon("Mop", "A Mop for Mopping.", 60, 100, mopImage,
												new Point(0, 0)));
										updateEquippedItemIndexAfterChange();
										try {
											itemSound = true;
											soundPlayer();
										} catch (UnsupportedAudioFileException | IOException
												| LineUnavailableException e1) {
											e1.printStackTrace();
										}
									}
									// he hands you a weight to smash the stairwell with
									else if (mopped) {
										npc.setState(1);
										if (weightCount == 0) {
											weightCount++;
											updateEquippedItemIndexBeforeChange();
											suki.addToInventory(
													new Weapon("10lb Weight", "For gettin' those gainz!", 40,
															120, weightImage,
															new Point(0, 0)));
											updateEquippedItemIndexAfterChange();
											try {
												itemSound = true;
												soundPlayer();
											} catch (UnsupportedAudioFileException | IOException
													| LineUnavailableException e1) {
												e1.printStackTrace();
											}
										}
									}
								}

								// WIN CONDITION
								// if you have the ID card, mr moo will let you leave the school
								if (currentMap == maps.get(1)) {
									try {
										if (sound.isRunning()) {
											sound.stop();
										}
										if (!sound.isRunning()) {
											talkSound = true;
											soundPlayer();
										}
									} catch (UnsupportedAudioFileException | IOException
											| LineUnavailableException e1) {
										e1.printStackTrace();
									}
									int index = suki.searchInventory("ID Card");
									if (index >= 1) {
										if (mooCount == 0) {
											mooCount++;
											npc.setCurrentDialogueIndex(0);
											npc.setState(1);
											try {
												winSound = true;
												soundPlayer();
											} catch (UnsupportedAudioFileException | IOException
													| LineUnavailableException e1) {
												e1.printStackTrace();
											}
											if (npc.getCurrentDialogueIndex() == 1) {
												Timer timer = new Timer(2000, new ActionListener() {
													@Override
													public void actionPerformed(ActionEvent e) {
														screen = 4;
														try {
															music.stop();
															musicPlayer();
														} catch (UnsupportedAudioFileException | IOException
																| LineUnavailableException e1) {
															e1.printStackTrace();
														}
														String score = minutes + "m " + seconds + "s";
														String gameName = JOptionPane.showInputDialog(frame,
																"SCORE: " + score + "\nPlease enter a game name: ",
																"GAME OVER... YOU WIN!!!", JOptionPane.PLAIN_MESSAGE);
														gameTimer.stop();
														int totTime = minutes * 60 + seconds;
														arrTopTime[5] = totTime;
														arrTopName[5] = gameName;
														for (int j = arrTopTime.length - 2; j >= 0; j--) {
															for (int k = 0; k <= j; k++) {
																if (arrTopTime[k + 1] < arrTopTime[k]) {
																	int tmp = arrTopTime[k];
																	arrTopTime[k] = arrTopTime[k + 1];
																	arrTopTime[k + 1] = tmp;
																	String nameTmp = arrTopName[k];
																	arrTopName[k] = arrTopName[k + 1];
																	arrTopName[k + 1] = nameTmp;
																}
															}
														}
														// saves new top leaderboard names and times to a file
														try {
															PrintWriter outFile;
															outFile = new PrintWriter(new FileWriter(
																	"ICS4U1 Culminating\\src\\leaderboard.txt"));
															for (int j = 0; j < 5; j++) {
																outFile.println(arrTopName[j] + '\n' + arrTopTime[j]);
															}
															outFile.close();
															screen = 7;
															try {
																music.stop();
																musicPlayer();
															} catch (UnsupportedAudioFileException
																	| LineUnavailableException e1) {
																e1.printStackTrace();
															}
															myPanel.repaint();
														} catch (IOException e1) {
															e1.printStackTrace();
														}
													}
												});
												timer.setRepeats(false);
												timer.start();
											}
										}
									}
								}

								// get the current line of dialogue
								String currentDialogueLine = npc.getDialogues()[npc.getState()][npc
										.getCurrentDialogueIndex()];

								// add npc name to dialogue
								currentDialogue = npc.getName() + ": " + currentDialogueLine;

								// check if there are more lines in the current dialogue set
								if (npc.getCurrentDialogueIndex() < npc.getDialogues()[npc.getState()].length - 1) {
									currentDialogue += "\n\nPress SPACE to interact."; // Prompt for next line
								}
								// interact with NPC and get the current dialogue
								npc.interact();
							} else {
								// if all dialogues are finished, display "We've already spoken."
								// THIS NEVER HAPPENS IN THE GAME BECAUSE THE DIALOGUE LOOPS
								currentDialogue = "We've already spoken.";
							}

							// start dialogue and set the timer
							dialogueActive = true;
							dialogueEndTime = System.currentTimeMillis() + 4000; // Reset 4-second timer

							break; // interact with only one NPC at a time
						}
					}
				}
			}

			// INTERACTION WITH DOORS
			if (key == KeyEvent.VK_C) {
				for (Door door : currentMap.getDoors()) {
					// VENTS - transition to second floor
					if (door.interactable() && door.getMapDest() == 224 && currentMap == maps.get(34)) {
						try {
							ventSound = true;
							soundPlayer();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
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
					}
					// STAIRWELL - transition to first floor
					else if (door.interactable() && door.getMapDest() == 1 && currentMap == maps.get(2)) {
						try {
							doorSound = true;
							soundPlayer();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
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
					// if normal door is interactable, go to destination / change maps
					if (door.interactable()) {
						try {
							doorSound = true;
							soundPlayer();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
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
			// press F to mop
			if ((currentMap == maps.get(23) || currentMap == maps.get(22)) && key == KeyEvent.VK_F) {
				if (moppable) {
					showMopMessage = false;
					mopped = true;
				}
			}
		}
	}

	// Description: detects key releases
	// Parameters: key release event
	// Return: void
	@Override
	public void keyReleased(KeyEvent e) {
		// game screen
		if (screen == 5) {
			// stop movement
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

			// retain direction
			if (key == KeyEvent.VK_W || key == KeyEvent.VK_S || key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
				suki.setDirection(suki.getCurrentDirection());
			}
		}
	}

	// Description: allows suki to move
	// Parameters: n/a
	// Return: void
	public void move() {
		// game screen
		if (screen == 5) {
			int moveX = 0, moveY = 0;
			// diagonal movement speed adjustment
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

			// create a future hitbox for suki to detect collisions (i dont think this is
			// necessary for final ver of game)
			Rectangle futureHitbox = new Rectangle(suki.getHitboxM());
			futureHitbox.translate(moveX, moveY);

			suki.move(moveX, moveY);
			suki.getHitboxC().x = (screenWidth / 2) - (suki.getHitboxC().width / 2);
			suki.getHitboxC().y = (screenHeight / 2) - (suki.getHitboxC().height / 2);
			currentMap.setTLLocation(
					new Point(currentMap.getTLLocation().x - moveX, currentMap.getTLLocation().y - moveY));
		}
	}

	// Description: attack cows
	// Parameters: n/a
	// Return: void
	public void attack() {
		int index = suki.getEquippedItem();
		if (index < 0 || index >= suki.getInventory().size()) {
			System.out.println("one punch man");
			int damage = 10;
			int reach = 30;

			// check cows to attack
			Iterator<Cow> cowIterator = currentMap.getCows().iterator();
			while (cowIterator.hasNext()) {
				Cow cow = cowIterator.next();
				int x = cow.getX() + cow.image.getWidth();
				int y = cow.getY() + cow.image.getHeight();
				Point pos = new Point(x, y);

				double distance = suki.getGamePos().distance(pos);
				if (distance <= reach) {
					System.out.println("Hit cow at distance: " + distance);

					// damage cow
					cow.hurt(damage);
					try {
						cowSound = true;
						soundPlayer();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
						e1.printStackTrace();
					}

					// apply knockback if not boss battle
					if (currentMap != maps.get(11)) {

						knockback(cow, suki, 50);
					}

					if (!cow.isAlive()) {
						System.out.println("Cow DIE!");
						// if the boss is dead, set bossDefeated to true and give the player the ID card
						if (currentMap == maps.get(11)) {
							bossDefeated = true;
							suki.addToInventory(
									new Collectible("ID Card", "Waste Cow's ID Card", IDcardImage, new Point(0, 0), 0));
						}
						currentMap.removeCow(cow); // remove dead cow from the map
					}
				}
			}
			return;
		}

		// if not empty handed
		Item equippedItem = suki.getInventory().get(suki.getEquippedItem());
		int damage = 0;
		int reach = 0;
		if (equippedItem instanceof Weapon) {
			Weapon weapon = (Weapon) equippedItem; // cast to Weapon
			System.out.println("Attacking with weapon: " + weapon.getName());
			damage = weapon.getDamage();
			reach = weapon.getReach();
		} else if (equippedItem == null) {
			System.out.println("No item equipped! punching... ");
			damage = 10;
			reach = 30;
		} else {
			System.out.println("equipped item is not a weapon! cannot attack.");
			return; // cancel attack
		}

		// check for cows within range
		Iterator<Cow> cowIterator = currentMap.getCows().iterator();
		while (cowIterator.hasNext() && currentMap.getCows().size() > 0) {
			Cow cow = cowIterator.next();

			double distance = suki.getGamePos().distance(cow.getGamePos());
			if (distance <= reach) {
				System.out.println("hit cow at distance: " + distance);

				// damage cow
				cow.hurt(damage);

				// Aaply knockback
				knockback(cow, suki, 50);

				if (!cow.isAlive()) {
					System.out.println("Cow DIE!");
					cowIterator.remove(); // remove dead cow from the map
				}
			}
		}
	}

	private void knockback(Cow cow, Player player, int knockbackDistance) {
		Point cowPos = cow.getGamePos();
		Point playerPos = player.getGamePos();

		// calculate knockback direction
		double dx = cowPos.x - playerPos.x;
		double dy = cowPos.y - playerPos.y;
		double length = Math.sqrt(dx * dx + dy * dy);

		// find direction and apply knockback
		dx = (dx / length) * knockbackDistance;
		dy = (dy / length) * knockbackDistance;

		cow.setGamePos(cowPos.x + (int) dx, cowPos.y + (int) dy);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (screen == 5) {
			if (!showInventory) {
				try {
					swingSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
				attack();
			}
			if (showInventory) {
				// same info as in paint component for inventory
				Point mousePos = e.getPoint();
				int boxSize = 50;
				int padding = 10;
				int startX = 60;
				int startY = 100;
				int cols = 6;

				// detect where mouse is and equip clicked item
				for (int i = 0; i < suki.getInventory().size(); i++) {
					int row = i / cols;
					int col = i % cols;
					int x = startX + col * (boxSize + padding);
					int y = startY + row * (boxSize + padding);

					Rectangle itemRect = new Rectangle(x, y, boxSize, boxSize);
					if (itemRect.contains(mousePos)) {
						try {
							clickSound = true;
							soundPlayer();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
						suki.setEquippedItem(i);
						showInventory = false;
						System.out.println(i + " equipped.");
						break;
					}
				}
			}
		}
	}

	// Description: mouse movement (for changing images when hovering over them)
	// Parameters: mouse point from mouse event
	// Return: void
	@Override
	public void mouseMoved(MouseEvent e) {
		Point mousePoint = e.getPoint();
		// update hover boolean for buttons on the current screen
		hoverPlay = recPlay.contains(mousePoint);
		hoverAbout = recAbout.contains(mousePoint);
		hoverNewGame = recNewGame.contains(mousePoint);
		hoverLB = recLB.contains(mousePoint);
		hoverBack = recBack.contains(mousePoint);
		repaint();
	}

	/*
	 * screen 0 = main menu
	 * screen 1 = start menu
	 * screen 2 = about menu
	 * screen 3 = settings menu
	 * screen 4 = leaderboard
	 * screen 5 = game
	 */

	// Description: BUTTONS FOR MENUS
	// Parameters: mouse things... where the click is
	// Return: void
	@Override
	public void mousePressed(MouseEvent e) {
		Point selectedPoint = new Point(e.getX(), e.getY());
		// main screen --> play, about
		if (screen == 0) {

			System.out.println("clicked" + e.getX() + " " + e.getY());
			if (recPlay.contains(selectedPoint)) {
				screen = 1;
				myPanel.repaint();
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}

			} else if (recAbout.contains(selectedPoint)) {
				screen = 2;
				myPanel.repaint();
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		}

		// start menu
		// starts a new game, shows leaderboard, or returns to main menu

		else if (screen == 1) {
			if (recNewGame.contains(selectedPoint)) {
				gameTimer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						seconds++;
						if (seconds == 60) {
							seconds = 0;
							minutes++;
						}
					}
				});
				gameTimer.start();
				try {
					resetVariables();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				screen = 5;
				try {
					clickSound = true;
					soundPlayer();
					music.stop();
					musicPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
			if (recLB.contains(selectedPoint)) {
				screen = 4;
				myPanel.repaint();
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}

			} else if (recBack.contains(selectedPoint)) {
				screen = 0;
				myPanel.repaint();
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		}
		// about menu --> back
		else if (screen == 2) {
			if (recBack.contains(selectedPoint)) {
				screen = 0;
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}

		}
		// settings menu --> quit game, back
		else if (screen == 3) {
			if (recBack.contains(selectedPoint)) {
				screen = 5;
				try {
					clickSound = true;
					soundPlayer();
					music.stop();
					musicPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
			if (recQuitGame.contains(selectedPoint)) {
				screen = 0;
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}

		}
		// leaderboard --> back
		else if (screen == 4) {
			if (recBack.contains(selectedPoint)) {
				screen = 1;
				try {
					clickSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		}
		// main game --> options
		else if (screen == 5) {
			if (recOptions.contains(selectedPoint)) {
				screen = 3;
				try {
					clickSound = true;
					soundPlayer();
					music.stop();
					musicPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// needed for implementing interface
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	// needed for implementing interface
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	// needed for implementing interface
	@Override
	public void mouseExited(MouseEvent e) {

	}

	// needed for implementing interface
	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	// Description: remember equipped item before inventory gets sorted
	// Parameters: n/a
	// Return: void
	public void updateEquippedItemIndexBeforeChange() {
		// get the name of the currently equipped item before any inventory changes
		int equippedIndex = suki.getEquippedItem();
		Item equippedItem = (equippedIndex >= 0 && equippedIndex < suki.getInventory().size())
				? suki.getInventory().get(equippedIndex)
				: null;

		if (equippedItem != null) {
			suki.setEquippedItemName(equippedItem.getName()); // store the item's name
		} else {
			suki.setEquippedItemName(null); // clear the name if no item is equipped
		}
	}

	// Description: change equipped item back to what it was after inventory gets
	// sorted
	// Parameters: n/a
	// Return: void
	public void updateEquippedItemIndexAfterChange() {
		// get the stored name of the previously equipped item
		String equippedItemName = suki.getEquippedItemName();

		if (equippedItemName != null) {
			// try to find the item's new index in the inventory
			int newIndex = suki.searchInventory(equippedItemName);
			if (newIndex >= 0) {
				suki.setEquippedItem(newIndex); // update the equipped index to the new position
				return;
			}
		}

		// if the item is no longer in the inventory, equip the first item or unequip
		if (!suki.getInventory().isEmpty()) {
			suki.setEquippedItem(0);
		} else {
			suki.setEquippedItem(-1); // no items equipped
		}
	}

	// Description: check if suki is colliding with a door
	// Parameter: door that could be collided into
	// Return: void
	public void checkCollision(Door door) {

		// in game hitboxes
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		Rectangle doorRect = door.getDoorRect();

		// if intersects...
		if (playerRect.intersects(doorRect)) {
			// check conditions for each door
			// math storage is locked
			if (door.getMapDest() == 33) {
				int index = suki.searchInventory("Math Storage Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedMessage = false;
				} else {
					door.setInteractable(false);
					showLockedMessage = true;
				}
			}
			// vent needs screwdriver
			else if (door.getMapDest() == 224 && currentMap == maps.get(34)) {
				int equippedIndex = suki.getEquippedItem();
				if (equippedIndex < 0 || !suki.getInventory().get(equippedIndex).getName().equals("Screwdriver")) {
					door.setInteractable(false);
					showVentMessage = true;
				} else {
					door.setInteractable(true);
					showVentMessage = false;
				}
			}
			// stairwell needs dumbbell
			else if (door.getMapDest() == 1 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("10lb Weight");
				if (suki.getEquippedItem() == index) {
					door.setInteractable(true);
					showStairwellLockedMessage = false;
				} else {
					door.setInteractable(false);
					showStairwellLockedMessage = true;
				}
			}
			// staff lounge is locked
			else if (door.getMapDest() == 20 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("Staff Lounge Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedLoungeMessage = false;
				} else {
					door.setInteractable(false);
					showLockedLoungeMessage = true;
				}
			}
			// balcony is locked
			else if (door.getMapDest() == 21 && currentMap == maps.get(2)) {
				int index = suki.searchInventory("Balcony Key");
				if (index >= 0) {
					door.setInteractable(true);
					showLockedBalconyMessage = false;
				} else {
					door.setInteractable(false);
					showLockedBalconyMessage = true;
				}
			}
			// washrooms are inaccessible until mop is acquired
			else if (door.getMapDest() == 22 || door.getMapDest() == 23) {
				int index = suki.searchInventory("Mop");
				if (index >= 0) {
					door.setInteractable(true);
					showWaterMessage = false;
				} else {
					door.setInteractable(false);
					showWaterMessage = true;
				}
			} else {
				// default behavior for unlocked doors
				door.setInteractable(true);
				showLockedLoungeMessage = false;
				showBoxMessage = false;
				showStairwellLockedMessage = false;
			}
		} else {
			// reset only the message for the current door if the player walks away
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
			// disable door interaction when not colliding
			door.setInteractable(false);
		}
	}

	// Description: check player collision with item
	// Parameters: player suki
	// Return: void
	public void checkPlayerCollisions(Player player) throws IOException {
		// check collectibles
		Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		Iterator<Collectible> collectibleIterator = currentMap.getItems().iterator();
		// check all using iterator
		while (collectibleIterator.hasNext()) {
			Collectible collectible = collectibleIterator.next();
			Rectangle itemRect = new Rectangle(collectible.getGamePos().x, collectible.getGamePos().y,
					collectible.getImage().getWidth(), collectible.getImage().getHeight());
			if (playerRect.intersects(itemRect)) {
				// specific rooms:
				// broom for beaker
				if (currentMap == maps.get(328)) {
					int index = suki.searchInventory("Broom");
					if (index >= 0) {
						beakerCleaned = true;
						currentMap.getNPCs().get(0).setState(1);
						currentMap.getNPCs().get(0).setCurrentDialogueIndex(0);
						collectible.setImage(ImageIO.read(getClass().getResource("/sprites/beakerEmpty.png")));
						Collectible beaker = collectible;
						System.out.println("Picked up collectible: " + collectible.getName());
						updateEquippedItemIndexBeforeChange();
						player.addToInventory(beaker);
						updateEquippedItemIndexAfterChange();
						try {
							itemSound = true;
							soundPlayer();
						} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
						collectibleIterator.remove();
						beakerWalkCount++;
					} else {
						showBeakerMessage = true;
					}
				}
				// PENCIL ESSAY WRITING:
				else if (currentMap == maps.get(310)) {
					int index = suki.searchInventory("Sharp Pencil");
					if (index >= 0) {
						JTextArea textArea = new JTextArea(collectible.getDescription());
						textArea.setEditable(true);
						JScrollPane scrollPane = new JScrollPane(textArea);
						JOptionPane.showMessageDialog(null, scrollPane, "My Synthesis Essay",
								JOptionPane.INFORMATION_MESSAGE);
						String input = textArea.getText();
						// ensure essay is not empty
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
							try {
								itemSound = true;
								soundPlayer();
							} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
								e1.printStackTrace();
							}
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
				}
				// mr lee angry at suki stealingchips
				else if (currentMap == maps.get(341)) {
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
					collectibleIterator.remove(); // remove from map
					updateEquippedItemIndexBeforeChange();
					player.addToInventory(collectible); // add to player's inventory
					updateEquippedItemIndexAfterChange();
					try {
						itemSound = true;
						soundPlayer();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
						e1.printStackTrace();
					}
					System.out.println("Picked up collectible: " + collectible.getName());
				}
				// behaviour for non-special items:
				else {
					collectibleIterator.remove(); // remove from map
					updateEquippedItemIndexBeforeChange();
					player.addToInventory(collectible); // add to player's inventory
					updateEquippedItemIndexAfterChange();
					try {
						itemSound = true;
						soundPlayer();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
						e1.printStackTrace();
					}
					System.out.println("Picked up collectible: " + collectible.getName());
				}
			} else {
				showBeakerMessage = false; // disable the message when not near the beaker
				showEssayMessage = false; // disable essay message when not near the paper
			}
		}

		// check weapons
		Iterator<Weapon> weaponIterator = currentMap.getWeapons().iterator();
		while (weaponIterator.hasNext()) {
			Weapon weapon = weaponIterator.next();
			Rectangle itemRect = new Rectangle(weapon.getGamePos().x, weapon.getGamePos().y,
					weapon.getImage().getWidth(), weapon.getImage().getHeight());
			if (playerRect.intersects(itemRect)) {
				weaponIterator.remove(); // remove from map
				updateEquippedItemIndexBeforeChange();
				player.addToInventory(weapon); // add to player's inventory
				updateEquippedItemIndexAfterChange();
				try {
					itemSound = true;
					soundPlayer();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
				System.out.println("Picked up weapon: " + weapon.getName());
			}
		}
	}

	// Description: check suki's collisions with walls
	// Parameters: wall to be crashed into
	// Return: void
	public void checkCollision(Wall wall) {
		// game screen
		if (screen == 5) {
			Rectangle wallRect = new Rectangle(wall.getRect().x, wall.getRect().y, wall.getRect().width,
					wall.getRect().height);
			Rectangle playerRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y - 10,
					(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
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

	// Description: check suki's collisions with SPECIAL TRIANGULAR walls
	// Parameters: TRIANGULAR wall to be crashed into
	// Return: void
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

	// Description: handle when player collides with wall
	// Parameters: triangular wall, it's sides, it's points
	// Return: void
	public void handleTriCollision(Triangle wall, Line[] sides, Point[] vertices) {
		final double EPSILON = 1e-4; // small buffer to push the player out of the triangle more
		for (Point p : vertices) {
			if (wall.containsPoint(p)) {
				double minDist = Double.MAX_VALUE;
				int closestSideIndex = -1;

				// find closest side to player
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

					// normalize direction vector
					double length = Math.sqrt(dx * dx + dy * dy);
					dx /= length;
					dy /= length;

					// move slightly more than the minimum distance
					double pushDistance = minDist + EPSILON;

					// apply response to collision
					triCollisionResponse(dx * pushDistance, dy * pushDistance);
				}
			}
		}
	}

	// Description: adjust suki and map to not move when player hits wall
	// Parameters: distance to adjust suki
	// Return: void
	private void triCollisionResponse(double dx, double dy) {
		int roundedDx = (int) Math.round(dx);
		int roundedDy = (int) Math.round(dy);

		// adjust suki game coords
		suki.setGameX(suki.getGamePos().x - roundedDx);
		suki.setGameY(suki.getGamePos().y - roundedDy);

		// update the map's top-left location
		currentMap.setTLLocation(new Point(
				currentMap.getTLLocation().x + roundedDx,
				currentMap.getTLLocation().y + roundedDy));

		// adjust cow positions
		for (Cow cow : currentMap.getCows()) {
			cow.inScreenMove(roundedDx, roundedDy);
		}

	}

	// Description: stop cows from overlapping eachother
	// Parameters: cow...
	// Return: void
	public void checkCollision(Cow cow) {
		final int BUFFER = 1; // small buffer to prevent re-collision
		for (Cow cow2 : currentMap.getCows()) {
			if (cow != cow2 && cow.getHitbox().intersects(cow2.getHitbox())) {
				Rectangle intersection = cow.getHitbox().intersection(cow2.getHitbox());
				int dx = intersection.width / 2 + BUFFER;
				int dy = intersection.height / 2 + BUFFER;

				// collision along x-axis
				if (intersection.width > intersection.height) {
					if (cow.getX() < cow2.getX()) {
						cow.setGamePos(cow.getGamePos().x - dx, cow.getGamePos().y);
						cow2.setGamePos(cow2.getGamePos().x + dx, cow2.getGamePos().y);
					} else {
						cow.setGamePos(cow.getGamePos().x + dx, cow.getGamePos().y);
						cow2.setGamePos(cow2.getGamePos().x - dx, cow2.getGamePos().y);
					}
				}

				// collision along y-axis
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

		// check for potential hits on next move. only move if no collision
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

	public void checkProjectileCollisions() {

		// check for collision with walls
		Iterator<Projectile> it = projectiles.iterator();
		Rectangle sukiRect = new Rectangle(suki.getGamePos().x, suki.getGamePos().y,
				(int) suki.getHitboxM().getWidth(), (int) suki.getHitboxM().getHeight());
		while (it.hasNext()) {
			Projectile projectile = it.next();
			if (projectile.getRect().intersects((sukiRect))) {
				suki.takeDamage(10);
				it.remove();
			} else {
				for (Wall wall : currentMap.getRectWalls()) {
					if (projectile.getRect().intersects(wall.getRect())) {
						it.remove();
						continue;
					}
				}
				for (Triangle tri : currentMap.getTriWalls()) {
					if (tri.intersects(projectile.getRect()) > 0) {
						it.remove();
					}
				}
			}
		}
	}

	// Description: makes cows move
	// Parameters: n/a
	// Return: void
	public void moveCows() {
		for (Cow cow : currentMap.getCows()) {
			// check for collision
			checkCollision(cow);
			// move cows
			if (!cow.isColliding()) {
				cow.move(cow.getSpeed(), cow.getSpeed(), suki);
			}
		}
	}

	// Descritpion: spawn projectiles for the boss
	// Parameters: Cow object (boss)
	// Return: void
	public void spawnProjectiles(Cow cow) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastProjectileTime > FIRE_RATE) {
			lastProjectileTime = currentTime;
			for (int angle = 0; angle < 360; angle += 20) { // fires a projectile for every 20 degrees
				double radians = Math.toRadians(angle);
				double dx = Math.cos(radians);
				double dy = Math.sin(radians);
				projectiles.add(new Projectile(cow.getHitbox().x + 90, cow.getHitbox().y + 130, dx, dy));
			}
		}
	}

	// Description: moves the projectiles outwards
	// Parameters: n/a
	// Return: void
	public void updateProjectiles() {
		Iterator<Projectile> it = projectiles.iterator();
		while (it.hasNext()) {
			Projectile projectile = it.next();
			projectile.move();
		}
	}

	// Description: display game over panel when suki dies
	// Parameters: n/a
	// Return: void
	public void showGameOverPanel() {
		// create the gameover panel
		try {
			if (!sound.isRunning()) {
				loseSound = true;
				soundPlayer();
			}
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
		JPanel gameOverPanel = new JPanel();
		gameOverPanel.setLayout(new BorderLayout());
		gameOverPanel.setBackground(Color.BLACK);

		// add label
		JLabel gameOverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
		gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
		gameOverLabel.setForeground(Color.RED);
		gameOverPanel.add(gameOverLabel, BorderLayout.CENTER);

		// add quit button
		JButton returnButton = new JButton("Return to Home Screen");
		returnButton.setFont(new Font("Arial", Font.PLAIN, 18));
		returnButton.addActionListener(e -> {
			screen = 0; // set the screen to the home screen
			frame.getContentPane().remove(gameOverPanel); // remove the panel
			frame.getContentPane().add(this); // add the main game panel
			frame.revalidate();
			frame.repaint();
		});
		gameOverPanel.add(returnButton, BorderLayout.SOUTH);

		// display panel
		frame.getContentPane().remove(this); // remove game panel
		frame.getContentPane().add(gameOverPanel); // add this panel
		frame.revalidate();
		frame.repaint();
	}

	// MAIN
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		
		Scanner inFile = new Scanner(new File("ICS4U1 Culminating\\src\\leaderboard.txt"));
		int i = 0;
		while (inFile.hasNextLine()) {
			String name = inFile.nextLine();
			int score = Integer.parseInt(inFile.nextLine());

			arrTopName[i] = name;
			arrTopTime[i] = score;
			i++;
		}
		for (int j = 0; j < 5; j++) {
			if (arrTopName[j] == "") {
				arrTopTime[j] = 10000000;
			}
		}
		inFile.close();

		
		frame = new JFrame("ChaseCow");
		myPanel = new ChaseCow();
		frame.add(myPanel);
		// don't resize window.
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
